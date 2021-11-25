package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCBlockTagsProvider extends BlockTagsProvider {

	public CCBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags() {
		this.tag(BlockTags.BEACON_BASE_BLOCKS).add(CCBlocks.SILVER_BLOCK.get(), CCBlocks.NECROMIUM_BLOCK.get());
		this.tag(BlockTags.BUTTONS).add(CCBlocks.SILVER_BUTTON.get());
		this.tag(BlockTags.CAMPFIRES).add(CCBlocks.CURSED_CAMPFIRE.get());
		this.tag(BlockTags.FIRE).add(CCBlocks.CURSED_FIRE.get());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(CCBlocks.GOLDEN_BARS.get(), CCBlocks.GOLDEN_LANTERN.get());
		this.tag(BlockTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get());
		this.tag(BlockTags.PRESSURE_PLATES).add(CCBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE.get());
		this.tag(BlockTags.RAILS).add(CCBlocks.SPIKED_RAIL.get());
		this.tag(BlockTags.SLABS).add(CCBlocks.DIRT_BRICK_SLAB.get(), CCBlocks.DIRT_TILE_SLAB.get(), CCBlocks.COBBLESTONE_BRICK_SLAB.get(), CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.SANGUINE_SLAB.get());
		this.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).add(CCBlocks.SOUL_SILVER_ORE.get());
		this.tag(BlockTags.SOUL_SPEED_BLOCKS).add(CCBlocks.SOUL_SILVER_ORE.get());
		this.tag(BlockTags.STAIRS).add(CCBlocks.DIRT_BRICK_STAIRS.get(), CCBlocks.DIRT_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.SANGUINE_STAIRS.get());
		this.tag(BlockTags.WALL_POST_OVERRIDE).add(CCBlocks.CURSED_TORCH.get());
		this.tag(BlockTags.WALLS).add(CCBlocks.DIRT_BRICK_WALL.get(), CCBlocks.DIRT_TILE_WALL.get(), CCBlocks.COBBLESTONE_BRICK_WALL.get(), CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_BRICK_WALL.get());

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CCBlocks.ROCKY_DIRT.get(), CCBlocks.SILVER_BLOCK.get(), CCBlocks.RAW_SILVER_BLOCK.get(), CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SILVER_BARS.get(), CCBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), CCBlocks.SILVER_BUTTON.get(), CCBlocks.SPIKED_RAIL.get(), CCBlocks.SANGUINE_PLATES.get(), CCBlocks.SANGUINE_STAIRS.get(), CCBlocks.SANGUINE_SLAB.get(), CCBlocks.SANGUINE_VERTICAL_SLAB.get(), CCBlocks.CURSED_LANTERN.get(), CCBlocks.BRAZIER.get(), CCBlocks.SOUL_BRAZIER.get(), CCBlocks.CURSED_BRAZIER.get(), CCBlocks.ENDER_BRAZIER.get(), CCBlocks.GRAVESTONE.get(), CCBlocks.GOLDEN_LANTERN.get(), CCBlocks.GOLDEN_BARS.get(), CCBlocks.SPINEL_BLOCK.get(), CCBlocks.SPINEL_LAMP.get(), CCBlocks.SPINEL_PILLAR.get(), CCBlocks.SPINEL_BRICKS.get(), CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.SPINEL_BRICK_VERTICAL_SLAB.get(), CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get(), CCBlocks.NECROMIUM_BLOCK.get(), CCBlocks.LAPIS_LAZULI_LAMP.get(), CCBlocks.LAPIS_LAZULI_PILLAR.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get(), CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.COBBLESTONE_BRICK_SLAB.get(), CCBlocks.COBBLESTONE_BRICK_WALL.get(), CCBlocks.COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_TILES.get(), CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get());
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(CCBlocks.CURSED_CAMPFIRE.get());
		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(CCBlocks.ROCKY_DIRT.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCBlocks.DIRT_BRICKS.get(), CCBlocks.DIRT_BRICK_STAIRS.get(), CCBlocks.DIRT_BRICK_SLAB.get(), CCBlocks.DIRT_BRICK_WALL.get(), CCBlocks.DIRT_BRICK_VERTICAL_SLAB.get(), CCBlocks.DIRT_TILES.get(), CCBlocks.DIRT_TILE_STAIRS.get(), CCBlocks.DIRT_TILE_SLAB.get(), CCBlocks.DIRT_TILE_WALL.get(), CCBlocks.DIRT_TILE_VERTICAL_SLAB.get());
		this.tag(BlockTags.MINEABLE_WITH_HOE).add(CCBlocks.ROTTEN_FLESH_BLOCK.get());
		this.tag(BlockTags.NEEDS_STONE_TOOL).add(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get(), CCBlocks.SPINEL_BLOCK.get(), CCBlocks.SPINEL_LAMP.get(), CCBlocks.SPINEL_PILLAR.get(), CCBlocks.SPINEL_BRICKS.get(), CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.SPINEL_BRICK_VERTICAL_SLAB.get(), CCBlocks.LAPIS_LAZULI_LAMP.get(), CCBlocks.LAPIS_LAZULI_PILLAR.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get(), CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get());
		this.tag(BlockTags.NEEDS_IRON_TOOL).add(CCBlocks.SILVER_BLOCK.get(), CCBlocks.RAW_SILVER_BLOCK.get(), CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get());
		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(CCBlocks.NECROMIUM_BLOCK.get());

		this.tag(CCBlockTags.BRAZIERS).add(CCBlocks.BRAZIER.get(), CCBlocks.SOUL_BRAZIER.get(), CCBlocks.ENDER_BRAZIER.get(), CCBlocks.CURSED_BRAZIER.get());
		this.tag(CCBlockTags.CURSED_FIRE_BASE_BLOCKS).add(CCBlocks.ROTTEN_FLESH_BLOCK.get());
		this.tag(CCBlockTags.DEEPER_SPAWN_BLOCKS).add(Blocks.GRAVEL).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(Tags.Blocks.ORES);
		this.tag(CCBlockTags.IGNORE_RAIL_PLACEMENT);
		this.tag(CCBlockTags.SILVER_ORES).add(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get());
		this.tag(CCBlockTags.SPINEL_ORES).add(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());
		this.tag(CCBlockTags.PROSPECTING_METALS).addTag(Tags.Blocks.ORES_IRON).addTag(Tags.Blocks.ORES_GOLD).addTag(Tags.Blocks.ORES_NETHERITE_SCRAP).addTag(CCBlockTags.ORES_SILVER).addTag(CCBlockTags.ORES_COPPER);
		this.tag(CCBlockTags.TREASURING_GEMS).addTag(CCBlockTags.AMETHYST).addTag(Tags.Blocks.ORES_DIAMOND).addTag(Tags.Blocks.ORES_EMERALD).addTag(Tags.Blocks.ORES_LAPIS).addTag(CCBlockTags.ORES_SPINEL);
		this.tag(CCBlockTags.AMETHYST).add(Blocks.AMETHYST_BLOCK, Blocks.AMETHYST_CLUSTER, Blocks.BUDDING_AMETHYST, Blocks.LARGE_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.SMALL_AMETHYST_BUD);

		this.tag(Tags.Blocks.DIRT).add(CCBlocks.DIRT_BRICKS.get(), CCBlocks.DIRT_BRICK_STAIRS.get(), CCBlocks.DIRT_BRICK_SLAB.get(), CCBlocks.DIRT_BRICK_WALL.get(), CCBlocks.DIRT_BRICK_VERTICAL_SLAB.get(), CCBlocks.DIRT_TILES.get(), CCBlocks.DIRT_TILE_STAIRS.get(), CCBlocks.DIRT_TILE_SLAB.get(), CCBlocks.DIRT_TILE_WALL.get(), CCBlocks.DIRT_TILE_VERTICAL_SLAB.get());
		this.tag(CCBlockTags.STORAGE_BLOCKS_COPPER).add(Blocks.COPPER_BLOCK);
		this.tag(CCBlockTags.STORAGE_BLOCKS_SILVER).add(CCBlocks.SILVER_BLOCK.get());
		this.tag(CCBlockTags.STORAGE_BLOCKS_SPINEL).add(CCBlocks.SPINEL_BLOCK.get());
		this.tag(CCBlockTags.STORAGE_BLOCKS_NECROMIUM).add(CCBlocks.NECROMIUM_BLOCK.get());
		this.tag(CCBlockTags.STORAGE_BLOCKS_RAW_COPPER).add(Blocks.RAW_COPPER_BLOCK);
		this.tag(CCBlockTags.STORAGE_BLOCKS_RAW_IRON).add(Blocks.RAW_IRON_BLOCK);
		this.tag(CCBlockTags.STORAGE_BLOCKS_RAW_GOLD).add(Blocks.RAW_GOLD_BLOCK);
		this.tag(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER).add(CCBlocks.RAW_SILVER_BLOCK.get());
		this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(CCBlockTags.STORAGE_BLOCKS_COPPER).addTag(CCBlockTags.STORAGE_BLOCKS_SILVER).addTag(CCBlockTags.STORAGE_BLOCKS_SPINEL).addTag(CCBlockTags.STORAGE_BLOCKS_NECROMIUM).addTag(CCBlockTags.STORAGE_BLOCKS_RAW_COPPER).addTag(CCBlockTags.STORAGE_BLOCKS_RAW_IRON).addTag(CCBlockTags.STORAGE_BLOCKS_RAW_GOLD).addTag(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER);
		this.tag(Tags.Blocks.ORES).addTag(CCBlockTags.ORES_COPPER).addTag(CCBlockTags.ORES_SILVER).addTag(CCBlockTags.ORES_SPINEL);
		this.tag(Tags.Blocks.ORES_COAL).remove(Blocks.COAL_ORE).addTag(BlockTags.COAL_ORES);
		this.tag(Tags.Blocks.ORES_EMERALD).remove(Blocks.EMERALD_ORE).addTag(BlockTags.EMERALD_ORES);
		this.tag(Tags.Blocks.ORES_DIAMOND).remove(Blocks.DIAMOND_ORE).addTag(BlockTags.DIAMOND_ORES);
		this.tag(Tags.Blocks.ORES_IRON).remove(Blocks.IRON_ORE).addTag(BlockTags.IRON_ORES);
		this.tag(Tags.Blocks.ORES_LAPIS).remove(Blocks.LAPIS_ORE).addTag(BlockTags.LAPIS_ORES);
		this.tag(Tags.Blocks.ORES_REDSTONE).remove(Blocks.REDSTONE_ORE).addTag(BlockTags.REDSTONE_ORES);
		this.tag(CCBlockTags.ORES_SILVER).addTag(CCBlockTags.SILVER_ORES);
		this.tag(CCBlockTags.ORES_SPINEL).addTag(CCBlockTags.SPINEL_ORES);
		this.tag(CCBlockTags.ORES_COPPER).addTag(BlockTags.COPPER_ORES);

		this.tag(BlueprintBlockTags.VERTICAL_SLABS).add(CCBlocks.DIRT_BRICK_VERTICAL_SLAB.get(), CCBlocks.DIRT_TILE_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.SPINEL_BRICK_VERTICAL_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get(), CCBlocks.SANGUINE_VERTICAL_SLAB.get());

	}
}