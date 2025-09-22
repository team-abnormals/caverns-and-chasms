package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.DataUtil.CustomNoteBlockInstrument;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.IntStream;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCSoundEvents {
	public static final SoundSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getSoundSubHelper();

	public static final RegistryObject<SoundEvent> EPILOGUE = HELPER.createSoundEvent("music.record.epilogue");

	public static final RegistryObject<SoundEvent> ROCKY_DIRT_BREAK = HELPER.createSoundEvent("block.rocky_dirt.break");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_FALL = HELPER.createSoundEvent("block.rocky_dirt.fall");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_HIT = HELPER.createSoundEvent("block.rocky_dirt.hit");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_PLACE = HELPER.createSoundEvent("block.rocky_dirt.place");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_STEP = HELPER.createSoundEvent("block.rocky_dirt.step");

	public static final RegistryObject<SoundEvent> RHYOLITE_BREAK = HELPER.createSoundEvent("block.rhyolite.break");
	public static final RegistryObject<SoundEvent> RHYOLITE_FALL = HELPER.createSoundEvent("block.rhyolite.fall");
	public static final RegistryObject<SoundEvent> RHYOLITE_HIT = HELPER.createSoundEvent("block.rhyolite.hit");
	public static final RegistryObject<SoundEvent> RHYOLITE_PLACE = HELPER.createSoundEvent("block.rhyolite.place");
	public static final RegistryObject<SoundEvent> RHYOLITE_STEP = HELPER.createSoundEvent("block.rhyolite.step");

	public static final RegistryObject<SoundEvent> FLINT_BLOCK_BREAK = HELPER.createSoundEvent("block.flint_block.break");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_FALL = HELPER.createSoundEvent("block.flint_block.fall");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_HIT = HELPER.createSoundEvent("block.flint_block.hit");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_PLACE = HELPER.createSoundEvent("block.flint_block.place");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_STEP = HELPER.createSoundEvent("block.flint_block.step");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_STRIKE = HELPER.createSoundEvent("block.flint_block.strike");

	public static final RegistryObject<SoundEvent> SOUL_SILVER_ORE_BREAK = HELPER.createSoundEvent("block.soul_silver_ore.break");
	public static final RegistryObject<SoundEvent> SOUL_SILVER_ORE_FALL = HELPER.createSoundEvent("block.soul_silver_ore.fall");
	public static final RegistryObject<SoundEvent> SOUL_SILVER_ORE_HIT = HELPER.createSoundEvent("block.soul_silver_ore.hit");
	public static final RegistryObject<SoundEvent> SOUL_SILVER_ORE_PLACE = HELPER.createSoundEvent("block.soul_silver_ore.place");
	public static final RegistryObject<SoundEvent> SOUL_SILVER_ORE_STEP = HELPER.createSoundEvent("block.soul_silver_ore.step");

	public static final RegistryObject<SoundEvent> SPINEL_BREAK = HELPER.createSoundEvent("block.spinel.break");
	public static final RegistryObject<SoundEvent> SPINEL_FALL = HELPER.createSoundEvent("block.spinel.fall");
	public static final RegistryObject<SoundEvent> SPINEL_HIT = HELPER.createSoundEvent("block.spinel.hit");
	public static final RegistryObject<SoundEvent> SPINEL_PLACE = HELPER.createSoundEvent("block.spinel.place");
	public static final RegistryObject<SoundEvent> SPINEL_STEP = HELPER.createSoundEvent("block.spinel.step");

	public static final RegistryObject<SoundEvent> SUGILITE_BREAK = HELPER.createSoundEvent("block.sugilite.break");
	public static final RegistryObject<SoundEvent> SUGILITE_FALL = HELPER.createSoundEvent("block.sugilite.fall");
	public static final RegistryObject<SoundEvent> SUGILITE_HIT = HELPER.createSoundEvent("block.sugilite.hit");
	public static final RegistryObject<SoundEvent> SUGILITE_PLACE = HELPER.createSoundEvent("block.sugilite.place");
	public static final RegistryObject<SoundEvent> SUGILITE_STEP = HELPER.createSoundEvent("block.sugilite.step");

	public static final RegistryObject<SoundEvent> ZIRCONIA_BREAK = HELPER.createSoundEvent("block.zirconia.break");
	public static final RegistryObject<SoundEvent> ZIRCONIA_FALL = HELPER.createSoundEvent("block.zirconia.fall");
	public static final RegistryObject<SoundEvent> ZIRCONIA_HIT = HELPER.createSoundEvent("block.zirconia.hit");
	public static final RegistryObject<SoundEvent> ZIRCONIA_PLACE = HELPER.createSoundEvent("block.zirconia.place");
	public static final RegistryObject<SoundEvent> ZIRCONIA_STEP = HELPER.createSoundEvent("block.zirconia.step");

	public static final RegistryObject<SoundEvent> NECROMIUM_BREAK = HELPER.createSoundEvent("block.necromium.break");
	public static final RegistryObject<SoundEvent> NECROMIUM_FALL = HELPER.createSoundEvent("block.necromium.fall");
	public static final RegistryObject<SoundEvent> NECROMIUM_HIT = HELPER.createSoundEvent("block.necromium.hit");
	public static final RegistryObject<SoundEvent> NECROMIUM_PLACE = HELPER.createSoundEvent("block.necromium.place");
	public static final RegistryObject<SoundEvent> NECROMIUM_STEP = HELPER.createSoundEvent("block.necromium.step");

	public static final RegistryObject<SoundEvent> FALSE_HOPE_BREAK = HELPER.createSoundEvent("block.false_hope.break");
	public static final RegistryObject<SoundEvent> FALSE_HOPE_FALL = HELPER.createSoundEvent("block.false_hope.fall");
	public static final RegistryObject<SoundEvent> FALSE_HOPE_HIT = HELPER.createSoundEvent("block.false_hope.hit");
	public static final RegistryObject<SoundEvent> FALSE_HOPE_PLACE = HELPER.createSoundEvent("block.false_hope.place");
	public static final RegistryObject<SoundEvent> FALSE_HOPE_STEP = HELPER.createSoundEvent("block.false_hope.step");

	public static final RegistryObject<SoundEvent> CAVE_GROWTHS_BREAK = HELPER.createSoundEvent("block.cave_growths.break");
	public static final RegistryObject<SoundEvent> CAVE_GROWTHS_FALL = HELPER.createSoundEvent("block.cave_growths.fall");
	public static final RegistryObject<SoundEvent> CAVE_GROWTHS_HIT = HELPER.createSoundEvent("block.cave_growths.hit");
	public static final RegistryObject<SoundEvent> CAVE_GROWTHS_PLACE = HELPER.createSoundEvent("block.cave_growths.place");
	public static final RegistryObject<SoundEvent> CAVE_GROWTHS_STEP = HELPER.createSoundEvent("block.cave_growths.step");

	public static final RegistryObject<SoundEvent> MOSCHATEL_BREAK = HELPER.createSoundEvent("block.moschatel.break");
	public static final RegistryObject<SoundEvent> MOSCHATEL_FALL = HELPER.createSoundEvent("block.moschatel.fall");
	public static final RegistryObject<SoundEvent> MOSCHATEL_HIT = HELPER.createSoundEvent("block.moschatel.hit");
	public static final RegistryObject<SoundEvent> MOSCHATEL_PLACE = HELPER.createSoundEvent("block.moschatel.place");
	public static final RegistryObject<SoundEvent> MOSCHATEL_STEP = HELPER.createSoundEvent("block.moschatel.step");

	public static final RegistryObject<SoundEvent> TMT_BREAK = HELPER.createSoundEvent("block.tmt.break");
	public static final RegistryObject<SoundEvent> TMT_FALL = HELPER.createSoundEvent("block.tmt.fall");
	public static final RegistryObject<SoundEvent> TMT_HIT = HELPER.createSoundEvent("block.tmt.hit");
	public static final RegistryObject<SoundEvent> TMT_PLACE = HELPER.createSoundEvent("block.tmt.place");
	public static final RegistryObject<SoundEvent> TMT_STEP = HELPER.createSoundEvent("block.tmt.step");

	public static final RegistryObject<SoundEvent> SANGUINE_BREAK = HELPER.createSoundEvent("block.sanguine.break");
	public static final RegistryObject<SoundEvent> SANGUINE_FALL = HELPER.createSoundEvent("block.sanguine.fall");
	public static final RegistryObject<SoundEvent> SANGUINE_HIT = HELPER.createSoundEvent("block.sanguine.hit");
	public static final RegistryObject<SoundEvent> SANGUINE_PLACE = HELPER.createSoundEvent("block.sanguine.place");
	public static final RegistryObject<SoundEvent> SANGUINE_STEP = HELPER.createSoundEvent("block.sanguine.step");

	public static final RegistryObject<SoundEvent> ROTTEN_FLESH_BREAK = HELPER.createSoundEvent("block.rotten_flesh.break");
	public static final RegistryObject<SoundEvent> ROTTEN_FLESH_FALL = HELPER.createSoundEvent("block.rotten_flesh.fall");
	public static final RegistryObject<SoundEvent> ROTTEN_FLESH_HIT = HELPER.createSoundEvent("block.rotten_flesh.hit");
	public static final RegistryObject<SoundEvent> ROTTEN_FLESH_PLACE = HELPER.createSoundEvent("block.rotten_flesh.place");
	public static final RegistryObject<SoundEvent> ROTTEN_FLESH_STEP = HELPER.createSoundEvent("block.rotten_flesh.step");

	public static final RegistryObject<SoundEvent> LAVA_LAMP_BREAK = HELPER.createSoundEvent("block.lava_lamp.break");
	public static final RegistryObject<SoundEvent> LAVA_LAMP_FALL = HELPER.createSoundEvent("block.lava_lamp.fall");
	public static final RegistryObject<SoundEvent> LAVA_LAMP_HIT = HELPER.createSoundEvent("block.lava_lamp.hit");
	public static final RegistryObject<SoundEvent> LAVA_LAMP_PLACE = HELPER.createSoundEvent("block.lava_lamp.place");
	public static final RegistryObject<SoundEvent> LAVA_LAMP_STEP = HELPER.createSoundEvent("block.lava_lamp.step");
	public static final RegistryObject<SoundEvent> LAVA_LAMP_GLUG = HELPER.createSoundEvent("block.lava_lamp.glug");

	public static final RegistryObject<SoundEvent> FLOODLIGHT_BREAK = HELPER.createSoundEvent("block.floodlight.break");
	public static final RegistryObject<SoundEvent> FLOODLIGHT_FALL = HELPER.createSoundEvent("block.floodlight.fall");
	public static final RegistryObject<SoundEvent> FLOODLIGHT_HIT = HELPER.createSoundEvent("block.floodlight.hit");
	public static final RegistryObject<SoundEvent> FLOODLIGHT_PLACE = HELPER.createSoundEvent("block.floodlight.place");
	public static final RegistryObject<SoundEvent> FLOODLIGHT_STEP = HELPER.createSoundEvent("block.floodlight.step");

	public static final RegistryObject<SoundEvent> COPPER_CHAIN_BREAK = HELPER.createSoundEvent("block.copper_chain.break");
	public static final RegistryObject<SoundEvent> COPPER_CHAIN_STEP = HELPER.createSoundEvent("block.copper_chain.step");
	public static final RegistryObject<SoundEvent> COPPER_CHAIN_PLACE = HELPER.createSoundEvent("block.copper_chain.place");
	public static final RegistryObject<SoundEvent> COPPER_CHAIN_HIT = HELPER.createSoundEvent("block.copper_chain.hit");
	public static final RegistryObject<SoundEvent> COPPER_CHAIN_FALL = HELPER.createSoundEvent("block.copper_chain.fall");

	public static final RegistryObject<SoundEvent> COPPER_LANTERN_BREAK = HELPER.createSoundEvent("block.copper_lantern.break");
	public static final RegistryObject<SoundEvent> COPPER_LANTERN_STEP = HELPER.createSoundEvent("block.copper_lantern.step");
	public static final RegistryObject<SoundEvent> COPPER_LANTERN_PLACE = HELPER.createSoundEvent("block.copper_lantern.place");
	public static final RegistryObject<SoundEvent> COPPER_LANTERN_HIT = HELPER.createSoundEvent("block.copper_lantern.hit");
	public static final RegistryObject<SoundEvent> COPPER_LANTERN_FALL = HELPER.createSoundEvent("block.copper_lantern.fall");

	public static final RegistryObject<SoundEvent> COPPER_BULB_BREAK = HELPER.createSoundEvent("block.copper_bulb.break");
	public static final RegistryObject<SoundEvent> COPPER_BULB_STEP = HELPER.createSoundEvent("block.copper_bulb.step");
	public static final RegistryObject<SoundEvent> COPPER_BULB_PLACE = HELPER.createSoundEvent("block.copper_bulb.place");
	public static final RegistryObject<SoundEvent> COPPER_BULB_HIT = HELPER.createSoundEvent("block.copper_bulb.hit");
	public static final RegistryObject<SoundEvent> COPPER_BULB_FALL = HELPER.createSoundEvent("block.copper_bulb.fall");
	public static final RegistryObject<SoundEvent> COPPER_BULB_TURN_ON = HELPER.createSoundEvent("block.copper_bulb.turn_on");
	public static final RegistryObject<SoundEvent> COPPER_BULB_TURN_OFF = HELPER.createSoundEvent("block.copper_bulb.turn_off");

	public static final RegistryObject<SoundEvent> COPPER_DOOR_CLOSE = HELPER.createSoundEvent("block.copper_door.close");
	public static final RegistryObject<SoundEvent> COPPER_DOOR_OPEN = HELPER.createSoundEvent("block.copper_door.open");

	public static final RegistryObject<SoundEvent> COPPER_GRATE_BREAK = HELPER.createSoundEvent("block.copper_grate.break");
	public static final RegistryObject<SoundEvent> COPPER_GRATE_STEP = HELPER.createSoundEvent("block.copper_grate.step");
	public static final RegistryObject<SoundEvent> COPPER_GRATE_PLACE = HELPER.createSoundEvent("block.copper_grate.place");
	public static final RegistryObject<SoundEvent> COPPER_GRATE_HIT = HELPER.createSoundEvent("block.copper_grate.hit");
	public static final RegistryObject<SoundEvent> COPPER_GRATE_FALL = HELPER.createSoundEvent("block.copper_grate.fall");

	public static final RegistryObject<SoundEvent> COPPER_TRAPDOOR_CLOSE = HELPER.createSoundEvent("block.copper_trapdoor.close");
	public static final RegistryObject<SoundEvent> COPPER_TRAPDOOR_OPEN = HELPER.createSoundEvent("block.copper_trapdoor.open");

	public static final RegistryObject<SoundEvent> POLISHED_TUFF_BREAK = HELPER.createSoundEvent("block.polished_tuff.break");
	public static final RegistryObject<SoundEvent> POLISHED_TUFF_FALL = HELPER.createSoundEvent("block.polished_tuff.fall");
	public static final RegistryObject<SoundEvent> POLISHED_TUFF_HIT = HELPER.createSoundEvent("block.polished_tuff.hit");
	public static final RegistryObject<SoundEvent> POLISHED_TUFF_PLACE = HELPER.createSoundEvent("block.polished_tuff.place");
	public static final RegistryObject<SoundEvent> POLISHED_TUFF_STEP = HELPER.createSoundEvent("block.polished_tuff.step");

	public static final RegistryObject<SoundEvent> TUFF_BRICKS_BREAK = HELPER.createSoundEvent("block.tuff_bricks.break");
	public static final RegistryObject<SoundEvent> TUFF_BRICKS_FALL = HELPER.createSoundEvent("block.tuff_bricks.fall");
	public static final RegistryObject<SoundEvent> TUFF_BRICKS_HIT = HELPER.createSoundEvent("block.tuff_bricks.hit");
	public static final RegistryObject<SoundEvent> TUFF_BRICKS_PLACE = HELPER.createSoundEvent("block.tuff_bricks.place");
	public static final RegistryObject<SoundEvent> TUFF_BRICKS_STEP = HELPER.createSoundEvent("block.tuff_bricks.step");

	public static final RegistryObject<SoundEvent> SILVER_BREAK = HELPER.createSoundEvent("block.silver.break");
	public static final RegistryObject<SoundEvent> SILVER_FALL = HELPER.createSoundEvent("block.silver.fall");
	public static final RegistryObject<SoundEvent> SILVER_HIT = HELPER.createSoundEvent("block.silver.hit");
	public static final RegistryObject<SoundEvent> SILVER_PLACE = HELPER.createSoundEvent("block.silver.place");
	public static final RegistryObject<SoundEvent> SILVER_STEP = HELPER.createSoundEvent("block.silver.step");

	public static final RegistryObject<SoundEvent> TIN_BREAK = HELPER.createSoundEvent("block.tin.break");
	public static final RegistryObject<SoundEvent> TIN_FALL = HELPER.createSoundEvent("block.tin.fall");
	public static final RegistryObject<SoundEvent> TIN_HIT = HELPER.createSoundEvent("block.tin.hit");
	public static final RegistryObject<SoundEvent> TIN_PLACE = HELPER.createSoundEvent("block.tin.place");
	public static final RegistryObject<SoundEvent> TIN_STEP = HELPER.createSoundEvent("block.tin.step");
	public static final RegistryObject<SoundEvent> TIN_DEFLECT = HELPER.createSoundEvent("block.tin.deflect");

	public static final RegistryObject<SoundEvent> TIN_ORE_BREAK = HELPER.createSoundEvent("block.tin_ore.break");
	public static final RegistryObject<SoundEvent> TIN_ORE_FALL = HELPER.createSoundEvent("block.tin_ore.fall");
	public static final RegistryObject<SoundEvent> TIN_ORE_HIT = HELPER.createSoundEvent("block.tin_ore.hit");
	public static final RegistryObject<SoundEvent> TIN_ORE_PLACE = HELPER.createSoundEvent("block.tin_ore.place");
	public static final RegistryObject<SoundEvent> TIN_ORE_STEP = HELPER.createSoundEvent("block.tin_ore.step");
	public static final RegistryObject<SoundEvent> TIN_ORE_DEFLECT = HELPER.createSoundEvent("block.tin_ore.deflect");

	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_BREAK = HELPER.createSoundEvent("block.deepslate_tin_ore.break");
	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_FALL = HELPER.createSoundEvent("block.deepslate_tin_ore.fall");
	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_HIT = HELPER.createSoundEvent("block.deepslate_tin_ore.hit");
	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_PLACE = HELPER.createSoundEvent("block.deepslate_tin_ore.place");
	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_STEP = HELPER.createSoundEvent("block.deepslate_tin_ore.step");
	public static final RegistryObject<SoundEvent> DEEPSLATE_TIN_ORE_DEFLECT = HELPER.createSoundEvent("block.deepslate_tin_ore.deflect");

	public static final RegistryObject<SoundEvent> STORAGE_DUCT_BREAK = HELPER.createSoundEvent("block.storage_duct.break");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_FALL = HELPER.createSoundEvent("block.storage_duct.fall");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HIT = HELPER.createSoundEvent("block.storage_duct.hit");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_PLACE = HELPER.createSoundEvent("block.storage_duct.place");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_STEP = HELPER.createSoundEvent("block.storage_duct.step");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_DEFLECT = HELPER.createSoundEvent("block.storage_duct.deflect");

	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HATCH_OPEN = HELPER.createSoundEvent("block.storage_duct_hatch.open");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HATCH_CLOSE = HELPER.createSoundEvent("block.storage_duct_hatch.close");

	public static final RegistryObject<SoundEvent> ATONING_TABLE_BREAK = HELPER.createSoundEvent("block.atoning_table.break");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_FALL = HELPER.createSoundEvent("block.atoning_table.fall");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_HIT = HELPER.createSoundEvent("block.atoning_table.hit");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_PLACE = HELPER.createSoundEvent("block.atoning_table.place");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_STEP = HELPER.createSoundEvent("block.atoning_table.step");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_USE = HELPER.createSoundEvent("block.atoning_table.use");
	public static final RegistryObject<SoundEvent> ATONING_TABLE_WHISPERS = HELPER.createSoundEvent("block.atoning_table.whispers");

	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_BREAK = HELPER.createSoundEvent("block.bejeweled_anvil.break");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_FALL = HELPER.createSoundEvent("block.bejeweled_anvil.fall");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_HIT = HELPER.createSoundEvent("block.bejeweled_anvil.hit");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_PLACE = HELPER.createSoundEvent("block.bejeweled_anvil.place");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_STEP = HELPER.createSoundEvent("block.bejeweled_anvil.step");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_USE = HELPER.createSoundEvent("block.bejeweled_anvil.use");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_LAND = HELPER.createSoundEvent("block.bejeweled_anvil.land");
	public static final RegistryObject<SoundEvent> BEJEWELED_ANVIL_SHATTER = HELPER.createSoundEvent("block.bejeweled_anvil.shatter");

	public static final RegistryObject<SoundEvent> COPPER_BUTTON_CLICK_OFF = HELPER.createSoundEvent("block.copper_button.click_off");
	public static final RegistryObject<SoundEvent> COPPER_BUTTON_CLICK_ON = HELPER.createSoundEvent("block.copper_button.click_on");

	public static final RegistryObject<SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_OFF = HELPER.createSoundEvent("block.medium_weighted_pressure_plate.click_off");
	public static final RegistryObject<SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_ON = HELPER.createSoundEvent("block.medium_weighted_pressure_plate.click_on");

	public static final RegistryObject<SoundEvent> DIMMER_BUZZ = HELPER.createSoundEvent("block.dimmer.buzz");
	public static final RegistryObject<SoundEvent> HOOP_SCORE = HELPER.createSoundEvent("block.hoop.score");

	public static final RegistryObject<SoundEvent> TOOLBOX_OPEN = HELPER.createSoundEvent("block.toolbox.open");
	public static final RegistryObject<SoundEvent> TOOLBOX_CLOSE = HELPER.createSoundEvent("block.toolbox.close");

	public static final RegistryObject<SoundEvent> DISMANTLING_TABLE_USE = HELPER.createSoundEvent("block.dismantling_table.use");

	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_MIME = HELPER.createSoundEvent("block.note_block.imitate.mime");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_DEEPER = HELPER.createSoundEvent("block.note_block.imitate.deeper");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_PEEPER = HELPER.createSoundEvent("block.note_block.imitate.peeper");

	public static final RegistryObject<SoundEvent> TUNING_FORK_VIBRATE = HELPER.createSoundEvent("item.tuning_fork.vibrate");

	public static final RegistryObject<SoundEvent> TETHER_POTION_EQUIP = HELPER.createSoundEvent("item.tether_potion.equip");
	public static final RegistryObject<SoundEvent> TETHER_POTION_BREAK = HELPER.createSoundEvent("item.tether_potion.break");

	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_COPPER = HELPER.createSoundEvent("item.armor.equip_copper");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SILVER = HELPER.createSoundEvent("item.armor.equip_silver");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_NECROMIUM = HELPER.createSoundEvent("item.armor.equip_necromium");
	public static final RegistryObject<SoundEvent> ARMOR_NECROMIUM_INFLICT = HELPER.createSoundEvent("item.armor.necromium_inflict");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SANGUINE = HELPER.createSoundEvent("item.armor.equip_sanguine");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_COWL = HELPER.createSoundEvent("item.armor.equip_cowl");

	public static final RegistryObject<SoundEvent> BEJEWELED_APPLE_EAT = HELPER.createSoundEvent("item.bejeweled_apple.eat");
	public static final RegistryObject<SoundEvent> BEJEWELED_APPLE_BURP = HELPER.createSoundEvent("item.bejeweled_apple.burp");

	public static final RegistryObject<SoundEvent> BEJEWELED_PEARL_TELEPORT = HELPER.createSoundEvent("item.bejeweled_pearl.teleport");
	public static final RegistryObject<SoundEvent> BEJEWELED_PEARL_CRUMBLE = HELPER.createSoundEvent("item.bejeweled_pearl.crumble");

	public static final RegistryObject<SoundEvent> ZIRCONIA_ANVIL_USE = HELPER.createSoundEvent("item.zirconia.anvil_use");

	public static final RegistryObject<SoundEvent> DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");

	public static final RegistryObject<SoundEvent> PEEPER_DEATH = HELPER.createSoundEvent("entity.peeper.death");
	public static final RegistryObject<SoundEvent> PEEPER_HURT = HELPER.createSoundEvent("entity.peeper.hurt");
	public static final RegistryObject<SoundEvent> PEEPER_PRIMED = HELPER.createSoundEvent("entity.peeper.primed");
	public static final RegistryObject<SoundEvent> PEEPER_EXPLODE = HELPER.createSoundEvent("entity.peeper.explode");

	public static final RegistryObject<SoundEvent> MIME_DEATH = HELPER.createSoundEvent("entity.mime.death");
	public static final RegistryObject<SoundEvent> MIME_HURT = HELPER.createSoundEvent("entity.mime.hurt");
	public static final RegistryObject<SoundEvent> MIME_MIME = HELPER.createSoundEvent("entity.mime.mime");
	public static final RegistryObject<SoundEvent> MIME_IMPERSONATE = HELPER.createSoundEvent("entity.mime.impersonate");

	public static final RegistryObject<SoundEvent> COPPER_GOLEM_DEATH = HELPER.createSoundEvent("entity.copper_golem.death");
	public static final RegistryObject<SoundEvent> COPPER_GOLEM_HURT = HELPER.createSoundEvent("entity.copper_golem.hurt");
	public static final RegistryObject<SoundEvent> COPPER_GOLEM_REPAIR = HELPER.createSoundEvent("entity.copper_golem.repair");
	public static final RegistryObject<SoundEvent> COPPER_GOLEM_DAMAGE = HELPER.createSoundEvent("entity.copper_golem.damage");
	public static final RegistryObject<SoundEvent> COPPER_GOLEM_GEAR = HELPER.createSoundEvent("entity.copper_golem.gear");
	public static final RegistryObject<SoundEvent> COPPER_GOLEM_STEP = HELPER.createSoundEvent("entity.copper_golem.step");

	public static final RegistryObject<SoundEvent> GLARE_DEATH = HELPER.createSoundEvent("entity.glare.death");
	public static final RegistryObject<SoundEvent> GLARE_ANGRY = HELPER.createSoundEvent("entity.glare.angry");
	public static final RegistryObject<SoundEvent> GLARE_EAT = HELPER.createSoundEvent("entity.glare.eat");
	public static final RegistryObject<SoundEvent> GLARE_HURT = HELPER.createSoundEvent("entity.glare.hurt");
	public static final RegistryObject<SoundEvent> GLARE_AMBIENT = HELPER.createSoundEvent("entity.glare.ambient");
	public static final RegistryObject<SoundEvent> GLARE_TAME = HELPER.createSoundEvent("entity.glare.tame");
	public static final RegistryObject<SoundEvent> GLARE_UNTAME = HELPER.createSoundEvent("entity.glare.untame");

	public static final RegistryObject<SoundEvent> BLUNT_ARROW_HIT = HELPER.createSoundEvent("entity.blunt_arrow.hit");

	public static final RegistryObject<SoundEvent> REWIND = HELPER.createSoundEvent("effect.rewind.rewind");

	public static final RegistryObject<SoundEvent> PARROT_IMITATE_DEEPER = HELPER.createSoundEvent("entity.parrot.imitate.deeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_PEEPER = HELPER.createSoundEvent("entity.parrot.imitate.peeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_MIME = HELPER.createSoundEvent("entity.parrot.imitate.mime");

	public static final RegistryObject<SoundEvent> NOTE_BLOCK_STATIC = HELPER.createSoundEvent("block.note_block.static");

	public static final ImmutableList<RegistryObject<SoundEvent>> GOAT_HORN_SOUND_VARIANTS = registerGoatHornSoundVariants();

	public static final ImmutableList<RegistryObject<SoundEvent>> LOST_GOAT_HORN_SOUND_VARIANTS = registerLostGoatHornSoundVariants();

	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_HARMONY_SOUND_VARIANTS = registerCopperHornSoundVariants("harmony");
	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_MELODY_SOUND_VARIANTS = registerCopperHornSoundVariants("melody");
	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_BASS_SOUND_VARIANTS = registerCopperHornSoundVariants("bass");

	private static ImmutableList<RegistryObject<SoundEvent>> registerGoatHornSoundVariants() {
		return IntStream.range(8, 10).mapToObj((suffix) -> HELPER.createSoundEvent("item.goat_horn.sound." + suffix)).collect(ImmutableList.toImmutableList());
	}

	private static ImmutableList<RegistryObject<SoundEvent>> registerLostGoatHornSoundVariants() {
		return IntStream.range(0, 2).mapToObj((suffix) -> HELPER.createSoundEvent("item.lost_goat_horn.sound." + suffix)).collect(ImmutableList.toImmutableList());
	}

	private static ImmutableList<RegistryObject<SoundEvent>> registerCopperHornSoundVariants(String variant) {
		return IntStream.range(0, 10).mapToObj((suffix) -> HELPER.createSoundEvent("item.copper_horn.sound." + variant + "." + suffix)).collect(ImmutableList.toImmutableList());
	}

	public static class CCSoundTypes {
		public static final ForgeSoundType ROCKY_DIRT = new ForgeSoundType(1.0F, 1.0F, ROCKY_DIRT_BREAK, ROCKY_DIRT_STEP, ROCKY_DIRT_PLACE, ROCKY_DIRT_HIT, ROCKY_DIRT_FALL);
		public static final ForgeSoundType RHYOLITE = new ForgeSoundType(1.0F, 1.0F, RHYOLITE_BREAK, RHYOLITE_STEP, RHYOLITE_PLACE, RHYOLITE_HIT, RHYOLITE_FALL);
		public static final ForgeSoundType FLINT_BLOCK = new ForgeSoundType(1.0F, 1.0F, FLINT_BLOCK_BREAK, FLINT_BLOCK_STEP, FLINT_BLOCK_PLACE, FLINT_BLOCK_HIT, FLINT_BLOCK_FALL);
		public static final ForgeSoundType SOUL_SILVER_ORE = new ForgeSoundType(1.0F, 1.0F, SOUL_SILVER_ORE_BREAK, SOUL_SILVER_ORE_STEP, SOUL_SILVER_ORE_PLACE, SOUL_SILVER_ORE_HIT, SOUL_SILVER_ORE_FALL);
		public static final ForgeSoundType SPINEL = new ForgeSoundType(1.0F, 1.0F, SPINEL_BREAK, SPINEL_STEP, SPINEL_PLACE, SPINEL_HIT, SPINEL_FALL);
		public static final ForgeSoundType SUGILITE = new ForgeSoundType(1.0F, 1.0F, SUGILITE_BREAK, SUGILITE_STEP, SUGILITE_PLACE, SUGILITE_HIT, SUGILITE_FALL);
		public static final ForgeSoundType ZIRCONIA = new ForgeSoundType(1.0F, 1.0F, ZIRCONIA_BREAK, ZIRCONIA_STEP, ZIRCONIA_PLACE, ZIRCONIA_HIT, ZIRCONIA_FALL);
		public static final ForgeSoundType NECROMIUM = new ForgeSoundType(1.0F, 1.0F, NECROMIUM_BREAK, NECROMIUM_STEP, NECROMIUM_PLACE, NECROMIUM_HIT, NECROMIUM_FALL);
		public static final ForgeSoundType FALSE_HOPE = new ForgeSoundType(1.0F, 1.0F, FALSE_HOPE_BREAK, FALSE_HOPE_STEP, FALSE_HOPE_PLACE, FALSE_HOPE_HIT, FALSE_HOPE_FALL);
		public static final ForgeSoundType CAVE_GROWTHS = new ForgeSoundType(1.0F, 1.0F, CAVE_GROWTHS_BREAK, CAVE_GROWTHS_STEP, CAVE_GROWTHS_PLACE, CAVE_GROWTHS_HIT, CAVE_GROWTHS_FALL);
		public static final ForgeSoundType MOSCHATEL = new ForgeSoundType(1.0F, 1.0F, MOSCHATEL_BREAK, MOSCHATEL_STEP, MOSCHATEL_PLACE, MOSCHATEL_HIT, MOSCHATEL_FALL);
		public static final ForgeSoundType TMT = new ForgeSoundType(1.0F, 1.0F, TMT_BREAK, TMT_STEP, TMT_PLACE, TMT_HIT, TMT_FALL);
		public static final ForgeSoundType SANGUINE = new ForgeSoundType(1.0F, 1.0F, SANGUINE_BREAK, SANGUINE_STEP, SANGUINE_PLACE, SANGUINE_HIT, SANGUINE_FALL);
		public static final ForgeSoundType ROTTEN_FLESH = new ForgeSoundType(1.0F, 1.0F, ROTTEN_FLESH_BREAK, ROTTEN_FLESH_STEP, ROTTEN_FLESH_PLACE, ROTTEN_FLESH_HIT, ROTTEN_FLESH_FALL);
		public static final ForgeSoundType LAVA_LAMP = new ForgeSoundType(1.0F, 1.0F, LAVA_LAMP_BREAK, LAVA_LAMP_STEP, LAVA_LAMP_PLACE, LAVA_LAMP_HIT, LAVA_LAMP_FALL);
		public static final ForgeSoundType FLOODLIGHT = new ForgeSoundType(1.0F, 1.0F, FLOODLIGHT_BREAK, FLOODLIGHT_STEP, FLOODLIGHT_PLACE, FLOODLIGHT_HIT, FLOODLIGHT_FALL);
		public static final ForgeSoundType COPPER_LANTERN = new ForgeSoundType(1.0F, 1.0F, COPPER_LANTERN_BREAK, COPPER_LANTERN_STEP, COPPER_LANTERN_PLACE, COPPER_LANTERN_HIT, COPPER_LANTERN_FALL);
		public static final ForgeSoundType COPPER_CHAIN = new ForgeSoundType(1.0F, 1.0F, COPPER_CHAIN_BREAK, COPPER_CHAIN_STEP, COPPER_CHAIN_PLACE, COPPER_CHAIN_HIT, COPPER_CHAIN_FALL);
		public static final ForgeSoundType COPPER_BULB = new ForgeSoundType(1.0F, 1.0F, COPPER_BULB_BREAK, COPPER_BULB_STEP, COPPER_BULB_PLACE, COPPER_BULB_HIT, COPPER_BULB_FALL);
		public static final ForgeSoundType COPPER_GRATE = new ForgeSoundType(1.0F, 1.0F, COPPER_GRATE_BREAK, COPPER_GRATE_STEP, COPPER_GRATE_PLACE, COPPER_GRATE_HIT, COPPER_GRATE_FALL);
		public static final ForgeSoundType SILVER = new ForgeSoundType(1.0F, 1.0F, SILVER_BREAK, SILVER_STEP, SILVER_PLACE, SILVER_HIT, SILVER_FALL);
		public static final ForgeSoundType TIN = new ForgeSoundType(1.0F, 1.0F, TIN_BREAK, TIN_STEP, TIN_PLACE, TIN_HIT, TIN_FALL);
		public static final ForgeSoundType TIN_ORE = new ForgeSoundType(1.0F, 1.0F, TIN_ORE_BREAK, TIN_ORE_STEP, TIN_ORE_PLACE, TIN_ORE_HIT, TIN_ORE_FALL);
		public static final ForgeSoundType DEEPSLATE_TIN_ORE = new ForgeSoundType(1.0F, 1.0F, DEEPSLATE_TIN_ORE_BREAK, DEEPSLATE_TIN_ORE_STEP, DEEPSLATE_TIN_ORE_PLACE, DEEPSLATE_TIN_ORE_HIT, DEEPSLATE_TIN_ORE_FALL);
		public static final ForgeSoundType STORAGE_DUCT = new ForgeSoundType(1.0F, 1.0F, STORAGE_DUCT_BREAK, STORAGE_DUCT_STEP, STORAGE_DUCT_PLACE, STORAGE_DUCT_HIT, STORAGE_DUCT_FALL);
		public static final ForgeSoundType ATONING_TABLE = new ForgeSoundType(1.0F, 1.0F, ATONING_TABLE_BREAK, ATONING_TABLE_STEP, ATONING_TABLE_PLACE, ATONING_TABLE_HIT, ATONING_TABLE_FALL);
		public static final ForgeSoundType BEJEWELED_ANVIL = new ForgeSoundType(1.0F, 1.0F, BEJEWELED_ANVIL_BREAK, BEJEWELED_ANVIL_STEP, BEJEWELED_ANVIL_PLACE, BEJEWELED_ANVIL_HIT, BEJEWELED_ANVIL_FALL);
		public static final ForgeSoundType POLISHED_TUFF = new ForgeSoundType(1.0F, 1.0F, POLISHED_TUFF_BREAK, POLISHED_TUFF_STEP, POLISHED_TUFF_PLACE, POLISHED_TUFF_HIT, POLISHED_TUFF_FALL);
		public static final ForgeSoundType TUFF_BRICKS = new ForgeSoundType(1.0F, 1.0F, TUFF_BRICKS_BREAK, TUFF_BRICKS_STEP, TUFF_BRICKS_PLACE, TUFF_BRICKS_HIT, TUFF_BRICKS_FALL);
	}

	public static void registerNoteBlocks() {
		registerHeadInstrument(CCBlocks.MIME_HEAD, NOTE_BLOCK_IMITATE_MIME);
		registerHeadInstrument(CCBlocks.DEEPER_HEAD, NOTE_BLOCK_IMITATE_DEEPER);
		registerHeadInstrument(CCBlocks.PEEPER_HEAD, NOTE_BLOCK_IMITATE_PEEPER);
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(CCBlockTags.STATIC_NOTE_BLOCKS), NOTE_BLOCK_STATIC.get(), false));
	}

	public static void registerHeadInstrument(RegistryObject<Block> block, RegistryObject<SoundEvent> soundEvent) {
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(block.get()), soundEvent.get(), true));
	}
}