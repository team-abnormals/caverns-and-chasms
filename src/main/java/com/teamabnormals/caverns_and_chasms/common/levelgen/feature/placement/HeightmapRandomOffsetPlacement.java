package com.teamabnormals.caverns_and_chasms.common.levelgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class HeightmapRandomOffsetPlacement extends PlacementModifier {
	public static final MapCodec<HeightmapRandomOffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
				Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(placement -> placement.heightmap),
				Codec.INT.fieldOf("min_offset").forGetter(placement -> placement.minOffset),
				Codec.INT.fieldOf("max_offset").forGetter(placement -> placement.maxOffset)
		).apply(instance, HeightmapRandomOffsetPlacement::new);
	});
	private final Heightmap.Types heightmap;
	private final int minOffset;
	private final int maxOffset;

	private HeightmapRandomOffsetPlacement(Heightmap.Types heightMapType, int minOffset, int maxOffset) {
		this.heightmap = heightMapType;
		this.minOffset = minOffset;
		this.maxOffset = maxOffset;
	}

	public static HeightmapRandomOffsetPlacement of(Heightmap.Types heightMapType, int minOffset, int maxOffset) {
		return new HeightmapRandomOffsetPlacement(heightMapType, minOffset, maxOffset);
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		int i = pos.getX();
		int j = pos.getZ();
		int k = context.getHeight(this.heightmap, i, j) + random.nextInt(this.maxOffset - this.minOffset + 1) + this.minOffset;
		return k > context.getMinBuildHeight() ? Stream.of(new BlockPos(i, k, j)) : Stream.of();
	}

	@Override
	public PlacementModifierType<?> type() {
		return CCPlacementModifierTypes.HEIGHTMAP_RANDOM_OFFSET.get();
	}
}