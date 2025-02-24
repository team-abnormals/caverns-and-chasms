package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
