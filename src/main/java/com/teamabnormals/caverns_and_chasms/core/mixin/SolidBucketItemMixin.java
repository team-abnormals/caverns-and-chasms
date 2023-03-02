package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SolidBucketItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SolidBucketItem.class)
public abstract class SolidBucketItemMixin extends Item {

	public SolidBucketItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			items.add(this.getDefaultInstance());
		}
	}
}