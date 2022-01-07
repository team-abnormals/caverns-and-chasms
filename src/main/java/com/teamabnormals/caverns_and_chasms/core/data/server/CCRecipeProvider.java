package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Registry;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CCRecipeProvider extends RecipeProvider {
	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());
	public static final BlockFamily AZALEA_PLANKS = new BlockFamily.Builder(CCBlocks.AZALEA_PLANKS.get()).button(CCBlocks.AZALEA_BUTTON.get()).fence(CCBlocks.AZALEA_FENCE.get()).fenceGate(CCBlocks.AZALEA_FENCE_GATE.get()).pressurePlate(CCBlocks.AZALEA_PRESSURE_PLATE.get()).sign(CCBlocks.AZALEA_SIGN.getFirst().get(), CCBlocks.AZALEA_SIGN.getSecond().get()).slab(CCBlocks.AZALEA_SLAB.get()).stairs(CCBlocks.AZALEA_STAIRS.get()).door(CCBlocks.AZALEA_DOOR.get()).trapdoor(CCBlocks.AZALEA_TRAPDOOR.get()).recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

	public CCRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.BUNDLE, CreativeModeTab.TAB_TOOLS, "f_41377_");
		ShapedRecipeBuilder.shaped(Items.BUNDLE).define('R', Items.RABBIT_HIDE).define('S', Items.STRING).pattern("SRS").pattern("R R").pattern("RRR").unlockedBy("has_rabbit_hide", has(Items.RABBIT_HIDE)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));
		ShapedRecipeBuilder.shaped(CCItems.SPINEL_PEARL.get()).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.SPINEL_CROWN.get()).define('G', Items.GOLD_INGOT).define('S', CCItems.SPINEL.get()).define('B', CCBlocks.SPINEL_BLOCK.get()).pattern("SBS").pattern("GGG").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.TUNING_FORK.get()).define('#', CCItemTags.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(CCItemTags.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.BAROMETER.get()).define('#', CCItemTags.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);

		ShapedRecipeBuilder.shaped(CCBlocks.COPPER_BARS.get(), 16).define('#', CCItemTags.INGOTS_COPPER).pattern("###").pattern("###").unlockedBy("has_copper_ingot", has(CCItemTags.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		waxRecipe(consumer, CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());

		nineBlockStorageRecipes(consumer, CCItems.RAW_SILVER.get(), CCBlocks.RAW_SILVER_BLOCK.get());

		oreSmelting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 200, "silver_ingot");
		oreSmelting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 200, "spinel");
		oreBlasting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 100, "silver_ingot");
		oreBlasting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 100, "spinel");

		generateRecipes(consumer, AZALEA_PLANKS);
		planksFromLogs(consumer, CCBlocks.AZALEA_PLANKS.get(), CCItemTags.AZALEA_LOGS);
		woodFromLogs(consumer, CCBlocks.AZALEA_WOOD.get(), CCBlocks.AZALEA_LOG.get());
		woodFromLogs(consumer, CCBlocks.STRIPPED_AZALEA_WOOD.get(), CCBlocks.STRIPPED_AZALEA_LOG.get());
		woodenBoat(consumer, CCItems.AZALEA_BOAT.get(), CCBlocks.AZALEA_PLANKS.get());
	}

	private static void waxRecipe(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike result) {
		ShapelessRecipeBuilder.shapeless(result).requires(input).requires(Items.HONEYCOMB).group(getItemName(result)).unlockedBy(getHasName(input), has(input)).save(consumer, getConversionRecipeName(result, Items.HONEYCOMB));
	}

	private static ResourceLocation getConversionRecipeName(ItemLike result, ItemLike input) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(result) + "_from_" + getItemName(input));
	}

	private static void planksFromLogs(Consumer<FinishedRecipe> p_126018_, ItemLike p_126019_, Tag<Item> p_126020_) {
		ShapelessRecipeBuilder.shapeless(p_126019_, 4).requires(p_126020_).group("planks").unlockedBy("has_logs", has(p_126020_)).save(p_126018_);
	}

	private static void woodFromLogs(Consumer<FinishedRecipe> p_126003_, ItemLike p_126004_, ItemLike p_126005_) {
		ShapedRecipeBuilder.shaped(p_126004_, 3).define('#', p_126005_).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(p_126005_)).save(p_126003_);
	}

	private static void woodenBoat(Consumer<FinishedRecipe> p_126022_, ItemLike p_126023_, ItemLike p_126024_) {
		ShapedRecipeBuilder.shaped(p_126023_).define('#', p_126024_).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER)).save(p_126022_);
	}

	private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, ItemLike block) {
		nineBlockStorageRecipes(consumer, item, block, getItemName(block), null, getItemName(item), null);
	}

	private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, ItemLike block, String shapedName, @Nullable String shapedGroup, String shapelessName, @Nullable String shapelessGroup) {
		ShapelessRecipeBuilder.shapeless(item, 9).requires(block).group(shapelessGroup).unlockedBy(getHasName(block), has(block)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapelessName));
		ShapedRecipeBuilder.shaped(block).define('#', item).pattern("###").pattern("###").pattern("###").group(shapedGroup).unlockedBy(getHasName(item), has(item)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, shapedName));
	}

	private static void oreSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, ItemLike result, float xp, int cookTime, String group) {
		oreCooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, result, xp, cookTime, group, "_from_smelting");
	}

	private static void oreBlasting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, ItemLike result, float xp, int cookTime, String group) {
		oreCooking(consumer, RecipeSerializer.BLASTING_RECIPE, ingredients, result, xp, cookTime, group, "_from_blasting");
	}

	private static void oreCooking(Consumer<FinishedRecipe> consumer, SimpleCookingSerializer<?> cookingType, List<ItemLike> ingredients, ItemLike result, float xp, int cookTime, String group, String recipeSuffix) {
		for (ItemLike itemlike : ingredients) {
			SimpleCookingRecipeBuilder.cooking(Ingredient.of(itemlike), result, xp, cookTime, cookingType).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(result) + recipeSuffix + "_" + getItemName(itemlike)));
		}
	}

	private static String getHasName(ItemLike item) {
		return "has_" + getItemName(item);
	}

	private static String getItemName(ItemLike item) {
		return Registry.ITEM.getKey(item.asItem()).getPath();
	}
}