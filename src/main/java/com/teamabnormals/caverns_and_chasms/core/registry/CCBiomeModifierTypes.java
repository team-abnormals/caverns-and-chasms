package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBiomeModifierTypes {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(Keys.BIOME_MODIFIER_SERIALIZERS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Codec<BlacklistedAddSpawnsBiomeModifier>> ADD_SPAWNS_BLACKLISTED = BIOME_MODIFIER_SERIALIZERS.register("add_spawns_blacklisted", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("blacklisted_biomes").forGetter(BlacklistedAddSpawnsBiomeModifier::blacklistedBiomes),
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(BlacklistedAddSpawnsBiomeModifier::biomes),
					new ExtraCodecs.EitherCodec<>(SpawnerData.CODEC.listOf(), SpawnerData.CODEC).xmap(
							either -> either.map(Function.identity(), List::of),
							list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
					).fieldOf("spawners").forGetter(BlacklistedAddSpawnsBiomeModifier::spawners)
			).apply(builder, BlacklistedAddSpawnsBiomeModifier::new))
	);

	public static final RegistryObject<Codec<BlacklistedAddFeaturesBiomeModifier>> ADD_FEATURES_BLACKLISTED = BIOME_MODIFIER_SERIALIZERS.register("add_features_blacklisted", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("blacklisted_biomes").forGetter(BlacklistedAddFeaturesBiomeModifier::blacklistedBiomes),
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(BlacklistedAddFeaturesBiomeModifier::biomes),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(BlacklistedAddFeaturesBiomeModifier::features),
					Decoration.CODEC.fieldOf("step").forGetter(BlacklistedAddFeaturesBiomeModifier::step)
			).apply(builder, BlacklistedAddFeaturesBiomeModifier::new))
	);

	public record BlacklistedAddSpawnsBiomeModifier(HolderSet<Biome> blacklistedBiomes, HolderSet<Biome> biomes, List<SpawnerData> spawners) implements BiomeModifier {

		@Override
		public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
			if (phase == Phase.ADD && this.biomes.contains(biome) && !this.blacklistedBiomes.contains(biome)) {
				MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
				for (SpawnerData spawner : this.spawners) {
					EntityType<?> type = spawner.type;
					spawns.addSpawn(type.getCategory(), spawner);
				}
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return ADD_SPAWNS_BLACKLISTED.get();
		}
	}

	public record BlacklistedAddFeaturesBiomeModifier(HolderSet<Biome> blacklistedBiomes, HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Decoration step) implements BiomeModifier {
		@Override
		public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
			if (phase == Phase.ADD && this.biomes.contains(biome) && !this.blacklistedBiomes.contains(biome)) {
				BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
				this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return ADD_FEATURES_BLACKLISTED.get();
		}
	}
}