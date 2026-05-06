package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class SchistFeature extends OreFeature {
	public SchistFeature(Codec<OreConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		BlockPos origin = context.origin();
		WorldGenLevel level = context.level();
		RandomSource random = context.random();
		OreConfiguration config = context.config();

		float angle = random.nextFloat() * (float) Math.PI;

		float veinRadius = config.size / 5.0F;
		int extraSize = Mth.ceil((config.size / 16.0F * 2.0F + 1.0F) / 2.0F);

		double startX = origin.getX() + Math.sin(angle) * veinRadius;
		double endX = origin.getX() - Math.sin(angle) * veinRadius;

		double startZ = origin.getZ() + Math.cos(angle) * veinRadius;
		double endZ = origin.getZ() - Math.cos(angle) * veinRadius;

		double startY = origin.getY() + random.nextInt(3) - 2;
		double endY = origin.getY() + random.nextInt(3) - 2;

		int boxStartX = origin.getX() - Mth.ceil(veinRadius) - extraSize;
		int boxStartY = origin.getY() - 2 - extraSize;
		int boxStartZ = origin.getZ() - Mth.ceil(veinRadius) - extraSize;

		int boxWidth = 2 * (Mth.ceil(veinRadius) + extraSize);
		int boxHeight = 2 * (2 + extraSize);

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