package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.HoldButtonBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class HoldButtonBlockEntity extends BlockEntity {
	private int pressTime;

	public HoldButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_BUTTON.get(), pos, state);
	}

	public void setPressed() {
		this.pressTime = 4;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldButtonBlockEntity blockEntity) {
		if (blockEntity.pressTime > 0) {
			--blockEntity.pressTime;
		} else if (state.getValue(HoldButtonBlock.PRESSED)) {
			HoldButtonBlock holdButtonBlock = (HoldButtonBlock) state.getBlock();
			level.setBlock(pos, state.setValue(HoldButtonBlock.PRESSED, false).setValue(HoldButtonBlock.POWERED, true), 3);
			holdButtonBlock.updateNeighbours(state, level, pos);
			level.scheduleTick(new BlockPos(pos), state.getBlock(), 4);
			level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.buttonClickOff(), SoundSource.BLOCKS);
			level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
		}
	}
}
