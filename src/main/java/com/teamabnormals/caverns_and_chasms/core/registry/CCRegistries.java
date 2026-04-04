package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DataPackRegistryEvent;

public final class CCRegistries {
	public static final ResourceKey<Registry<RatVariant>> RAT_VARIANT = key("rat_variant");

	public static void registerRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(RAT_VARIANT, RatVariant.CODEC, RatVariant.NETWORK_CODEC);
	}

	private static <T> ResourceKey<Registry<T>> key(String name) {
		return ResourceKey.createRegistryKey(CavernsAndChasms.location(name));
	}
}