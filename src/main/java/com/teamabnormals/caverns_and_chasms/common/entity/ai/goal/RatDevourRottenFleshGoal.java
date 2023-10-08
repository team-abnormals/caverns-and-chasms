package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;
import java.util.Random;

public class RatDevourRottenFleshGoal extends MoveToBlockGoal {
	private final Rat rat;
	private int eatingTime;

	public RatDevourRottenFleshGoal(Rat rat, double speed, int length, int yMax) {
		super(rat, speed, length, yMax);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isTame()) {
			return false;
		} else if (this.rat.getTamer() == null) {
			return false;
		} else if (this.rat.isPassenger()) {
			return false;
		} else {
			return super.canUse();
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (this.eatingTime > 160) {
			return false;
		} else if (this.rat.isPassenger()) {
			return false;
		} else {
			return super.canContinueToUse();
		}
	}

	@Override
	public void start() {
		super.start();
		this.eatingTime = 0;
	}

	@Override
	public void stop() {
		super.stop();
		this.rat.setTamer(null);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		this.rat.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, this.blockPos.getY() + 0.5D, this.blockPos.getZ() + 0.5D, (float) (this.rat.getMaxHeadYRot() + 20), (float) this.rat.getMaxHeadXRot());

		RandomSource random = this.rat.getRandom();

		if (this.blockPos.closerToCenterThan(this.rat.position(), 1.5D) && this.eatingTime < 160) {
			++this.eatingTime;
			if (random.nextFloat() < 0.3F) {
				ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH);
				this.rat.playSound(this.rat.getEatingSound(flesh), 0.5F + 0.5F * (float)random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
				this.rat.spawnItemParticles(flesh, 4);
			}
		}

		if (this.eatingTime == 160) {
			this.eatingTime++;
			this.rat.playSound(SoundEvents.PLAYER_BURP, 0.5F, random.nextFloat() * 0.1F + 0.9F);
			this.rat.tame(this.rat.getTamer());
			if (!this.rat.level.isClientSide()) {
				for(int i = 0; i < 4; ++i) {
					double d0 = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;
					NetworkUtil.spawnParticle(ParticleTypes.HEART.writeToString(), this.rat.getRandomX(1.0D), this.rat.getRandomY() + 0.15D, this.rat.getRandomZ(1.0D), d0, d1, d2);
				}
			}
		}
	}

	@Override
	protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).is(CCBlocks.ROTTEN_FLESH_BLOCK.get());
	}
}
