package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BouncerBlock extends TargetBlock {

	public BouncerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public float getJumpFactor() {
		return 1.75F;
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float distance) {
		entity.causeFallDamage(distance * (4.0F / 7.0F), 1.0F, level.damageSources().fall());
	}
}