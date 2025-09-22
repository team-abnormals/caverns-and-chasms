package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.api.BlueprintArmorMaterial;
import com.teamabnormals.blueprint.core.api.BlueprintItemTier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class CCTiers {

	public static class CCArmorMaterials {
		public static final ArmorMaterial COPPER = createCopperMaterial("copper");
		public static final ArmorMaterial EXPOSED_COPPER = createCopperMaterial("exposed_copper");
		public static final ArmorMaterial WEATHERED_COPPER = createCopperMaterial("weathered_copper");
		public static final ArmorMaterial OXIDIZED_COPPER = createCopperMaterial("oxidized_copper");

		public static final ArmorMaterial SILVER = new BlueprintArmorMaterial(CavernsAndChasms.location("silver"), 11, new int[]{2, 4, 6, 2}, 17, () -> CCSoundEvents.ARMOR_EQUIP_SILVER.get(), 0.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final ArmorMaterial NECROMIUM = new BlueprintArmorMaterial(CavernsAndChasms.location("necromium"), 37, new int[]{3, 6, 8, 3}, 15, () -> CCSoundEvents.ARMOR_EQUIP_NECROMIUM.get(), 2.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));
		public static final ArmorMaterial SANGUINE = new BlueprintArmorMaterial(CavernsAndChasms.location("sanguine"), 23, new int[]{2, 5, 7, 3}, 17, () -> CCSoundEvents.ARMOR_EQUIP_SANGUINE.get(), 1.0F, 0.0F, () -> Ingredient.of(CCItems.LIVING_FLESH.get()));

		public static final ArmorMaterial COWL = new BlueprintArmorMaterial(CavernsAndChasms.location("cowl"), 5, new int[]{1, 2, 3, 1}, 15, () -> CCSoundEvents.ARMOR_EQUIP_COWL.get(), 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER));

		public static BlueprintArmorMaterial createCopperMaterial(String name) {
			return new BlueprintArmorMaterial(CavernsAndChasms.location(name), 11 + 45, new int[]{2, 4, 5, 2}, 8, () -> CCSoundEvents.ARMOR_EQUIP_COPPER.get(), 0.0F, 0.0F, () -> Ingredient.of(Tags.Items.INGOTS_COPPER));
		}
	}

	public static class CCItemTiers {
		public static final Tier FOIL = new BlueprintItemTier(1, 1822, 12.0F, 0.0F, 18, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final Tier SILVER = new BlueprintItemTier(2, 157, 9.0F, 1.0F, 18, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final Tier COPPER = new BlueprintItemTier(1, 191 + 3000, 5.0F, 1.0F, 13, () -> Ingredient.of(Tags.Items.INGOTS_COPPER));
		public static final Tier NECROMIUM = new BlueprintItemTier(4, 2031, 9.0F, 3.0F, 15, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));
	}
}