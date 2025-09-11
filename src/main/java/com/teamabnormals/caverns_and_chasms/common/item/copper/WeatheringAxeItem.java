package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class WeatheringAxeItem extends AxeItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public WeatheringAxeItem(WeatherState weatherState, Tier tier, float damage, float speed, Properties properties) {
		super(tier, damage, speed, properties);
		this.weatherState = weatherState;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean b) {
		this.updateOxidation(stack, level);
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
