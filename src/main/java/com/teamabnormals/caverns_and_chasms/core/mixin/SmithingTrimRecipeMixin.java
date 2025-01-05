package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeMixin {

	@Inject(method = "assemble", at = @At(value = "RETURN", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;setTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/armortrim/ArmorTrim;)Z"))
	private void assemble(Container container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
		if (!cir.getReturnValue().isEmpty()) {
			CompoundTag tag = cir.getReturnValue().getOrCreateTag();
			if (container.getItem(0).getOrCreateTag().getBoolean("EmissiveTrim")) {
				tag.putBoolean("EmissiveTrim", true);
				tag.putBoolean("FadedTrim", false);
			} else if (container.getItem(0).getOrCreateTag().getBoolean("FadedTrim")) {
				tag.putBoolean("FadedTrim", true);
				tag.putBoolean("EmissiveTrim", false);
			}
		}
	}
}