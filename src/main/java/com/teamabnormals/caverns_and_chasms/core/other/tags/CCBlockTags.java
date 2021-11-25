package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class CCBlockTags {
	public static final Tag.Named<Block> DEEPER_SPAWN_BLOCKS = blockTag("deeper_spawn_blocks");
	public static final Tag.Named<Block> PROSPECTING_METALS = blockTag("prospecting_metals");
	public static final Tag.Named<Block> TREASURING_GEMS = blockTag("treasuring_gems");
	public static final Tag.Named<Block> CURSED_FIRE_BASE_BLOCKS = blockTag("cursed_fire_base_blocks");
	public static final Tag.Named<Block> BRAZIERS = blockTag("braziers");
	public static final Tag.Named<Block> IGNORE_RAIL_PLACEMENT = blockTag("ignore_rail_placement");
	public static final Tag.Named<Block> SILVER_ORES = blockTag("silver_ores");
	public static final Tag.Named<Block> SPINEL_ORES = blockTag("spinel_ores");

	public static final Tag.Named<Block> AMETHYST = TagUtil.forgeBlockTag("amethyst");
	public static final Tag.Named<Block> ORES_SILVER = TagUtil.forgeBlockTag("ores/silver");
	public static final Tag.Named<Block> ORES_COPPER = TagUtil.forgeBlockTag("ores/copper");
	public static final Tag.Named<Block> ORES_SPINEL = TagUtil.forgeBlockTag("ores/spinel");

	public static final Tag.Named<Block> STORAGE_BLOCKS_COPPER = TagUtil.forgeBlockTag("storage_blocks/copper");
	public static final Tag.Named<Block> STORAGE_BLOCKS_SILVER = TagUtil.forgeBlockTag("storage_blocks/silver");
	public static final Tag.Named<Block> STORAGE_BLOCKS_SPINEL = TagUtil.forgeBlockTag("storage_blocks/spinel");
	public static final Tag.Named<Block> STORAGE_BLOCKS_NECROMIUM = TagUtil.forgeBlockTag("storage_blocks/necromium");
	public static final Tag.Named<Block> STORAGE_BLOCKS_RAW_IRON = TagUtil.forgeBlockTag("storage_blocks/raw_iron");
	public static final Tag.Named<Block> STORAGE_BLOCKS_RAW_COPPER = TagUtil.forgeBlockTag("storage_blocks/raw_copper");
	public static final Tag.Named<Block> STORAGE_BLOCKS_RAW_GOLD = TagUtil.forgeBlockTag("storage_blocks/raw_gold");
	public static final Tag.Named<Block> STORAGE_BLOCKS_RAW_SILVER = TagUtil.forgeBlockTag("storage_blocks/raw_silver");

	private static Tag.Named<Block> blockTag(String name) {
		return TagUtil.blockTag(CavernsAndChasms.MOD_ID, name);
	}
}