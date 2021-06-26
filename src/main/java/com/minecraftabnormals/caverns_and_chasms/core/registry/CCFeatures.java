package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCFeatures {
	public static final RuleTest SOUL_SAND_VALLEY = new TagMatchRuleTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS);

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		MobSpawnInfoBuilder spawns = event.getSpawns();
		RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome);

		List<Supplier<ConfiguredFeature<?, ?>>> oreFeatures = event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES);
		List<Supplier<ConfiguredFeature<?, ?>>> decorFeatures = event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION);

		if (DataUtil.matchesKeys(biome, Biomes.SOUL_SAND_VALLEY)) {
			removeGoldOre(decorFeatures);
			event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Configured.ORE_SILVER_SOUL);
		}

		if (BiomeDictionary.getTypes(biomeKey).contains(BiomeDictionary.Type.OVERWORLD)) {
			if (event.getClimate().temperature <= 0.3D) {
				removeGoldOre(oreFeatures);
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Configured.ORE_SILVER);
			}

			if (biome.equals(Biomes.ICE_SPIKES.location())) {
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Configured.ORE_SILVER_EXTRA);
			}

			if (event.getCategory() == Biome.Category.EXTREME_HILLS) {
				removeEmeraldOre(oreFeatures);
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Configured.ORE_EMERALD_CHUNK);
			}

			if (event.getCategory() == Biome.Category.DESERT || event.getCategory() == Biome.Category.JUNGLE) {
				removeLapisOre(oreFeatures);
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Configured.ORE_SUGILITE);
				spawns.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(CCEntities.MIME.get(), 150, 1, 1));
			}

			if (event.getCategory() != Biome.Category.OCEAN && event.getCategory() != Biome.Category.BEACH && !DataUtil.matchesKeys(biome, Biomes.STONE_SHORE))
				spawns.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(CCEntities.CAVEFISH.get(), 350, 4, 7));
		}
	}

	public static final class States {
		public static final BlockState EMERALD_ORE = Blocks.EMERALD_ORE.defaultBlockState();
		public static final BlockState SILVER_ORE = CCBlocks.SILVER_ORE.get().defaultBlockState();
		public static final BlockState SOUL_SILVER_ORE = CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState();
		public static final BlockState SUGILITE_ORE = CCBlocks.SUGILITE_ORE.get().defaultBlockState();
	}

	public static final class Configured {
		public static final ConfiguredFeature<?, ?> ORE_EMERALD_CHUNK = Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.EMERALD_ORE, 19)).range(32).squared().count(1);
		public static final ConfiguredFeature<?, ?> ORE_SUGILITE = Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.SUGILITE_ORE, 7)).decorated(Placement.DEPTH_AVERAGE.configured(new DepthAverageConfig(16, 16))).squared();
		public static final ConfiguredFeature<?, ?> ORE_SILVER = Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.SILVER_ORE, 9)).range(32).squared().count(2);
		public static final ConfiguredFeature<?, ?> ORE_SILVER_SOUL = Feature.NO_SURFACE_ORE.configured(new OreFeatureConfig(SOUL_SAND_VALLEY, States.SOUL_SILVER_ORE, 17)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(8, 16, 128))).squared().count(45);
		public static final ConfiguredFeature<?, ?> ORE_SILVER_EXTRA = Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.SILVER_ORE, 9)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(32, 32, 80))).squared().count(20);

		public static void registerConfiguredFeatures() {
			register("ore_emerald_chunk", ORE_EMERALD_CHUNK);
			register("ore_sugilite", ORE_SUGILITE);
			register("ore_silver", ORE_SILVER);
			register("ore_silver_soul", ORE_SILVER_SOUL);
			register("ore_silver_extra", ORE_SILVER_EXTRA);
		}

		private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name), configuredFeature);
		}
	}

	public static void removeGoldOre(List<Supplier<ConfiguredFeature<?, ?>>> oreFeatures) {
		List<Supplier<ConfiguredFeature<?, ?>>> toRemove = new ArrayList<>();
		for (Supplier<ConfiguredFeature<?, ?>> feature : oreFeatures) {
			if (feature.get().config instanceof DecoratedFeatureConfig) {
				DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.get().config;
				if (decorated.feature.get().config instanceof DecoratedFeatureConfig) {
					DecoratedFeatureConfig decorated2 = (DecoratedFeatureConfig) decorated.feature.get().config;
					if (decorated2.feature.get().config instanceof DecoratedFeatureConfig) {
						DecoratedFeatureConfig decorated3 = (DecoratedFeatureConfig) decorated2.feature.get().config;
						if (decorated3.feature.get().config instanceof OreFeatureConfig) {
							OreFeatureConfig ore = (OreFeatureConfig) decorated3.feature.get().config;
							if (ore.state.is(Blocks.GOLD_ORE) || ore.state.is(Blocks.NETHER_GOLD_ORE)) {
								toRemove.add(feature);
							}
						}
					}
				}
			}
		}
		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeLapisOre(List<Supplier<ConfiguredFeature<?, ?>>> oreFeatures) {
		List<Supplier<ConfiguredFeature<?, ?>>> toRemove = new ArrayList<>();
		for (Supplier<ConfiguredFeature<?, ?>> feature : oreFeatures) {
			if (feature.get().config instanceof DecoratedFeatureConfig) {
				DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.get().config;
				if (decorated.feature.get().config instanceof DecoratedFeatureConfig) {
					DecoratedFeatureConfig decorated2 = (DecoratedFeatureConfig) decorated.feature.get().config;
					if (decorated2.feature.get().config instanceof OreFeatureConfig) {
						OreFeatureConfig ore = (OreFeatureConfig) decorated2.feature.get().config;
						if (ore.state.is(Blocks.LAPIS_ORE)) {
							toRemove.add(feature);
						}
					}
				}
			}
		}
		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeEmeraldOre(List<Supplier<ConfiguredFeature<?, ?>>> oreFeatures) {
		List<Supplier<ConfiguredFeature<?, ?>>> toRemove = new ArrayList<>();
		for (Supplier<ConfiguredFeature<?, ?>> feature : oreFeatures) {
			if (feature.get().config instanceof DecoratedFeatureConfig) {
				DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.get().config;
				if (decorated.feature.get().config instanceof ReplaceBlockConfig) {
					ReplaceBlockConfig ore = (ReplaceBlockConfig) decorated.feature.get().config;
					if (ore.state.is(Blocks.EMERALD_ORE)) {
						toRemove.add(feature);
					}
				}
			}
		}
		toRemove.forEach(oreFeatures::remove);
	}
}
