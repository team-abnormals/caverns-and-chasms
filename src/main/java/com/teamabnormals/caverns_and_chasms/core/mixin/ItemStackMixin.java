package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.SpinelCrownItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"), method = "hurt", cancellable = true)
	private void hurt(int amount, Random random, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
		if (this.getItem() instanceof SpinelCrownItem) {
			player.removeEffect(CCEffects.REWIND.get());
		}
	}
}