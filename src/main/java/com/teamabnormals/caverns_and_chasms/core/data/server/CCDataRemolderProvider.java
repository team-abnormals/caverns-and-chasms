package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.blueprint.common.remolder.data.RemolderProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;

import java.util.concurrent.CompletableFuture;

public class CCDataRemolderProvider extends RemolderProvider {

	public CCDataRemolderProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, Target.DATA_PACK, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {
//		HolderGetter<Structure> structures = provider.lookupOrThrow(Registries.STRUCTURE);
//		this.entry("worldgen/structure_set/mineshafts")
//				.path("worldgen/structure_set/mineshafts")
//				.remolder(add(target("structures[]"), value(
//						StructureSet.entry(structures.getOrThrow(CCStructures.MINESHAFT_LUSH), 1), StructureSelectionEntry.CODEC)
//				));
	}
}