package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class ImpactPotionItem extends TetherPotionItem {

	public ImpactPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public MutableComponent toolTipHeader() {
		return Component.translatable("potion.whenSmashed");
	}


	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
		if (contents != null) {
			this.addPotionTooltip(contents.getAllEffects(), tooltip::add, 0.25F, context.tickRate());
		}
	}
}
