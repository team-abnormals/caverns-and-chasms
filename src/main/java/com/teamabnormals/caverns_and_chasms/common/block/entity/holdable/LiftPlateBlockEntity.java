package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.LiftPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

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
			} else if (blockEntity.timePressed != 0) {
				blockEntity.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}

			LiftPlateBlock liftPlateBlock = (LiftPlateBlock) state.getBlock();
			if (state.getValue(LiftPlateBlock.PRESSED) && liftPlateBlock.getSignalStrength(level, pos) == 0) {
				BlockState blockState = state.setValue(LiftPlateBlock.PRESSED, false).setValue(LiftPlateBlock.POWERED, true);
				level.setBlock(pos, blockState, 2);
				level.setBlocksDirty(pos, state, blockState);
				liftPlateBlock.updateNeighbours(level, pos);
				level.scheduleTick(new BlockPos(pos), state.getBlock(), 8);
				level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.get().pressurePlateClickOff(), SoundSource.BLOCKS);
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
			}
			if (state.getValue(LiftPlateBlock.PRESSED) && level.getGameTime() % 2 == 0) {
				level.playSound(null, pos, CCSoundEvents.TIN_PRESSURE_PLATE_HOLD.get(), SoundSource.BLOCKS);
			}
		}
	}
}