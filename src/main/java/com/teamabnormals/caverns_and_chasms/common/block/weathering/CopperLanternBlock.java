package com.teamabnormals.caverns_and_chasms.common.block.weathering;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopperLanternBlock extends LanternBlock {
	protected static final VoxelShape AABB = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	protected static final VoxelShape HANGING_AABB = Block.box(4.0D, 3.0D, 4.0D, 12.0D, 11.0D, 12.0D);

	public CopperLanternBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(HANGING) ? HANGING_AABB : AABB;
	}
}
