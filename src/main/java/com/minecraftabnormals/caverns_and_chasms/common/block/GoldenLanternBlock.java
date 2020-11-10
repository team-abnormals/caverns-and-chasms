package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class GoldenLanternBlock extends DirectionalBlock implements IWaterLoggable {

	protected VoxelShape downShape;
	protected VoxelShape upShape;
	protected VoxelShape northShape;
	protected VoxelShape southShape;
	protected VoxelShape westShape;
	protected VoxelShape eastShape;

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public GoldenLanternBlock(Properties properties) {
		super(properties);

		this.downShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 2.0D, 5.0D, 11.0D, 14.0D, 11.0D));
		this.upShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), makeCuboidShape(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 9.0D));
		this.northShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), makeCuboidShape(7.0D, 14.0D, 9.0D, 9.0D, 16.0D, 0.0D));
		this.southShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), makeCuboidShape(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 16.0D));
		this.westShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), makeCuboidShape(9.0D, 14.0D, 7.0D, 0.0D, 16.0D, 9.0D));
		this.eastShape = VoxelShapes.or(makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), makeCuboidShape(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), makeCuboidShape(7.0D, 14.0D, 7.0D, 16.0D, 16.0D, 9.0D));

		this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
			default:
			case DOWN:
				return this.downShape;
			case UP:
				return this.upShape;
			case NORTH:
				return this.northShape;
			case SOUTH:
				return this.southShape;
			case WEST:
				return this.westShape;
			case EAST:
				return this.eastShape;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, direction.getOpposite()).with(WATERLOGGED, Boolean.valueOf(fluidstate.isTagged(FluidTags.WATER) && fluidstate.getLevel() == 8));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockPos blockpos = pos.offset(direction);
		return Block.hasEnoughSolidSide(world, blockpos, direction);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return this.isValidPosition(state, world, currentPos) ? state : Blocks.AIR.getDefaultState();
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.with(FACING, mirror.mirror(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
}