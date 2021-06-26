package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class CCOreBlock extends OreBlock {
	private int minXP;
	private final int maxXP;

	public CCOreBlock(int min, int max, Properties properties) {
		super(properties);
		minXP = min;
		maxXP = max;
	}

	protected int xpOnDrop(Random rand) {
		return MathHelper.nextInt(rand, minXP, maxXP);
	}
}
