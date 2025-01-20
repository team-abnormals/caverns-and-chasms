package com.teamabnormals.caverns_and_chasms.common.levelgen.structure;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithPieces.TinMonolithPiece;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class TinMonolithStructure extends Structure {
	public static final int SPACING = 96;
	public static final int SEPARATION = 32;
	public static final int SALT = 47621501;

	public static final Codec<TinMonolithStructure> CODEC = simpleCodec(TinMonolithStructure::new);

	public TinMonolithStructure(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 64, chunkpos.getMinBlockZ());
		return Optional.of(new Structure.GenerationStub(blockpos, (builder) -> {
			generatePieces(builder, context);
		}));
	}

	private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		builder.addPiece(new TinMonolithPiece(chunkpos.getMinBlockX(), chunkpos.getMinBlockZ()));
	}

	public StructureType<?> type() {
		return CCStructureTypes.TIN_MONOLITH_TYPE.get();
	}
}