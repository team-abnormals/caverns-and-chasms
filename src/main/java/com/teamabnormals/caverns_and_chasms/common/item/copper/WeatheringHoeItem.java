package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringHoeItem extends HoeItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringHoeItem(WeatherState weatherState, Tier tier, Properties properties) {
		super(tier, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
