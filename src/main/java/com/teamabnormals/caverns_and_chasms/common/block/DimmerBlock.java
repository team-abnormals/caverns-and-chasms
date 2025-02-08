package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DimmerBlock extends AbstractDimmerBlock {
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	private static final VoxelShape SHAPE = Shapes.or(box(5.0D, 3.0D, 5.0D, 11.0D, 10.0D, 11.0D), box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D));
	private static final VoxelShape HANGING_SHAPE = Shapes.or(box(5.0D, 7.0D, 5.0D, 11.0D, 13.0D, 11.0D), box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));

	public DimmerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(HANGING, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis() == Direction.Axis.Y) {
				BlockState blockstate = this.defaultBlockState().setValue(HANGING, Boolean.valueOf(direction == Direction.UP));
				if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
					return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
				}
			}
		}

		return null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(HANGING) ? HANGING_SHAPE : SHAPE;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction direction = getConnectedDirection(state).getOpposite();
		return Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite());
	}

	protected static Direction getConnectedDirection(BlockState state) {
		return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetShape, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, offsetShape, level, pos, offsetPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER, HANGING, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}