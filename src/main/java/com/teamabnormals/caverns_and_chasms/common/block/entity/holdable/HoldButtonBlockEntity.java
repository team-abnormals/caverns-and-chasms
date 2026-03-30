package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.HoldButtonBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class HoldButtonBlockEntity extends BlockEntity {
	private int holdTime;
	private int timePressed;

	public int getTimePressed() {
		return this.timePressed;
	}

	public HoldButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_BUTTON.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.holdTime = compound.getShort("HoldTime");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putShort("HoldTime", (short) this.holdTime);
	}

	public void setHeld() {
		this.holdTime = 2;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldButtonBlockEntity blockEntity) {
		if (!level.isClientSide) {
			if (state.getValue(HoldButtonBlock.PRESSED)) {
				blockEntity.timePressed++;
				level.blockUpdated(pos, state.getBlock());
			} else if (blockEntity.timePressed != 0) {
				blockEntity.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}

			if (blockEntity.holdTime > 0) {
				--blockEntity.holdTime;
			} else if (state.getValue(HoldButtonBlock.PRESSED)) {
				HoldButtonBlock holdButtonBlock = (HoldButtonBlock) state.getBlock();
				level.setBlock(pos, state.setValue(HoldButtonBlock.PRESSED, false).setValue(HoldButtonBlock.POWERED, true), 3);
				holdButtonBlock.updateNeighbours(state, level, pos);
				level.scheduleTick(new BlockPos(pos), state.getBlock(), 8);
				level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.get().buttonClickOff(), SoundSource.BLOCKS);
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
			}
			if (state.getValue(HoldButtonBlock.PRESSED) && level.getGameTime() % 2 == 0) {
				level.playSound(null, pos, CCSoundEvents.TIN_BUTTON_HOLD.get(), SoundSource.BLOCKS);
			}
		}
	}
}