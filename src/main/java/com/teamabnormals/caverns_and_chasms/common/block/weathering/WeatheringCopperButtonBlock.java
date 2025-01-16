package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import com.teamabnormals.caverns_and_chasms.common.block.CopperButtonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class WeatheringCopperButtonBlock extends CopperButtonBlock implements CCWeatheringCopper {

	public WeatheringCopperButtonBlock(WeatheringCopper.WeatherState weatherState, int ticks, BlockBehaviour.Properties properties) {
		super(weatherState, ticks, properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (((stack.is(ItemTags.AXES) && this.weatherState != WeatherState.UNAFFECTED) || stack.is(Items.HONEYCOMB)) && !state.getValue(POWERED))
			return InteractionResult.PASS;
		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
		return action == ToolActions.AXE_SCRAPE ? CCWeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(POWERED))
			this.onRandomTick(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return CCWeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatheringCopper.WeatherState getAge() {
		return this.weatherState;
	}
}