package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintTrimMaterialTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCTrimMaterials;
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
				CCTrimMaterials.SPINEL,
				CCTrimMaterials.ZIRCONIA,
				CCTrimMaterials.NECROMIUM
		);
	}
}