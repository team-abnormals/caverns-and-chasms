package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCBiomeTagsProvider extends BiomeTagsProvider {

	public CCBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(CCBiomeTags.HAS_SPINEL_ORE).add(Biomes.LUSH_CAVES);
		this.tag(CCBiomeTags.HAS_SILVER_ORE).addTag(Tags.Biomes.IS_COLD_OVERWORLD).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST);
		this.tag(CCBiomeTags.HAS_EXTRA_SILVER_ORE).add(Biomes.ICE_SPIKES);
		this.tag(CCBiomeTags.HAS_SOUL_SILVER_ORE).add(Biomes.SOUL_SAND_VALLEY);
		this.tag(CCBiomeTags.HAS_ROCKY_DIRT).addTag(BiomeTags.IS_OVERWORLD);
		this.tag(CCBiomeTags.HAS_FRAGILE_STONE).addTag(BiomeTags.IS_OVERWORLD);

		this.tag(CCBiomeTags.HAS_LURID_CAVE_GROWTHS).addTag(Tags.Biomes.IS_COLD);
		this.tag(CCBiomeTags.HAS_WISPY_CAVE_GROWTHS).addTag(Tags.Biomes.IS_SNOWY).addTag(BiomeTags.IS_OCEAN);
		this.tag(CCBiomeTags.HAS_ZESTY_CAVE_GROWTHS).addTag(Tags.Biomes.IS_HOT);
		this.tag(CCBiomeTags.HAS_GRAINY_CAVE_GROWTHS).addTag(Tags.Biomes.IS_DESERT);

		this.tag(CCBiomeTags.WITHOUT_LURID_CAVE_GROWTHS).addTag(Tags.Biomes.IS_SNOWY).addTag(BiomeTags.IS_OCEAN);
		this.tag(CCBiomeTags.WITHOUT_ZESTY_CAVE_GROWTHS).addTag(Tags.Biomes.IS_DESERT).addTag(BiomeTags.IS_OCEAN);
		this.tag(CCBiomeTags.WITHOUT_GRAINY_CAVE_GROWTHS).addTag(BiomeTags.IS_OCEAN);

		this.tag(CCBiomeTags.HAS_MIME).addTag(BlueprintBiomeTags.WITH_DEFAULT_MONSTER_SPAWNS);
		this.tag(CCBiomeTags.HAS_GLARE).addTag(BlueprintBiomeTags.WITH_DEFAULT_MONSTER_SPAWNS).add(Biomes.MUSHROOM_FIELDS);
		this.tag(CCBiomeTags.HAS_LOST_GOAT).addTag(Tags.Biomes.IS_PEAK).add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.MEADOW, Biomes.SNOWY_SLOPES, Biomes.GROVE);
	}
}