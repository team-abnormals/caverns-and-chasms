package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ImpactPotionItem extends TetherPotionItem {

	public ImpactPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public MutableComponent toolTipHeader() {
		return Component.translatable("potion.whenSmashed");
	}

	@Override
	public boolean shortDuration() {
		return false;
	}
}
