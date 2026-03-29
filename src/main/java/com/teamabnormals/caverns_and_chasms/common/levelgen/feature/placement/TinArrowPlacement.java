package com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TinArrowPlacement extends PlacementModifier {
	private static final Vector2i[] CLOSEST_MONOLITH_OFFSETS = {new Vector2i(-1, 0), new Vector2i(0, 0), new Vector2i(-1, -1), new Vector2i(0, -1)};

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

	@Nullable
	public static Vector2i getClosestMonolithPosition(WorldGenLevel level, BlockPos pos) {
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;

		Vector2i vec2i = new Vector2i(Math.floorDiv(chunkX + TinMonolithStructure.HALF_SPACING, TinMonolithStructure.SPACING), Math.floorDiv(chunkZ + TinMonolithStructure.HALF_SPACING, TinMonolithStructure.SPACING));

		Map<Vector2i, Vector2i[]> map = CCFeatures.CLOSEST_MONOLITH_POSITIONS_AT.get(level.getLevel());

		if (!map.containsKey(vec2i)) {
			Vector2i[] monolithPositions = new Vector2i[4];
			for (int i = 0; i < 4; i++) {
				Vector2i offset = CLOSEST_MONOLITH_OFFSETS[i];
				monolithPositions[i] = getPotentialStructurePos(level.getSeed(), vec2i.x + offset.x, vec2i.y + offset.y);
			}
			map.put(vec2i, monolithPositions);
		}

		Vector2i[] closestPositions = map.get(vec2i);
		Vector2i closestPos = null;
		int closestDistSqr = Integer.MAX_VALUE;

		for (Vector2i monolithPos : closestPositions) {
			if (monolithPos == null) {
				continue;
			}

			int distSqr = Mth.square(monolithPos.x - pos.getX()) + Mth.square(monolithPos.y - pos.getZ());
			if (distSqr < closestDistSqr) {
				closestPos = monolithPos;
				closestDistSqr = distSqr;
			}
		}

		return closestPos;
	}

	@Nullable
	private static Vector2i getPotentialStructurePos(long seed, int spacingX, int spacingZ) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureWithSalt(seed, spacingX, spacingZ, TinMonolithStructure.SALT);
		int k = TinMonolithStructure.SPACING - TinMonolithStructure.SEPARATION;
		int l = RandomSpreadType.TRIANGULAR.evaluate(worldgenrandom, k);
		int i1 = RandomSpreadType.TRIANGULAR.evaluate(worldgenrandom, k);
		int chunkX = spacingX * TinMonolithStructure.SPACING + l;
		int chunkZ = spacingZ * TinMonolithStructure.SPACING + i1;

		if (Mth.abs(chunkX) <= TinMonolithStructure.NO_MONOLITHS_RANGE || Mth.abs(chunkZ) <= TinMonolithStructure.NO_MONOLITHS_RANGE) {
			return null;
		}

		return new Vector2i(chunkX << 4, chunkZ << 4);
	}
}