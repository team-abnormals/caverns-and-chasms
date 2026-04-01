package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.HeightmapRandomOffsetPlacement;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.NoiseDensityPlacement;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.TinMonolithDistanceFilter;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

import java.util.List;

public class CCPlacedFeatures {
	public static final ResourceKey<PlacedFeature> ORE_GOLD_BURIED = createKey("ore_gold_buried");
	public static final ResourceKey<PlacedFeature> ORE_SILVER_BURIED = createKey("ore_silver_buried");

	public static final ResourceKey<PlacedFeature> ORE_SILVER_BURIED_WITH_GOLD = createKey("ore_silver_buried_with_gold");
	public static final ResourceKey<PlacedFeature> ORE_GOLD_BURIED_WITH_SILVER = createKey("ore_gold_buried_with_silver");
	public static final ResourceKey<PlacedFeature> ORE_GOLD_AND_SILVER_LOWER = createKey("ore_gold_and_silver_lower");

	public static final ResourceKey<PlacedFeature> ORE_SILVER_EXTRA = createKey("ore_silver_extra");
	public static final ResourceKey<PlacedFeature> ORE_SILVER_SOUL = createKey("ore_silver_soul");

	public static final ResourceKey<PlacedFeature> ORE_TIN = createKey("ore_tin");

	public static final ResourceKey<PlacedFeature> ORE_SPINEL = createKey("ore_spinel");
	public static final ResourceKey<PlacedFeature> ORE_SPINEL_BURIED = createKey("ore_spinel_buried");

	public static final ResourceKey<PlacedFeature> ORE_ROCKY_DIRT = createKey("ore_rocky_dirt");
	public static final ResourceKey<PlacedFeature> ORE_FRAGILE_STONE = createKey("ore_fragile_stone");
	public static final ResourceKey<PlacedFeature> ORE_FRAGILE_STONE_BURIED = createKey("ore_fragile_stone_buried");
	public static final ResourceKey<PlacedFeature> ORE_RHYOLITE = createKey("ore_rhyolite");

	public static final ResourceKey<PlacedFeature> PATCH_CAVE_GROWTHS = createKey("patch_cave_growths");
	public static final ResourceKey<PlacedFeature> PATCH_CAVE_GROWTHS_DEEP = createKey("patch_cave_growths_deep");
	public static final ResourceKey<PlacedFeature> CAVE_GROWTH_GROVE = createKey("cave_growth_grove");

	public static final ResourceKey<PlacedFeature> FALSE_HOPE = createKey("false_hope");

	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		HolderGetter<NoiseParameters> noise = context.lookup(Registries.NOISE);

		register(context, ORE_GOLD_BURIED, CCConfiguredFeatures.ORE_GOLD_BURIED, List.of());
		register(context, ORE_SILVER_BURIED, CCConfiguredFeatures.ORE_SILVER_BURIED, List.of());

		register(context, ORE_SILVER_BURIED_WITH_GOLD, CCConfiguredFeatures.ORE_SILVER_BURIED_WITH_GOLD, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
		register(context, ORE_GOLD_BURIED_WITH_SILVER, CCConfiguredFeatures.ORE_GOLD_BURIED_WITH_SILVER, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
		register(context, ORE_GOLD_AND_SILVER_LOWER, CCConfiguredFeatures.ORE_GOLD_AND_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));

		register(context, ORE_SILVER_EXTRA, CCConfiguredFeatures.ORE_SILVER, commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
		register(context, ORE_SILVER_SOUL, CCConfiguredFeatures.ORE_SOUL_SILVER, commonOrePlacement(45, PlacementUtils.RANGE_10_10));

		register(context, ORE_TIN, CCConfiguredFeatures.ORE_TIN, InSquarePlacement.spread(), TinMonolithDistanceFilter.of(512), HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(48)));

		register(context, ORE_SPINEL, CCConfiguredFeatures.ORE_SPINEL, commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(64))));
		register(context, ORE_SPINEL_BURIED, CCConfiguredFeatures.ORE_SPINEL_BURIED, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(48))));

		register(context, ORE_ROCKY_DIRT, CCConfiguredFeatures.ORE_ROCKY_DIRT, commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160))));
		register(context, ORE_FRAGILE_STONE, CCConfiguredFeatures.ORE_FRAGILE_STONE, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
		register(context, ORE_FRAGILE_STONE_BURIED, CCConfiguredFeatures.ORE_FRAGILE_STONE_BURIED, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
		register(context, ORE_RHYOLITE, CCConfiguredFeatures.ORE_RHYOLITE, commonOrePlacement(15, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32))));

		register(context, PATCH_CAVE_GROWTHS, CCConfiguredFeatures.PATCH_CAVE_GROWTHS, new NoiseDensityPlacement(noise.get(CCNoiseParameters.CAVE_GROWTHS).get(), 2.5F, 0.8F), InSquarePlacement.spread(), HeightmapRandomOffsetPlacement.of(Heightmap.Types.WORLD_SURFACE_WG, -40, -2), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), BiomeFilter.biome());
		register(context, PATCH_CAVE_GROWTHS_DEEP, CCConfiguredFeatures.PATCH_CAVE_GROWTHS, new NoiseDensityPlacement(noise.get(CCNoiseParameters.CAVE_GROWTHS).get(), 0.35F, 0.75F), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(172)), SurfaceRelativeThresholdFilter.of(Heightmap.Types.WORLD_SURFACE_WG, Integer.MIN_VALUE, -40), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), BiomeFilter.biome());
		register(context, CAVE_GROWTH_GROVE, CCConfiguredFeatures.CAVE_GROWTH_GROVE, RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, SurfaceRelativeThresholdFilter.of(Heightmap.Types.WORLD_SURFACE_WG, Integer.MIN_VALUE, -6), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), BiomeFilter.biome());

		register(context, FALSE_HOPE, CCConfiguredFeatures.FALSE_HOPE, CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), SurfaceRelativeThresholdFilter.of(Heightmap.Types.WORLD_SURFACE_WG, Integer.MIN_VALUE, -8), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), BiomeFilter.biome());
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, CavernsAndChasms.location(name));
	}

	public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
		context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
	}

	public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		register(context, key, feature, List.of(modifiers));
	}
}