package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.block.GoldenLanternBlock;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

	public static final RegistryObject<Block> DIRT_BRICKS = HELPER.createBlock("dirt_bricks", () -> new Block(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_STAIRS = HELPER.createBlock("dirt_brick_stairs", () -> new StairsBlock(() -> DIRT_BRICKS.get().getDefaultState(), Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_SLAB = HELPER.createBlock("dirt_brick_slab", () -> new SlabBlock(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_WALL = HELPER.createBlock("dirt_brick_wall", () -> new WallBlock(Properties.DIRT_BRICKS), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> DIRT_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "dirt_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> DIRT_TILES = HELPER.createBlock("dirt_tiles", () -> new Block(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_STAIRS = HELPER.createBlock("dirt_tile_stairs", () -> new StairsBlock(() -> DIRT_BRICKS.get().getDefaultState(), Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_SLAB = HELPER.createBlock("dirt_tile_slab", () -> new SlabBlock(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_WALL = HELPER.createBlock("dirt_tile_wall", () -> new WallBlock(Properties.DIRT_BRICKS), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> DIRT_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "dirt_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.DIRT_BRICKS), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> COBBLESTONE_TILES = HELPER.createBlock("cobblestone_tiles", () -> new Block(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_STAIRS = HELPER.createBlock("cobblestone_tile_stairs", () -> new StairsBlock(() -> COBBLESTONE_TILES.get().getDefaultState(), Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_SLAB = HELPER.createBlock("cobblestone_tile_slab", () -> new SlabBlock(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_WALL = HELPER.createBlock("cobblestone_tile_wall", () -> new WallBlock(Properties.COBBLESTONE_TILES), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILES = HELPER.createBlock("mossy_cobblestone_tiles", () -> new Block(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_STAIRS = HELPER.createBlock("mossy_cobblestone_tile_stairs", () -> new StairsBlock(() -> COBBLESTONE_TILES.get().getDefaultState(), Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_SLAB = HELPER.createBlock("mossy_cobblestone_tile_slab", () -> new SlabBlock(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_WALL = HELPER.createBlock("mossy_cobblestone_tile_wall", () -> new WallBlock(Properties.COBBLESTONE_TILES), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "mossy_cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_TILES), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> GOLDEN_LANTERN = HELPER.createBlock("golden_lantern", () -> new GoldenLanternBlock(Properties.GOLDEN_LANTERN), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> GOLDEN_BARS = HELPER.createBlock("golden_bars", () -> new PaneBlock(Properties.GOLDEN_BARS), ItemGroup.DECORATIONS);

	public static final RegistryObject<Block> SUGILITE_ORE = HELPER.createBlock("sugilite_ore", () -> new Block(Properties.SUGILITE_ORE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BLOCK = HELPER.createBlock("sugilite_block", () -> new Block(Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICKS = HELPER.createBlock("sugilite_bricks", () -> new Block(Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_STAIRS = HELPER.createBlock("sugilite_brick_stairs", () -> new StairsBlock(() -> SUGILITE_BRICKS.get().getDefaultState(), Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_SLAB = HELPER.createBlock("sugilite_brick_slab", () -> new SlabBlock(Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "sugilite_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_WALL = HELPER.createBlock("sugilite_brick_wall", () -> new WallBlock(Properties.SUGILITE), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> SUGILITE_PILLAR = HELPER.createBlock("sugilite_pillar", () -> new RotatedPillarBlock(Properties.SUGILITE), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_LAMP = HELPER.createBlock("sugilite_lamp", () -> new Block(Properties.LAMP), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> LAPIS_LAZULI_BRICKS = HELPER.createBlock("lapis_bricks", () -> new Block(Properties.LAPIS_LAZULI), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_STAIRS = HELPER.createBlock("lapis_brick_stairs", () -> new StairsBlock(() -> LAPIS_LAZULI_BRICKS.get().getDefaultState(), Properties.LAPIS_LAZULI), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_SLAB = HELPER.createBlock("lapis_brick_slab", () -> new SlabBlock(Properties.LAPIS_LAZULI), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "lapis_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.LAPIS_LAZULI), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_WALL = HELPER.createBlock("lapis_brick_wall", () -> new WallBlock(Properties.LAPIS_LAZULI), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> LAPIS_LAZULI_PILLAR = HELPER.createBlock("lapis_pillar", () -> new RotatedPillarBlock(Properties.LAPIS_LAZULI), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_LAMP = HELPER.createBlock("lapis_lamp", () -> new Block(Properties.LAMP), ItemGroup.BUILDING_BLOCKS);

	static class Properties {
		public static final AbstractBlock.Properties DIRT_BRICKS = AbstractBlock.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.GROUND);
		public static final AbstractBlock.Properties COBBLESTONE_TILES = AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(2.0F, 6.0F);

		public static final AbstractBlock.Properties GOLDEN_LANTERN = Block.Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.LANTERN).setLightLevel((state) -> 15);
		public static final AbstractBlock.Properties GOLDEN_BARS = AbstractBlock.Properties.create(Material.IRON, MaterialColor.AIR).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).notSolid();

		public static final AbstractBlock.Properties SUGILITE_ORE = AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F);
		public static final AbstractBlock.Properties SUGILITE = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.PURPLE).setRequiresTool().hardnessAndResistance(3.0F, 3.0F);
		public static final AbstractBlock.Properties LAPIS_LAZULI = AbstractBlock.Properties.create(Material.IRON, MaterialColor.LAPIS).setRequiresTool().hardnessAndResistance(3.0F, 3.0F);
		public static final AbstractBlock.Properties LAMP = AbstractBlock.Properties.create(Material.REDSTONE_LIGHT).setLightLevel((state) -> 15).hardnessAndResistance(0.3F).sound(SoundType.GLASS).setAllowsSpawn(Properties::alwaysAllowSpawn);

		private static boolean alwaysAllowSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}
	}
}