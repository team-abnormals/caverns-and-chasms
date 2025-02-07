package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithPieces.TinMonolithPiece;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.ContextlessType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CCStructureTypes {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<StructureType<TinMonolithStructure>> TIN_MONOLITH_TYPE = STRUCTURE_TYPES.register("tin_monolith", () -> () -> TinMonolithStructure.CODEC);

	public static class CCStructurePieceTypes {
		public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, CavernsAndChasms.MOD_ID);

		public static final RegistryObject<ContextlessType> TIN_MONOLITH = STRUCTURE_PIECE_TYPES.register("tin_monolith", () -> TinMonolithPiece::new);
	}

	public static class CCTemplatePools {
		public static final ResourceKey<StructureTemplatePool> FORGE = createKey("forge/forge");
		public static final ResourceKey<StructureTemplatePool> FORGE_ENTRANCES = createKey("forge/entrances");
		public static final ResourceKey<StructureTemplatePool> FORGE_DECORATIONS = createKey("forge/decorations");
		public static final ResourceKey<StructureTemplatePool> FORGE_ARCHAEOLOGY = createKey("forge/archaeology");

		public static final String[] ENTRANCES = new String[]{"gate_1", "gate_2"};
		public static final String[] DECORATIONS = new String[]{"oak_platform_1", "oak_shelf_1"};
		public static final String[] ARCHAEOLOGY = new String[]{"gravel_pile_1"};

		public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
			Holder<StructureTemplatePool> empty = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);

			context.register(FORGE, new StructureTemplatePool(empty, ImmutableList.of(Pair.of(LegacySinglePoolElement.single(FORGE.location().toString()), 1)), StructureTemplatePool.Projection.RIGID));

			createPool(context, FORGE_ENTRANCES, empty, ENTRANCES);
			createPool(context, FORGE_DECORATIONS, empty, DECORATIONS);
			createPool(context, FORGE_ARCHAEOLOGY, empty, ARCHAEOLOGY);
		}

		public static void createPool(BootstapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, Holder<StructureTemplatePool> empty, String... strs) {
			List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> list = Lists.newArrayList();
			for (String str : strs) {
				list.add(Pair.of(LegacySinglePoolElement.single(key.location() + "/" + str), 1));
			}

			context.register(key, new StructureTemplatePool(empty, ImmutableList.copyOf(list), StructureTemplatePool.Projection.RIGID));
		}

		public static ResourceKey<StructureTemplatePool> createKey(String name) {
			return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}

	public static class CCStructures {
		public static final ResourceKey<Structure> TIN_MONOLITH = createKey("tin_monolith");
		public static final ResourceKey<Structure> FORGE = createKey("forge");

		public static void bootstrap(BootstapContext<Structure> context) {
			HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
			HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

			context.register(FORGE, new JigsawStructure(
					new StructureSettings(
							biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
							Map.of(),
							Decoration.UNDERGROUND_STRUCTURES,
							TerrainAdjustment.NONE),
					pools.getOrThrow(CCTemplatePools.FORGE), 6, UniformHeight.of(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(32)), false));

			context.register(TIN_MONOLITH, new TinMonolithStructure(new StructureSettings(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));
		}

		public static ResourceKey<Structure> createKey(String name) {
			return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}

	public static class CCStructureSets {
		public static final ResourceKey<StructureSet> FORGES = createKey("forges");
		public static final ResourceKey<StructureSet> TIN_MONOLITHS = createKey("tin_monoliths");

		public static void bootstrap(BootstapContext<StructureSet> context) {
			HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

			context.register(FORGES, new StructureSet(structures.getOrThrow(CCStructures.FORGE), new RandomSpreadStructurePlacement(24, 4, RandomSpreadType.LINEAR, 294502589)));
			context.register(TIN_MONOLITHS, new StructureSet(structures.getOrThrow(CCStructures.TIN_MONOLITH), new RandomSpreadStructurePlacement(TinMonolithStructure.SPACING, TinMonolithStructure.SEPARATION, RandomSpreadType.TRIANGULAR, TinMonolithStructure.SALT)));
		}

		public static ResourceKey<StructureSet> createKey(String name) {
			return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}
}