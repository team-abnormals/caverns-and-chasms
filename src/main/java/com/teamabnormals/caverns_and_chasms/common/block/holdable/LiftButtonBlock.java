package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.LiftButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class LiftButtonBlock extends ButtonBlock implements EntityBlock, HoldableBlock {
	public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");
	protected final WeatherState weatherState;

	public LiftButtonBlock(WeatherState weatherState, int ticks, Properties properties) {
		super(CCProperties.COPPER_BLOCK_SET.get(), ticks, properties);
		this.registerDefaultState(this.defaultBlockState().setValue(PRESSED, false));
		this.weatherState = weatherState;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiftButtonBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return HoldButtonBlock.createTickerHelper(entityType, CCBlockEntityTypes.LIFT_BUTTON.get(), LiftButtonBlockEntity::tick);
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
				return switch (direction) {
					case EAST -> flag ? PRESSED_EAST_AABB : EAST_AABB;
					case WEST -> flag ? PRESSED_WEST_AABB : WEST_AABB;
					case SOUTH -> flag ? PRESSED_SOUTH_AABB : SOUTH_AABB;
					case NORTH, UP, DOWN -> flag ? PRESSED_NORTH_AABB : NORTH_AABB;
				};
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
	public void press(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof LiftButtonBlockEntity liftButton) {
			liftButton.setHeld();
			if (!state.getValue(PRESSED)) {
				level.setBlock(pos, state.setValue(PRESSED, true), 3);
				level.playSound(player, pos, CCProperties.TIN_BLOCK_SET.get().buttonClickOn(), SoundSource.BLOCKS);
				level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
			}
		}
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof LiftButtonBlockEntity holdButtonBlockEntity) {
			holdButtonBlockEntity.setHeld();
			if (!state.getValue(PRESSED)) {
				level.setBlock(pos, state.setValue(PRESSED, true), 3);
				level.playSound(player, pos, CCProperties.TIN_BLOCK_SET.get().buttonClickOn(), SoundSource.BLOCKS);
				level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	public void updateNeighbours(BlockState state, Level level, BlockPos pos) {
		level.updateNeighborsAt(pos, this);
		level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, PRESSED, FACE);
	}
}