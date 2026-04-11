package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SubtlePotionRecipe {

	public static List<IJeiBrewingRecipe> createRecipes(IRecipeRegistration registration) {
		ArrayList<IJeiBrewingRecipe> recipes = new ArrayList<>();

		Stream<ItemStack> inputs = registration.getIngredientManager().getAllItemStacks().stream().filter(stack -> {
			return stack.getItem() instanceof PotionItem && stack.has(DataComponents.POTION_CONTENTS) && !stack.has(CCDataComponents.SUBTLE);
		});

		inputs.forEach(stack -> {
			ItemStack output = SubtlePotion.setSubtle(stack.copy());
			recipes.add(registration.getVanillaRecipeFactory().createBrewingRecipe(
					List.of(new ItemStack(CCItems.TURQUOISE.get())), stack, output, CavernsAndChasms.location("jei.subtle_potion." + output.getDescriptionId())
			));
		});

		return recipes;
	}
}
