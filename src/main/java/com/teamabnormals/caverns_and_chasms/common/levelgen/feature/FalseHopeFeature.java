package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FalseHopeFeature extends Feature<NoneFeatureConfiguration> {
	private static final int[][] OFFSETS = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

	public FalseHopeFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		RandomSource random = context.random();
		BlockPos origin = context.origin();

		for (int i = 0; i < 8; i++) {
			BlockPos blockpos = origin.offset(random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(2), random.nextInt(8) - random.nextInt(8));
			if (level.isEmptyBlock(blockpos)) {
				BlockState belowstate = level.getBlockState(blockpos.below());
				if ((isStone(belowstate) || isDirt(belowstate)) && hasCeilingAbove(level, blockpos) && isOnAngledCliff(level, blockpos)) {
					level.setBlock(blockpos, CCBlocks.FALSE_HOPE.get().defaultBlockState(), 2);
					MutableBlockPos mutable = new MutableBlockPos();
					for (int x = -4; x <= 4; x++) {
						for (int y = -4; y <= 4; y++) {
							for (int z = -4; z <= 4; z++) {
								if (x * x + y * y + z * z <= 16) {
									mutable.setWithOffset(blockpos, x, y, z);
									if (level.getBlockState(mutable).is(BlockTags.BASE_STONE_OVERWORLD)) {
										boolean covered = level.getBlockState(mutable.above()).isSolid();
										level.setBlock(mutable, covered ? Blocks.DIRT.defaultBlockState() : Blocks.GRASS_BLOCK.defaultBlockState(), 2);
										if (!covered && level.getBlockState(mutable.above()).isAir() && random.nextInt(3) == 0) {
											level.setBlock(mutable.above(), Blocks.GRASS.defaultBlockState(), 2);
										}
									}
								}
							}
						}
					}

					return true;
				}
			}
		}

		return false;
	}

	private static boolean isOnTopOfCave(WorldGenLevel level, BlockPos blockPos) {
		MutableBlockPos mutable = blockPos.mutable();
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				for (int y = 1; y <= 8; y++) {
					mutable.setWithOffset(blockPos, x, y, z);
					if (level.getBlockState(mutable).isSolid())
						break;
					else if (y == 8)
						return false;
				}
			}
		}

		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				for (int y = -1; y >= -6; y--) {
					mutable.setWithOffset(blockPos, x, y, z);
					if (level.getBlockState(mutable).isSolid())
						break;
					else if (y == -6)
						return true;
				}
			}
		}

		return false;
	}

	private static boolean hasCeilingAbove(WorldGenLevel level, BlockPos blockPos) {
		MutableBlockPos mutable = blockPos.mutable();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 1; y <= 8; y++) {
					mutable.setWithOffset(blockPos, x, y, z);
					if (level.getBlockState(mutable).isSolid())
						break;
					else if (y == 8)
						return false;
				}
			}
		}
		return true;
	}

	private static boolean hasCeilingAboveOld(WorldGenLevel level, BlockPos blockPos) {
		MutableBlockPos mutable = blockPos.mutable();
		for (int i = 0; i < 10; i++) {
			if (level.getBlockState(mutable.move(Direction.UP)).isSolid())
				return true;
		}
		return false;
	}

	private static boolean isOnAngledCliff(WorldGenLevel level, BlockPos blockPos) {
		MutableBlockPos mutable = blockPos.mutable();
		MutableBlockPos offsetpos = new MutableBlockPos();

		for (int y = 0; y > -6; y--) {
			label:
			{
				for (int[] offset : OFFSETS) {
					offsetpos.setWithOffset(mutable, offset[0], -1, offset[1]);
					if (!level.getBlockState(offsetpos).isSolid()) {
						mutable.set(offsetpos);
						break label;
					}
				}
				return false;
			}
		}
		return true;
	}
}