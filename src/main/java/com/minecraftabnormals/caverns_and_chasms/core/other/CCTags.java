package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class CCTags {

	public static class Blocks {
		public static final ITag.INamedTag<Block> DEEPER_SPAWN_BLOCKS = createTag("deeper_spawn_blocks");
		public static final ITag.INamedTag<Block> PROSPECTING_METALS = createTag("prospecting_metals");
		public static final ITag.INamedTag<Block> TREASURING_GEMS = createTag("treasuring_gems");
		public static final ITag.INamedTag<Block> CURSED_FIRE_BASE_BLOCKS = createTag("cursed_fire_base_blocks");
		public static final ITag.INamedTag<Block> BRAZIERS = createTag("braziers");
		public static final ITag.INamedTag<Block> IGNORE_RAIL_PLACEMENT = createTag("ignore_rail_placement");

		private static ITag.INamedTag<Block> createTag(String name) {
			return BlockTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}

	public static class Items {
		public static final ITag.INamedTag<Item> IGNORE_RAIL_PLACEMENT = createTag("ignore_rail_placement");
		public static final ITag.INamedTag<Item> EXPERIENCE_BOOST_ITEMS = createTag("experience_boost_items");
		public static final ITag.INamedTag<Item> AFFLICTION_ITEMS = createTag("affliction_items");

		private static ITag.INamedTag<Item> createTag(String name) {
			return ItemTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}

	public static class EntityTypes {
		public static final ITag.INamedTag<EntityType<?>> COLLAR_DROP_MOBS = createTag("collar_drop_mobs");
		public static final ITag.INamedTag<EntityType<?>> UNDEAD_PETS = createTag("undead_pets");

		private static ITag.INamedTag<EntityType<?>> createTag(String name) {
			return EntityTypeTags.bind(CavernsAndChasms.MOD_ID + ":" + name);
		}
	}
}