package com.minecraftabnormals.caverns_and_chasms.common.recipe;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MimingRecipe extends SingleItemRecipe {
	public MimingRecipe(ResourceLocation p_i50021_1_, String p_i50021_2_, Ingredient p_i50021_3_, ItemStack p_i50021_4_) {
		super(CCRecipes.RecipeTypes.MIMING, CCRecipes.Serailizers.MIMING.get(), p_i50021_1_, p_i50021_2_, p_i50021_3_, p_i50021_4_);
	}

	public boolean matches(Container inv, Level worldIn) {
		return this.ingredient.test(inv.getItem(0));
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(CCItems.SUGILITE.get());
	}
}