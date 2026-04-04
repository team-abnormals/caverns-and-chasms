package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;
import java.util.stream.Stream;

public class SmithingModifierRecipe implements SmithingRecipe {
	private final ResourceLocation id;
	public final Ingredient template;
	public final Ingredient base;
	public final Ingredient addition;

	public SmithingModifierRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition) {
		this.id = id;
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return this.template.test(container.getItem(0)) && this.base.test(container.getItem(1)) && this.addition.test(container.getItem(2));
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		ItemStack armor = container.getItem(1);
		ItemStack modifier = container.getItem(2);
		if (this.base.test(armor) && ArmorTrim.getTrim(registryAccess, armor).isPresent()) {
			ItemStack output = armor.copy();
			output.setCount(1);
			CompoundTag tag = armor.getOrCreateTag();
			if (modifier.is(CCItems.SPINEL.get()) && !tag.getBoolean("FadedTrim")) {
				output.getOrCreateTag().putBoolean("FadedTrim", true);
				return output;
			} else if (modifier.is(Items.GLOW_INK_SAC) && !tag.getBoolean("EmissiveTrim")) {
				output.getOrCreateTag().putBoolean("EmissiveTrim", true);
				return output;
			} else if (modifier.is(Items.PRISMARINE_SHARD) && !tag.getBoolean("PulseTrim")) {
				output.getOrCreateTag().putBoolean("PulseTrim", true);
				return output;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		ItemStack stack = new ItemStack(Items.IRON_CHESTPLATE);
		Optional<Holder.Reference<TrimPattern>> optional = registryAccess.registryOrThrow(Registries.TRIM_PATTERN).holders().findFirst();
		if (optional.isPresent()) {
			Optional<Holder.Reference<TrimMaterial>> optional1 = registryAccess.registryOrThrow(Registries.TRIM_MATERIAL).getHolder(TrimMaterials.REDSTONE);
			if (optional1.isPresent()) {
				ArmorTrim armortrim = new ArmorTrim(optional1.get(), optional.get());
				ArmorTrim.setTrim(registryAccess, stack, armortrim);
				stack.getOrCreateTag().putBoolean("EmissiveTrim", true);
			}
		}

		return stack;
	}

	@Override
	public boolean isTemplateIngredient(ItemStack stack) {
		return this.template.test(stack);
	}

	@Override
	public boolean isBaseIngredient(ItemStack stack) {
		return this.base.test(stack);
	}

	@Override
	public boolean isAdditionIngredient(ItemStack stack) {
		return this.addition.test(stack);
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.SMITHING_MODIFIER.get();
	}

	@Override
	public boolean isIncomplete() {
		return Stream.of(this.template, this.base, this.addition).anyMatch(ForgeHooks::hasNoElements);
	}

	public static class Serializer implements RecipeSerializer<SmithingModifierRecipe> {
		public SmithingModifierRecipe fromJson(ResourceLocation id, JsonObject json) {
			Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
			Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
			Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
			return new SmithingModifierRecipe(id, ingredient, ingredient1, ingredient2);
		}

		public SmithingModifierRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			Ingredient ingredient1 = Ingredient.fromNetwork(buf);
			Ingredient ingredient2 = Ingredient.fromNetwork(buf);
			return new SmithingModifierRecipe(id, ingredient, ingredient1, ingredient2);
		}

		public void toNetwork(FriendlyByteBuf buf, SmithingModifierRecipe recipe) {
			recipe.template.toNetwork(buf);
			recipe.base.toNetwork(buf);
			recipe.addition.toNetwork(buf);
		}
	}
}
