package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RollerDoorHeaderBottomBlock extends RollerDoorHeaderBlock {
	public RollerDoorHeaderBottomBlock(Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING))[state.getValue(OPENNESS)];
	}
}
