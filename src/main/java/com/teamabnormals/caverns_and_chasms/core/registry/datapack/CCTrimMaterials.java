package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class CCTrimMaterials {
	public static final ResourceKey<TrimMaterial> SPINEL = createKey("spinel");
	public static final ResourceKey<TrimMaterial> ZIRCONIA = createKey("zirconia");
	public static final ResourceKey<TrimMaterial> SILVER = createKey("silver");
	public static final ResourceKey<TrimMaterial> SILVER_DARKER = createKey("silver_darker");
	public static final ResourceKey<TrimMaterial> TIN = createKey("tin");
	public static final ResourceKey<TrimMaterial> TURQUOISE = createKey("turquoise");
	public static final ResourceKey<TrimMaterial> NECROMIUM = createKey("necromium");
	public static final ResourceKey<TrimMaterial> NECROMIUM_DARKER = createKey("necromium_darker");
	public static final ResourceKey<TrimMaterial> SANGUINE = createKey("sanguine");
	public static final ResourceKey<TrimMaterial> SANGUINE_DARKER = createKey("sanguine_darker");

	public static final ResourceKey<TrimMaterial> EXPOSED_COPPER = createKey("exposed_copper");
	public static final ResourceKey<TrimMaterial> WEATHERED_COPPER = createKey("weathered_copper");
	public static final ResourceKey<TrimMaterial> OXIDIZED_COPPER = createKey("oxidized_copper");

	public static final ResourceKey<TrimMaterial> COPPER_DARKER = createKey("copper_darker");
	public static final ResourceKey<TrimMaterial> EXPOSED_COPPER_DARKER = createKey("exposed_copper_darker");
	public static final ResourceKey<TrimMaterial> WEATHERED_COPPER_DARKER = createKey("weathered_copper_darker");
	public static final ResourceKey<TrimMaterial> OXIDIZED_COPPER_DARKER = createKey("oxidized_copper_darker");

	public static final ResourceKey<TrimMaterial> WAXED_COPPER = createKey("waxed_copper");
	public static final ResourceKey<TrimMaterial> WAXED_EXPOSED_COPPER = createKey("waxed_exposed_copper");
	public static final ResourceKey<TrimMaterial> WAXED_WEATHERED_COPPER = createKey("waxed_weathered_copper");
	public static final ResourceKey<TrimMaterial> WAXED_OXIDIZED_COPPER = createKey("waxed_oxidized_copper");

	public static void bootstrap(BootstapContext<TrimMaterial> context) {
		register(context, SPINEL, CCItems.SPINEL.get(), Style.EMPTY.withColor(0xD684AC), Map.of());
		register(context, ZIRCONIA, CCItems.ZIRCONIA.get(), Style.EMPTY.withColor(0xADAFF1), Map.of());
		register(context, SILVER, CCItems.SILVER_INGOT.get(), Style.EMPTY.withColor(0xC7D8E6), Map.of());
		register(context, TIN, CCItems.TIN_INGOT.get(), Style.EMPTY.withColor(0xCEC0A3), Map.of());
		register(context, TURQUOISE, CCItems.TURQUOISE.get(), Style.EMPTY.withColor(0x2BFF75), Map.of());
		register(context, NECROMIUM, CCItems.NECROMIUM_INGOT.get(), Style.EMPTY.withColor(0x627C6E), Map.of());
		register(context, SANGUINE, CCItems.LIVING_FLESH.get(), Style.EMPTY.withColor(0x6D353A), Map.of());

		register(context, EXPOSED_COPPER, CCItems.EXPOSED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x99715D), Map.of());
		register(context, WEATHERED_COPPER, CCItems.WEATHERED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x747757), Map.of());
		register(context, OXIDIZED_COPPER, CCItems.OXIDIZED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x458B6B), Map.of());

		register(context, WAXED_COPPER, CCItems.WAXED_COPPER_INGOT.get(), Style.EMPTY.withColor(0xB4684D), Map.of());
		register(context, WAXED_EXPOSED_COPPER, CCItems.WAXED_EXPOSED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x99715D), Map.of());
		register(context, WAXED_WEATHERED_COPPER, CCItems.WAXED_WEATHERED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x747757), Map.of());
		register(context, WAXED_OXIDIZED_COPPER, CCItems.WAXED_OXIDIZED_COPPER_INGOT.get(), Style.EMPTY.withColor(0x458B6B), Map.of());
	}

	public static void registerArmorMaterialOverrides() {
		registerArmorMaterialOverrides(SILVER, CCArmorMaterials.SILVER, SILVER_DARKER);
		registerArmorMaterialOverrides(NECROMIUM, CCArmorMaterials.NECROMIUM, NECROMIUM_DARKER);
		registerArmorMaterialOverrides(SANGUINE, CCArmorMaterials.SANGUINE, SANGUINE_DARKER);

		registerArmorMaterialOverrides(TrimMaterials.COPPER, CCArmorMaterials.COPPER, COPPER_DARKER);
		registerArmorMaterialOverrides(EXPOSED_COPPER, CCArmorMaterials.EXPOSED_COPPER, EXPOSED_COPPER_DARKER);
		registerArmorMaterialOverrides(WEATHERED_COPPER, CCArmorMaterials.WEATHERED_COPPER, WEATHERED_COPPER_DARKER);
		registerArmorMaterialOverrides(OXIDIZED_COPPER, CCArmorMaterials.OXIDIZED_COPPER, OXIDIZED_COPPER_DARKER);

		registerArmorMaterialOverrides(WAXED_COPPER, CCArmorMaterials.COPPER, COPPER_DARKER);
		registerArmorMaterialOverrides(WAXED_EXPOSED_COPPER, CCArmorMaterials.EXPOSED_COPPER, EXPOSED_COPPER_DARKER);
		registerArmorMaterialOverrides(WAXED_WEATHERED_COPPER, CCArmorMaterials.WEATHERED_COPPER, WEATHERED_COPPER_DARKER);
		registerArmorMaterialOverrides(WAXED_OXIDIZED_COPPER, CCArmorMaterials.OXIDIZED_COPPER, OXIDIZED_COPPER_DARKER);
	}

	public static void registerArmorMaterialOverrides(ResourceKey<TrimMaterial> trim, ArmorMaterial material, ResourceKey<TrimMaterial> darkerTrim) {
		BlueprintTrims.registerArmorMaterialOverrides(trim, Map.of(material, darkerTrim.location().getNamespace() + "_" + darkerTrim.location().getPath()));
	}

	private static ResourceKey<TrimMaterial> createKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, CavernsAndChasms.location(name));
	}

	private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style, Map<ArmorMaterials, String> overrides) {
		ResourceLocation location = key.location();
		boolean waxedCopper = key.equals(WAXED_COPPER);
		String path = location.getPath().replace("waxed_", "");
		context.register(key, new TrimMaterial((!waxedCopper ? location.getNamespace() + "_" : "") + path, ForgeRegistries.ITEMS.getHolder(item).get(), -1.0F, overrides, Component.translatable(Util.makeDescriptionId("trim_material", new ResourceLocation(waxedCopper ? "minecraft" : location.getNamespace(), path))).withStyle(style)));
	}
}