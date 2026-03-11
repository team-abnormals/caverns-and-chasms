package com.teamabnormals.caverns_and_chasms.common.level;

import com.teamabnormals.caverns_and_chasms.common.network.S2CCustomSoundExplosionMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;

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

	public static CustomSoundExplosion spawnExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, SoundEvent sound) {
		CustomSoundExplosion explosion = new CustomSoundExplosion(level, entity, x, y, z, radius, fire, blockInteraction, sound);
		if (ForgeEventFactory.onExplosionStart(level, explosion))
			return explosion;
		explosion.explode();
		explosion.finalizeExplosion(true);
		CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CCustomSoundExplosionMessage((float) x, (float) y, (float) z, radius, explosion.getToBlow(), sound));
		return explosion;
	}
}