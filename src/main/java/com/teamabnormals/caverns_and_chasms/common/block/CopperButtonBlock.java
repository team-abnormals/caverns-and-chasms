package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CopperButtonBlock extends ButtonBlock {
	protected final WeatheringCopper.WeatherState weatherState;

	public CopperButtonBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
		super(false, properties);
		this.weatherState = weatherState;
	}

	@Override
	public int getPressDuration() {
		return switch (this.weatherState) {
			default -> 20;
			case EXPOSED -> 30;
			case WEATHERED -> 40;
			case OXIDIZED -> 50;
		};
	}

	@Override
	protected SoundEvent getSound(boolean powered) {
		return powered ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
	}
}