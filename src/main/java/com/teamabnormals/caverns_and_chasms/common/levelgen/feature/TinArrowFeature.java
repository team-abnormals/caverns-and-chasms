package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement.TinArrowPlacement;
import net.minecraft.core.BlockPos;
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
		BlockPos blockpos = context.origin();
		RandomSource random = context.random();

		BlockPos monolithPos = TinArrowPlacement.getClosestMonolithPosition(level.getSeed(), blockpos);

		if (monolithPos == null)
			return false;

		double length = (1 + random.nextDouble() * 0.25D) * config.size;
		int size = Mth.ceil(length / 2);
		float rot = random.nextFloat() * Mth.TWO_PI;

		Vec3 lengthAxis = new Vec3(monolithPos.getX() - blockpos.getX(), -blockpos.getY(), monolithPos.getZ() - blockpos.getZ()).normalize();
		Vec3 widthAxisUnrotated = (Math.abs(lengthAxis.dot(X_VECTOR)) > Math.abs(lengthAxis.dot(Y_VECTOR)) ? X_VECTOR : Y_VECTOR).cross(lengthAxis);
		Vec3 widthAxis = widthAxisUnrotated.scale(Mth.cos(rot)).add(lengthAxis.cross(widthAxisUnrotated).scale(Mth.sin(rot))).add(lengthAxis.scale(lengthAxis.dot(widthAxisUnrotated) * (1 - Mth.cos(rot)))).normalize();
		Vec3 heightAxis = lengthAxis.cross(widthAxis).normalize();

		boolean placed = false;

		try (BulkSectionAccess bulksectionaccess = new BulkSectionAccess(level)) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			for (int x = -size; x <= size; ++x) {
				for (int y = -size; y <= size; ++y) {
					for (int z = -size; z <= size; ++z) {
						Vec3 offset = new Vec3(x, y, z);

						double axisX = offset.dot(widthAxis);
						double axisY = offset.dot(heightAxis);
						double axisZ = offset.dot(lengthAxis);

						double pointAlongLength = 0.5D - axisZ / length;
						double radius = pointAlongLength * length * 0.1D;

						double ellipse = (axisX * axisX) / (radius * radius) + (axisY * axisY) / (radius * radius * 4);

						if (pointAlongLength > 0.0D && pointAlongLength < 1.0D && ellipse < 1) {
							int x1 = blockpos.getX() + x;
							int y1 = blockpos.getY() + y;
							int z1 = blockpos.getZ() + z;

							mutable.set(x1, y1, z1);

							if (level.ensureCanWrite(mutable)) {
								LevelChunkSection levelchunksection = bulksectionaccess.getSection(mutable);

								if (levelchunksection != null) {
									int x2 = SectionPos.sectionRelative(x1);
									int y2 = SectionPos.sectionRelative(y1);
									int z2 = SectionPos.sectionRelative(z1);
									BlockState blockstate = levelchunksection.getBlockState(x2, y2, z2);

									for (OreConfiguration.TargetBlockState targetblockstate : config.targetStates) {
										if (OreFeature.canPlaceOre(blockstate, bulksectionaccess::getBlockState, random, config, targetblockstate, mutable)) {
											levelchunksection.setBlockState(x2, y2, z2, targetblockstate.state, false);
											placed = true;
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return placed;
	}
}