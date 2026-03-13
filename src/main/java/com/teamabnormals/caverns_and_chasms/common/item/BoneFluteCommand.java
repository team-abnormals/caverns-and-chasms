package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public enum BoneFluteCommand {
	SIT(CCSoundEvents.BONE_FLUTE_SIT.get()),
	RECALL(CCSoundEvents.BONE_FLUTE_RECALL.get()),
	MOVE(CCSoundEvents.BONE_FLUTE_MOVE.get()),
	ATTACK(CCSoundEvents.BONE_FLUTE_ATTACK.get());

	private final ResourceLocation crosshairIcon;
	private final ResourceLocation crosshairIconBackground;
	private final SoundEvent sound;

	BoneFluteCommand(SoundEvent sound) {
		this.crosshairIcon = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + ".png");
		this.crosshairIconBackground = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + "_background.png");
		this.sound = sound;
	}

	public ResourceLocation getCrosshairIcon() {
		return this.crosshairIcon;
	}

	public ResourceLocation getCrosshairIconBackground() {
		return this.crosshairIconBackground;
	}

	public SoundEvent getSound() {
		return this.sound;
	}
}