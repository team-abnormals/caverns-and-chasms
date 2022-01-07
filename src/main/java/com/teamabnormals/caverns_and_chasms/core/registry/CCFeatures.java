package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.world.gen.feature.RockyDirtFeature;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CavernsAndChasms.MOD_ID);

	public static final RuleTest SOUL_SAND_VALLEY = new TagMatchTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS);

	public static final RegistryObject<Feature<OreConfiguration>> ROCKY_DIRT_ORE = FEATURES.register("rocky_dirt_ore", () -> new RockyDirtFeature(OreConfiguration.CODEC));

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		MobSpawnSettingsBuilder spawns = event.getSpawns();
		ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome);

		List<Supplier<PlacedFeature>> oreFeatures = event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES);
		List<Supplier<PlacedFeature>> decorFeatures = event.getGeneration().getFeatures(Decoration.UNDERGROUND_DECORATION);
		List<Supplier<PlacedFeature>> vegetationFeatures = event.getGeneration().getFeatures(Decoration.VEGETAL_DECORATION);

		if (DataUtil.matchesKeys(biome, Biomes.SOUL_SAND_VALLEY)) {
			removeGoldOre(decorFeatures);
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CCPlacedFeatures.ORE_SILVER_SOUL);
		}

		if (BiomeDictionary.getTypes(biomeKey).contains(BiomeDictionary.Type.OVERWORLD)) {
			if (event.getClimate().temperature <= 0.3D) {
				removeGoldOre(oreFeatures);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_LOWER);
			}

			if (biome.equals(Biomes.ICE_SPIKES.location())) {
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_EXTRA);
			}

			if (event.getCategory() == Biome.BiomeCategory.JUNGLE || event.getCategory() == Biome.BiomeCategory.SWAMP || DataUtil.matchesKeys(biome, Biomes.LUSH_CAVES)) {
				removeLapisOre(oreFeatures);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL_BURIED);
				spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(CCEntityTypes.MIME.get(), 150, 1, 1));
			}

			removeDirtOre(oreFeatures);
			generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_ROCKY_DIRT);

			if (event.getCategory() != Biome.BiomeCategory.OCEAN && event.getCategory() != Biome.BiomeCategory.BEACH)
				spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(CCEntityTypes.CAVEFISH.get(), 350, 4, 7));
		}
	}

	public static final class CCConfiguredFeatures {
		public static final List<OreConfiguration.TargetBlockState> ORE_SILVER_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
		public static final List<OreConfiguration.TargetBlockState> ORE_SPINEL_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState()));

		public static final ConfiguredFeature<?, ?> ORE_SILVER = register("ore_silver", Feature.ORE.configured(new OreConfiguration(ORE_SILVER_TARGET_LIST, 9)));
		public static final ConfiguredFeature<?, ?> ORE_SILVER_BURIED = register("ore_silver_buried", Feature.ORE.configured(new OreConfiguration(ORE_SILVER_TARGET_LIST, 9, 0.5F)));
		public static final ConfiguredFeature<?, ?> ORE_SOUL_SILVER = register("ore_soul_silver", Feature.ORE.configured(new OreConfiguration(SOUL_SAND_VALLEY, CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState(), 17, 1.0F)));
		public static final ConfiguredFeature<?, ?> ORE_SPINEL = register("ore_spinel", Feature.ORE.configured(new OreConfiguration(ORE_SPINEL_TARGET_LIST, 7)));
		public static final ConfiguredFeature<?, ?> ORE_SPINEL_BURIED = register("ore_spinel_buried", Feature.ORE.configured(new OreConfiguration(ORE_SPINEL_TARGET_LIST, 7, 1.0F)));
		public static final ConfiguredFeature<?, ?> ORE_ROCKY_DIRT = register("ore_rocky_dirt", ROCKY_DIRT_ORE.get().configured(new OreConfiguration(OreFeatures.NATURAL_STONE, CCBlocks.ROCKY_DIRT.get().defaultBlockState(), 33)));

		public static final ConfiguredFeature<?, ?> AZALEA_TREE = register("azalea_tree", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(CCBlocks.AZALEA_LOG.get()), new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)), new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50), new TwoLayersFeatureSize(1, 0, 1))).dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT)).forceDirt().build()));

		private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
			return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name), configuredFeature);
		}
	}

	public static final class CCPlacedFeatures {
		public static final PlacedFeature ORE_SILVER_EXTRA = register("ore_silver_extra", CCConfiguredFeatures.ORE_SILVER.placed(commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))));
		public static final PlacedFeature ORE_SILVER = register("ore_silver", CCConfiguredFeatures.ORE_SILVER_BURIED.placed(commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))));
		public static final PlacedFeature ORE_SILVER_LOWER = register("ore_silver_lower", CCConfiguredFeatures.ORE_SILVER_BURIED.placed(orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48)))));
		public static final PlacedFeature ORE_SILVER_SOUL = register("ore_silver_soul", CCConfiguredFeatures.ORE_SOUL_SILVER.placed(commonOrePlacement(45, PlacementUtils.RANGE_10_10)));
		public static final PlacedFeature ORE_SPINEL = register("ore_spinel", CCConfiguredFeatures.ORE_SPINEL.placed(commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32)))));
		public static final PlacedFeature ORE_SPINEL_BURIED = register("ore_spinel_buried", CCConfiguredFeatures.ORE_SPINEL_BURIED.placed(commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));
		public static final PlacedFeature ORE_ROCKY_DIRT = register("ore_rocky_dirt", CCConfiguredFeatures.ORE_ROCKY_DIRT.placed(commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160)))));

		private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
			return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
		}

		private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier p_195345_) {
			return orePlacement(CountPlacement.of(count), p_195345_);
		}

		public static PlacedFeature register(String name, PlacedFeature placedFeature) {
			return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name), placedFeature);
		}
	}

	public static void removeGoldOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.GOLD_ORE) || targetBlockState.state.is(Blocks.DEEPSLATE_GOLD_ORE) || targetBlockState.state.is(Blocks.NETHER_GOLD_ORE)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}
		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeLapisOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.LAPIS_ORE) || targetBlockState.state.is(Blocks.DEEPSLATE_LAPIS_ORE)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}

		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeDirtOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.DIRT)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}

		toRemove.forEach(oreFeatures::remove);
	}
}