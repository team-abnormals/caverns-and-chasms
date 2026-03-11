package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.material.FluidState;

public class RhyoliteFeature extends OreFeature {

	public RhyoliteFeature(Codec<OreConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		BlockPos origin = context.origin();
		WorldGenLevel level = context.level();
		if (!isNearLava(level, origin)) {
			return false;
		} else {
			RandomSource random = context.random();
			OreConfiguration config = context.config();

			float angle = random.nextFloat() * (float) Math.PI;

			float veinRadius = config.size / 7.0F;
			int extraSize = Mth.ceil((config.size / 16.0F * 2.0F + 1.0F) / 2.0F);

			double startX = origin.getX() + Math.sin(angle) * veinRadius;
			double endX = origin.getX() - Math.sin(angle) * veinRadius;

			double startZ = origin.getZ() + Math.cos(angle) * veinRadius;
			double endZ = origin.getZ() - Math.cos(angle) * veinRadius;

			double startY = origin.getY() + random.nextInt(2) - 2;
			double endY = origin.getY() + random.nextInt(2) - 2;

			int boxStartX = origin.getX() - Mth.ceil(veinRadius) - extraSize;
			int boxStartY = origin.getY() - 2 - extraSize;
			int boxStartZ = origin.getZ() - Mth.ceil(veinRadius) - extraSize;

			int boxWidth = 2 * (Mth.ceil(veinRadius) + extraSize);
			int boxHeight = 2 * (1 + extraSize);

			for (int x = boxStartX; x <= boxStartX + boxWidth; x++) {
				for (int z = boxStartZ; z <= boxStartZ + boxWidth; z++) {
					if (boxStartY <= level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z)) {
						return this.doPlace(level, random, config, startX, endX, startZ, endZ, startY, endY, boxStartX, boxStartY, boxStartZ, boxWidth, boxHeight);
					}
				}
			}

			return false;
		}
	}

	private static boolean isNearLava(WorldGenLevel level, BlockPos pos) {
		int lavaCount = 0;
		int airCount = 0;

		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, -2, -4), pos.offset(4, 2, 4))) {
			FluidState ifluidstate = level.getFluidState(blockpos);
			if (ifluidstate.is(FluidTags.LAVA) && ifluidstate.getAmount() == 8)
				lavaCount++;

			if (level.getBlockState(blockpos).isAir())
				airCount++;
		}
		return lavaCount > 10 && airCount > 10;
	}
}