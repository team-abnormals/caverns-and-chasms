package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintTrimMaterialTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimMaterials;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCTrimMaterialTagsProvider extends TagsProvider<TrimMaterial> {

	public CCTrimMaterialTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, Registries.TRIM_MATERIAL, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(BlueprintTrimMaterialTags.GENERATES_OVERRIDES).add(
				CCTrimMaterials.SANGUINE,
				CCTrimMaterials.SILVER,
				CCTrimMaterials.TIN,
				CCTrimMaterials.SPINEL,
				CCTrimMaterials.ZIRCONIA,
				CCTrimMaterials.TURQUOISE,
				CCTrimMaterials.NECROMIUM,
				CCTrimMaterials.WAXED_COPPER,
				CCTrimMaterials.EXPOSED_COPPER, CCTrimMaterials.WAXED_EXPOSED_COPPER,
				CCTrimMaterials.WEATHERED_COPPER, CCTrimMaterials.WAXED_WEATHERED_COPPER,
				CCTrimMaterials.OXIDIZED_COPPER, CCTrimMaterials.WAXED_OXIDIZED_COPPER
		);
	}
}