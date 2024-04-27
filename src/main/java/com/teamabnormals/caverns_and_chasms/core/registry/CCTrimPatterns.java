package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;

public class CCTrimPatterns {
	public static final ResourceKey<TrimPattern> EXILE = createKey("exile");
	public static final ResourceKey<TrimPattern> SANGUINE = createKey("sanguine");

	public static void bootstrap(BootstapContext<TrimPattern> context) {
		register(context, EXILE, CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		register(context, SANGUINE, CCItems.LIVING_FLESH.get());
	}

	public static ResourceKey<TrimPattern> createKey(String name) {
		return ResourceKey.create(Registries.TRIM_PATTERN, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
	}

	private static void register(BootstapContext<TrimPattern> context, ResourceKey<TrimPattern> key, Item item) {
		context.register(key, new TrimPattern(key.location(), ForgeRegistries.ITEMS.getHolder(item).get(), Component.translatable(Util.makeDescriptionId("trim_pattern", key.location()))));
	}
}