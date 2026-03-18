package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class RollerDoorBlock extends AbstractMovingDoorBlock {
	private static final Map<Direction, VoxelShape> WALL_HEADER_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 12, 0, 16, 16, 4),
			Direction.SOUTH, box(0, 12, 12, 16, 16, 16),
			Direction.WEST, box(0, 12, 0, 4, 16, 16),
			Direction.EAST, box(12, 12, 0, 16, 16, 16)));
	private static final Map<Direction, VoxelShape> CEILING_HEADER_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 0, 0, 16, 4, 4),
			Direction.SOUTH, box(0, 0, 12, 16, 4, 16),
			Direction.WEST, box(0, 0, 0, 4, 4, 16),
			Direction.EAST, box(12, 0, 0, 16, 4, 16)));
	private static final Map<Direction, VoxelShape> FLOOR_HEADER_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 12, 0, 16, 16, 4),
			Direction.SOUTH, box(0, 12, 12, 16, 16, 16),
			Direction.WEST, box(0, 12, 0, 4, 16, 16),
			Direction.EAST, box(12, 12, 0, 16, 16, 16)));

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

	public RollerDoorBlock(boolean isHeader, MovingDoorType doorType, Properties properties) {
		super(isHeader, doorType, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return this.isHeader() ? new MovingDoorHeaderBlockEntity(pos, state) : new MovingDoorBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		if (this.isHeader()) {
			return createTickerHelper(entityType, CCBlockEntityTypes.MOVING_DOOR_HEADER.get(), MovingDoorHeaderBlockEntity::tick);
		} else {
			return createTickerHelper(entityType, CCBlockEntityTypes.MOVING_DOOR.get(), MovingDoorBlockEntity::tick);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		FluidState fluidState = level.getFluidState(blockPos);
		Direction clickedFace = context.getClickedFace();
		BlockPos clickedPos = blockPos.relative(clickedFace.getOpposite());
		BlockState clickedState = level.getBlockState(clickedPos);

		boolean flag = false;
		if (clickedState.getBlock() instanceof RollerDoorBlock) {
			AttachFace face = clickedState.getValue(FACE);
			Direction facing = clickedState.getValue(FACING);
			if (face == AttachFace.WALL) {
				if (facing.getAxis() != clickedFace.getAxis())
					flag = true;
			} else if (clickedFace.getAxis() != Axis.Y) {
				flag = true;
			}
		}

		AttachFace face = flag ? clickedState.getValue(FACE) : clickedFace.getAxis() == Axis.Y ? AttachFace.WALL : context.getClickLocation().y - context.getClickedPos().getY() > 0.5D ? AttachFace.FLOOR : AttachFace.CEILING;
		Direction facing = flag ? clickedState.getValue(FACING) : face == AttachFace.WALL ? context.getHorizontalDirection().getOpposite() : clickedFace.getOpposite();

		BlockPos abovePos = blockPos.relative(this.getAboveDirection(facing, face));
		BlockState aboveState = level.getBlockState(abovePos);

		BlockState placeState = this.canBePartOfSameDoor(aboveState.getBlock()) && this.doDoorsAlign(facing, face, aboveState) ? this.getNormalBlock().defaultBlockState() : this.getHeaderBlock().defaultBlockState();

		return placeState.setValue(FACING, facing).setValue(FACE, face).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED))
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return state;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof MovingDoorBlockEntity blockEntity)
			blockEntity.onRemove(newState);

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FACE, WATERLOGGED);
	}

	@Override
	public Direction getAboveDirection(BlockState state) {
		return this.getAboveDirection(state.getValue(RollerDoorBlock.FACING), state.getValue(RollerDoorBlock.FACE));
	}

	public Direction getAboveDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.UP : facing;
	}

	@Override
	public Direction getRightDirection(BlockState state) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		return state.getValue(RollerDoorBlock.FACE) == AttachFace.WALL ? facing.getCounterClockWise() : facing.getClockWise();
	}

	@Override
	public boolean doDoorsAlign(BlockState state, BlockState otherState) {
		return this.doDoorsAlign(state.getValue(RollerDoorBlock.FACING), state.getValue(RollerDoorBlock.FACE), otherState);
	}

	public boolean doDoorsAlign(Direction facing, AttachFace face, BlockState otherState) {
		return facing == otherState.getValue(RollerDoorBlock.FACING) && face == otherState.getValue(RollerDoorBlock.FACE);
	}

	@Override
	public boolean canBePartOfSameDoor(Block block) {
		return block instanceof RollerDoorBlock;
	}

	public VoxelShape getHeaderShape(BlockState state) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		return face == AttachFace.WALL ? WALL_HEADER_SHAPES.get(facing) : face == AttachFace.CEILING ? CEILING_HEADER_SHAPES.get(facing) : FLOOR_HEADER_SHAPES.get(facing);
	}

	public VoxelShape getDoorShape(MovingDoorBlockEntity blockEntity, BlockState state) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		double openness = blockEntity.isBottom() ? blockEntity.getOpenness(1.0F) : 0.0D;

		if (face == AttachFace.WALL) {
			if (facing == Direction.NORTH)
				return Block.box(0.0D, 16.0D * openness, 1.0D, 16.0D, 16.0D, 3.0D);
			else if (facing == Direction.SOUTH)
				return Block.box(0.0D, 16.0D * openness, 13.0D, 16.0D, 16.0D, 15.0D);
			else if (facing == Direction.WEST)
				return Block.box(1.0D, 16.0D * openness, 0.0D, 3.0D, 16.0D, 16.0D);
			else
				return Block.box(13.0D, 16.0D * openness, 0.0D, 15.0D, 16.0D, 16.0D);
		} else {
			int offset = face == AttachFace.FLOOR ? 12 : 0;
			if (facing == Direction.NORTH)
				return Block.box(0.0D, 1.0D + offset, 0.0D, 16.0D, 3.0D + offset, 16.0D - 16.0D * openness);
			else if (facing == Direction.SOUTH)
				return Block.box(0.0D, 1.0D + offset, 16.0D * openness, 16.0D, 3.0D + offset, 16.0D);
			else if (facing == Direction.WEST)
				return Block.box(0.0D, 1.0D + offset, 0.0D, 16.0D - 16.0D * openness, 3.0D + offset, 16.0D);
			else
				return Block.box(16.0D * openness, 1.0D + offset, 0.0D, 16.0D, 3.0D + offset, 16.0D);
		}
	}

	@Override
	public BlockState copyDirectionPropertiesTo(BlockState state, BlockState copyFromState) {
		return state.setValue(RollerDoorBlock.FACING, copyFromState.getValue(RollerDoorBlock.FACING)).setValue(RollerDoorBlock.FACE, copyFromState.getValue(RollerDoorBlock.FACE));
	}
}