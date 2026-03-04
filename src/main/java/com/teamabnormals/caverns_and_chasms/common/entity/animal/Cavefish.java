package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

public class Cavefish extends AbstractSchoolingFish {

	public Cavefish(EntityType<? extends Cavefish> p_30015_, Level p_30016_) {
		super(p_30015_, p_30016_);
	}

	public Cavefish(PlayMessages.SpawnEntity message, Level level) {
		this(CCEntityTypes.CAVEFISH.get(), level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
	}

	public ItemStack getBucketItemStack() {
		return new ItemStack(CCItems.CAVEFISH_BUCKET.get());
	}

	protected SoundEvent getAmbientSound() {
		return CCSoundEvents.CAVEFISH_AMBIENT.get();
	}

	protected SoundEvent getDeathSound() {
		return CCSoundEvents.CAVEFISH_DEATH.get();
	}

	protected SoundEvent getHurtSound(DamageSource p_30039_) {
		return CCSoundEvents.CAVEFISH_HURT.get();
	}

	@Override
	protected SoundEvent getFlopSound() {
		return CCSoundEvents.CAVEFISH_FLOP.get();
	}

	public int getMaxSchoolSize() {
		return 12;
	}

	public int getMaxSpawnClusterSize() {
		return 12;
	}

	public int getMinSchoolSize() {
		return 6;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.hasFollowers() && this.isFollower()) {
			if (!tryMergeSchools(this, this.leader)) {
				this.stopFollowing();
			}
		}

		if (!this.isSchoolFull() && this.random.nextInt(50) == 0) {
			List<? extends Cavefish> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
					cavefish -> cavefish != this &&
							((cavefish.canBeFollowed() && cavefish != this.leader) ||
									(!this.isFollower() && !this.hasFollowers() && cavefish.schoolSize < cavefish.getMaxSchoolSize() && !cavefish.isFollower())));

			for (Cavefish cavefish : list) {
				if ((this.hasFollowers() || this.isFollower()) && tryMergeSchools(this.isFollower() ? this.leader : this, cavefish)) {
					break;
				} else if (!this.isFollower() && !this.hasFollowers()) {
					this.startFollowing(cavefish);
					break;
				}

			}
		}
	}

	public boolean isSchoolFull() {
		return this.hasFollowers() ? this.schoolSize <= this.getMinSchoolSize() : this.isFollower() && this.leader.schoolSize <= this.getMinSchoolSize();
	}

	public static boolean tryMergeSchools(AbstractSchoolingFish from, AbstractSchoolingFish to) {
		if (from.schoolSize + to.schoolSize <= to.getMaxSchoolSize()) {
			List<? extends Cavefish> fromFollowers = from.level().getEntitiesOfClass(Cavefish.class, from.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
					t -> t.isFollower() && t.leader == from);

			fromFollowers.forEach(follower -> {
				follower.stopFollowing();
				follower.startFollowing(to);
			});

			from.startFollowing(to);
			return true;
		}
		return false;
	}

	public static boolean checkCavefishSpawnRules(EntityType<Cavefish> cavefish, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
		return pos.getY() <= level.getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER);
	}
}