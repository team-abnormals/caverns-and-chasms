package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

public class GoldenLanternBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
	protected VoxelShape downShape;
	protected VoxelShape upShape;
	protected VoxelShape northShape;
	protected VoxelShape southShape;
	protected VoxelShape westShape;
	protected VoxelShape eastShape;

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public GoldenLanternBlock(Properties properties) {
		super(properties);

		this.downShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 2.0D, 5.0D, 11.0D, 14.0D, 11.0D));
		this.upShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 9.0D));
		this.northShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 0.0D, 9.0D, 16.0D, 9.0D));
		this.southShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 16.0D));
		this.westShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(0.0D, 14.0D, 7.0D, 9.0D, 16.0D, 9.0D));
		this.eastShape = Shapes.or(box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), box(1.0D, 10.0D, 1.0D, 15.0D, 12.0D, 15.0D), box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D), box(7.0D, 14.0D, 7.0D, 16.0D, 16.0D, 9.0D));

		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction);
		return canSupportCenter(world, blockpos, direction.getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}