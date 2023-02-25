package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;

public class SilverSwordItem extends SwordItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.GOLDEN_SWORD);

	public SilverSwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
