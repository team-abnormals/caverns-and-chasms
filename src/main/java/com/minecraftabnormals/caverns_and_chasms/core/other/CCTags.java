package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class CCTags {
	public static final ITag.INamedTag<Block> DEEPER_SPAWN_BLOCKS = BlockTags.makeWrapperTag(CavernsAndChasms.MODID + ":deeper_spawn_blocks");
	public static final ITag.INamedTag<Block> PROSPECTING_METALS = BlockTags.makeWrapperTag(CavernsAndChasms.MODID + ":prospecting_metals");
	public static final ITag.INamedTag<Block> TREASURING_GEMS = BlockTags.makeWrapperTag(CavernsAndChasms.MODID + ":treasuring_gems");

	public static final ITag.INamedTag<Item> XP_BOOST_TOOLS = ItemTags.makeWrapperTag(CavernsAndChasms.MODID + ":xp_boost_tools");
}
