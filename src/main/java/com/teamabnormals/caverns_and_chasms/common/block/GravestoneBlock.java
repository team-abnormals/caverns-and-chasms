package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GravestoneBlock extends HorizontalDirectionalBlock {
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 10);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.box(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D),
			Block.box(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.box(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D)};

	public GravestoneBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(CHARGE, 0));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return canSupportCenter(worldIn, pos.below(), Direction.UP);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, CHARGE, POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(CHARGE, 0).setValue(POWERED, false);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
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

	public void powerBlock(BlockState state, Level world, BlockPos pos) {
		world.setBlock(pos, state.setValue(POWERED, true).setValue(GravestoneBlock.CHARGE, state.getValue(CHARGE) < 10 ? state.getValue(CHARGE) + 1 : state.getValue(CHARGE)), 3);
		this.updateNeighbors(state, world, pos);
		world.scheduleTick(pos, this, 20);
	}

	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
		if (state.getValue(POWERED)) {
			worldIn.setBlock(pos, state.setValue(POWERED, false), 3);
			this.updateNeighbors(state, worldIn, pos);
		}
	}

	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWERED) ? 15 : 0;
	}

	public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWERED) ? 15 : 0;
	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	private void updateNeighbors(BlockState state, Level worldIn, BlockPos pos) {
		worldIn.updateNeighborsAt(pos, this);
		worldIn.updateNeighborsAt(pos.below(), this);
	}
}