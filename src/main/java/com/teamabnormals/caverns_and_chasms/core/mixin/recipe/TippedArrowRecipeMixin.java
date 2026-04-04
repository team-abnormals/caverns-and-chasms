package com.teamabnormals.caverns_and_chasms.core.mixin.recipe;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowRecipe.class)
public abstract class TippedArrowRecipeMixin {

	@Inject(at = @At("RETURN"), method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
	private void assemble(CraftingContainer container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
		if (!cir.getReturnValue().isEmpty()) {
			ItemStack potion = container.getItem(1 + container.getWidth());
			if (potion.getOrCreateTag().getBoolean("Subtle") && PotionUtils.getPotion(potion) instanceof SubtlePotion subtlePotion) {
				ItemStack returnStack = cir.getReturnValue();
				returnStack.getOrCreateTag().putBoolean("Subtle", true);
				PotionUtils.setPotion(returnStack, subtlePotion.getPotion());
			}
		}
	}
}