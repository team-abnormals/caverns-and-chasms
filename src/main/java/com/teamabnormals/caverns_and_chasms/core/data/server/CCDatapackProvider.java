package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCProcessorLists;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructureSets;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCTemplatePools;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.*;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CCDatapackProvider extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Keys.BIOME_MODIFIERS, CCBiomeModifiers::bootstrap)
			.add(Registries.CONFIGURED_FEATURE, CCConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, CCPlacedFeatures::bootstrap)
			.add(Registries.NOISE, CCNoiseParameters::bootstrap)
			.add(Registries.TEMPLATE_POOL, CCTemplatePools::bootstrap)
			.add(Registries.PROCESSOR_LIST, CCProcessorLists::bootstrap)
			.add(Registries.STRUCTURE, CCStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, CCStructureSets::bootstrap)
			.add(Registries.DAMAGE_TYPE, CCDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, CCTrimMaterials::bootstrap)
			.add(Registries.TRIM_PATTERN, CCTrimPatterns::bootstrap)
			.add(Registries.ENCHANTMENT, CCEnchantments::bootstrap)
			.add(Registries.JUKEBOX_SONG, CCJukeboxSongs::bootstrap)
			.add(Registries.PAINTING_VARIANT, CCPaintingVariants::bootstrap)
			.add(Registries.BANNER_PATTERN, CCBannerPatterns::bootstrap)
			.add(CCRegistries.RAT_VARIANT, CCRatVariants::bootstrap)
			.add(BlueprintDataPackRegistries.STRUCTURE_REPALETTERS, CCStructureRepaletters::bootstrap);

	public CCDatapackProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider, BUILDER, CCStructureRepaletters::applyConditions, Set.of(CavernsAndChasms.MOD_ID, "minecraft"));
	}
}