package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MusicDiscCopying extends CustomRecipe {

	public MusicDiscCopying(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < input.size(); ++k) {
			ItemStack stack = input.getItem(k);
			if (!stack.isEmpty()) {
				if (stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
					++i;
				} else {
					if (!stack.is(CCItems.ZIRCONIA.get())) {
						return false;
					}

					++j;
				}

				if (j > 1 || i > 1) {
					return false;
				}
			}
		}

		return i == 1 && j == 1;
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider registries) {
		ItemStack returnStack = new ItemStack(CCItems.MUSIC_DISC_COPY.get());

		for (int i = 0; i < container.size(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
					returnStack.set(DataComponents.JUKEBOX_PLAYABLE, stack.get(DataComponents.JUKEBOX_PLAYABLE));
				}
			}
		}

		return returnStack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
		NonNullList<ItemStack> items = NonNullList.withSize(container.size(), ItemStack.EMPTY);

		for (int i = 0; i < items.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				items.set(i, item.getCraftingRemainingItem());
			} else if (item.has(DataComponents.JUKEBOX_PLAYABLE)) {
				items.set(i, item.copyWithCount(1));
			}
		}

		return items;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return x * y >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.MUSIC_DISC_COPYING.get();
	}
}
