package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin implements DyeableLeatherItem {

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");
		return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
	}
}
