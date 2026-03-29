package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

public class TinCanItem extends BundleItem {

	public TinCanItem(Properties properties) {
		super(properties);
	}

	public record TinCanTooltip(NonNullList<ItemStack> items, int weight) implements TooltipComponent {
	}
}
