package com.minecraftabnormals.caverns_and_chasms.common.enchantment;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class ProspectingEnchantment extends Enchantment {

	public ProspectingEnchantment() {
		super(Rarity.RARE, CCEnchantments.ORE_DETECTOR, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return !(ench instanceof TreasuringEnchantment) && super.checkCompatibility(ench);
	}
}
