package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;

public class CCTrimPatterns {
	public static final ResourceKey<TrimPattern> EXILE = createKey("exile");
	public static final ResourceKey<TrimPattern> FORGER = createKey("forger");
	public static final ResourceKey<TrimPattern> IMMOLATE = createKey("immolate");
	public static final ResourceKey<TrimPattern> RIM = createKey("rim");
	public static final ResourceKey<TrimPattern> PLATE = createKey("plate");
	public static final ResourceKey<TrimPattern> CORE = createKey("core");
	public static final ResourceKey<TrimPattern> SANGUINE = createKey("sanguine");
	public static final ResourceKey<TrimPattern> COPPER = createKey("copper");

	public static void bootstrap(BootstapContext<TrimPattern> context) {
		register(context, EXILE, CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, FORGER, CCItems.FORGER_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, IMMOLATE, CCItems.IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, RIM, CCItems.RIM_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, PLATE, CCItems.PLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, CORE, CCItems.CORE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, SANGUINE, CCItems.LIVING_FLESH.get());
		register(context, COPPER, Items.COPPER_INGOT);
	}

	public static ResourceKey<TrimPattern> createKey(String name) {
		return ResourceKey.create(Registries.TRIM_PATTERN, CavernsAndChasms.location(name));
	}

	private static void register(BootstapContext<TrimPattern> context, ResourceKey<TrimPattern> key, Item item) {
		context.register(key, new TrimPattern(key.location(), ForgeRegistries.ITEMS.getHolder(item).get(), Component.translatable(Util.makeDescriptionId("trim_pattern", key.location()))));
	}
}