package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(TieredItem.class)
public abstract class TieredItemMixin extends Item {

	@Shadow
	Tier getTier() {
		return null;
	}

	public TieredItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (this.getTier() == Tiers.GOLD)
			tooltip.add(new TranslatableComponent("tooltip.caverns_and_chasms.boosting").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
