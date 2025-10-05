package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;

public record RatVariant(ResourceLocation texture, int weight) implements WeightedEntry {
	public static final Codec<RatVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("asset_id").forGetter(entry -> entry.texture),
					Codec.INT.optionalFieldOf("weight", 1).forGetter(entry -> entry.weight))
			.apply(instance, RatVariant::new));

	public static final Codec<RatVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("asset_id").forGetter(entry -> entry.texture))
			.apply(instance, (asset) -> new RatVariant(asset, -1)));

	@Override
	public Weight getWeight() {
		return Weight.of(this.weight());
	}

	public static Holder<RatVariant> getSpawnVariant(RegistryAccess registryAccess, RandomSource random) {
		Registry<RatVariant> registry = registryAccess.registryOrThrow(CCRegistries.RAT_VARIANT);
		WeightedRandomList<RatVariant> variants = WeightedRandomList.create(registry.stream().toList());
		return registry.wrapAsHolder(variants.getRandom(random).orElseThrow());
	}
}