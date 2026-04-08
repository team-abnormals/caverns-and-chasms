package com.teamabnormals.caverns_and_chasms.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.stream.Stream;

public class SmithingModifierRecipe implements SmithingRecipe {
	public final Ingredient template;
	public final Ingredient base;
	public final Ingredient addition;

	public SmithingModifierRecipe(Ingredient template, Ingredient base, Ingredient addition) {
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	@Override
	public boolean matches(SmithingRecipeInput container, Level level) {
		return this.template.test(container.getItem(0)) && this.base.test(container.getItem(1)) && this.addition.test(container.getItem(2));
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
		ItemStack armor = input.base();
		ItemStack modifier = input.addition();
		ArmorTrim trim = armor.get(DataComponents.TRIM);
		if (this.base.test(armor) && trim != null) {
			ItemStack output = armor.copy();
			output.setCount(1);
			if (modifier.is(CCItems.SPINEL.get()) && !armor.has(CCDataComponents.FADED_TRIM)) {
				output.set(CCDataComponents.FADED_TRIM, Unit.INSTANCE);
				return output;
			} else if (modifier.is(Items.GLOW_INK_SAC) && !armor.has(CCDataComponents.EMISSIVE_TRIM)) {
				output.set(CCDataComponents.EMISSIVE_TRIM, Unit.INSTANCE);
				return output;
			} else if (modifier.is(Items.PRISMARINE_SHARD) && !armor.has(CCDataComponents.PULSE_TRIM)) {
				output.set(CCDataComponents.PULSE_TRIM, Unit.INSTANCE);
				return output;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack stack = new ItemStack(Items.IRON_CHESTPLATE);
		Optional<Holder.Reference<TrimPattern>> optional = registries.lookupOrThrow(Registries.TRIM_PATTERN).listElements().findFirst();
		if (optional.isPresent()) {
			Optional<Holder.Reference<TrimMaterial>> optional1 = registries.lookupOrThrow(Registries.TRIM_MATERIAL).get(TrimMaterials.REDSTONE);
			if (optional1.isPresent()) {
				stack.set(DataComponents.TRIM, new ArmorTrim(optional1.get(), optional.get()));
				stack.set(CCDataComponents.EMISSIVE_TRIM, Unit.INSTANCE);
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
	public RecipeSerializer<?> getSerializer() {
		return CCRecipeSerializers.SMITHING_MODIFIER.get();
	}

	@Override
	public boolean isIncomplete() {
		return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
	}

	public static class Serializer implements RecipeSerializer<SmithingModifierRecipe> {
		private static final MapCodec<SmithingModifierRecipe> CODEC = RecordCodecBuilder.mapCodec(p_301227_ -> p_301227_.group(
						Ingredient.CODEC.fieldOf("template").forGetter(p_301070_ -> p_301070_.template),
						Ingredient.CODEC.fieldOf("base").forGetter(p_300969_ -> p_300969_.base),
						Ingredient.CODEC.fieldOf("addition").forGetter(p_300977_ -> p_300977_.addition)
				).apply(p_301227_, SmithingModifierRecipe::new)
		);

		public static final StreamCodec<RegistryFriendlyByteBuf, SmithingModifierRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<SmithingModifierRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SmithingModifierRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static SmithingModifierRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			return new SmithingModifierRecipe(ingredient, ingredient1, ingredient2);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, SmithingModifierRecipe recipe) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition);
		}
	}
}
