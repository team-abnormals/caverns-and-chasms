package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepaletterEntry;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepalleterManager;
import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import com.teamabnormals.caverns_and_chasms.common.block.IngotLayer;
import com.teamabnormals.caverns_and_chasms.common.levelgen.structure.ChanceStructureRepaletter;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CCStructureRepaletters {

	public static void bootstrap(BootstapContext<StructureRepaletterEntry> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

		context.register(create("lava_lamps_in_bastions"), new StructureRepaletterEntry(
				HolderSet.direct(Stream.of(BuiltinStructures.BASTION_REMNANT).map(structures::getOrThrow).collect(Collectors.toList())),
				Optional.empty(), false, new ChanceStructureRepaletter(Blocks.LANTERN, CCBlocks.LAVA_LAMP.get().defaultBlockState(), 0.2F))
		);

		context.register(create("gold_ingots_in_bastions"), new StructureRepaletterEntry(
				HolderSet.direct(Stream.of(BuiltinStructures.BASTION_REMNANT).map(structures::getOrThrow).collect(Collectors.toList())),
				Optional.empty(), false, new ChanceStructureRepaletter(Blocks.GOLD_BLOCK, CCBlocks.GOLD_INGOT.get().defaultBlockState()
				.setValue(IngotBlock.LAYERS, 3)
				.setValue(IngotBlock.TOP_INGOT, IngotLayer.BOTH), 0.4F))
		);
	}

	private static ResourceKey<StructureRepaletterEntry> create(String name) {
		return ResourceKey.create(BlueprintDataPackRegistries.STRUCTURE_REPALETTERS, CavernsAndChasms.location(name));
	}

	public static void registerRepaletters() {
		StructureRepalleterManager.registerSerializer(CavernsAndChasms.location("chance"), ChanceStructureRepaletter.CODEC);
	}
}