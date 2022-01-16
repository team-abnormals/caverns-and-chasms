package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.world.gen.feature.OreWithDirtFeature;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.*;

import java.util.List;

public class CCFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Feature<OreConfiguration>> ORE_WITH_DIRT = FEATURES.register("ore_with_dirt", () -> new OreWithDirtFeature(OreConfiguration.CODEC));

	public static final class CCConfiguredFeatures {
		public static final List<OreConfiguration.TargetBlockState> ORE_SILVER_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
		public static final List<OreConfiguration.TargetBlockState> ORE_SPINEL_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.SPINEL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState()));

		public static final ConfiguredFeature<?, ?> ORE_SILVER = register("ore_silver", Feature.ORE.configured(new OreConfiguration(ORE_SILVER_TARGET_LIST, 9)));
		public static final ConfiguredFeature<?, ?> ORE_SILVER_BURIED = register("ore_silver_buried", Feature.ORE.configured(new OreConfiguration(ORE_SILVER_TARGET_LIST, 9, 0.5F)));
		public static final ConfiguredFeature<?, ?> ORE_SOUL_SILVER = register("ore_soul_silver", Feature.ORE.configured(new OreConfiguration(new TagMatchTest(BlockTags.WITHER_SUMMON_BASE_BLOCKS), CCBlocks.SOUL_SILVER_ORE.get().defaultBlockState(), 17, 1.0F)));
		public static final ConfiguredFeature<?, ?> ORE_SPINEL = register("ore_spinel", Feature.ORE.configured(new OreConfiguration(ORE_SPINEL_TARGET_LIST, 7)));
		public static final ConfiguredFeature<?, ?> ORE_SPINEL_BURIED = register("ore_spinel_buried", Feature.ORE.configured(new OreConfiguration(ORE_SPINEL_TARGET_LIST, 7, 1.0F)));
		public static final ConfiguredFeature<?, ?> ORE_ROCKY_DIRT = register("ore_rocky_dirt", CCFeatures.ORE_WITH_DIRT.get().configured(new OreConfiguration(OreFeatures.NATURAL_STONE, CCBlocks.ROCKY_DIRT.get().defaultBlockState(), 33)));
		public static final ConfiguredFeature<?, ?> ORE_FRAGILE_STONE = register("ore_fragile_stone", Feature.ORE.configured(new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, CCBlocks.FRAGILE_STONE.get().defaultBlockState(), 33)));

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
		public static final PlacedFeature ORE_FRAGILE_STONE = register("ore_fragile_stone", CCConfiguredFeatures.ORE_FRAGILE_STONE.placed(commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(60)))));

		private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
			return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
		}

		private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
			return orePlacement(CountPlacement.of(count), modifier);
		}

		public static PlacedFeature register(String name, PlacedFeature placedFeature) {
			return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name), placedFeature);
		}
	}
}