package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.recipe.SmithingModifierRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public class SmithingModifierCategoryExtension<R extends SmithingModifierRecipe> implements ISmithingCategoryExtension<R> {

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(R recipe, T ingredientAcceptor) {
		ingredientAcceptor.addIngredients(recipe.template);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(R recipe, T ingredientAcceptor) {
		ingredientAcceptor.addIngredients(recipe.base);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(R recipe, T ingredientAcceptor) {
		ingredientAcceptor.addIngredients(recipe.addition);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setOutput(R recipe, T ingredientAcceptor) {
		Ingredient templateIngredient = recipe.template;
		Ingredient baseIngredient = recipe.base;
		Ingredient additionIngredient = recipe.addition;

		ItemStack[] additions = additionIngredient.getItems();
		if (additions.length == 0) {
			return;
		}
		ItemStack addition = additions[0];

		for (ItemStack template : templateIngredient.getItems()) {
			for (ItemStack base : baseIngredient.getItems()) {
				Minecraft minecraft = Minecraft.getInstance();
				ClientLevel level = minecraft.level;
				if (level == null) {
					throw new NullPointerException("level must not be null.");
				}
				RegistryAccess registryAccess = level.registryAccess();

				Optional<Reference<TrimPattern>> pattern = registryAccess.registryOrThrow(Registries.TRIM_PATTERN).holders().findFirst();
				if (pattern.isPresent()) {
					Optional<Holder.Reference<TrimMaterial>> material = registryAccess.registryOrThrow(Registries.TRIM_MATERIAL).getHolder(TrimMaterials.REDSTONE);
					if (material.isPresent()) {
						ArmorTrim trim = new ArmorTrim(material.get(), pattern.get());
						ArmorTrim.setTrim(registryAccess, base, trim);
						Container recipeInput = createInput(template, base, addition);
						ItemStack output = recipe.assemble(recipeInput, registryAccess);
						ingredientAcceptor.addItemStack(output);
					}
				}
			}
		}
	}

	private static Container createInput(ItemStack template, ItemStack base, ItemStack addition) {
		Container container = new SimpleContainer(3);
		container.setItem(0, template);
		container.setItem(1, base);
		container.setItem(2, addition);
		return container;
	}
}