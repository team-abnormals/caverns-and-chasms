package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class CCTrimMaterials {
	public static final ResourceKey<TrimMaterial> SPINEL = createKey("spinel");
	public static final ResourceKey<TrimMaterial> SILVER = createKey("silver");
	public static final ResourceKey<TrimMaterial> SILVER_DARKER = createKey("silver_darker");
	public static final ResourceKey<TrimMaterial> NECROMIUM = createKey("necromium");
	public static final ResourceKey<TrimMaterial> NECROMIUM_DARKER = createKey("necromium_darker");
	public static final ResourceKey<TrimMaterial> SANGUINE = createKey("sanguine");
	public static final ResourceKey<TrimMaterial> SANGUINE_DARKER = createKey("sanguine_darker");

	public static void bootstrap(BootstapContext<TrimMaterial> context) {
		register(context, SPINEL, CCItems.SPINEL.get(), Style.EMPTY.withColor(0xD684AC), Map.of());
		register(context, SILVER, CCItems.SILVER_INGOT.get(), Style.EMPTY.withColor(0xC7D8E6), Map.of());
		register(context, NECROMIUM, CCItems.NECROMIUM_INGOT.get(), Style.EMPTY.withColor(0x627C6E), Map.of());
		register(context, SANGUINE, CCItems.LIVING_FLESH.get(), Style.EMPTY.withColor(0x6D353A), Map.of());
	}

	private static ResourceKey<TrimMaterial> createKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
	}

	private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style, Map<ArmorMaterials, String> overrides) {
		ResourceLocation location = key.location();
		context.register(key, new TrimMaterial(location.getNamespace() + "_" + location.getPath(), ForgeRegistries.ITEMS.getHolder(item).get(), -1.0F, overrides, Component.translatable(Util.makeDescriptionId("trim_material", location)).withStyle(style)));
	}
}