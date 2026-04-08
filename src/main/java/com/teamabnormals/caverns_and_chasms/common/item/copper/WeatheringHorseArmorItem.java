package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringHorseArmorItem extends CopperHorseArmorItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringHorseArmorItem(WeatherState weatherState, Holder<ArmorMaterial> armorMaterial, Properties properties) {
		super(armorMaterial, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
