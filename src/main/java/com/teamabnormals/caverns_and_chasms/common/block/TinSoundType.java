package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TinSoundType extends DeferredSoundType {
	private final Supplier<SoundEvent> deflectSound;

	public TinSoundType(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound, Supplier<SoundEvent> deflectSound) {
		super(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
		this.deflectSound = deflectSound;
	}

	@NotNull
	public SoundEvent getDeflectSound() {
		return this.deflectSound.get();
	}
}