package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithPieces.TinMonolithPiece;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.TinMonolithStructure;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCLootTableProvider.CCArchaeologyLoot;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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

	public static class CCProcessorLists {
		public static final ResourceKey<StructureProcessorList> FORGE_ARCHAEOLOGY = createKey("forge_archaeology");

		public static void bootstrap(BootstapContext<StructureProcessorList> context) {
			float legendary = 0.001F;
			float epic = 0.002F;
			float rare = 0.01F;
			float uncommon = 0.02F;
			float common = 0.04F;

			register(context, FORGE_ARCHAEOLOGY, ImmutableList.of(
					new RuleProcessor(ImmutableList.of(
							replaceGravelWith(CCBlocks.TURQUOISE_ORE.get(), legendary),

							replaceGravelWith(Blocks.IRON_BLOCK, epic),
							replaceGravelWith(Blocks.RAW_IRON_BLOCK, epic),
							replaceGravelWith(Blocks.COAL_BLOCK, epic),
							replaceGravelWith(Blocks.FURNACE, epic),
							replaceGravelWith(Blocks.BLAST_FURNACE, epic),

							replaceGravelWith(Blocks.INFESTED_STONE, rare),
							replaceGravelWith(CCBlocks.FLINT_BLOCK.get(), rare),
							archyLootProcessor(CCArchaeologyLoot.FORGE_RARE, rare),

							replaceGravelWith(Blocks.IRON_ORE, uncommon),
							replaceGravelWith(Blocks.COBBLESTONE, uncommon),
							replaceGravelWith(Blocks.STONE, uncommon),

							replaceGravelWith(Blocks.COAL_ORE, common),
							replaceGravelWith(CCBlocks.FRAGILE_STONE.get(), common),
							archyLootProcessor(CCArchaeologyLoot.FORGE_COMMON, common)
					)),

					archyLootProcessor(CCArchaeologyLoot.FORGE_RARE, 1),
					archyLootProcessor(CCArchaeologyLoot.FORGE_COMMON, 5)
			));
		}

		private static ProcessorRule replaceGravelWith(Block block, float chance) {
			return new ProcessorRule(new RandomBlockMatchTest(Blocks.GRAVEL, chance), AlwaysTrueTest.INSTANCE, block.defaultBlockState());
		}

		private static ProcessorRule archyLootProcessor(ResourceLocation lootTable, float chance) {
			return new ProcessorRule(new RandomBlockMatchTest(Blocks.GRAVEL, chance), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), new AppendLoot(lootTable));
		}

		private static CappedProcessor archyLootProcessor(ResourceLocation lootTable, int max) {
			return new CappedProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.GRAVEL), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), new AppendLoot(lootTable)))), ConstantInt.of(max));
		}

		private static ResourceKey<StructureProcessorList> createKey(String name) {
			return ResourceKey.create(Registries.PROCESSOR_LIST, CavernsAndChasms.location(name));
		}

		private static void register(BootstapContext<StructureProcessorList> context, ResourceKey<StructureProcessorList> key, List<StructureProcessor> processors) {
			context.register(key, new StructureProcessorList(processors));
		}
	}

	public static class CCTemplatePools {
		public static final ResourceKey<StructureTemplatePool> FORGE = createKey("forge/forge");
		public static final ResourceKey<StructureTemplatePool> FORGE_ENTRANCES = createKey("forge/entrances");
		public static final ResourceKey<StructureTemplatePool> FORGE_ARCHAEOLOGY = createKey("forge/archaeology");
		public static final ResourceKey<StructureTemplatePool> FORGE_DECORATIONS = createKey("forge/decorations");
		public static final ResourceKey<StructureTemplatePool> FORGE_SMALL_DECORATIONS = createKey("forge/small_decorations");
		public static final ResourceKey<StructureTemplatePool> FORGE_PILE_DECORATIONS = createKey("forge/pile_decorations");

		public static final List<Entry> ENTRANCES = List.of(of("gate", 6), of("broken_gate", 4));
		public static final List<Entry> ARCHAEOLOGY = List.of(of("gravel_pile", 32));
		public static final List<Entry> DECORATIONS = List.of(of("oak_platform", 8), of("oak_shelf", 2), of("tnt_pile", 3));
		public static final List<Entry> SMALL_DECORATIONS = List.of(of("empty", 1, 17), of("cauldron", 1), of("furnace", 1), of("blast_furnace", 1), of("damaged_anvil", 1), of("dimmer", 1), of("dimmer_scaffolding", 1), of("anvil", 1), of("tnt", 2), of("tnt_scaffolding", 2), of("water_cauldron", 3), of("scaffolding", 4));
		public static final List<Entry> PILE_DECORATIONS = List.of(of("empty", 1, 150), of("stone_button", 1, 60), of("mushroom", 2, 3), of("cave_growths", 6, 1), of("candle", 4, 4), of("coal", 4, 5), of("charcoal", 4, 4), of("toolbox", 1, 8));

		public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
			Holder<StructureTemplatePool> empty = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);

			context.register(FORGE, new StructureTemplatePool(empty, ImmutableList.of(Pair.of(LegacySinglePoolElement.single(FORGE.location().toString()), 1)), StructureTemplatePool.Projection.RIGID));

			createPool(context, FORGE_ENTRANCES, empty, ENTRANCES);
			createPool(context, FORGE_ARCHAEOLOGY, empty, ARCHAEOLOGY);
			createPool(context, FORGE_DECORATIONS, empty, DECORATIONS);
			createPool(context, FORGE_SMALL_DECORATIONS, empty, SMALL_DECORATIONS);
			createPool(context, FORGE_PILE_DECORATIONS, empty, PILE_DECORATIONS);
		}

		public static void createPool(BootstapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, Holder<StructureTemplatePool> empty, List<Entry> strs) {
			boolean processor = FORGE_ARCHAEOLOGY.equals(key);
			Reference<StructureProcessorList> archyProcessor = context.lookup(Registries.PROCESSOR_LIST).getOrThrow(CCProcessorLists.FORGE_ARCHAEOLOGY);

			List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> list = Lists.newArrayList();
			for (Entry entry : strs) {
				if (entry.name().equals("empty")) {
					list.add(Pair.of(EmptyPoolElement.empty(), entry.weight()));
				} else {
					for (int i = 1; i <= entry.count(); i++) {
						String name = key.location() + "/" + entry.name() + "_" + i;
						list.add(Pair.of(processor ? LegacySinglePoolElement.single(name, archyProcessor) : LegacySinglePoolElement.single(name), entry.weight()));
					}
				}
			}

			context.register(key, new StructureTemplatePool(empty, ImmutableList.copyOf(list), StructureTemplatePool.Projection.RIGID));
		}

		public static Entry of(String name, int count, int weight) {
			return new Entry(name, count, weight);
		}

		public static Entry of(String name, int count) {
			return new Entry(name, count);
		}

		public record Entry(String name, int count, int weight) {

			public Entry(String name, int count) {
				this(name, count, 1);
			}
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
					new StructureSettings(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), Map.of(), Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.BEARD_THIN),
					pools.getOrThrow(CCTemplatePools.FORGE), 6, UniformHeight.of(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(16)), false));

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

			context.register(FORGES, new StructureSet(structures.getOrThrow(CCStructures.FORGE), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 294502589)));
			context.register(TIN_MONOLITHS, new StructureSet(structures.getOrThrow(CCStructures.TIN_MONOLITH), new RandomSpreadStructurePlacement(TinMonolithStructure.SPACING, TinMonolithStructure.SEPARATION, RandomSpreadType.TRIANGULAR, TinMonolithStructure.SALT)));
		}

		public static ResourceKey<StructureSet> createKey(String name) {
			return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
		}
	}
}