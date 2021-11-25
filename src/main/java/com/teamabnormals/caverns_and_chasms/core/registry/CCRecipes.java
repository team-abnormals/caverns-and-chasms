package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CCRecipes {

	public static class Serializers {
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CavernsAndChasms.MOD_ID);
		public static final RegistryObject<RecipeSerializer<MimingRecipe>> MIMING = RECIPE_SERIALIZERS.register("miming", () -> new SingleItemRecipe.Serializer<>(MimingRecipe::new) {
		});
	}

	public static class RecipeTypes {
		public static final RecipeType<MimingRecipe> MIMING = RecipeType.register(CavernsAndChasms.MOD_ID + ":miming");
	}
}
