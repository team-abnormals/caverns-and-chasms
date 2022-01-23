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
		switch(this.weatherState) {
		case UNAFFECTED:
		default:
			return 20;
		case EXPOSED:
			return 30;
		case WEATHERED:
			return 40;
		case OXIDIZED:
			return 50;
		}
	}

	@Override
	protected SoundEvent getSound(boolean powered) {
		return powered ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
	}
}