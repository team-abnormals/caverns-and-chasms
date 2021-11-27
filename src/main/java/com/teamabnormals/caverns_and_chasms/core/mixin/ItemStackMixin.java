package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.SpinelCrownItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract void enchant(Enchantment enchantment, int level);

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/nbt/CompoundTag;)V")
	private void init(ItemLike item, int count, CompoundTag tag, CallbackInfo ci) {
		if (item instanceof SpinelCrownItem) {
			this.enchant(Enchantments.BINDING_CURSE, 1);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"), method = "hurt", cancellable = true)
	private void hurt(int amount, Random random, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
		if (this.getItem() instanceof SpinelCrownItem) {
			player.curePotionEffects(new ItemStack(CCItems.SPINEL_CROWN.get()));
		}
	}
}