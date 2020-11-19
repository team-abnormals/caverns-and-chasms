package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class CCTags {

	public static class Blocks {
		public static final ITag.INamedTag<Block> DEEPER_SPAWN_BLOCKS = createTag("deeper_spawn_blocks");
		public static final ITag.INamedTag<Block> PROSPECTING_METALS = createTag("prospecting_metals");
		public static final ITag.INamedTag<Block> TREASURING_GEMS = createTag("treasuring_gems");
		public static final ITag.INamedTag<Block> CURSED_FIRE_BASE_BLOCKS = createTag("cursed_fire_base_blocks");

		private static ITag.INamedTag<Block> createTag(String name) {
			return BlockTags.makeWrapperTag(CavernsAndChasms.MODID + ":" + name);
		}
	}

	public static class Items {
	}
}
