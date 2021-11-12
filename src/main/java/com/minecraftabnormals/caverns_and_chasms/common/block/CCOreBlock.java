package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.world.level.block.OreBlock;
import net.minecraft.util.Mth;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CCOreBlock extends OreBlock {
	private int minXP;
	private final int maxXP;

	public CCOreBlock(int min, int max, Properties properties) {
		super(properties);
		minXP = min;
		maxXP = max;
	}

	protected int xpOnDrop(Random rand) {
		return Mth.nextInt(rand, minXP, maxXP);
	}
}
