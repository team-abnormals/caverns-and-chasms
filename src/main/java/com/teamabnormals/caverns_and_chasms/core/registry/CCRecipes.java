package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.*;

public class CCRecipes {

	public static class CCRecipeSerializers {
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CavernsAndChasms.MOD_ID);
		public static final RegistryObject<RecipeSerializer<MimingRecipe>> MIMING = RECIPE_SERIALIZERS.register("miming", () -> new SingleItemRecipe.Serializer<>(MimingRecipe::new) {
		});
	}

	public static class CCRecipeTypes {
		public static final RecipeType<MimingRecipe> MIMING = RecipeType.register(CavernsAndChasms.MOD_ID + ":miming");
	}
}
