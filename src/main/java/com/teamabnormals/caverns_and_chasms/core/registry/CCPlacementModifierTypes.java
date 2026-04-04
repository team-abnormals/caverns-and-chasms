package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.HeightmapRandomOffsetPlacement;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.NoiseDensityPlacement;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.TinMonolithDistanceFilter;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCPlacementModifierTypes {
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<PlacementModifierType<TinMonolithDistanceFilter>> TIN_MONOLITH_DISTANCE_FILTER = PLACEMENT_MODIFIER_TYPES.register("tin_monolith_distance_filter", () -> () -> TinMonolithDistanceFilter.CODEC);
	public static final RegistryObject<PlacementModifierType<HeightmapRandomOffsetPlacement>> HEIGHTMAP_RANDOM_OFFSET = PLACEMENT_MODIFIER_TYPES.register("heightmap_random_offset", () -> () -> HeightmapRandomOffsetPlacement.CODEC);
	public static final RegistryObject<PlacementModifierType<NoiseDensityPlacement>> NOISE_DENSITY = PLACEMENT_MODIFIER_TYPES.register("noise_density", () -> () -> NoiseDensityPlacement.CODEC);
}