package com.teamabnormals.caverns_and_chasms.common.levelgen.structure;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructurePieceTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TinMonolithPieces {
	public static final int RADIUS = 16;

	public static class TinMonolithPiece extends StructurePiece {

		public TinMonolithPiece(int x, int z) {
			super(CCStructurePieceTypes.TIN_MONOLITH.get(), 0, new BoundingBox(x - RADIUS, -63, z - RADIUS, x + RADIUS, 48, z + RADIUS));
		}

		public TinMonolithPiece(CompoundTag tag) {
			super(CCStructurePieceTypes.TIN_MONOLITH.get(), tag);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compound) {
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox bounds, ChunkPos chunkPos, BlockPos origin) {
			NormalNoise shapeNoise = NormalNoise.create(new XoroshiroRandomSource(random.nextLong()), new NormalNoise.NoiseParameters(1, 1.0D, 0.8D));
			NormalNoise veinNoise = NormalNoise.create(new XoroshiroRandomSource(random.nextLong()), new NormalNoise.NoiseParameters(-4, 1.0D));
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			int minX = chunkPos.getMinBlockX() - origin.getX();
			int maxX = chunkPos.getMaxBlockX() - origin.getX();
			int minZ = chunkPos.getMinBlockZ() - origin.getZ();
			int maxZ = chunkPos.getMaxBlockZ() - origin.getZ();

			int tipHeight = Mth.clamp(level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX(), origin.getZ()) - 4, 0, 48);
			int height = 63 + tipHeight;

			for (int y = 0; y <= height; ++y) {
				double radius = (double) y / height * RADIUS;
				int radiusInt = Mth.ceil(radius);

				int minX1 = Math.max(minX, -radiusInt);
				int maxX1 = Math.min(maxX, radiusInt);
				int minZ1 = Math.max(minZ, -radiusInt);
				int maxZ1 = Math.min(maxZ, radiusInt);

				for (int x = minX1; x <= maxX1; ++x) {
					for (int z = minZ1; z <= maxZ1; ++z) {

						double distFromCenter = Math.sqrt(x * x + z * z);
						double shapeNoiseAtPos = distFromCenter == 0 ? 1.0D : shapeNoise.getValue(origin.getX() + x / distFromCenter, y * 0.025D, origin.getZ() + z / distFromCenter);
						double radiusWithNoise = radius * (1 + shapeNoiseAtPos * 0.2D - 0.2D);
						double relativeDistFromCenter = distFromCenter / radiusWithNoise;

						if (relativeDistFromCenter <= 1.0D) {
							int levelX = origin.getX() + x;
							int levelY = tipHeight - y;
							int levelZ = origin.getZ() + z;

							mutable.set(levelX, levelY, levelZ);
							BlockState blockstate = level.getBlockState(mutable);

							if (blockstate.is(BlockTags.FEATURES_CANNOT_REPLACE))
								continue;

							boolean isInside = distFromCenter <= radiusWithNoise - 3.0D;
							double veinNoiseAtPos = Math.abs(veinNoise.getValue(levelX, levelY, levelZ));
							double oreDensity = veinNoiseAtPos > 0.3D ? 0.0D : (0.3D - veinNoiseAtPos) * 3.5D;

							if (random.nextFloat() * oreDensity > Math.max(0.4D + relativeDistFromCenter, 0.6D))
								level.setBlock(mutable, CCBlocks.RAW_TIN_BLOCK.get().defaultBlockState(), 2);
							else if (random.nextFloat() * oreDensity > Math.min(0.3D + relativeDistFromCenter * 0.3D, 0.5D))
								level.setBlock(mutable, isInside ? CCBlocks.CASSITERITE_TIN_ORE.get().defaultBlockState() : CCBlocks.CYLINDRITE_TIN_ORE.get().defaultBlockState(), 2);
							else
								level.setBlock(mutable, isInside ? CCBlocks.CASSITERITE.get().defaultBlockState() : (computeDeepslateGradient(levelY, random) ? CCBlocks.CYLINDRITE : CCBlocks.SMOOTH_CYLINDRITE).get().defaultBlockState(), 2);
						}
					}
				}
			}
		}

		private static boolean computeDeepslateGradient(int y, RandomSource random) {
			if (y <= -8) {
				return true;
			} else if (y >= 8) {
				return false;
			} else {
				double d0 = Mth.map(y, -8, 8, 1.0D, 0.0D);
				return random.nextFloat() < d0;
			}
		}
	}
}