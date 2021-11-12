package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;

public class CCTags {

	public static class Blocks {
		public static final Tag.Named<Block> DEEPER_SPAWN_BLOCKS = createTag("deeper_spawn_blocks");
		public static final Tag.Named<Block> PROSPECTING_METALS = createTag("prospecting_metals");
		public static final Tag.Named<Block> TREASURING_GEMS = createTag("treasuring_gems");
		public static final Tag.Named<Block> CURSED_FIRE_BASE_BLOCKS = createTag("cursed_fire_base_blocks");
		public static final Tag.Named<Block> BRAZIERS = createTag("braziers");
		public static final Tag.Named<Block> IGNORE_RAIL_PLACEMENT = createTag("ignore_rail_placement");

		private static Tag.Named<Block> createTag(String name) {
			return BlockTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}

	public static class Items {
		public static final Tag.Named<Item> IGNORE_RAIL_PLACEMENT = createTag("ignore_rail_placement");
		public static final Tag.Named<Item> EXPERIENCE_BOOST_ITEMS = createTag("experience_boost_items");
		public static final Tag.Named<Item> AFFLICTION_ITEMS = createTag("affliction_items");

		private static Tag.Named<Item> createTag(String name) {
			return ItemTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}

	public static class EntityTypes {
		public static final Tag.Named<EntityType<?>> COLLAR_DROP_MOBS = createTag("collar_drop_mobs");
		public static final Tag.Named<EntityType<?>> UNDEAD_PETS = createTag("undead_pets");

		private static Tag.Named<EntityType<?>> createTag(String name) {
			return EntityTypeTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}
}