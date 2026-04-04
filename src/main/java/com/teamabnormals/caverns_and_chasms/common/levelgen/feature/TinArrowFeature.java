package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.TinMonolithDistanceFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

public class TinArrowFeature extends Feature<OreConfiguration> {
	private static final Vec3 X_VECTOR = new Vec3(1.0D, 0.0D, 0.0D);
	private static final Vec3 Y_VECTOR = new Vec3(0.0D, 1.0D, 0.0D);

	public TinArrowFeature(Codec<OreConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		OreConfiguration config = context.config();
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		RandomSource random = context.random();

		Vector2i monolithPos = TinMonolithDistanceFilter.getClosestMonolithPosition(level, origin);

		if (monolithPos == null)
			return false;

		double length = (1 + random.nextDouble() * 0.25D) * config.size;
		int size = Mth.ceil(length / 2);
		float rot = random.nextFloat() * Mth.TWO_PI;

		Vec3 lengthAxis = new Vec3(monolithPos.x - origin.getX(), -origin.getY(), monolithPos.y - origin.getZ()).normalize();
		Vec3 widthAxis = (Math.abs(lengthAxis.dot(X_VECTOR)) > Math.abs(lengthAxis.dot(Y_VECTOR)) ? X_VECTOR : Y_VECTOR).cross(lengthAxis);
		widthAxis = widthAxis.scale(Mth.cos(rot)).add(lengthAxis.cross(widthAxis).scale(Mth.sin(rot))).add(lengthAxis.scale(lengthAxis.dot(widthAxis) * (1 - Mth.cos(rot)))).normalize();
		Vec3 heightAxis = lengthAxis.cross(widthAxis).normalize();

		boolean placed = false;

		try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			// Cone
			for (int x = -size; x <= size; ++x) {
				for (int y = -size; y <= size; ++y) {
					for (int z = -size; z <= size; ++z) {
						Vec3 offset = new Vec3(x, y, z);

						double axisX = offset.dot(widthAxis);
						double axisY = offset.dot(heightAxis);
						double axisZ = offset.dot(lengthAxis);

						double pointAlongLength = 0.5D - axisZ / length;
						double radiusBig = pointAlongLength * length * 0.3D;
						double radiusSmall = radiusBig * 0.75D;

						double ellipse = Mth.square(axisX / radiusSmall) + Mth.square(axisY / radiusBig);

						if (pointAlongLength > 0.0D && pointAlongLength < 1.0D && ellipse < 1) {
							if (tryToPlaceBlock(level, origin, mutable, x, y, z, bulkSectionAccess, config, random)) {
								placed = true;
							}
						}
					}
				}
			}

			// 3D Bresenham line
			int x0, y0, z0;
			x0 = y0 = z0 = 0;

			int x1 = (int) (lengthAxis.x * size);
			int y1 = (int) (lengthAxis.y * size);
			int z1 = (int) (lengthAxis.z * size);

			int dx = Mth.abs(x1 - x0);
			int dy = Mth.abs(y1 - y0);
			int dz = Mth.abs(z1 - z0);

			int stepX = x1 > x0 ? 1 : -1;
			int stepY = y1 > y0 ? 1 : -1;
			int stepZ = z1 > z0 ? 1 : -1;

			int dm = Math.max(Math.max(dx, dy), dz);

			x1 = y1 = z1 = dm / 2;

			for (int i = 0; i <= dm; i++) {
				if (tryToPlaceBlock(level, origin, mutable, x0, y0, z0, bulkSectionAccess, config, random)) {
					placed = true;
				}

				x1 -= dx;
				if (x1 < 0) {
					x1 += dm;
					x0 += stepX;
				}

				y1 -= dy;
				if (y1 < 0) {
					y1 += dm;
					y0 += stepY;
				}

				z1 -= dz;
				if (z1 < 0) {
					z1 += dm;
					z0 += stepZ;
				}
			}
		}

		return placed;
	}

	private static boolean tryToPlaceBlock(WorldGenLevel level, BlockPos origin, MutableBlockPos mutable, int x, int y, int z, BulkSectionAccess bulkSectionAccess, OreConfiguration config, RandomSource random) {
		int x1 = origin.getX() + x;
		int y1 = origin.getY() + y;
		int z1 = origin.getZ() + z;

		mutable.set(x1, y1, z1);

		if (level.ensureCanWrite(mutable)) {
			LevelChunkSection levelchunksection = bulkSectionAccess.getSection(mutable);

			if (levelchunksection != null) {
				int x2 = SectionPos.sectionRelative(x1);
				int y2 = SectionPos.sectionRelative(y1);
				int z2 = SectionPos.sectionRelative(z1);
				BlockState blockstate = levelchunksection.getBlockState(x2, y2, z2);

				for (OreConfiguration.TargetBlockState targetblockstate : config.targetStates) {
					if (OreFeature.canPlaceOre(blockstate, bulkSectionAccess::getBlockState, random, config, targetblockstate, mutable)) {
						levelchunksection.setBlockState(x2, y2, z2, targetblockstate.state, false);
						return true;
					}
				}
			}
		}

		return false;
	}
}