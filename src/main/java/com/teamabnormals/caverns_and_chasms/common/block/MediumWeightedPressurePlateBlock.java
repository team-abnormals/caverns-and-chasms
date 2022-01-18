package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;

public class MediumWeightedPressurePlateBlock extends WeightedPressurePlateBlock {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.LIGHT_WEIGHTED_PRESSURE_PLATE);

	public MediumWeightedPressurePlateBlock(Properties properties) {
		super(50, properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this.asItem(), group, items);
	}
}