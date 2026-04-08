package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringSwordItem extends SwordItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringSwordItem(WeatherState weatherState, Tier tier, Properties properties) {
		super(tier, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
