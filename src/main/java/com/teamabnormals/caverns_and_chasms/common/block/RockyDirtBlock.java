package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class RockyDirtBlock extends Block {

	public RockyDirtBlock(Properties properties) {
		super(properties);
	}

	@Override
	public SoundType getSoundType(BlockState state) {
		return CCSoundTypes.ROCKY_DIRT;
	}	
}