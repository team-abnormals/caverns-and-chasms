package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

	@Shadow
	@Final
	@Nullable
	private Entity source;

	@Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private boolean explode(Entity entity, DamageSource source, float damage) {
		return !(this.source instanceof PrimedTmt) && entity.hurt(source, damage);
	}

	@Redirect(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
	private void explode(Level level, ParticleOptions particle, double x, double y, double z, double p_46635_, double p_46636_, double p_46637_) {
		level.addParticle(this.source instanceof PrimedTmt ? CCParticleTypes.SPINEL_BOOM_EMITTER.get() : particle, x, y, z, p_46635_, p_46636_, p_46637_);
	}
}
