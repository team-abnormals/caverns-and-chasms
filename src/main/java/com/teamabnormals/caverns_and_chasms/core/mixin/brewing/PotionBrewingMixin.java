package com.teamabnormals.caverns_and_chasms.core.mixin.brewing;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public abstract class PotionBrewingMixin {

	@Inject(at = @At("RETURN"), method = "mix", cancellable = true)
	private void getAllEffects(ItemStack potion, ItemStack potionItem, CallbackInfoReturnable<ItemStack> cir) {
		if (potionItem.has(CCDataComponents.SUBTLE)) {
			cir.setReturnValue(SubtlePotion.setSubtle(cir.getReturnValue()));
		}
	}
}