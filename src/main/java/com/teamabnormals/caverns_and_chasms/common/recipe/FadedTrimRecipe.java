package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FadedTrimRecipe extends CustomRecipe {
	public FadedTrimRecipe(ResourceLocation p_273671_, CraftingBookCategory p_273056_) {
		super(p_273671_, p_273056_);
	}

	public boolean matches(CraftingContainer container, Level level) {
		if (!this.canCraftInDimensions(container.getWidth(), container.getHeight())) {
			return false;
		} else {

			for (int i = 0; i < container.getContainerSize(); ++i) {
				ItemStack stack = container.getItem(i);

				switch (i) {
					case 0:
					case 2:
					case 3:
					case 5:
					case 6:
					case 7:
					case 8:
						if (!stack.is(CCItems.SPINEL.get())) {
							return false;
						}
						break;
					case 1:
						CompoundTag tag = stack.getOrCreateTag();
						if (!stack.is(ItemTags.TRIM_TEMPLATES) || tag.getBoolean("FadedTrim") || tag.getBoolean("EmissiveTrim")) {
							return false;
						}
						break;
					default:
				}

			}

			CraftingContainer checker = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
				public ItemStack quickMoveStack(Player p_218264_, int p_218265_) {
					return ItemStack.EMPTY;
				}

				public boolean stillValid(Player p_29888_) {
					return false;
				}
			}, 3, 3);

			for (int i = 0; i < container.getContainerSize(); ++i) {
				if (container.getItem(i).is(CCItems.SPINEL.get())) {
					checker.setItem(i, Items.DIAMOND.getDefaultInstance());
				} else {
					checker.setItem(i, container.getItem(i));
				}
			}

			return !level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, checker, level).isEmpty();
		}

	}

	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		ItemStack stack = new ItemStack(container.getItem(1).getItem());
		stack.getOrCreateTag().putBoolean("FadedTrim", true);
		return stack;
	}

	public boolean canCraftInDimensions(int x, int y) {
		return x == 3 && y == 3;
	}

	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.FADED_TRIM_DUPING.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < items.size(); ++i) {
			ItemStack item = container.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				items.set(i, item.getCraftingRemainingItem());
			} else if (item.is(ItemTags.TRIM_TEMPLATES)) {
				items.set(i, item.copyWithCount(1));
			}
		}

		return items;
	}
}