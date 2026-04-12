package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorMaterial;

public class CCArmorMaterialTags {
	public static final TagKey<ArmorMaterial> COPPER = armorMaterialTag("copper");

	private static TagKey<ArmorMaterial> armorMaterialTag(String name) {
		return armorMaterialTag(CavernsAndChasms.MOD_ID, name);
	}

	private static TagKey<ArmorMaterial> armorMaterialTag(String modid, String name) {
		return TagKey.create(Registries.ARMOR_MATERIAL, ResourceLocation.fromNamespaceAndPath(modid, name));
	}
}