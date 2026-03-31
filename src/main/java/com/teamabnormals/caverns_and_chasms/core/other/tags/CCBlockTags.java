package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCBlockTags {
	public static final TagKey<Block> DEEPER_SPAWNABLE_ON = blockTag("deeper_spawnable_on");
	public static final TagKey<Block> CUPRIC_FIRE_BASE_BLOCKS = blockTag("cupric_fire_base_blocks");
	public static final TagKey<Block> BRAZIERS = blockTag("braziers");
	public static final TagKey<Block> IGNORE_RAIL_PLACEMENT = blockTag("ignore_rail_placement");
	public static final TagKey<Block> SILVER_ORES = blockTag("silver_ores");
	public static final TagKey<Block> TIN_ORES = blockTag("tin_ores");
	public static final TagKey<Block> SPINEL_ORES = blockTag("spinel_ores");
	public static final TagKey<Block> TURQUOISE_ORES = blockTag("turquoise_ores");
	public static final TagKey<Block> AZALEA_LOGS = blockTag("azalea_logs");
	public static final TagKey<Block> CAVE_GROWTHS = blockTag("cave_growths");
	public static final TagKey<Block> COPPER_RAILS = blockTag("copper_rails");
	public static final TagKey<Block> FLOODLIGHTS = blockTag("floodlights");
	public static final TagKey<Block> DEFLECTS_PROJECTILES = blockTag("deflects_projectiles");
	public static final TagKey<Block> WEAKER_DEFLECT_VELOCITY = blockTag("weaker_deflect_velocity");
	public static final TagKey<Block> WEAKEST_DEFLECT_VELOCITY = blockTag("weakest_deflect_velocity");
	public static final TagKey<Block> MAINTAINS_DEFLECT_VELOCITY = blockTag("maintains_deflect_velocity");
	public static final TagKey<Block> HAS_BONUS_DEFLECT = blockTag("has_bonus_deflect");
	public static final TagKey<Block> STATIC_NOTE_BLOCKS = blockTag("static_note_blocks");
	public static final TagKey<Block> ALARM_NOTE_BLOCKS = blockTag("alarm_note_blocks");
	public static final TagKey<Block> WARDEN_NOTE_BLOCKS = blockTag("warden_note_blocks");
	public static final TagKey<Block> COPPER_GOLEM_SUMMON_BLOCKS = blockTag("copper_golem_summon_blocks");
	public static final TagKey<Block> WINCH_DOES_NOT_UNWIND_ON = blockTag("winch_does_not_unwind_on");
	public static final TagKey<Block> WINCH_FORCES_UNWIND_ON = blockTag("winch_forces_unwind_on");
	public static final TagKey<Block> WINCH_WINDS_FASTER_ON = blockTag("winch_winds_faster_on");
	public static final TagKey<Block> WINCH_WINDS_SLOWER_ON = blockTag("winch_winds_slower_on");
	public static final TagKey<Block> HOLDS_FASTER_ON = blockTag("holds_faster_on");
	public static final TagKey<Block> HOLDS_SLOWER_ON = blockTag("holds_slower_on");
	public static final TagKey<Block> SPARKLERS = blockTag("sparklers");
	public static final TagKey<Block> STANDING_SPARKLERS = blockTag("standing_sparklers");
	public static final TagKey<Block> WALL_SPARKLERS = blockTag("wall_sparklers");
	public static final TagKey<Block> RAT_FOOD_BLOCKS = blockTag("rat_food_blocks");

	public static final TagKey<Block> MOB_INTERACTABLE_DOORS = TagUtil.blockTag("forge", "mob_interactable_doors");
	public static final TagKey<Block> WAXABLE_COPPER_BLOCKS = TagUtil.blockTag("forge", "waxable_copper_blocks");
	public static final TagKey<Block> WAXED_COPPER_BLOCKS = TagUtil.blockTag("forge", "waxed_copper_blocks");
	public static final TagKey<Block> ORES_AMETHYST = TagUtil.blockTag("forge", "ores/amethyst");
	public static final TagKey<Block> ORES_SILVER = TagUtil.blockTag("forge", "ores/silver");
	public static final TagKey<Block> ORES_TIN = TagUtil.blockTag("forge", "ores/tin");
	public static final TagKey<Block> ORES_SPINEL = TagUtil.blockTag("forge", "ores/spinel");
	public static final TagKey<Block> ORES_TURQUOISE = TagUtil.blockTag("forge", "ores/turquoise");
	public static final TagKey<Block> STORAGE_BLOCKS_SILVER = TagUtil.blockTag("forge", "storage_blocks/silver");
	public static final TagKey<Block> STORAGE_BLOCKS_TIN = TagUtil.blockTag("forge", "storage_blocks/tin");
	public static final TagKey<Block> STORAGE_BLOCKS_SPINEL = TagUtil.blockTag("forge", "storage_blocks/spinel");
	public static final TagKey<Block> STORAGE_BLOCKS_TURQUOISE = TagUtil.blockTag("forge", "storage_blocks/turquoise");
	public static final TagKey<Block> STORAGE_BLOCKS_NECROMIUM = TagUtil.blockTag("forge", "storage_blocks/necromium");
	public static final TagKey<Block> STORAGE_BLOCKS_RAW_SILVER = TagUtil.blockTag("forge", "storage_blocks/raw_silver");
	public static final TagKey<Block> STORAGE_BLOCKS_RAW_TIN = TagUtil.blockTag("forge", "storage_blocks/raw_tin");
	public static final TagKey<Block> STORAGE_BLOCKS_ZIRCONIA = TagUtil.blockTag("forge", "storage_blocks/zirconia");
	public static final TagKey<Block> STORAGE_BLOCKS_CHARCOAL = TagUtil.blockTag("forge", "storage_blocks/charcoal");

	private static TagKey<Block> blockTag(String name) {
		return TagUtil.blockTag(CavernsAndChasms.MOD_ID, name);
	}
}