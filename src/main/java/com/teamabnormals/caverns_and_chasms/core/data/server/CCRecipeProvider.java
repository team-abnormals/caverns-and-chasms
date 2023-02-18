package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CCRecipeProvider extends RecipeProvider {
	public static final ModLoadedCondition BOATLOAD_LOADED = new ModLoadedCondition("boatload");

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());

	public static final BlockFamily AZALEA_PLANKS = new BlockFamily.Builder(CCBlocks.AZALEA_PLANKS.get()).button(CCBlocks.AZALEA_BUTTON.get()).fence(CCBlocks.AZALEA_FENCE.get()).fenceGate(CCBlocks.AZALEA_FENCE_GATE.get()).pressurePlate(CCBlocks.AZALEA_PRESSURE_PLATE.get()).sign(CCBlocks.AZALEA_SIGN.getFirst().get(), CCBlocks.AZALEA_SIGN.getSecond().get()).slab(CCBlocks.AZALEA_SLAB.get()).stairs(CCBlocks.AZALEA_STAIRS.get()).door(CCBlocks.AZALEA_DOOR.get()).trapdoor(CCBlocks.AZALEA_TRAPDOOR.get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();
	public static final BlockFamily DRIPSTONE_SHINGLES = new BlockFamily.Builder(CCBlocks.DRIPSTONE_SHINGLES.get()).slab(CCBlocks.DRIPSTONE_SHINGLE_SLAB.get()).stairs(CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get()).wall(CCBlocks.DRIPSTONE_SHINGLE_WALL.get()).chiseled(CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get()).getFamily();
	public static final BlockFamily CALCITE = new BlockFamily.Builder(Blocks.CALCITE).slab(CCBlocks.CALCITE_SLAB.get()).stairs(CCBlocks.CALCITE_STAIRS.get()).wall(CCBlocks.CALCITE_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_CALCITE = new BlockFamily.Builder(CCBlocks.POLISHED_CALCITE.get()).slab(CCBlocks.POLISHED_CALCITE_SLAB.get()).stairs(CCBlocks.POLISHED_CALCITE_STAIRS.get()).getFamily();
	public static final BlockFamily TUFF = new BlockFamily.Builder(Blocks.TUFF).slab(CCBlocks.TUFF_SLAB.get()).stairs(CCBlocks.TUFF_STAIRS.get()).wall(CCBlocks.TUFF_WALL.get()).getFamily();
	public static final BlockFamily POLISHED_TUFF = new BlockFamily.Builder(CCBlocks.POLISHED_TUFF.get()).slab(CCBlocks.POLISHED_TUFF_SLAB.get()).stairs(CCBlocks.POLISHED_TUFF_STAIRS.get()).getFamily();

	public CCRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.BUNDLE, CreativeModeTab.TAB_TOOLS, "f_41377_");
		ShapedRecipeBuilder.shaped(Items.BUNDLE).define('R', Items.RABBIT_HIDE).define('S', Items.STRING).pattern("SRS").pattern("R R").pattern("RRR").unlockedBy("has_rabbit_hide", has(Items.RABBIT_HIDE)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));
		ShapedRecipeBuilder.shaped(CCItems.BEJEWELED_PEARL.get(), 2).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.BEJEWELED_APPLE.get(), 2).define('A', Items.GOLDEN_APPLE).define('S', CCItems.SPINEL.get()).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(CCBlocks.TMT.get(), 4).define('T', Items.TNT).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.TUNING_FORK.get()).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.BAROMETER.get()).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.LAVA_LAMP.get()).define('G', Tags.Items.INGOTS_GOLD).define('B', BlueprintItemTags.BUCKETS_LAVA).pattern("GGG").pattern("GBG").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.FLOODLIGHT.get()).define('C', Tags.Items.INGOTS_COPPER).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		// ShapedRecipeBuilder.shaped(CCBlocks.INDUCTOR.get()).define('C', Tags.Items.INGOTS_COPPER).define('I', Items.IRON_BLOCK).define('R', Items.REDSTONE).pattern("CIC").pattern("CRC").pattern("CIC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

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

		ShapedRecipeBuilder.shaped(Blocks.CALCITE, 2).define('B', Items.BONE_MEAL).define('C', Blocks.COBBLESTONE).pattern("CB").pattern("BC").unlockedBy("has_bone_meal", has(Items.BONE_MEAL)).save(consumer);
		generateRecipes(consumer, CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_STAIRS.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.CALCITE_WALL.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE.get(), Blocks.CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_STAIRS.get(), Blocks.CALCITE);
		ShapedRecipeBuilder.shaped(CCBlocks.POLISHED_CALCITE.get(), 4).define('#', Blocks.CALCITE).pattern("##").pattern("##").unlockedBy("has_calcite", has(Blocks.CALCITE)).save(consumer);
		generateRecipes(consumer, POLISHED_CALCITE);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_SLAB.get(), CCBlocks.POLISHED_CALCITE.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_CALCITE_STAIRS.get(), CCBlocks.POLISHED_CALCITE.get());

		generateRecipes(consumer, TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_STAIRS.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.TUFF_WALL.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF.get(), Blocks.TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_STAIRS.get(), Blocks.TUFF);
		ShapedRecipeBuilder.shaped(CCBlocks.POLISHED_TUFF.get(), 4).define('#', Blocks.TUFF).pattern("##").pattern("##").unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		generateRecipes(consumer, POLISHED_TUFF);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_SLAB.get(), CCBlocks.POLISHED_TUFF.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.POLISHED_TUFF_STAIRS.get(), CCBlocks.POLISHED_TUFF.get());

		ShapedRecipeBuilder.shaped(CCBlocks.DRIPSTONE_SHINGLES.get(), 4).define('#', Blocks.DRIPSTONE_BLOCK).pattern("##").pattern("##").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), CCBlocks.DRIPSTONE_SHINGLES.get(), 2);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), CCBlocks.DRIPSTONE_SHINGLES.get());
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_SLAB.get(), Blocks.DRIPSTONE_BLOCK, 2);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_STAIRS.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.DRIPSTONE_SHINGLE_WALL.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterResultFromBase(consumer, CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		ShapelessRecipeBuilder.shapeless(CCBlocks.FLOODED_DRIPSTONE_SHINGLES.get(), 8).requires(BlueprintItemTags.BUCKETS_WATER).requires(CCBlocks.DRIPSTONE_SHINGLES.get(), 8).unlockedBy("has_dripstone_shingles", has(CCBlocks.DRIPSTONE_SHINGLES.get())).save(consumer);

		generateRecipes(consumer, AZALEA_PLANKS);
		planksFromLogs(consumer, CCBlocks.AZALEA_PLANKS.get(), CCItemTags.AZALEA_LOGS);
		woodFromLogs(consumer, CCBlocks.AZALEA_WOOD.get(), CCBlocks.AZALEA_LOG.get());
		woodFromLogs(consumer, CCBlocks.STRIPPED_AZALEA_WOOD.get(), CCBlocks.STRIPPED_AZALEA_LOG.get());
		woodenBoat(consumer, CCItems.AZALEA_BOAT.getFirst().get(), CCBlocks.AZALEA_PLANKS.get());
		chestBoat(consumer, CCItems.AZALEA_BOAT.getSecond().get(), CCItems.AZALEA_BOAT.getFirst().get());
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapelessRecipeBuilder.shapeless(CCItems.AZALEA_FURNACE_BOAT.get()).requires(Blocks.FURNACE).requires(CCItems.AZALEA_BOAT.getFirst().get()).group("furnace_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapedRecipeBuilder.shaped(CCItems.LARGE_AZALEA_BOAT.get()).define('#', CCBlocks.AZALEA_PLANKS.get()).define('B', CCItems.AZALEA_BOAT.getFirst().get()).pattern("#B#").pattern("###").group("large_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
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

	private static void chestBoat(Consumer<FinishedRecipe> p_236372_, ItemLike p_236373_, ItemLike p_236374_) {
		ShapelessRecipeBuilder.shapeless(p_236373_).requires(Blocks.CHEST).requires(p_236374_).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(p_236372_);
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
}