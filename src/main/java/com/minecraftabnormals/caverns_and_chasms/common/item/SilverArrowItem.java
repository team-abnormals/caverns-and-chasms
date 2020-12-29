package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.common.entity.SilverArrowEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SilverArrowItem extends ArrowItem {

	public SilverArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
		return new SilverArrowEntity(worldIn, shooter);
	}

	public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
		int enchant = EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantments.INFINITY, bow);
		return enchant > 0 && this.getClass() == SilverArrowItem.class;
	}
}
