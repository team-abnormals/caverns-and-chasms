package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.CopperButtonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class WeatheringCopperButtonBlock extends CopperButtonBlock implements WeatheringCopper {

	public WeatheringCopperButtonBlock(WeatheringCopper.WeatherState weatherState, int ticks, BlockBehaviour.Properties properties) {
		super(weatherState, ticks, properties);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (((stack.is(ItemTags.AXES) && this.weatherState != WeatherState.UNAFFECTED) || stack.getItem() instanceof HoneycombItem) && !state.getValue(POWERED))
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		return super.useItemOn(stack, state, level, pos, player, hand, result);
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility action, boolean simulate) {
		return action == ItemAbilities.AXE_SCRAPE ? WeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(POWERED))
			this.changeOverTime(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatheringCopper.WeatherState getAge() {
		return this.weatherState;
	}
}