package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.WeatheringToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.Arrays;
import java.util.List;

public final class NBTWaxingRecipeMaker {

	public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();
		WeatheringCopperItem.WAXABLES.get().forEach((base, waxed) -> {
			if (base instanceof WeatheringCopperItem) {
				NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(base), Ingredient.of(CCItemTags.WAX));
				ItemStack output = new ItemStack(waxed);
				ResourceLocation id = CavernsAndChasms.location(group + "." + output.getDescriptionId());
				String name = base.builtInRegistryHolder().key().location().getPath().replace("exposed_|weathered_|oxidized_", "");
				recipes.add(new RecipeHolder<>(id, new ShapelessRecipe("caverns_and_chasms." + name + ".wax", CraftingBookCategory.EQUIPMENT, output, inputs)));
			}
		});

		recipes.addAll(Arrays.asList(
				createRecipe((WeatheringToolboxBlock) CCBlocks.TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.EXPOSED_TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.WEATHERED_TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.OXIDIZED_TOOLBOX.get())
		));

		return recipes;
	}

	private static final String group = "caverns_and_chasms.toolbox.wax";

	private static RecipeHolder<CraftingRecipe> createRecipe(WeatheringToolboxBlock toolbox) {
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(toolbox), Ingredient.of(CCItemTags.WAX));
		ItemStack output = ToolboxBlock.getWeatheredItemStack(toolbox.getWeatherState(), false);
		ResourceLocation id = CavernsAndChasms.location(group + "." + output.getDescriptionId());
		return new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.EQUIPMENT, output, inputs));
	}
}