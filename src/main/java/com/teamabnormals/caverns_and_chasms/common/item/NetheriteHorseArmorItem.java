package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class NetheriteHorseArmorItem extends HorseArmorItem {
	private final float knockbackResistance;

	public NetheriteHorseArmorItem(int armorValue, float knockbackResistance, String tierArmor, Properties builder) {
		super(armorValue, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_" + tierArmor + ".png"), builder);
		this.knockbackResistance = knockbackResistance;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}