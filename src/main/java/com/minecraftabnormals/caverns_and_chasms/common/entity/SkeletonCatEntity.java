package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SkeletonCatEntity extends CatEntity {

	public SkeletonCatEntity(EntityType<? extends SkeletonCatEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public SkeletonCatEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		SkeletonCatEntity cat = CCEntities.SKELETON_CAT.get().create(world);
		if (this.rand.nextBoolean()) {
			cat.setCatType(this.getCatType());
		} else {
			cat.setCatType(cat.getCatType());
		}

		if (this.isTamed()) {
			cat.setOwnerId(this.getOwnerId());
			cat.setTamed(true);
			if (this.rand.nextBoolean()) {
				cat.setCollarColor(this.getCollarColor());
			} else {
				cat.setCollarColor(cat.getCollarColor());
			}
		}

		return cat;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}
}