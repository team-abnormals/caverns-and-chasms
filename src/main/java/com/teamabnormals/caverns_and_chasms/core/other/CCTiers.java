package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.api.BlueprintItemTier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial.Layer;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class CCTiers {

	public static class CCArmorMaterials {
		public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, CavernsAndChasms.MOD_ID);

		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER = registerCopper("copper", Tags.Items.INGOTS_COPPER);
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXPOSED_COPPER = registerCopper("exposed_copper", CCItemTags.INGOTS_EXPOSED_COPPER);
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WEATHERED_COPPER = registerCopper("weathered_copper", CCItemTags.INGOTS_WEATHERED_COPPER);
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OXIDIZED_COPPER = registerCopper("oxidized_copper", CCItemTags.INGOTS_OXIDIZED_COPPER);

		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SILVER = register("silver", defense(2, 4, 6, 2, 9), 17, CCSoundEvents.ARMOR_EQUIP_SILVER, 0.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NECROMIUM = register("necromium", defense(3, 6, 8, 3, 12), 15, CCSoundEvents.ARMOR_EQUIP_NECROMIUM, 2.0F, 0.0F, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SANGUINE = register("sanguine", defense(2, 5, 7, 3, 7), 17, CCSoundEvents.ARMOR_EQUIP_SANGUINE, 1.0F, 0.0F, () -> Ingredient.of(CCItems.LIVING_FLESH.get()));

		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COWL = register("cowl", defense(1, 2, 3, 1, 3), 15, CCSoundEvents.ARMOR_EQUIP_COWL, 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER));
		public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TOOLBELT = register("toolbelt", defense(1, 2, 3, 1, 3), 15, CCSoundEvents.ARMOR_EQUIP_TOOLBELT, 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER));

		public static DeferredHolder<ArmorMaterial, ArmorMaterial> registerCopper(String name, TagKey<Item> repairTag) {
			return register(name, defense(1, 4, 5, 2, 15), 8, CCSoundEvents.ARMOR_EQUIP_COPPER, 0.0F, 0.05F, () -> Ingredient.of(repairTag));
		}

		public static EnumMap<Type, Integer> defense(int boots, int leggings, int chestplate, int helmet, int body) {
			return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, boots);
				map.put(ArmorItem.Type.LEGGINGS, leggings);
				map.put(ArmorItem.Type.CHESTPLATE, chestplate);
				map.put(ArmorItem.Type.HELMET, helmet);
				map.put(ArmorItem.Type.BODY, body);
			});
		}

		private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, EnumMap<Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
			List<Layer> list = List.of(new ArmorMaterial.Layer(CavernsAndChasms.location(name)));
			return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
		}

		private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngridient, List<ArmorMaterial.Layer> layers) {
			EnumMap<Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);

			for (ArmorItem.Type type : ArmorItem.Type.values()) {
				map.put(type, defense.get(type));
			}

			return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(map, enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance));
		}
	}

	public static class CCItemTiers {
		public static final Tier COPPER = createCopperTier(Tags.Items.INGOTS_COPPER);
		public static final Tier EXPOSED_COPPER = createCopperTier(CCItemTags.INGOTS_EXPOSED_COPPER);
		public static final Tier WEATHERED_COPPER = createCopperTier(CCItemTags.INGOTS_WEATHERED_COPPER);
		public static final Tier OXIDIZED_COPPER = createCopperTier(CCItemTags.INGOTS_OXIDIZED_COPPER);

		public static final Tier SILVER = new BlueprintItemTier(CCBlockTags.INCORRECT_FOR_SILVER_TOOL, 157, 9.0F, 1.0F, 18, () -> Ingredient.of(CCItemTags.INGOTS_SILVER));
		public static final Tier NECROMIUM = new BlueprintItemTier(CCBlockTags.INCORRECT_FOR_NECROMIUM_TOOL, 2031, 9.0F, 3.0F, 15, () -> Ingredient.of(CCItemTags.INGOTS_NECROMIUM));

		public static BlueprintItemTier createCopperTier(TagKey<Item> repairTag) {
			return new BlueprintItemTier(CCBlockTags.INCORRECT_FOR_COPPER_TOOL, 191 + 3000, 5.0F, 1.0F, 13, () -> Ingredient.of(repairTag));
		}
	}
}