package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.minecraftabnormals.abnormals_core.core.util.registry.BlockSubRegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.common.block.*;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final BlockSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> SILVER_BLOCK = HELPER.createBlock("silver_block", () -> new Block(Block.Properties.copy(Blocks.GOLD_BLOCK)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SILVER_ORE = HELPER.createBlock("silver_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().harvestLevel(2).harvestTool(ToolType.PICKAXE).strength(3.0F, 3.0F)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SOUL_SILVER_ORE = HELPER.createBlock("soul_silver_ore", () -> new CCOreBlock(0, 1, Block.Properties.copy(Blocks.SOUL_SOIL).harvestTool(ToolType.SHOVEL)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SILVER_BARS = HELPER.createBlock("silver_bars", () -> new PaneBlock(Properties.METAL_BARS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> MEDIUM_WEIGHTED_PRESSURE_PLATE = HELPER.createBlock("medium_weighted_pressure_plate", () -> new WeightedPressurePlateBlock(50, Properties.SILVER_PRESSURE_PLATE), ItemGroup.TAB_REDSTONE);
	public static final RegistryObject<Block> SILVER_BUTTON = HELPER.createCompatBlock("quark", "silver_button", () -> new SilverButtonBlock(Properties.SILVER_BUTTON), ItemGroup.TAB_REDSTONE);
	public static final RegistryObject<Block> SPIKED_RAIL = HELPER.createBlock("spiked_rail", () -> new SpikedRailBlock(AbstractBlock.Properties.copy(Blocks.POWERED_RAIL)), ItemGroup.TAB_TRANSPORTATION);

	public static final RegistryObject<Block> SANGUINE_PLATES = HELPER.createBlock("sanguine_plates", () -> new Block(Properties.SANGUINE_PLATES), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_STAIRS = HELPER.createBlock("sanguine_plate_stairs", () -> new StairsBlock(() -> SANGUINE_PLATES.get().defaultBlockState(), Properties.SANGUINE_PLATES), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_SLAB = HELPER.createBlock("sanguine_plate_slab", () -> new SlabBlock(Properties.SANGUINE_PLATES), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "sanguine_plate_vertical_slab", () -> new VerticalSlabBlock(Properties.SANGUINE_PLATES), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> NECROMIUM_BLOCK = HELPER.createBlock("necromium_block", () -> new Block(Block.Properties.copy(Blocks.NETHERITE_BLOCK).harvestLevel(3)), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> BRAZIER = HELPER.createBlock("brazier", () -> new BrazierBlock(1, Properties.BRAZIER), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> SOUL_BRAZIER = HELPER.createBlock("soul_brazier", () -> new BrazierBlock(2, Properties.BRAZIER_DIM), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> ENDER_BRAZIER = HELPER.createCompatBlock("endergetic", "ender_brazier", () -> new BrazierBlock(3, Properties.BRAZIER), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> CURSED_BRAZIER = HELPER.createBlock("cursed_brazier", () -> new BrazierBlock(4, Properties.BRAZIER_DIM), ItemGroup.TAB_DECORATIONS);

	public static final RegistryObject<Block> CURSED_FIRE = HELPER.createBlockNoItem("cursed_fire", () -> new CursedFireBlock(Block.Properties.copy(Blocks.SOUL_FIRE)));
	public static final RegistryObject<Block> CURSED_CAMPFIRE = HELPER.createBlock("cursed_campfire", () -> new CursedCampfireBlock(Block.Properties.copy(Blocks.SOUL_CAMPFIRE)), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> CURSED_LANTERN = HELPER.createBlock("cursed_lantern", () -> new LanternBlock(Block.Properties.copy(Blocks.SOUL_LANTERN)), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> CURSED_WALL_TORCH = HELPER.createBlockNoItem("cursed_wall_torch", () -> new CursedWallTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)));
	public static final RegistryObject<Block> CURSED_TORCH = HELPER.createWallOrFloorBlock("cursed_torch", () -> new CursedTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)), CURSED_WALL_TORCH, ItemGroup.TAB_DECORATIONS);

	public static final RegistryObject<Block> ROTTEN_FLESH_BLOCK = HELPER.createBlock("rotten_flesh_block", () -> new Block(Properties.ROTTEN_FLESH_BLOCK), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> GRAVESTONE = HELPER.createBlock("gravestone", () -> new GravestoneBlock(Block.Properties.copy(Blocks.STONE)), ItemGroup.TAB_DECORATIONS);

	public static final RegistryObject<Block> GOLDEN_LANTERN = HELPER.createBlock("golden_lantern", () -> new GoldenLanternBlock(Properties.GOLDEN_LANTERN), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> GOLDEN_BARS = HELPER.createBlock("golden_bars", () -> new PaneBlock(Properties.METAL_BARS), ItemGroup.TAB_DECORATIONS);

	public static final RegistryObject<Block> SUGILITE_ORE = HELPER.createBlock("sugilite_ore", () -> new CCOreBlock(2, 5, Properties.SUGILITE_ORE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BLOCK = HELPER.createBlock("sugilite_block", () -> new Block(Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICKS = HELPER.createBlock("sugilite_bricks", () -> new Block(Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_STAIRS = HELPER.createBlock("sugilite_brick_stairs", () -> new StairsBlock(() -> SUGILITE_BRICKS.get().defaultBlockState(), Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_SLAB = HELPER.createBlock("sugilite_brick_slab", () -> new SlabBlock(Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "sugilite_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_BRICK_WALL = HELPER.createBlock("sugilite_brick_wall", () -> new WallBlock(Properties.SUGILITE), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> SUGILITE_PILLAR = HELPER.createBlock("sugilite_pillar", () -> new RotatedPillarBlock(Properties.SUGILITE), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_LAMP = HELPER.createBlock("sugilite_lamp", () -> new Block(Properties.LAMP), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> LAPIS_LAZULI_BRICKS = HELPER.createBlock("lapis_bricks", () -> new Block(Properties.LAPIS_LAZULI), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_STAIRS = HELPER.createBlock("lapis_brick_stairs", () -> new StairsBlock(() -> LAPIS_LAZULI_BRICKS.get().defaultBlockState(), Properties.LAPIS_LAZULI), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_SLAB = HELPER.createBlock("lapis_brick_slab", () -> new SlabBlock(Properties.LAPIS_LAZULI), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "lapis_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.LAPIS_LAZULI), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_WALL = HELPER.createBlock("lapis_brick_wall", () -> new WallBlock(Properties.LAPIS_LAZULI), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> LAPIS_LAZULI_PILLAR = HELPER.createBlock("lapis_pillar", () -> new RotatedPillarBlock(Properties.LAPIS_LAZULI), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_LAMP = HELPER.createBlock("lapis_lamp", () -> new Block(Properties.LAMP), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> ROCKY_DIRT = HELPER.createBlock("rocky_dirt", () -> new Block(Properties.ROCKY_DIRT), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> DIRT_BRICKS = HELPER.createBlock("dirt_bricks", () -> new Block(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_STAIRS = HELPER.createBlock("dirt_brick_stairs", () -> new StairsBlock(() -> DIRT_BRICKS.get().defaultBlockState(), Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_SLAB = HELPER.createBlock("dirt_brick_slab", () -> new SlabBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_BRICK_WALL = HELPER.createBlock("dirt_brick_wall", () -> new WallBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> DIRT_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "dirt_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> DIRT_TILES = HELPER.createBlock("dirt_tiles", () -> new Block(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_STAIRS = HELPER.createBlock("dirt_tile_stairs", () -> new StairsBlock(() -> DIRT_TILES.get().defaultBlockState(), Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_SLAB = HELPER.createBlock("dirt_tile_slab", () -> new SlabBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DIRT_TILE_WALL = HELPER.createBlock("dirt_tile_wall", () -> new WallBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> DIRT_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "dirt_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.DIRT_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> COBBLESTONE_BRICKS = HELPER.createBlock("cobblestone_bricks", () -> new Block(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("cobblestone_brick_stairs", () -> new StairsBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_SLAB = HELPER.createBlock("cobblestone_brick_slab", () -> new SlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_WALL = HELPER.createBlock("cobblestone_brick_wall", () -> new WallBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cobblestone_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICKS = HELPER.createBlock("mossy_cobblestone_bricks", () -> new Block(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("mossy_cobblestone_brick_stairs", () -> new StairsBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_SLAB = HELPER.createBlock("mossy_cobblestone_brick_slab", () -> new SlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_WALL = HELPER.createBlock("mossy_cobblestone_brick_wall", () -> new WallBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "mossy_cobblestone_brick_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> COBBLESTONE_TILES = HELPER.createBlock("cobblestone_tiles", () -> new Block(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_STAIRS = HELPER.createBlock("cobblestone_tile_stairs", () -> new StairsBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_SLAB = HELPER.createBlock("cobblestone_tile_slab", () -> new SlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_WALL = HELPER.createBlock("cobblestone_tile_wall", () -> new WallBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILES = HELPER.createBlock("mossy_cobblestone_tiles", () -> new Block(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_STAIRS = HELPER.createBlock("mossy_cobblestone_tile_stairs", () -> new StairsBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_SLAB = HELPER.createBlock("mossy_cobblestone_tile_slab", () -> new SlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_WALL = HELPER.createBlock("mossy_cobblestone_tile_wall", () -> new WallBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "mossy_cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.COBBLESTONE_BRICKS), ItemGroup.TAB_BUILDING_BLOCKS);

	static class Properties {
		public static final AbstractBlock.Properties ROCKY_DIRT = AbstractBlock.Properties.of(Material.DIRT, MaterialColor.DIRT).requiresCorrectToolForDrops().harvestTool(ToolType.SHOVEL).strength(1.5F).sound(SoundType.GRAVEL);
		public static final AbstractBlock.Properties DIRT_BRICKS = AbstractBlock.Properties.of(Material.DIRT, MaterialColor.DIRT).harvestTool(ToolType.SHOVEL).strength(0.5F).sound(SoundType.GRAVEL);
		public static final AbstractBlock.Properties COBBLESTONE_BRICKS = AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F);

		public static final AbstractBlock.Properties GOLDEN_LANTERN = AbstractBlock.Properties.of(Material.METAL).strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 15);
		public static final AbstractBlock.Properties METAL_BARS = AbstractBlock.Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
		public static final AbstractBlock.Properties SILVER_PRESSURE_PLATE = AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(SoundType.WOOD);
		public static final AbstractBlock.Properties SILVER_BUTTON = AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.METAL);
		public static final AbstractBlock.Properties SANGUINE_PLATES = Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.METAL);

		public static final AbstractBlock.Properties BRAZIER = AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(15)).noOcclusion();
		public static final AbstractBlock.Properties BRAZIER_DIM = AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(10)).noOcclusion();

		public static final AbstractBlock.Properties SUGILITE_ORE = AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final AbstractBlock.Properties SUGILITE = AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final AbstractBlock.Properties LAPIS_LAZULI = AbstractBlock.Properties.of(Material.METAL, MaterialColor.LAPIS).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final AbstractBlock.Properties LAMP = AbstractBlock.Properties.of(Material.BUILDABLE_GLASS).lightLevel((state) -> 15).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Properties::alwaysAllowSpawn);

		public static final AbstractBlock.Properties ROTTEN_FLESH_BLOCK = AbstractBlock.Properties.of(Material.GRASS, MaterialColor.TERRACOTTA_ORANGE).harvestTool(ToolType.HOE).strength(0.8F).sound(SoundType.CORAL_BLOCK);

		private static boolean alwaysAllowSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}

		private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
			return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		}
	}
}