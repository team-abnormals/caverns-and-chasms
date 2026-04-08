package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringCopperArmorItem extends CopperArmorItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringCopperArmorItem(WeatherState weatherState, Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
		this.weatherState = weatherState;
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
