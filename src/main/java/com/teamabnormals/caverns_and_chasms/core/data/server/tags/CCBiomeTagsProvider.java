package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.data.server.tags.BlueprintBiomeTagsProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCBiomeTagsProvider extends BlueprintBiomeTagsProvider {

	public CCBiomeTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(CavernsAndChasms.MOD_ID, generator, fileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(CCBiomeTags.HAS_SPINEL_ORE).addTag(BiomeTags.IS_JUNGLE).addTag(BlueprintBiomeTags.IS_SWAMP).add(Biomes.LUSH_CAVES);
		this.tag(CCBiomeTags.HAS_SILVER_ORE);
		this.tag(CCBiomeTags.HAS_EXTRA_SILVER_ORE).add(Biomes.ICE_SPIKES);
		this.tag(CCBiomeTags.HAS_SOUL_SILVER_ORE).add(Biomes.SOUL_SAND_VALLEY);

		this.tag(CCBiomeTags.HAS_MIME).addTag(BiomeTags.IS_JUNGLE).addTag(BlueprintBiomeTags.IS_SWAMP).add(Biomes.LUSH_CAVES);

		this.tag(CCBiomeTags.WITHOUT_CAVEFISH_SPAWNS).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_BEACH).add(Biomes.STONY_SHORE, Biomes.LUSH_CAVES);
	}
}