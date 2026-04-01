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
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.Map;

public class TinMonolithDistanceFilter extends PlacementFilter {
	private static final Vector2i[] CLOSEST_MONOLITH_OFFSETS = {new Vector2i(-1, 0), new Vector2i(0, 0), new Vector2i(-1, -1), new Vector2i(0, -1)};

	public static final Codec<TinMonolithDistanceFilter> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("max_distance").forGetter((placement) -> placement.maxDistance)
		).apply(instance, TinMonolithDistanceFilter::new);
	});
	private final int maxDistance;

	private TinMonolithDistanceFilter(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public static TinMonolithDistanceFilter of(int maxDistance) {
		return new TinMonolithDistanceFilter(maxDistance);
	}

	@Override
	protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
		Vector2i vec2i = getClosestMonolithPosition(context.getLevel(), pos);
		if (vec2i == null) {
			return false;
		} else {
			double dx = vec2i.x - pos.getX();
			double dz = vec2i.y - pos.getZ();
			return dx * dx + dz * dz <= this.maxDistance * this.maxDistance;
		}
	}

	@Override
	public PlacementModifierType<?> type() {
		return CCPlacementModifierTypes.TIN_MONOLITH_DISTANCE_FILTER.get();
	}

	@Nullable
	public static Vector2i getClosestMonolithPosition(WorldGenLevel level, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();

		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		Vector2i vec2i = new Vector2i(Math.floorDiv(chunkX + TinMonolithStructure.HALF_SPACING, TinMonolithStructure.SPACING), Math.floorDiv(chunkZ + TinMonolithStructure.HALF_SPACING, TinMonolithStructure.SPACING));

		Map<Vector2i, Vector2i[]> map = CCFeatures.CLOSEST_MONOLITH_POSITIONS_AT.get(level.getLevel());

		Vector2i[] closestPositions = map.get(vec2i);

		if (closestPositions == null) {
			closestPositions = new Vector2i[4];
			for (int i = 0; i < 4; i++) {
				Vector2i offset = CLOSEST_MONOLITH_OFFSETS[i];
				closestPositions[i] = getPotentialStructurePos(level.getSeed(), vec2i.x + offset.x, vec2i.y + offset.y);
			}
			map.put(vec2i, closestPositions);
		}

		Vector2i closestPos = null;
		int closestDistSqr = Integer.MAX_VALUE;

		for (Vector2i monolithPos : closestPositions) {
			if (monolithPos == null) {
				continue;
			}

			int dx = monolithPos.x - x;
			int dz = monolithPos.y - z;
			int distSqr = dx * dx + dz * dz;

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

		if (Mth.abs(chunkX) <= TinMonolithStructure.NO_MONOLITHS_RANGE && Mth.abs(chunkZ) <= TinMonolithStructure.NO_MONOLITHS_RANGE) {
			return null;
		}

		return new Vector2i(chunkX << 4, chunkZ << 4);
	}
}