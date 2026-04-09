package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.common.block.CaveGrowthsBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.common.Tags;

public class CaveGrowthGroveFeature extends Feature<NoneFeatureConfiguration> {

	public CaveGrowthGroveFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		RandomSource random = context.random();
		BlockPos blockpos = context.origin();
		WorldGenLevel level = context.level();
		ServerLevel serverLevel = level.getLevel();
		NormalNoise noise = CCFeatures.CAVE_GROWTH_GRADIENT.get(serverLevel);
		boolean placed = false;

		int size = 4 + random.nextInt(3) + random.nextInt(2) + random.nextInt(2);

		MutableBlockPos mutable = new MutableBlockPos();
		for (int x = -size; x <= size; ++x) {
			for (int y = -size; y <= size; ++y) {
				for (int z = -size; z <= size; ++z) {
					if (x * x + y * y + z * z > size * size)
						continue;

					mutable.setWithOffset(blockpos, x, y, z);
					BlockState blockstate = level.getBlockState(mutable);

					if (random.nextFloat() < 0.4F && blockstate.is(BlockTags.BASE_STONE_OVERWORLD)) {
						level.setBlock(mutable, random.nextInt(6) > 0 ? CCBlocks.SUGILITE.get().defaultBlockState() : blockstate.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES) ? CCBlocks.DEEPSLATE_SPINEL_ORE.get().defaultBlockState() : CCBlocks.SPINEL_ORE.get().defaultBlockState(), 2);
						placed = true;
					}

					if (level.isEmptyBlock(mutable) && !CaveGrowthsFeature.isNextToLava(level, mutable)) {
						for (int i = 0; i < 2; i++) {
							Direction direction = Direction.values()[random.nextInt(6)];
							if (level.getBlockState(mutable.relative(direction)).is(BlockTags.BASE_STONE_OVERWORLD)) {
								double noisevalue = noise.getValue(x, y, z);
								Block block = noisevalue < -0.55F ? CCBlocks.GRAINY_CAVE_GROWTHS.get() : noisevalue < -0.3F ? CCBlocks.ZESTY_CAVE_GROWTHS.get() : noisevalue < -0.0F ? CCBlocks.CAVE_GROWTHS.get() : noisevalue < 0.3F ? CCBlocks.LURID_CAVE_GROWTHS.get() : noisevalue < 0.55F ? CCBlocks.WISPY_CAVE_GROWTHS.get() : CCBlocks.WEIRD_CAVE_GROWTHS.get();
								level.setBlock(mutable, block.defaultBlockState().setValue(CaveGrowthsBlock.FACING, direction.getOpposite()), 2);
								placed = true;
								break;
							}
						}
					}
				}
			}
		}

		return placed;
	}
}