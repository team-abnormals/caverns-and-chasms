package com.teamabnormals.caverns_and_chasms.common.level;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CustomSoundExplosion extends Explosion {
	private final SoundEvent sound;

	public CustomSoundExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator calculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound) {
		super(level, source, damageSource, calculator, x, y, z, radius, fire, blockInteraction);
		this.sound = sound;
	}

	public CustomSoundExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound) {
		this(level, source, null, null, x, y, z, radius, fire, blockInteraction, sound);
	}

	public SoundEvent getSound() {
		return this.sound;
	}
}