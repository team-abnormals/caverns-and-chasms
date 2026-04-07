package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = {"net/minecraft/world/item/PotionItem", "net/minecraft/world/item/TippedArrowItem"})
public abstract class PotionItemMixin extends Item {

	public PotionItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		if (stack.getOrCreateTag().getBoolean("Subtle")) {
			return CCItems.FANCY;
		}
		return super.getRarity(stack);
	}

	@Override
	public Component getName(ItemStack stack) {
		Component component = super.getName(stack);
		if (stack.getOrCreateTag().getBoolean("Subtle")) {
			return Component.translatable("item.caverns_and_chasms.potion.subtle", component);
		}

		return component;
	}
}