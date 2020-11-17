package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GravestoneBlock extends HorizontalBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.makeCuboidShape(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D)};

	public GravestoneBlock(Properties properties) {
		super(properties);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, ACTIVATED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(ACTIVATED, context.getWorld().isBlockPowered(context.getPos()));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(HORIZONTAL_FACING)) {
			case NORTH:
			default:
				return SHAPES[0];
			case EAST:
				return SHAPES[1];
			case SOUTH:
				return SHAPES[2];
			case WEST:
				return SHAPES[3];
		}
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isRemote) {
			boolean flag = state.get(ACTIVATED);
			if (flag != worldIn.isBlockPowered(pos)) {
				if (flag) {
					worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlockState(pos, state.func_235896_a_(ACTIVATED), 2);
				}
			}
		}
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.get(ACTIVATED) && !worldIn.isBlockPowered(pos)) {
			worldIn.setBlockState(pos, state.func_235896_a_(ACTIVATED), 2);
		}
	}
}