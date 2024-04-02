package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.OreWithDirtFeature;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Feature<OreConfiguration>> ORE_WITH_DIRT = FEATURES.register("ore_with_dirt", () -> new OreWithDirtFeature(OreConfiguration.CODEC));

	public static final class CCConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED = createKey("ore_gold_buried");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER = createKey("ore_silver");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_BURIED = createKey("ore_silver_buried");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SOUL_SILVER = createKey("ore_soul_silver");

		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_BURIED_WITH_GOLD = createKey("ore_silver_buried_with_gold");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED_WITH_SILVER = createKey("ore_gold_buried_with_silver");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_AND_SILVER_BURIED = createKey("ore_gold_and_silver_buried");

		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPINEL = createKey("ore_spinel");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPINEL_BURIED = createKey("ore_spinel_buried");

		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ROCKY_DIRT = createKey("ore_rocky_dirt");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FRAGILE_STONE = createKey("ore_fragile_stone");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FRAGILE_STONE_BURIED = createKey("ore_fragile_stone_buried");

		public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE = createKey("azalea_tree");

		public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
			HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

			RuleTest baseStone = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
			RuleTest stoneOre = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
			RuleTest deepslateOre = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
			List<OreConfiguration.TargetBlockState> goldTargets = List.of(OreConfiguration.target(stoneOre, Blocks.GOLD_ORE.defaultBlockState()), OreConfiguration.target(deepslateOre, Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()));
			List<OreConfiguration.TargetBlockState> silverTargets = List.of(OreConfiguration.target(stoneOre, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
			List<OreConfiguration.TargetBlockState> spinelTargets = List.of(OreConfiguration.target(stoneOre, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState()));

			register(context, ORE_GOLD_BURIED, Feature.ORE, new OreConfiguration(goldTargets, 9, 0.5F));
			register(context, ORE_SILVER, Feature.ORE, new OreConfiguration(silverTargets, 9));
			register(context, ORE_SILVER_BURIED, Feature.ORE, new OreConfiguration(silverTargets, 9, 0.5F));
			register(context, ORE_SOUL_SILVER, Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS), CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState(), 17, 1.0F));

			register(context, ORE_SILVER_BURIED_WITH_GOLD, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_SILVER_BURIED, 0.80F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_GOLD_BURIED).get()));
			register(context, ORE_GOLD_BURIED_WITH_SILVER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_GOLD_BURIED, 0.80F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_SILVER_BURIED).get()));
			register(context, ORE_GOLD_AND_SILVER_BURIED, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_SILVER_BURIED, 0.5F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_GOLD_BURIED).get()));

			register(context, ORE_SPINEL, Feature.ORE, new OreConfiguration(spinelTargets, 6));
			register(context, ORE_SPINEL_BURIED, Feature.ORE, new OreConfiguration(spinelTargets, 12, 0.5F));

			register(context, ORE_ROCKY_DIRT, CCFeatures.ORE_WITH_DIRT.get(), new OreConfiguration(baseStone, CCBlocks.ROCKY_DIRT.get().defaultBlockState(), 33));
			register(context, ORE_FRAGILE_STONE, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(stoneOre, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 0.1F));
			register(context, ORE_FRAGILE_STONE_BURIED, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(stoneOre, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 1.0F));

			register(context, AZALEA_TREE, Feature.TREE, (new TreeConfigurationBuilder(BlockStateProvider.simple(CCBlocks.AZALEA_LOG.get()), new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)), new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50), new TwoLayersFeatureSize(1, 0, 1))).dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT)).forceDirt().build());
		}

		private static WeightedPlacedFeature weighted(ResourceKey<PlacedFeature> feature, float weight, HolderGetter<PlacedFeature> placedFeatures) {
			return new WeightedPlacedFeature(placedFeatures.get(feature).get(), weight);
		}

		public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}

		public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
			context.register(key, new ConfiguredFeature<>(feature, config));
		}
	}

	public static final class CCPlacedFeatures {
		public static final ResourceKey<PlacedFeature> ORE_GOLD_BURIED = createKey("ore_gold_buried");
		public static final ResourceKey<PlacedFeature> ORE_SILVER_BURIED = createKey("ore_silver_buried");

		public static final ResourceKey<PlacedFeature> ORE_SILVER_BURIED_WITH_GOLD = createKey("ore_silver_buried_with_gold");
		public static final ResourceKey<PlacedFeature> ORE_GOLD_BURIED_WITH_SILVER = createKey("ore_gold_buried_with_silver");
		public static final ResourceKey<PlacedFeature> ORE_GOLD_AND_SILVER_LOWER = createKey("ore_gold_and_silver_lower");

		public static final ResourceKey<PlacedFeature> ORE_SILVER_EXTRA = createKey("ore_silver_extra");
		public static final ResourceKey<PlacedFeature> ORE_SILVER_SOUL = createKey("ore_silver_soul");

		public static final ResourceKey<PlacedFeature> ORE_SPINEL = createKey("ore_spinel");
		public static final ResourceKey<PlacedFeature> ORE_SPINEL_BURIED = createKey("ore_spinel_buried");

		public static final ResourceKey<PlacedFeature> ORE_ROCKY_DIRT = createKey("ore_rocky_dirt");
		public static final ResourceKey<PlacedFeature> ORE_FRAGILE_STONE = createKey("ore_fragile_stone");
		public static final ResourceKey<PlacedFeature> ORE_FRAGILE_STONE_BURIED = createKey("ore_fragile_stone_buried");

		public static void bootstrap(BootstapContext<PlacedFeature> context) {
			register(context, ORE_GOLD_BURIED, CCConfiguredFeatures.ORE_GOLD_BURIED, List.of());
			register(context, ORE_SILVER_BURIED, CCConfiguredFeatures.ORE_SILVER_BURIED, List.of());

			register(context, ORE_SILVER_BURIED_WITH_GOLD, CCConfiguredFeatures.ORE_SILVER_BURIED_WITH_GOLD, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
			register(context, ORE_GOLD_BURIED_WITH_SILVER, CCConfiguredFeatures.ORE_GOLD_BURIED_WITH_SILVER, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
			register(context, ORE_GOLD_AND_SILVER_LOWER, CCConfiguredFeatures.ORE_GOLD_AND_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));

			register(context, ORE_SILVER_EXTRA, CCConfiguredFeatures.ORE_SILVER, commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
			register(context, ORE_SILVER_SOUL, CCConfiguredFeatures.ORE_SOUL_SILVER, commonOrePlacement(45, PlacementUtils.RANGE_10_10));

			register(context, ORE_SPINEL, CCConfiguredFeatures.ORE_SPINEL, commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(64))));
			register(context, ORE_SPINEL_BURIED, CCConfiguredFeatures.ORE_SPINEL_BURIED, commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(48))));

			register(context, ORE_ROCKY_DIRT, CCConfiguredFeatures.ORE_ROCKY_DIRT, commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160))));
			register(context, ORE_FRAGILE_STONE, CCConfiguredFeatures.ORE_FRAGILE_STONE, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
			register(context, ORE_FRAGILE_STONE_BURIED, CCConfiguredFeatures.ORE_FRAGILE_STONE_BURIED, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
		}

		private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
			return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
		}

		private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
			return orePlacement(CountPlacement.of(count), modifier);
		}

		public static ResourceKey<PlacedFeature> createKey(String name) {
			return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
			context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
			register(context, key, feature, List.of(modifiers));
		}
	}
}