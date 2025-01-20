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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TinMonolithPieces {
	public static class TinMonolithPiece extends StructurePiece {

		public TinMonolithPiece(int x, int z) {
			super(CCStructurePieceTypes.TIN_MONOLITH.get(), 0, new BoundingBox(x - 16, -63, z - 16, x + 16, 56, z + 16));
		}

		public TinMonolithPiece(CompoundTag tag) {
			super(CCStructurePieceTypes.TIN_MONOLITH.get(), tag);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compound) {
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox bounds, ChunkPos chunkPos, BlockPos origin) {
			NormalNoise noise = NormalNoise.create(new XoroshiroRandomSource(level.getSeed()), new NormalNoise.NoiseParameters(1, 1.0D, 0.8D));
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			int minX = chunkPos.getMinBlockX() - origin.getX();
			int maxX = chunkPos.getMaxBlockX() - origin.getX();
			int minZ = chunkPos.getMinBlockZ() - origin.getZ();
			int maxZ = chunkPos.getMaxBlockZ() - origin.getZ();

			for (int y = 0; y > -111; --y) {
				double radius = (y + 112) / 112.0D * 16;
				int radiusInt = Mth.ceil(radius);

				int minX1 = Math.max(minX, -radiusInt);
				int maxX1 = Math.min(maxX, radiusInt);
				int minZ1 = Math.max(minZ, -radiusInt);
				int maxZ1 = Math.min(maxZ, radiusInt);

				for (int x = minX1; x <= maxX1; ++x) {
					for (int z = minZ1; z <= maxZ1; ++z) {
						double distance = y > -8 ? Math.sqrt(x * x + 256 + 64 * y + 4 * y * y + z * z) : Math.sqrt(x * x + z * z);
						double noiseAtPos = distance == 0 ? 1.0D : noise.getValue(origin.getX() + x / distance, y * 0.025D, origin.getZ() + z / distance);
						double distance1 = distance / (radius * (1 + noiseAtPos * 0.2D - 0.2D));

						if (distance1 <= 1.0D) {
							mutable.set(origin.getX() + x, 48 + y, origin.getZ() + z);
							BlockState blockstate = level.getBlockState(mutable);

							boolean isstone = blockstate.is(BlockTags.STONE_ORE_REPLACEABLES);
							boolean isdeepslate = blockstate.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

							if (!isstone && !isdeepslate)
								continue;

							if (random.nextFloat() < 0.05D + distance1 * 0.2D)
								level.setBlock(mutable, isdeepslate ? Blocks.COBBLED_DEEPSLATE.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState(), 2);
							else if (random.nextFloat() > Math.max(0.3D + distance1, 0.5D))
								level.setBlock(mutable, CCBlocks.RAW_TIN_BLOCK.get().defaultBlockState(), 2);
							else if (isdeepslate)
								level.setBlock(mutable, CCBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState(), 2);
							else
								level.setBlock(mutable, CCBlocks.TIN_ORE.get().defaultBlockState(), 2);
						}
					}
				}
			}
		}
	}
}