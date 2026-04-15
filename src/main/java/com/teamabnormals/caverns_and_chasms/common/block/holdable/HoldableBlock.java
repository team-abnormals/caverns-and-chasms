package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.HoldableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public interface HoldableBlock {
	default void setHeld(Level level, HoldableBlockEntity blockEntity) {
		if (!level.isClientSide) {
			blockEntity.setHeld();
		} else if (this.shouldResetRightClickDelay()) {
			Minecraft.getInstance().rightClickDelay = 0;
		}
	}

	default boolean shouldResetRightClickDelay() {
		return true;
	}
}