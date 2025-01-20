package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithPieces.TinMonolithPiece;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.ContextlessType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class CCStructureTypes {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<StructureType<TinMonolithStructure>> TIN_MONOLITH_TYPE = STRUCTURE_TYPES.register("tin_monolith", () -> () -> TinMonolithStructure.CODEC);

	public static class CCStructurePieceTypes {
		public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<ContextlessType> TIN_MONOLITH = STRUCTURE_PIECE_TYPES.register("tin_monolith", () -> TinMonolithPiece::new);
	}

	public static class CCStructures {
		public static final ResourceKey<Structure> TIN_MONOLITH = createKey("tin_monolith");

		public static void bootstrap(BootstapContext<Structure> context) {
			HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

			context.register(TIN_MONOLITH, new TinMonolithStructure(new Structure.StructureSettings(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));
		}

		public static ResourceKey<Structure> createKey(String name) {
			return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}

	public static class CCStructureSets {
		public static final ResourceKey<StructureSet> TIN_MONOLITHs = createKey("tin_monoliths");

		public static void bootstrap(BootstapContext<StructureSet> context) {
			HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

			context.register(TIN_MONOLITHs, new StructureSet(structures.getOrThrow(CCStructures.TIN_MONOLITH), new RandomSpreadStructurePlacement(TinMonolithStructure.SPACING, TinMonolithStructure.SEPARATION, RandomSpreadType.LINEAR, TinMonolithStructure.SALT)));
		}

		public static ResourceKey<StructureSet> createKey(String name) {
			return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}
}