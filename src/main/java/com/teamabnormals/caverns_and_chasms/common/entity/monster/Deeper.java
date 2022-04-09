package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;

public class Deeper extends Creeper {

	public Deeper(EntityType<? extends Deeper> type, Level worldIn) {
		super(type, worldIn);
		this.explosionRadius = 4;
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
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity entity) {
			if (entity.getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG))
				amount *= 2;
		}
		return super.hurt(source, amount);
	}

	@Override
	public void explodeCreeper() {
		if (!this.level.isClientSide) {
			Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, this.isOnFire(), explosion$mode);
			this.discard();
			this.spawnLingeringCloud();
		}
	}
}