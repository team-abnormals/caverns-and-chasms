package com.teamabnormals.caverns_and_chasms.common.block;

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

	static float[] getColor(LevelReader reader) {
		if (reader instanceof Level level) {
			long ticks = level.getGameTime();
			int colorIndex = (int) (ticks / TICK_RATE);
			int colorLength = COLORS.length;
			float f = (float) (ticks % TICK_RATE) / (float) TICK_RATE;
			float[] fromColor = COLORS[colorIndex % colorLength].getTextureDiffuseColors();
			float[] toColor = COLORS[(colorIndex + 1) % colorLength].getTextureDiffuseColors();
			float r = fromColor[0] * (1.0F - f) + toColor[0] * f;
			float g = fromColor[1] * (1.0F - f) + toColor[1] * f;
			float b = fromColor[2] * (1.0F - f) + toColor[2] * f;
			return new float[]{r, g, b};
		} else {
			return DyeColor.RED.getTextureDiffuseColors();
		}
	}
}
