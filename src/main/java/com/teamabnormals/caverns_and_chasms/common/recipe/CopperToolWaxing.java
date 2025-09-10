package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class CopperToolWaxing extends CustomRecipe {

	public CopperToolWaxing(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < container.getContainerSize(); ++k) {
			ItemStack stack = container.getItem(k);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof WeatheringCopperItem && !stack.getOrCreateTag().getBoolean("waxed")) {
					++i;
				} else {
					if (!stack.is(Items.HONEYCOMB)) {
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
		ItemStack returnStack = ItemStack.EMPTY;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof WeatheringCopperItem) {
					returnStack = stack.copy();
				}
			}
		}

		returnStack.getOrCreateTag().putBoolean("waxed", true);
		return returnStack;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return x * y >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.COPPER_TOOL_WAXING.get();
	}
}
