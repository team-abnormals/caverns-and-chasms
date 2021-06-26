package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.abnormals_core.core.api.AbnormalsArmorMaterial;
import com.minecraftabnormals.abnormals_core.core.api.AbnormalsItemTier;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

public class CCTiers {

	public static class Armor {
		public static final IArmorMaterial SILVER = new AbnormalsArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "silver"), 10, new int[]{1, 3, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(CCItems.SILVER_INGOT.get()));
		public static final IArmorMaterial NECROMIUM = new AbnormalsArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "necromium"), 37, new int[]{3, 6, 8, 3}, 13, SoundEvents.ARMOR_EQUIP_NETHERITE, 2.0F, 0.0F, () -> Ingredient.of(CCItems.NECROMIUM_INGOT.get()));
		public static final IArmorMaterial SANGUINE = new AbnormalsArmorMaterial(new ResourceLocation(CavernsAndChasms.MOD_ID, "sanguine"), 15, new int[]{2, 5, 6, 2}, 12, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.of(CCItems.SANGUINE_PLATING.get()));
	}

	public static class Tools {
		public static final IItemTier SILVER = new AbnormalsItemTier(0, 111, 7.0F, 0.0F, 17, () -> Ingredient.of(CCItems.SILVER_INGOT.get()));
		public static final IItemTier NECROMIUM = new AbnormalsItemTier(4, 2031, 9.0F, 3.0F, 13, () -> Ingredient.of(CCItems.NECROMIUM_INGOT.get()));
	}
}