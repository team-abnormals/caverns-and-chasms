package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.other.CCEnums;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionContents.class)
public abstract class PotionContentsMixin {

	@Inject(method = "createItemStack", at = @At("RETURN"), cancellable = true)
	private static void createItemStack(Item item, Holder<Potion> potion, CallbackInfoReturnable<ItemStack> cir) {
		if (SubtlePotion.isSubtle(cir.getReturnValue())) {
			ItemStack stack = cir.getReturnValue();
			stack.set(DataComponents.RARITY, CCEnums.FANCY.getValue());
			cir.setReturnValue(stack);
		}
	}
}