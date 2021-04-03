package com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class SkeletonWolfEntity extends WolfEntity {

	public SkeletonWolfEntity(EntityType<? extends SkeletonWolfEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public SkeletonWolfEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		SkeletonWolfEntity wolf = CCEntities.SKELETON_WOLF.get().create(world);
		UUID uuid = this.getOwnerId();
		if (uuid != null) {
			wolf.setOwnerId(uuid);
			wolf.setTamed(true);
		}

		return wolf;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
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