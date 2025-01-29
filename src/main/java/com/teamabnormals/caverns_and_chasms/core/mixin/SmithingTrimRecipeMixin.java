package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeMixin {

	@Shadow
	@Final
	Ingredient base;

	@Inject(method = "assemble", at = @At(value = "RETURN", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;setTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/armortrim/ArmorTrim;)Z"), cancellable = true)
	private void assemble(Container container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
		if (this.base.test(container.getItem(1))) {
			Optional<Holder.Reference<TrimPattern>> pattern = TrimPatterns.getFromTemplate(access, container.getItem(0));
			if (pattern.isPresent() && CCConfig.COMMON.preventReplacingTrims.get()) {
				cir.setReturnValue(ItemStack.EMPTY);
			}
		}

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