package com.teamabnormals.caverns_and_chasms.common.entity.skeleton;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SkeletonWolf extends Wolf {

	public SkeletonWolf(EntityType<? extends SkeletonWolf> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public SkeletonWolf getBreedOffspring(ServerLevel world, AgeableMob entity) {
		SkeletonWolf wolf = CCEntityTypes.SKELETON_WOLF.get().create(world);
		UUID uuid = this.getOwnerUUID();
		if (uuid != null) {
			wolf.setOwnerUUID(uuid);
			wolf.setTame(true);
		}

		return wolf;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SKELETON_DEATH;
	}

	@Override
	public float getWetShade(float partialTicks) {
		return 1.0F;
	}
}