package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.blueprint.common.world.modification.structure.SimpleStructureRepaletter;
import com.teamabnormals.blueprint.common.world.modification.structure.SimpleTagStructureRepaletter;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepaletterEntry;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepaletterManager;
import com.teamabnormals.blueprint.common.world.modification.structure.condition.BiomeStructureCondition;
import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import com.teamabnormals.caverns_and_chasms.common.block.IngotLayer;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.ChanceStructureRepaletter;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCConditionSerializers.CCConditions;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CCStructureRepaletters {
	public static final ResourceKey<StructureRepaletterEntry> BASTION_ADDITIONS = create("bastion_additions");
	public static final ResourceKey<StructureRepaletterEntry> LUSH_FORGES = create("lush_forges");
	public static final ResourceKey<StructureRepaletterEntry> TRIAL_CHAMBERS = create("trial_chambers");
	public static final ResourceKey<StructureRepaletterEntry> TRIAL_CHAMBERS_MISC = create("trial_chambers_misc");

	public static void bootstrap(BootstrapContext<StructureRepaletterEntry> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

		context.register(BASTION_ADDITIONS, new StructureRepaletterEntry.Builder().repaletters(
						new ChanceStructureRepaletter(Blocks.LANTERN, CCBlocks.LAVA_LAMP.get().defaultBlockState(), 0.2F),
						new ChanceStructureRepaletter(Blocks.GOLD_BLOCK, CCBlocks.GOLD_INGOT.get().defaultBlockState().setValue(IngotBlock.LAYERS, 3).setValue(IngotBlock.TOP_INGOT, IngotLayer.BOTH), 0.4F))
				.select(HolderSet.direct(Stream.of(BuiltinStructures.BASTION_REMNANT).map(structures::getOrThrow).collect(Collectors.toList()))));

		context.register(LUSH_FORGES, new StructureRepaletterEntry.Builder().repaletters(
						new ChanceStructureRepaletter(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.6F),
						new ChanceStructureRepaletter(CCBlocks.COBBLESTONE_BRICKS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICKS.get().defaultBlockState(), 0.6F),
						new ChanceStructureRepaletter(CCBlocks.COBBLESTONE_TILES.get(), CCBlocks.MOSSY_COBBLESTONE_TILES.get().defaultBlockState(), 0.6F))
				.condition(new BiomeStructureCondition(HolderSet.direct(context.lookup(Registries.BIOME).getOrThrow(Biomes.LUSH_CAVES))))
				.select(HolderSet.direct(Stream.of(CCStructures.FORGE).map(structures::getOrThrow).collect(Collectors.toList()))));

		context.register(TRIAL_CHAMBERS, new StructureRepaletterEntry.Builder().repaletters(
						new SimpleTagStructureRepaletter(CCBlockTags.STORAGE_BLOCKS_ALL_COPPER, CCBlocks.SCHIST_PILLAR.get()))
				.select(HolderSet.direct(Stream.of(BuiltinStructures.TRIAL_CHAMBERS).map(structures::getOrThrow).collect(Collectors.toList()))));

		context.register(TRIAL_CHAMBERS_MISC, new StructureRepaletterEntry.Builder().repaletters(
						new SimpleStructureRepaletter(Blocks.OAK_BUTTON, CCBlocks.WAXED_COPPER_BUTTON.get()),
						new SimpleStructureRepaletter(Blocks.OAK_PRESSURE_PLATE, CCBlocks.WAXED_COPPER_PRESSURE_PLATE.get()))
				.select(HolderSet.direct(Stream.of(BuiltinStructures.TRIAL_CHAMBERS).map(structures::getOrThrow).collect(Collectors.toList()))));
	}

	public static void applyConditions(BiConsumer<ResourceKey<?>, ICondition> builder) {
		builder.accept(TRIAL_CHAMBERS, CCConditions.TRIAL_CHAMBERS_REPALETTE);
	}

	private static ResourceKey<StructureRepaletterEntry> create(String name) {
		return ResourceKey.create(BlueprintDataPackRegistries.STRUCTURE_REPALETTERS, CavernsAndChasms.location(name));
	}

	public static void registerRepaletters() {
		StructureRepaletterManager.registerRepalleter(CavernsAndChasms.location("chance"), ChanceStructureRepaletter.CODEC, ChanceStructureRepaletter.CODEC);
	}
}