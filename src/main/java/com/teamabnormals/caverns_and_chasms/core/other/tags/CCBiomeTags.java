package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CCBiomeTags {
	public static final TagKey<Biome> HAS_SPINEL_ORE = biomeTag("has_feature/spinel_ore");
	public static final TagKey<Biome> HAS_SILVER_ORE = biomeTag("has_feature/silver_ore");
	public static final TagKey<Biome> HAS_EXTRA_SILVER_ORE = biomeTag("has_feature/extra_silver_ore");
	public static final TagKey<Biome> HAS_SOUL_SILVER_ORE = biomeTag("has_feature/soul_silver_ore");
	public static final TagKey<Biome> HAS_ROCKY_DIRT = biomeTag("has_feature/rocky_dirt");
	public static final TagKey<Biome> HAS_FRAGILE_STONE = biomeTag("has_feature/fragile_stone");

	public static final TagKey<Biome> HAS_LURID_CAVE_GROWTHS = biomeTag("has_feature/lurid_cave_growths");
	public static final TagKey<Biome> HAS_WISPY_CAVE_GROWTHS = biomeTag("has_feature/wispy_cave_growths");
	public static final TagKey<Biome> HAS_ZESTY_CAVE_GROWTHS = biomeTag("has_feature/zesty_cave_growths");
	public static final TagKey<Biome> HAS_GRAINY_CAVE_GROWTHS = biomeTag("has_feature/grainy_cave_growths");

	public static final TagKey<Biome> WITHOUT_LURID_CAVE_GROWTHS = biomeTag("without_feature/lurid_cave_growths");
	public static final TagKey<Biome> WITHOUT_WISPY_CAVE_GROWTHS = biomeTag("without_feature/wispy_cave_growths");
	public static final TagKey<Biome> WITHOUT_ZESTY_CAVE_GROWTHS = biomeTag("without_feature/zesty_cave_growths");
	public static final TagKey<Biome> WITHOUT_GRAINY_CAVE_GROWTHS = biomeTag("without_feature/grainy_cave_growths");

	public static final TagKey<Biome> HAS_MIME = biomeTag("has_monster/mime");
	public static final TagKey<Biome> HAS_LOST_GOAT = biomeTag("has_monster/lost_goat");
	public static final TagKey<Biome> HAS_GLARE = biomeTag("has_animal/glare");

	private static TagKey<Biome> biomeTag(String name) {
		return TagUtil.biomeTag(CavernsAndChasms.MOD_ID, name);
	}
}