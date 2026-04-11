package com.teamabnormals.caverns_and_chasms.common.levelgen.structure;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructurePieceTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class LushMineshaftPieces extends MineshaftPieces {
	static final Logger LOGGER = LogUtils.getLogger();
	private static final int DEFAULT_SHAFT_WIDTH = 3;
	private static final int DEFAULT_SHAFT_HEIGHT = 3;
	private static final int DEFAULT_SHAFT_LENGTH = 5;
	private static final int MAX_PILLAR_HEIGHT = 20;
	private static final int MAX_CHAIN_HEIGHT = 50;
	private static final int MAX_DEPTH = 8;
	public static final int MAGIC_START_Y = 50;

	private static LushMineshaftPieces.MineShaftPiece createRandomShaftPiece(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, @Nullable Direction orientation, int genDepth, LushMineshaftStructure.Type type) {
		int i = random.nextInt(100);
		if (i >= 80) {
			BoundingBox boundingbox = MineshaftPieces.MineShaftCrossing.findCrossing(pieces, random, x, y, z, orientation);
			if (boundingbox != null) {
				return new LushMineshaftPieces.MineShaftCrossing(genDepth, boundingbox, orientation, type);
			}
		} else if (i >= 70) {
			BoundingBox boundingbox1 = MineshaftPieces.MineShaftStairs.findStairs(pieces, random, x, y, z, orientation);
			if (boundingbox1 != null) {
				return new LushMineshaftPieces.MineShaftStairs(genDepth, boundingbox1, orientation, type);
			}
		} else {
			BoundingBox boundingbox2 = MineshaftPieces.MineShaftCorridor.findCorridorSize(pieces, random, x, y, z, orientation);
			if (boundingbox2 != null) {
				return new LushMineshaftPieces.MineShaftCorridor(genDepth, random, boundingbox2, orientation, type);
			}
		}

		return null;
	}

	static LushMineshaftPieces.MineShaftPiece generateAndAddPiece(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction, int genDepth) {
		if (genDepth > 8) {
			return null;
		} else if (Math.abs(x - piece.getBoundingBox().minX()) <= 80 && Math.abs(z - piece.getBoundingBox().minZ()) <= 80) {
			LushMineshaftStructure.Type mineshaftstructure$type = ((LushMineshaftPieces.MineShaftPiece) piece).type;
			LushMineshaftPieces.MineShaftPiece mineshaftpieces$mineshaftpiece = createRandomShaftPiece(pieces, random, x, y, z, direction, genDepth + 1, mineshaftstructure$type);
			if (mineshaftpieces$mineshaftpiece != null) {
				pieces.addPiece(mineshaftpieces$mineshaftpiece);
				mineshaftpieces$mineshaftpiece.addChildren(piece, pieces, random);
			}

			return mineshaftpieces$mineshaftpiece;
		} else {
			return null;
		}
	}

	public static class MineShaftCorridor extends LushMineshaftPieces.MineShaftPiece {
		private final boolean hasRails;
		private final boolean spiderCorridor;
		private boolean hasPlacedSpider;
		private final int numSections;

		public MineShaftCorridor(CompoundTag tag) {
			super(CCStructurePieceTypes.MINE_SHAFT_CORRIDOR.get(), tag);
			this.hasRails = tag.getBoolean("hr");
			this.spiderCorridor = tag.getBoolean("sc");
			this.hasPlacedSpider = tag.getBoolean("hps");
			this.numSections = tag.getInt("Num");
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putBoolean("hr", this.hasRails);
			tag.putBoolean("sc", this.spiderCorridor);
			tag.putBoolean("hps", this.hasPlacedSpider);
			tag.putInt("Num", this.numSections);
		}

		public MineShaftCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction orientation, LushMineshaftStructure.Type type) {
			super(CCStructurePieceTypes.MINE_SHAFT_CORRIDOR.get(), genDepth, type, boundingBox);
			this.setOrientation(orientation);
			this.hasRails = random.nextInt(3) == 0;
			this.spiderCorridor = !this.hasRails && random.nextInt(23) == 0;
			if (this.getOrientation().getAxis() == Direction.Axis.Z) {
				this.numSections = boundingBox.getZSpan() / 5;
			} else {
				this.numSections = boundingBox.getXSpan() / 5;
			}
		}

		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
			int i = this.getGenDepth();
			int j = random.nextInt(4);
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch (direction) {
					case NORTH:
					default:
						if (j <= 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, direction, i);
						} else if (j == 2) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, i);
						} else {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, i);
						}
						break;
					case SOUTH:
						if (j <= 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, direction, i);
						} else if (j == 2) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, i);
						} else {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, i);
						}
						break;
					case WEST:
						if (j <= 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
						} else if (j == 2) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
						} else {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
						}
						break;
					case EAST:
						if (j <= 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
						} else if (j == 2) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
						} else {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
						}
				}
			}

			if (i < 8) {
				if (direction != Direction.NORTH && direction != Direction.SOUTH) {
					for (int i1 = this.boundingBox.minX() + 3; i1 + 3 <= this.boundingBox.maxX(); i1 += 5) {
						int j1 = random.nextInt(5);
						if (j1 == 0) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, i1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i + 1);
						} else if (j1 == 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, i1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i + 1);
						}
					}
				} else {
					for (int k = this.boundingBox.minZ() + 3; k + 3 <= this.boundingBox.maxZ(); k += 5) {
						int l = random.nextInt(5);
						if (l == 0) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), k, Direction.WEST, i + 1);
						} else if (l == 1) {
							LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), k, Direction.EAST, i + 1);
						}
					}
				}
			}
		}

		@Override
		protected boolean createChest(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
			BlockPos blockpos = this.getWorldPos(x, y, z);
			if (box.isInside(blockpos) && level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos.below()).isAir()) {
				BlockState blockstate = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
				this.placeBlock(level, blockstate, x, y, z, box);
				MinecartChest minecartchest = new MinecartChest(level.getLevel(), (double) blockpos.getX() + 0.5, (double) blockpos.getY() + 0.5, (double) blockpos.getZ() + 0.5);
				minecartchest.setLootTable(lootTable, random.nextLong());
				level.addFreshEntity(minecartchest);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
			if (!this.isInInvalidLocation(level, box)) {
				int i = 0;
				int j = 2;
				int k = 0;
				int l = 2;
				int i1 = this.numSections * 5 - 1;
				BlockState blockstate = this.type.getPlanksState();
				this.generateBox(level, box, 0, 0, 0, 2, 1, i1, CAVE_AIR, CAVE_AIR, false);
				this.generateMaybeBox(level, box, random, 0.8F, 0, 2, 0, 2, 2, i1, CAVE_AIR, CAVE_AIR, false, false);
				if (this.spiderCorridor) {
					this.generateMaybeBox(level, box, random, 0.6F, 0, 0, 0, 2, 1, i1, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
				}

				for (int j1 = 0; j1 < this.numSections; j1++) {
					int k1 = 2 + j1 * 5;
					this.placeSupport(level, box, 0, 0, k1, 2, 2, random);
					this.maybePlaceCobWeb(level, box, random, 0.1F, 0, 2, k1 - 1);
					this.maybePlaceCobWeb(level, box, random, 0.1F, 2, 2, k1 - 1);
					this.maybePlaceCobWeb(level, box, random, 0.1F, 0, 2, k1 + 1);
					this.maybePlaceCobWeb(level, box, random, 0.1F, 2, 2, k1 + 1);
					this.maybePlaceCobWeb(level, box, random, 0.05F, 0, 2, k1 - 2);
					this.maybePlaceCobWeb(level, box, random, 0.05F, 2, 2, k1 - 2);
					this.maybePlaceCobWeb(level, box, random, 0.05F, 0, 2, k1 + 2);
					this.maybePlaceCobWeb(level, box, random, 0.05F, 2, 2, k1 + 2);
					if (random.nextInt(100) == 0) {
						this.createChest(level, box, random, 2, 0, k1 - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
					}

					if (random.nextInt(100) == 0) {
						this.createChest(level, box, random, 0, 0, k1 + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
					}

					if (this.spiderCorridor && !this.hasPlacedSpider) {
						int l1 = 1;
						int i2 = k1 - 1 + random.nextInt(3);
						BlockPos blockpos = this.getWorldPos(1, 0, i2);
						if (box.isInside(blockpos) && this.isInterior(level, 1, 0, i2, box)) {
							this.hasPlacedSpider = true;
							level.setBlock(blockpos, Blocks.SPAWNER.defaultBlockState(), 2);
							if (level.getBlockEntity(blockpos) instanceof SpawnerBlockEntity spawnerblockentity) {
								spawnerblockentity.setEntityId(EntityType.CAVE_SPIDER, random);
							}
						}
					}
				}

				for (int j2 = 0; j2 <= 2; j2++) {
					for (int l2 = 0; l2 <= i1; l2++) {
						this.setPlanksBlock(level, box, blockstate, j2, -1, l2);
					}
				}

				int k2 = 2;
				this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, 2);
				if (this.numSections > 1) {
					int i3 = i1 - 2;
					this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, i3);
				}

				if (this.hasRails) {
					BlockState blockstate1 = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

					for (int j3 = 0; j3 <= i1; j3++) {
						BlockState blockstate2 = this.getBlock(level, 1, -1, j3, box);
						if (!blockstate2.isAir() && blockstate2.isSolidRender(level, this.getWorldPos(1, -1, j3))) {
							float f = this.isInterior(level, 1, 0, j3, box) ? 0.7F : 0.9F;
							this.maybeGenerateBlock(level, box, random, f, 1, 0, j3, blockstate1);
						}
					}
				}
			}
		}

		private void placeDoubleLowerOrUpperSupport(WorldGenLevel level, BoundingBox box, int x, int y, int z) {
			BlockState blockstate = this.type.getWoodState();
			BlockState blockstate1 = this.type.getPlanksState();
			if (this.getBlock(level, x, y, z, box).is(blockstate1.getBlock())) {
				this.fillPillarDownOrChainUp(level, blockstate, x, y, z, box);
			}

			if (this.getBlock(level, x + 2, y, z, box).is(blockstate1.getBlock())) {
				this.fillPillarDownOrChainUp(level, blockstate, x + 2, y, z, box);
			}
		}

		@Override
		protected void fillColumnDown(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
			if (box.isInside(blockpos$mutableblockpos)) {
				int i = blockpos$mutableblockpos.getY();

				while (this.isReplaceableByStructures(level.getBlockState(blockpos$mutableblockpos)) && blockpos$mutableblockpos.getY() > level.getMinBuildHeight() + 1) {
					blockpos$mutableblockpos.move(Direction.DOWN);
				}

				if (this.canPlaceColumnOnTopOf(level, blockpos$mutableblockpos, level.getBlockState(blockpos$mutableblockpos))) {
					while (blockpos$mutableblockpos.getY() < i) {
						blockpos$mutableblockpos.move(Direction.UP);
						level.setBlock(blockpos$mutableblockpos, state, 2);
					}
				}
			}
		}

		protected void fillPillarDownOrChainUp(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
			if (box.isInside(blockpos$mutableblockpos)) {
				int i = blockpos$mutableblockpos.getY();
				int j = 1;
				boolean flag = true;

				for (boolean flag1 = true; flag || flag1; j++) {
					if (flag) {
						blockpos$mutableblockpos.setY(i - j);
						BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
						boolean flag2 = this.isReplaceableByStructures(blockstate) && !blockstate.is(Blocks.LAVA);
						if (!flag2 && this.canPlaceColumnOnTopOf(level, blockpos$mutableblockpos, blockstate)) {
							fillColumnBetween(level, state, blockpos$mutableblockpos, i - j + 1, i);
							return;
						}

						flag = j <= 20 && flag2 && blockpos$mutableblockpos.getY() > level.getMinBuildHeight() + 1;
					}

					if (flag1) {
						blockpos$mutableblockpos.setY(i + j);
						BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
						boolean flag3 = this.isReplaceableByStructures(blockstate1);
						if (!flag3 && this.canHangChainBelow(level, blockpos$mutableblockpos, blockstate1)) {
							level.setBlock(blockpos$mutableblockpos.setY(i + 1), this.type.getFenceState(), 2);
							fillColumnBetween(level, Blocks.CHAIN.defaultBlockState(), blockpos$mutableblockpos, i + 2, i + j);
							return;
						}

						flag1 = j <= 50 && flag3 && blockpos$mutableblockpos.getY() < level.getMaxBuildHeight() - 1;
					}
				}
			}
		}

		private static void fillColumnBetween(WorldGenLevel level, BlockState state, BlockPos.MutableBlockPos pos, int minY, int maxY) {
			for (int i = minY; i < maxY; i++) {
				level.setBlock(pos.setY(i), state, 2);
			}
		}

		private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos pos, BlockState state) {
			return state.isFaceSturdy(level, pos, Direction.UP);
		}

		private boolean canHangChainBelow(LevelReader level, BlockPos pos, BlockState state) {
			return Block.canSupportCenter(level, pos, Direction.DOWN) && !(state.getBlock() instanceof FallingBlock);
		}

		private void placeSupport(WorldGenLevel level, BoundingBox box, int minX, int minY, int z, int maxY, int maxX, RandomSource random) {
			if (this.isSupportingBox(level, box, minX, maxX, maxY, z)) {
				BlockState blockstate = this.type.getPlanksState();
				BlockState blockstate1 = this.type.getFenceState();
				this.generateBox(level, box, minX, minY, z, minX, maxY - 1, z, blockstate1.setValue(FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
				this.generateBox(level, box, maxX, minY, z, maxX, maxY - 1, z, blockstate1.setValue(FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
				if (random.nextInt(4) == 0) {
					this.generateBox(level, box, minX, maxY, z, minX, maxY, z, blockstate, CAVE_AIR, false);
					this.generateBox(level, box, maxX, maxY, z, maxX, maxY, z, blockstate, CAVE_AIR, false);
				} else {
					this.generateBox(level, box, minX, maxY, z, maxX, maxY, z, blockstate, CAVE_AIR, false);
					this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z - 1, CCBlocks.PINK_SPARKLER.getSecond().get().defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
					this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z + 1, CCBlocks.PINK_SPARKLER.getSecond().get().defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
				}
			}
		}

		private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox box, RandomSource random, float chance, int x, int y, int z) {
			if (this.isInterior(level, x, y, z, box) && random.nextFloat() < chance && this.hasSturdyNeighbours(level, box, x, y, z, 2)) {
				this.placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, box);
			}
		}

		private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox box, int x, int y, int z, int required) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
			int i = 0;

			for (Direction direction : Direction.values()) {
				blockpos$mutableblockpos.move(direction);
				if (box.isInside(blockpos$mutableblockpos) && level.getBlockState(blockpos$mutableblockpos).isFaceSturdy(level, blockpos$mutableblockpos, direction.getOpposite())) {
					if (++i >= required) {
						return true;
					}
				}

				blockpos$mutableblockpos.move(direction.getOpposite());
			}

			return false;
		}
	}

	public static class MineShaftCrossing extends LushMineshaftPieces.MineShaftPiece {
		private final Direction direction;
		private final boolean isTwoFloored;

		public MineShaftCrossing(CompoundTag tag) {
			super(CCStructurePieceTypes.MINE_SHAFT_CROSSING.get(), tag);
			this.isTwoFloored = tag.getBoolean("tf");
			this.direction = Direction.from2DDataValue(tag.getInt("D"));
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putBoolean("tf", this.isTwoFloored);
			tag.putInt("D", this.direction.get2DDataValue());
		}

		public MineShaftCrossing(int genDepth, BoundingBox boundingBox, @Nullable Direction direction, LushMineshaftStructure.Type type) {
			super(CCStructurePieceTypes.MINE_SHAFT_CROSSING.get(), genDepth, type, boundingBox);
			this.direction = direction;
			this.isTwoFloored = boundingBox.getYSpan() > 3;
		}

		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
			int i = this.getGenDepth();
			switch (this.direction) {
				case NORTH:
				default:
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
					break;
				case SOUTH:
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
					break;
				case WEST:
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
					break;
				case EAST:
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
			}

			if (this.isTwoFloored) {
				if (random.nextBoolean()) {
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
				}

				if (random.nextBoolean()) {
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.WEST, i);
				}

				if (random.nextBoolean()) {
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.EAST, i);
				}

				if (random.nextBoolean()) {
					LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
				}
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
			if (!this.isInInvalidLocation(level, box)) {
				BlockState blockstate = this.type.getPlanksState();
				if (this.isTwoFloored) {
					this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
					this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
					this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
					this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
					this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
				} else {
					this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
					this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
				}

				this.placeSupportPillar(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
				this.placeSupportPillar(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
				this.placeSupportPillar(level, box, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
				this.placeSupportPillar(level, box, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
				int i = this.boundingBox.minY() - 1;

				for (int j = this.boundingBox.minX(); j <= this.boundingBox.maxX(); j++) {
					for (int k = this.boundingBox.minZ(); k <= this.boundingBox.maxZ(); k++) {
						this.setPlanksBlock(level, box, blockstate, j, i, k);
					}
				}
			}
		}

		private void placeSupportPillar(WorldGenLevel level, BoundingBox box, int x, int y, int z, int maxY) {
			if (!this.getBlock(level, x, maxY + 1, z, box).isAir()) {
				this.generateBox(level, box, x, y, z, x, maxY, z, this.type.getPlanksState(), CAVE_AIR, false);
			}
		}
	}

	public abstract static class MineShaftPiece extends MineshaftPieces.MineShaftPiece {
		public LushMineshaftStructure.Type type;

		public MineShaftPiece(StructurePieceType structurePieceType, int genDepth, LushMineshaftStructure.Type type, BoundingBox boundingBox) {
			super(structurePieceType, genDepth, null, boundingBox);
			this.type = type;
		}

		public MineShaftPiece(StructurePieceType type, CompoundTag tag) {
			super(type, tag);
			this.type = LushMineshaftStructure.Type.byId(tag.getInt("MST"));
		}

		@Override
		protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox box) {
			BlockState blockstate = this.getBlock(level, x, y, z, box);
			return !blockstate.is(this.type.getPlanksState().getBlock()) && !blockstate.is(this.type.getWoodState().getBlock()) && !blockstate.is(this.type.getFenceState().getBlock()) && !blockstate.is(Blocks.CHAIN);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			tag.putInt("MST", this.type.ordinal());
		}
	}

	public static class MineShaftRoom extends LushMineshaftPieces.MineShaftPiece {
		private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();

		public MineShaftRoom(int genDepth, RandomSource random, int x, int z, LushMineshaftStructure.Type type) {
			super(CCStructurePieceTypes.MINE_SHAFT_ROOM.get(), genDepth, type, new BoundingBox(x, 50, z, x + 7 + random.nextInt(6), 54 + random.nextInt(6), z + 7 + random.nextInt(6)));
			this.type = type;
		}

		public MineShaftRoom(CompoundTag tag) {
			super(CCStructurePieceTypes.MINE_SHAFT_ROOM.get(), tag);
			BoundingBox.CODEC.listOf().parse(NbtOps.INSTANCE, tag.getList("Entrances", 11)).resultOrPartial(LushMineshaftPieces.LOGGER::error).ifPresent(this.childEntranceBoxes::addAll);
		}

		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
			int i = this.getGenDepth();
			int k = this.boundingBox.getYSpan() - 3 - 1;
			if (k <= 0) {
				k = 1;
			}

			int j = 0;

			while (j < this.boundingBox.getXSpan()) {
				j += random.nextInt(this.boundingBox.getXSpan());
				if (j + 3 > this.boundingBox.getXSpan()) {
					break;
				}

				LushMineshaftPieces.MineShaftPiece mineshaftpieces$mineshaftpiece = LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + j, this.boundingBox.minY() + random.nextInt(k) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
				if (mineshaftpieces$mineshaftpiece != null) {
					BoundingBox boundingbox = mineshaftpieces$mineshaftpiece.getBoundingBox();
					this.childEntranceBoxes.add(new BoundingBox(boundingbox.minX(), boundingbox.minY(), this.boundingBox.minZ(), boundingbox.maxX(), boundingbox.maxY(), this.boundingBox.minZ() + 1));
				}

				j += 4;
			}

			j = 0;

			while (j < this.boundingBox.getXSpan()) {
				j += random.nextInt(this.boundingBox.getXSpan());
				if (j + 3 > this.boundingBox.getXSpan()) {
					break;
				}

				LushMineshaftPieces.MineShaftPiece mineshaftpieces$mineshaftpiece1 = LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + j, this.boundingBox.minY() + random.nextInt(k) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
				if (mineshaftpieces$mineshaftpiece1 != null) {
					BoundingBox boundingbox1 = mineshaftpieces$mineshaftpiece1.getBoundingBox();
					this.childEntranceBoxes.add(new BoundingBox(boundingbox1.minX(), boundingbox1.minY(), this.boundingBox.maxZ() - 1, boundingbox1.maxX(), boundingbox1.maxY(), this.boundingBox.maxZ()));
				}

				j += 4;
			}

			j = 0;

			while (j < this.boundingBox.getZSpan()) {
				j += random.nextInt(this.boundingBox.getZSpan());
				if (j + 3 > this.boundingBox.getZSpan()) {
					break;
				}

				LushMineshaftPieces.MineShaftPiece mineshaftpieces$mineshaftpiece2 = LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.WEST, i);
				if (mineshaftpieces$mineshaftpiece2 != null) {
					BoundingBox boundingbox2 = mineshaftpieces$mineshaftpiece2.getBoundingBox();
					this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), boundingbox2.minY(), boundingbox2.minZ(), this.boundingBox.minX() + 1, boundingbox2.maxY(), boundingbox2.maxZ()));
				}

				j += 4;
			}

			j = 0;

			while (j < this.boundingBox.getZSpan()) {
				j += random.nextInt(this.boundingBox.getZSpan());
				if (j + 3 > this.boundingBox.getZSpan()) {
					break;
				}

				StructurePiece structurepiece = LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.EAST, i);
				if (structurepiece != null) {
					BoundingBox boundingbox3 = structurepiece.getBoundingBox();
					this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, boundingbox3.minY(), boundingbox3.minZ(), this.boundingBox.maxX(), boundingbox3.maxY(), boundingbox3.maxZ()));
				}

				j += 4;
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
			if (!this.isInInvalidLocation(level, box)) {
				this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);

				for (BoundingBox boundingbox : this.childEntranceBoxes) {
					this.generateBox(level, box, boundingbox.minX(), boundingbox.maxY() - 2, boundingbox.minZ(), boundingbox.maxX(), boundingbox.maxY(), boundingbox.maxZ(), CAVE_AIR, CAVE_AIR, false);
				}

				this.generateUpperHalfSphere(level, box, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
			}
		}

		@Override
		public void move(int x, int y, int z) {
			super.move(x, y, z);

			for (BoundingBox boundingbox : this.childEntranceBoxes) {
				boundingbox.move(x, y, z);
			}
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			BoundingBox.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.childEntranceBoxes).resultOrPartial(LushMineshaftPieces.LOGGER::error).ifPresent(p_227930_ -> tag.put("Entrances", p_227930_));
		}
	}

	public static class MineShaftStairs extends LushMineshaftPieces.MineShaftPiece {
		public MineShaftStairs(int genDepth, BoundingBox boundingBox, Direction orientation, LushMineshaftStructure.Type type) {
			super(CCStructurePieceTypes.MINE_SHAFT_STAIRS.get(), genDepth, type, boundingBox);
			this.setOrientation(orientation);
		}

		public MineShaftStairs(CompoundTag tag) {
			super(CCStructurePieceTypes.MINE_SHAFT_STAIRS.get(), tag);
		}

		@Nullable
		public static BoundingBox findStairs(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction) {
			BoundingBox $$9 = switch (direction) {
				default -> new BoundingBox(0, -5, -8, 2, 2, 0);
				case SOUTH -> new BoundingBox(0, -5, 0, 2, 2, 8);
				case WEST -> new BoundingBox(-8, -5, 0, 0, 2, 2);
				case EAST -> new BoundingBox(0, -5, 0, 8, 2, 2);
			};
			$$9.move(x, y, z);
			return pieces.findCollisionPiece($$9) != null ? null : $$9;
		}

		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
			int i = this.getGenDepth();
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch (direction) {
					case NORTH:
					default:
						LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
						break;
					case SOUTH:
						LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
						break;
					case WEST:
						LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST, i);
						break;
					case EAST:
						LushMineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, i);
				}
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
			if (!this.isInInvalidLocation(level, box)) {
				this.generateBox(level, box, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
				this.generateBox(level, box, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

				for (int i = 0; i < 5; i++) {
					this.generateBox(level, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
				}
			}
		}
	}
}