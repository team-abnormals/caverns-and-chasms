package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.item.AbnormalsArmorMaterial;
import com.minecraftabnormals.caverns_and_chasms.common.item.AbnormalsItemTier;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

public class CCTiers {

	public static class Armor {
		public static final IArmorMaterial SILVER = new AbnormalsArmorMaterial(new ResourceLocation(CavernsAndChasms.MODID, "silver"), 10, new int[]{1, 3, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.fromItems(CCItems.SILVER_INGOT.get()));
		public static final IArmorMaterial NECROMIUM = new AbnormalsArmorMaterial(new ResourceLocation(CavernsAndChasms.MODID, "necromium"), 39, new int[]{3, 6, 8, 3}, 13, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2.0F, 0.0F, () -> Ingredient.fromItems(CCItems.NECROMIUM_INGOT.get()));
	}

	public static class Tools {
		public static final IItemTier SILVER = new AbnormalsItemTier(0, 111, 7.0F, 0.0F, 17, () -> Ingredient.fromItems(CCItems.SILVER_INGOT.get()));
		public static final IItemTier NECROMIUM = new AbnormalsItemTier(4, 2251, 9.0F, 4.0F, 11, () -> Ingredient.fromItems(CCItems.NECROMIUM_INGOT.get()));
	}
}