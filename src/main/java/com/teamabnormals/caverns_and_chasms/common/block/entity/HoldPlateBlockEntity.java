package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.HoldButtonBlock;
import com.teamabnormals.caverns_and_chasms.common.block.HoldPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.pressed = compound.getBoolean("Pressed");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putBoolean("Pressed", this.pressed);
	}

	public void setPressed() {
		this.pressed = true;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldPlateBlockEntity blockEntity) {
		if (!level.isClientSide) {
			if (blockEntity.pressed) {
				blockEntity.pressed = false;
			} else if (state.getValue(HoldButtonBlock.PRESSED)) {
				BlockState blockState = state.setValue(HoldButtonBlock.PRESSED, false).setValue(HoldButtonBlock.POWERED, true);
				level.setBlock(pos, blockState, 2);
				level.setBlocksDirty(pos, state, blockState);
				((HoldPlateBlock) state.getBlock()).updateNeighbours(level, pos);
				level.scheduleTick(new BlockPos(pos), state.getBlock(), 8);
				level.playSound(null, pos, CCProperties.TIN_BLOCK_SET.pressurePlateClickOff(), SoundSource.BLOCKS);
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
			}
		}
	}
}
