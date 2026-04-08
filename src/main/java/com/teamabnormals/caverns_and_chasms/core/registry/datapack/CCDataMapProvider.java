package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;
import net.neoforged.neoforge.registries.datamaps.builtin.VibrationFrequency;

import java.util.concurrent.CompletableFuture;

public class CCDataMapProvider extends DataMapProvider {

	public CCDataMapProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void gather(Provider provider) {
		this.builder(NeoForgeDataMaps.PARROT_IMITATIONS)
				.add(CCEntityTypes.DEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_DEEPER.get()), false)
				.add(CCEntityTypes.EVENDEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_EVENDEEPER.get()), false)
				.add(CCEntityTypes.PEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_PEEPER.get()), false)
				.add(CCEntityTypes.MIME, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_MIME.get()), false)
				.add(CCEntityTypes.GRAZER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_GRAZER.get()), false)
				.add(CCEntityTypes.SADDLED_GRAZER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_GRAZER.get()), false);

		this.builder(NeoForgeDataMaps.VIBRATION_FREQUENCIES)
				.add(CCGameEvents.TUNING_FORK_VIBRATE, new VibrationFrequency(10), false);

		this.builder(NeoForgeDataMaps.FURNACE_FUELS);

		this.builder(NeoForgeDataMaps.COMPOSTABLES)
				.add(CCBlocks.FALSE_HOPE.getId(), new Compostable(0.65F), false)
				.add(CCBlocks.MOSCHATEL.getId(), new Compostable(0.65F), false)
				.add(CCBlocks.CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.LURID_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.WISPY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.GRAINY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.WEIRD_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.ZESTY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false);
	}
}