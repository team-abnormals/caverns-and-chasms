package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.RatVariant.RatAssetGroup;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CCRatVariants {
	public static final ResourceKey<RatVariant> BLUE = createKey("blue");
	public static final ResourceKey<RatVariant> BROWN = createKey("brown");
	public static final ResourceKey<RatVariant> GRAY = createKey("gray");
	public static final ResourceKey<RatVariant> BLACK = createKey("black");
	public static final ResourceKey<RatVariant> WHITE = createKey("white");

	public static void bootstrap(BootstapContext<RatVariant> context) {
		registerVariant(context, BLUE, 7);
		registerVariant(context, GRAY, 7);
		registerVariant(context, BROWN, 5);
		registerVariant(context, BLACK, 5);
		registerVariant(context, WHITE, 1);
	}

	public static void registerVariant(BootstapContext<RatVariant> context, ResourceKey<RatVariant> key, int weight) {
		Component component = Component.translatable(Util.makeDescriptionId("rat_variant", key.location()));
		context.register(key, new RatVariant(component, createAssetGroup(key, ""), createAssetGroup(key, "_dirty"), weight));
	}

	private static RatAssetGroup createAssetGroup(ResourceKey<RatVariant> key, String suffix) {
		ResourceLocation texture = key.location().withPrefix("entity/rat/").withSuffix("_rat").withSuffix(suffix);
		ResourceLocation hurtTexture = key.location().withPrefix("entity/rat/").withSuffix("_rat_hurt").withSuffix(suffix);
		return new RatAssetGroup(texture, hurtTexture);
	}

	public static ResourceKey<RatVariant> createKey(String name) {
		return ResourceKey.create(CCRegistries.RAT_VARIANT, CavernsAndChasms.location(name));
	}
}
