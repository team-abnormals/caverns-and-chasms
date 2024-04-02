package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.LargeArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LargeArrowItem extends ArrowItem {

	public LargeArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
		LargeArrow largeArrow = new LargeArrow(worldIn, shooter);
		largeArrow.setBaseDamage(largeArrow.getBaseDamage());
		return largeArrow;
	}
}