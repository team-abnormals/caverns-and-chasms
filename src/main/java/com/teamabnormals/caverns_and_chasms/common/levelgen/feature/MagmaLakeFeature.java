package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;

public class MagmaLakeFeature extends LakeFeature {
	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public MagmaLakeFeature(Codec<Configuration> p_66259_) {
		super(p_66259_);
	}

	public boolean place(FeaturePlaceContext<Configuration> context) {
		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();
		RandomSource random = context.random();
		MagmaLakeFeature.Configuration config = context.config();
		if (pos.getY() <= level.getMinBuildHeight() + 4) {
			return false;
		} else {
			pos = pos.below(4);
			boolean[] aboolean = new boolean[2048];
			int i = random.nextInt(4) + 4;

			for (int j = 0; j < i; ++j) {
				double d0 = random.nextDouble() * 6.0D + 3.0D;
				double d1 = random.nextDouble() * 4.0D + 2.0D;
				double d2 = random.nextDouble() * 6.0D + 3.0D;
				double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
				double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
				double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

				for (int l = 1; l < 15; ++l) {
					for (int i1 = 1; i1 < 15; ++i1) {
						for (int j1 = 1; j1 < 7; ++j1) {
							double d6 = ((double) l - d3) / (d0 / 2.0D);
							double d7 = ((double) j1 - d4) / (d1 / 2.0D);
							double d8 = ((double) i1 - d5) / (d2 / 2.0D);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < 1.0D) {
								aboolean[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}

			BlockState fluidState = config.fluid().getState(random, pos);

			for (int x = 0; x < 16; ++x) {
				for (int z = 0; z < 16; ++z) {
					for (int y = 0; y < 8; ++y) {
						boolean flag = !aboolean[(x * 16 + z) * 8 + y] && (x < 15 && aboolean[((x + 1) * 16 + z) * 8 + y] || x > 0 && aboolean[((x - 1) * 16 + z) * 8 + y] || z < 15 && aboolean[(x * 16 + z + 1) * 8 + y] || z > 0 && aboolean[(x * 16 + (z - 1)) * 8 + y] || y < 7 && aboolean[(x * 16 + z) * 8 + y + 1] || y > 0 && aboolean[(x * 16 + z) * 8 + (y - 1)]);
						if (flag) {
							BlockState offsetState = level.getBlockState(pos.offset(x, y, z));
							if (y >= 4 && offsetState.liquid()) {
								return false;
							}

							if (y < 4 && !offsetState.isSolid() && level.getBlockState(pos.offset(x, y, z)) != fluidState) {
								return false;
							}
						}
					}
				}
			}

			for (int x = 0; x < 16; ++x) {
				for (int z = 0; z < 16; ++z) {
					for (int y = 0; y < 8; ++y) {
						if (aboolean[(x * 16 + z) * 8 + y]) {
							BlockPos offsetPos = pos.offset(x, y, z);
							if (this.canReplaceBlock(level.getBlockState(offsetPos))) {
								boolean maxHeight = y >= 4;

								if (maxHeight) {
									level.setBlock(offsetPos, AIR, 2);
								} else if (y == 3) {
									boolean xEdge = Math.abs(x - 8) < 3;
									boolean zEdge = Math.abs(z - 8) < 3;
									boolean chance = (xEdge && zEdge) ? random.nextInt(4) != 0 : ((xEdge || zEdge) ? random.nextBoolean() : random.nextInt(4) == 0);
									level.setBlock(offsetPos, chance ? fluidState : Blocks.MAGMA_BLOCK.defaultBlockState(), 2);
								} else {
									level.setBlock(offsetPos, fluidState, 2);
								}


								if (maxHeight) {
									level.scheduleTick(offsetPos, AIR.getBlock(), 0);
									this.markAboveForPostProcessing(level, offsetPos);
								}
							}
						}
					}
				}
			}

			BlockState barrierState = config.barrier().getState(random, pos);
			if (!barrierState.isAir()) {
				for (int x = 0; x < 16; ++x) {
					for (int z = 0; z < 16; ++z) {
						for (int y = 0; y < 8; ++y) {
							boolean flag2 = !aboolean[(x * 16 + z) * 8 + y] && (x < 15 && aboolean[((x + 1) * 16 + z) * 8 + y] || x > 0 && aboolean[((x - 1) * 16 + z) * 8 + y] || z < 15 && aboolean[(x * 16 + z + 1) * 8 + y] || z > 0 && aboolean[(x * 16 + (z - 1)) * 8 + y] || y < 7 && aboolean[(x * 16 + z) * 8 + y + 1] || y > 0 && aboolean[(x * 16 + z) * 8 + (y - 1)]);
							if (flag2 && (y < 4 || random.nextInt(2) != 0)) {
								BlockState offsetState = level.getBlockState(pos.offset(x, y, z));
								if (offsetState.isSolid() && !offsetState.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
									BlockPos offsetPos = pos.offset(x, y, z);
									level.setBlock(offsetPos, barrierState, 2);
									this.markAboveForPostProcessing(level, offsetPos);
								}
							}
						}
					}
				}
			}

			if (fluidState.getFluidState().is(FluidTags.WATER)) {
				for (int x = 0; x < 16; ++x) {
					for (int z = 0; z < 16; ++z) {
						int y = 4;
						BlockPos offsetPos = pos.offset(x, y, z);
						if (level.getBiome(offsetPos).value().shouldFreeze(level, offsetPos, false) && this.canReplaceBlock(level.getBlockState(offsetPos))) {
							level.setBlock(offsetPos, Blocks.ICE.defaultBlockState(), 2);
						}
					}
				}
			}

			return true;
		}
	}

	private boolean canReplaceBlock(BlockState state) {
		return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
	}
}