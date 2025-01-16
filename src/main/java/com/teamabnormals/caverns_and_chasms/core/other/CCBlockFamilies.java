package com.teamabnormals.caverns_and_chasms.core.other;

import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Blocks;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;

public class CCBlockFamilies {
	public static final BlockFamily AZALEA_PLANKS_FAMILY = new BlockFamily.Builder(AZALEA_PLANKS.get()).button(AZALEA_BUTTON.get()).fence(AZALEA_FENCE.get()).fenceGate(AZALEA_FENCE_GATE.get()).pressurePlate(AZALEA_PRESSURE_PLATE.get()).sign(AZALEA_SIGNS.getFirst().get(), AZALEA_SIGNS.getSecond().get()).slab(AZALEA_SLAB.get()).stairs(AZALEA_STAIRS.get()).door(AZALEA_DOOR.get()).trapdoor(AZALEA_TRAPDOOR.get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();
	public static final BlockFamily DRIPSTONE_SHINGLES_FAMILY = new BlockFamily.Builder(DRIPSTONE_SHINGLES.get()).slab(DRIPSTONE_SHINGLE_SLAB.get()).stairs(DRIPSTONE_SHINGLE_STAIRS.get()).wall(DRIPSTONE_SHINGLE_WALL.get()).chiseled(CHISELED_DRIPSTONE_SHINGLES.get()).getFamily();
	public static final BlockFamily CALCITE_FAMILY = new BlockFamily.Builder(Blocks.CALCITE).slab(CALCITE_SLAB.get()).stairs(CALCITE_STAIRS.get()).wall(CALCITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_CALCITE_FAMILY = new BlockFamily.Builder(POLISHED_CALCITE.get()).slab(POLISHED_CALCITE_SLAB.get()).stairs(POLISHED_CALCITE_STAIRS.get()).getFamily();
	public static final BlockFamily TUFF_FAMILY = new BlockFamily.Builder(Blocks.TUFF).slab(TUFF_SLAB.get()).stairs(TUFF_STAIRS.get()).wall(TUFF_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_TUFF_FAMILY = new BlockFamily.Builder(POLISHED_TUFF.get()).slab(POLISHED_TUFF_SLAB.get()).stairs(POLISHED_TUFF_STAIRS.get()).getFamily();
	public static final BlockFamily SUGILITE_FAMILY = new BlockFamily.Builder(SUGILITE.get()).slab(SUGILITE_SLAB.get()).stairs(SUGILITE_STAIRS.get()).wall(SUGILITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_SUGILITE_FAMILY = new BlockFamily.Builder(POLISHED_SUGILITE.get()).slab(POLISHED_SUGILITE_SLAB.get()).stairs(POLISHED_SUGILITE_STAIRS.get()).getFamily();
	public static final BlockFamily COBBLESTONE_BRICKS_FAMILY = new BlockFamily.Builder(COBBLESTONE_BRICKS.get()).slab(COBBLESTONE_BRICK_SLAB.get()).stairs(COBBLESTONE_BRICK_STAIRS.get()).wall(COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily COBBLESTONE_TILES_FAMILY = new BlockFamily.Builder(COBBLESTONE_TILES.get()).slab(COBBLESTONE_TILE_SLAB.get()).stairs(COBBLESTONE_TILE_STAIRS.get()).wall(COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_BRICKS_FAMILY = new BlockFamily.Builder(MOSSY_COBBLESTONE_BRICKS.get()).slab(MOSSY_COBBLESTONE_BRICK_SLAB.get()).stairs(MOSSY_COBBLESTONE_BRICK_STAIRS.get()).wall(MOSSY_COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_TILES_FAMILY = new BlockFamily.Builder(MOSSY_COBBLESTONE_TILES.get()).slab(MOSSY_COBBLESTONE_TILE_SLAB.get()).stairs(MOSSY_COBBLESTONE_TILE_STAIRS.get()).wall(MOSSY_COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily COBBLED_DEEPSLATE_BRICKS_FAMILY = new BlockFamily.Builder(COBBLED_DEEPSLATE_BRICKS.get()).slab(COBBLED_DEEPSLATE_BRICK_SLAB.get()).stairs(COBBLED_DEEPSLATE_BRICK_STAIRS.get()).wall(COBBLED_DEEPSLATE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily COBBLED_DEEPSLATE_TILES_FAMILY = new BlockFamily.Builder(COBBLED_DEEPSLATE_TILES.get()).slab(COBBLED_DEEPSLATE_TILE_SLAB.get()).stairs(COBBLED_DEEPSLATE_TILE_STAIRS.get()).wall(COBBLED_DEEPSLATE_TILE_WALL.get()).getFamily();
	public static final BlockFamily LAPIS_LAZULI_BRICKS_FAMILY = new BlockFamily.Builder(LAPIS_LAZULI_BRICKS.get()).slab(LAPIS_LAZULI_BRICK_SLAB.get()).stairs(LAPIS_LAZULI_BRICK_STAIRS.get()).wall(LAPIS_LAZULI_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SPINEL_BRICKS_FAMILY = new BlockFamily.Builder(SPINEL_BRICKS.get()).slab(SPINEL_BRICK_SLAB.get()).stairs(SPINEL_BRICK_STAIRS.get()).wall(SPINEL_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SANGUINE_TILES_FAMILY = new BlockFamily.Builder(SANGUINE_TILES.get()).slab(SANGUINE_TILE_SLAB.get()).stairs(SANGUINE_TILE_STAIRS.get()).wall(SANGUINE_TILE_WALL.get()).getFamily();
	public static final BlockFamily FORTIFIED_SANGUINE_TILES_FAMILY = new BlockFamily.Builder(FORTIFIED_SANGUINE_TILES.get()).slab(FORTIFIED_SANGUINE_TILE_SLAB.get()).stairs(FORTIFIED_SANGUINE_TILE_STAIRS.get()).wall(FORTIFIED_SANGUINE_TILE_WALL.get()).getFamily();
	public static final BlockFamily CUT_AMETHYST_BRICKS_FAMILY = new BlockFamily.Builder(CUT_AMETHYST_BRICKS.get()).slab(CUT_AMETHYST_BRICK_SLAB.get()).stairs(CUT_AMETHYST_BRICK_STAIRS.get()).wall(CUT_AMETHYST_BRICK_WALL.get()).getFamily();
	public static final BlockFamily IRON_BRICKS_FAMILY = new BlockFamily.Builder(IRON_BRICKS.get()).slab(IRON_BRICK_SLAB.get()).stairs(IRON_BRICK_STAIRS.get()).wall(IRON_BRICK_WALL.get()).chiseled(CHISELED_IRON_BRICKS.get()).getFamily();
	public static final BlockFamily TIN_BRICKS_FAMILY = new BlockFamily.Builder(TIN_BRICKS.get()).slab(TIN_BRICK_SLAB.get()).stairs(TIN_BRICK_STAIRS.get()).wall(TIN_BRICK_WALL.get()).chiseled(CHISELED_TIN_BRICKS.get()).getFamily();
	public static final BlockFamily GOLD_BRICKS_FAMILY = new BlockFamily.Builder(GOLD_BRICKS.get()).slab(GOLD_BRICK_SLAB.get()).stairs(GOLD_BRICK_STAIRS.get()).wall(GOLD_BRICK_WALL.get()).chiseled(CHISELED_GOLD_BRICKS.get()).getFamily();
	public static final BlockFamily SILVER_BRICKS_FAMILY = new BlockFamily.Builder(SILVER_BRICKS.get()).slab(SILVER_BRICK_SLAB.get()).stairs(SILVER_BRICK_STAIRS.get()).wall(SILVER_BRICK_WALL.get()).chiseled(CHISELED_SILVER_BRICKS.get()).getFamily();
}
