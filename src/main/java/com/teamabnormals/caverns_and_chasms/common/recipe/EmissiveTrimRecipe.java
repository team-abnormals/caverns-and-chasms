package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class EmissiveTrimRecipe extends FadedTrimRecipe {

	public EmissiveTrimRecipe(ResourceLocation p_273671_, CraftingBookCategory p_273056_) {
		super(p_273671_, p_273056_, false);
	}

	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.EMISSIVE_TRIM_DUPING.get();
	}
}