package com.teamabnormals.caverns_and_chasms.common.block;

import java.util.Random;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
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

public class FloodlightBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape DOWN_SHAPE = Shapes.or(box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), box(7.0D, 4.0D, 7.0D, 9.0D, 16.0D, 9.0D));
	private static final VoxelShape UP_SHAPE = Shapes.or(box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D), box(7.0D, 0.0D, 7.0D, 9.0D, 12.0D, 9.0D));
	private static final VoxelShape NORTH_SHAPE = Shapes.or(box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D), box(7.0D, 7.0D, 4.0D, 9.0D, 9.0D, 16.0D));
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(box(0.0D, 0.0D, 12.0D, 16.0D, 16.0D, 16.0D), box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 12.0D));
	private static final VoxelShape WEST_SHAPE = Shapes.or(box(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D), box(4.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D));
	private static final VoxelShape EAST_SHAPE = Shapes.or(box(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), box(0.0D, 7.0D, 7.0D, 12.0D, 9.0D, 9.0D));
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public FloodlightBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
		case DOWN:
		default:
			return DOWN_SHAPE;
		case UP:
			return UP_SHAPE;
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		if (random.nextInt(3) == 0) {
			Vec3i vec3i = state.getValue(FACING).getNormal();
			double x = 0.1D + random.nextDouble() * 0.8D + vec3i.getX();
			double y = 0.1D + random.nextDouble() * 0.8D + vec3i.getY();
			double z = 0.1D + random.nextDouble() * 0.8D + vec3i.getZ();
			level.addParticle(CCParticleTypes.FLOODLIGHT_DUST.get(), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0, 0);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
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

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}