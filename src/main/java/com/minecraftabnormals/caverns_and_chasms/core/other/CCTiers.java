package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class CCTiers {

	public static class Armor {
		public static final IArmorMaterial SILVER = new IArmorMaterial() {
			@Override
			public int getDamageReductionAmount(EquipmentSlotType slot) {
				return new int[]{2, 5, 6, 2}[slot.getIndex()];
			}

			@Override
			public int getDurability(EquipmentSlotType slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 10;
			}

			@Override
			public int getEnchantability() {
				return 20;
			}

			@Override
			public SoundEvent getSoundEvent() {
				return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return Ingredient.fromItems(CCItems.SILVER_INGOT.get());
			}

			@Override
			public String getName() {
				return CavernsAndChasms.MODID + ":silver";
			}

			@Override
			public float getToughness() {
				return 1.0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0;
			}
		};

		public static final IArmorMaterial NECROMIUM = new IArmorMaterial() {
			@Override
			public int getDamageReductionAmount(EquipmentSlotType slot) {
				return new int[]{2, 5, 6, 2}[slot.getIndex()];
			}

			@Override
			public int getDurability(EquipmentSlotType slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 10;
			}

			@Override
			public int getEnchantability() {
				return 20;
			}

			@Override
			public SoundEvent getSoundEvent() {
				return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return Ingredient.fromItems(CCItems.NECROMIUM_INGOT.get());
			}

			@Override
			public String getName() {
				return CavernsAndChasms.MODID + ":necromium";
			}

			@Override
			public float getToughness() {
				return 1.0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0;
			}
		};
	}

	public static class Tools {
		public static final IItemTier SILVER = new IItemTier() {
			@Override
			public float getAttackDamage() {
				return 0.0F;
			}

			@Override
			public float getEfficiency() {
				return 7.0F;
			}

			@Override
			public int getMaxUses() {
				return 125;
			}

			@Override
			public int getHarvestLevel() {
				return 2;
			}

			@Override
			public int getEnchantability() {
				return 17;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return Ingredient.fromItems(CCItems.SILVER_INGOT.get());
			}
		};

		public static final IItemTier NECROMIUM = new IItemTier() {
			@Override
			public float getAttackDamage() {
				return 0.0F;
			}

			@Override
			public float getEfficiency() {
				return 7.0F;
			}

			@Override
			public int getMaxUses() {
				return 125;
			}

			@Override
			public int getHarvestLevel() {
				return 2;
			}

			@Override
			public int getEnchantability() {
				return 17;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return Ingredient.fromItems(CCItems.NECROMIUM_INGOT.get());
			}
		};
	}

}
