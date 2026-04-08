package com.teamabnormals.caverns_and_chasms.core.mixin.recipe;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeMixin {

	@Shadow
	@Final
	Ingredient base;

	@Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;setTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/armortrim/ArmorTrim;)Z"), cancellable = true)
	private void assemble(SmithingRecipeInput input, HolderLookup.Provider registries, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = input.base();
		if (this.base.test(stack)) {
			ArmorTrim trim = stack.get(DataComponents.TRIM);
			if (trim != null && CCConfig.COMMON.preventReplacingTrims.get()) {
				cir.setReturnValue(ItemStack.EMPTY);
			}
		}
	}
}