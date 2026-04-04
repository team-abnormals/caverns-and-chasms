package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.RicochetArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RicochetArrowItem extends ArrowItem {

	public RicochetArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
		return new RicochetArrow(worldIn, shooter);
	}
}