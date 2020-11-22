package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class DeeperEntity extends CreeperEntity {

	public DeeperEntity(EntityType<? extends DeeperEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(CCItems.DEEPER_SPAWN_EGG.get());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.ENTITY_DEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_DEEPER_DEATH.get();
	}

	@Override
	public void explode() {
		if (!this.world.isRemote) {
			Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
			float f = this.isCharged() ? 2.0F : 1.0F;
			this.dead = true;
			this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float) this.explosionRadius * f, this.isBurning(), explosion$mode);
			this.remove();
			this.spawnLingeringCloud();
		}
	}
}