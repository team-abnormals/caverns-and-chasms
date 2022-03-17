package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCBlockTags {
	public static final TagKey<Block> DEEPER_SPAWNABLE_BLOCKS = blockTag("deeper_spawnable_blocks");
	public static final TagKey<Block> PROSPECTING_METALS = blockTag("prospecting_metals");
	public static final TagKey<Block> TREASURING_GEMS = blockTag("treasuring_gems");
	public static final TagKey<Block> CURSED_FIRE_BASE_BLOCKS = blockTag("cursed_fire_base_blocks");
	public static final TagKey<Block> BRAZIERS = blockTag("braziers");
	public static final TagKey<Block> IGNORE_RAIL_PLACEMENT = blockTag("ignore_rail_placement");
	public static final TagKey<Block> SILVER_ORES = blockTag("silver_ores");
	public static final TagKey<Block> SPINEL_ORES = blockTag("spinel_ores");
	public static final TagKey<Block> AZALEA_LOGS = blockTag("azalea_logs");

	public static final TagKey<Block> WAXABLE_COPPER_BLOCKS = TagUtil.blockTag("forge", "waxable_copper");
	public static final TagKey<Block> WAXED_COPPER_BLOCKS = TagUtil.blockTag("forge", "waxed_copper");
	public static final TagKey<Block> ORES_AMETHYST = TagUtil.blockTag("forge", "ores/amethyst");
	public static final TagKey<Block> ORES_SILVER = TagUtil.blockTag("forge", "ores/silver");
	public static final TagKey<Block> ORES_SPINEL = TagUtil.blockTag("forge", "ores/spinel");
	public static final TagKey<Block> STORAGE_BLOCKS_SILVER = TagUtil.blockTag("forge", "storage_blocks/silver");
	public static final TagKey<Block> STORAGE_BLOCKS_SPINEL = TagUtil.blockTag("forge", "storage_blocks/spinel");
	public static final TagKey<Block> STORAGE_BLOCKS_NECROMIUM = TagUtil.blockTag("forge", "storage_blocks/necromium");
	public static final TagKey<Block> STORAGE_BLOCKS_RAW_SILVER = TagUtil.blockTag("forge", "storage_blocks/raw_silver");

	private static TagKey<Block> blockTag(String name) {
		return TagUtil.blockTag(CavernsAndChasms.MOD_ID, name);
	}
}