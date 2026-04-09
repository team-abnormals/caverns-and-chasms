package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class MimingRecipe extends SingleItemRecipe {
	public MimingRecipe(String group, Ingredient ingredient, ItemStack result) {
		super(CCRecipeTypes.MIMING.get(), CCRecipeSerializers.MIMING.get(), group, ingredient, result);
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return this.ingredient.test(input.item());
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(CCItems.MIME_HEAD.get());
	}
}