package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.mojang.datafixers.util.Either;
import com.teamabnormals.blueprint.client.renderer.texture.atlas.BlueprintPalettedPermutations;
import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimMaterials;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimPatterns;
import com.teamabnormals.clayworks.core.Clayworks;
import com.teamabnormals.clayworks.core.api.ClayworksTrims;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class CCSpriteSourceProvider extends SpriteSourceProvider {

	public CCSpriteSourceProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, helper, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void addSources() {
		this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
				.addSource(BlueprintTrims.patternPermutationsOfVanillaMaterials(CCTrimPatterns.EXILE, CCTrimPatterns.FORGER, CCTrimPatterns.IMMOLATE, CCTrimPatterns.RIM, CCTrimPatterns.PLATE, CCTrimPatterns.CORE, CCTrimPatterns.SANGUINE, CCTrimPatterns.COPPER))
				.addSource(BlueprintTrims.materialPatternPermutations(
						CCTrimMaterials.SPINEL,
						CCTrimMaterials.ZIRCONIA,
						CCTrimMaterials.SILVER, CCTrimMaterials.SILVER_DARKER,
						CCTrimMaterials.TIN,
						CCTrimMaterials.TURQUOISE,
						CCTrimMaterials.NECROMIUM, CCTrimMaterials.NECROMIUM_DARKER,
						CCTrimMaterials.SANGUINE, CCTrimMaterials.SANGUINE_DARKER,
						CCTrimMaterials.COPPER_DARKER,
						CCTrimMaterials.EXPOSED_COPPER, CCTrimMaterials.EXPOSED_COPPER_DARKER,
						CCTrimMaterials.WEATHERED_COPPER, CCTrimMaterials.WEATHERED_COPPER_DARKER,
						CCTrimMaterials.OXIDIZED_COPPER, CCTrimMaterials.OXIDIZED_COPPER_DARKER
				));
		this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
				.addSource(new DirectoryLister("entity/toolbox", "entity/toolbox/"))
				.addSource(new DirectoryLister("entity/roller_door", "entity/roller_door/"))
				.addSource(new DirectoryLister("entity/winch", "entity/winch/"))
				.addSource(new SingleFile(CavernsAndChasms.location("entity/atoning_table_book"), Optional.empty()))
				.addSource(BlueprintTrims.materialPermutationsForItemLayers(
						CCTrimMaterials.SPINEL,
						CCTrimMaterials.ZIRCONIA,
						CCTrimMaterials.SILVER, CCTrimMaterials.SILVER_DARKER,
						CCTrimMaterials.TIN,
						CCTrimMaterials.TURQUOISE,
						CCTrimMaterials.NECROMIUM, CCTrimMaterials.NECROMIUM_DARKER,
						CCTrimMaterials.SANGUINE, CCTrimMaterials.SANGUINE_DARKER,
						CCTrimMaterials.COPPER_DARKER,
						CCTrimMaterials.EXPOSED_COPPER, CCTrimMaterials.EXPOSED_COPPER_DARKER,
						CCTrimMaterials.WEATHERED_COPPER, CCTrimMaterials.WEATHERED_COPPER_DARKER,
						CCTrimMaterials.OXIDIZED_COPPER, CCTrimMaterials.OXIDIZED_COPPER_DARKER
				));
		this.atlas(ClayworksTrims.DECORATED_POT_ATLAS)
				.addSource(materialPatternPermutations(
						CCTrimMaterials.SPINEL,
						CCTrimMaterials.ZIRCONIA,
						CCTrimMaterials.SILVER,
						CCTrimMaterials.TIN,
						CCTrimMaterials.TURQUOISE,
						CCTrimMaterials.NECROMIUM,
						CCTrimMaterials.SANGUINE,
						CCTrimMaterials.WAXED_COPPER,
						CCTrimMaterials.EXPOSED_COPPER, CCTrimMaterials.WAXED_EXPOSED_COPPER,
						CCTrimMaterials.WEATHERED_COPPER, CCTrimMaterials.WAXED_WEATHERED_COPPER,
						CCTrimMaterials.OXIDIZED_COPPER, CCTrimMaterials.WAXED_OXIDIZED_COPPER
				));
	}

	@SafeVarargs
	public static BlueprintPalettedPermutations materialPatternPermutations(ResourceKey<TrimMaterial>... keys) {
		return new BlueprintPalettedPermutations(Either.left(List.of(new DirectoryLister("entity/decorated_pot_trim_patterns", "entity/decorated_pot_trim_patterns/"))), ClayworksTrims.TRIM_PALETTE_KEY, getPermutations(keys));
	}

	@SafeVarargs
	private static HashMap<String, ResourceLocation> getPermutations(ResourceKey<TrimMaterial>... keys) {
		HashMap<String, ResourceLocation> permutations = new HashMap<>();
		for (var key : keys) {
			ResourceLocation location = key.location();
			String name = location.getNamespace() + "_" + location.getPath();

			if (location.getNamespace().equals("minecraft")) {
				name = location.getPath();
				location = new ResourceLocation(Clayworks.MOD_ID, name);
			}

			if (location.equals(CCTrimMaterials.WAXED_COPPER.location())) {
				permutations.put(name, new ResourceLocation(Clayworks.MOD_ID, "entity/decorated_pot_trim_palettes/copper"));
			} else {
				permutations.put(name, location.withPath(string -> "entity/decorated_pot_trim_palettes/" + string.replace("waxed_", "")));
			}
		}
		return permutations;
	}

}