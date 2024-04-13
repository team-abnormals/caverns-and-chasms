package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.api.BlueprintArmorMaterial;
import com.teamabnormals.blueprint.core.api.BlueprintItemTier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CCTiers {

	public static class CCArmorMaterials {
		public static final ArmorMaterial SILVER = new BlueprintArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "silver"), 10, new int[]{1, 3, 5, 2}, 20, () -> SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final ArmorMaterial NECROMIUM = new BlueprintArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "necromium"), 37, new int[]{3, 6, 8, 3}, 13, () -> SoundEvents.ARMOR_EQUIP_NETHERITE, 2.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));
		public static final ArmorMaterial SANGUINE = new BlueprintArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "sanguine"), 15, new int[]{2, 5, 6, 2}, 12, () -> SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.of(CCItems.LIVING_FLESH.get()));
	}

	public static class CCItemTiers {
		public static final Tier SILVER = new BlueprintItemTier(0, 111, 7.0F, 0.0F, 17, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final Tier NECROMIUM = new BlueprintItemTier(4, 2031, 9.0F, 3.0F, 13, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));
	}
}