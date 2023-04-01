package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.api.conditions.QuarkFlagRecipeCondition;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CCRecipeProvider extends RecipeProvider {
	public static final ModLoadedCondition WOODWORKS_LOADED = new ModLoadedCondition("woodworks");
	public static final ModLoadedCondition BOATLOAD_LOADED = new ModLoadedCondition("boatload");
	public static final ModLoadedCondition BUZZIER_BEES_LOADED = new ModLoadedCondition("buzzier_bees");
	public static final ModLoadedCondition ENDERGETIC_LOADED = new ModLoadedCondition("endergetic");

	public static final OrCondition WOODEN_LADDERS = new OrCondition(WOODWORKS_LOADED, quarkFlag("variant_ladders"));
	public static final OrCondition WOODEN_BOOKSHELVES = new OrCondition(WOODWORKS_LOADED, quarkFlag("variant_bookshelves"));
	public static final OrCondition WOODEN_CHESTS = new OrCondition(WOODWORKS_LOADED, quarkFlag("variant_chests"));

	public static final QuarkFlagRecipeCondition VERTICAL_PLANKS = quarkFlag("vertical_planks");
	public static final QuarkFlagRecipeCondition VERTICAL_SLABS = quarkFlag("vertical_slabs");
	public static final QuarkFlagRecipeCondition HEDGES = quarkFlag("hedges");
	public static final QuarkFlagRecipeCondition WOODEN_POSTS = quarkFlag("wooden_posts");
	public static final QuarkFlagRecipeCondition GOLD_METAL_BUTTON = quarkFlag("gold_metal_button");
	public static final AndCondition WOOD_TO_CHEST_RECIPES = new AndCondition(quarkFlag("variant_chests"), quarkFlag("wood_to_chest_recipes"));

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());

	public static final BlockFamily AZALEA_PLANKS = new BlockFamily.Builder(CCBlocks.AZALEA_PLANKS.get()).button(CCBlocks.AZALEA_BUTTON.get()).fence(CCBlocks.AZALEA_FENCE.get()).fenceGate(CCBlocks.AZALEA_FENCE_GATE.get()).pressurePlate(CCBlocks.AZALEA_PRESSURE_PLATE.get()).sign(CCBlocks.AZALEA_SIGN.getFirst().get(), CCBlocks.AZALEA_SIGN.getSecond().get()).slab(CCBlocks.AZALEA_SLAB.get()).stairs(CCBlocks.AZALEA_STAIRS.get()).door(CCBlocks.AZALEA_DOOR.get()).trapdoor(CCBlocks.AZALEA_TRAPDOOR.get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();
	public static final BlockFamily DRIPSTONE_SHINGLES = new BlockFamily.Builder(CCBlocks.DRIPSTONE_SHINGLES.get()).slab(CCBlocks.DRIPSTONE_SHINGLE_SLAB.get()).stairs(CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get()).wall(CCBlocks.DRIPSTONE_SHINGLE_WALL.get()).chiseled(CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get()).getFamily();
	public static final BlockFamily CALCITE = new BlockFamily.Builder(Blocks.CALCITE).slab(CCBlocks.CALCITE_SLAB.get()).stairs(CCBlocks.CALCITE_STAIRS.get()).wall(CCBlocks.CALCITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_CALCITE = new BlockFamily.Builder(CCBlocks.POLISHED_CALCITE.get()).slab(CCBlocks.POLISHED_CALCITE_SLAB.get()).stairs(CCBlocks.POLISHED_CALCITE_STAIRS.get()).getFamily();
	public static final BlockFamily TUFF = new BlockFamily.Builder(Blocks.TUFF).slab(CCBlocks.TUFF_SLAB.get()).stairs(CCBlocks.TUFF_STAIRS.get()).wall(CCBlocks.TUFF_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_TUFF = new BlockFamily.Builder(CCBlocks.POLISHED_TUFF.get()).slab(CCBlocks.POLISHED_TUFF_SLAB.get()).stairs(CCBlocks.POLISHED_TUFF_STAIRS.get()).getFamily();
	public static final BlockFamily COBBLESTONE_BRICKS = new BlockFamily.Builder(CCBlocks.COBBLESTONE_BRICKS.get()).slab(CCBlocks.COBBLESTONE_BRICK_SLAB.get()).stairs(CCBlocks.COBBLESTONE_BRICK_STAIRS.get()).wall(CCBlocks.COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily COBBLESTONE_TILES = new BlockFamily.Builder(CCBlocks.COBBLESTONE_TILES.get()).slab(CCBlocks.COBBLESTONE_TILE_SLAB.get()).stairs(CCBlocks.COBBLESTONE_TILE_STAIRS.get()).wall(CCBlocks.COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_BRICKS = new BlockFamily.Builder(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).slab(CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get()).stairs(CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get()).wall(CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get()).getFamily();
	public static final BlockFamily MOSSY_COBBLESTONE_TILES = new BlockFamily.Builder(CCBlocks.MOSSY_COBBLESTONE_TILES.get()).slab(CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get()).stairs(CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get()).wall(CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get()).getFamily();
	public static final BlockFamily LAPIS_LAZULI_BRICKS = new BlockFamily.Builder(CCBlocks.LAPIS_LAZULI_BRICKS.get()).slab(CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get()).stairs(CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get()).wall(CCBlocks.LAPIS_LAZULI_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SPINEL_BRICKS = new BlockFamily.Builder(CCBlocks.SPINEL_BRICKS.get()).slab(CCBlocks.SPINEL_BRICK_SLAB.get()).stairs(CCBlocks.SPINEL_BRICK_STAIRS.get()).wall(CCBlocks.SPINEL_BRICK_WALL.get()).getFamily();
	public static final BlockFamily SANGUINE_PLATES = new BlockFamily.Builder(CCBlocks.SANGUINE_PLATES.get()).slab(CCBlocks.SANGUINE_SLAB.get()).stairs(CCBlocks.SANGUINE_STAIRS.get()).getFamily();
	public static final BlockFamily CUT_AMETHYST_BRICKS = new BlockFamily.Builder(CCBlocks.CUT_AMETHYST_BRICKS.get()).slab(CCBlocks.CUT_AMETHYST_BRICK_SLAB.get()).stairs(CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get()).wall(CCBlocks.CUT_AMETHYST_BRICK_WALL.get()).getFamily();

	public CCRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.BUNDLE, CreativeModeTab.TAB_TOOLS, "f_41377_");
		ShapedRecipeBuilder.shaped(Items.BUNDLE).define('R', Items.LEATHER).define('S', Items.STRING).pattern("SRS").pattern("R R").pattern("RRR").unlockedBy("has_leather", has(Items.LEATHER)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));

		ShapedRecipeBuilder.shaped(Blocks.RAIL, 3).define('#', Items.STICK).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(Blocks.ACTIVATOR_RAIL, 3).define('#', Blocks.REDSTONE_TORCH).define('S', Items.STICK).define('X', Items.IRON_NUGGET).pattern("XSX").pattern("X#X").pattern("XSX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(Blocks.DETECTOR_RAIL, 3).define('R', Items.REDSTONE).define('#', Blocks.STONE_PRESSURE_PLATE).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(Blocks.POWERED_RAIL, 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', Items.GOLD_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.SPIKED_RAIL.get(), 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', CCItemTags.NUGGETS_SILVER).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(Blocks.CHAIN).define('#', Items.IRON_NUGGET).pattern("#").pattern("#").pattern("#").unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET)).save(consumer);

		ShapedRecipeBuilder.shaped(CCItems.BEJEWELED_PEARL.get(), 2).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.BEJEWELED_APPLE.get(), 2).define('A', Items.GOLDEN_APPLE).define('S', CCItems.SPINEL.get()).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.TMT.get(), 4).define('T', Items.TNT).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(CCItems.BLUNT_ARROW.get(), 4).requires(Items.ARROW).requires(CCItems.SPINEL.get()).unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer);

		SpecialRecipeBuilder.special(CCRecipeSerializers.TOOLBOX_WAXING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":toolbox_waxing");
		ShapedRecipeBuilder.shaped(CCBlocks.TOOLBOX.get()).define('C', Blocks.COPPER_BLOCK).define('I', Tags.Items.INGOTS_COPPER).pattern(" I ").pattern("C C").pattern("CCC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.TUNING_FORK.get()).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.BAROMETER.get()).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		mimingRecipe(consumer, Items.MUSIC_DISC_11, CCItems.MUSIC_DISC_EPILOGUE.get());
		ShapedRecipeBuilder.shaped(CCBlocks.ROCKY_DIRT.get(), 4).define('D', Blocks.DIRT).define('C', Blocks.COBBLESTONE).pattern("DC").pattern("CD").unlockedBy("has_dirt", has(Blocks.DIRT)).unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE)).save(consumer);
		ShapelessRecipeBuilder.shapeless(Blocks.ROOTED_DIRT).requires(Blocks.DIRT).requires(Blocks.HANGING_ROOTS).unlockedBy("has_hanging_roots", has(Blocks.HANGING_ROOTS)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Blocks.ROOTED_DIRT)));

		ShapedRecipeBuilder.shaped(CCBlocks.LAVA_LAMP.get()).define('G', Tags.Items.INGOTS_GOLD).define('B', BlueprintItemTags.BUCKETS_LAVA).pattern("GGG").pattern("GBG").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.FLOODLIGHT.get()).define('C', Tags.Items.INGOTS_COPPER).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
