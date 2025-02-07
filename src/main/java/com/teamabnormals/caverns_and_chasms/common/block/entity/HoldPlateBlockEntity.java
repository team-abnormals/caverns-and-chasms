package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.HoldButtonBlock;
import com.teamabnormals.caverns_and_chasms.common.block.HoldPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class HoldPlateBlockEntity extends BlockEntity {
	private boolean pressed;

	public HoldPlateBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_PLATE.get(), pos, state);
	}

	public void setPressed() {
		this.pressed = true;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldPlateBlockEntity blockEntity) {
		if (blockEntity.pressed) {
			blockEntity.pressed = false;
		} else if (state.getValue(HoldButtonBlock.PRESSED)) {
			HoldPlateBlock holdPlateBlock = (HoldPlateBlock) state.getBlock();
			level.setBlock(pos, state.setValue(HoldButtonBlock.PRESSED, false).setValue(HoldButtonBlock.POWERED, true), 3);
			holdPlateBlock.updateNeighbours(level, pos);
			level.scheduleTick(new BlockPos(pos), state.getBlock(), 4);
			level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.pressurePlateClickOff(), SoundSource.BLOCKS);
			level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
		}
	}
}
