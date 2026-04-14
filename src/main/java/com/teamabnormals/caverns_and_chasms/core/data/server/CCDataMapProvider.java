package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import com.teamabnormals.caverns_and_chasms.core.other.CCLootTables;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.*;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;

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

		this.builder(CCDataMaps.TRIAL_TOKENS)
				.add(CCItems.TRIAL_TOKEN, new TrialToken(new ItemStack(Items.TRIAL_KEY), CCLootTables.SPAWNER_TRIAL_CHAMBER_TOKEN, BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES), false)
				.add(CCItems.OMINOUS_TRIAL_TOKEN, new TrialToken(new ItemStack(Items.OMINOUS_TRIAL_KEY), CCLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_TOKEN, BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES), false);

		this.builder(NeoForgeDataMaps.VIBRATION_FREQUENCIES)
				.add(CCGameEvents.TUNING_FORK_VIBRATE, new VibrationFrequency(10), false);

		this.builder(NeoForgeDataMaps.FURNACE_FUELS)
				.add(CHARCOAL_BLOCK.getId(), new FurnaceFuel(12800), false);

		this.builder(NeoForgeDataMaps.COMPOSTABLES)
				.add(FALSE_HOPE.getId(), new Compostable(0.65F), false)
				.add(MOSCHATEL.getId(), new Compostable(0.65F), false)
				.add(CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(LURID_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(WISPY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(GRAINY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(WEIRD_CAVE_GROWTHS.getId(), new Compostable(0.30F), false)
				.add(ZESTY_CAVE_GROWTHS.getId(), new Compostable(0.30F), false);

		this.builder(NeoForgeDataMaps.OXIDIZABLES)
				.add(COPPER_BARS, new Oxidizable(EXPOSED_COPPER_BARS.get()), false)
				.add(EXPOSED_COPPER_BARS, new Oxidizable(WEATHERED_COPPER_BARS.get()), false)
				.add(WEATHERED_COPPER_BARS, new Oxidizable(OXIDIZED_COPPER_BARS.get()), false)
				.add(COPPER_BUTTON, new Oxidizable(EXPOSED_COPPER_BUTTON.get()), false)
				.add(EXPOSED_COPPER_BUTTON, new Oxidizable(WEATHERED_COPPER_BUTTON.get()), false)
				.add(WEATHERED_COPPER_BUTTON, new Oxidizable(OXIDIZED_COPPER_BUTTON.get()), false)
				.add(TOOLBOX, new Oxidizable(EXPOSED_TOOLBOX.get()), false)
				.add(EXPOSED_TOOLBOX, new Oxidizable(WEATHERED_TOOLBOX.get()), false)
				.add(WEATHERED_TOOLBOX, new Oxidizable(OXIDIZED_TOOLBOX.get()), false)
				.add(Blocks.LIGHTNING_ROD.builtInRegistryHolder(), new Oxidizable(EXPOSED_LIGHTNING_ROD.get()), false)
				.add(EXPOSED_LIGHTNING_ROD, new Oxidizable(WEATHERED_LIGHTNING_ROD.get()), false)
				.add(WEATHERED_LIGHTNING_ROD, new Oxidizable(OXIDIZED_LIGHTNING_ROD.get()), false)
				.add(FLOODLIGHT, new Oxidizable(EXPOSED_FLOODLIGHT.get()), false)
				.add(EXPOSED_FLOODLIGHT, new Oxidizable(WEATHERED_FLOODLIGHT.get()), false)
				.add(WEATHERED_FLOODLIGHT, new Oxidizable(OXIDIZED_FLOODLIGHT.get()), false)
				.add(COPPER_RAIL, new Oxidizable(EXPOSED_COPPER_RAIL.get()), false)
				.add(EXPOSED_COPPER_RAIL, new Oxidizable(WEATHERED_COPPER_RAIL.get()), false)
				.add(WEATHERED_COPPER_RAIL, new Oxidizable(OXIDIZED_COPPER_RAIL.get()), false)
				.add(COPPER_BRICKS, new Oxidizable(EXPOSED_COPPER_BRICKS.get()), false)
				.add(EXPOSED_COPPER_BRICKS, new Oxidizable(WEATHERED_COPPER_BRICKS.get()), false)
				.add(WEATHERED_COPPER_BRICKS, new Oxidizable(OXIDIZED_COPPER_BRICKS.get()), false)
				.add(COPPER_BRICK_STAIRS, new Oxidizable(EXPOSED_COPPER_BRICK_STAIRS.get()), false)
				.add(EXPOSED_COPPER_BRICK_STAIRS, new Oxidizable(WEATHERED_COPPER_BRICK_STAIRS.get()), false)
				.add(WEATHERED_COPPER_BRICK_STAIRS, new Oxidizable(OXIDIZED_COPPER_BRICK_STAIRS.get()), false)
				.add(COPPER_BRICK_SLAB, new Oxidizable(EXPOSED_COPPER_BRICK_SLAB.get()), false)
				.add(EXPOSED_COPPER_BRICK_SLAB, new Oxidizable(WEATHERED_COPPER_BRICK_SLAB.get()), false)
				.add(WEATHERED_COPPER_BRICK_SLAB, new Oxidizable(OXIDIZED_COPPER_BRICK_SLAB.get()), false)
				.add(COPPER_BRICK_WALL, new Oxidizable(EXPOSED_COPPER_BRICK_WALL.get()), false)
				.add(EXPOSED_COPPER_BRICK_WALL, new Oxidizable(WEATHERED_COPPER_BRICK_WALL.get()), false)
				.add(WEATHERED_COPPER_BRICK_WALL, new Oxidizable(OXIDIZED_COPPER_BRICK_WALL.get()), false)
				.add(CHISELED_COPPER_BRICKS, new Oxidizable(EXPOSED_CHISELED_COPPER_BRICKS.get()), false)
				.add(EXPOSED_CHISELED_COPPER_BRICKS, new Oxidizable(WEATHERED_CHISELED_COPPER_BRICKS.get()), false)
				.add(WEATHERED_CHISELED_COPPER_BRICKS, new Oxidizable(OXIDIZED_CHISELED_COPPER_BRICKS.get()), false)
				.add(COPPER_INGOT, new Oxidizable(EXPOSED_COPPER_INGOT.get()), false)
				.add(EXPOSED_COPPER_INGOT, new Oxidizable(WEATHERED_COPPER_INGOT.get()), false)
				.add(WEATHERED_COPPER_INGOT, new Oxidizable(OXIDIZED_COPPER_INGOT.get()), false)
				.add(COPPER_CHAIN, new Oxidizable(EXPOSED_COPPER_CHAIN.get()), false)
				.add(EXPOSED_COPPER_CHAIN, new Oxidizable(WEATHERED_COPPER_CHAIN.get()), false)
				.add(WEATHERED_COPPER_CHAIN, new Oxidizable(OXIDIZED_COPPER_CHAIN.get()), false)
				.add(COPPER_LANTERN, new Oxidizable(EXPOSED_COPPER_LANTERN.get()), false)
				.add(EXPOSED_COPPER_LANTERN, new Oxidizable(WEATHERED_COPPER_LANTERN.get()), false)
				.add(WEATHERED_COPPER_LANTERN, new Oxidizable(OXIDIZED_COPPER_LANTERN.get()), false);

		this.builder(NeoForgeDataMaps.WAXABLES)
				.add(COPPER_BARS, new Waxable(WAXED_COPPER_BARS.get()), false)
				.add(EXPOSED_COPPER_BARS, new Waxable(WAXED_EXPOSED_COPPER_BARS.get()), false)
				.add(WEATHERED_COPPER_BARS, new Waxable(WAXED_WEATHERED_COPPER_BARS.get()), false)
				.add(OXIDIZED_COPPER_BARS, new Waxable(WAXED_OXIDIZED_COPPER_BARS.get()), false)
				.add(COPPER_BUTTON, new Waxable(WAXED_COPPER_BUTTON.get()), false)
				.add(EXPOSED_COPPER_BUTTON, new Waxable(WAXED_EXPOSED_COPPER_BUTTON.get()), false)
				.add(WEATHERED_COPPER_BUTTON, new Waxable(WAXED_WEATHERED_COPPER_BUTTON.get()), false)
				.add(OXIDIZED_COPPER_BUTTON, new Waxable(WAXED_OXIDIZED_COPPER_BUTTON.get()), false)
				.add(TOOLBOX, new Waxable(WAXED_TOOLBOX.get()), false)
				.add(EXPOSED_TOOLBOX, new Waxable(WAXED_EXPOSED_TOOLBOX.get()), false)
				.add(WEATHERED_TOOLBOX, new Waxable(WAXED_WEATHERED_TOOLBOX.get()), false)
				.add(OXIDIZED_TOOLBOX, new Waxable(WAXED_OXIDIZED_TOOLBOX.get()), false)
				.add(Blocks.LIGHTNING_ROD.builtInRegistryHolder(), new Waxable(WAXED_LIGHTNING_ROD.get()), false)
				.add(EXPOSED_LIGHTNING_ROD, new Waxable(WAXED_EXPOSED_LIGHTNING_ROD.get()), false)
				.add(WEATHERED_LIGHTNING_ROD, new Waxable(WAXED_WEATHERED_LIGHTNING_ROD.get()), false)
				.add(OXIDIZED_LIGHTNING_ROD, new Waxable(WAXED_OXIDIZED_LIGHTNING_ROD.get()), false)
				.add(FLOODLIGHT, new Waxable(WAXED_FLOODLIGHT.get()), false)
				.add(EXPOSED_FLOODLIGHT, new Waxable(WAXED_EXPOSED_FLOODLIGHT.get()), false)
				.add(WEATHERED_FLOODLIGHT, new Waxable(WAXED_WEATHERED_FLOODLIGHT.get()), false)
				.add(OXIDIZED_FLOODLIGHT, new Waxable(WAXED_OXIDIZED_FLOODLIGHT.get()), false)
				.add(COPPER_RAIL, new Waxable(WAXED_COPPER_RAIL.get()), false)
				.add(EXPOSED_COPPER_RAIL, new Waxable(WAXED_EXPOSED_COPPER_RAIL.get()), false)
				.add(WEATHERED_COPPER_RAIL, new Waxable(WAXED_WEATHERED_COPPER_RAIL.get()), false)
				.add(OXIDIZED_COPPER_RAIL, new Waxable(WAXED_OXIDIZED_COPPER_RAIL.get()), false)
				.add(COPPER_BRICKS, new Waxable(WAXED_COPPER_BRICKS.get()), false)
				.add(EXPOSED_COPPER_BRICKS, new Waxable(WAXED_EXPOSED_COPPER_BRICKS.get()), false)
				.add(WEATHERED_COPPER_BRICKS, new Waxable(WAXED_WEATHERED_COPPER_BRICKS.get()), false)
				.add(OXIDIZED_COPPER_BRICKS, new Waxable(WAXED_OXIDIZED_COPPER_BRICKS.get()), false)
				.add(COPPER_BRICK_STAIRS, new Waxable(WAXED_COPPER_BRICK_STAIRS.get()), false)
				.add(EXPOSED_COPPER_BRICK_STAIRS, new Waxable(WAXED_EXPOSED_COPPER_BRICK_STAIRS.get()), false)
				.add(WEATHERED_COPPER_BRICK_STAIRS, new Waxable(WAXED_WEATHERED_COPPER_BRICK_STAIRS.get()), false)
				.add(OXIDIZED_COPPER_BRICK_STAIRS, new Waxable(WAXED_OXIDIZED_COPPER_BRICK_STAIRS.get()), false)
				.add(COPPER_BRICK_SLAB, new Waxable(WAXED_COPPER_BRICK_SLAB.get()), false)
				.add(EXPOSED_COPPER_BRICK_SLAB, new Waxable(WAXED_EXPOSED_COPPER_BRICK_SLAB.get()), false)
				.add(WEATHERED_COPPER_BRICK_SLAB, new Waxable(WAXED_WEATHERED_COPPER_BRICK_SLAB.get()), false)
				.add(OXIDIZED_COPPER_BRICK_SLAB, new Waxable(WAXED_OXIDIZED_COPPER_BRICK_SLAB.get()), false)
				.add(COPPER_BRICK_WALL, new Waxable(WAXED_COPPER_BRICK_WALL.get()), false)
				.add(EXPOSED_COPPER_BRICK_WALL, new Waxable(WAXED_EXPOSED_COPPER_BRICK_WALL.get()), false)
				.add(WEATHERED_COPPER_BRICK_WALL, new Waxable(WAXED_WEATHERED_COPPER_BRICK_WALL.get()), false)
				.add(OXIDIZED_COPPER_BRICK_WALL, new Waxable(WAXED_OXIDIZED_COPPER_BRICK_WALL.get()), false)
				.add(CHISELED_COPPER_BRICKS, new Waxable(WAXED_CHISELED_COPPER_BRICKS.get()), false)
				.add(EXPOSED_CHISELED_COPPER_BRICKS, new Waxable(WAXED_EXPOSED_CHISELED_COPPER_BRICKS.get()), false)
				.add(WEATHERED_CHISELED_COPPER_BRICKS, new Waxable(WAXED_WEATHERED_CHISELED_COPPER_BRICKS.get()), false)
				.add(OXIDIZED_CHISELED_COPPER_BRICKS, new Waxable(WAXED_OXIDIZED_CHISELED_COPPER_BRICKS.get()), false)
				.add(COPPER_INGOT, new Waxable(WAXED_COPPER_INGOT.get()), false)
				.add(EXPOSED_COPPER_INGOT, new Waxable(WAXED_EXPOSED_COPPER_INGOT.get()), false)
				.add(WEATHERED_COPPER_INGOT, new Waxable(WAXED_WEATHERED_COPPER_INGOT.get()), false)
				.add(OXIDIZED_COPPER_INGOT, new Waxable(WAXED_OXIDIZED_COPPER_INGOT.get()), false)
				.add(COPPER_CHAIN, new Waxable(WAXED_COPPER_CHAIN.get()), false)
				.add(EXPOSED_COPPER_CHAIN, new Waxable(WAXED_EXPOSED_COPPER_CHAIN.get()), false)
				.add(WEATHERED_COPPER_CHAIN, new Waxable(WAXED_WEATHERED_COPPER_CHAIN.get()), false)
				.add(OXIDIZED_COPPER_CHAIN, new Waxable(WAXED_OXIDIZED_COPPER_CHAIN.get()), false)
				.add(COPPER_LANTERN, new Waxable(WAXED_COPPER_LANTERN.get()), false)
				.add(EXPOSED_COPPER_LANTERN, new Waxable(WAXED_EXPOSED_COPPER_LANTERN.get()), false)
				.add(WEATHERED_COPPER_LANTERN, new Waxable(WAXED_WEATHERED_COPPER_LANTERN.get()), false)
				.add(OXIDIZED_COPPER_LANTERN, new Waxable(WAXED_OXIDIZED_COPPER_LANTERN.get()), false);
	}
}