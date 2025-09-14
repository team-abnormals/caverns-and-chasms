package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class CopperDoorBlock extends DoorBlock {

	public CopperDoorBlock(Properties properties, BlockSetType type) {
		super(properties, type);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP)) {
			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)
					? Blocks.AIR.defaultBlockState()
					: super.updateShape(state, facing, facingState, level, currentPos, facingPos);
		} else {
			return facingState.getBlock() instanceof DoorBlock && facingState.getValue(HALF) != doubleblockhalf
					? facingState.setValue(HALF, doubleblockhalf)
					: Blocks.AIR.defaultBlockState();
		}
	}
}