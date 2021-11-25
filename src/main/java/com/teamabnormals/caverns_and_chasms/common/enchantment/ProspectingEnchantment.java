package com.teamabnormals.caverns_and_chasms.common.enchantment;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ProspectingEnchantment extends Enchantment {

	public ProspectingEnchantment() {
		super(Rarity.RARE, CCEnchantments.ORE_DETECTOR, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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