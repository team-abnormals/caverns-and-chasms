package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class MusicDiscCopying extends CustomRecipe {

	public MusicDiscCopying(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < container.getContainerSize(); ++k) {
			ItemStack stack = container.getItem(k);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof RecordItem) {
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
	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		ItemStack returnStack = new ItemStack(CCItems.MUSIC_DISC_COPY.get());

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof RecordItem) {
					returnStack.getOrCreateTag().putString("music_disc", ForgeRegistries.ITEMS.getKey(item).toString());
				}
			}
		}

		return returnStack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < items.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				items.set(i, item.getCraftingRemainingItem());
			} else if (item.is(ItemTags.MUSIC_DISCS)) {
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
