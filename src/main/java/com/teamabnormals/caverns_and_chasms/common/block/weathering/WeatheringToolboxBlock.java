package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class WeatheringToolboxBlock extends ToolboxBlock implements WeatheringCopper {

	public WeatheringToolboxBlock(WeatherState weatherState, Properties properties) {
		super(weatherState, properties);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if ((stack.is(ItemTags.AXES) && this.getWeatherState() != WeatherState.UNAFFECTED) || stack.getItem() instanceof HoneycombItem) {
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, result);
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
	public void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockEntity toolbox = level.getBlockEntity(pos);
		if (toolbox != null) {
			RegistryAccess access = level.registryAccess();
			CompoundTag tag = toolbox.serializeAttachments(access);
			WeatheringCopper.super.changeOverTime(state, level, pos, random);
			level.getBlockEntity(pos).loadWithComponents(tag, access);
		} else {
			WeatheringCopper.super.changeOverTime(state, level, pos, random);
		}
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