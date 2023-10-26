package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;

public class CopperButtonBlock extends ButtonBlock {
	protected final WeatherState weatherState;

	public CopperButtonBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
		super(false, properties);
		this.weatherState = weatherState;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!(this instanceof WeatheringCopperButtonBlock) && player.getItemInHand(hand).is(Tags.Items.TOOLS_AXES) && !state.getValue(POWERED)) {
			return InteractionResult.PASS;
		}
		return super.use(state, level, pos, player, hand, result);
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