package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.block.GoldenLanternBlock;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

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

	public static final RegistryObject<Block> GOLDEN_LANTERN 	= HELPER.createBlock("golden_lantern", () -> new GoldenLanternBlock(Properties.GOLDEN_LANTERN), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> GOLDEN_BARS 	= HELPER.createBlock("golden_bars", () -> new PaneBlock(Properties.GOLDEN_BARS), ItemGroup.DECORATIONS);

	static class Properties {
		public static final AbstractBlock.Properties COBBLESTONE_TILES = AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(2.0F, 6.0F);
		public static final AbstractBlock.Properties GOLDEN_LANTERN = Block.Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.LANTERN).setLightLevel((state) -> 15);
		public static final AbstractBlock.Properties GOLDEN_BARS = AbstractBlock.Properties.create(Material.IRON, MaterialColor.AIR).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).notSolid();
	}
}