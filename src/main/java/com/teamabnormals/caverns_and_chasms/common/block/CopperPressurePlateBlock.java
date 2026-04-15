package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class CopperPressurePlateBlock extends PressurePlateBlock {
	protected final WeatherState weatherState;

	public CopperPressurePlateBlock(WeatherState weatherState, Properties properties) {
		super(CCProperties.COPPER_BLOCK_SET.get(), properties);
		this.weatherState = weatherState;
	}
}