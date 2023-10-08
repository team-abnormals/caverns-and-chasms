package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CCLightningRodBlock extends LightningRodBlock {

	public CCLightningRodBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return this.isRandomlyTicking;
	}
}