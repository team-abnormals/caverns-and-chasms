package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;
import java.util.stream.Stream;

public class SubtleTippedArrowRecipe {

	public static List<CraftingRecipe> createRecipes(IRecipeRegistration registration) {
		String group = "jei.tipped.arrow";
		ItemStack arrowStack = new ItemStack(Items.ARROW);
		Ingredient arrowIngredient = Ingredient.of(arrowStack);

		Stream<ItemStack> arrows = registration.getIngredientManager().getAllItemStacks().stream().filter(stack -> stack.getItem() instanceof TippedArrowItem && PotionUtils.getPotion(stack) instanceof SubtlePotion);

		return arrows.<CraftingRecipe>map(arrow -> {
					Potion potion = ((SubtlePotion) PotionUtils.getPotion(arrow)).getPotion();

					ItemStack input = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
					input.getOrCreateTag().putBoolean("Subtle", true);
					ItemStack output = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW, 8), potion);
					output.getOrCreateTag().putBoolean("Subtle", true);

					Ingredient potionIngredient = Ingredient.of(input);
					NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
							arrowIngredient, arrowIngredient, arrowIngredient,
							arrowIngredient, potionIngredient, arrowIngredient,
							arrowIngredient, arrowIngredient, arrowIngredient
					);

					ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, "jei.tipped.arrow." + output.getDescriptionId());
					return new ShapedRecipe(id, group, CraftingBookCategory.MISC, 3, 3, inputs, output);
				})
				.toList();
	}
}
