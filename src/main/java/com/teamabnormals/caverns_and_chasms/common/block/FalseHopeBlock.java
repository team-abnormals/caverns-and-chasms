package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FalseHopeBlock extends FlowerBlock {
	private static final VoxelShape UP_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	private static final VoxelShape DOWN_SHAPE = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 16.0D, 11.0D);
	private static final VoxelShape NORTH_SHAPE = Block.box(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 16.0D);
	private static final VoxelShape SOUTH_SHAPE = Block.box(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 10.0D);
	private static final VoxelShape WEST_SHAPE = Block.box(6.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
	private static final VoxelShape EAST_SHAPE = Block.box(0.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D);
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public FalseHopeBlock(Supplier<MobEffect> effect, int effectDuration, Properties properties) {
		super(effect, effectDuration, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> DOWN_SHAPE;
			case UP -> UP_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
		};
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction.getOpposite());
		return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, offsetState, level, pos, offsetPos);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext Context) {
		return this.defaultBlockState().setValue(FACING, Context.getClickedFace());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(FACING);
	}

	@Override
	public float getMaxHorizontalOffset() {
		return 0.15F;
	}

	@Override
	public float getMaxVerticalOffset() {
		return 0.0F;
	}
}