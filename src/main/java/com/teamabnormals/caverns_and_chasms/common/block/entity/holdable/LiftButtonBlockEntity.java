package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LiftButtonBlockEntity extends AbstractHoldableButtonBlockEntity {
	public LiftButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.LIFT_BUTTON.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, LiftButtonBlockEntity blockEntity) {
		blockEntity.handleTick(level, pos, state);
	}
}