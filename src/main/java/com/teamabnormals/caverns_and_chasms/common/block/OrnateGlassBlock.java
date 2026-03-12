package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OrnateGlassBlock extends GlassBlock implements RainbowBeaconBeamBlock {

	public OrnateGlassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader reader, BlockPos pos, BlockPos beaconPos) {
		return RainbowBeaconBeamBlock.getColor(reader);
	}
}
