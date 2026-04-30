package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.HoldPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HoldPlateBlockEntity extends BlockEntity {
	private int timePressed;

	public int getTimePressed() {
		return this.timePressed;
	}

	public HoldPlateBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_PRESSURE_PLATE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldPlateBlockEntity blockEntity) {
		if (!level.isClientSide) {
			// TODO: Should probably update the block below too like in HoldPlateBlock.updateNeighbors
			if (state.getValue(HoldPlateBlock.POWERED)) {
				blockEntity.timePressed++;
				level.blockUpdated(pos, state.getBlock());
				if (level.getGameTime() % 2 == 0) {
					level.playSound(null, pos, CCSoundEvents.TIN_PRESSURE_PLATE_HOLD.get(), SoundSource.BLOCKS);
				}
			} else if (blockEntity.timePressed != 0) {
				blockEntity.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}
		}
	}
}