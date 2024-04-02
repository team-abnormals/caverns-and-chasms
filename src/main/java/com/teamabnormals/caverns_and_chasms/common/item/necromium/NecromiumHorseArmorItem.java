package com.teamabnormals.caverns_and_chasms.common.item.necromium;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;

public class NecromiumHorseArmorItem extends HorseArmorItem {

	public NecromiumHorseArmorItem(int armorValue, String tierArmor, Item.Properties builder) {
		super(armorValue, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_" + tierArmor + ".png"), builder);
	}
}