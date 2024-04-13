package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCTrimMaterials;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

public final class CCSpriteSourceProvider extends SpriteSourceProvider {

	public CCSpriteSourceProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, helper, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void addSources() {
		this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
				.addSource(BlueprintTrims.materialPatternPermutations(
						CCTrimMaterials.SPINEL,
						CCTrimMaterials.SILVER,
						CCTrimMaterials.SILVER_DARKER,
						CCTrimMaterials.NECROMIUM,
						CCTrimMaterials.NECROMIUM_DARKER,
						CCTrimMaterials.SANGUINE,
						CCTrimMaterials.SANGUINE_DARKER
				));
		this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
				.addSource(new DirectoryLister("entity/toolbox", "entity/toolbox/"))
				.addSource(BlueprintTrims.materialPermutationsForItemLayers(
						CCTrimMaterials.SPINEL,
						CCTrimMaterials.SILVER,
						CCTrimMaterials.SILVER_DARKER,
						CCTrimMaterials.NECROMIUM,
						CCTrimMaterials.NECROMIUM_DARKER,
						CCTrimMaterials.SANGUINE,
						CCTrimMaterials.SANGUINE_DARKER
				));
	}

}