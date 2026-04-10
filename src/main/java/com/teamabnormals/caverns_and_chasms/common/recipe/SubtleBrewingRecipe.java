package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

import java.util.Optional;

public class SubtleBrewingRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		return input.has(DataComponents.POTION_CONTENTS) && !input.has(CCDataComponents.SUBTLE);
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.is(CCItems.TURQUOISE.get());
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (input.isEmpty()) {
			return ItemStack.EMPTY;
		}

		Optional<Holder<Potion>> optional = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
		if (optional.isPresent()) {
			SubtlePotion subtle = new SubtlePotion(optional.get().value());
			ItemStack output = input.copy();
			output.set(DataComponents.POTION_CONTENTS, new PotionContents(Holder.direct(subtle)));
			output.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
			return output;
		}

		return ItemStack.EMPTY;
	}
}