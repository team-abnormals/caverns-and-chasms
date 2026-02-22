package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class WallDimmerBlock extends AbstractDimmerBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Shapes.or(box(5.0D, 6.0D, 10.0D, 11.0D, 13.0D, 16.0D), box(7.0D, 3.0D, 12.0D, 9.0D, 6.0D, 14.0D), box(7.0D, 3.0D, 14.0D, 9.0D, 5.0D, 16.0D)),
			Direction.SOUTH, Shapes.or(box(5.0D, 6.0D, 0.0D, 11.0D, 13.0D, 6.0D), box(7.0D, 3.0D, 2.0D, 9.0D, 6.0D, 4.0D), box(7.0D, 3.0D, 0.0D, 9.0D, 5.0D, 2.0D)),
			Direction.WEST, Shapes.or(box(10.0D, 6.0D, 5.0D, 16.0D, 13.0D, 11.0D), box(12.0D, 3.0D, 7.0D, 14.0D, 6.0D, 9.0D), box(14.0D, 3.0D, 7.0D, 16.0D, 5.0D, 9.0D)),
			Direction.EAST, Shapes.or(box(0.0D, 6.0D, 5.0D, 6.0D, 13.0D, 11.0D), box(2.0D, 3.0D, 7.0D, 4.0D, 6.0D, 9.0D), box(0.0D, 3.0D, 7.0D, 2.0D, 5.0D, 9.0D))));

	public WallDimmerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	public String getDescriptionId() {
		return this.asItem().getDescriptionId();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	public boolean canSurvive(BlockState p_58133_, LevelReader p_58134_, BlockPos p_58135_) {
		Direction direction = p_58133_.getValue(FACING);
		BlockPos blockpos = p_58135_.relative(direction.getOpposite());
		BlockState blockstate = p_58134_.getBlockState(blockpos);
		return blockstate.isFaceSturdy(p_58134_, blockpos, direction);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = this.defaultBlockState();
		LevelReader levelreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction[] adirection = context.getNearestLookingDirections();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		for (Direction direction : adirection) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.setValue(FACING, direction1);
				if (blockstate.canSurvive(levelreader, blockpos)) {
					return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
				}
			}
		}

		return null;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror rotation) {
		return state.rotate(rotation.getRotation(state.getValue(FACING)));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER, FACING, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}