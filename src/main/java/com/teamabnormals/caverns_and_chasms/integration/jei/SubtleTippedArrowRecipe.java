package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SubtleTippedArrowRecipe {

	public static List<RecipeHolder<CraftingRecipe>> createRecipes(IRecipeRegistration registration) {
		String group = "jei.tipped.arrow";
		ItemStack arrowStack = new ItemStack(Items.ARROW);
		Ingredient arrowIngredient = Ingredient.of(arrowStack);

		Stream<ItemStack> arrows = registration.getIngredientManager().getAllItemStacks().stream().filter(stack -> {
			return stack.getItem() instanceof TippedArrowItem && SubtlePotion.isSubtle(stack);
		});

		return arrows.<RecipeHolder<CraftingRecipe>>map(arrow -> {
			Holder<Potion> potion = arrow.get(DataComponents.POTION_CONTENTS).potion().get();

			ItemStack input = PotionContents.createItemStack(Items.LINGERING_POTION, potion);
			SubtlePotion.setSubtle(input);

			ItemStack output = PotionContents.createItemStack(Items.TIPPED_ARROW, potion);
			output.setCount(8);
			SubtlePotion.setSubtle(output);

			Ingredient potionIngredient = Ingredient.of(input);

			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, "jei.tipped.arrow." + output.getDescriptionId());
			return new RecipeHolder<>(id, new ShapedRecipe(group, CraftingBookCategory.MISC, ShapedRecipePattern.of(Map.of('#', arrowIngredient, 'X', potionIngredient), "###", "#X#", "###"), output));
		}).toList();
	}
}
