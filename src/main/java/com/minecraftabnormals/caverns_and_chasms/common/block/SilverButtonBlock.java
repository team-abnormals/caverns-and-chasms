package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class SilverButtonBlock extends AbstractButtonBlock {

	public SilverButtonBlock(AbstractBlock.Properties properties) {
		super(false, properties);
	}

	@Override
	public int getPressDuration() {
		return 50;
	}

	@Override
	protected SoundEvent getSound(boolean powered) {
		return powered ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
	}
}