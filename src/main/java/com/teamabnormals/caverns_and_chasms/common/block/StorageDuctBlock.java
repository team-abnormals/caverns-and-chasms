package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class StorageDuctBlock extends BaseEntityBlock {
	public static final DirectionProperty START_FACE = DirectionProperty.create("start_face", Direction.values());
	public static final DirectionProperty END_FACE = DirectionProperty.create("end_face", Direction.values());
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public StorageDuctBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(START_FACE, Direction.UP).setValue(END_FACE, Direction.DOWN).setValue(OPEN, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageDuctBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	/*
	public static Container getContainer(ChestBlock p_51512_, BlockState p_51513_, Level p_51514_, BlockPos p_51515_, boolean p_51516_) {
		return ;
	}
	*/

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof StorageDuctBlockEntity storageDuct && canOpen(level, pos, state)) {
			if (!level.isClientSide) {
				player.openMenu(storageDuct);
				PiglinAi.angerNearbyPiglins(player, true);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof Container) {
				Containers.dropContents(level, pos, (Container) blockentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Direction startFace = context.getClickedFace().getOpposite();
		Direction endFace = startFace.getOpposite();

		if (hasNeighborConnectedToFace(startFace, level, blockPos)) {
			for (Direction face : Direction.values()) {
				if (face != startFace && hasNeighborConnectedToFace(face, level, blockPos)) {
					endFace = face;
					break;
				}
			}
		}

		return this.defaultBlockState().setValue(START_FACE, startFace).setValue(END_FACE, endFace);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (facing != state.getValue(START_FACE) && !hasEndConnection(level, pos, state) && facingState.getBlock() instanceof StorageDuctBlock && hasFace(facing.getOpposite(), facingState))
			return state.setValue(END_FACE, facing);
		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	public static boolean hasStartConnection(LevelAccessor level, BlockPos pos, BlockState state) {
		return hasNeighborConnectedToFace(state.getValue(START_FACE), level, pos);
	}

	public static boolean hasEndConnection(LevelAccessor level, BlockPos pos, BlockState state) {
		return hasNeighborConnectedToFace(state.getValue(END_FACE), level, pos);
	}

	public static boolean hasNeighborConnectedToFace(Direction face, LevelAccessor level, BlockPos pos) {
		BlockState neighborState = level.getBlockState(pos.relative(face));
		return neighborState.getBlock() instanceof StorageDuctBlock && hasFace(face.getOpposite(), neighborState);
	}

	public static boolean hasFace(Direction face, BlockState state) {
		return state.getValue(START_FACE) == face || state.getValue(END_FACE) == face;
	}

	public static boolean canOpen(Level level, BlockPos pos, BlockState state) {
		Direction startFace = state.getValue(START_FACE);
		Direction endFace = state.getValue(END_FACE);
		BlockPos startFacePos = pos.relative(startFace);
		BlockPos endFacePos = pos.relative(endFace);
		return !level.getBlockState(startFacePos).isFaceSturdy(level, startFacePos, startFace.getOpposite()) || !level.getBlockState(endFacePos).isFaceSturdy(level, endFacePos, endFace.getOpposite());
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(START_FACE, rotation.rotate(state.getValue(START_FACE))).setValue(END_FACE, rotation.rotate(state.getValue(END_FACE)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(START_FACE, mirror.getRotation(state.getValue(START_FACE)).rotate(state.getValue(START_FACE))).setValue(END_FACE, mirror.getRotation(state.getValue(END_FACE)).rotate(state.getValue(END_FACE)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(START_FACE, END_FACE, OPEN);
	}
}
