package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class CopperSwordItem extends SwordItem implements WeatheringCopperItem {

	public CopperSwordItem(Tier tier, int damage, float speed, Properties properties) {
		super(tier, damage, speed, properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean b) {
		WeatheringCopperItem.updateOxidation(stack, level);
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getOrCreateDescriptionId(stack);
	}
}
