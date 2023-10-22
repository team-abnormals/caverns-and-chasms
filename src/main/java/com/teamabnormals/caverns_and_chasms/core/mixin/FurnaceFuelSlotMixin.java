package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceFuelSlot.class)
public final class FurnaceFuelSlotMixin {

	@Inject(at = @At("RETURN"), method = "isBucket", cancellable = true)
	private static void isBucket(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(CCItems.GOLDEN_BUCKET.get()) || stack.is(CCItems.GOLDEN_WATER_BUCKET.get()) && GoldenBucketItem.canBeFilled(stack)) {
			cir.setReturnValue(true);
		}
	}
}