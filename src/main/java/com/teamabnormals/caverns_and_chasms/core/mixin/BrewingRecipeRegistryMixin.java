package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.other.CCPotionUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public final class BrewingRecipeRegistryMixin {

	@Inject(at = @At("HEAD"), method = "isValidInput", cancellable = true, remap = false)
	private static void isValidInput(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getCount() == 1 && stack.getItem() == CCItems.TETHER_POTION.get()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "hasOutput", cancellable = true, remap = false)
	private static void hasOutput(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		if (input.getItem() == CCItems.TETHER_POTION.get() && CCPotionUtil.isElegantPotion(input)) {
			cir.setReturnValue(false);
		}
	}
}