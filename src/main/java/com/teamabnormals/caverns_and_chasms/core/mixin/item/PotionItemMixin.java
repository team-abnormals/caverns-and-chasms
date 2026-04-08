package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = {"net/minecraft/world/item/PotionItem", "net/minecraft/world/item/TippedArrowItem"})
public abstract class PotionItemMixin extends Item {

	public PotionItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public Component getName(ItemStack stack) {
		Component component = super.getName(stack);
		if (stack.has(CCDataComponents.SUBTLE)) {
			return Component.translatable("item.caverns_and_chasms.potion.subtle").append(" ").append(component);
		}

		return component;
	}
}