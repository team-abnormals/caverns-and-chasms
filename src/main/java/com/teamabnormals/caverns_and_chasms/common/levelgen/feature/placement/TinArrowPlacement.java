package com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCPlacementModifierTypes;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2i;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TinArrowPlacement extends PlacementModifier {
	public static final Codec<TinArrowPlacement> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("max_count").forGetter((placement) -> placement.maxCount),
				Codec.INT.fieldOf("max_distance").forGetter((placement) -> placement.maxDistance)
		).apply(instance, TinArrowPlacement::new);
	});
	private final int maxCount;
	private final int maxDistance;

	private TinArrowPlacement(int maxCount, int maxDistance) {
		this.maxCount = maxCount;
		this.maxDistance = maxDistance;
	}

	public static TinArrowPlacement of(int maxCount, int maxDistance) {
		return new TinArrowPlacement(maxCount, maxDistance);
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return IntStream.range(0, this.count(context.getLevel(), random, pos)).mapToObj((i) -> pos);
	}

	private int count(WorldGenLevel level, RandomSource random, BlockPos pos) {
		Vector2i vec2i = getClosestMonolithPosition(level, pos);

		if (vec2i == null)
			return 0;

		double d0 = vec2i.x - pos.getX();
		double d1 = vec2i.y - pos.getZ();
		double d2 = Math.sqrt(d0 * d0 + d1 * d1);

		if (d2 < this.maxDistance) {
			double d3 = this.maxCount * (1 - d2 / this.maxDistance);
			int i = (int) d3;
			return i + (random.nextFloat() < d3 - i ? 1 : 0);
		} else {
			return 0;
		}
	}

	@Override
	public PlacementModifierType<?> type() {
		return CCPlacementModifierTypes.TIN_ARROW.get();
	}

	public static Vector2i getClosestMonolithPosition(WorldGenLevel level, BlockPos pos) {
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;
		if (chunkX < -TinMonolithStructure.BLOCK_GEN_RANGE || chunkX >= TinMonolithStructure.BLOCK_GEN_RANGE || chunkZ < -TinMonolithStructure.BLOCK_GEN_RANGE || chunkZ >= TinMonolithStructure.BLOCK_GEN_RANGE) {
			Vector2i spacingPos = new Vector2i(Math.floorDiv(chunkX, TinMonolithStructure.SPACING), Math.floorDiv(chunkZ, TinMonolithStructure.SPACING));
			Map<Vector2i, Vector2i> map = CCFeatures.MONOLITH_POSITIONS.get(level.getLevel());
			if (!map.containsKey(spacingPos)) {
				ChunkPos chunkPos = getPotentialStructureChunk(level.getSeed(), spacingPos.x, spacingPos.y);
				map.put(spacingPos, new Vector2i(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ()));
			}
			return map.get(spacingPos);
		} else {
			return null;
		}
	}

	private static ChunkPos getPotentialStructureChunk(long seed, int spacingX, int spacingZ) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureWithSalt(seed, spacingX, spacingZ, TinMonolithStructure.SALT);
		int k = TinMonolithStructure.SPACING - TinMonolithStructure.SEPARATION;
		int l = RandomSpreadType.TRIANGULAR.evaluate(worldgenrandom, k);
		int i1 = RandomSpreadType.TRIANGULAR.evaluate(worldgenrandom, k);
		return new ChunkPos(spacingX * TinMonolithStructure.SPACING + l, spacingZ * TinMonolithStructure.SPACING + i1);
	}
}