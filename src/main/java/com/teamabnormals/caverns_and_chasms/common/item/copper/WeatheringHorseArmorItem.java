package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringHorseArmorItem extends CopperHorseArmorItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringHorseArmorItem(WeatherState weatherState, int armorValue, String tierArmor, Properties properties) {
		super(armorValue, tierArmor, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
