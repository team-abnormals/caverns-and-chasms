package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
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
			Optional<ArmorTrim> pattern = ArmorTrim.getTrim(access, container.getItem(1));
			if (pattern.isPresent() && CCConfig.COMMON.preventReplacingTrims.get()) {
				cir.setReturnValue(ItemStack.EMPTY);
			}
		}

		if (!cir.getReturnValue().isEmpty()) {
			CompoundTag tag = cir.getReturnValue().getOrCreateTag();
			CompoundTag inputTag = container.getItem(0).getOrCreateTag();
			tag.putBoolean("EmissiveTrim", inputTag.getBoolean("EmissiveTrim"));
			tag.putBoolean("FadedTrim", inputTag.getBoolean("FadedTrim"));
		}
	}
}