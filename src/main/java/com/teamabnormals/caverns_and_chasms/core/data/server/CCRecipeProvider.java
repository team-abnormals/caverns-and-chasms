package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;

public class CCRecipeProvider extends RecipeProvider {
	public static final ModLoadedCondition BOATLOAD_LOADED = new ModLoadedCondition("boatload");
	public static final ModLoadedCondition ENDERGETIC_LOADED = new ModLoadedCondition("endergetic");

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());

	public static final BlockFamily AZALEA_PLANKS = new BlockFamily.Builder(CCBlocks.AZALEA_PLANKS.get()).button(CCBlocks.AZALEA_BUTTON.get()).fence(CCBlocks.AZALEA_FENCE.get()).fenceGate(CCBlocks.AZALEA_FENCE_GATE.get()).pressurePlate(CCBlocks.AZALEA_PRESSURE_PLATE.get()).sign(CCBlocks.AZALEA_SIGNS.getFirst().get(), CCBlocks.AZALEA_SIGNS.getSecond().get()).slab(CCBlocks.AZALEA_SLAB.get()).stairs(CCBlocks.AZALEA_STAIRS.get()).door(CCBlocks.AZALEA_DOOR.get()).trapdoor(CCBlocks.AZALEA_TRAPDOOR.get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();
	public static final BlockFamily DRIPSTONE_SHINGLES = new BlockFamily.Builder(CCBlocks.DRIPSTONE_SHINGLES.get()).slab(CCBlocks.DRIPSTONE_SHINGLE_SLAB.get()).stairs(CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get()).wall(CCBlocks.DRIPSTONE_SHINGLE_WALL.get()).chiseled(CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get()).getFamily();
	public static final BlockFamily CALCITE = new BlockFamily.Builder(Blocks.CALCITE).slab(CCBlocks.CALCITE_SLAB.get()).stairs(CCBlocks.CALCITE_STAIRS.get()).wall(CCBlocks.CALCITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_CALCITE = new BlockFamily.Builder(CCBlocks.POLISHED_CALCITE.get()).slab(CCBlocks.POLISHED_CALCITE_SLAB.get()).stairs(CCBlocks.POLISHED_CALCITE_STAIRS.get()).getFamily();
	public static final BlockFamily TUFF = new BlockFamily.Builder(Blocks.TUFF).slab(CCBlocks.TUFF_SLAB.get()).stairs(CCBlocks.TUFF_STAIRS.get()).wall(CCBlocks.TUFF_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_TUFF = new BlockFamily.Builder(CCBlocks.POLISHED_TUFF.get()).slab(CCBlocks.POLISHED_TUFF_SLAB.get()).stairs(CCBlocks.POLISHED_TUFF_STAIRS.get()).getFamily();
	public static final BlockFamily SUGILITE = new BlockFamily.Builder(CCBlocks.SUGILITE.get()).slab(CCBlocks.SUGILITE_SLAB.get()).stairs(CCBlocks.SUGILITE_STAIRS.get()).wall(CCBlocks.SUGILITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_SUGILITE = new BlockFamily.Builder(CCBlocks.POLISHED_SUGILITE.get()).slab(CCBlocks.POLISHED_SUGILITE_SLAB.get()).stairs(CCBlocks.POLISHED_SUGILITE_STAIRS.get()).getFamily();
	public static final BlockFamily COBBLESTONE_BRICKS = new BlockFamily.Builder(CCBlocks.COBBLESTONE_BRICKS.get()).slab(CCBlocks.COBBLESTONE_BRICK_SLAB.get()).stairs(CCBlocks.COBBLESTONE_BRICK_STAIRS.get()).wall(CCBlocks.COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily COBBLESTONE_TILES = new BlockFamily.Builder(CCBlocks.COBBLESTONE_TILES.get()).slab(CCBlocks.COBBLESTONE_TILE_SLAB.get()).stairs(CCBlocks.COBBLESTONE_TILE_STAIRS.get()).wall(CCBlocks.COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_BRICKS = new BlockFamily.Builder(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).slab(CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get()).stairs(CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get()).wall(CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_TILES = new BlockFamily.Builder(CCBlocks.MOSSY_COBBLESTONE_TILES.get()).slab(CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get()).stairs(CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get()).wall(CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily LAPIS_LAZULI_BRICKS = new BlockFamily.Builder(CCBlocks.LAPIS_LAZULI_BRICKS.get()).slab(CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get()).stairs(CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get()).wall(CCBlocks.LAPIS_LAZULI_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SPINEL_BRICKS = new BlockFamily.Builder(CCBlocks.SPINEL_BRICKS.get()).slab(CCBlocks.SPINEL_BRICK_SLAB.get()).stairs(CCBlocks.SPINEL_BRICK_STAIRS.get()).wall(CCBlocks.SPINEL_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SANGUINE_PLATES = new BlockFamily.Builder(CCBlocks.SANGUINE_PLATES.get()).slab(CCBlocks.SANGUINE_SLAB.get()).stairs(CCBlocks.SANGUINE_STAIRS.get()).getFamily();
	public static final BlockFamily CUT_AMETHYST_BRICKS = new BlockFamily.Builder(CCBlocks.CUT_AMETHYST_BRICKS.get()).slab(CCBlocks.CUT_AMETHYST_BRICK_SLAB.get()).stairs(CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get()).wall(CCBlocks.CUT_AMETHYST_BRICK_WALL.get()).getFamily();

	public CCRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(TOOLS, Items.BUNDLE).define('R', Items.LEATHER).define('S', Items.STRING).pattern("SRS").pattern("R R").pattern("RRR").unlockedBy("has_leather", has(Items.LEATHER)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));

		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.RAIL, 3).define('#', Items.STICK).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.ACTIVATOR_RAIL, 3).define('#', Blocks.REDSTONE_TORCH).define('S', Items.STICK).define('X', Items.IRON_NUGGET).pattern("XSX").pattern("X#X").pattern("XSX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.DETECTOR_RAIL, 3).define('R', Items.REDSTONE).define('#', Blocks.STONE_PRESSURE_PLATE).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.POWERED_RAIL, 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', Items.GOLD_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, CCBlocks.SPIKED_RAIL.get(), 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', CCItemTags.NUGGETS_SILVER).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.CHAIN).define('#', Items.IRON_NUGGET).pattern("#").pattern("#").pattern("#").unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET)).save(consumer);

		ShapedRecipeBuilder.shaped(MISC, CCItems.BEJEWELED_PEARL.get(), 2).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(FOOD, CCItems.BEJEWELED_APPLE.get(), 2).define('A', Items.GOLDEN_APPLE).define('S', CCItems.SPINEL.get()).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, CCBlocks.TMT.get(), 4).define('T', Items.TNT).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(COMBAT, CCItems.BLUNT_ARROW.get(), 4).requires(Items.ARROW).requires(CCItems.SPINEL.get()).unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.LARGE_ARROW.get(), 4).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).define('Y', Items.FEATHER).pattern("X").pattern("#").pattern("Y").unlockedBy("has_feather", has(Items.FEATHER)).unlockedBy("has_silver", has(CCItemTags.INGOTS_SILVER)).save(consumer);

		SpecialRecipeBuilder.special(CCRecipeSerializers.TOOLBOX_WAXING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":toolbox_waxing");
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.TOOLBOX.get()).define('C', Blocks.COPPER_BLOCK).define('I', Tags.Items.INGOTS_COPPER).pattern(" I ").pattern("I I").pattern("CCC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.TUNING_FORK.get()).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.BAROMETER.get()).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCItems.OXIDIZED_COPPER_GOLEM.get(), CCItems.WAXED_OXIDIZED_COPPER_GOLEM.get());
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		mimingRecipe(consumer, MISC, Items.MUSIC_DISC_11, CCItems.MUSIC_DISC_EPILOGUE.get());
		mimingRecipe(consumer, MISC, Items.MOJANG_BANNER_PATTERN, CCItems.ABNORMALS_BANNER_PATTERN.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.ROCKY_DIRT.get(), 4).define('D', Blocks.DIRT).define('C', Blocks.COBBLESTONE).pattern("DC").pattern("CD").unlockedBy("has_dirt", has(Blocks.DIRT)).unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE)).save(consumer);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.ROOTED_DIRT).requires(Blocks.DIRT).requires(Blocks.HANGING_ROOTS).unlockedBy("has_hanging_roots", has(Blocks.HANGING_ROOTS)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Blocks.ROOTED_DIRT)));

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.LAVA_LAMP.get()).define('G', Tags.Items.INGOTS_GOLD).define('B', BlueprintItemTags.BUCKETS_LAVA).pattern("GGG").pattern("GBG").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.FLOODLIGHT.get()).define('C', Tags.Items.INGOTS_COPPER).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCBlocks.FLOODLIGHT.get(), CCBlocks.WAXED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.EXPOSED_FLOODLIGHT.get(), CCBlocks.WAXED_EXPOSED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.WEATHERED_FLOODLIGHT.get(), CCBlocks.WAXED_WEATHERED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.OXIDIZED_FLOODLIGHT.get(), CCBlocks.WAXED_OXIDIZED_FLOODLIGHT.get());
//		ShapedRecipeBuilder.shaped(CCBlocks.INDUCTOR.get()).define('C', Tags.Items.INGOTS_COPPER).define('I', Items.IRON_BLOCK).define('R', Items.REDSTONE).pattern("CIC").pattern("CRC").pattern("CIC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.CUPRIC_TORCH.get(), 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Items.STICK).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.CUPRIC_CAMPFIRE.get()).define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_stick", has(Items.STICK)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.CUPRIC_LANTERN.get()).define('#', CCBlocks.CUPRIC_TORCH.get()).define('X', Items.IRON_NUGGET).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper", has(CCBlocks.CUPRIC_TORCH.get())).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.BRAZIER.get()).define('#', ItemTags.COALS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_coal", has(ItemTags.COALS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.SOUL_BRAZIER.get()).define('#', ItemTags.SOUL_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.CUPRIC_BRAZIER.get()).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		conditionalRecipe(consumer, ENDERGETIC_LOADED, DECORATIONS, ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.ENDER_BRAZIER.get()).define('#', CCItemTags.ENDER_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_end_stone", has(CCItemTags.ENDER_FIRE_BASE_BLOCKS)));

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.COPPER_BARS.get(), 16).define('#', Tags.Items.INGOTS_COPPER).pattern("###").pattern("###").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());

		ShapelessRecipeBuilder.shapeless(REDSTONE, CCBlocks.COPPER_BUTTON.get()).requires(ItemTags.WOODEN_BUTTONS).requires(Tags.Items.INGOTS_COPPER).unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, REDSTONE, CCBlocks.COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());

		waxRecipe(consumer, DECORATIONS, Blocks.LIGHTNING_ROD, CCBlocks.WAXED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.EXPOSED_LIGHTNING_ROD.get(), CCBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.WEATHERED_LIGHTNING_ROD.get(), CCBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, CCBlocks.OXIDIZED_LIGHTNING_ROD.get(), CCBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());

		nineBlockStorageRecipes(consumer, MISC, CCItems.SPINEL.get(), BUILDING_BLOCKS, CCBlocks.SPINEL_BLOCK.get());
		nineBlockStorageRecipes(consumer, MISC, CCItems.RAW_SILVER.get(), BUILDING_BLOCKS, CCBlocks.RAW_SILVER_BLOCK.get());
		nineBlockStorageRecipes(consumer, FOOD, Items.ROTTEN_FLESH, BUILDING_BLOCKS, CCBlocks.ROTTEN_FLESH_BLOCK.get());
		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, MISC, CCItems.SILVER_INGOT.get(), BUILDING_BLOCKS, CCBlocks.SILVER_BLOCK.get(), "silver_ingot_from_silver_block", "silver_ingot");
		nineBlockStorageRecipesWithCustomPacking(consumer, MISC, CCItems.SILVER_NUGGET.get(), MISC, CCItems.SILVER_INGOT.get(), "silver_ingot_from_nuggets", "silver_ingot");
		nineBlockStorageRecipesWithCustomPacking(consumer, MISC, CCItems.COPPER_NUGGET.get(), MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");

		oreSmelting(consumer, SILVER_SMELTABLES, MISC, CCItems.SILVER_INGOT.get(), 1.0F, 200, "silver_ingot");
		oreSmelting(consumer, SPINEL_SMELTABLES, MISC, CCItems.SPINEL.get(), 0.2F, 200, "spinel");
		oreBlasting(consumer, SILVER_SMELTABLES, MISC, CCItems.SILVER_INGOT.get(), 1.0F, 100, "silver_ingot");
		oreBlasting(consumer, SPINEL_SMELTABLES, MISC, CCItems.SPINEL.get(), 0.2F, 100, "spinel");

		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_AXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_BOOTS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_CHESTPLATE.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_HELMET.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_HOE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_LEGGINGS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_PICKAXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_SHOVEL.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("#").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_SWORD.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("X").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		pressurePlateBuilder(REDSTONE, CCBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), Ingredient.of(CCItemTags.INGOTS_SILVER)).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.SILVER_BARS.get(), 16).define('#', CCItemTags.INGOTS_SILVER).pattern("###").pattern("###").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 200).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getSmeltingRecipeName(CCItems.SILVER_NUGGET.get())));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 100).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getBlastingRecipeName(CCItems.SILVER_NUGGET.get())));
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.KUNAI.get()).define('#', Tags.Items.RODS_WOODEN).define('S', CCItemTags.NUGGETS_SILVER).pattern(" S").pattern("# ").unlockedBy("has_silver_nugget", has(CCItemTags.NUGGETS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, CCBlocks.GOLDEN_BARS.get(), 16).define('#', Items.GOLD_INGOT).pattern("###").pattern("###").unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(MISC, CCItems.GOLDEN_BUCKET.get()).define('#', Blocks.GOLD_BLOCK).pattern("# #").pattern(" # ").unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK)).save(consumer);
		conditionalRecipe(consumer, new NotCondition(new ModLoadedCondition("environmental")), FOOD, ShapedRecipeBuilder.shaped(FOOD, Blocks.CAKE).define('A', CCItems.GOLDEN_MILK_BUCKET.get()).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Items.EGG).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Items.EGG)), new ResourceLocation(CavernsAndChasms.MOD_ID, getSimpleRecipeName(Blocks.CAKE)));
		conditionalRecipe(consumer, new NotCondition(new TagEmptyCondition(CCItemTags.BOTTLES_MILK.location())), MISC, ShapelessRecipeBuilder.shapeless(MISC, CCItems.GOLDEN_MILK_BUCKET.get()).requires(CCItems.GOLDEN_BUCKET.get()).requires(Ingredient.of(CCItemTags.BOTTLES_MILK), 3).unlockedBy("has_milk_bottle", has(CCItemTags.BOTTLES_MILK)));

		ShapelessRecipeBuilder.shapeless(MISC, CCItems.SANGUINE_PLATING.get()).requires(Items.ROTTEN_FLESH, 3).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 2).requires(Items.GHAST_TEAR, 2).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_HELMET.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("XXX").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_CHESTPLATE.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_LEGGINGS.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_BOOTS.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("X X").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.SANGUINE_PLATES.get(), 12).define('#', CCItems.SANGUINE_PLATING.get()).pattern("##").pattern("##").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		generateRecipes(consumer, SANGUINE_PLATES);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SANGUINE_SLAB.get(), CCBlocks.SANGUINE_PLATES.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SANGUINE_STAIRS.get(), CCBlocks.SANGUINE_PLATES.get());

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, MISC, CCItems.NECROMIUM_INGOT.get(), BUILDING_BLOCKS, CCBlocks.NECROMIUM_BLOCK.get(), "necromium_ingot_from_necromium_block", "necromium_ingot");
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.NECROMIUM_INGOT.get()).requires(Items.NETHERITE_SCRAP, 4).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 4).group("necromium_ingot").unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(consumer);
		nineBlockStorageRecipesWithCustomPacking(consumer, MISC, CCItems.NECROMIUM_NUGGET.get(), MISC, CCItems.NECROMIUM_INGOT.get(), "necromium_ingot_from_nuggets", "necromium_ingot");
		necromiumSmithing(consumer, Items.DIAMOND_CHESTPLATE, COMBAT, CCItems.NECROMIUM_CHESTPLATE.get());
		necromiumSmithing(consumer, Items.DIAMOND_LEGGINGS, COMBAT, CCItems.NECROMIUM_LEGGINGS.get());
		necromiumSmithing(consumer, Items.DIAMOND_HELMET, COMBAT, CCItems.NECROMIUM_HELMET.get());
		necromiumSmithing(consumer, Items.DIAMOND_BOOTS, COMBAT, CCItems.NECROMIUM_BOOTS.get());
		necromiumSmithing(consumer, Items.DIAMOND_SWORD, COMBAT, CCItems.NECROMIUM_SWORD.get());
		necromiumSmithing(consumer, Items.DIAMOND_AXE, TOOLS, CCItems.NECROMIUM_AXE.get());
		necromiumSmithing(consumer, Items.DIAMOND_PICKAXE, TOOLS, CCItems.NECROMIUM_PICKAXE.get());
		necromiumSmithing(consumer, Items.DIAMOND_HOE, TOOLS, CCItems.NECROMIUM_HOE.get());
		necromiumSmithing(consumer, Items.DIAMOND_SHOVEL, TOOLS, CCItems.NECROMIUM_SHOVEL.get());
		necromiumSmithing(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NECROMIUM_HORSE_ARMOR.get());
		netheriteSmithing(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NETHERITE_HORSE_ARMOR.get());

		nineBlockStorageRecipesWithCustomPacking(consumer, MISC, CCItems.NETHERITE_NUGGET.get(), MISC, Items.NETHERITE_INGOT, "netherite_ingot_from_nuggets", "netherite_ingot");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_BRICKS.get()).define('#', Items.LAPIS_LAZULI).pattern("##").pattern("##").unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(consumer);
		generateRecipes(consumer, LAPIS_LAZULI_BRICKS);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_PILLAR.get(), 2).define('#', CCBlocks.LAPIS_LAZULI_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(CCBlocks.LAPIS_LAZULI_BRICKS.get()), has(CCBlocks.LAPIS_LAZULI_BRICKS.get())).unlockedBy(getHasName(CCBlocks.LAPIS_LAZULI_PILLAR.get()), has(CCBlocks.LAPIS_LAZULI_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_LAMP.get()).define('#', Items.LAPIS_LAZULI).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.LAPIS_LAZULI_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.LAPIS_LAZULI_PILLAR.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.SPINEL_BRICKS.get()).define('#', CCItems.SPINEL.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCItems.SPINEL.get()), has(CCItems.SPINEL.get())).save(consumer);
		generateRecipes(consumer, SPINEL_BRICKS);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.SPINEL_PILLAR.get(), 2).define('#', CCBlocks.SPINEL_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(CCBlocks.SPINEL_BRICKS.get()), has(CCBlocks.SPINEL_BRICKS.get())).unlockedBy(getHasName(CCBlocks.SPINEL_PILLAR.get()), has(CCBlocks.SPINEL_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.SPINEL_LAMP.get()).define('#', CCItems.SPINEL.get()).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.SPINEL_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.SPINEL_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.SPINEL_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SPINEL_PILLAR.get(), CCBlocks.SPINEL_BRICKS.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.CALCITE).requires(Blocks.DIORITE).requires(Items.AMETHYST_SHARD).unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.CALCITE).getPath()));
		generateRecipes(consumer, CALCITE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CALCITE_STAIRS.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.CALCITE_WALL.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE_STAIRS.get(), Blocks.CALCITE);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE.get(), 4).define('#', Blocks.CALCITE).pattern("##").pattern("##").unlockedBy("has_calcite", has(Blocks.CALCITE)).save(consumer);
		generateRecipes(consumer, POLISHED_CALCITE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE_SLAB.get(), CCBlocks.POLISHED_CALCITE.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_CALCITE_STAIRS.get(), CCBlocks.POLISHED_CALCITE.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.TUFF, 2).requires(Blocks.BASALT).requires(Blocks.COBBLESTONE).unlockedBy("has_stone", has(Blocks.BASALT)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.TUFF).getPath()));
		generateRecipes(consumer, TUFF);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.TUFF_STAIRS.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.TUFF_WALL.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF_STAIRS.get(), Blocks.TUFF);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF.get(), 4).define('#', Blocks.TUFF).pattern("##").pattern("##").unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		generateRecipes(consumer, POLISHED_TUFF);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF_SLAB.get(), CCBlocks.POLISHED_TUFF.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_TUFF_STAIRS.get(), CCBlocks.POLISHED_TUFF.get());

		generateRecipes(consumer, SUGILITE);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.SUGILITE.get()).requires(Blocks.GRANITE).requires(CCItems.SPINEL.get()).unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(CCBlocks.SUGILITE.get()).getPath()));
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SUGILITE_SLAB.get(), CCBlocks.SUGILITE.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.SUGILITE_STAIRS.get(), CCBlocks.SUGILITE.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.SUGILITE_WALL.get(), CCBlocks.SUGILITE.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE.get(), CCBlocks.SUGILITE.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE_SLAB.get(), CCBlocks.SUGILITE.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE_STAIRS.get(), CCBlocks.SUGILITE.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE.get(), 4).define('#', CCBlocks.SUGILITE.get()).pattern("##").pattern("##").unlockedBy("has_sugilite", has(CCBlocks.SUGILITE.get())).save(consumer);
		generateRecipes(consumer, POLISHED_SUGILITE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE_SLAB.get(), CCBlocks.POLISHED_SUGILITE.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.POLISHED_SUGILITE_STAIRS.get(), CCBlocks.POLISHED_SUGILITE.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLES.get(), 4).define('#', Blocks.DRIPSTONE_BLOCK).pattern("##").pattern("##").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), CCBlocks.DRIPSTONE_SHINGLES.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), Blocks.DRIPSTONE_BLOCK, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.FLOODED_DRIPSTONE_SHINGLES.get(), 8).requires(BlueprintItemTags.BUCKETS_WATER).requires(CCBlocks.DRIPSTONE_SHINGLES.get(), 8).unlockedBy("has_dripstone_shingles", has(CCBlocks.DRIPSTONE_SHINGLES.get())).save(consumer);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, MISC, Items.AMETHYST_SHARD, BUILDING_BLOCKS, CCBlocks.AMETHYST_BLOCK.get(), "amethyst_from_amethyst_block", "amethyst_shard");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST.get(), 4).define('#', Blocks.AMETHYST_BLOCK).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.AMETHYST_BLOCK), has(Blocks.AMETHYST_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICKS.get(), 4).define('#', CCBlocks.CUT_AMETHYST.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.CUT_AMETHYST.get()), has(CCBlocks.CUT_AMETHYST.get())).save(consumer);
		generateRecipes(consumer, CUT_AMETHYST_BRICKS);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), CCBlocks.CUT_AMETHYST_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), CCBlocks.CUT_AMETHYST_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), CCBlocks.CUT_AMETHYST_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICKS.get(), CCBlocks.CUT_AMETHYST.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), CCBlocks.CUT_AMETHYST.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), CCBlocks.CUT_AMETHYST.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), CCBlocks.CUT_AMETHYST.get());

		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICKS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), Blocks.AMETHYST_BLOCK, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), Blocks.AMETHYST_BLOCK);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, MISC, Items.ECHO_SHARD, BUILDING_BLOCKS, CCBlocks.ECHO_BLOCK.get(), "echo_shard_from_echo_block", "echo_shard");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE)).save(consumer);
		generateRecipes(consumer, COBBLESTONE_BRICKS);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICK_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.COBBLESTONE_BRICK_WALL.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICKS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICK_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.COBBLESTONE_BRICK_WALL.get(), Blocks.COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILES.get(), 4).define('#', CCBlocks.COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.COBBLESTONE_BRICKS.get()), has(CCBlocks.COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, COBBLESTONE_TILES);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_TILES.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILES.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILES.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.COBBLESTONE_TILE_WALL.get(), Blocks.COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.MOSSY_COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.MOSSY_COBBLESTONE), has(Blocks.MOSSY_COBBLESTONE)).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_BRICKS);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), Blocks.MOSSY_COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILES.get(), 4).define('#', CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()), has(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_TILES);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILES.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, DECORATIONS, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), Blocks.MOSSY_COBBLESTONE);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).requires(CCBlocks.COBBLESTONE_BRICKS.get()).requires(Blocks.VINE).group("cobblestone_bricks").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILES.get()).requires(CCBlocks.COBBLESTONE_TILES.get()).requires(Blocks.VINE).group("mossy_cobblestone_tiles").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).requires(CCBlocks.COBBLESTONE_BRICKS.get()).requires(Blocks.MOSS_BLOCK).group("cobblestone_bricks").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSS_BLOCK));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CCBlocks.MOSSY_COBBLESTONE_TILES.get()).requires(CCBlocks.COBBLESTONE_TILES.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_tiles").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSS_BLOCK));

		generateRecipes(consumer, AZALEA_PLANKS);
		planksFromLogs(consumer, CCBlocks.AZALEA_PLANKS.get(), CCItemTags.AZALEA_LOGS, 4);
		woodFromLogs(consumer, CCBlocks.AZALEA_WOOD.get(), CCBlocks.AZALEA_LOG.get());
		woodFromLogs(consumer, CCBlocks.STRIPPED_AZALEA_WOOD.get(), CCBlocks.STRIPPED_AZALEA_LOG.get());
		hangingSign(consumer, CCBlocks.AZALEA_HANGING_SIGNS.getFirst().get(), CCBlocks.STRIPPED_AZALEA_LOG.get());
		woodenBoat(consumer, CCItems.AZALEA_BOAT.getFirst().get(), CCBlocks.AZALEA_PLANKS.get());
		chestBoat(consumer, CCItems.AZALEA_BOAT.getSecond().get(), CCItems.AZALEA_BOAT.getFirst().get());
		conditionalRecipe(consumer, BOATLOAD_LOADED, TRANSPORTATION, ShapelessRecipeBuilder.shapeless(TRANSPORTATION, CCItems.AZALEA_FURNACE_BOAT.get()).requires(Blocks.FURNACE).requires(CCItems.AZALEA_BOAT.getFirst().get()).group("furnace_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
		conditionalRecipe(consumer, BOATLOAD_LOADED, TRANSPORTATION, ShapedRecipeBuilder.shaped(TRANSPORTATION, CCItems.LARGE_AZALEA_BOAT.get()).define('#', CCBlocks.AZALEA_PLANKS.get()).define('B', CCItems.AZALEA_BOAT.getFirst().get()).pattern("#B#").pattern("###").group("large_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
	}

	protected static void netheriteSmithing(Consumer<FinishedRecipe> consumer, Item input, RecipeCategory category, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(Items.NETHERITE_INGOT), category, output).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(output) + "_smithing"));
	}

	protected static void necromiumSmithing(Consumer<FinishedRecipe> consumer, Item input, RecipeCategory category, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(CCItemTags.INGOTS_NECROMIUM), category, output).unlocks("has_necromium_ingot", has(CCItemTags.INGOTS_NECROMIUM)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(output) + "_smithing"));
	}

	private static void waxRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike result) {
		ShapelessRecipeBuilder.shapeless(category, result).requires(input).requires(Items.HONEYCOMB).group(getItemName(result)).unlockedBy(getHasName(input), has(input)).save(consumer, getModConversionRecipeName(result, Items.HONEYCOMB));
	}


	public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory blockCategory, ItemLike block) {
		nineBlockStorageRecipes(consumer, itemCategory, item, blockCategory, block, getItemName(block), null, getItemName(item), null);
	}

	public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory blockCategory, ItemLike block, String shapedName, @Nullable String shapedGroup, String shapelessName, @Nullable String shapelessGroup) {
		ShapelessRecipeBuilder.shapeless(itemCategory, item, 9).requires(block).group(shapelessGroup).unlockedBy(getHasName(block), has(block)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapelessName));
		ShapedRecipeBuilder.shaped(blockCategory, block).define('#', item).pattern("###").pattern("###").pattern("###").group(shapedGroup).unlockedBy(getHasName(item), has(item)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapedName));
	}

	public static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> p_176563_, RecipeCategory itemCategory, ItemLike item, RecipeCategory blockCategory, ItemLike block, String p_176566_, String p_176567_) {
		nineBlockStorageRecipes(p_176563_, itemCategory, item, blockCategory, block, p_176566_, p_176567_, getSimpleRecipeName(item), null);
	}

	public static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> p_176617_, RecipeCategory itemCategory, ItemLike item, RecipeCategory blockCategory, ItemLike p_176619_, String p_176620_, String p_176621_) {
		nineBlockStorageRecipes(p_176617_, itemCategory, item, blockCategory, p_176619_, getSimpleRecipeName(p_176619_), null, p_176620_, p_176621_);
	}

	public static void stonecutterResultFromBase(Consumer<FinishedRecipe> p_176736_, RecipeCategory category, ItemLike p_176737_, ItemLike p_176738_) {
		stonecutterResultFromBase(p_176736_, category, p_176737_, p_176738_, 1);
	}

	public static void stonecutterResultFromBase(Consumer<FinishedRecipe> p_176547_, RecipeCategory category, ItemLike p_176548_, ItemLike p_176549_, int p_176550_) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(p_176549_), category, p_176548_, p_176550_).unlockedBy(getHasName(p_176549_), has(p_176549_)).save(p_176547_, getModConversionRecipeName(p_176548_, p_176549_) + "_stonecutting");
	}

	public static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike chestBoat, ItemLike boat) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat).requires(Tags.Items.CHESTS_WOODEN).requires(boat).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(consumer);
	}

	public static void foodCookingRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, output, 0.35F, 200).unlockedBy(getHasName(input), has(input)).save(consumer);
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), category, output, 0.35F, 100).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_smoking");
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), category, output, 0.35F, 600).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_campfire_cooking");
	}

	protected static void oreSmelting(Consumer<FinishedRecipe> p_176592_, List<ItemLike> p_176593_, RecipeCategory category, ItemLike p_176594_, float p_176595_, int p_176596_, String p_176597_) {
		oreCooking(p_176592_, RecipeSerializer.SMELTING_RECIPE, p_176593_, category, p_176594_, p_176595_, p_176596_, p_176597_, "_from_smelting");
	}

	protected static void oreBlasting(Consumer<FinishedRecipe> p_176626_, List<ItemLike> p_176627_, RecipeCategory category, ItemLike p_176628_, float p_176629_, int p_176630_, String p_176631_) {
		oreCooking(p_176626_, RecipeSerializer.BLASTING_RECIPE, p_176627_, category, p_176628_, p_176629_, p_176630_, p_176631_, "_from_blasting");
	}

	protected static void oreCooking(Consumer<FinishedRecipe> p_176534_, RecipeSerializer<? extends AbstractCookingRecipe> p_176535_, List<ItemLike> p_176536_, RecipeCategory category, ItemLike p_176537_, float p_176538_, int p_176539_, String p_176540_, String p_176541_) {
		for (ItemLike itemlike : p_176536_) {
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, p_176537_, p_176538_, p_176539_, p_176535_).group(p_176540_).unlockedBy(getHasName(itemlike), has(itemlike)).save(p_176534_, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(p_176537_) + p_176541_ + "_" + getItemName(itemlike)));
		}
	}

	public static void mimingRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output) {
		mimingRecipe(category, Ingredient.of(input), output, 1).unlockedBy(getHasName(input), has(input)).save(consumer);
	}

	public static SingleItemRecipeBuilder mimingRecipe(RecipeCategory category, Ingredient input, ItemLike output, int count) {
		return new SingleItemRecipeBuilder(category, CCRecipeSerializers.MIMING.get(), input, output, count);
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe) {
		conditionalRecipe(consumer, condition, category, recipe, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + category.getFolderName() + "/" + id.getPath())).build(consumer, id);
	}

	private static ResourceLocation getModConversionRecipeName(ItemLike result, ItemLike input) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(result) + "_from_" + getItemName(input));
	}

}