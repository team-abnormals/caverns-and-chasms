package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {

	@Accessor(value = "delegate", remap = false)
	Holder.Reference<Item> getDelegate();

	@Mutable
	@Accessor(value = "delegate", remap = false)
	void setDelegate(Holder.Reference<Item> delegate);
}