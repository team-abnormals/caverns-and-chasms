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
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) source.getTrueSource();
			if (entity.getHeldItemMainhand().getToolTypes().contains(ToolType.PICKAXE))
				amount *= 2;
		}
		return super.attackEntityFrom(source, amount);
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