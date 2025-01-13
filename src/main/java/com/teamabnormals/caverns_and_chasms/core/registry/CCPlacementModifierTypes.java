package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.levelgen.placement.BetterNoiseBasedCountPlacement;
import com.teamabnormals.caverns_and_chasms.common.levelgen.placement.NoiseBasedRarityFilter;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCPlacementModifierTypes {
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<PlacementModifierType<BetterNoiseBasedCountPlacement>> BETTER_NOISE_BASED_COUNT = PLACEMENT_MODIFIER_TYPES.register("better_noise_based_count", () -> () -> BetterNoiseBasedCountPlacement.CODEC);
	public static final RegistryObject<PlacementModifierType<NoiseBasedRarityFilter>> NOISE_BASED_RARITY_FILTER = PLACEMENT_MODIFIER_TYPES.register("noise_based_rarity_filter", () -> () -> NoiseBasedRarityFilter.CODEC);
}