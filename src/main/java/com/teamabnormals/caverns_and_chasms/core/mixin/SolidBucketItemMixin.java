package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SolidBucketItem.class)
public abstract class SolidBucketItemMixin extends BlockItem {

	public SolidBucketItemMixin(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (getBlock() == Blocks.POWDER_SNOW && this.allowedIn(tab)) {
			items.add(new ItemStack(this));
		}
	}
}