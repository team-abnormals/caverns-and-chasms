package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class CCItemTags {
	public static final Tag.Named<Item> IGNORE_RAIL_PLACEMENT = itemTag("ignore_rail_placement");
	public static final Tag.Named<Item> CURSED_FIRE_BASE_BLOCKS = itemTag("cursed_fire_base_blocks");
	public static final Tag.Named<Item> AFFLICTION_ITEMS = itemTag("affliction_items");
	public static final Tag.Named<Item> EXPERIENCE_BOOST_ITEMS = itemTag("experience_boost_items");
	public static final Tag.Named<Item> SILVER_ORES = itemTag("silver_ores");
	public static final Tag.Named<Item> SPINEL_ORES = itemTag("spinel_ores");
	public static final Tag.Named<Item> SILVER_GEAR = itemTag("silver_gear");
	public static final Tag.Named<Item> NECROMIUM_GEAR = itemTag("necromium_gear");

	public static final Tag.Named<Item> ORES_SILVER = TagUtil.forgeItemTag("ores/silver");
	public static final Tag.Named<Item> ORES_COPPER = TagUtil.forgeItemTag("ores/copper");
	public static final Tag.Named<Item> ORES_SPINEL = TagUtil.forgeItemTag("ores/spinel");

	public static final Tag.Named<Item> STORAGE_BLOCKS_COPPER = TagUtil.forgeItemTag("storage_blocks/copper");
	public static final Tag.Named<Item> STORAGE_BLOCKS_SILVER = TagUtil.forgeItemTag("storage_blocks/silver");
	public static final Tag.Named<Item> STORAGE_BLOCKS_SPINEL = TagUtil.forgeItemTag("storage_blocks/spinel");
	public static final Tag.Named<Item> STORAGE_BLOCKS_NECROMIUM = TagUtil.forgeItemTag("storage_blocks/necromium");
	public static final Tag.Named<Item> STORAGE_BLOCKS_RAW_IRON = TagUtil.forgeItemTag("storage_blocks/raw_iron");
	public static final Tag.Named<Item> STORAGE_BLOCKS_RAW_COPPER = TagUtil.forgeItemTag("storage_blocks/raw_copper");
	public static final Tag.Named<Item> STORAGE_BLOCKS_RAW_GOLD = TagUtil.forgeItemTag("storage_blocks/raw_gold");
	public static final Tag.Named<Item> STORAGE_BLOCKS_RAW_SILVER = TagUtil.forgeItemTag("storage_blocks/raw_silver");

	private static Tag.Named<Item> itemTag(String name) {
		return TagUtil.itemTag(CavernsAndChasms.MOD_ID, name);
	}
}