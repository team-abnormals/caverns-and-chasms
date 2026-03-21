package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;

public enum BoneFluteCommand {
	SIT,
	RECALL,
	MOVE,
	ATTACK;

	private final ResourceLocation crosshairIcon;
	private final ResourceLocation crosshairIconBackground;

	BoneFluteCommand() {
		this.crosshairIcon = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + ".png");
		this.crosshairIconBackground = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + "_background.png");
	}

	public ResourceLocation getCrosshairIcon() {
		return this.crosshairIcon;
	}

	public ResourceLocation getCrosshairIconBackground() {
		return this.crosshairIconBackground;
	}
}