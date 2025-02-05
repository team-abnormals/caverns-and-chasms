package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class MusicDiscCopyRecipe {
	private static final String group = "caverns_and_chasms.music_disc.copy";

	public static List<CraftingRecipe> createRecipes() {
		ArrayList<CraftingRecipe> recipes = Lists.newArrayList();
		for (ItemStack stack : Ingredient.of(ItemTags.MUSIC_DISCS).getItems()) {
			if (!stack.is(CCItems.MUSIC_DISC_COPY.get())) {
				recipes.add(createRecipes(stack.getItem()));
			}
		}
		return recipes;
	}

	public static CraftingRecipe createRecipes(Item record) {
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(record), Ingredient.of(CCItems.ZIRCONIA.get()));
		ItemStack output = new ItemStack(CCItems.MUSIC_DISC_COPY.get());
		output.getOrCreateTag().putString("music_disc", ForgeRegistries.ITEMS.getKey(record).toString());
		ResourceLocation id = new ResourceLocation(CavernsAndChasms.MOD_ID, group + "." + output.getDescriptionId());
		return new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
	}
}