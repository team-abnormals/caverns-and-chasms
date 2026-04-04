package com.teamabnormals.caverns_and_chasms.common.entity.animal.rat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;

public record RatVariant(Component displayName, RatAssetGroup assets, RatAssetGroup dirtyAssets, int weight) implements WeightedEntry {
	public static final Codec<RatVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ExtraCodecs.COMPONENT.fieldOf("description").forGetter(entry -> entry.displayName),
					RatAssetGroup.CODEC.fieldOf("assets").forGetter(entry -> entry.assets),
					RatAssetGroup.CODEC.fieldOf("dirty_assets").forGetter(entry -> entry.dirtyAssets),
					Codec.INT.optionalFieldOf("weight", 1).forGetter(entry -> entry.weight))
			.apply(instance, RatVariant::new));

	public static final Codec<RatVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ExtraCodecs.COMPONENT.fieldOf("description").forGetter(entry -> entry.displayName),
					RatAssetGroup.CODEC.fieldOf("assets").forGetter(entry -> entry.assets),
					RatAssetGroup.CODEC.fieldOf("dirty_assets").forGetter(entry -> entry.dirtyAssets))
			.apply(instance, (desc, assets, dirtyAssets) -> new RatVariant(desc, assets, dirtyAssets, -1)));

	@Override
	public Weight getWeight() {
		return Weight.of(this.weight());
	}

	public static Holder<RatVariant> getSpawnVariant(RegistryAccess registryAccess, RandomSource random) {
		Registry<RatVariant> registry = registryAccess.registryOrThrow(CCRegistries.RAT_VARIANT);
		WeightedRandomList<RatVariant> variants = WeightedRandomList.create(registry.stream().toList());
		return registry.wrapAsHolder(variants.getRandom(random).orElseThrow());
	}

	public ResourceLocation getTexture(boolean hurt, boolean dirty) {
		RatAssetGroup assets = dirty ? this.dirtyAssets : this.assets;
		ResourceLocation texture = hurt ? assets.hurtTexture() : assets.texture();
		return texture.withPrefix("textures/").withSuffix(".png");
	}

	public record RatAssetGroup(ResourceLocation texture, ResourceLocation hurtTexture) {
		public static final Codec<RatAssetGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ResourceLocation.CODEC.fieldOf("normal").forGetter(entry -> entry.texture),
						ResourceLocation.CODEC.fieldOf("hurt").forGetter(entry -> entry.hurtTexture))
				.apply(instance, RatAssetGroup::new));
	}
}