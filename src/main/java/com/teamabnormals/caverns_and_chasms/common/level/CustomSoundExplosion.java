package com.teamabnormals.caverns_and_chasms.common.level;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CustomSoundExplosion extends Explosion {
	private final SoundEvent sound;

	public CustomSoundExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, SoundEvent sound) {
		this(level, source, x, y, z, radius, BlockInteraction.DESTROY, sound);
	}

	public CustomSoundExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, BlockInteraction blockInteraction, SoundEvent sound) {
		super(level, source, null, null, x, y, z, radius, false, blockInteraction);
		this.sound = sound;
	}

	public SoundEvent getSound() {
		return this.sound;
	}
}