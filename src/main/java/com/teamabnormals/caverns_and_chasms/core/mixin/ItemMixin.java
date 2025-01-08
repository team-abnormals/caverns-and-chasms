package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
	private void isValidRepairItem(ItemStack item, ItemStack repairIngredient, CallbackInfoReturnable<Boolean> cir) {
		if (repairIngredient.is(CCItems.ZIRCONIA.get())) {
			cir.setReturnValue(true);
		}
	}
}