package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.OreWithDirtFeature;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Feature<OreConfiguration>> ORE_WITH_DIRT = FEATURES.register("ore_with_dirt", () -> new OreWithDirtFeature(OreConfiguration.CODEC));

	public static final class CCConfiguredFeatures {
		public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER = register("ore_silver", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())), 9)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER_BURIED = register("ore_silver_buried", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())), 9, 0.5F)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_SOUL_SILVER = register("ore_soul_silver", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS), CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState(), 17, 1.0F)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_SPINEL = register("ore_spinel", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState())), 7)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_SPINEL_BURIED = register("ore_spinel_buried", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState())), 7, 1.0F)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_ROCKY_DIRT = register("ore_rocky_dirt", () -> new ConfiguredFeature<>(CCFeatures.ORE_WITH_DIRT.get(), new OreConfiguration(OreFeatures.NATURAL_STONE, CCBlocks.ROCKY_DIRT.get().defaultBlockState(), 33)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_FRAGILE_STONE = register("ore_fragile_stone", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 0.1F)));
		public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_FRAGILE_STONE_BURIED = register("ore_fragile_stone_buried", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.FRAGILE_STONE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.FRAGILE_DEEPSLATE.get().defaultBlockState())), 48, 1.0F)));

		public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> AZALEA_TREE = register("azalea_tree", () -> new ConfiguredFeature<>(Feature.TREE, (new TreeConfigurationBuilder(BlockStateProvider.simple(CCBlocks.AZALEA_LOG.get()), new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)), new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50), new TwoLayersFeatureSize(1, 0, 1))).dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT)).forceDirt().build()));

		private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
			return CONFIGURED_FEATURES.register(name, feature);
		}
	}

	public static final class CCPlacedFeatures {
		public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<PlacedFeature> ORE_SILVER_EXTRA = register("ore_silver_extra", CCConfiguredFeatures.ORE_SILVER, commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
		public static final RegistryObject<PlacedFeature> ORE_SILVER = register("ore_silver", CCConfiguredFeatures.ORE_SILVER_BURIED, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
		public static final RegistryObject<PlacedFeature> ORE_SILVER_LOWER = register("ore_silver_lower", CCConfiguredFeatures.ORE_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
		public static final RegistryObject<PlacedFeature> ORE_SILVER_SOUL = register("ore_silver_soul", CCConfiguredFeatures.ORE_SOUL_SILVER, commonOrePlacement(45, PlacementUtils.RANGE_10_10));
		public static final RegistryObject<PlacedFeature> ORE_SPINEL = register("ore_spinel", CCConfiguredFeatures.ORE_SPINEL, commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
		public static final RegistryObject<PlacedFeature> ORE_SPINEL_BURIED = register("ore_spinel_buried", CCConfiguredFeatures.ORE_SPINEL_BURIED, commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));
		public static final RegistryObject<PlacedFeature> ORE_ROCKY_DIRT = register("ore_rocky_dirt", CCConfiguredFeatures.ORE_ROCKY_DIRT, commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160))));
		public static final RegistryObject<PlacedFeature> ORE_FRAGILE_STONE = register("ore_fragile_stone", CCConfiguredFeatures.ORE_FRAGILE_STONE, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
		public static final RegistryObject<PlacedFeature> ORE_FRAGILE_STONE_BURIED = register("ore_fragile_stone_buried", CCConfiguredFeatures.ORE_FRAGILE_STONE_BURIED, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

		private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
			return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
		}

		private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
			return orePlacement(CountPlacement.of(count), modifier);
		}

		@SuppressWarnings("unchecked")
		private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) {
			return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
		}
	}
}