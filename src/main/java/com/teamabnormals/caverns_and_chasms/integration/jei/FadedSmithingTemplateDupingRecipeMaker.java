package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public final class FadedSmithingTemplateDupingRecipeMaker {
	private static final String fadedGroup = "caverns_and_chasms.faded_smithing_template.dupe";
	private static final String emissiveGroup = "caverns_and_chasms.faded_smithing_template.dupe";

	public static List<CraftingRecipe> createRecipes() {
		Level level = Minecraft.getInstance().level;
		List<CraftingRecipe> trimRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream().filter(recipe -> recipe.getResultItem(level.registryAccess()).is(ItemTags.TRIM_TEMPLATES)).toList();
		List<CraftingRecipe> recipes = Lists.newArrayList();

		for (CraftingRecipe recipe : trimRecipes) {
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			if (!ingredients.get(1).getItems()[0].is(ItemTags.TRIM_TEMPLATES))
				continue;

			for (String type : new String[]{"faded", "emissive"}) {
				boolean faded = type.equals("faded");
				Item item = faded ? CCItems.SPINEL.get() : Items.BLAZE_POWDER;
				NonNullList<Ingredient> inputs = NonNullList.of(
						Ingredient.EMPTY,
						Ingredient.of(item),
						ingredients.get(1),
						Ingredient.of(item),
						Ingredient.of(item),
						ingredients.get(4),
						Ingredient.of(item),
						Ingredient.of(item),
						Ingredient.of(item),
						Ingredient.of(item)
				);

				ItemStack output = ingredients.get(1).getItems()[0].copy();
				output.getOrCreateTag().putBoolean(faded ? "FadedTrim" : "EmissiveTrim", true);
				ResourceLocation id = new ResourceLocation(CavernsAndChasms.MOD_ID, BuiltInRegistries.ITEM.getKey(output.getItem()).getPath() + ".dupe." + type);
				recipes.add(new ShapedRecipe(id, faded ? fadedGroup : emissiveGroup, CraftingBookCategory.EQUIPMENT, 3, 3, inputs, output));
			}
		}

		return recipes;
	}
}