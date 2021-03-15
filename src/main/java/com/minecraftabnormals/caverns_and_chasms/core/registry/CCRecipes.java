package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CCRecipes {

	public static class Serailizers {
		public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CavernsAndChasms.MOD_ID);
		public static final RegistryObject<IRecipeSerializer<MimingRecipe>> MIMING = RECIPE_SERIALIZERS.register("miming", () -> new SingleItemRecipe.Serializer<MimingRecipe>(MimingRecipe::new) {});
	}

	public static class RecipeTypes {
		public static final IRecipeType<MimingRecipe> MIMING = IRecipeType.register(CavernsAndChasms.MOD_ID + ":miming");
	}
}
