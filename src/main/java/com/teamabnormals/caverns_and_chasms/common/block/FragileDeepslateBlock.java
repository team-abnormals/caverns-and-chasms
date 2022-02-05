package com.teamabnormals.caverns_and_chasms.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FragileDeepslateBlock extends RotatedPillarBlock implements FragileBlock {
	public FragileDeepslateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockentity, ItemStack stack) {
		if (!level.isClientSide() && FragileBlock.shouldBreakNeighbors(player, stack)) {
			FragileBlock.breakNeighbors(level, pos);
		}
		super.playerDestroy(level, player, pos, state, blockentity, stack);
	}

	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
		if (!level.isClientSide()) {
			FragileBlock.breakNeighbors(level, pos);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		FragileBlock.breakNeighbors(level, pos);
		level.destroyBlock(pos, true);
	}
}