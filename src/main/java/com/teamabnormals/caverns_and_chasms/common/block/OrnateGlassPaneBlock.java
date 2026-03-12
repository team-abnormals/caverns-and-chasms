package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OrnateGlassPaneBlock extends IronBarsBlock implements RainbowBeaconBeamBlock {

	public OrnateGlassPaneBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader reader, BlockPos pos, BlockPos beaconPos) {
		return RainbowBeaconBeamBlock.getColor(reader);
	}
}
