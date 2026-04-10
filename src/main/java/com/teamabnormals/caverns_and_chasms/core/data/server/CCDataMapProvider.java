package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.*;

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

		this.builder(NeoForgeDataMaps.FURNACE_FUELS)
				.add(CCBlocks.CHARCOAL_BLOCK.getId(), new FurnaceFuel(12800), false);

		this.builder(NeoForgeDataMaps.COMPOSTABLES)
				.add(CCBlocks.FALSE_HOPE.getId(), new Compostable(0.65F), false)
				.add(CCBlocks.MOSCHATEL.getId(), new Compostable(0.65F), false)
				.add(CCBlocks.CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.LURID_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.WISPY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.GRAINY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.WEIRD_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(CCBlocks.ZESTY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false);


		this.builder(NeoForgeDataMaps.OXIDIZABLES)
				.add(CCBlocks.COPPER_BARS, new Oxidizable(CCBlocks.EXPOSED_COPPER_BARS.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BARS, new Oxidizable(CCBlocks.WEATHERED_COPPER_BARS.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BARS, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BARS.get()), false)
				.add(CCBlocks.COPPER_BUTTON, new Oxidizable(CCBlocks.EXPOSED_COPPER_BUTTON.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BUTTON, new Oxidizable(CCBlocks.WEATHERED_COPPER_BUTTON.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BUTTON, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BUTTON.get()), false)
				.add(CCBlocks.TOOLBOX, new Oxidizable(CCBlocks.EXPOSED_TOOLBOX.get()), false)
				.add(CCBlocks.EXPOSED_TOOLBOX, new Oxidizable(CCBlocks.WEATHERED_TOOLBOX.get()), false)
				.add(CCBlocks.WEATHERED_TOOLBOX, new Oxidizable(CCBlocks.OXIDIZED_TOOLBOX.get()), false)
				.add(Holder.direct(Blocks.LIGHTNING_ROD), new Oxidizable(CCBlocks.EXPOSED_LIGHTNING_ROD.get()), false)
				.add(CCBlocks.EXPOSED_LIGHTNING_ROD, new Oxidizable(CCBlocks.WEATHERED_LIGHTNING_ROD.get()), false)
				.add(CCBlocks.WEATHERED_LIGHTNING_ROD, new Oxidizable(CCBlocks.OXIDIZED_LIGHTNING_ROD.get()), false)
				.add(CCBlocks.FLOODLIGHT, new Oxidizable(CCBlocks.EXPOSED_FLOODLIGHT.get()), false)
				.add(CCBlocks.EXPOSED_FLOODLIGHT, new Oxidizable(CCBlocks.WEATHERED_FLOODLIGHT.get()), false)
				.add(CCBlocks.WEATHERED_FLOODLIGHT, new Oxidizable(CCBlocks.OXIDIZED_FLOODLIGHT.get()), false)
				.add(CCBlocks.COPPER_RAIL, new Oxidizable(CCBlocks.EXPOSED_COPPER_RAIL.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_RAIL, new Oxidizable(CCBlocks.WEATHERED_COPPER_RAIL.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_RAIL, new Oxidizable(CCBlocks.OXIDIZED_COPPER_RAIL.get()), false)
				.add(CCBlocks.COPPER_BRICKS, new Oxidizable(CCBlocks.EXPOSED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BRICKS, new Oxidizable(CCBlocks.WEATHERED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BRICKS, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.COPPER_BRICK_STAIRS, new Oxidizable(CCBlocks.EXPOSED_COPPER_BRICK_STAIRS.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BRICK_STAIRS, new Oxidizable(CCBlocks.WEATHERED_COPPER_BRICK_STAIRS.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BRICK_STAIRS, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BRICK_STAIRS.get()), false)
				.add(CCBlocks.COPPER_BRICK_SLAB, new Oxidizable(CCBlocks.EXPOSED_COPPER_BRICK_SLAB.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BRICK_SLAB, new Oxidizable(CCBlocks.WEATHERED_COPPER_BRICK_SLAB.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BRICK_SLAB, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BRICK_SLAB.get()), false)
				.add(CCBlocks.COPPER_BRICK_WALL, new Oxidizable(CCBlocks.EXPOSED_COPPER_BRICK_WALL.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_BRICK_WALL, new Oxidizable(CCBlocks.WEATHERED_COPPER_BRICK_WALL.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_BRICK_WALL, new Oxidizable(CCBlocks.OXIDIZED_COPPER_BRICK_WALL.get()), false)
				.add(CCBlocks.CHISELED_COPPER_BRICKS, new Oxidizable(CCBlocks.EXPOSED_CHISELED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.EXPOSED_CHISELED_COPPER_BRICKS, new Oxidizable(CCBlocks.WEATHERED_CHISELED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.WEATHERED_CHISELED_COPPER_BRICKS, new Oxidizable(CCBlocks.OXIDIZED_CHISELED_COPPER_BRICKS.get()), false)
				.add(CCBlocks.COPPER_INGOT, new Oxidizable(CCBlocks.EXPOSED_COPPER_INGOT.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_INGOT, new Oxidizable(CCBlocks.WEATHERED_COPPER_INGOT.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_INGOT, new Oxidizable(CCBlocks.OXIDIZED_COPPER_INGOT.get()), false)
				.add(CCBlocks.COPPER_CHAIN, new Oxidizable(CCBlocks.EXPOSED_COPPER_CHAIN.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_CHAIN, new Oxidizable(CCBlocks.WEATHERED_COPPER_CHAIN.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_CHAIN, new Oxidizable(CCBlocks.OXIDIZED_COPPER_CHAIN.get()), false)
				.add(CCBlocks.COPPER_LANTERN, new Oxidizable(CCBlocks.EXPOSED_COPPER_LANTERN.get()), false)
				.add(CCBlocks.EXPOSED_COPPER_LANTERN, new Oxidizable(CCBlocks.WEATHERED_COPPER_LANTERN.get()), false)
				.add(CCBlocks.WEATHERED_COPPER_LANTERN, new Oxidizable(CCBlocks.OXIDIZED_COPPER_LANTERN.get()), false);
	}
}