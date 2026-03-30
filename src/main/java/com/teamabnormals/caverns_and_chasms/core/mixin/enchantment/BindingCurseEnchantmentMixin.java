package com.teamabnormals.caverns_and_chasms.core.mixin.enchantment;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.BindingCurseEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BindingCurseEnchantment.class)
public abstract class BindingCurseEnchantmentMixin {

	@Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
	private void overrideOtherStackedOnMe(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(CCItems.AEGIS.get())) {
			cir.setReturnValue(false);
		}
	}
}
