package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class CCConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED = createKey("ore_gold_buried");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER = createKey("ore_silver");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_BURIED = createKey("ore_silver_buried");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SOUL_SILVER = createKey("ore_soul_silver");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_BURIED_WITH_GOLD = createKey("ore_silver_buried_with_gold");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED_WITH_SILVER = createKey("ore_gold_buried_with_silver");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_AND_SILVER_BURIED = createKey("ore_gold_and_silver_buried");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TIN = createKey("ore_tin");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPINEL = createKey("ore_spinel");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPINEL_BURIED = createKey("ore_spinel_buried");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ROCKY_DIRT = createKey("ore_rocky_dirt");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FRAGILE_STONE = createKey("ore_fragile_stone");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FRAGILE_STONE_BURIED = createKey("ore_fragile_stone_buried");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_RHYOLITE = createKey("ore_rhyolite");

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CAVE_GROWTHS = createKey("patch_cave_growths");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_GROWTH_GROVE = createKey("cave_growth_grove");

	public static final ResourceKey<ConfiguredFeature<?, ?>> FALSE_HOPE = createKey("false_hope");

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

		RuleTest baseStone = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
		RuleTest stoneOre = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateOre = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		List<OreConfiguration.TargetBlockState> goldTargets = List.of(OreConfiguration.target(stoneOre, Blocks.GOLD_ORE.defaultBlockState()), OreConfiguration.target(deepslateOre, Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> silverTargets = List.of(OreConfiguration.target(stoneOre, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
		List<OreConfiguration.TargetBlockState> tinTargets = List.of(OreConfiguration.target(stoneOre, CCBlocks.TIN_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState()));
		List<OreConfiguration.TargetBlockState> spinelTargets = List.of(OreConfiguration.target(stoneOre, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState()));

		register(context, ORE_GOLD_BURIED, Feature.ORE, new OreConfiguration(goldTargets, 9, 0.5F));
		register(context, ORE_SILVER, Feature.ORE, new OreConfiguration(silverTargets, 9));
		register(context, ORE_SILVER_BURIED, Feature.ORE, new OreConfiguration(silverTargets, 9, 0.5F));
		register(context, ORE_SOUL_SILVER, Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS), CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState(), 17, 1.0F));

		register(context, ORE_SILVER_BURIED_WITH_GOLD, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_SILVER_BURIED, 0.80F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_GOLD_BURIED).get()));
		register(context, ORE_GOLD_BURIED_WITH_SILVER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_GOLD_BURIED, 0.80F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_SILVER_BURIED).get()));
		register(context, ORE_GOLD_AND_SILVER_BURIED, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(weighted(CCPlacedFeatures.ORE_SILVER_BURIED, 0.5F, placedFeatures)), placedFeatures.get(CCPlacedFeatures.ORE_GOLD_BURIED).get()));

		register(context, ORE_TIN, CCFeatures.TIN_ARROW.get(), new OreConfiguration(tinTargets, 6));

		register(context, ORE_SPINEL, Feature.ORE, new OreConfiguration(spinelTargets, 12));
		register(context, ORE_SPINEL_BURIED, Feature.ORE, new OreConfiguration(spinelTargets, 12, 1.0F));

		register(context, ORE_ROCKY_DIRT, CCFeatures.ORE_WITH_DIRT.get(), new OreConfiguration(baseStone, CCBlocks.ROCKY_DIRT.get().defaultBlockState(), 33));
		register(context, ORE_FRAGILE_STONE, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(stoneOre, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 0.1F));
		register(context, ORE_FRAGILE_STONE_BURIED, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(stoneOre, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(deepslateOre, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 1.0F));
		register(context, ORE_RHYOLITE, CCFeatures.RHYOLITE.get(), new OreConfiguration(baseStone, CCBlocks.RHYOLITE.get().defaultBlockState(), 64));

		register(context, TreeFeatures.AZALEA_TREE, Feature.TREE, (new TreeConfigurationBuilder(BlockStateProvider.simple(CCBlocks.AZALEA_LOG.get()), new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)), new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50), new TwoLayersFeatureSize(1, 0, 1))).dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT)).forceDirt().build());

		register(context, PATCH_CAVE_GROWTHS, CCFeatures.CAVE_GROWTHS_PATCH.get(), NoneFeatureConfiguration.NONE);
		register(context, CAVE_GROWTH_GROVE, CCFeatures.CAVE_GROWTH_GROVE.get(), NoneFeatureConfiguration.NONE);

		register(context, FALSE_HOPE, CCFeatures.FALSE_HOPE.get(), NoneFeatureConfiguration.NONE);

		register(context, MiscOverworldFeatures.LAKE_LAVA, CCFeatures.MAGMA_LAKE.get(), new LakeFeature.Configuration(
				BlockStateProvider.simple(Blocks.LAVA),
				BlockStateProvider.simple(CCBlocks.RHYOLITE.get()))
		);
	}

	private static WeightedPlacedFeature weighted(ResourceKey<PlacedFeature> feature, float weight, HolderGetter<PlacedFeature> placedFeatures) {
		return new WeightedPlacedFeature(placedFeatures.get(feature).get(), weight);
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, CavernsAndChasms.location(name));
	}

	public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
		context.register(key, new ConfiguredFeature<>(feature, config));
	}
}