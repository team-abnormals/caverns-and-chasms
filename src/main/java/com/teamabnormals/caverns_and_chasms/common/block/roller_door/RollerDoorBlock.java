package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.HoldButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class RollerDoorBlock extends HorizontalDirectionalBlock implements RollerDoor {
	public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
	public static final IntegerProperty OPENNESS = IntegerProperty.create("openness", 0, 15);
	public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final Map<Direction, VoxelShape[]> WALL_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 0, 1, 16, 16, 3, Shapes.empty(), Direction.UP),
			Direction.SOUTH, RollerDoor.makeShapes(0, 0, 13, 16, 16, 15, Shapes.empty(), Direction.UP),
			Direction.WEST, RollerDoor.makeShapes(1, 0, 0, 3, 16, 16, Shapes.empty(), Direction.UP),
			Direction.EAST, RollerDoor.makeShapes(13, 0, 0, 15, 16, 16, Shapes.empty(), Direction.UP)));
	protected static final Map<Direction, VoxelShape[]> CEILING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, Shapes.empty(), Direction.NORTH),
			Direction.SOUTH, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, Shapes.empty(), Direction.SOUTH),
			Direction.WEST, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, Shapes.empty(), Direction.WEST),
			Direction.EAST, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, Shapes.empty(), Direction.EAST)));
	protected static final Map<Direction, VoxelShape[]> FLOOR_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, Shapes.empty(), Direction.NORTH),
			Direction.SOUTH, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, Shapes.empty(), Direction.SOUTH),
			Direction.WEST, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, Shapes.empty(), Direction.WEST),
			Direction.EAST, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, Shapes.empty(), Direction.EAST)));

	public RollerDoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(OPENNESS, 0).setValue(BOTTOM, false).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		AttachFace face = state.getValue(FACE);
		Map<Direction, VoxelShape[]> map = face == AttachFace.WALL ? WALL_SHAPES : face == AttachFace.CEILING ? CEILING_SHAPES : FLOOR_SHAPES;
		return map.get(state.getValue(FACING))[state.getValue(BOTTOM) ? state.getValue(OPENNESS) : 0];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		return this.handleLifting(state, level, pos, player, hand, hitResult);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = level.getFluidState(blockpos);
		Direction clickedface = context.getClickedFace();
		BlockPos clickedpos = blockpos.relative(clickedface.getOpposite());
		BlockState clickedstate = level.getBlockState(clickedpos);

		boolean flag = false;
		if (clickedstate.getBlock() instanceof RollerDoor) {
			AttachFace face = clickedstate.getValue(FACE);
			Direction facing = clickedstate.getValue(FACING);
			if (face == AttachFace.WALL) {
				if (facing.getAxis() != clickedface.getAxis())
					flag = true;
			} else if (clickedface.getAxis() != Axis.Y) {
				flag = true;
			}
		}

		AttachFace face = flag ? clickedstate.getValue(FACE) : clickedface.getAxis() == Axis.Y ? AttachFace.WALL : context.getClickLocation().y - context.getClickedPos().getY() > 0.5D ? AttachFace.FLOOR : AttachFace.CEILING;
		Direction facing = flag ? clickedstate.getValue(FACING) : face == AttachFace.WALL ? context.getHorizontalDirection().getOpposite() : clickedface.getOpposite();

		return this.getUpdatedState(level, blockpos, facing, face, 0, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetShape, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		boolean waterlogged = state.getValue(WATERLOGGED);

		if (waterlogged)
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return this.getUpdatedState(level, pos, state.getValue(FACING), state.getValue(FACE), state.getValue(OPENNESS), waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FACE, OPENNESS, BOTTOM, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}
