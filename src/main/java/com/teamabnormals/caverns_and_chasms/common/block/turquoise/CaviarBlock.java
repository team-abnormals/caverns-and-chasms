package com.teamabnormals.caverns_and_chasms.common.block.turquoise;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CaviarBlock extends Block {
	protected static final VoxelShape SHAPE = Shapes.or(
			Block.box(2, 0, 2, 14, 9, 14),
			Block.box(3, 9, 3, 13, 11, 13),
			Block.box(7, 13, 7, 9, 15, 9),
			Block.box(5, 11, 5, 11, 13, 11)
	);

	public CaviarBlock(Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState p_53556_, BlockGetter p_53557_, BlockPos p_53558_, CollisionContext p_53559_) {
		return SHAPE;
	}

	public boolean isPathfindable(BlockState p_53535_, BlockGetter p_53536_, BlockPos p_53537_, PathComputationType p_53538_) {
		return false;
	}
}