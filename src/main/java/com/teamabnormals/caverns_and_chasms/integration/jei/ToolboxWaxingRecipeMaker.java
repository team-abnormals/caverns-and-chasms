package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.WeatheringToolboxBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.Arrays;
import java.util.List;

public final class ToolboxWaxingRecipeMaker {
	private static final String group = "caverns_and_chasms.toolbox.wax";

	public static List<CraftingRecipe> createRecipes() {
		return Arrays.asList(
				createRecipe((WeatheringToolboxBlock) CCBlocks.TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.EXPOSED_TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.WEATHERED_TOOLBOX.get()),
				createRecipe((WeatheringToolboxBlock) CCBlocks.OXIDIZED_TOOLBOX.get())
		);
	}

	private static CraftingRecipe createRecipe(WeatheringToolboxBlock toolbox) {
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(toolbox), Ingredient.of(Items.HONEYCOMB));
		ItemStack output = ToolboxBlock.getWeatheredItemStack(toolbox.getWeatherState(), false);
		ResourceLocation id = new ResourceLocation(CavernsAndChasms.MOD_ID, group + "." + output.getDescriptionId());
		return new ShapelessRecipe(id, group, CraftingBookCategory.EQUIPMENT, output, inputs);
	}
}