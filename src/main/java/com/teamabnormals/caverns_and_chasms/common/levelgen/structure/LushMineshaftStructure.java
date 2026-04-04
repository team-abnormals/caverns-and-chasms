package com.teamabnormals.caverns_and_chasms.common.levelgen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class LushMineshaftStructure extends Structure {
	public static final Codec<LushMineshaftStructure> CODEC = RecordCodecBuilder.create((p_227971_) -> {
		return p_227971_.group(settingsCodec(p_227971_), LushMineshaftStructure.Type.CODEC.fieldOf("mineshaft_type").forGetter((p_227969_) -> {
			return p_227969_.type;
		})).apply(p_227971_, LushMineshaftStructure::new);
	});
	private final LushMineshaftStructure.Type type;

	public LushMineshaftStructure(StructureSettings settings, Type type) {
		super(settings);
		this.type = type;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext p_227964_) {
		p_227964_.random().nextDouble();
		ChunkPos chunkpos = p_227964_.chunkPos();
		BlockPos blockpos = new BlockPos(chunkpos.getMiddleBlockX(), 50, chunkpos.getMinBlockZ());
		StructurePiecesBuilder structurepiecesbuilder = new StructurePiecesBuilder();
		int i = this.generatePiecesAndAdjust(structurepiecesbuilder, p_227964_);
		return Optional.of(new Structure.GenerationStub(blockpos.offset(0, i, 0), Either.right(structurepiecesbuilder)));
	}

	private int generatePiecesAndAdjust(StructurePiecesBuilder p_227966_, Structure.GenerationContext p_227967_) {
		ChunkPos chunkpos = p_227967_.chunkPos();
		WorldgenRandom worldgenrandom = p_227967_.random();
		ChunkGenerator chunkgenerator = p_227967_.chunkGenerator();
		LushMineshaftPieces.MineShaftRoom mineshaftpieces$mineshaftroom = new LushMineshaftPieces.MineShaftRoom(0, worldgenrandom, chunkpos.getBlockX(2), chunkpos.getBlockZ(2), this.type);
		p_227966_.addPiece(mineshaftpieces$mineshaftroom);
		mineshaftpieces$mineshaftroom.addChildren(mineshaftpieces$mineshaftroom, p_227966_, worldgenrandom);
		int i = chunkgenerator.getSeaLevel();
		return p_227966_.moveBelowSeaLevel(i, chunkgenerator.getMinY(), worldgenrandom, 10);
	}

	public StructureType<?> type() {
		return CCStructureTypes.LUSH_MINESHAFT.get();
	}

	public enum Type implements StringRepresentable {
		LUSH("lush", () -> CCBlocks.AZALEA_LOG.get(), () -> CCBlocks.AZALEA_PLANKS.get(), () -> CCBlocks.AZALEA_FENCE.get());

		public static final Codec<LushMineshaftStructure.Type> CODEC = StringRepresentable.fromEnum(LushMineshaftStructure.Type::values);
		private static final IntFunction<LushMineshaftStructure.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
		private final String name;
		private final BlockState woodState;
		private final BlockState planksState;
		private final BlockState fenceState;

		private Type(String p_227985_, Supplier<Block> p_227986_, Supplier<Block> p_227987_, Supplier<Block> p_227988_) {
			this.name = p_227985_;
			this.woodState = p_227986_.get().defaultBlockState();
			this.planksState = p_227987_.get().defaultBlockState();
			this.fenceState = p_227988_.get().defaultBlockState();
		}

		public String getName() {
			return this.name;
		}

		public static LushMineshaftStructure.Type byId(int p_227991_) {
			return BY_ID.apply(p_227991_);
		}

		public BlockState getWoodState() {
			return this.woodState;
		}

		public BlockState getPlanksState() {
			return this.planksState;
		}

		public BlockState getFenceState() {
			return this.fenceState;
		}

		public String getSerializedName() {
			return this.name;
		}
	}
}