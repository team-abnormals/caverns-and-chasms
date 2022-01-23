package com.teamabnormals.caverns_and_chasms.common.block;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class WeatheringCopperButtonBlock extends CopperButtonBlock implements WeatheringCopper {
	public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().put(CCBlocks.COPPER_BUTTON.get(), CCBlocks.EXPOSED_COPPER_BUTTON.get()).put(CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WEATHERED_COPPER_BUTTON.get()).put(CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.OXIDIZED_COPPER_BUTTON.get()).build());
	public static Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

	public WeatheringCopperButtonBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
		super(weatherState, properties);
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction action) {
		return action == ToolActions.AXE_SCRAPE ? getPrevious(state).orElse(null) : super.getToolModifiedState(state, world, pos, player, stack, action);
	}

	@Override
	public Optional<BlockState> getNext(BlockState state) {
		return getNext(state.getBlock()).map((nextState) -> nextState.withPropertiesOf(state));
	}

	public static Optional<BlockState> getPrevious(BlockState state) {
		return getPrevious(state.getBlock()).map((prevState) -> prevState.withPropertiesOf(state));
	}

	public static BlockState getFirst(BlockState state) {
		return getFirst(state.getBlock()).withPropertiesOf(state);
	}

	public static Optional<Block> getNext(Block block) {
		return Optional.ofNullable(NEXT_BY_BLOCK.get().get(block));
	}

	public static Optional<Block> getPrevious(Block block) {
		return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
	}

	public static Block getFirst(Block block) {
		Block firstBlock = block;

		for (Block previousBlock = PREVIOUS_BY_BLOCK.get().get(block); previousBlock != null; previousBlock = PREVIOUS_BY_BLOCK.get().get(previousBlock)) {
			firstBlock = previousBlock;
		}

		return firstBlock;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		this.onRandomTick(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatheringCopper.WeatherState getAge() {
		return this.weatherState;
	}
}