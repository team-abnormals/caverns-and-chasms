package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.RollerDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class RollerDoorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, HoldableBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public RollerDoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RollerDoorBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.ROLLER_DOOR.get(), RollerDoorBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		return blockentity instanceof RollerDoorBlockEntity rollerdoorentity ? rollerdoorentity.getDoorShape(state) : Shapes.empty();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		RollerDoorHeaderBlockEntity headerentity = findHeaderBlockEntity(level, state, pos);
		if (headerentity != null && level.getBlockEntity(pos) instanceof RollerDoorBlockEntity rollerdoorentity && !player.getItemInHand(hand).is(CCBlocks.ROLLER_DOOR.get().asItem()) && (rollerdoorentity.isBottom() || (rollerdoorentity.hasBottomBelow() && isHitResultInLiftArea(state, rollerdoorentity, pos, hitResult)))) {
			if (!level.isClientSide)
				headerentity.setBeingLifted();
			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
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
		if (clickedstate.getBlock() instanceof RollerDoorBlock) {
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

		BlockPos abovepos = blockpos.relative(getAboveDirection(facing, face));
		BlockState abovestate = level.getBlockState(abovepos);

		BlockState placestate = abovestate.getBlock() instanceof RollerDoorBlock && isDoorParallel(abovestate, facing, face) ? CCBlocks.ROLLER_DOOR.get().defaultBlockState() : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();

		return placestate.setValue(FACING, facing).setValue(FACE, face).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED))
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return state;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof RollerDoorBlockEntity blockentity)
			blockentity.onRemove(newState);

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
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	private static boolean isHitResultInLiftArea(BlockState state, RollerDoorBlockEntity rollerDoorEntity, BlockPos pos, BlockHitResult hitResult) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		Direction direction = getBelowDirection(facing, face);
		Axis axis = direction.getAxis();
		double d0 = hitResult.getLocation().get(axis) - pos.get(axis);
		if (direction.getAxisDirection() == AxisDirection.POSITIVE)
			d0 = 1.0D - d0;
		return d0 < rollerDoorEntity.getOpenness(1.0F);
	}

	public static RollerDoorHeaderBlockEntity findHeaderBlockEntity(LevelAccessor level, BlockState state, BlockPos pos) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		Direction direction = getAboveDirection(facing, face);
		MutableBlockPos mutable = pos.mutable();
		while (true) {
			BlockState offsetstate = level.getBlockState(mutable);
			if (!(offsetstate.getBlock() instanceof RollerDoorBlock) || !isDoorParallel(offsetstate, facing, face))
				return null;
			else if (level.getBlockEntity(mutable) instanceof RollerDoorHeaderBlockEntity rollerDoor)
				return rollerDoor;
			mutable.move(direction);
		}
	}

	public static Direction getAboveDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.UP : facing;
	}

	public static Direction getBelowDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.DOWN : facing.getOpposite();
	}

	public static Direction getLeftDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? facing.getClockWise() : facing.getCounterClockWise();
	}

	public static Direction getRightDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? facing.getCounterClockWise() : facing.getClockWise();
	}

	public static boolean isDoorParallel(BlockState neighborState, Direction facing, AttachFace face) {
		return neighborState.getValue(RollerDoorBlock.FACING) == facing && neighborState.getValue(RollerDoorBlock.FACE) == face;
	}

	public static int calculateColumnLength(Level level, BlockPos pos, Direction facing, AttachFace face) {
		int length = 0;

		MutableBlockPos mutable = pos.mutable();
		Direction belowdir = RollerDoorBlock.getBelowDirection(facing, face);

		while (true) {
			mutable.move(belowdir);
			BlockState offsetstate = level.getBlockState(mutable);

			if (offsetstate.is(CCBlocks.ROLLER_DOOR.get()) && offsetstate.getValue(RollerDoorBlock.FACING) == facing && offsetstate.getValue(RollerDoorBlock.FACE) == face)
				++length;
			else
				break;
		}

		return length;
	}
}