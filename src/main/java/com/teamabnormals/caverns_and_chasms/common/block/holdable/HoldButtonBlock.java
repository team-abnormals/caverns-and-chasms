package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.HoldButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class HoldButtonBlock extends ButtonBlock implements EntityBlock, HoldableBlock {

	public HoldButtonBlock(BlockSetType type, Properties properties) {
		super(type, 20, properties);
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

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
		return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof HoldButtonBlockEntity holdButtonBlockEntity) {
			holdButtonBlockEntity.setHeld();
			if (!state.getValue(POWERED)) {
				level.setBlock(pos, state.setValue(POWERED, true), 3);
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
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? this.getSignalBasedOnTime(state, level, pos) : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) && getConnectedDirection(state) == direction ? this.getSignalBasedOnTime(state, level, pos) : 0;
	}

	public int getSignalBasedOnTime(BlockState state, BlockGetter level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof HoldButtonBlockEntity blockEntity && blockEntity.getTimePressed() > 0) {
			return Math.min(1 + blockEntity.getTimePressed() / HoldPlateBlock.getOutputSpeed(level.getBlockState(pos.relative(getConnectedDirection(state).getOpposite()))), 15);
		}
		return 0;
	}
}