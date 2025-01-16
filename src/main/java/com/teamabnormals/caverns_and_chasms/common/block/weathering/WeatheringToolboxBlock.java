package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class WeatheringToolboxBlock extends ToolboxBlock implements CCWeatheringCopper {

	public WeatheringToolboxBlock(WeatherState weatherState, Properties properties) {
		super(weatherState, properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if ((stack.is(ItemTags.AXES) && this.getWeatherState() != WeatherState.UNAFFECTED) || stack.is(Items.HONEYCOMB)) {
			return InteractionResult.PASS;
		}
		return super.use(state, level, pos, player, hand, result);
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
	public void applyChangeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockEntity toolbox = level.getBlockEntity(pos);
		if (toolbox != null) {
			CompoundTag tag = toolbox.serializeNBT();
			CCWeatheringCopper.super.applyChangeOverTime(state, level, pos, random);
			level.getBlockEntity(pos).deserializeNBT(tag);
		} else {
			CCWeatheringCopper.super.applyChangeOverTime(state, level, pos, random);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return CCWeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatherState getAge() {
		return this.getWeatherState();
	}
}