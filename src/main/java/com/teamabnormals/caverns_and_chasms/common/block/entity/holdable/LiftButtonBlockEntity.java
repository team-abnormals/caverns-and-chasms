package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.LiftButtonBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class LiftButtonBlockEntity extends BlockEntity {
	private int holdTime;
	private int timePressed;

	public LiftButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.LIFT_BUTTON.get(), pos, state);
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.holdTime = tag.getShort("HoldTime");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putShort("HoldTime", (short) this.holdTime);
	}

	public void setHeld() {
		this.holdTime = 4;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, LiftButtonBlockEntity blockEntity) {
		if (!level.isClientSide) {
			if (state.getValue(LiftButtonBlock.PRESSED)) {
				blockEntity.timePressed++;
				level.blockUpdated(pos, state.getBlock());
			} else if (blockEntity.timePressed != 0) {
				blockEntity.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}

			if (blockEntity.holdTime > 0) {
				--blockEntity.holdTime;
			} else if (state.getValue(LiftButtonBlock.PRESSED)) {
				LiftButtonBlock liftButtonBlock = (LiftButtonBlock) state.getBlock();
				level.setBlock(pos, state.setValue(LiftButtonBlock.PRESSED, false).setValue(LiftButtonBlock.POWERED, true), 3);
				liftButtonBlock.updateNeighbours(state, level, pos);
				level.scheduleTick(new BlockPos(pos), state.getBlock(), 8);
				level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.get().buttonClickOff(), SoundSource.BLOCKS);
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
			}
			if (state.getValue(LiftButtonBlock.PRESSED) && level.getGameTime() % 2 == 0) {
				level.playSound(null, pos, CCSoundEvents.TIN_BUTTON_HOLD.get(), SoundSource.BLOCKS);
			}
		}
	}
}