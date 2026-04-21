package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.DataUtil.CustomNoteBlockInstrument;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.block.TinSoundType;
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

	public static final RegistryObject<SoundEvent> ANALOGUE = HELPER.createSoundEvent("music.record.analogue");
	public static final RegistryObject<SoundEvent> EPILOGUE = HELPER.createSoundEvent("music.record.epilogue");

	public static final RegistryObject<SoundEvent> ROCKY_DIRT_BREAK = HELPER.createSoundEvent("block.rocky_dirt.break");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_FALL = HELPER.createSoundEvent("block.rocky_dirt.fall");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_HIT = HELPER.createSoundEvent("block.rocky_dirt.hit");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_PLACE = HELPER.createSoundEvent("block.rocky_dirt.place");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_STEP = HELPER.createSoundEvent("block.rocky_dirt.step");

	public static final RegistryObject<SoundEvent> FRAGILE_STONE_BREAK = HELPER.createSoundEvent("block.fragile_stone.break");
	public static final RegistryObject<SoundEvent> FRAGILE_STONE_FALL = HELPER.createSoundEvent("block.fragile_stone.fall");
	public static final RegistryObject<SoundEvent> FRAGILE_STONE_HIT = HELPER.createSoundEvent("block.fragile_stone.hit");
	public static final RegistryObject<SoundEvent> FRAGILE_STONE_PLACE = HELPER.createSoundEvent("block.fragile_stone.place");
	public static final RegistryObject<SoundEvent> FRAGILE_STONE_STEP = HELPER.createSoundEvent("block.fragile_stone.step");

	public static final RegistryObject<SoundEvent> FRAGILE_DEEPSLATE_BREAK = HELPER.createSoundEvent("block.fragile_deepslate.break");
	public static final RegistryObject<SoundEvent> FRAGILE_DEEPSLATE_FALL = HELPER.createSoundEvent("block.fragile_deepslate.fall");
	public static final RegistryObject<SoundEvent> FRAGILE_DEEPSLATE_HIT = HELPER.createSoundEvent("block.fragile_deepslate.hit");
	public static final RegistryObject<SoundEvent> FRAGILE_DEEPSLATE_PLACE = HELPER.createSoundEvent("block.fragile_deepslate.place");
	public static final RegistryObject<SoundEvent> FRAGILE_DEEPSLATE_STEP = HELPER.createSoundEvent("block.fragile_deepslate.step");

	public static final RegistryObject<SoundEvent> CHARCOAL_BREAK = HELPER.createSoundEvent("block.charcoal.break");
	public static final RegistryObject<SoundEvent> CHARCOAL_FALL = HELPER.createSoundEvent("block.charcoal.fall");
	public static final RegistryObject<SoundEvent> CHARCOAL_HIT = HELPER.createSoundEvent("block.charcoal.hit");
	public static final RegistryObject<SoundEvent> CHARCOAL_PLACE = HELPER.createSoundEvent("block.charcoal.place");
	public static final RegistryObject<SoundEvent> CHARCOAL_STEP = HELPER.createSoundEvent("block.charcoal.step");

	public static final RegistryObject<SoundEvent> ECHO_BLOCK_BREAK = HELPER.createSoundEvent("block.echo_block.break");
	public static final RegistryObject<SoundEvent> ECHO_BLOCK_FALL = HELPER.createSoundEvent("block.echo_block.fall");
	public static final RegistryObject<SoundEvent> ECHO_BLOCK_HIT = HELPER.createSoundEvent("block.echo_block.hit");
	public static final RegistryObject<SoundEvent> ECHO_BLOCK_PLACE = HELPER.createSoundEvent("block.echo_block.place");
	public static final RegistryObject<SoundEvent> ECHO_BLOCK_STEP = HELPER.createSoundEvent("block.echo_block.step");

	public static final RegistryObject<SoundEvent> GUNPOWDER_BREAK = HELPER.createSoundEvent("block.gunpowder.break");
	public static final RegistryObject<SoundEvent> GUNPOWDER_FALL = HELPER.createSoundEvent("block.gunpowder.fall");
	public static final RegistryObject<SoundEvent> GUNPOWDER_HIT = HELPER.createSoundEvent("block.gunpowder.hit");
	public static final RegistryObject<SoundEvent> GUNPOWDER_PLACE = HELPER.createSoundEvent("block.gunpowder.place");
	public static final RegistryObject<SoundEvent> GUNPOWDER_STEP = HELPER.createSoundEvent("block.gunpowder.step");
	public static final RegistryObject<SoundEvent> GUNPOWDER_EXPLODE = HELPER.createSoundEvent("block.gunpowder.explode");

	public static final RegistryObject<SoundEvent> SPARKLER_BREAK = HELPER.createSoundEvent("block.sparkler.break");
	public static final RegistryObject<SoundEvent> SPARKLER_FALL = HELPER.createSoundEvent("block.sparkler.fall");
	public static final RegistryObject<SoundEvent> SPARKLER_HIT = HELPER.createSoundEvent("block.sparkler.hit");
	public static final RegistryObject<SoundEvent> SPARKLER_PLACE = HELPER.createSoundEvent("block.sparkler.place");
	public static final RegistryObject<SoundEvent> SPARKLER_STEP = HELPER.createSoundEvent("block.sparkler.step");
	public static final RegistryObject<SoundEvent> SPARKLER_SPARKLE = HELPER.createSoundEvent("block.sparkler.sparkle");
	public static final RegistryObject<SoundEvent> SPARKLER_FIZZLE = HELPER.createSoundEvent("block.sparkler.fizzle");
	public static final RegistryObject<SoundEvent> SPARKLER_EXPLODE = HELPER.createSoundEvent("block.sparkler.explode");

	public static final RegistryObject<SoundEvent> RHYOLITE_BREAK = HELPER.createSoundEvent("block.rhyolite.break");
	public static final RegistryObject<SoundEvent> RHYOLITE_FALL = HELPER.createSoundEvent("block.rhyolite.fall");
	public static final RegistryObject<SoundEvent> RHYOLITE_HIT = HELPER.createSoundEvent("block.rhyolite.hit");
	public static final RegistryObject<SoundEvent> RHYOLITE_PLACE = HELPER.createSoundEvent("block.rhyolite.place");
	public static final RegistryObject<SoundEvent> RHYOLITE_STEP = HELPER.createSoundEvent("block.rhyolite.step");

	public static final RegistryObject<SoundEvent> MAGMATIC_RHYOLITE_BREAK = HELPER.createSoundEvent("block.magmatic_rhyolite.break");
	public static final RegistryObject<SoundEvent> MAGMATIC_RHYOLITE_FALL = HELPER.createSoundEvent("block.magmatic_rhyolite.fall");
	public static final RegistryObject<SoundEvent> MAGMATIC_RHYOLITE_HIT = HELPER.createSoundEvent("block.magmatic_rhyolite.hit");
	public static final RegistryObject<SoundEvent> MAGMATIC_RHYOLITE_PLACE = HELPER.createSoundEvent("block.magmatic_rhyolite.place");
	public static final RegistryObject<SoundEvent> MAGMATIC_RHYOLITE_STEP = HELPER.createSoundEvent("block.magmatic_rhyolite.step");

	public static final RegistryObject<SoundEvent> CASSITERITE_BREAK = HELPER.createSoundEvent("block.cassiterite.break");
	public static final RegistryObject<SoundEvent> CASSITERITE_FALL = HELPER.createSoundEvent("block.cassiterite.fall");
	public static final RegistryObject<SoundEvent> CASSITERITE_HIT = HELPER.createSoundEvent("block.cassiterite.hit");
	public static final RegistryObject<SoundEvent> CASSITERITE_PLACE = HELPER.createSoundEvent("block.cassiterite.place");
	public static final RegistryObject<SoundEvent> CASSITERITE_STEP = HELPER.createSoundEvent("block.cassiterite.step");
	public static final RegistryObject<SoundEvent> CASSITERITE_DEFLECT = HELPER.createSoundEvent("block.cassiterite.deflect");

	public static final RegistryObject<SoundEvent> CYLINDRITE_BREAK = HELPER.createSoundEvent("block.cylindrite.break");
	public static final RegistryObject<SoundEvent> CYLINDRITE_FALL = HELPER.createSoundEvent("block.cylindrite.fall");
	public static final RegistryObject<SoundEvent> CYLINDRITE_HIT = HELPER.createSoundEvent("block.cylindrite.hit");
	public static final RegistryObject<SoundEvent> CYLINDRITE_PLACE = HELPER.createSoundEvent("block.cylindrite.place");
	public static final RegistryObject<SoundEvent> CYLINDRITE_STEP = HELPER.createSoundEvent("block.cylindrite.step");
	public static final RegistryObject<SoundEvent> CYLINDRITE_DEFLECT = HELPER.createSoundEvent("block.cylindrite.deflect");

	public static final RegistryObject<SoundEvent> FLINT_BLOCK_BREAK = HELPER.createSoundEvent("block.flint_block.break");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_FALL = HELPER.createSoundEvent("block.flint_block.fall");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_HIT = HELPER.createSoundEvent("block.flint_block.hit");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_PLACE = HELPER.createSoundEvent("block.flint_block.place");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_STEP = HELPER.createSoundEvent("block.flint_block.step");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_STRIKE = HELPER.createSoundEvent("block.flint_block.strike");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_LAND = HELPER.createSoundEvent("block.flint_block.land");
	public static final RegistryObject<SoundEvent> FLINT_BLOCK_RATTLE = HELPER.createSoundEvent("block.flint_block.rattle");

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

	public static final RegistryObject<SoundEvent> ORNATE_GLASS_BREAK = HELPER.createSoundEvent("block.ornate_glass.break");
	public static final RegistryObject<SoundEvent> ORNATE_GLASS_FALL = HELPER.createSoundEvent("block.ornate_glass.fall");
	public static final RegistryObject<SoundEvent> ORNATE_GLASS_HIT = HELPER.createSoundEvent("block.ornate_glass.hit");
	public static final RegistryObject<SoundEvent> ORNATE_GLASS_PLACE = HELPER.createSoundEvent("block.ornate_glass.place");
	public static final RegistryObject<SoundEvent> ORNATE_GLASS_STEP = HELPER.createSoundEvent("block.ornate_glass.step");

	public static final RegistryObject<SoundEvent> TURQUOISE_BREAK = HELPER.createSoundEvent("block.turquoise.break");
	public static final RegistryObject<SoundEvent> TURQUOISE_FALL = HELPER.createSoundEvent("block.turquoise.fall");
	public static final RegistryObject<SoundEvent> TURQUOISE_HIT = HELPER.createSoundEvent("block.turquoise.hit");
	public static final RegistryObject<SoundEvent> TURQUOISE_PLACE = HELPER.createSoundEvent("block.turquoise.place");
	public static final RegistryObject<SoundEvent> TURQUOISE_STEP = HELPER.createSoundEvent("block.turquoise.step");

	public static final RegistryObject<SoundEvent> FLOAT_GLASS_BREAK = HELPER.createSoundEvent("block.float_glass.break");
	public static final RegistryObject<SoundEvent> FLOAT_GLASS_FALL = HELPER.createSoundEvent("block.float_glass.fall");
	public static final RegistryObject<SoundEvent> FLOAT_GLASS_HIT = HELPER.createSoundEvent("block.float_glass.hit");
	public static final RegistryObject<SoundEvent> FLOAT_GLASS_PLACE = HELPER.createSoundEvent("block.float_glass.place");
	public static final RegistryObject<SoundEvent> FLOAT_GLASS_STEP = HELPER.createSoundEvent("block.float_glass.step");
	public static final RegistryObject<SoundEvent> FLOAT_GLASS_DEFLECT = HELPER.createSoundEvent("block.float_glass.deflect");

	public static final RegistryObject<SoundEvent> NECROMIUM_BREAK = HELPER.createSoundEvent("block.necromium.break");
	public static final RegistryObject<SoundEvent> NECROMIUM_FALL = HELPER.createSoundEvent("block.necromium.fall");
	public static final RegistryObject<SoundEvent> NECROMIUM_HIT = HELPER.createSoundEvent("block.necromium.hit");
	public static final RegistryObject<SoundEvent> NECROMIUM_PLACE = HELPER.createSoundEvent("block.necromium.place");
	public static final RegistryObject<SoundEvent> NECROMIUM_STEP = HELPER.createSoundEvent("block.necromium.step");

	public static final RegistryObject<SoundEvent> POLISHED_DRIPSTONE_BREAK = HELPER.createSoundEvent("block.polished_dripstone.break");
	public static final RegistryObject<SoundEvent> POLISHED_DRIPSTONE_FALL = HELPER.createSoundEvent("block.polished_dripstone.fall");
	public static final RegistryObject<SoundEvent> POLISHED_DRIPSTONE_HIT = HELPER.createSoundEvent("block.polished_dripstone.hit");
	public static final RegistryObject<SoundEvent> POLISHED_DRIPSTONE_PLACE = HELPER.createSoundEvent("block.polished_dripstone.place");
	public static final RegistryObject<SoundEvent> POLISHED_DRIPSTONE_STEP = HELPER.createSoundEvent("block.polished_dripstone.step");

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

	public static final RegistryObject<SoundEvent> FORTIFIED_SANGUINE_BREAK = HELPER.createSoundEvent("block.fortified_sanguine.break");
	public static final RegistryObject<SoundEvent> FORTIFIED_SANGUINE_FALL = HELPER.createSoundEvent("block.fortified_sanguine.fall");
	public static final RegistryObject<SoundEvent> FORTIFIED_SANGUINE_HIT = HELPER.createSoundEvent("block.fortified_sanguine.hit");
	public static final RegistryObject<SoundEvent> FORTIFIED_SANGUINE_PLACE = HELPER.createSoundEvent("block.fortified_sanguine.place");
	public static final RegistryObject<SoundEvent> FORTIFIED_SANGUINE_STEP = HELPER.createSoundEvent("block.fortified_sanguine.step");

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

	public static final RegistryObject<SoundEvent> BRAZIER_CRACKLE = HELPER.createSoundEvent("block.brazier.crackle");

	public static final RegistryObject<SoundEvent> SADDLED_EGG_BREAK = HELPER.createSoundEvent("block.saddled_egg.break");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_FALL = HELPER.createSoundEvent("block.saddled_egg.fall");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_HIT = HELPER.createSoundEvent("block.saddled_egg.hit");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_PLACE = HELPER.createSoundEvent("block.saddled_egg.place");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_STEP = HELPER.createSoundEvent("block.saddled_egg.step");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_DEFLECT = HELPER.createSoundEvent("block.saddled_egg.deflect");
	public static final RegistryObject<SoundEvent> SADDLED_EGG_HATCH = HELPER.createSoundEvent("block.saddled_egg.hatch");

	public static final RegistryObject<SoundEvent> DIMMER_BREAK = HELPER.createSoundEvent("block.dimmer.break");
	public static final RegistryObject<SoundEvent> DIMMER_FALL = HELPER.createSoundEvent("block.dimmer.fall");
	public static final RegistryObject<SoundEvent> DIMMER_HIT = HELPER.createSoundEvent("block.dimmer.hit");
	public static final RegistryObject<SoundEvent> DIMMER_PLACE = HELPER.createSoundEvent("block.dimmer.place");
	public static final RegistryObject<SoundEvent> DIMMER_STEP = HELPER.createSoundEvent("block.dimmer.step");
	public static final RegistryObject<SoundEvent> DIMMER_DEFLECT = HELPER.createSoundEvent("block.dimmer.deflect");
	public static final RegistryObject<SoundEvent> DIMMER_BUZZ = HELPER.createSoundEvent("block.dimmer.buzz");

	public static final RegistryObject<SoundEvent> REFRACTOR_BREAK = HELPER.createSoundEvent("block.refractor.break");
	public static final RegistryObject<SoundEvent> REFRACTOR_FALL = HELPER.createSoundEvent("block.refractor.fall");
	public static final RegistryObject<SoundEvent> REFRACTOR_HIT = HELPER.createSoundEvent("block.refractor.hit");
	public static final RegistryObject<SoundEvent> REFRACTOR_PLACE = HELPER.createSoundEvent("block.refractor.place");
	public static final RegistryObject<SoundEvent> REFRACTOR_STEP = HELPER.createSoundEvent("block.refractor.step");
	public static final RegistryObject<SoundEvent> REFRACTOR_DEFLECT = HELPER.createSoundEvent("block.refractor.deflect");
	public static final RegistryObject<SoundEvent> REFRACTOR_REFRACT = HELPER.createSoundEvent("block.refractor.refract");

	public static final RegistryObject<SoundEvent> BOUNCER_BREAK = HELPER.createSoundEvent("block.bouncer.break");
	public static final RegistryObject<SoundEvent> BOUNCER_FALL = HELPER.createSoundEvent("block.bouncer.fall");
	public static final RegistryObject<SoundEvent> BOUNCER_HIT = HELPER.createSoundEvent("block.bouncer.hit");
	public static final RegistryObject<SoundEvent> BOUNCER_PLACE = HELPER.createSoundEvent("block.bouncer.place");
	public static final RegistryObject<SoundEvent> BOUNCER_STEP = HELPER.createSoundEvent("block.bouncer.step");
	public static final RegistryObject<SoundEvent> BOUNCER_DEFLECT = HELPER.createSoundEvent("block.bouncer.deflect");
	public static final RegistryObject<SoundEvent> BOUNCER_BOOST = HELPER.createSoundEvent("block.bouncer.boost");

	public static final RegistryObject<SoundEvent> TINPLATE_BREAK = HELPER.createSoundEvent("block.tinplate.break");
	public static final RegistryObject<SoundEvent> TINPLATE_FALL = HELPER.createSoundEvent("block.tinplate.fall");
	public static final RegistryObject<SoundEvent> TINPLATE_HIT = HELPER.createSoundEvent("block.tinplate.hit");
	public static final RegistryObject<SoundEvent> TINPLATE_PLACE = HELPER.createSoundEvent("block.tinplate.place");
	public static final RegistryObject<SoundEvent> TINPLATE_STEP = HELPER.createSoundEvent("block.tinplate.step");
	public static final RegistryObject<SoundEvent> TINPLATE_DEFLECT = HELPER.createSoundEvent("block.tinplate.deflect");
	public static final RegistryObject<SoundEvent> TINPLATE_SECOND_DEFLECT = HELPER.createSoundEvent("block.tinplate.second_deflect");

	public static final RegistryObject<SoundEvent> TIN_BREAK = HELPER.createSoundEvent("block.tin.break");
	public static final RegistryObject<SoundEvent> TIN_FALL = HELPER.createSoundEvent("block.tin.fall");
	public static final RegistryObject<SoundEvent> TIN_HIT = HELPER.createSoundEvent("block.tin.hit");
	public static final RegistryObject<SoundEvent> TIN_PLACE = HELPER.createSoundEvent("block.tin.place");
	public static final RegistryObject<SoundEvent> TIN_STEP = HELPER.createSoundEvent("block.tin.step");
	public static final RegistryObject<SoundEvent> TIN_DEFLECT = HELPER.createSoundEvent("block.tin.deflect");

	public static final RegistryObject<SoundEvent> TIN_BULB_BREAK = HELPER.createSoundEvent("block.tin_bulb.break");
	public static final RegistryObject<SoundEvent> TIN_BULB_FALL = HELPER.createSoundEvent("block.tin_bulb.fall");
	public static final RegistryObject<SoundEvent> TIN_BULB_HIT = HELPER.createSoundEvent("block.tin_bulb.hit");
	public static final RegistryObject<SoundEvent> TIN_BULB_PLACE = HELPER.createSoundEvent("block.tin_bulb.place");
	public static final RegistryObject<SoundEvent> TIN_BULB_STEP = HELPER.createSoundEvent("block.tin_bulb.step");
	public static final RegistryObject<SoundEvent> TIN_BULB_DEFLECT = HELPER.createSoundEvent("block.tin_bulb.deflect");
	public static final RegistryObject<SoundEvent> TIN_BULB_TURN_ON = HELPER.createSoundEvent("block.tin_bulb.turn_on");
	public static final RegistryObject<SoundEvent> TIN_BULB_TURN_OFF = HELPER.createSoundEvent("block.tin_bulb.turn_off");

	public static final RegistryObject<SoundEvent> TIN_CHAIN_BREAK = HELPER.createSoundEvent("block.tin_chain.break");
	public static final RegistryObject<SoundEvent> TIN_CHAIN_FALL = HELPER.createSoundEvent("block.tin_chain.fall");
	public static final RegistryObject<SoundEvent> TIN_CHAIN_HIT = HELPER.createSoundEvent("block.tin_chain.hit");
	public static final RegistryObject<SoundEvent> TIN_CHAIN_PLACE = HELPER.createSoundEvent("block.tin_chain.place");
	public static final RegistryObject<SoundEvent> TIN_CHAIN_STEP = HELPER.createSoundEvent("block.tin_chain.step");
	public static final RegistryObject<SoundEvent> TIN_CHAIN_DEFLECT = HELPER.createSoundEvent("block.tin_chain.deflect");

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

	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_BREAK = HELPER.createSoundEvent("block.cylindrite_tin_ore.break");
	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_FALL = HELPER.createSoundEvent("block.cylindrite_tin_ore.fall");
	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_HIT = HELPER.createSoundEvent("block.cylindrite_tin_ore.hit");
	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_PLACE = HELPER.createSoundEvent("block.cylindrite_tin_ore.place");
	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_STEP = HELPER.createSoundEvent("block.cylindrite_tin_ore.step");
	public static final RegistryObject<SoundEvent> CYLINDRITE_TIN_ORE_DEFLECT = HELPER.createSoundEvent("block.cylindrite_tin_ore.deflect");

	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_BREAK = HELPER.createSoundEvent("block.cassiterite_tin_ore.break");
	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_FALL = HELPER.createSoundEvent("block.cassiterite_tin_ore.fall");
	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_HIT = HELPER.createSoundEvent("block.cassiterite_tin_ore.hit");
	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_PLACE = HELPER.createSoundEvent("block.cassiterite_tin_ore.place");
	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_STEP = HELPER.createSoundEvent("block.cassiterite_tin_ore.step");
	public static final RegistryObject<SoundEvent> CASSITERITE_TIN_ORE_DEFLECT = HELPER.createSoundEvent("block.cassiterite_tin_ore.deflect");

	public static final RegistryObject<SoundEvent> STORAGE_DUCT_BREAK = HELPER.createSoundEvent("block.storage_duct.break");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_FALL = HELPER.createSoundEvent("block.storage_duct.fall");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HIT = HELPER.createSoundEvent("block.storage_duct.hit");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_PLACE = HELPER.createSoundEvent("block.storage_duct.place");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_STEP = HELPER.createSoundEvent("block.storage_duct.step");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_DEFLECT = HELPER.createSoundEvent("block.storage_duct.deflect");

	public static final RegistryObject<SoundEvent> CAVIAR_BREAK = HELPER.createSoundEvent("block.caviar.break");
	public static final RegistryObject<SoundEvent> CAVIAR_FALL = HELPER.createSoundEvent("block.caviar.fall");
	public static final RegistryObject<SoundEvent> CAVIAR_HIT = HELPER.createSoundEvent("block.caviar.hit");
	public static final RegistryObject<SoundEvent> CAVIAR_PLACE = HELPER.createSoundEvent("block.caviar.place");
	public static final RegistryObject<SoundEvent> CAVIAR_STEP = HELPER.createSoundEvent("block.caviar.step");

	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HATCH_OPEN = HELPER.createSoundEvent("block.storage_duct_hatch.open");
	public static final RegistryObject<SoundEvent> STORAGE_DUCT_HATCH_CLOSE = HELPER.createSoundEvent("block.storage_duct_hatch.close");

	public static final RegistryObject<SoundEvent> ROLLER_DOOR_BREAK = HELPER.createSoundEvent("block.roller_door.break");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_FALL = HELPER.createSoundEvent("block.roller_door.fall");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_HIT = HELPER.createSoundEvent("block.roller_door.hit");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_PLACE = HELPER.createSoundEvent("block.roller_door.place");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_STEP = HELPER.createSoundEvent("block.roller_door.step");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_DEFLECT = HELPER.createSoundEvent("block.roller_door.deflect");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_START_ROLL = HELPER.createSoundEvent("block.roller_door.start_roll");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_ROLL = HELPER.createSoundEvent("block.roller_door.roll");
	public static final RegistryObject<SoundEvent> ROLLER_DOOR_STOP_ROLL = HELPER.createSoundEvent("block.roller_door.stop_roll");

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

	public static final RegistryObject<SoundEvent> TIN_BUTTON_CLICK_OFF = HELPER.createSoundEvent("block.tin_button.click_off");
	public static final RegistryObject<SoundEvent> TIN_BUTTON_CLICK_ON = HELPER.createSoundEvent("block.tin_button.click_on");
	public static final RegistryObject<SoundEvent> TIN_BUTTON_HOLD = HELPER.createSoundEvent("block.tin_button.hold");

	public static final RegistryObject<SoundEvent> TIN_PRESSURE_PLATE_CLICK_OFF = HELPER.createSoundEvent("block.tin_pressure_plate.click_off");
	public static final RegistryObject<SoundEvent> TIN_PRESSURE_PLATE_CLICK_ON = HELPER.createSoundEvent("block.tin_pressure_plate.click_on");
	public static final RegistryObject<SoundEvent> TIN_PRESSURE_PLATE_HOLD = HELPER.createSoundEvent("block.tin_pressure_plate.hold");

	public static final RegistryObject<SoundEvent> SCATTERER_SCATTER = HELPER.createSoundEvent("block.scatterer.scatter");
	public static final RegistryObject<SoundEvent> SCATTERER_SPLURT = HELPER.createSoundEvent("block.scatterer.splurt");
	public static final RegistryObject<SoundEvent> SCATTERER_FAIL = HELPER.createSoundEvent("block.scatterer.fail");

	public static final RegistryObject<SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_OFF = HELPER.createSoundEvent("block.medium_weighted_pressure_plate.click_off");
	public static final RegistryObject<SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_ON = HELPER.createSoundEvent("block.medium_weighted_pressure_plate.click_on");

	public static final RegistryObject<SoundEvent> WINCH_WIND = HELPER.createSoundEvent("block.winch.wind");
	public static final RegistryObject<SoundEvent> WINCH_LOCK = HELPER.createSoundEvent("block.winch.lock");
	public static final RegistryObject<SoundEvent> HOOP_SCORE = HELPER.createSoundEvent("block.hoop.score");
	public static final RegistryObject<SoundEvent> HOOP_SHRINK = HELPER.createSoundEvent("block.hoop.shrink");
	public static final RegistryObject<SoundEvent> HOOP_EXPAND = HELPER.createSoundEvent("block.hoop.expand");
	public static final RegistryObject<SoundEvent> RESISTOR_BUZZ = HELPER.createSoundEvent("block.resistor.buzz");
	public static final RegistryObject<SoundEvent> RESISTOR_TOGGLE = HELPER.createSoundEvent("block.resistor.toggle");

	public static final RegistryObject<SoundEvent> HALT_RAIL_HALT = HELPER.createSoundEvent("block.halt_rail.halt");
	public static final RegistryObject<SoundEvent> HALT_RAIL_EXTEND = HELPER.createSoundEvent("block.halt_rail.extend");
	public static final RegistryObject<SoundEvent> HALT_RAIL_CONTRACT = HELPER.createSoundEvent("block.halt_rail.contract");
	public static final RegistryObject<SoundEvent> SLAUGHTER_RAIL_EXTEND = HELPER.createSoundEvent("block.slaughter_rail.extend");
	public static final RegistryObject<SoundEvent> SLAUGHTER_RAIL_CONTRACT = HELPER.createSoundEvent("block.slaughter_rail.contract");
	public static final RegistryObject<SoundEvent> SPIKED_RAIL_EXTEND = HELPER.createSoundEvent("block.spiked_rail.extend");
	public static final RegistryObject<SoundEvent> SPIKED_RAIL_CONTRACT = HELPER.createSoundEvent("block.spiked_rail.contract");

	public static final RegistryObject<SoundEvent> TOOLBOX_OPEN = HELPER.createSoundEvent("block.toolbox.open");
	public static final RegistryObject<SoundEvent> TOOLBOX_CLOSE = HELPER.createSoundEvent("block.toolbox.close");

	public static final RegistryObject<SoundEvent> DISMANTLING_TABLE_USE = HELPER.createSoundEvent("block.dismantling_table.use");

	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_MIME = HELPER.createSoundEvent("block.note_block.imitate.mime");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_DEEPER = HELPER.createSoundEvent("block.note_block.imitate.deeper");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_EVENDEEPER = HELPER.createSoundEvent("block.note_block.imitate.evendeeper");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_PEEPER = HELPER.createSoundEvent("block.note_block.imitate.peeper");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_IMITATE_WARDEN = HELPER.createSoundEvent("block.note_block.imitate.warden");

	public static final RegistryObject<SoundEvent> CAVIAR_EAT = HELPER.createSoundEvent("item.caviar.eat");
	public static final RegistryObject<SoundEvent> CAVIAR_BURP = HELPER.createSoundEvent("item.caviar.burp");

	public static final RegistryObject<SoundEvent> TINPLATE_WAX = HELPER.createSoundEvent("item.tinplate.wax");

	public static final RegistryObject<SoundEvent> TUNING_FORK_VIBRATE = HELPER.createSoundEvent("item.tuning_fork.vibrate");

	public static final RegistryObject<SoundEvent> TETHER_POTION_EQUIP = HELPER.createSoundEvent("item.tether_potion.equip");
	public static final RegistryObject<SoundEvent> TETHER_POTION_BREAK = HELPER.createSoundEvent("item.tether_potion.break");

	public static final RegistryObject<SoundEvent> MONOCLE_USE = HELPER.createSoundEvent("item.monocle.use");
	public static final RegistryObject<SoundEvent> MONOCLE_STOP_USING = HELPER.createSoundEvent("item.monocle.stop_using");
	public static final RegistryObject<SoundEvent> MONOCLE_EQUIP = HELPER.createSoundEvent("item.monocle.equip");

	public static final RegistryObject<SoundEvent> AEGIS_DEFLECT = HELPER.createSoundEvent("item.aegis.deflect");
	public static final RegistryObject<SoundEvent> AEGIS_STUN = HELPER.createSoundEvent("item.aegis.stun");

	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_COPPER = HELPER.createSoundEvent("item.armor.equip_copper");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SILVER = HELPER.createSoundEvent("item.armor.equip_silver");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_NECROMIUM = HELPER.createSoundEvent("item.armor.equip_necromium");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SANGUINE = HELPER.createSoundEvent("item.armor.equip_sanguine");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_COWL = HELPER.createSoundEvent("item.armor.equip_cowl");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_TOOLBELT = HELPER.createSoundEvent("item.armor.equip_toolbelt");

	public static final RegistryObject<SoundEvent> NECROMIUM_INFLICT = HELPER.createSoundEvent("item.armor.necromium_inflict");
	public static final RegistryObject<SoundEvent> SILVER_RESIST = HELPER.createSoundEvent("item.armor.silver_resist");
	public static final RegistryObject<SoundEvent> SANGUINE_HEAL = HELPER.createSoundEvent("item.armor.sanguine_heal");

	public static final RegistryObject<SoundEvent> UNICORN_HORN_EQUIP = HELPER.createSoundEvent("item.unicorn_horn.equip");
	public static final RegistryObject<SoundEvent> UNICORN_HORN_UNEQUIP = HELPER.createSoundEvent("item.unicorn_horn.unequip");

	public static final RegistryObject<SoundEvent> SILVER_STRIKE = HELPER.createSoundEvent("item.silver.strike");

	public static final RegistryObject<SoundEvent> BEJEWELED_APPLE_EAT = HELPER.createSoundEvent("item.bejeweled_apple.eat");
	public static final RegistryObject<SoundEvent> BEJEWELED_APPLE_BURP = HELPER.createSoundEvent("item.bejeweled_apple.burp");

	public static final RegistryObject<SoundEvent> BEJEWELED_PEARL_TELEPORT = HELPER.createSoundEvent("item.bejeweled_pearl.teleport");
	public static final RegistryObject<SoundEvent> BEJEWELED_PEARL_CRUMBLE = HELPER.createSoundEvent("item.bejeweled_pearl.crumble");

	public static final RegistryObject<SoundEvent> ZIRCONIA_ANVIL_USE = HELPER.createSoundEvent("item.zirconia.anvil_use");

	public static final RegistryObject<SoundEvent> DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");
	public static final RegistryObject<SoundEvent> DEEPER_EXPLODE = HELPER.createSoundEvent("entity.deeper.explode");

	public static final RegistryObject<SoundEvent> EVENDEEPER_DEATH = HELPER.createSoundEvent("entity.evendeeper.death");
	public static final RegistryObject<SoundEvent> EVENDEEPER_HURT = HELPER.createSoundEvent("entity.evendeeper.hurt");
	public static final RegistryObject<SoundEvent> EVENDEEPER_PRIMED = HELPER.createSoundEvent("entity.evendeeper.primed");
	public static final RegistryObject<SoundEvent> EVENDEEPER_EXPLODE = HELPER.createSoundEvent("entity.evendeeper.explode");

	public static final RegistryObject<SoundEvent> PEEPER_DEATH = HELPER.createSoundEvent("entity.peeper.death");
	public static final RegistryObject<SoundEvent> PEEPER_HURT = HELPER.createSoundEvent("entity.peeper.hurt");
	public static final RegistryObject<SoundEvent> PEEPER_PULSE = HELPER.createSoundEvent("entity.peeper.pulse");
	public static final RegistryObject<SoundEvent> PEEPER_PRIMED = HELPER.createSoundEvent("entity.peeper.primed");
	public static final RegistryObject<SoundEvent> PEEPER_EXPLODE = HELPER.createSoundEvent("entity.peeper.explode");

	public static final RegistryObject<SoundEvent> MIME_DEATH = HELPER.createSoundEvent("entity.mime.death");
	public static final RegistryObject<SoundEvent> MIME_HURT = HELPER.createSoundEvent("entity.mime.hurt");
	public static final RegistryObject<SoundEvent> MIME_MIME = HELPER.createSoundEvent("entity.mime.mime");
	public static final RegistryObject<SoundEvent> MIME_IMPERSONATE = HELPER.createSoundEvent("entity.mime.impersonate");
	public static final RegistryObject<SoundEvent> MIME_CONVERT = HELPER.createSoundEvent("entity.mime.convert");

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

	public static final RegistryObject<SoundEvent> GRAZER_AMBIENT = HELPER.createSoundEvent("entity.grazer.ambient");
	public static final RegistryObject<SoundEvent> GRAZER_HURT = HELPER.createSoundEvent("entity.grazer.hurt");
	public static final RegistryObject<SoundEvent> GRAZER_DEATH = HELPER.createSoundEvent("entity.grazer.death");
	public static final RegistryObject<SoundEvent> GRAZER_DEFLECT = HELPER.createSoundEvent("entity.grazer.deflect");
	public static final RegistryObject<SoundEvent> GRAZER_RICOCHET = HELPER.createSoundEvent("entity.grazer.ricochet");
	public static final RegistryObject<SoundEvent> GRAZER_CHARGE = HELPER.createSoundEvent("entity.grazer.charge");
	public static final RegistryObject<SoundEvent> GRAZER_STRUGGLE = HELPER.createSoundEvent("entity.grazer.struggle");
	public static final RegistryObject<SoundEvent> GRAZER_STEP = HELPER.createSoundEvent("entity.grazer.step");
	public static final RegistryObject<SoundEvent> GRAZER_MOUNT = HELPER.createSoundEvent("entity.grazer.mount");
	public static final RegistryObject<SoundEvent> GRAZER_DISMOUNT = HELPER.createSoundEvent("entity.grazer.dismount");

	public static final RegistryObject<SoundEvent> RAT_DEATH = HELPER.createSoundEvent("entity.rat.death");
	public static final RegistryObject<SoundEvent> RAT_HURT = HELPER.createSoundEvent("entity.rat.hurt");
	public static final RegistryObject<SoundEvent> RAT_AMBIENT = HELPER.createSoundEvent("entity.rat.ambient");
	public static final RegistryObject<SoundEvent> RAT_STEP = HELPER.createSoundEvent("entity.rat.step");
	public static final RegistryObject<SoundEvent> RAT_ANGRY = HELPER.createSoundEvent("entity.rat.angry");
	public static final RegistryObject<SoundEvent> RAT_HAPPY = HELPER.createSoundEvent("entity.rat.happy");
	public static final RegistryObject<SoundEvent> RAT_EAT = HELPER.createSoundEvent("entity.rat.eat");
	public static final RegistryObject<SoundEvent> RAT_ATTACK = HELPER.createSoundEvent("entity.rat.attack");
	public static final RegistryObject<SoundEvent> RAT_LATCH = HELPER.createSoundEvent("entity.rat.latch");
	public static final RegistryObject<SoundEvent> RAT_SPIT = HELPER.createSoundEvent("entity.rat.spit");
	public static final RegistryObject<SoundEvent> RAT_WOUNDED = HELPER.createSoundEvent("entity.rat.wounded");

	public static final RegistryObject<SoundEvent> CAVEFISH_DEATH = HELPER.createSoundEvent("entity.cavefish.death");
	public static final RegistryObject<SoundEvent> CAVEFISH_HURT = HELPER.createSoundEvent("entity.cavefish.hurt");
	public static final RegistryObject<SoundEvent> CAVEFISH_AMBIENT = HELPER.createSoundEvent("entity.cavefish.ambient");
	public static final RegistryObject<SoundEvent> CAVEFISH_FLOP = HELPER.createSoundEvent("entity.cavefish.flop");

	public static final RegistryObject<SoundEvent> BLUNT_ARROW_HIT = HELPER.createSoundEvent("entity.blunt_arrow.hit");
	public static final RegistryObject<SoundEvent> LARGE_ARROW_HIT = HELPER.createSoundEvent("entity.large_arrow.hit");
	public static final RegistryObject<SoundEvent> RICOCHET_ARROW_HIT = HELPER.createSoundEvent("entity.ricochet_arrow.hit");
	public static final RegistryObject<SoundEvent> RICOCHET_ARROW_DEFLECT = HELPER.createSoundEvent("entity.ricochet_arrow.deflect");

	public static final RegistryObject<SoundEvent> KUNAI_HIT = HELPER.createSoundEvent("entity.kunai.hit");
	public static final RegistryObject<SoundEvent> KUNAI_THROW = HELPER.createSoundEvent("entity.kunai.throw");

	public static final RegistryObject<SoundEvent> REWIND = HELPER.createSoundEvent("effect.rewind.rewind");
	public static final RegistryObject<SoundEvent> DRAIN = HELPER.createSoundEvent("effect.vampirism.drain");

	public static final RegistryObject<SoundEvent> PARROT_IMITATE_DEEPER = HELPER.createSoundEvent("entity.parrot.imitate.deeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_EVENDEEPER = HELPER.createSoundEvent("entity.parrot.imitate.evendeeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_PEEPER = HELPER.createSoundEvent("entity.parrot.imitate.peeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_MIME = HELPER.createSoundEvent("entity.parrot.imitate.mime");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_GRAZER = HELPER.createSoundEvent("entity.parrot.imitate.grazer");

	public static final RegistryObject<SoundEvent> NOTE_BLOCK_STATIC = HELPER.createSoundEvent("block.note_block.static");
	public static final RegistryObject<SoundEvent> NOTE_BLOCK_ALARM = HELPER.createSoundEvent("block.note_block.alarm");

	public static final ImmutableList<RegistryObject<SoundEvent>> GOAT_HORN_SOUND_VARIANTS = registerGoatHornSoundVariants();

	public static final ImmutableList<RegistryObject<SoundEvent>> LOST_GOAT_HORN_SOUND_VARIANTS = registerLostGoatHornSoundVariants();

	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_HARMONY_SOUND_VARIANTS = registerCopperHornSoundVariants("harmony");
	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_MELODY_SOUND_VARIANTS = registerCopperHornSoundVariants("melody");
	public static final ImmutableList<RegistryObject<SoundEvent>> COPPER_HORN_BASS_SOUND_VARIANTS = registerCopperHornSoundVariants("bass");

	public static final RegistryObject<SoundEvent> BONE_FLUTE_SIT = HELPER.createSoundEvent("item.bone_flute.sit");
	public static final RegistryObject<SoundEvent> BONE_FLUTE_RECALL = HELPER.createSoundEvent("item.bone_flute.recall");
	public static final RegistryObject<SoundEvent> BONE_FLUTE_MOVE = HELPER.createSoundEvent("item.bone_flute.move");
	public static final RegistryObject<SoundEvent> BONE_FLUTE_ATTACK = HELPER.createSoundEvent("item.bone_flute.attack");

	public static final RegistryObject<SoundEvent> PACKING_CONTAINER_DROP_CONTENTS = HELPER.createSoundEvent("item.packing_container.drop_contents");
	public static final RegistryObject<SoundEvent> PACKING_CONTAINER_INSERT = HELPER.createSoundEvent("item.packing_container.insert");
	public static final RegistryObject<SoundEvent> PACKING_CONTAINER_INSERT_FAIL = HELPER.createSoundEvent("item.packing_container.insert_fail");
	public static final RegistryObject<SoundEvent> PACKING_CONTAINER_REMOVE_ONE = HELPER.createSoundEvent("item.packing_container.remove_one");

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
		public static final ForgeSoundType FRAGILE_STONE = new ForgeSoundType(1.0F, 1.0F, FRAGILE_STONE_BREAK, FRAGILE_STONE_STEP, FRAGILE_STONE_PLACE, FRAGILE_STONE_HIT, FRAGILE_STONE_FALL);
		public static final ForgeSoundType FRAGILE_DEEPSLATE = new ForgeSoundType(1.0F, 1.0F, FRAGILE_DEEPSLATE_BREAK, FRAGILE_DEEPSLATE_STEP, FRAGILE_DEEPSLATE_PLACE, FRAGILE_DEEPSLATE_HIT, FRAGILE_DEEPSLATE_FALL);
		public static final ForgeSoundType CHARCOAL = new ForgeSoundType(1.0F, 1.0F, CHARCOAL_BREAK, CHARCOAL_STEP, CHARCOAL_PLACE, CHARCOAL_HIT, CHARCOAL_FALL);
		public static final ForgeSoundType ECHO_BLOCK = new ForgeSoundType(1.0F, 1.0F, ECHO_BLOCK_BREAK, ECHO_BLOCK_STEP, ECHO_BLOCK_PLACE, ECHO_BLOCK_HIT, ECHO_BLOCK_FALL);
		public static final ForgeSoundType GUNPOWDER = new ForgeSoundType(1.0F, 1.0F, GUNPOWDER_BREAK, GUNPOWDER_STEP, GUNPOWDER_PLACE, GUNPOWDER_HIT, GUNPOWDER_FALL);
		public static final ForgeSoundType SPARKLER = new ForgeSoundType(1.0F, 1.0F, SPARKLER_BREAK, SPARKLER_STEP, SPARKLER_PLACE, SPARKLER_HIT, SPARKLER_FALL);
		public static final ForgeSoundType RHYOLITE = new ForgeSoundType(1.0F, 1.0F, RHYOLITE_BREAK, RHYOLITE_STEP, RHYOLITE_PLACE, RHYOLITE_HIT, RHYOLITE_FALL);
		public static final ForgeSoundType MAGMATIC_RHYOLITE = new ForgeSoundType(1.0F, 1.0F, MAGMATIC_RHYOLITE_BREAK, MAGMATIC_RHYOLITE_STEP, MAGMATIC_RHYOLITE_PLACE, MAGMATIC_RHYOLITE_HIT, MAGMATIC_RHYOLITE_FALL);
		public static final ForgeSoundType CASSITERITE = new TinSoundType(1.0F, 1.0F, CASSITERITE_BREAK, CASSITERITE_STEP, CASSITERITE_PLACE, CASSITERITE_HIT, CASSITERITE_FALL, CASSITERITE_DEFLECT);
		public static final ForgeSoundType CYLINDRITE = new TinSoundType(1.0F, 1.0F, CYLINDRITE_BREAK, CYLINDRITE_STEP, CYLINDRITE_PLACE, CYLINDRITE_HIT, CYLINDRITE_FALL, CYLINDRITE_DEFLECT);
		public static final ForgeSoundType FLINT_BLOCK = new ForgeSoundType(1.0F, 1.0F, FLINT_BLOCK_BREAK, FLINT_BLOCK_STEP, FLINT_BLOCK_PLACE, FLINT_BLOCK_HIT, FLINT_BLOCK_FALL);
		public static final ForgeSoundType SOUL_SILVER_ORE = new ForgeSoundType(1.0F, 1.0F, SOUL_SILVER_ORE_BREAK, SOUL_SILVER_ORE_STEP, SOUL_SILVER_ORE_PLACE, SOUL_SILVER_ORE_HIT, SOUL_SILVER_ORE_FALL);
		public static final ForgeSoundType SPINEL = new ForgeSoundType(1.0F, 1.0F, SPINEL_BREAK, SPINEL_STEP, SPINEL_PLACE, SPINEL_HIT, SPINEL_FALL);
		public static final ForgeSoundType SUGILITE = new ForgeSoundType(1.0F, 1.0F, SUGILITE_BREAK, SUGILITE_STEP, SUGILITE_PLACE, SUGILITE_HIT, SUGILITE_FALL);
		public static final ForgeSoundType ZIRCONIA = new ForgeSoundType(1.0F, 1.0F, ZIRCONIA_BREAK, ZIRCONIA_STEP, ZIRCONIA_PLACE, ZIRCONIA_HIT, ZIRCONIA_FALL);
		public static final ForgeSoundType TURQUOISE = new ForgeSoundType(1.0F, 1.0F, TURQUOISE_BREAK, TURQUOISE_STEP, TURQUOISE_PLACE, TURQUOISE_HIT, TURQUOISE_FALL);
		public static final ForgeSoundType CAVIAR = new ForgeSoundType(1.0F, 1.0F, CAVIAR_BREAK, CAVIAR_STEP, CAVIAR_PLACE, CAVIAR_HIT, CAVIAR_FALL);
		public static final ForgeSoundType ORNATE_GLASS = new ForgeSoundType(1.0F, 1.0F, ORNATE_GLASS_BREAK, ORNATE_GLASS_STEP, ORNATE_GLASS_PLACE, ORNATE_GLASS_HIT, ORNATE_GLASS_FALL);
		public static final ForgeSoundType FLOAT_GLASS = new TinSoundType(1.0F, 1.0F, FLOAT_GLASS_BREAK, FLOAT_GLASS_STEP, FLOAT_GLASS_PLACE, FLOAT_GLASS_HIT, FLOAT_GLASS_FALL, FLOAT_GLASS_DEFLECT);
		public static final ForgeSoundType NECROMIUM = new ForgeSoundType(1.0F, 1.0F, NECROMIUM_BREAK, NECROMIUM_STEP, NECROMIUM_PLACE, NECROMIUM_HIT, NECROMIUM_FALL);
		public static final ForgeSoundType POLISHED_DRIPSTONE = new ForgeSoundType(1.0F, 1.0F, POLISHED_DRIPSTONE_BREAK, POLISHED_DRIPSTONE_STEP, POLISHED_DRIPSTONE_PLACE, POLISHED_DRIPSTONE_HIT, POLISHED_DRIPSTONE_FALL);
		public static final ForgeSoundType FALSE_HOPE = new ForgeSoundType(1.0F, 1.0F, FALSE_HOPE_BREAK, FALSE_HOPE_STEP, FALSE_HOPE_PLACE, FALSE_HOPE_HIT, FALSE_HOPE_FALL);
		public static final ForgeSoundType CAVE_GROWTHS = new ForgeSoundType(1.0F, 1.0F, CAVE_GROWTHS_BREAK, CAVE_GROWTHS_STEP, CAVE_GROWTHS_PLACE, CAVE_GROWTHS_HIT, CAVE_GROWTHS_FALL);
		public static final ForgeSoundType MOSCHATEL = new ForgeSoundType(1.0F, 1.0F, MOSCHATEL_BREAK, MOSCHATEL_STEP, MOSCHATEL_PLACE, MOSCHATEL_HIT, MOSCHATEL_FALL);
		public static final ForgeSoundType TMT = new ForgeSoundType(1.0F, 1.0F, TMT_BREAK, TMT_STEP, TMT_PLACE, TMT_HIT, TMT_FALL);
		public static final ForgeSoundType SANGUINE = new ForgeSoundType(1.0F, 1.0F, SANGUINE_BREAK, SANGUINE_STEP, SANGUINE_PLACE, SANGUINE_HIT, SANGUINE_FALL);
		public static final ForgeSoundType FORTIFIED_SANGUINE = new ForgeSoundType(1.0F, 1.0F, FORTIFIED_SANGUINE_BREAK, FORTIFIED_SANGUINE_STEP, FORTIFIED_SANGUINE_PLACE, FORTIFIED_SANGUINE_HIT, FORTIFIED_SANGUINE_FALL);
		public static final ForgeSoundType ROTTEN_FLESH = new ForgeSoundType(1.0F, 1.0F, ROTTEN_FLESH_BREAK, ROTTEN_FLESH_STEP, ROTTEN_FLESH_PLACE, ROTTEN_FLESH_HIT, ROTTEN_FLESH_FALL);
		public static final ForgeSoundType LAVA_LAMP = new ForgeSoundType(1.0F, 1.0F, LAVA_LAMP_BREAK, LAVA_LAMP_STEP, LAVA_LAMP_PLACE, LAVA_LAMP_HIT, LAVA_LAMP_FALL);
		public static final ForgeSoundType FLOODLIGHT = new ForgeSoundType(1.0F, 1.0F, FLOODLIGHT_BREAK, FLOODLIGHT_STEP, FLOODLIGHT_PLACE, FLOODLIGHT_HIT, FLOODLIGHT_FALL);
		public static final ForgeSoundType COPPER_LANTERN = new ForgeSoundType(1.0F, 1.0F, COPPER_LANTERN_BREAK, COPPER_LANTERN_STEP, COPPER_LANTERN_PLACE, COPPER_LANTERN_HIT, COPPER_LANTERN_FALL);
		public static final ForgeSoundType COPPER_CHAIN = new ForgeSoundType(1.0F, 1.0F, COPPER_CHAIN_BREAK, COPPER_CHAIN_STEP, COPPER_CHAIN_PLACE, COPPER_CHAIN_HIT, COPPER_CHAIN_FALL);
		public static final ForgeSoundType COPPER_BULB = new ForgeSoundType(1.0F, 1.0F, COPPER_BULB_BREAK, COPPER_BULB_STEP, COPPER_BULB_PLACE, COPPER_BULB_HIT, COPPER_BULB_FALL);
		public static final ForgeSoundType COPPER_GRATE = new ForgeSoundType(1.0F, 1.0F, COPPER_GRATE_BREAK, COPPER_GRATE_STEP, COPPER_GRATE_PLACE, COPPER_GRATE_HIT, COPPER_GRATE_FALL);
		public static final ForgeSoundType SILVER = new ForgeSoundType(1.0F, 1.0F, SILVER_BREAK, SILVER_STEP, SILVER_PLACE, SILVER_HIT, SILVER_FALL);
		public static final ForgeSoundType TIN = new TinSoundType(1.0F, 1.0F, TIN_BREAK, TIN_STEP, TIN_PLACE, TIN_HIT, TIN_FALL, TIN_DEFLECT);
		public static final ForgeSoundType TINPLATE = new TinSoundType(1.0F, 1.0F, TINPLATE_BREAK, TINPLATE_STEP, TINPLATE_PLACE, TINPLATE_HIT, TINPLATE_FALL, TINPLATE_DEFLECT);
		public static final ForgeSoundType DIMMER = new TinSoundType(1.0F, 1.0F, DIMMER_BREAK, DIMMER_STEP, DIMMER_PLACE, DIMMER_HIT, DIMMER_FALL, DIMMER_DEFLECT);
		public static final ForgeSoundType REFRACTOR = new TinSoundType(1.0F, 1.0F, REFRACTOR_BREAK, REFRACTOR_STEP, REFRACTOR_PLACE, REFRACTOR_HIT, REFRACTOR_FALL, REFRACTOR_DEFLECT);
		public static final ForgeSoundType BOUNCER = new TinSoundType(1.0F, 1.0F, BOUNCER_BREAK, BOUNCER_STEP, BOUNCER_PLACE, BOUNCER_HIT, BOUNCER_FALL, BOUNCER_DEFLECT);
		public static final ForgeSoundType SADDLED_EGG = new TinSoundType(1.0F, 1.0F, SADDLED_EGG_BREAK, SADDLED_EGG_STEP, SADDLED_EGG_PLACE, SADDLED_EGG_HIT, SADDLED_EGG_FALL, SADDLED_EGG_DEFLECT);
		public static final ForgeSoundType TIN_BULB = new TinSoundType(1.0F, 1.0F, TIN_BULB_BREAK, TIN_BULB_STEP, TIN_BULB_PLACE, TIN_BULB_HIT, TIN_BULB_FALL, TIN_BULB_DEFLECT);
		public static final ForgeSoundType TIN_CHAIN = new TinSoundType(1.0F, 1.0F, TIN_CHAIN_BREAK, TIN_CHAIN_STEP, TIN_CHAIN_PLACE, TIN_CHAIN_HIT, TIN_CHAIN_FALL, TIN_CHAIN_DEFLECT);
		public static final ForgeSoundType TIN_ORE = new TinSoundType(1.0F, 1.0F, TIN_ORE_BREAK, TIN_ORE_STEP, TIN_ORE_PLACE, TIN_ORE_HIT, TIN_ORE_FALL, TIN_ORE_DEFLECT);
		public static final ForgeSoundType DEEPSLATE_TIN_ORE = new TinSoundType(1.0F, 1.0F, DEEPSLATE_TIN_ORE_BREAK, DEEPSLATE_TIN_ORE_STEP, DEEPSLATE_TIN_ORE_PLACE, DEEPSLATE_TIN_ORE_HIT, DEEPSLATE_TIN_ORE_FALL, DEEPSLATE_TIN_ORE_DEFLECT);
		public static final ForgeSoundType CYLINDRITE_TIN_ORE = new TinSoundType(1.0F, 1.0F, CYLINDRITE_TIN_ORE_BREAK, CYLINDRITE_TIN_ORE_STEP, CYLINDRITE_TIN_ORE_PLACE, CYLINDRITE_TIN_ORE_HIT, CYLINDRITE_TIN_ORE_FALL, CYLINDRITE_TIN_ORE_DEFLECT);
		public static final ForgeSoundType CASSITERITE_TIN_ORE = new TinSoundType(1.0F, 1.0F, CASSITERITE_TIN_ORE_BREAK, CASSITERITE_TIN_ORE_STEP, CASSITERITE_TIN_ORE_PLACE, CASSITERITE_TIN_ORE_HIT, CASSITERITE_TIN_ORE_FALL, CASSITERITE_TIN_ORE_DEFLECT);
		public static final ForgeSoundType STORAGE_DUCT = new TinSoundType(1.0F, 1.0F, STORAGE_DUCT_BREAK, STORAGE_DUCT_STEP, STORAGE_DUCT_PLACE, STORAGE_DUCT_HIT, STORAGE_DUCT_FALL, STORAGE_DUCT_DEFLECT);
		public static final ForgeSoundType ROLLER_DOOR = new TinSoundType(1.0F, 1.0F, ROLLER_DOOR_BREAK, ROLLER_DOOR_STEP, ROLLER_DOOR_PLACE, ROLLER_DOOR_HIT, ROLLER_DOOR_FALL, ROLLER_DOOR_DEFLECT);
		public static final ForgeSoundType ATONING_TABLE = new ForgeSoundType(1.0F, 1.0F, ATONING_TABLE_BREAK, ATONING_TABLE_STEP, ATONING_TABLE_PLACE, ATONING_TABLE_HIT, ATONING_TABLE_FALL);
		public static final ForgeSoundType BEJEWELED_ANVIL = new ForgeSoundType(1.0F, 1.0F, BEJEWELED_ANVIL_BREAK, BEJEWELED_ANVIL_STEP, BEJEWELED_ANVIL_PLACE, BEJEWELED_ANVIL_HIT, BEJEWELED_ANVIL_FALL);
		public static final ForgeSoundType POLISHED_TUFF = new ForgeSoundType(1.0F, 1.0F, POLISHED_TUFF_BREAK, POLISHED_TUFF_STEP, POLISHED_TUFF_PLACE, POLISHED_TUFF_HIT, POLISHED_TUFF_FALL);
		public static final ForgeSoundType TUFF_BRICKS = new ForgeSoundType(1.0F, 1.0F, TUFF_BRICKS_BREAK, TUFF_BRICKS_STEP, TUFF_BRICKS_PLACE, TUFF_BRICKS_HIT, TUFF_BRICKS_FALL);
	}

	public static void registerNoteBlocks() {
		registerHeadInstrument(CCBlocks.MIME_HEAD, NOTE_BLOCK_IMITATE_MIME);
		registerHeadInstrument(CCBlocks.DEEPER_HEAD, NOTE_BLOCK_IMITATE_DEEPER);
		registerHeadInstrument(CCBlocks.EVENDEEPER_HEAD, NOTE_BLOCK_IMITATE_EVENDEEPER);
		registerHeadInstrument(CCBlocks.PEEPER_HEAD, NOTE_BLOCK_IMITATE_PEEPER);
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(CCBlockTags.WARDEN_NOTE_BLOCKS), NOTE_BLOCK_IMITATE_WARDEN.get(), false));
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(CCBlockTags.STATIC_NOTE_BLOCKS), NOTE_BLOCK_STATIC.get(), false));
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(CCBlockTags.ALARM_NOTE_BLOCKS), NOTE_BLOCK_ALARM.get(), false));
	}

	public static void registerHeadInstrument(RegistryObject<Block> block, RegistryObject<SoundEvent> soundEvent) {
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.getBlockState().is(block.get()), soundEvent.get(), true));
	}
}