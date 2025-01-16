package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class CCWeatheringCopperFullBlock extends Block implements CCWeatheringCopper {
	private final WeatherState weatherState;

	public CCWeatheringCopperFullBlock(WeatherState weatherState, Properties properties) {
		super(properties);
		this.weatherState = weatherState;
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
		return action == ToolActions.AXE_SCRAPE ? CCWeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		this.onRandomTick(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return CCWeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}