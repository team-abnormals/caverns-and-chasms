package com.teamabnormals.caverns_and_chasms.common.level;

import com.teamabnormals.caverns_and_chasms.common.network.S2CCustomSoundExplosionMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class CustomExplosion extends Explosion {
	private final SoundEvent sound;
	private final ParticleOptions emitter;
	private final ParticleOptions particle;

	public CustomExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator calculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound, ParticleOptions emitter, ParticleOptions particle) {
		super(level, source, damageSource, calculator, x, y, z, radius, fire, blockInteraction);
		this.sound = sound;
		this.emitter = emitter;
		this.particle = particle;
	}

	public CustomExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound, ParticleOptions emitter, ParticleOptions particle) {
		this(level, source, null, null, x, y, z, radius, fire, blockInteraction, sound, emitter, particle);
	}

	public CustomExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound) {
		this(level, source, x, y, z, radius, fire, blockInteraction, sound, ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.EXPLOSION);
	}

	public SoundEvent getSound() {
		return this.sound;
	}

	public ParticleOptions getEmitter() {
		return this.emitter;
	}

	public ParticleOptions getParticle() {
		return this.particle;
	}

	public static CustomExplosion spawnExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound) {
		return spawnExplosion(level, entity, x, y, z, radius, fire, blockInteraction, sound, ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.EXPLOSION);
	}

	public static CustomExplosion spawnExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound, ParticleOptions emitter, ParticleOptions particle) {
		CustomExplosion explosion = new CustomExplosion(level, entity, x, y, z, radius, fire, blockInteraction, sound, emitter, particle);
		if (ForgeEventFactory.onExplosionStart(level, explosion))
			return explosion;
		explosion.explode();
		explosion.finalizeExplosion(true);
		CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CCustomSoundExplosionMessage((float) x, (float) y, (float) z, radius, explosion.getToBlow(), sound, emitter, particle));
		return explosion;
	}
}