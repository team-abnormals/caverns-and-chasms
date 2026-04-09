package com.teamabnormals.caverns_and_chasms.core.mixin.brewing;

import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionBrewing.Mix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionBrewing.class)
public abstract class PotionBrewingMixin {

	@Shadow @Final private List<Mix<Item>> containerMixes;

	@Shadow @Final private List<Mix<Potion>> potionMixes;

	@Inject(at = @At("RETURN"), method = "mix", cancellable = true)
	private void mix(ItemStack ingredient, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (!stack.isEmpty()) {
			Potion potion = PotionUtils.getPotion(stack);
			if (potion instanceof SubtlePotion subtlePotion) {
				potion = subtlePotion.getPotion();
			}

			Item item = stack.getItem();
			int i = 0;
			for (int j = containerMixes.size(); i < j; ++i) {
				PotionBrewing.Mix<Item> mix = containerMixes.get(i);
				if (mix.from.get() == item && mix.ingredient.test(ingredient)) {
					if (ingredient.is(CCItems.TURQUOISE.get())) {
						if (!stack.has(CCDataComponents.SUBTLE)) {
							ItemStack newStack = new ItemStack(mix.to.get());
							newStack.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
							cir.setReturnValue(PotionUtils.setPotion(newStack, potion));
						} else {
							cir.setReturnValue(stack);
						}
					} else if (stack.has(CCDataComponents.SUBTLE)) {
						ItemStack newStack = new ItemStack(mix.to.get());
						newStack.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
						cir.setReturnValue(PotionUtils.setPotion(newStack, potion));
					}

				}
			}

			i = 0;

			for (int k = potionMixes.size(); i < k; ++i) {
				PotionBrewing.Mix<Potion> mix = potionMixes.get(i);
				if (mix.from.get() == potion && mix.ingredient.test(ingredient) && stack.has(CCDataComponents.SUBTLE)) {
					ItemStack newStack = new ItemStack(item);
					newStack.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
					cir.setReturnValue(PotionUtils.setPotion(newStack, mix.to.get()));
				}
			}
		}
	}
}