package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class CCBlockTags {
	public static final Tag.Named<Block> DEEPER_SPAWNABLE_BLOCKS = blockTag("deeper_spawnable_blocks");
	public static final Tag.Named<Block> PROSPECTING_METALS = blockTag("prospecting_metals");
	public static final Tag.Named<Block> TREASURING_GEMS = blockTag("treasuring_gems");
	public static final Tag.Named<Block> CURSED_FIRE_BASE_BLOCKS = blockTag("cursed_fire_base_blocks");
	public static final Tag.Named<Block> BRAZIERS = blockTag("braziers");
	public static final Tag.Named<Block> IGNORE_RAIL_PLACEMENT = blockTag("ignore_rail_placement");
	public static final Tag.Named<Block> SILVER_ORES = blockTag("silver_ores");
	public static final Tag.Named<Block> SPINEL_ORES = blockTag("spinel_ores");
	public static final Tag.Named<Block> AZALEA_LOGS = blockTag("azalea_logs");

	public static final Tag.Named<Block> WAXABLE_COPPER_BLOCKS = TagUtil.forgeBlockTag("waxable_copper");
	public static final Tag.Named<Block> WAXED_COPPER_BLOCKS = TagUtil.forgeBlockTag("waxed_copper");
	public static final Tag.Named<Block> ORES_AMETHYST = TagUtil.forgeBlockTag("ores/amethyst");
	public static final Tag.Named<Block> ORES_SILVER = TagUtil.forgeBlockTag("ores/silver");
	public static final Tag.Named<Block> ORES_SPINEL = TagUtil.forgeBlockTag("ores/spinel");
	public static final Tag.Named<Block> STORAGE_BLOCKS_SILVER = TagUtil.forgeBlockTag("storage_blocks/silver");
	public static final Tag.Named<Block> STORAGE_BLOCKS_SPINEL = TagUtil.forgeBlockTag("storage_blocks/spinel");
	public static final Tag.Named<Block> STORAGE_BLOCKS_NECROMIUM = TagUtil.forgeBlockTag("storage_blocks/necromium");
	public static final Tag.Named<Block> STORAGE_BLOCKS_RAW_SILVER = TagUtil.forgeBlockTag("storage_blocks/raw_silver");

	private static Tag.Named<Block> blockTag(String name) {
		return TagUtil.blockTag(CavernsAndChasms.MOD_ID, name);
	}
}