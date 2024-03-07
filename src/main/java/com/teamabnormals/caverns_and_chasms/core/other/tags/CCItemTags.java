package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CCItemTags {
	public static final TagKey<Item> IGNORE_RAIL_PLACEMENT = itemTag("ignore_rail_placement");
	public static final TagKey<Item> CUPRIC_FIRE_BASE_BLOCKS = itemTag("cupric_fire_base_blocks");
	public static final TagKey<Item> MAGIC_DAMAGE_ITEMS = itemTag("magic_damage_items");
	public static final TagKey<Item> EXPERIENCE_BOOST_ITEMS = itemTag("experience_boost_items");
	public static final TagKey<Item> SLOWNESS_INFLICTING_ITEMS = itemTag("slowness_inflicting_items");
	public static final TagKey<Item> SILVER_ORES = itemTag("silver_ores");
	public static final TagKey<Item> SPINEL_ORES = itemTag("spinel_ores");
	public static final TagKey<Item> AZALEA_LOGS = itemTag("azalea_logs");
	public static final TagKey<Item> ADDITIONAL_TOOLBOX_TOOLS = itemTag("additional_toolbox_tools");

	public static final TagKey<Item> GLARE_FOOD = itemTag("glare_food");
	public static final TagKey<Item> RAT_FOOD = itemTag("rat_food");
	public static final TagKey<Item> RAT_TAME_ITEMS = itemTag("rat_tame_items");

	public static final TagKey<Item> INGOTS_NECROMIUM = TagUtil.itemTag("forge", "ingots/necromium");
	public static final TagKey<Item> INGOTS_SILVER = TagUtil.itemTag("forge", "ingots/silver");

	public static final TagKey<Item> RAW_MATERIALS_SILVER = TagUtil.itemTag("forge", "raw_materials/silver");

	public static final TagKey<Item> NUGGETS_COPPER = TagUtil.itemTag("forge", "nuggets/copper");
	public static final TagKey<Item> NUGGETS_NETHERITE = TagUtil.itemTag("forge", "nuggets/netherite");
	public static final TagKey<Item> NUGGETS_SILVER = TagUtil.itemTag("forge", "nuggets/silver");
	public static final TagKey<Item> NUGGETS_NECROMIUM = TagUtil.itemTag("forge", "nuggets/necromium");

	public static final TagKey<Item> ORES_AMETHYST = TagUtil.itemTag("forge", "ores/amethyst");
	public static final TagKey<Item> ORES_SILVER = TagUtil.itemTag("forge", "ores/silver");
	public static final TagKey<Item> ORES_SPINEL = TagUtil.itemTag("forge", "ores/spinel");

	public static final TagKey<Item> STORAGE_BLOCKS_SILVER = TagUtil.itemTag("forge", "storage_blocks/silver");
	public static final TagKey<Item> STORAGE_BLOCKS_SPINEL = TagUtil.itemTag("forge", "storage_blocks/spinel");
	public static final TagKey<Item> STORAGE_BLOCKS_NECROMIUM = TagUtil.itemTag("forge", "storage_blocks/necromium");
	public static final TagKey<Item> STORAGE_BLOCKS_RAW_SILVER = TagUtil.itemTag("forge", "storage_blocks/raw_silver");

	public static final TagKey<Item> TOOLS_SWORDS_SILVER = TagUtil.itemTag("forge", "tools/swords/silver");
	public static final TagKey<Item> TOOLS_PICKAXES_SILVER = TagUtil.itemTag("forge", "tools/pickaxes/silver");
	public static final TagKey<Item> TOOLS_AXES_SILVER = TagUtil.itemTag("forge", "tools/axes/silver");
	public static final TagKey<Item> TOOLS_SHOVELS_SILVER = TagUtil.itemTag("forge", "tools/shovels/silver");
	public static final TagKey<Item> TOOLS_HOES_SILVER = TagUtil.itemTag("forge", "tools/hoes/silver");
	public static final TagKey<Item> ARMORS_HELMETS_SILVER = TagUtil.itemTag("forge", "armors/helmets/silver");
	public static final TagKey<Item> ARMORS_CHESTPLATES_SILVER = TagUtil.itemTag("forge", "armors/chestplates/silver");
	public static final TagKey<Item> ARMORS_LEGGINGS_SILVER = TagUtil.itemTag("forge", "armors/leggings/silver");
	public static final TagKey<Item> ARMORS_BOOTS_SILVER = TagUtil.itemTag("forge", "armors/boots/silver");

	public static final TagKey<Item> TOOLS_SWORDS_NECROMIUM = TagUtil.itemTag("forge", "tools/swords/necromium");
	public static final TagKey<Item> TOOLS_PICKAXES_NECROMIUM = TagUtil.itemTag("forge", "tools/pickaxes/necromium");
	public static final TagKey<Item> TOOLS_AXES_NECROMIUM = TagUtil.itemTag("forge", "tools/axes/necromium");
	public static final TagKey<Item> TOOLS_SHOVELS_NECROMIUM = TagUtil.itemTag("forge", "tools/shovels/necromium");
	public static final TagKey<Item> TOOLS_HOES_NECROMIUM = TagUtil.itemTag("forge", "tools/hoes/necromium");
	public static final TagKey<Item> ARMORS_HELMETS_NECROMIUM = TagUtil.itemTag("forge", "armors/helmets/necromium");
	public static final TagKey<Item> ARMORS_CHESTPLATES_NECROMIUM = TagUtil.itemTag("forge", "armors/chestplates/necromium");
	public static final TagKey<Item> ARMORS_LEGGINGS_NECROMIUM = TagUtil.itemTag("forge", "armors/leggings/necromium");
	public static final TagKey<Item> ARMORS_BOOTS_NECROMIUM = TagUtil.itemTag("forge", "armors/boots/necromium");

	public static final TagKey<Item> BOTTLES_MILK = TagUtil.itemTag("forge", "bottles/milk");

	public static final TagKey<Item> ENDER_FIRE_BASE_BLOCKS = TagUtil.itemTag("endergetic", "ender_fire_base_blocks");

	private static TagKey<Item> itemTag(String name) {
		return TagUtil.itemTag(CavernsAndChasms.MOD_ID, name);
	}
}