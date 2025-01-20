package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCBiomeModifiers;
import com.teamabnormals.caverns_and_chasms.core.other.CCDamageTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCConfiguredFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCNoiseParameters;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCPlacedFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructureSets;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCTrimMaterials;
import com.teamabnormals.caverns_and_chasms.core.registry.CCTrimPatterns;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CCDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, CCConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, CCPlacedFeatures::bootstrap)
			.add(Registries.NOISE, CCNoiseParameters::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, CCBiomeModifiers::bootstrap)
			.add(Registries.STRUCTURE, CCStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, CCStructureSets::bootstrap)
			.add(Registries.DAMAGE_TYPE, CCDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, CCTrimMaterials::bootstrap)
			.add(Registries.TRIM_PATTERN, CCTrimPatterns::bootstrap);

	public CCDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider, BUILDER, Set.of(CavernsAndChasms.MOD_ID, "minecraft"));
	}
}