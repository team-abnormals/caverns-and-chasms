package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringPickaxeItem extends PickaxeItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringPickaxeItem(WeatherState weatherState, Tier tier, int damage, float speed, Properties properties) {
		super(tier, damage, speed, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
