package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractDimmerBlock extends Block {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public AbstractDimmerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		int i = level.getBestNeighborSignal(pos);
		if (i > 0)
			level.scheduleTick(pos, this, 1);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide) {
			int i = level.getBestNeighborSignal(pos);
			int j = state.getValue(POWER);
			if (i > j) {
				level.scheduleTick(pos, this, 1);
			} else if (i < j) {
				level.scheduleTick(pos, this, 2);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int i = level.getBestNeighborSignal(pos);
		int j = state.getValue(POWER);
		if (i > j) {
			int k = j + 1;
			level.setBlock(pos, state.setValue(POWER, k), 2);
			if (i > k)
				level.scheduleTick(pos, this, 1);
		} else if (i < j) {
			int k = j - 1;
			level.setBlock(pos, state.setValue(POWER, k), 2);
			if (i < k)
				level.scheduleTick(pos, this, 2);
		}
	}
}