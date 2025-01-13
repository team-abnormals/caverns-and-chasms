package com.teamabnormals.caverns_and_chasms.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class CaveGrowthsFeature extends Feature<NoneFeatureConfiguration> {

	public CaveGrowthsFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		RandomSource random = context.random();
		BlockPos blockpos = context.origin();
		WorldGenLevel level = context.level();
		boolean placed = false;

		BlockState blockstate = CCBlocks.CAVE_GROWTHS.get().defaultBlockState();
		boolean isVariant = false;
		double variantChance = Mth.clamp((62.0D - blockpos.getY()) / 126.0D, 0.1D, 1.0D);

		if (random.nextFloat() < variantChance) {
			List<BlockState> possibleVariants = Lists.newArrayList();
			Holder<Biome> biome = level.getBiome(blockpos);

			if (blockpos.getY() < 0 || (biome.is(CCBiomeTags.HAS_GRAINY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_GRAINY_CAVE_GROWTHS)))
				possibleVariants.add(CCBlocks.GRAINY_CAVE_GROWTHS.get().defaultBlockState());

			if (biome.is(CCBiomeTags.HAS_ZESTY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_ZESTY_CAVE_GROWTHS))
				possibleVariants.add(CCBlocks.ZESTY_CAVE_GROWTHS.get().defaultBlockState());

			if (biome.is(CCBiomeTags.HAS_WISPY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_WISPY_CAVE_GROWTHS))
				possibleVariants.add(CCBlocks.WISPY_CAVE_GROWTHS.get().defaultBlockState());

			if (biome.is(CCBiomeTags.HAS_LURID_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_LURID_CAVE_GROWTHS))
				possibleVariants.add(CCBlocks.LURID_CAVE_GROWTHS.get().defaultBlockState());

			if (!possibleVariants.isEmpty()) {
				blockstate = possibleVariants.get(random.nextInt(possibleVariants.size()));
				isVariant = true;
			}
		}

		float sizeMultiplier = 1.0F;

		if ((isVariant && random.nextInt(12) == 0) || blockpos.getY() < 0)
			sizeMultiplier *= 1.5F;

		int tries = (int) (256 * sizeMultiplier * sizeMultiplier * sizeMultiplier);
		int xzRange = (int) (5 * sizeMultiplier);
		int yRange = (int) (3 * sizeMultiplier);

		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int i = 0; i < tries; ++i) {
			mutable.setWithOffset(blockpos, random.nextInt(xzRange) - random.nextInt(xzRange), random.nextInt(yRange) - random.nextInt(yRange), random.nextInt(xzRange) - random.nextInt(xzRange));
			if (level.isEmptyBlock(mutable) && level.getBlockState(mutable.below()).is(Tags.Blocks.STONE) && !isNextToLava(level, mutable)) {
				level.setBlock(mutable, blockstate, 2);
				placed = true;
			}
		}

		return placed;
	}

	private static boolean isNextToLava(WorldGenLevel level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				mutable.setWithOffset(pos, x, -1, z);
				if (level.getBlockState(mutable).is(Blocks.LAVA))
					return true;
			}
		}

		return false;
	}
}