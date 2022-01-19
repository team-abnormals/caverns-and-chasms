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
	public static final Tag.Named<Item> AZALEA_LOGS = itemTag("azalea_logs");

	public static final Tag.Named<Item> INGOTS_NECROMIUM = TagUtil.forgeItemTag("ingots/necromium");
	public static final Tag.Named<Item> INGOTS_SILVER = TagUtil.forgeItemTag("ingots/silver");

	public static final Tag.Named<Item> RAW_MATERIALS_SILVER = TagUtil.forgeItemTag("raw_materials/silver");

	public static final Tag.Named<Item> NUGGETS_NECROMIUM = TagUtil.forgeItemTag("nuggets/necromium");
	public static final Tag.Named<Item> NUGGETS_SILVER = TagUtil.forgeItemTag("nuggets/silver");

	public static final Tag.Named<Item> RAW_FISHES = TagUtil.forgeItemTag("raw_fishes");
	public static final Tag.Named<Item> RAW_FISHES_CAVEFISH = TagUtil.forgeItemTag("raw_fishes/cavefish");
	public static final Tag.Named<Item> COOKED_FISHES = TagUtil.forgeItemTag("cooked_fishes");
	public static final Tag.Named<Item> COOKED_FISHES_CAVEFISH = TagUtil.forgeItemTag("cooked_fishes/cavefish");

	public static final Tag.Named<Item> ORES_AMETHYST = TagUtil.forgeItemTag("ores/amethyst");
	public static final Tag.Named<Item> ORES_SILVER = TagUtil.forgeItemTag("ores/silver");
	public static final Tag.Named<Item> ORES_SPINEL = TagUtil.forgeItemTag("ores/spinel");

	public static final Tag.Named<Item> STORAGE_BLOCKS_SILVER = TagUtil.forgeItemTag("storage_blocks/silver");
	public static final Tag.Named<Item> STORAGE_BLOCKS_SPINEL = TagUtil.forgeItemTag("storage_blocks/spinel");
	public static final Tag.Named<Item> STORAGE_BLOCKS_NECROMIUM = TagUtil.forgeItemTag("storage_blocks/necromium");
	public static final Tag.Named<Item> STORAGE_BLOCKS_RAW_SILVER = TagUtil.forgeItemTag("storage_blocks/raw_silver");

	public static final Tag.Named<Item> TOOLS_PICKAXES = TagUtil.forgeItemTag("tools/pickaxes");
	public static final Tag.Named<Item> TOOLS_AXES = TagUtil.forgeItemTag("tools/axes");
	public static final Tag.Named<Item> TOOLS_SHOVELS = TagUtil.forgeItemTag("tools/shovels");

	private static Tag.Named<Item> itemTag(String name) {
		return TagUtil.itemTag(CavernsAndChasms.MOD_ID, name);
	}
}