package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionBrewing.Mix;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionBrewing.class)
public abstract class PotionBrewingMixin {

	@Shadow
	@Final
	private static List<Mix<Item>> CONTAINER_MIXES;

	@Inject(at = @At("RETURN"), method = "mix", cancellable = true)
	private static void isValidRepairItem(ItemStack ingredient, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (!stack.isEmpty()) {
			Potion potion = PotionUtils.getPotion(stack);
			Item item = stack.getItem();
			int i = 0;
			for (int j = CONTAINER_MIXES.size(); i < j; ++i) {
				PotionBrewing.Mix<Item> mix = CONTAINER_MIXES.get(i);
				if (mix.from.get() == item && mix.ingredient.test(ingredient) && ingredient.is(Items.PRISMARINE_CRYSTALS)) {
					if (!stack.getOrCreateTag().getBoolean("Subtle")) {

						ItemStack newStack = new ItemStack(mix.to.get());
						newStack.getOrCreateTag().putBoolean("Subtle", true);
						cir.setReturnValue(PotionUtils.setPotion(newStack, potion));
					} else {
						cir.setReturnValue(stack);
					}
				}
			}
		}
	}
}