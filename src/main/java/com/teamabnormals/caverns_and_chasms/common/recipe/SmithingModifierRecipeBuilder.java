package com.teamabnormals.caverns_and_chasms.common.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.Map;

public class SmithingModifierRecipeBuilder {
	private final RecipeCategory category;
	private final Ingredient template;
	private final Ingredient base;
	private final Ingredient addition;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	public SmithingModifierRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition) {
		this.category = category;
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	public static SmithingModifierRecipeBuilder smithingModifier(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category) {
		return new SmithingModifierRecipeBuilder(category, template, base, addition);
	}

	public SmithingModifierRecipeBuilder unlocks(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return this;
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
		this.ensureValid(recipeId);
		Advancement.Builder builder = recipeOutput.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(builder::addCriterion);
		SmithingModifierRecipe recipe = new SmithingModifierRecipe(this.template, this.base, this.addition);
		recipeOutput.accept(recipeId, recipe, builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation location) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}