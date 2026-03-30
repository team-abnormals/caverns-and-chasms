package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Peeper;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends LivingEntity {

	protected CreeperMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@WrapOperation(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"))
	private Explosion explodeCreeper(Level level, Entity creeper, double x, double y, double z, float power, ExplosionInteraction interaction, Operation<Explosion> original) {
		if (CCConfig.COMMON.creeperExplosionNerf.get()) {
			power *= CCConfig.COMMON.creeperExplosionNerfFactor.get().floatValue();
		}

		return original.call(level, creeper, x, y, z, power, interaction);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;isAlive()Z"))
	private boolean tick(Creeper creeper, Operation<Boolean> original) {
		return creeper instanceof Peeper || original.call(creeper);
	}
}
