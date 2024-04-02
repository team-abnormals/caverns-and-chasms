package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;

public class SilverHorseArmorItem extends HorseArmorItem {

	public SilverHorseArmorItem(Item.Properties builder) {
		super(7, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_silver.png"), builder);
	}
}