package com.teamabnormals.caverns_and_chasms.common.item.necromium;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class NecromiumAxeItem extends AxeItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(CCItems.NECROMIUM_PICKAXE);

	public NecromiumAxeItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
