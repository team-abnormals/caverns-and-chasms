package com.teamabnormals.caverns_and_chasms.core.mixin.recipe;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowRecipe.class)
public abstract class TippedArrowRecipeMixin {

	@Inject(at = @At("RETURN"), method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
	private void assemble(CraftingInput input, Provider registries, CallbackInfoReturnable<ItemStack> cir) {
		if (!cir.getReturnValue().isEmpty()) {
			ItemStack potion = input.getItem(1, 1);
			if (potion.has(CCDataComponents.SUBTLE) && potion.get(DataComponents.POTION_CONTENTS).potion().get().value() instanceof SubtlePotion subtlePotion) {
				ItemStack returnStack = cir.getReturnValue();
				returnStack.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
				returnStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Holder.direct(subtlePotion)));
			}
		}
	}
}