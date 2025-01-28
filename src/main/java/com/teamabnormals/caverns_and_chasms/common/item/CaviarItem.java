package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CaviarItem extends Item {

	public CaviarItem(Properties properties) {
		super(properties);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack copy = stack.copy();
		super.finishUsingItem(stack, level, entity);

		if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(this, 400);
		}

		return copy;
	}
}