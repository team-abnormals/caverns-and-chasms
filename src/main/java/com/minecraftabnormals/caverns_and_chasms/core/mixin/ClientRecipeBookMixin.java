package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCRecipes;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

	@Inject(at = @At("HEAD"), method = "getCategory", cancellable = true)
	private static void getCategory(IRecipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
		if (recipe.getType() == CCRecipes.RecipeTypes.MIMING) {
			cir.setReturnValue(RecipeBookCategories.UNKNOWN);
		}
	}
}