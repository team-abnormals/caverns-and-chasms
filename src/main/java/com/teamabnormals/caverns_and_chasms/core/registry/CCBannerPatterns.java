package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public final class CCBannerPatterns {
	public static final ResourceKey<BannerPattern> ABNORMALS = create("abnormals");

	public static void bootstrap(BootstrapContext<BannerPattern> context) {
		register(context, ABNORMALS);
	}

	private static ResourceKey<BannerPattern> create(String name) {
		return ResourceKey.create(Registries.BANNER_PATTERN, CavernsAndChasms.location(name));
	}

	public static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> resourceKey) {
		context.register(resourceKey, new BannerPattern(resourceKey.location(), "block.minecraft.banner." + resourceKey.location().toShortLanguageKey()));
	}
}