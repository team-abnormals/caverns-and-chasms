package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CCRecipeProvider extends RecipeProvider {
	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());

	public CCRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.BUNDLE, CreativeModeTab.TAB_TOOLS, "f_41377_");
		ShapedRecipeBuilder.shaped(Items.BUNDLE).define('R', Items.RABBIT_HIDE).define('S', Items.STRING).pattern("SRS").pattern("R R").pattern("RRR").unlockedBy("has_rabbit_hide", has(Items.RABBIT_HIDE)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));
		ShapedRecipeBuilder.shaped(CCItems.SPINEL_PEARL.get()).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(CCItems.TUNING_FORK.get()).define('#', CCItemTags.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(CCItemTags.INGOTS_COPPER)).save(consumer);

		nineBlockStorageRecipes(consumer, CCItems.RAW_SILVER.get(), CCBlocks.RAW_SILVER_BLOCK.get());

		oreSmelting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 200, "silver_ingot");
		oreSmelting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 200, "spinel");
		oreBlasting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 100, "silver_ingot");
		oreBlasting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 100, "spinel");
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