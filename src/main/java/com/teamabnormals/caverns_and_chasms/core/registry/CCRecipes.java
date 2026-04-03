package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.common.recipe.MusicDiscCopying;
import com.teamabnormals.caverns_and_chasms.common.recipe.NBTWaxing;
import com.teamabnormals.caverns_and_chasms.common.recipe.SmithingModifierRecipe;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCRecipes {

	public static class CCRecipeSerializers {
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CavernsAndChasms.MOD_ID);

		public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MimingRecipe>> MIMING = RECIPE_SERIALIZERS.register("miming", () -> new SingleItemRecipe.Serializer<>(MimingRecipe::new) {
		});

		public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<NBTWaxing>> NBT_WAXING = RECIPE_SERIALIZERS.register("crafting_special_nbtwaxing", () -> new SimpleCraftingRecipeSerializer<>(NBTWaxing::new));
		public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<MusicDiscCopying>> MUSIC_DISC_COPYING = RECIPE_SERIALIZERS.register("crafting_special_musicdisccopying", () -> new SimpleCraftingRecipeSerializer<>(MusicDiscCopying::new));
		public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmithingModifierRecipe>> SMITHING_MODIFIER = RECIPE_SERIALIZERS.register("smithing_modifier", SmithingModifierRecipe.Serializer::new);
	}

	public static class CCRecipeTypes {
		public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CavernsAndChasms.MOD_ID);

		public static final DeferredHolder<RecipeType<?>, RecipeType<MimingRecipe>> MIMING = RECIPE_TYPES.register("miming", () -> registerRecipeType("miming"));

		public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String name) {
			return new RecipeType<>() {
				public String toString() {
					return CavernsAndChasms.MOD_ID + ":" + name;
				}
			};
		}
	}
}
