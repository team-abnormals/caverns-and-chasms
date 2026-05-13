package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.AbstractHoldableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class AbstractHoldableButtonBlock extends ButtonBlock implements EntityBlock, HoldableBlock {

	public AbstractHoldableButtonBlock(BlockSetType type, int ticks, Properties properties) {
		super(type, ticks, properties);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof AbstractHoldableBlockEntity holdable) {
			this.setHeld(level, holdable);
			if (!state.getValue(POWERED)) {
				this.press(state, level, pos, player);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void press(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof AbstractHoldableBlockEntity holdable) {
			this.setHeld(level, holdable);
		}
		level.setBlock(pos, state.setValue(POWERED, true), 3);
		this.updateNeighbours(state, level, pos);
		this.playSound(player, level, pos, true);
		level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
	}

	public void deactivate(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		level.setBlock(pos, this.getDeactivationState(state), 3);
		this.updateNeighbours(state, level, pos);
		this.playSound(player, level, pos, false);
		level.gameEvent(player, GameEvent.BLOCK_DEACTIVATE, pos);
	}

	public BlockState getDeactivationState(BlockState state) {
		return state.setValue(POWERED, false);
	}
}