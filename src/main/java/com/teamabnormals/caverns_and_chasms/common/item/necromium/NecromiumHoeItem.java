package com.teamabnormals.caverns_and_chasms.common.item.necromium;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class NecromiumHoeItem extends HoeItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(CCItems.NECROMIUM_AXE);

	public NecromiumHoeItem(Tier tier, int maxDamage, float attackSpeedIn, Properties builder) {
		super(tier, maxDamage, attackSpeedIn, builder);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}