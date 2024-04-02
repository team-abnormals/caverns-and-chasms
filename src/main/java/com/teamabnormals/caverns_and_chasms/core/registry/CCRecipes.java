package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.common.recipe.ToolboxWaxing;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CCRecipes {

	public static class CCRecipeSerializers {
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<RecipeSerializer<MimingRecipe>> MIMING = RECIPE_SERIALIZERS.register("miming", () -> new SingleItemRecipe.Serializer<>(MimingRecipe::new) {
		});

		public static final RegistryObject<SimpleCraftingRecipeSerializer<ToolboxWaxing>> TOOLBOX_WAXING = RECIPE_SERIALIZERS.register("crafting_special_toolboxwaxing", () -> new SimpleCraftingRecipeSerializer<>(ToolboxWaxing::new));
	}

	public static class CCRecipeTypes {
		public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<RecipeType<MimingRecipe>> MIMING = RECIPE_TYPES.register("miming", () -> registerRecipeType("miming"));

		public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String name) {
			return new RecipeType<>() {
				public String toString() {
					return CavernsAndChasms.MOD_ID + ":" + name;
				}
			};
		}
	}
}
