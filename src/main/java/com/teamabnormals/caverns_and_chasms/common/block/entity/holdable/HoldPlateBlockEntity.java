package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.HoldPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HoldPlateBlockEntity extends BlockEntity {
	private int signalTimer;
	private int signal;

	public HoldPlateBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_PRESSURE_PLATE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldPlateBlockEntity blockEntity) {
		if (!level.isClientSide) {
			if (state.getValue(HoldPlateBlock.POWERED)) {
				if (--blockEntity.signalTimer <= 0) {
					blockEntity.signal = Math.min(blockEntity.signal + 1, 15);
					blockEntity.signalTimer = HoldPlateBlock.getOutputSpeed(level.getBlockState(pos.below()));
					HoldPlateBlock block = (HoldPlateBlock) state.getBlock();
					block.updateNeighbours(level, pos);
				}

				if (level.getGameTime() % 2 == 0) {
					level.playSound(null, pos, CCSoundEvents.TIN_PRESSURE_PLATE_HOLD.get(), SoundSource.BLOCKS);
				}
			} else if (blockEntity.signal > 0) {
				blockEntity.signalTimer = 0;
				blockEntity.signal = 0;
			}
		}
	}

	public int getSignal() {
		return this.signal;
	}
}