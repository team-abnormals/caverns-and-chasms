package com.teamabnormals.caverns_and_chasms.common.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class NoiseBasedRarityFilter extends PlacementFilter {
	public static final Codec<NoiseBasedRarityFilter> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter(placement -> placement.noiseParameters),
				Codec.FLOAT.fieldOf("noise_to_chance_ratio").forGetter((placement) -> placement.noiseToChanceRatio),
				Codec.DOUBLE.fieldOf("noise_offset").orElse(0.0D).forGetter((placement) -> placement.noiseOffset)
		).apply(instance, NoiseBasedRarityFilter::new);
	});
	private final Holder<NoiseParameters> noiseParameters;
	private final float noiseToChanceRatio;
	private final double noiseOffset;
	private volatile boolean initialized;
	private NormalNoise noise;

	public NoiseBasedRarityFilter(Holder<NormalNoise.NoiseParameters> noiseParameters, float noiseToChanceRatio, double noiseOffset) {
		this.noiseParameters = noiseParameters;
		this.noiseToChanceRatio = noiseToChanceRatio;
		this.noiseOffset = noiseOffset;
	}

	protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
		if (!this.initialized) {
			synchronized (this) {
				if (!this.initialized) {
					this.noise = NormalNoise.create(WorldgenRandom.Algorithm.LEGACY.newInstance(context.getLevel().getSeed()).forkPositional().fromHashOf(this.noiseParameters.unwrapKey().orElseThrow().location()), this.noiseParameters.value());
					this.initialized = true;
				}
			}
		}
		return random.nextFloat() < (float) (this.noise.getValue(pos.getX(), 0.0F, pos.getZ()) + this.noiseOffset) * this.noiseToChanceRatio;
	}

	public PlacementModifierType<?> type() {
		return CCPlacementModifierTypes.NOISE_BASED_RARITY_FILTER.get();
	}
}