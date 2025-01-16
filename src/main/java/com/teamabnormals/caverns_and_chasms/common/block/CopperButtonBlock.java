package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.weathering.WeatheringCopperButtonBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
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

public class CopperButtonBlock extends ButtonBlock {
	protected final WeatherState weatherState;

	public CopperButtonBlock(WeatheringCopper.WeatherState weatherState, int ticks, BlockBehaviour.Properties properties) {
		super(properties, CCProperties.COPPER_BLOCK_SET, ticks, false);
		this.weatherState = weatherState;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!(this instanceof WeatheringCopperButtonBlock) && player.getItemInHand(hand).is(ItemTags.AXES) && !state.getValue(POWERED)) {
			return InteractionResult.PASS;
		}
		return super.use(state, level, pos, player, hand, result);
	}
}