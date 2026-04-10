package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public final class MusicDiscCopyRecipe {
	private static final String group = "caverns_and_chasms.music_disc.copy";

	public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
		ArrayList<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();
		for (ItemStack stack : Ingredient.of(Tags.Items.MUSIC_DISCS).getItems()) {
			if (!stack.is(CCItems.MUSIC_DISC_COPY.get())) {
				recipes.add(createRecipes(stack));
			}
		}
		return recipes;
	}

	public static RecipeHolder<CraftingRecipe> createRecipes(ItemStack record) {
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(record), Ingredient.of(CCItems.ZIRCONIA.get()));
		ItemStack output = new ItemStack(CCItems.MUSIC_DISC_COPY.get());
		output.set(DataComponents.JUKEBOX_PLAYABLE, record.get(DataComponents.JUKEBOX_PLAYABLE));
		ResourceLocation id = CavernsAndChasms.location(group + "." + output.getDescriptionId());
		return new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, output, inputs));
	}
}