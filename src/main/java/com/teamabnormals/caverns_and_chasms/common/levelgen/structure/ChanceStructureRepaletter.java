package com.teamabnormals.caverns_and_chasms.common.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureModificationContext;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepaletter;
import com.teamabnormals.blueprint.core.util.BlockUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public record ChanceStructureRepaletter(Block replacesBlock, BlockState replacesWith, float chance) implements StructureRepaletter, StructureRepaletter.Replacer {
	public static final MapCodec<ChanceStructureRepaletter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replaces_block").forGetter(repaletter -> repaletter.replacesBlock),
			BlockState.CODEC.fieldOf("replaces_with").forGetter(repaletter -> repaletter.replacesWith),
			Codec.FLOAT.fieldOf("chance").forGetter(repaletter -> repaletter.chance)
	).apply(instance, ChanceStructureRepaletter::new));

	@Override
	public Replacer createReplacer(StructureModificationContext context) {
		return this;
	}

	@Nullable
	@Override
	public BlockState getReplacement(ServerLevelAccessor level, BlockState state, RandomSource random) {
		return state.is(this.replacesBlock) && level.getRandom().nextFloat() < this.chance ? BlockUtil.transferAllBlockStates(state, this.replacesWith) : null;
	}

	@Override
	public MapCodec<? extends StructureRepaletter> codec() {
		return CODEC;
	}

	@Override
	public MapCodec<? extends Replacer> savedTagCodec() {
		return CODEC;
	}
}