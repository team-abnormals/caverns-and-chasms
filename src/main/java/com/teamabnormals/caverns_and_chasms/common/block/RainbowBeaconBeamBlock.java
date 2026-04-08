package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public interface RainbowBeaconBeamBlock {
	int TICK_RATE = 25;
	DyeColor[] COLORS = new DyeColor[]{
			DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW,
			DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN,
			DyeColor.LIGHT_BLUE, DyeColor.BLUE,
			DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK,
	};

	static Integer getColor(LevelReader reader) {
		if (reader instanceof Level level) {
			long ticks = level.getGameTime();
			int colorIndex = (int) (ticks / TICK_RATE);
			int colorLength = COLORS.length;
			float f = (float) (ticks % TICK_RATE) / (float) TICK_RATE;
			int fromColor = COLORS[colorIndex % colorLength].getTextureDiffuseColor();
			int toColor = COLORS[(colorIndex + 1) % colorLength].getTextureDiffuseColor();
			return FastColor.ARGB32.lerp(f, fromColor, toColor);
		} else {
			return DyeColor.RED.getTextureDiffuseColor();
		}
	}
}
