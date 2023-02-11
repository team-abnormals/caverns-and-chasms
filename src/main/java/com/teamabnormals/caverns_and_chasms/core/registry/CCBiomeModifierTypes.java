package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBiomeModifierTypes {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(Keys.BIOME_MODIFIER_SERIALIZERS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Codec<InvertedAddFeaturesBiomeModifier>> ADD_FEATURES_INVERTED = BIOME_MODIFIER_SERIALIZERS.register("add_features_inverted", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("inverted_biomes").forGetter(InvertedAddFeaturesBiomeModifier::biomes),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(InvertedAddFeaturesBiomeModifier::features),
					Decoration.CODEC.fieldOf("step").forGetter(InvertedAddFeaturesBiomeModifier::step)
			).apply(builder, InvertedAddFeaturesBiomeModifier::new))
	);

	public record InvertedAddFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Decoration step) implements BiomeModifier {
		@Override
		public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
			if (phase == Phase.ADD && !this.biomes.contains(biome)) {
				BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
				this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return ADD_FEATURES_INVERTED.get();
		}
	}
}