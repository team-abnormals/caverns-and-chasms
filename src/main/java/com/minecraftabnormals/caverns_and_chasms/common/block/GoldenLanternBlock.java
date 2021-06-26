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

import net.minecraft.block.AbstractBlock.Properties;

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

		this.downShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 2.0D, 5.0D, 11.0D, 14.0D, 11.0D));
		this.upShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 9.0D));
		this.northShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 9.0D, 9.0D, 16.0D, 0.0D));
		this.southShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 16.0D));
		this.westShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(9.0D, 14.0D, 7.0D, 0.0D, 16.0D, 9.0D));
		this.eastShape = VoxelShapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 16.0D, 16.0D, 9.0D));

		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(FACING)) {
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
		Direction direction = context.getClickedFace();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction);
		return canSupportCenter(world, blockpos, direction.getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return this.canSurvive(state, world, currentPos) ? state : Blocks.AIR.defaultBlockState();
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}