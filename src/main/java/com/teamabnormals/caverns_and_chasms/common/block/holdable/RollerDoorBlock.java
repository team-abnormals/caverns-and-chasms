package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.client.resources.sounds.MovingDoorMoveSoundInstance;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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

	public RollerDoorBlock(boolean isHeader, MovingDoorType defaultDoorType, Properties properties) {
		super(isHeader, defaultDoorType, properties);
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
	public AbstractMovingDoorBlock getNormalBlock() {
		return (AbstractMovingDoorBlock) CCBlocks.ROLLER_DOOR.get();
	}

	public AbstractMovingDoorBlock getHeaderBlock() {
		return (AbstractMovingDoorBlock) CCBlocks.ROLLER_DOOR_HEADER.get();
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
		double openness = blockEntity.isBottom() ? blockEntity.getOpenness() : 0.0D;

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

	public AABB calculatePushAABB(double openness, int doorsBelowCount, BlockState state, Vec3i moveVector) {
		Direction facing = state.getValue(FACING);
		AttachFace face = state.getValue(FACE);

		AABB aabb;
		if (face == AttachFace.WALL) {
			if (facing == Direction.EAST)
				aabb = new AABB(0.8125D, 0.0D, 0.0D, 0.9375D, 1.0D, 1.0D);
			else if (facing == Direction.WEST)
				aabb = new AABB(0.0625D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
			else if (facing == Direction.SOUTH)
				aabb = new AABB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 0.9375D);
			else
				aabb = new AABB(0.0D, 0.0D, 0.0625D, 1.0D, 1.0D, 0.1875D);
		} else if (face == AttachFace.FLOOR) {
			aabb = new AABB(0.0D, 0.8125D, 0.0D, 1.0D, 0.9375D, 1.0D);
		} else {
			aabb = new AABB(0.0D, 0.0625D, 0.0D, 1.0D, 0.1875D, 1.0D);
		}

		Vec3 vec3 = Vec3.atLowerCornerOf(moveVector);

		if (doorsBelowCount == 0) {
			Vec3 vec31 = vec3.scale(openness);
			return aabb.contract(vec31.x, vec31.y, vec31.z);
		} else {
			return aabb.expandTowards(vec3.scale(doorsBelowCount - openness));
		}
	}

	@Override
	public MovingDoorMoveSoundInstance createSoundInstance(MovingDoorHeaderBlockEntity headerEntity) {
		return new MovingDoorMoveSoundInstance(headerEntity, CCSoundEvents.ROLLER_DOOR_START_ROLL.get(), CCSoundEvents.ROLLER_DOOR_ROLL.get(), CCSoundEvents.ROLLER_DOOR_STOP_ROLL.get(), 12);
	}

	@Override
	public BlockState copyDirectionPropertiesTo(BlockState state, BlockState copyFromState) {
		return state.setValue(RollerDoorBlock.FACING, copyFromState.getValue(RollerDoorBlock.FACING)).setValue(RollerDoorBlock.FACE, copyFromState.getValue(RollerDoorBlock.FACE));
	}
}