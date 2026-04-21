package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.common.world.storage.receiver.LevelConcurrentHashMapReceiver;
import com.teamabnormals.blueprint.common.world.storage.receiver.LevelNoiseReceiver;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCNoiseParameters;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector2i;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Feature<OreConfiguration>> ORE_WITH_DIRT = FEATURES.register("ore_with_dirt", () -> new OreWithDirtFeature(OreConfiguration.CODEC));
	public static final RegistryObject<Feature<OreConfiguration>> TIN_ARROW = FEATURES.register("tin_arrow", () -> new TinArrowFeature(OreConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CAVE_GROWTHS_PATCH = FEATURES.register("cave_growths_patch", () -> new CaveGrowthsFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CAVE_GROWTH_GROVE = FEATURES.register("cave_growth_grove", () -> new CaveGrowthGroveFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<LakeFeature.Configuration>> MAGMA_LAKE = FEATURES.register("magma_lake", () -> new MagmaLakeFeature(LakeFeature.Configuration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FALSE_HOPE = FEATURES.register("false_hope", () -> new FalseHopeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<OreConfiguration>> RHYOLITE = FEATURES.register("rhyolite", () -> new RhyoliteFeature(OreConfiguration.CODEC));

	public static final LevelConcurrentHashMapReceiver<Vector2i, Vector2i[]> CLOSEST_MONOLITH_POSITIONS_AT = new LevelConcurrentHashMapReceiver<>();
	public static final LevelNoiseReceiver MOSCHATEL_NOISE = new LevelNoiseReceiver(WorldgenRandom.Algorithm.LEGACY, CCNoiseParameters.CAVE_GROWTHS_MOSCHATEL);
	public static final LevelNoiseReceiver CAVE_GROWTH_GRADIENT = new LevelNoiseReceiver(WorldgenRandom.Algorithm.LEGACY, CCNoiseParameters.CAVE_GROWTH_GRADIENT);
}