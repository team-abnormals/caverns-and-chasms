package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class RollerDoorBlock extends HorizontalDirectionalBlock implements RollerDoor {
	public static final IntegerProperty OPENNESS = IntegerProperty.create("openness", 0, 15);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final Map<Direction, VoxelShape[]> SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, makeShapes(0.0D, 1.0D, 16.0D, 3.0D),
			Direction.SOUTH, makeShapes(0.0D, 13.0D, 16.0D, 15.0D),
			Direction.WEST, makeShapes(1.0D, 0.0D, 3.0D, 16.0D),
			Direction.EAST, makeShapes(13.0D, 0.0D, 15.0D, 16.0D)));

	private static VoxelShape[] makeShapes(double x1, double z1, double x2, double z2) {
		VoxelShape[] shapes = new VoxelShape[16];
		for (int i = 0; i < 16; i++) {
			shapes[i] = box(x1, i, z1, x2, 16.0D, z2);
		}
		return shapes;
	}

	public RollerDoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPENNESS, 0).setValue(WATERLOGGED, false));
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING))[0];
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockPos clickedpos = blockpos.relative(context.getClickedFace().getOpposite());
		BlockState clickedstate = level.getBlockState(clickedpos);

		Direction facing = clickedstate.getBlock() instanceof RollerDoor ? clickedstate.getValue(FACING) : context.getHorizontalDirection().getOpposite();
		BlockState placestate = RollerDoor.getCorrectDoorAndOpenness(level, blockpos, facing, 0);
		FluidState fluidstate = level.getFluidState(blockpos);

		return placestate.setValue(FACING, facing).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetShape, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		Direction facing = state.getValue(FACING);
		boolean waterlogged = state.getValue(WATERLOGGED);

		if (waterlogged)
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return RollerDoor.getCorrectDoorAndOpenness(level, pos, facing, state.getValue(OPENNESS)).setValue(FACING, facing).setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPENNESS, WATERLOGGED);
	}
}
