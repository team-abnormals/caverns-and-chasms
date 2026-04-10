package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.CopperRailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class WeatheringCopperRailBlock extends CopperRailBlock implements WeatheringCopper {

	public WeatheringCopperRailBlock(WeatheringCopper.WeatherState weatherState, Properties properties) {
		super(weatherState, properties);
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility action, boolean simulate) {
		return action == ItemAbilities.AXE_SCRAPE ? WeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		this.changeOverTime(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatherState getAge() {
		return this.getWeatherState();
	}
}