//		ShapedRecipeBuilder.shaped(CCBlocks.INDUCTOR.get()).define('C', Tags.Items.INGOTS_COPPER).define('I', Items.IRON_BLOCK).define('R', Items.REDSTONE).pattern("CIC").pattern("CRC").pattern("CIC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.CUPRIC_TORCH.get(), 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Items.STICK).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.CUPRIC_CAMPFIRE.get()).define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_stick", has(Items.STICK)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.CUPRIC_LANTERN.get()).define('#', CCBlocks.CUPRIC_TORCH.get()).define('X', Items.IRON_NUGGET).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper", has(CCBlocks.CUPRIC_TORCH.get())).save(consumer);
		conditionalRecipe(consumer, BUZZIER_BEES_LOADED, ShapedRecipeBuilder.shaped(CCBlocks.CUPRIC_CANDLE.get()).define('S', Items.STRING).define('H', Items.HONEYCOMB).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("S").pattern("H").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)));

		ShapedRecipeBuilder.shaped(CCBlocks.BRAZIER.get()).define('#', ItemTags.COALS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_coal", has(ItemTags.COALS)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.SOUL_BRAZIER.get()).define('#', ItemTags.SOUL_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.CUPRIC_BRAZIER.get()).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		conditionalRecipe(consumer, ENDERGETIC_LOADED, ShapedRecipeBuilder.shaped(CCBlocks.ENDER_BRAZIER.get()).define('#', CCItemTags.ENDER_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_end_stone", has(CCItemTags.ENDER_FIRE_BASE_BLOCKS)));

		ShapedRecipeBuilder.shaped(CCBlocks.COPPER_BARS.get(), 16).define('#', Tags.Items.INGOTS_COPPER).pattern("###").pattern("###").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());

		ShapelessRecipeBuilder.shapeless(CCBlocks.COPPER_BUTTON.get()).requires(ItemTags.WOODEN_BUTTONS).requires(Tags.Items.INGOTS_COPPER).unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, CCBlocks.COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get());
		waxRecipe(consumer, CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
		waxRecipe(consumer, CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
		waxRecipe(consumer, CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());

		nineBlockStorageRecipes(consumer, CCItems.SPINEL.get(), CCBlocks.SPINEL_BLOCK.get());
		nineBlockStorageRecipes(consumer, CCItems.RAW_SILVER.get(), CCBlocks.RAW_SILVER_BLOCK.get());
		nineBlockStorageRecipes(consumer, Items.ROTTEN_FLESH, CCBlocks.ROTTEN_FLESH_BLOCK.get());
		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, CCItems.SILVER_INGOT.get(), CCBlocks.SILVER_BLOCK.get(), "silver_ingot_from_silver_block", "silver_ingot");
		nineBlockStorageRecipesWithCustomPacking(consumer, CCItems.SILVER_NUGGET.get(), CCItems.SILVER_INGOT.get(), "silver_ingot_from_nuggets", "silver_ingot");
		nineBlockStorageRecipesWithCustomPacking(consumer, CCItems.COPPER_NUGGET.get(), Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");

		oreSmelting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 200, "silver_ingot");
		oreSmelting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 200, "spinel");
		oreBlasting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 100, "silver_ingot");
		oreBlasting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 100, "spinel");

		ShapedRecipeBuilder.shaped(CCItems.SILVER_AXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_BOOTS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_CHESTPLATE.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_HELMET.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_HOE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_LEGGINGS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_PICKAXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_SHOVEL.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("#").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SILVER_SWORD.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("X").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		pressurePlateBuilder(CCBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), Ingredient.of(CCItemTags.INGOTS_SILVER)).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.SILVER_BARS.get(), 16).define('#', CCItemTags.INGOTS_SILVER).pattern("###").pattern("###").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		conditionalRecipe(consumer, GOLD_METAL_BUTTON, ShapelessRecipeBuilder.shapeless(CCBlocks.SILVER_BUTTON.get()).requires(ItemTags.WOODEN_BUTTONS).requires(CCItemTags.INGOTS_SILVER).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), CCItems.SILVER_NUGGET.get(), 0.1F, 200).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getSmeltingRecipeName(CCItems.SILVER_NUGGET.get())));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), CCItems.SILVER_NUGGET.get(), 0.1F, 100).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getBlastingRecipeName(CCItems.SILVER_NUGGET.get())));
		ShapedRecipeBuilder.shaped(CCItems.KUNAI.get()).define('#', Tags.Items.RODS_WOODEN).define('S', CCItemTags.NUGGETS_SILVER).pattern(" S").pattern("# ").unlockedBy("has_silver_nugget", has(CCItemTags.NUGGETS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.GOLDEN_BARS.get(), 16).define('#', Items.GOLD_INGOT).pattern("###").pattern("###").unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.GOLDEN_BUCKET.get()).define('#', Blocks.GOLD_BLOCK).pattern("# #").pattern(" # ").unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK)).save(consumer);
		conditionalRecipe(consumer, new NotCondition(new ModLoadedCondition("environmental")), ShapedRecipeBuilder.shaped(Blocks.CAKE).define('A', CCItems.GOLDEN_MILK_BUCKET.get()).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Items.EGG).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Items.EGG)), new ResourceLocation(CavernsAndChasms.MOD_ID, getSimpleRecipeName(Blocks.CAKE)));
		conditionalRecipe(consumer, new NotCondition(new TagEmptyCondition(CCItemTags.BOTTLES_MILK.location())), ShapelessRecipeBuilder.shapeless(CCItems.GOLDEN_MILK_BUCKET.get()).requires(CCItems.GOLDEN_BUCKET.get()).requires(Ingredient.of(CCItemTags.BOTTLES_MILK), 3).unlockedBy("has_milk_bottle", has(CCItemTags.BOTTLES_MILK)));

		ShapelessRecipeBuilder.shapeless(CCItems.SANGUINE_PLATING.get()).requires(Items.ROTTEN_FLESH, 3).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 2).requires(Items.GHAST_TEAR, 2).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SANGUINE_HELMET.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("XXX").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SANGUINE_CHESTPLATE.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SANGUINE_LEGGINGS.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SANGUINE_BOOTS.get()).define('X', CCItems.SANGUINE_PLATING.get()).pattern("X X").pattern("X X").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.SANGUINE_PLATES.get(), 12).define('#', CCItems.SANGUINE_PLATING.get()).pattern("##").pattern("##").unlockedBy("has_sanguine_plating", has(CCItems.SANGUINE_PLATING.get())).save(consumer);
		generateRecipes(consumer, SANGUINE_PLATES);
		verticalSlabRecipes(consumer, SANGUINE_PLATES, CCBlocks.SANGUINE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.SANGUINE_SLAB.get(), CCBlocks.SANGUINE_PLATES.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.SANGUINE_STAIRS.get(), CCBlocks.SANGUINE_PLATES.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.SANGUINE_VERTICAL_SLAB.get(), CCBlocks.SANGUINE_PLATES.get(), 2);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, CCItems.NECROMIUM_INGOT.get(), CCBlocks.NECROMIUM_BLOCK.get(), "necromium_ingot_from_necromium_block", "necromium_ingot");
		ShapelessRecipeBuilder.shapeless(CCItems.NECROMIUM_INGOT.get()).requires(Items.NETHERITE_SCRAP, 4).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 4).group("necromium_ingot").unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(consumer);
		nineBlockStorageRecipesWithCustomPacking(consumer, CCItems.NECROMIUM_NUGGET.get(), CCItems.NECROMIUM_INGOT.get(), "necromium_ingot_from_nuggets", "necromium_ingot");
		necromiumSmithing(consumer, Items.DIAMOND_CHESTPLATE, CCItems.NECROMIUM_CHESTPLATE.get());
		necromiumSmithing(consumer, Items.DIAMOND_LEGGINGS, CCItems.NECROMIUM_LEGGINGS.get());
		necromiumSmithing(consumer, Items.DIAMOND_HELMET, CCItems.NECROMIUM_HELMET.get());
		necromiumSmithing(consumer, Items.DIAMOND_BOOTS, CCItems.NECROMIUM_BOOTS.get());
		necromiumSmithing(consumer, Items.DIAMOND_SWORD, CCItems.NECROMIUM_SWORD.get());
		necromiumSmithing(consumer, Items.DIAMOND_AXE, CCItems.NECROMIUM_AXE.get());
		necromiumSmithing(consumer, Items.DIAMOND_PICKAXE, CCItems.NECROMIUM_PICKAXE.get());
		necromiumSmithing(consumer, Items.DIAMOND_HOE, CCItems.NECROMIUM_HOE.get());
		necromiumSmithing(consumer, Items.DIAMOND_SHOVEL, CCItems.NECROMIUM_SHOVEL.get());
		necromiumSmithing(consumer, Items.DIAMOND_HORSE_ARMOR, CCItems.NECROMIUM_HORSE_ARMOR.get());
		netheriteSmithing(consumer, Items.DIAMOND_HORSE_ARMOR, CCItems.NETHERITE_HORSE_ARMOR.get());

		nineBlockStorageRecipesWithCustomPacking(consumer, CCItems.NETHERITE_NUGGET.get(), Items.NETHERITE_INGOT, "netherite_ingot_from_nuggets", "netherite_ingot");

		ShapedRecipeBuilder.shaped(CCBlocks.LAPIS_LAZULI_BRICKS.get()).define('#', Items.LAPIS_LAZULI).pattern("##").pattern("##").unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(consumer);
		generateRecipes(consumer, LAPIS_LAZULI_BRICKS);
		ShapedRecipeBuilder.shaped(CCBlocks.LAPIS_LAZULI_PILLAR.get(), 2).define('#', CCBlocks.LAPIS_LAZULI_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(CCBlocks.LAPIS_LAZULI_BRICKS.get()), has(CCBlocks.LAPIS_LAZULI_BRICKS.get())).unlockedBy(getHasName(CCBlocks.LAPIS_LAZULI_PILLAR.get()), has(CCBlocks.LAPIS_LAZULI_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.LAPIS_LAZULI_LAMP.get()).define('#', Items.LAPIS_LAZULI).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		verticalSlabRecipes(consumer, LAPIS_LAZULI_BRICKS, CCBlocks.LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.LAPIS_LAZULI_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.LAPIS_LAZULI_PILLAR.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICKS.get(), 2);

		ShapedRecipeBuilder.shaped(CCBlocks.SPINEL_BRICKS.get()).define('#', CCItems.SPINEL.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCItems.SPINEL.get()), has(CCItems.SPINEL.get())).save(consumer);
		generateRecipes(consumer, SPINEL_BRICKS);
		ShapedRecipeBuilder.shaped(CCBlocks.SPINEL_PILLAR.get(), 2).define('#', CCBlocks.SPINEL_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(CCBlocks.SPINEL_BRICKS.get()), has(CCBlocks.SPINEL_BRICKS.get())).unlockedBy(getHasName(CCBlocks.SPINEL_PILLAR.get()), has(CCBlocks.SPINEL_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.SPINEL_LAMP.get()).define('#', CCItems.SPINEL.get()).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		verticalSlabRecipes(consumer, SPINEL_BRICKS, CCBlocks.SPINEL_BRICK_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.SPINEL_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.SPINEL_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.SPINEL_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.SPINEL_PILLAR.get(), CCBlocks.SPINEL_BRICKS.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.SPINEL_BRICK_VERTICAL_SLAB.get(), CCBlocks.SPINEL_BRICKS.get(), 2);

		ShapelessRecipeBuilder.shapeless(Blocks.CALCITE).requires(Blocks.DIORITE).requires(Items.AMETHYST_SHARD).unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.CALCITE).getPath()));
		generateRecipes(consumer, CALCITE);
		verticalSlabRecipes(consumer, CALCITE, CCBlocks.CALCITE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_STAIRS.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_WALL.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_STAIRS.get(), Blocks.CALCITE);
		ShapedRecipeBuilder.shaped(CCBlocks.POLISHED_CALCITE.get(), 4).define('#', Blocks.CALCITE).pattern("##").pattern("##").unlockedBy("has_calcite", has(Blocks.CALCITE)).save(consumer);
		generateRecipes(consumer, POLISHED_CALCITE);
		verticalSlabRecipes(consumer, POLISHED_CALCITE, CCBlocks.POLISHED_CALCITE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_SLAB.get(), CCBlocks.POLISHED_CALCITE.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_STAIRS.get(), CCBlocks.POLISHED_CALCITE.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.CALCITE_VERTICAL_SLAB.get(), Blocks.CALCITE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.POLISHED_CALCITE_VERTICAL_SLAB.get(), Blocks.CALCITE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.POLISHED_CALCITE_VERTICAL_SLAB.get(), CCBlocks.POLISHED_CALCITE.get(), 2);

		ShapelessRecipeBuilder.shapeless(Blocks.TUFF, 2).requires(Blocks.BASALT).requires(Blocks.COBBLESTONE).unlockedBy("has_stone", has(Blocks.BASALT)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.TUFF).getPath()));
		generateRecipes(consumer, TUFF);
		verticalSlabRecipes(consumer, TUFF, CCBlocks.TUFF_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_STAIRS.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_WALL.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_STAIRS.get(), Blocks.TUFF);
		ShapedRecipeBuilder.shaped(CCBlocks.POLISHED_TUFF.get(), 4).define('#', Blocks.TUFF).pattern("##").pattern("##").unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		generateRecipes(consumer, POLISHED_TUFF);
		verticalSlabRecipes(consumer, POLISHED_TUFF, CCBlocks.POLISHED_TUFF_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_SLAB.get(), CCBlocks.POLISHED_TUFF.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_STAIRS.get(), CCBlocks.POLISHED_TUFF.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.TUFF_VERTICAL_SLAB.get(), Blocks.TUFF, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.POLISHED_TUFF_VERTICAL_SLAB.get(), Blocks.TUFF, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.POLISHED_TUFF_VERTICAL_SLAB.get(), CCBlocks.POLISHED_TUFF.get(), 2);

		ShapedRecipeBuilder.shaped(CCBlocks.DRIPSTONE_SHINGLES.get(), 4).define('#', Blocks.DRIPSTONE_BLOCK).pattern("##").pattern("##").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES);
		verticalSlabRecipes(consumer, DRIPSTONE_SHINGLES, CCBlocks.DRIPSTONE_SHINGLE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), CCBlocks.DRIPSTONE_SHINGLES.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), Blocks.DRIPSTONE_BLOCK, 2);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.DRIPSTONE_SHINGLE_VERTICAL_SLAB.get(), Blocks.DRIPSTONE_BLOCK, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.DRIPSTONE_SHINGLE_VERTICAL_SLAB.get(), CCBlocks.DRIPSTONE_SHINGLES.get(), 2);
		ShapelessRecipeBuilder.shapeless(CCBlocks.FLOODED_DRIPSTONE_SHINGLES.get(), 8).requires(BlueprintItemTags.BUCKETS_WATER).requires(CCBlocks.DRIPSTONE_SHINGLES.get(), 8).unlockedBy("has_dripstone_shingles", has(CCBlocks.DRIPSTONE_SHINGLES.get())).save(consumer);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, Items.AMETHYST_SHARD, CCBlocks.AMETHYST_BLOCK.get(), "amethyst_from_amethyst_block", "amethyst_shard");
		ShapedRecipeBuilder.shaped(CCBlocks.CUT_AMETHYST.get(), 4).define('#', Blocks.AMETHYST_BLOCK).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.AMETHYST_BLOCK), has(Blocks.AMETHYST_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.CUT_AMETHYST_BRICKS.get(), 4).define('#', CCBlocks.CUT_AMETHYST.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.CUT_AMETHYST.get()), has(CCBlocks.CUT_AMETHYST.get())).save(consumer);
		generateRecipes(consumer, CUT_AMETHYST_BRICKS);
		verticalSlabRecipes(consumer, CUT_AMETHYST_BRICKS, CCBlocks.CUT_AMETHYST_BRICK_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), CCBlocks.CUT_AMETHYST_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), CCBlocks.CUT_AMETHYST_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), CCBlocks.CUT_AMETHYST_BRICKS.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.CUT_AMETHYST_BRICK_VERTICAL_SLAB.get(), CCBlocks.CUT_AMETHYST_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICKS.get(), CCBlocks.CUT_AMETHYST.get());
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), CCBlocks.CUT_AMETHYST.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), CCBlocks.CUT_AMETHYST.get());
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), CCBlocks.CUT_AMETHYST.get());
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.CUT_AMETHYST_BRICK_VERTICAL_SLAB.get(), CCBlocks.CUT_AMETHYST.get(), 2);

		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICKS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_SLAB.get(), Blocks.AMETHYST_BLOCK, 2);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_STAIRS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.CUT_AMETHYST_BRICK_WALL.get(), Blocks.AMETHYST_BLOCK);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.CUT_AMETHYST_BRICK_VERTICAL_SLAB.get(), Blocks.AMETHYST_BLOCK, 2);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, Items.ECHO_SHARD, CCBlocks.ECHO_BLOCK.get(), "echo_shard_from_echo_block", "echo_shard");

		ShapedRecipeBuilder.shaped(CCBlocks.COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE)).save(consumer);
		generateRecipes(consumer, COBBLESTONE_BRICKS);
		verticalSlabRecipes(consumer, COBBLESTONE_BRICKS, CCBlocks.COBBLESTONE_BRICK_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_WALL.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICKS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_BRICK_WALL.get(), Blocks.COBBLESTONE);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.COBBLESTONE_BRICK_VERTICAL_SLAB.get(), Blocks.COBBLESTONE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);

		ShapedRecipeBuilder.shaped(CCBlocks.COBBLESTONE_TILES.get(), 4).define('#', CCBlocks.COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.COBBLESTONE_BRICKS.get()), has(CCBlocks.COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, COBBLESTONE_TILES);
		verticalSlabRecipes(consumer, COBBLESTONE_TILES, CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_TILES.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILES.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_WALL.get(), CCBlocks.COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILES.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.COBBLESTONE_TILE_WALL.get(), Blocks.COBBLESTONE);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get(), Blocks.COBBLESTONE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_BRICKS.get(), 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.COBBLESTONE_TILES.get(), 2);

		ShapedRecipeBuilder.shaped(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.MOSSY_COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.MOSSY_COBBLESTONE), has(Blocks.MOSSY_COBBLESTONE)).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_BRICKS);
		verticalSlabRecipes(consumer, MOSSY_COBBLESTONE_BRICKS, CCBlocks.MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get(), Blocks.MOSSY_COBBLESTONE);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);

		ShapedRecipeBuilder.shaped(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), 4).define('#', CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()), has(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_TILES);
		verticalSlabRecipes(consumer, MOSSY_COBBLESTONE_TILES, CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILES.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterResultFromBase(consumer, CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get(), Blocks.MOSSY_COBBLESTONE);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), 2);
		conditionalStonecuttingRecipe(consumer, VERTICAL_SLABS, CCBlocks.MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get(), 2);

		ShapelessRecipeBuilder.shapeless(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).requires(CCBlocks.COBBLESTONE_BRICKS.get()).requires(Blocks.VINE).group("cobblestone_bricks").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(CCBlocks.MOSSY_COBBLESTONE_TILES.get()).requires(CCBlocks.COBBLESTONE_TILES.get()).requires(Blocks.VINE).group("mossy_cobblestone_tiles").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get()).requires(CCBlocks.COBBLESTONE_BRICKS.get()).requires(Blocks.MOSS_BLOCK).group("cobblestone_bricks").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSS_BLOCK));
		ShapelessRecipeBuilder.shapeless(CCBlocks.MOSSY_COBBLESTONE_TILES.get()).requires(CCBlocks.COBBLESTONE_TILES.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_tiles").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSS_BLOCK));

		generateRecipes(consumer, AZALEA_PLANKS);
		planksFromLogs(consumer, CCBlocks.AZALEA_PLANKS.get(), CCItemTags.AZALEA_LOGS);
		woodFromLogs(consumer, CCBlocks.AZALEA_WOOD.get(), CCBlocks.AZALEA_LOG.get());
		woodFromLogs(consumer, CCBlocks.STRIPPED_AZALEA_WOOD.get(), CCBlocks.STRIPPED_AZALEA_LOG.get());
		woodenBoat(consumer, CCItems.AZALEA_BOAT.getFirst().get(), CCBlocks.AZALEA_PLANKS.get());
		chestBoat(consumer, CCItems.AZALEA_BOAT.getSecond().get(), CCItems.AZALEA_BOAT.getFirst().get());
		verticalSlabRecipes(consumer, AZALEA_PLANKS, CCBlocks.AZALEA_VERTICAL_SLAB.get());
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapelessRecipeBuilder.shapeless(CCItems.AZALEA_FURNACE_BOAT.get()).requires(Blocks.FURNACE).requires(CCItems.AZALEA_BOAT.getFirst().get()).group("furnace_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapedRecipeBuilder.shaped(CCItems.LARGE_AZALEA_BOAT.get()).define('#', CCBlocks.AZALEA_PLANKS.get()).define('B', CCItems.AZALEA_BOAT.getFirst().get()).pattern("#B#").pattern("###").group("large_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
//		conditionalRecipe(consumer, WOODWORKS_LOADED, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_BOARDS.get(), 3).define('#', CCBlocks.AZALEA_PLANKS.get()).pattern("#").pattern("#").pattern("#").group("wooden_boards").unlockedBy(getHasName(CCBlocks.AZALEA_PLANKS.get()), has(CCBlocks.AZALEA_PLANKS.get())));
//		conditionalRecipe(consumer, WOODEN_BOOKSHELVES, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_BOOKSHELF.get()).define('#', CCBlocks.AZALEA_PLANKS.get()).define('X', Items.BOOK).pattern("###").pattern("XXX").pattern("###").group("wooden_bookshelf").unlockedBy("has_book", has(Items.BOOK)));
//		conditionalRecipe(consumer, WOODEN_LADDERS, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_LADDER.get(), 4).define('#', CCBlocks.AZALEA_PLANKS.get()).define('S', Items.STICK).pattern("S S").pattern("S#S").pattern("S S").group("wooden_ladder").unlockedBy("has_stick", has(Items.STICK)));
//		conditionalRecipe(consumer, WOODWORKS_LOADED, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_BEEHIVE.get()).define('#', CCBlocks.AZALEA_PLANKS.get()).define('H', Items.HONEYCOMB).pattern("###").pattern("HHH").pattern("###").group("wooden_beehive").unlockedBy("has_honeycomb", has(Items.HONEYCOMB)));
//		conditionalRecipe(consumer, WOODEN_CHESTS, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_CHEST.getFirst().get()).define('#', CCBlocks.AZALEA_PLANKS.get()).pattern("###").pattern("# #").pattern("###").group("wooden_chest").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])));
//		conditionalRecipe(consumer, WOODEN_CHESTS, ShapelessRecipeBuilder.shapeless(CCBlocks.AZALEA_CHEST.getSecond().get()).requires(CCBlocks.AZALEA_CHEST.getFirst().get()).requires(Blocks.TRIPWIRE_HOOK).group("wooden_trapped_chest").unlockedBy("has_tripwire_hook", has(Blocks.TRIPWIRE_HOOK)));
		conditionalRecipe(consumer, VERTICAL_PLANKS, ShapedRecipeBuilder.shaped(CCBlocks.VERTICAL_AZALEA_PLANKS.get(), 3).define('#', CCBlocks.AZALEA_PLANKS.get()).pattern("#").pattern("#").pattern("#").group("vertical_planks").unlockedBy(getHasName(CCBlocks.AZALEA_PLANKS.get()), has(CCBlocks.AZALEA_PLANKS.get())));
		conditionalRecipe(consumer, VERTICAL_PLANKS, ShapelessRecipeBuilder.shapeless(CCBlocks.VERTICAL_AZALEA_PLANKS.get()).requires(CCBlocks.AZALEA_PLANKS.get()).unlockedBy(getHasName(CCBlocks.VERTICAL_AZALEA_PLANKS.get()), has(CCBlocks.VERTICAL_AZALEA_PLANKS.get())), new ResourceLocation(RecipeBuilder.getDefaultRecipeId(CCBlocks.VERTICAL_AZALEA_PLANKS.get()) + "_revert"));
		conditionalRecipe(consumer, WOODEN_POSTS, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_POST.get(), 8).define('#', CCBlocks.AZALEA_WOOD.get()).pattern("#").pattern("#").pattern("#").group("wooden_post").unlockedBy(getHasName(CCBlocks.AZALEA_WOOD.get()), has(CCBlocks.AZALEA_WOOD.get())));
		conditionalRecipe(consumer, WOODEN_POSTS, ShapedRecipeBuilder.shaped(CCBlocks.STRIPPED_AZALEA_POST.get(), 8).define('#', CCBlocks.STRIPPED_AZALEA_WOOD.get()).pattern("#").pattern("#").pattern("#").group("wooden_post").unlockedBy(getHasName(CCBlocks.STRIPPED_AZALEA_WOOD.get()), has(CCBlocks.STRIPPED_AZALEA_WOOD.get())));
		conditionalRecipe(consumer, WOOD_TO_CHEST_RECIPES, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_CHEST.getFirst().get(), 4).define('#', CCItemTags.AZALEA_LOGS).pattern("###").pattern("# #").pattern("###").group("wooden_chest").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])), new ResourceLocation(RecipeBuilder.getDefaultRecipeId(CCBlocks.AZALEA_CHEST.getFirst().get()) + "_wood"));
		conditionalRecipe(consumer, HEDGES, ShapedRecipeBuilder.shaped(CCBlocks.AZALEA_HEDGE.get(), 2).define('#', Blocks.AZALEA_LEAVES).define('L', CCItemTags.AZALEA_LOGS).pattern("#").pattern("L").group("hedge").unlockedBy(getHasName(Blocks.AZALEA_LEAVES), has(Blocks.AZALEA_LEAVES)));
		conditionalRecipe(consumer, HEDGES, ShapedRecipeBuilder.shaped(CCBlocks.FLOWERING_AZALEA_HEDGE.get(), 2).define('#', Blocks.FLOWERING_AZALEA_LEAVES).define('L', CCItemTags.AZALEA_LOGS).pattern("#").pattern("L").group("hedge").unlockedBy(getHasName(Blocks.FLOWERING_AZALEA_LEAVES), has(Blocks.FLOWERING_AZALEA_LEAVES)));
	}

	public static QuarkFlagRecipeCondition quarkFlag(String flag) {
		return new QuarkFlagRecipeCondition(new ResourceLocation(Blueprint.MOD_ID, "quark_flag"), flag);
	}

	public static void conditionalStonecuttingRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, ItemLike output, ItemLike input, int count) {
		conditionalRecipe(consumer, condition, SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), output, count).unlockedBy(getHasName(input), has(input)), new ResourceLocation(CavernsAndChasms.MOD_ID, getConversionRecipeName(output, input) + "_stonecutting"));
	}

	public static void verticalSlabRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family, ItemLike verticalSlab) {
		conditionalRecipe(consumer, VERTICAL_SLABS, ShapedRecipeBuilder.shaped(verticalSlab, 6).define('#', family.getBaseBlock()).pattern("#").pattern("#").pattern("#").unlockedBy(getHasName(family.getBaseBlock()), has(family.getBaseBlock())));
		conditionalRecipe(consumer, VERTICAL_SLABS, ShapelessRecipeBuilder.shapeless(verticalSlab).requires(family.get(Variant.SLAB)).unlockedBy(getHasName(verticalSlab), has(verticalSlab)), new ResourceLocation(RecipeBuilder.getDefaultRecipeId(verticalSlab) + "_revert"));
	}

	protected static void netheriteSmithing(Consumer<FinishedRecipe> consumer, Item input, Item output) {
		UpgradeRecipeBuilder.smithing(Ingredient.of(input), Ingredient.of(Items.NETHERITE_INGOT), output).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(output) + "_smithing"));
	}

	protected static void necromiumSmithing(Consumer<FinishedRecipe> consumer, Item input, Item output) {
		UpgradeRecipeBuilder.smithing(Ingredient.of(input), Ingredient.of(CCItemTags.INGOTS_NECROMIUM), output).unlocks("has_necromium_ingot", has(CCItemTags.INGOTS_NECROMIUM)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(output) + "_smithing"));
	}

	private static void waxRecipe(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike result) {
		ShapelessRecipeBuilder.shapeless(result).requires(input).requires(Items.HONEYCOMB).group(getItemName(result)).unlockedBy(getHasName(input), has(input)).save(consumer, getModConversionRecipeName(result, Items.HONEYCOMB));
	}

	private static ResourceLocation getModConversionRecipeName(ItemLike result, ItemLike input) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(result) + "_from_" + getItemName(input));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeBuilder recipe) {
		conditionalRecipe(consumer, condition, recipe, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + recipe.getResult().getItemCategory().getRecipeFolderName() + "/" + id.getPath())).build(consumer, id);
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, UpgradeRecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + recipe.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())).build(consumer, id);
	}

	private static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike chestBoat, ItemLike boat) {
		ShapelessRecipeBuilder.shapeless(chestBoat).requires(Tags.Items.CHESTS_WOODEN).requires(boat).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(consumer);
	}

	public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, ItemLike block) {
		nineBlockStorageRecipes(consumer, item, block, getItemName(block), null, getItemName(item), null);
	}

	public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, ItemLike block, String shapedName, @Nullable String shapedGroup, String shapelessName, @Nullable String shapelessGroup) {
		ShapelessRecipeBuilder.shapeless(item, 9).requires(block).group(shapelessGroup).unlockedBy(getHasName(block), has(block)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapelessName));
		ShapedRecipeBuilder.shaped(block).define('#', item).pattern("###").pattern("###").pattern("###").group(shapedGroup).unlockedBy(getHasName(item), has(item)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapedName));
	}

	public static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> p_176563_, ItemLike p_176564_, ItemLike p_176565_, String p_176566_, String p_176567_) {
		nineBlockStorageRecipes(p_176563_, p_176564_, p_176565_, p_176566_, p_176567_, getSimpleRecipeName(p_176564_), null);
	}

	public static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> p_176617_, ItemLike p_176618_, ItemLike p_176619_, String p_176620_, String p_176621_) {
		nineBlockStorageRecipes(p_176617_, p_176618_, p_176619_, getSimpleRecipeName(p_176619_), null, p_176620_, p_176621_);
	}

	public static void stonecutterResultFromBase(Consumer<FinishedRecipe> p_176736_, ItemLike p_176737_, ItemLike p_176738_) {
		stonecutterResultFromBase(p_176736_, p_176737_, p_176738_, 1);
	}

	public static void stonecutterResultFromBase(Consumer<FinishedRecipe> p_176547_, ItemLike p_176548_, ItemLike p_176549_, int p_176550_) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(p_176549_), p_176548_, p_176550_).unlockedBy(getHasName(p_176549_), has(p_176549_)).save(p_176547_, getModConversionRecipeName(p_176548_, p_176549_) + "_stonecutting");
	}

	protected static void oreSmelting(Consumer<FinishedRecipe> p_176592_, List<ItemLike> p_176593_, ItemLike p_176594_, float p_176595_, int p_176596_, String p_176597_) {
		oreCooking(p_176592_, RecipeSerializer.SMELTING_RECIPE, p_176593_, p_176594_, p_176595_, p_176596_, p_176597_, "_from_smelting");
	}

	protected static void oreBlasting(Consumer<FinishedRecipe> p_176626_, List<ItemLike> p_176627_, ItemLike p_176628_, float p_176629_, int p_176630_, String p_176631_) {
		oreCooking(p_176626_, RecipeSerializer.BLASTING_RECIPE, p_176627_, p_176628_, p_176629_, p_176630_, p_176631_, "_from_blasting");
	}

	public static void foodCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike output) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), output, 0.35F, 200).unlockedBy(getHasName(input), has(input)).save(consumer);
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), output, 0.35F, 100).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_smoking");
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), output, 0.35F, 600).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_campfire_cooking");
	}

	public static void mimingRecipe(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike output) {
		mimingRecipe(Ingredient.of(input), output, 1).unlockedBy(getHasName(input), has(input)).save(consumer);
	}

	public static SingleItemRecipeBuilder mimingRecipe(Ingredient input, ItemLike output, int count) {
		return new SingleItemRecipeBuilder(CCRecipeSerializers.MIMING.get(), input, output, count);
	}

	protected static void oreCooking(Consumer<FinishedRecipe> p_176534_, SimpleCookingSerializer<?> p_176535_, List<ItemLike> p_176536_, ItemLike p_176537_, float p_176538_, int p_176539_, String p_176540_, String p_176541_) {
		for (ItemLike itemlike : p_176536_) {
			SimpleCookingRecipeBuilder.cooking(Ingredient.of(itemlike), p_176537_, p_176538_, p_176539_, p_176535_).group(p_176540_).unlockedBy(getHasName(itemlike), has(itemlike)).save(p_176534_, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(p_176537_) + p_176541_ + "_" + getItemName(itemlike)));
		}

	}
}