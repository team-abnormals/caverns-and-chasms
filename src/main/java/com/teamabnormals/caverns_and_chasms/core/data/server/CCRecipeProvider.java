package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;

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
		oreSmelting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 200, "silver_ingot");
		oreSmelting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 200, "spinel");
		oreBlasting(consumer, SILVER_SMELTABLES, CCItems.SILVER_INGOT.get(), 1.0F, 100, "silver_ingot");
		oreBlasting(consumer, SPINEL_SMELTABLES, CCItems.SPINEL.get(), 0.2F, 100, "spinel");
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