package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BouncerBlock extends TargetBlock {

	public BouncerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public float getJumpFactor() {
		return 1.75F;
	}
}