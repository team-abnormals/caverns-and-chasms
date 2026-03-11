package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.teamabnormals.caverns_and_chasms.common.block.weathering.WeatheringToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class NBTWaxing extends CustomRecipe {

	public NBTWaxing(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < container.getContainerSize(); ++k) {
			ItemStack stack = container.getItem(k);
			if (!stack.isEmpty()) {
				if (Block.byItem(stack.getItem()) instanceof WeatheringToolboxBlock || stack.getItem() instanceof WeatheringCopperItem) {
					++i;
				} else {
					if (!(stack.is(CCItemTags.WAX))) {
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
				Item item = stack.getItem();
				if (Block.byItem(item) instanceof WeatheringToolboxBlock || item instanceof WeatheringCopperItem) {
					returnStack = stack;
					break;
				}
			}
		}

		ItemStack waxedStack;
		if (Block.byItem(returnStack.getItem()) instanceof WeatheringToolboxBlock) {
			waxedStack = new ItemStack(HoneycombItem.getWaxed(Block.byItem(returnStack.getItem()).defaultBlockState()).get().getBlock().asItem());
		} else {
			waxedStack = WeatheringCopperItem.getWaxed(returnStack).get();
		}

		if (returnStack.hasTag()) {
			waxedStack.setTag(returnStack.getTag().copy());
		}

		return waxedStack;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return x * y >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.NBT_WAXING.get();
	}
}
