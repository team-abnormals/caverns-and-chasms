package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractHoldableButtonBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LiftButtonBlockEntity extends AbstractHoldableBlockEntity {
	public LiftButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.LIFT_BUTTON.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, LiftButtonBlockEntity blockEntity) {
		if (!level.isClientSide) {
			AbstractHoldableButtonBlock holdableButton = (AbstractHoldableButtonBlock) state.getBlock();
			if (blockEntity.holdTime > 0) {
				--blockEntity.holdTime;
			} else if (state.getValue(AbstractHoldableButtonBlock.POWERED)) {
				holdableButton.deactivate(state, level, pos, null);
			}
		}
	}
}