package com.minecraftabnormals.caverns_and_chasms.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SkeletonWolfEntity extends WolfEntity {

	public SkeletonWolfEntity(EntityType<? extends SkeletonWolfEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	@Override
	public float getShadingWhileWet(float partialTicks) {
		return 1.0F;
	}
}