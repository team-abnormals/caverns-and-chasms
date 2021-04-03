package com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SkeletonParrotEntity extends ParrotEntity {

	public SkeletonParrotEntity(EntityType<? extends SkeletonParrotEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public SkeletonParrotEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		SkeletonParrotEntity parrot = CCEntities.SKELETON_PARROT.get().create(world);
		if (this.rand.nextBoolean()) {
			parrot.setVariant(this.getVariant());
		} else {
			parrot.setVariant(parrot.getVariant());
		}

		if (this.isTamed()) {
			parrot.setOwnerId(this.getOwnerId());
			parrot.setTamed(true);
		}

		return parrot;
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
}