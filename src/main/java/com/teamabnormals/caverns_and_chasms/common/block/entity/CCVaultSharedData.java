package com.teamabnormals.caverns_and_chasms.common.block.entity;

import net.minecraft.world.item.ItemStack;

public interface CCVaultSharedData {
	ItemStack getInsertStack();

	void setInsertStack(ItemStack lastUsedItem);
}
