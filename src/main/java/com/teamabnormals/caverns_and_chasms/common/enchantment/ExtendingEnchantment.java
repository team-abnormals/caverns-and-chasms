package com.teamabnormals.caverns_and_chasms.common.enchantment;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ExtendingEnchantment extends Enchantment {

	public ExtendingEnchantment(Rarity rarity, EquipmentSlot... slot) {
		super(rarity, CCEnchantments.TOOLBELT, slot);
	}

	public int getMinCost(int p_45121_) {
		return 10 * p_45121_;
	}

	public int getMaxCost(int p_45123_) {
		return this.getMinCost(p_45123_) + 30;
	}

	public int getMaxLevel() {
		return 2;
	}
}