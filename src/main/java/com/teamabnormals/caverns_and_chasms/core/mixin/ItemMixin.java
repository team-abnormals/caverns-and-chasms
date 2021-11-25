package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(method = "getItemCategory", at = @At("RETURN"), cancellable = true)
	private void getItemCategory(CallbackInfoReturnable<CreativeModeTab> cir) {
		if ((Object) this == Items.BUNDLE) {
			cir.setReturnValue(CreativeModeTab.TAB_TOOLS);
		}
	}
}