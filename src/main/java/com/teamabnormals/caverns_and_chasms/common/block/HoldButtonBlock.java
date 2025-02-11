package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.HoldButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class HoldButtonBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");
	private static final VoxelShape CEILING_AABB_X = Block.box(6.0D, 14.0D, 5.0D, 10.0D, 16.0D, 11.0D);
	private static final VoxelShape CEILING_AABB_Z = Block.box(5.0D, 14.0D, 6.0D, 11.0D, 16.0D, 10.0D);
	private static final VoxelShape FLOOR_AABB_X = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 2.0D, 11.0D);
	private static final VoxelShape FLOOR_AABB_Z = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 2.0D, 10.0D);
	private static final VoxelShape NORTH_AABB = Block.box(5.0D, 6.0D, 14.0D, 11.0D, 10.0D, 16.0D);
	private static final VoxelShape SOUTH_AABB = Block.box(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 2.0D);
	private static final VoxelShape WEST_AABB = Block.box(14.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
	private static final VoxelShape EAST_AABB = Block.box(0.0D, 6.0D, 5.0D, 2.0D, 10.0D, 11.0D);
	private static final VoxelShape PRESSED_CEILING_AABB_X = Block.box(6.0D, 15.0D, 5.0D, 10.0D, 16.0D, 11.0D);
	private static final VoxelShape PRESSED_CEILING_AABB_Z = Block.box(5.0D, 15.0D, 6.0D, 11.0D, 16.0D, 10.0D);
	private static final VoxelShape PRESSED_FLOOR_AABB_X = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 1.0D, 11.0D);
	private static final VoxelShape PRESSED_FLOOR_AABB_Z = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 1.0D, 10.0D);
	private static final VoxelShape PRESSED_NORTH_AABB = Block.box(5.0D, 6.0D, 15.0D, 11.0D, 10.0D, 16.0D);
	private static final VoxelShape PRESSED_SOUTH_AABB = Block.box(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 1.0D);
	private static final VoxelShape PRESSED_WEST_AABB = Block.box(15.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
	private static final VoxelShape PRESSED_EAST_AABB = Block.box(0.0D, 6.0D, 5.0D, 1.0D, 10.0D, 11.0D);

	public HoldButtonBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(PRESSED, false).setValue(FACE, AttachFace.WALL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HoldButtonBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.HOLD_BUTTON.get(), HoldButtonBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(FACING);
		boolean flag = state.getValue(PRESSED);
		switch (state.getValue(FACE)) {
			case FLOOR:
				if (direction.getAxis() == Direction.Axis.X) {
					return flag ? PRESSED_FLOOR_AABB_X : FLOOR_AABB_X;
				}

				return flag ? PRESSED_FLOOR_AABB_Z : FLOOR_AABB_Z;
			case WALL:
				VoxelShape voxelshape;
				switch (direction) {
					case EAST:
						voxelshape = flag ? PRESSED_EAST_AABB : EAST_AABB;
						break;
					case WEST:
						voxelshape = flag ? PRESSED_WEST_AABB : WEST_AABB;
						break;
					case SOUTH:
						voxelshape = flag ? PRESSED_SOUTH_AABB : SOUTH_AABB;
						break;
					case NORTH:
					case UP:
					case DOWN:
						voxelshape = flag ? PRESSED_NORTH_AABB : NORTH_AABB;
						break;
					default:
						throw new IncompatibleClassChangeError();
				}

				return voxelshape;
			case CEILING:
			default:
				if (direction.getAxis() == Direction.Axis.X) {
					return flag ? PRESSED_CEILING_AABB_X : CEILING_AABB_X;
				} else {
					return flag ? PRESSED_CEILING_AABB_Z : CEILING_AABB_Z;
				}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canAttach(level, pos, getConnectedDirection(state).getOpposite());
	}

	public static boolean canAttach(LevelReader level, BlockPos pos, Direction direction) {
		BlockPos blockpos = pos.relative(direction);
		return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction.getOpposite());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED)) {
			level.setBlock(pos, state.setValue(POWERED, false), 3);
			this.updateNeighbours(state, level, pos);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof HoldButtonBlockEntity holdButtonBlockEntity) {
			holdButtonBlockEntity.setPressed();
			if (!state.getValue(PRESSED)) {
				level.setBlock(pos, state.setValue(PRESSED, true), 3);
				level.playSound(player, pos, CCProperties.TIN_BLOCK_SET.buttonClickOn(), SoundSource.BLOCKS);
				level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!isMoving && !state.is(newState.getBlock())) {
			if (state.getValue(POWERED)) {
				this.updateNeighbours(state, level, pos);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	public void updateNeighbours(BlockState state, Level level, BlockPos pos) {
		level.updateNeighborsAt(pos, this);
		level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		for (Direction direction : context.getNearestLookingDirections()) {
			BlockState blockstate;
			if (direction.getAxis() == Direction.Axis.Y) {
				blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection());
			} else {
				blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
			}

			if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
				return blockstate;
			}
		}

		return null;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		return getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, offsetState, level, pos, offsetPos);
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
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) && getConnectedDirection(state) == direction ? 15 : 0;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, PRESSED, FACE);
	}

	private static Direction getConnectedDirection(BlockState state) {
		switch (state.getValue(FACE)) {
			case CEILING:
				return Direction.DOWN;
			case FLOOR:
				return Direction.UP;
			default:
				return state.getValue(FACING);
		}
	}
}