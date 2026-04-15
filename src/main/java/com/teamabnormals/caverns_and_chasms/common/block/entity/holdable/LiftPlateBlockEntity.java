package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.LiftPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LiftPlateBlockEntity extends BlockEntity {
	private int timePressed;

	public LiftPlateBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.LIFT_PLATE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, LiftPlateBlockEntity blockEntity) {
		if (!level.isClientSide) {
			// TODO: Should probably update the block below too like in HoldPlateBlock.updateNeighbors
			if (state.getValue(LiftPlateBlock.PRESSED)) {
				blockEntity.timePressed++;
				level.blockUpdated(pos, state.getBlock());
				if (level.getGameTime() % 2 == 0) {
					level.playSound(null, pos, CCSoundEvents.TIN_PRESSURE_PLATE_HOLD.get(), SoundSource.BLOCKS);
				}
			} else if (blockEntity.timePressed != 0) {
				blockEntity.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}

			LiftPlateBlock liftPlateBlock = (LiftPlateBlock) state.getBlock();
			liftPlateBlock.deactivate(null, level, pos, state);
		}
	}
}