package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

public class CCRatVariants {
	public static final ResourceKey<RatVariant> BLUE = createKey("blue");
	public static final ResourceKey<RatVariant> BROWN = createKey("brown");
	public static final ResourceKey<RatVariant> GRAY = createKey("gray");
	public static final ResourceKey<RatVariant> WHITE = createKey("white");

	public static void bootstrap(BootstapContext<RatVariant> context) {
		registerVariant(context, BLUE, 7);
		registerVariant(context, GRAY, 7);
		registerVariant(context, BROWN, 5);
		registerVariant(context, WHITE, 1);
	}

	public static void registerVariant(BootstapContext<RatVariant> context, ResourceKey<RatVariant> key, int weight) {
		context.register(key, new RatVariant(key.location().withPrefix("entity/rat/").withSuffix("_rat"), weight));
	}

	public static ResourceKey<RatVariant> createKey(String name) {
		return ResourceKey.create(CCRegistries.RAT_VARIANT, CavernsAndChasms.location(name));
	}
}
