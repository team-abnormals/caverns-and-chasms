package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class RollerDoorHeaderBottomBlock extends RollerDoorHeaderBlock {
	public RollerDoorHeaderBottomBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		AttachFace face = state.getValue(FACE);
		Map<Direction, VoxelShape[]> map = face == AttachFace.WALL ? WALL_SHAPES : face == AttachFace.CEILING ? CEILING_SHAPES : FLOOR_SHAPES;
		return map.get(state.getValue(FACING))[state.getValue(OPENNESS)];
	}
}
