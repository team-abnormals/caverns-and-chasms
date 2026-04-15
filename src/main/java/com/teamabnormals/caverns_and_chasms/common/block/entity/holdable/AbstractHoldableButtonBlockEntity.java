package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractHoldableButtonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractHoldableButtonBlockEntity extends AbstractHoldableBlockEntity {
	private int timePressed;

	public AbstractHoldableButtonBlockEntity(BlockEntityType<?> blocKEntityType, BlockPos pos, BlockState state) {
		super(blocKEntityType, pos, state);
	}

	public int getTimePressed() {
		return this.timePressed;
	}

	public void handleTick(Level level, BlockPos pos, BlockState state) {
		if (!level.isClientSide) {
			if (state.getValue(AbstractHoldableButtonBlock.POWERED)) {
				this.timePressed++;
				level.blockUpdated(pos, state.getBlock());
				this.tickPowered(level, pos, state);
			} else if (this.timePressed != 0) {
				this.timePressed = 0;
				level.blockUpdated(pos, state.getBlock());
			}

			if (this.holdTime > 0) {
				--this.holdTime;
			} else if (state.getValue(AbstractHoldableButtonBlock.POWERED)) {
				AbstractHoldableButtonBlock holdableButton = (AbstractHoldableButtonBlock) state.getBlock();
				holdableButton.deactivate(state, level, pos, null);
			}
		}
	}

	protected void tickPowered(Level level, BlockPos pos, BlockState state) {
	}
}