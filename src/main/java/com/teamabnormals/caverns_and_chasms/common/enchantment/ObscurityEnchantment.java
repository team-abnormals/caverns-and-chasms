package com.teamabnormals.caverns_and_chasms.common.enchantment;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ObscurityEnchantment extends Enchantment {

	public ObscurityEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slot) {
		super(rarity, CCEnchantments.COWL, slot);
	}

	public int getMinCost(int p_45223_) {
		return 25;
	}

	public int getMaxCost(int p_45227_) {
		return 50;
	}

	@Override
	public boolean checkCompatibility(Enchantment enchantment) {
		return super.checkCompatibility(enchantment) && enchantment != CCEnchantments.CONCEAL.get();
	}
}