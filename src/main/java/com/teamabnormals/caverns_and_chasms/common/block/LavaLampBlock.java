package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LavaLampBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape X_AXIS_SHAPE = Shapes.or(box(0.0D, 2.0D, 2.0D, 2.0D, 14.0D, 14.0D), box(2.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D), box(14.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D));
	private static final VoxelShape Y_AXIS_SHAPE = Shapes.or(box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D), box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D), box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D));
	private static final VoxelShape Z_AXIS_SHAPE = Shapes.or(box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 2.0D), box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 14.0D), box(2.0D, 2.0D, 14.0D, 14.0D, 14.0D, 16.0D));
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public LavaLampBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		switch (state.getValue(AXIS)) {
		case X:
		default:
			return X_AXIS_SHAPE;
		case Y:
			return Y_AXIS_SHAPE;
		case Z:
			return Z_AXIS_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}