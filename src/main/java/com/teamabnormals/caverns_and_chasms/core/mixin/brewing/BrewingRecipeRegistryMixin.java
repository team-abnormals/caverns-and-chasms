package com.teamabnormals.caverns_and_chasms.core.mixin.brewing;

import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
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
		if (stack.getItem() instanceof TetherPotionItem) {
			cir.setReturnValue(true);
		}
	}
}