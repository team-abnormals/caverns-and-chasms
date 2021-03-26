package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GravestoneBlock extends HorizontalBlock {
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 10);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.makeCuboidShape(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D)};

	public GravestoneBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, Boolean.FALSE).with(CHARGE, 0));
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, CHARGE, POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(CHARGE, 0).with(POWERED, false);
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

	public void powerBlock(BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, state.with(POWERED, true).with(GravestoneBlock.CHARGE, state.get(CHARGE) < 10 ? state.get(CHARGE) + 1: state.get(CHARGE)), 3);
		this.updateNeighbors(state, world, pos);
		world.getPendingBlockTicks().scheduleTick(pos, this, 20);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.get(POWERED)) {
			worldIn.setBlockState(pos, state.with(POWERED, false), 3);
			this.updateNeighbors(state, worldIn, pos);
		}
	}

	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.get(POWERED) ? 15 : 0;
	}

	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.get(POWERED)  ? 15 : 0;
	}

	public boolean canProvidePower(BlockState state) {
		return true;
	}

	private void updateNeighbors(BlockState state, World worldIn, BlockPos pos) {
		worldIn.notifyNeighborsOfStateChange(pos, this);
		worldIn.notifyNeighborsOfStateChange(pos.down(), this);
	}
}