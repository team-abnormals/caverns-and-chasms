package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class SubtleBrewingRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		return input.getItem() instanceof PotionItem && input.has(DataComponents.POTION_CONTENTS) && !input.has(CCDataComponents.SUBTLE);
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.is(CCItems.TURQUOISE.get());
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return SubtlePotion.setSubtle(input.copy());
		}
		return ItemStack.EMPTY;
	}
}