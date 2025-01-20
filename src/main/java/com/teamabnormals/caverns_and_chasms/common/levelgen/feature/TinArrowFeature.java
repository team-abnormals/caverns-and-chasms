package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class TinArrowFeature extends Feature<NoneFeatureConfiguration> {
	private static final Vec3 X_VECTOR = new Vec3(1.0D, 0.0D, 0.0D);
	private static final Vec3 Y_VECTOR = new Vec3(0.0D, 1.0D, 0.0D);

	private volatile Map<Vector2i, ChunkPos> monolithPositions = new HashMap<>();

	public TinArrowFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos blockpos = context.origin();
		RandomSource random = context.random();

		int chunkX = blockpos.getX() >> 4;
		int chunkZ = blockpos.getZ() >> 4;

		Vector2i spacingPos = new Vector2i(Math.floorDiv(chunkX, TinMonolithStructure.SPACING), Math.floorDiv(chunkZ, TinMonolithStructure.SPACING));
		if (!monolithPositions.containsKey(spacingPos)) {
			synchronized (this) {
				this.monolithPositions.put(spacingPos, getPotentialStructureChunk(level.getSeed(), spacingPos.x, spacingPos.y));
			}
		}

		ChunkPos monolithChunkPos = monolithPositions.get(spacingPos);
		// System.out.println("Monolith Pos: " + (monolithChunkPos.x << 4) + ", " + (monolithChunkPos.z << 4));

		int distToMonolithX = (monolithChunkPos.x << 4) - blockpos.getX();
		int distToMonolithZ = (monolithChunkPos.z << 4) - blockpos.getZ();

		double length = random.nextInt(3) + 10;
		Vec3 lengthAxis = new Vec3(distToMonolithX, -blockpos.getY(), distToMonolithZ).normalize();

		float rot = random.nextFloat() * Mth.TWO_PI;
		Vec3 widthAxisUnrotated = (Math.abs(lengthAxis.dot(X_VECTOR)) > Math.abs(lengthAxis.dot(Y_VECTOR)) ? X_VECTOR : Y_VECTOR).cross(lengthAxis);
		Vec3 widthAxis = widthAxisUnrotated.scale(Mth.cos(rot)).add(lengthAxis.cross(widthAxisUnrotated).scale(Mth.sin(rot))).add(lengthAxis.scale(lengthAxis.dot(widthAxisUnrotated) * (1 - Mth.cos(rot)))).normalize();
		Vec3 heightAxis = lengthAxis.cross(widthAxis).normalize();

		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		boolean placed = false;
		for (int x = -7; x <= 7 ; ++x) {
			for (int y = -7; y <= 7 ; ++y) {
				for (int z = -7; z <= 7 ; ++z) {
					Vec3 offset = new Vec3(x, y ,z);

					double axisX = offset.dot(widthAxis);
					double axisY = offset.dot(heightAxis);
					double axisZ = offset.dot(lengthAxis);

					double pointAlongLength = 0.5D - axisZ / length;
					double radius = pointAlongLength * length * 0.1D;

					double ellipse = (axisX * axisX) / (radius * radius) + (axisY * axisY) / (radius * radius * 4);

					if (pointAlongLength > 0.0D && pointAlongLength < 1.0D && ellipse < 1) {
						mutable.setWithOffset(blockpos, x, y, z);
						BlockState blockstate = level.getBlockState(mutable);

						// TODO: Check if this needs to be made more generic.
						if (blockstate.is(BlockTags.STONE_ORE_REPLACEABLES)) {
							level.setBlock(mutable, CCBlocks.TIN_ORE.get().defaultBlockState(), 2);
							placed = true;
						} else if (blockstate.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
							level.setBlock(mutable, CCBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState(), 2);
							placed = true;
						}
					}
				}
			}
		}

		return placed;
	}

	private static ChunkPos getPotentialStructureChunk(long seed, int spacingX, int spacingZ) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureWithSalt(seed, spacingX, spacingZ, TinMonolithStructure.SALT);
		int k = TinMonolithStructure.SPACING - TinMonolithStructure.SEPARATION;
		int l = RandomSpreadType.LINEAR.evaluate(worldgenrandom, k);
		int i1 = RandomSpreadType.LINEAR.evaluate(worldgenrandom, k);
		return new ChunkPos(spacingX * TinMonolithStructure.SPACING + l, spacingZ * TinMonolithStructure.SPACING + i1);
	}
}