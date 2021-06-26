package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class DeeperEntity extends CreeperEntity {

	public DeeperEntity(EntityType<? extends DeeperEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSounds.ENTITY_DEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSounds.ENTITY_DEEPER_DEATH.get();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) source.getEntity();
			if (entity.getMainHandItem().getToolTypes().contains(ToolType.PICKAXE))
				amount *= 2;
		}
		return super.hurt(source, amount);
	}

	@Override
	public void explodeCreeper() {
		if (!this.level.isClientSide) {
			Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, this.isOnFire(), explosion$mode);
			this.remove();
			this.spawnLingeringCloud();
		}
	}
}