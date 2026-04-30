package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TinDeflection;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import com.teamabnormals.caverns_and_chasms.core.other.CCLootTables;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;

public class CCDataMapProvider extends DataMapProvider {

	public CCDataMapProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void gather(Provider provider) {
		this.builder(CCDataMaps.TIN_DEFLECTIONS)
				// Default deflection parameters
				.add(TIN_BLOCK, new TinDeflection(), false)
				.add(TIN_INGOT, new TinDeflection(), false)
				.add(SCATTERER, new TinDeflection(), false)
				.add(SPLURTER, new TinDeflection(), false)
				.add(TIN_BRICKS, new TinDeflection(), false)
				.add(TIN_BRICK_STAIRS, new TinDeflection(), false)
				.add(TIN_BRICK_SLAB, new TinDeflection(), false)
				.add(TIN_BRICK_WALL, new TinDeflection(), false)
				.add(CHISELED_TIN_BRICKS, new TinDeflection(), false)
				.add(TIN_BARS, new TinDeflection(), false)
				.add(HOLD_PRESSURE_PLATE, new TinDeflection(), false)
				.add(HOLD_BUTTON, new TinDeflection(), false)
				.add(WINCH, new TinDeflection(), false)
				.add(HOOP, new TinDeflection(), false)
				.add(STORAGE_DUCT_HATCH, new TinDeflection(), false)
				.add(RAW_TIN_BLOCK, new TinDeflection(CCSoundEvents.TIN_ORE_DEFLECT), false)
				.add(DIMMER, new TinDeflection(CCSoundEvents.DIMMER_DEFLECT), false)
				.add(WALL_DIMMER, new TinDeflection(CCSoundEvents.DIMMER_DEFLECT), false)
				.add(RESISTOR, new TinDeflection(CCSoundEvents.DIMMER_DEFLECT), false)
				.add(REFRACTOR, new TinDeflection(CCSoundEvents.REFRACTOR_DEFLECT), false)
				.add(SADDLED_EGG, new TinDeflection(CCSoundEvents.SADDLED_EGG_DEFLECT), false)
				.add(TIN_BULB, new TinDeflection(CCSoundEvents.TIN_BULB_DEFLECT), false)
				.add(TIN_CHAIN, new TinDeflection(CCSoundEvents.TIN_CHAIN_DEFLECT), false)
				.add(STORAGE_DUCT, new TinDeflection(CCSoundEvents.STORAGE_DUCT_DEFLECT), false)
				.add(ROLLER_DOOR, new TinDeflection(CCSoundEvents.ROLLER_DOOR_DEFLECT), false)
				.add(ROLLER_DOOR_HEADER, new TinDeflection(CCSoundEvents.ROLLER_DOOR_DEFLECT), false)
				.add(FLOAT_GLASS, new TinDeflection(CCSoundEvents.FLOAT_GLASS_DEFLECT), false)
				.add(FLOAT_GLASS_PANE, new TinDeflection(CCSoundEvents.FLOAT_GLASS_DEFLECT), false)
				// Weaker deflections
				.add(CCBlockTags.NORMAL_CASSITERITE_BLOCKS, new TinDeflection(0.4D, 0.5D, CCSoundEvents.CASSITERITE_DEFLECT), false)
				.add(CCBlockTags.POLISHED_CASSITERITE_BLOCKS, new TinDeflection(0.4D, 0.5D, CCSoundEvents.POLISHED_CASSITERITE_DEFLECT), false)
				.add(CCBlockTags.CASSITERITE_BRICKS, new TinDeflection(0.4D, 0.5D, CCSoundEvents.CASSITERITE_BRICKS_DEFLECT), false)
				.add(TIN_ORE, new TinDeflection(0.4D, 0.5D, CCSoundEvents.TIN_ORE_DEFLECT), false)
				.add(DEEPSLATE_TIN_ORE, new TinDeflection(0.4D, 0.5D, CCSoundEvents.DEEPSLATE_TIN_ORE_DEFLECT), false)
				.add(CYLINDRITE_TIN_ORE, new TinDeflection(0.4D, 0.5D, CCSoundEvents.CYLINDRITE_TIN_ORE_DEFLECT), false)
				.add(CASSITERITE_TIN_ORE, new TinDeflection(0.4D, 0.5D, CCSoundEvents.CASSITERITE_TIN_ORE_DEFLECT), false)
				// Weakest deflections
				.add(CCBlockTags.NORMAL_CYLINDRITE_BLOCKS, new TinDeflection(0.3D, 0.4D, CCSoundEvents.CYLINDRITE_DEFLECT), false)
				.add(CCBlockTags.POLISHED_CYLINDRITE_BLOCKS, new TinDeflection(0.3D, 0.4D, CCSoundEvents.POLISHED_CYLINDRITE_DEFLECT), false)
				.add(CCBlockTags.CYLINDRITE_BRICKS, new TinDeflection(0.3D, 0.4D, CCSoundEvents.CYLINDRITE_BRICKS_DEFLECT), false)
				// Other deflection parameters
				.add(BOUNCER, new TinDeflection(0.9D, 0.9D, CCSoundEvents.BOUNCER_DEFLECT), false)
				.add(TINPLATE_BLOCK, new TinDeflection(0.75D, 0.65D, true, CCSoundEvents.TINPLATE_DEFLECT), false);

		this.builder(NeoForgeDataMaps.PARROT_IMITATIONS)
				.add(CCEntityTypes.DEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_DEEPER.get()), false)
				.add(CCEntityTypes.EVENDEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_EVENDEEPER.get()), false)
				.add(CCEntityTypes.PEEPER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_PEEPER.get()), false)
				.add(CCEntityTypes.MIME, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_MIME.get()), false)
				.add(CCEntityTypes.GRAZER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_GRAZER.get()), false)
				.add(CCEntityTypes.SADDLED_GRAZER, new ParrotImitation(CCSoundEvents.PARROT_IMITATE_GRAZER.get()), false);

		this.builder(CCDataMaps.TRIAL_TOKENS)
				.add(CCItems.TRIAL_TOKEN, new TrialToken(
						new ItemStack(Items.TRIAL_KEY), CCSoundEvents.VAULT_INSERT_TOKEN,
						Map.of(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES, CCLootTables.SPAWNER_TRIAL_CHAMBER_TOKEN),
						Map.of(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, CCLootTables.TRIAL_CHAMBERS_TOKEN)
				), false)
				.add(CCItems.OMINOUS_TRIAL_TOKEN, new TrialToken(
						new ItemStack(Items.OMINOUS_TRIAL_KEY), CCSoundEvents.VAULT_INSERT_TOKEN,
						Map.of(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, CCLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_TOKEN),
						Map.of(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS, CCLootTables.TRIAL_CHAMBERS_TOKEN_OMINOUS)
				), false);

		this.builder(NeoForgeDataMaps.VIBRATION_FREQUENCIES)
				.add(CCGameEvents.TIN_DEFLECT, new VibrationFrequency(2), false)
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
				.add(LIFT_BUTTON, new Oxidizable(EXPOSED_LIFT_BUTTON.get()), false)
				.add(EXPOSED_LIFT_BUTTON, new Oxidizable(WEATHERED_LIFT_BUTTON.get()), false)
				.add(WEATHERED_LIFT_BUTTON, new Oxidizable(OXIDIZED_LIFT_BUTTON.get()), false)
				.add(LIFT_PRESSURE_PLATE, new Oxidizable(EXPOSED_LIFT_PRESSURE_PLATE.get()), false)
				.add(EXPOSED_LIFT_PRESSURE_PLATE, new Oxidizable(WEATHERED_LIFT_PRESSURE_PLATE.get()), false)
				.add(WEATHERED_LIFT_PRESSURE_PLATE, new Oxidizable(OXIDIZED_LIFT_PRESSURE_PLATE.get()), false)
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

		Builder<Waxable, Block> builder = this.builder(NeoForgeDataMaps.WAXABLES);

		WAXABLE_BLOCKS.get().forEach((unwaxed, waxed) -> builder.add(unwaxed.builtInRegistryHolder(), new Waxable(waxed), false));
	}

	public static final Supplier<BiMap<Block, Block>> WAXABLE_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
			.put(COPPER_BARS.get(), WAXED_COPPER_BARS.get()).put(EXPOSED_COPPER_BARS.get(), WAXED_EXPOSED_COPPER_BARS.get()).put(WEATHERED_COPPER_BARS.get(), WAXED_WEATHERED_COPPER_BARS.get()).put(OXIDIZED_COPPER_BARS.get(), WAXED_OXIDIZED_COPPER_BARS.get())
			.put(LIFT_BUTTON.get(), WAXED_LIFT_BUTTON.get()).put(EXPOSED_LIFT_BUTTON.get(), WAXED_EXPOSED_LIFT_BUTTON.get()).put(WEATHERED_LIFT_BUTTON.get(), WAXED_WEATHERED_LIFT_BUTTON.get()).put(OXIDIZED_LIFT_BUTTON.get(), WAXED_OXIDIZED_LIFT_BUTTON.get())
			.put(LIFT_PRESSURE_PLATE.get(), WAXED_LIFT_PRESSURE_PLATE.get()).put(EXPOSED_LIFT_PRESSURE_PLATE.get(), WAXED_EXPOSED_LIFT_PRESSURE_PLATE.get()).put(WEATHERED_LIFT_PRESSURE_PLATE.get(), WAXED_WEATHERED_LIFT_PRESSURE_PLATE.get()).put(OXIDIZED_LIFT_PRESSURE_PLATE.get(), WAXED_OXIDIZED_LIFT_PRESSURE_PLATE.get())
			.put(TOOLBOX.get(), WAXED_TOOLBOX.get()).put(EXPOSED_TOOLBOX.get(), WAXED_EXPOSED_TOOLBOX.get()).put(WEATHERED_TOOLBOX.get(), WAXED_WEATHERED_TOOLBOX.get()).put(OXIDIZED_TOOLBOX.get(), WAXED_OXIDIZED_TOOLBOX.get())
			.put(Blocks.LIGHTNING_ROD, WAXED_LIGHTNING_ROD.get()).put(EXPOSED_LIGHTNING_ROD.get(), WAXED_EXPOSED_LIGHTNING_ROD.get()).put(WEATHERED_LIGHTNING_ROD.get(), WAXED_WEATHERED_LIGHTNING_ROD.get()).put(OXIDIZED_LIGHTNING_ROD.get(), WAXED_OXIDIZED_LIGHTNING_ROD.get())
			.put(FLOODLIGHT.get(), WAXED_FLOODLIGHT.get()).put(EXPOSED_FLOODLIGHT.get(), WAXED_EXPOSED_FLOODLIGHT.get()).put(WEATHERED_FLOODLIGHT.get(), WAXED_WEATHERED_FLOODLIGHT.get()).put(OXIDIZED_FLOODLIGHT.get(), WAXED_OXIDIZED_FLOODLIGHT.get())
			.put(COPPER_RAIL.get(), WAXED_COPPER_RAIL.get()).put(EXPOSED_COPPER_RAIL.get(), WAXED_EXPOSED_COPPER_RAIL.get()).put(WEATHERED_COPPER_RAIL.get(), WAXED_WEATHERED_COPPER_RAIL.get()).put(OXIDIZED_COPPER_RAIL.get(), WAXED_OXIDIZED_COPPER_RAIL.get())
			.put(COPPER_BRICKS.get(), WAXED_COPPER_BRICKS.get()).put(EXPOSED_COPPER_BRICKS.get(), WAXED_EXPOSED_COPPER_BRICKS.get()).put(WEATHERED_COPPER_BRICKS.get(), WAXED_WEATHERED_COPPER_BRICKS.get()).put(OXIDIZED_COPPER_BRICKS.get(), WAXED_OXIDIZED_COPPER_BRICKS.get())
			.put(COPPER_BRICK_STAIRS.get(), WAXED_COPPER_BRICK_STAIRS.get()).put(EXPOSED_COPPER_BRICK_STAIRS.get(), WAXED_EXPOSED_COPPER_BRICK_STAIRS.get()).put(WEATHERED_COPPER_BRICK_STAIRS.get(), WAXED_WEATHERED_COPPER_BRICK_STAIRS.get()).put(OXIDIZED_COPPER_BRICK_STAIRS.get(), WAXED_OXIDIZED_COPPER_BRICK_STAIRS.get())
			.put(COPPER_BRICK_SLAB.get(), WAXED_COPPER_BRICK_SLAB.get()).put(EXPOSED_COPPER_BRICK_SLAB.get(), WAXED_EXPOSED_COPPER_BRICK_SLAB.get()).put(WEATHERED_COPPER_BRICK_SLAB.get(), WAXED_WEATHERED_COPPER_BRICK_SLAB.get()).put(OXIDIZED_COPPER_BRICK_SLAB.get(), WAXED_OXIDIZED_COPPER_BRICK_SLAB.get())
			.put(COPPER_BRICK_WALL.get(), WAXED_COPPER_BRICK_WALL.get()).put(EXPOSED_COPPER_BRICK_WALL.get(), WAXED_EXPOSED_COPPER_BRICK_WALL.get()).put(WEATHERED_COPPER_BRICK_WALL.get(), WAXED_WEATHERED_COPPER_BRICK_WALL.get()).put(OXIDIZED_COPPER_BRICK_WALL.get(), WAXED_OXIDIZED_COPPER_BRICK_WALL.get())
			.put(CHISELED_COPPER_BRICKS.get(), WAXED_CHISELED_COPPER_BRICKS.get()).put(EXPOSED_CHISELED_COPPER_BRICKS.get(), WAXED_EXPOSED_CHISELED_COPPER_BRICKS.get()).put(WEATHERED_CHISELED_COPPER_BRICKS.get(), WAXED_WEATHERED_CHISELED_COPPER_BRICKS.get()).put(OXIDIZED_CHISELED_COPPER_BRICKS.get(), WAXED_OXIDIZED_CHISELED_COPPER_BRICKS.get())
			.put(COPPER_INGOT.get(), WAXED_COPPER_INGOT.get()).put(EXPOSED_COPPER_INGOT.get(), WAXED_EXPOSED_COPPER_INGOT.get()).put(WEATHERED_COPPER_INGOT.get(), WAXED_WEATHERED_COPPER_INGOT.get()).put(OXIDIZED_COPPER_INGOT.get(), WAXED_OXIDIZED_COPPER_INGOT.get())
			.put(COPPER_CHAIN.get(), WAXED_COPPER_CHAIN.get()).put(EXPOSED_COPPER_CHAIN.get(), WAXED_EXPOSED_COPPER_CHAIN.get()).put(WEATHERED_COPPER_CHAIN.get(), WAXED_WEATHERED_COPPER_CHAIN.get()).put(OXIDIZED_COPPER_CHAIN.get(), WAXED_OXIDIZED_COPPER_CHAIN.get())
			.put(COPPER_LANTERN.get(), WAXED_COPPER_LANTERN.get()).put(EXPOSED_COPPER_LANTERN.get(), WAXED_EXPOSED_COPPER_LANTERN.get()).put(WEATHERED_COPPER_LANTERN.get(), WAXED_WEATHERED_COPPER_LANTERN.get()).put(OXIDIZED_COPPER_LANTERN.get(), WAXED_OXIDIZED_COPPER_LANTERN.get())
			.build()
	);
}