package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.common.entity.SilverArrow;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SilverArrowItem extends ArrowItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.SPECTRAL_ARROW);

	public SilverArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
		return new SilverArrow(worldIn, shooter);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
		return false;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
