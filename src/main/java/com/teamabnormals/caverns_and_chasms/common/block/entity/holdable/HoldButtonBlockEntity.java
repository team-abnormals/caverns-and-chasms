package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractHoldableButtonBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.HoldPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HoldButtonBlockEntity extends AbstractHoldableBlockEntity {
	private int signalTimer;
	private int signal;

	public HoldButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_BUTTON.get(), pos, state);
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.signal = tag.getShort("Signal");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putShort("Signal", (short) this.signal);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldButtonBlockEntity blockEntity) {
		if (!level.isClientSide) {
			AbstractHoldableButtonBlock block = (AbstractHoldableButtonBlock) state.getBlock();
			if (blockEntity.holdTime > 0) {
				--blockEntity.holdTime;

				if (--blockEntity.signalTimer <= 0) {
					blockEntity.signal = Math.min(blockEntity.signal + 1, 15);
					blockEntity.signalTimer = HoldPlateBlock.getOutputSpeed(level.getBlockState(pos.relative(getConnectedDirection(state).getOpposite())));
					block.updateNeighbours(state, level, pos);
				}

				if (level.getGameTime() % 2 == 0) {
					level.playSound(null, pos, CCSoundEvents.TIN_BUTTON_HOLD.get(), SoundSource.BLOCKS);
				}
			} else if (state.getValue(AbstractHoldableButtonBlock.POWERED)) {
				block.deactivate(state, level, pos, null);
				blockEntity.signalTimer = 0;
				blockEntity.signal = 0;
			}
		}
	}

	public int getSignal() {
		return this.signal;
	}

	protected static Direction getConnectedDirection(BlockState state) {
		return switch (state.getValue(ButtonBlock.FACE)) {
			case CEILING -> Direction.DOWN;
			case FLOOR -> Direction.UP;
			default -> state.getValue(ButtonBlock.FACING);
		};
	}
}