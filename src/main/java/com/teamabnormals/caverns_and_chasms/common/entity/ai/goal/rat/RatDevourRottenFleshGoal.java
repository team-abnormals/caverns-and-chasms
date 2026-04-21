package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.EnumSet;

public class RatDevourRottenFleshGoal extends Goal {
	private final Rat rat;
	private final double speedModifier;
	private BlockPos targetPos;
	private int eatingTime;
	private int tryTicks;
	private int maxStayTicks;

	public RatDevourRottenFleshGoal(Rat rat, double speedModifier) {
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isSittingBecauseOrdered() || this.rat.isTame() || this.rat.isAngry() || this.rat.getTamer() == null || this.rat.isPassenger()) {
			return false;
		}

		this.targetPos = this.rat.getRottenFleshPos();
		if (this.targetPos != null) {
			if (this.targetPos.distToCenterSqr(this.rat.position()) <= 256.0D) {
				return true;
			} else {
				this.rat.setTamer(null);
			}
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.rat.isSittingBecauseOrdered() || this.eatingTime >= 80 || this.rat.isTame() || this.rat.isPassenger()) {
			return false;
		} else {
			return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && this.rat.level().getBlockState(this.targetPos).is(CCBlockTags.RAT_FOOD_BLOCKS);
		}
	}

	@Override
	public void start() {
		this.eatingTime = 0;
		this.tryTicks = 0;
		this.maxStayTicks = this.rat.getRandom().nextInt(this.rat.getRandom().nextInt(1200) + 1200) + 1200;
		this.moveMobToBlock();
	}

	@Override
	public void stop() {
		if (this.eatingTime >= 80) {
			RandomSource random = this.rat.getRandom();

			this.rat.playSound(CCSoundEvents.RAT_HAPPY.get(), 0.5F, random.nextFloat() * 0.1F + 0.9F);

			this.rat.tame(this.rat.getTamer());
			this.rat.setTarget(null);
			this.rat.setCommandedTarget(null);
			this.rat.setDirty(false);

			for (int i = 0; i < 4; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				NetworkUtil.spawnParticle(ParticleTypes.HEART.writeToString(), this.rat.getRandomX(1.0D), this.rat.getRandomY() + 0.15D, this.rat.getRandomZ(1.0D), d0, d1, d2);
			}

			if (this.rat.level().getEntitiesOfClass(Rat.class, this.rat.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), (entity) -> entity != this.rat && entity.getTamer() != null && entity.getRottenFleshPos() == this.rat.getRottenFleshPos()).isEmpty()) {
				this.rat.level().destroyBlock(this.targetPos, false);
				Block.popResource(this.rat.level(), this.targetPos, new ItemStack(CCItems.BONE_FLUTE.get()));
			}
		}
		this.rat.setTamer(null);
	}

	@Override
	public void tick() {
		double dist = this.targetPos.distToCenterSqr(this.rat.position());
		if (dist > 1.0D) {
			++this.tryTicks;
			if (this.tryTicks % 40 == 0) {
				this.moveMobToBlock();
			}
		} else {
			--this.tryTicks;
		}

		if (dist <= 2.25D) {
			RandomSource random = this.rat.getRandom();
			++this.eatingTime;
			if (random.nextFloat() < 0.3F) {
				ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH);
				this.rat.playSound(this.rat.getEatingSound(flesh), 0.5F + 0.5F * (float) random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
				this.rat.spawnItemParticles(flesh, 4);
			}
		}

		this.rat.getLookControl().setLookAt(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos.getZ() + 0.5D, (float) (this.rat.getMaxHeadYRot() + 20), (float) this.rat.getMaxHeadXRot());
	}

	private void moveMobToBlock() {
		this.rat.getNavigation().moveTo(this.targetPos.getX() + 0.5D, this.targetPos.getY(), this.targetPos.getZ() + 0.5D, this.speedModifier);
	}
}