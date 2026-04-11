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
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.IntStream;

public class CCSoundEvents {
	public static final SoundSubRegistryHelper SOUNDS = CavernsAndChasms.REGISTRY_HELPER.getSoundSubHelper();

	public static final DeferredHolder<SoundEvent, SoundEvent> ANALOGUE = SOUNDS.createSoundEvent("music.record.analogue");
	public static final DeferredHolder<SoundEvent, SoundEvent> EPILOGUE = SOUNDS.createSoundEvent("music.record.epilogue");

	public static final DeferredHolder<SoundEvent, SoundEvent> ROCKY_DIRT_BREAK = SOUNDS.createSoundEvent("block.rocky_dirt.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROCKY_DIRT_FALL = SOUNDS.createSoundEvent("block.rocky_dirt.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROCKY_DIRT_HIT = SOUNDS.createSoundEvent("block.rocky_dirt.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROCKY_DIRT_PLACE = SOUNDS.createSoundEvent("block.rocky_dirt.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROCKY_DIRT_STEP = SOUNDS.createSoundEvent("block.rocky_dirt.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_STONE_BREAK = SOUNDS.createSoundEvent("block.fragile_stone.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_STONE_FALL = SOUNDS.createSoundEvent("block.fragile_stone.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_STONE_HIT = SOUNDS.createSoundEvent("block.fragile_stone.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_STONE_PLACE = SOUNDS.createSoundEvent("block.fragile_stone.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_STONE_STEP = SOUNDS.createSoundEvent("block.fragile_stone.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_DEEPSLATE_BREAK = SOUNDS.createSoundEvent("block.fragile_deepslate.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_DEEPSLATE_FALL = SOUNDS.createSoundEvent("block.fragile_deepslate.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_DEEPSLATE_HIT = SOUNDS.createSoundEvent("block.fragile_deepslate.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_DEEPSLATE_PLACE = SOUNDS.createSoundEvent("block.fragile_deepslate.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FRAGILE_DEEPSLATE_STEP = SOUNDS.createSoundEvent("block.fragile_deepslate.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> CHARCOAL_BREAK = SOUNDS.createSoundEvent("block.charcoal.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CHARCOAL_FALL = SOUNDS.createSoundEvent("block.charcoal.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CHARCOAL_HIT = SOUNDS.createSoundEvent("block.charcoal.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CHARCOAL_PLACE = SOUNDS.createSoundEvent("block.charcoal.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CHARCOAL_STEP = SOUNDS.createSoundEvent("block.charcoal.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_BLOCK_BREAK = SOUNDS.createSoundEvent("block.echo_block.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_BLOCK_FALL = SOUNDS.createSoundEvent("block.echo_block.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_BLOCK_HIT = SOUNDS.createSoundEvent("block.echo_block.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_BLOCK_PLACE = SOUNDS.createSoundEvent("block.echo_block.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_BLOCK_STEP = SOUNDS.createSoundEvent("block.echo_block.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_BREAK = SOUNDS.createSoundEvent("block.gunpowder.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_FALL = SOUNDS.createSoundEvent("block.gunpowder.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_HIT = SOUNDS.createSoundEvent("block.gunpowder.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_PLACE = SOUNDS.createSoundEvent("block.gunpowder.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_STEP = SOUNDS.createSoundEvent("block.gunpowder.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> GUNPOWDER_EXPLODE = SOUNDS.createSoundEvent("block.gunpowder.explode");

	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_BREAK = SOUNDS.createSoundEvent("block.sparkler.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_FALL = SOUNDS.createSoundEvent("block.sparkler.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_HIT = SOUNDS.createSoundEvent("block.sparkler.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_PLACE = SOUNDS.createSoundEvent("block.sparkler.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_STEP = SOUNDS.createSoundEvent("block.sparkler.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_SPARKLE = SOUNDS.createSoundEvent("block.sparkler.sparkle");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_FIZZLE = SOUNDS.createSoundEvent("block.sparkler.fizzle");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPARKLER_EXPLODE = SOUNDS.createSoundEvent("block.sparkler.explode");
	
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BREAK = SOUNDS.createSoundEvent("block.shale.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_FALL = SOUNDS.createSoundEvent("block.shale.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_HIT = SOUNDS.createSoundEvent("block.shale.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_PLACE = SOUNDS.createSoundEvent("block.shale.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_STEP = SOUNDS.createSoundEvent("block.shale.step");
	
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_SHALE_BREAK = SOUNDS.createSoundEvent("block.polished_shale.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_SHALE_FALL = SOUNDS.createSoundEvent("block.polished_shale.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_SHALE_HIT = SOUNDS.createSoundEvent("block.polished_shale.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_SHALE_PLACE = SOUNDS.createSoundEvent("block.polished_shale.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_SHALE_STEP = SOUNDS.createSoundEvent("block.polished_shale.step");
	
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BRICKS_BREAK = SOUNDS.createSoundEvent("block.shale_bricks.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BRICKS_FALL = SOUNDS.createSoundEvent("block.shale_bricks.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BRICKS_HIT = SOUNDS.createSoundEvent("block.shale_bricks.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BRICKS_PLACE = SOUNDS.createSoundEvent("block.shale_bricks.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SHALE_BRICKS_STEP = SOUNDS.createSoundEvent("block.shale_bricks.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> RHYOLITE_BREAK = SOUNDS.createSoundEvent("block.rhyolite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> RHYOLITE_FALL = SOUNDS.createSoundEvent("block.rhyolite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> RHYOLITE_HIT = SOUNDS.createSoundEvent("block.rhyolite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> RHYOLITE_PLACE = SOUNDS.createSoundEvent("block.rhyolite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> RHYOLITE_STEP = SOUNDS.createSoundEvent("block.rhyolite.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMATIC_RHYOLITE_BREAK = SOUNDS.createSoundEvent("block.magmatic_rhyolite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMATIC_RHYOLITE_FALL = SOUNDS.createSoundEvent("block.magmatic_rhyolite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMATIC_RHYOLITE_HIT = SOUNDS.createSoundEvent("block.magmatic_rhyolite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMATIC_RHYOLITE_PLACE = SOUNDS.createSoundEvent("block.magmatic_rhyolite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMATIC_RHYOLITE_STEP = SOUNDS.createSoundEvent("block.magmatic_rhyolite.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_BREAK = SOUNDS.createSoundEvent("block.cassiterite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_FALL = SOUNDS.createSoundEvent("block.cassiterite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_HIT = SOUNDS.createSoundEvent("block.cassiterite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_PLACE = SOUNDS.createSoundEvent("block.cassiterite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_STEP = SOUNDS.createSoundEvent("block.cassiterite.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_DEFLECT = SOUNDS.createSoundEvent("block.cassiterite.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_BREAK = SOUNDS.createSoundEvent("block.cylindrite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_FALL = SOUNDS.createSoundEvent("block.cylindrite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_HIT = SOUNDS.createSoundEvent("block.cylindrite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_PLACE = SOUNDS.createSoundEvent("block.cylindrite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_STEP = SOUNDS.createSoundEvent("block.cylindrite.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_DEFLECT = SOUNDS.createSoundEvent("block.cylindrite.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_BREAK = SOUNDS.createSoundEvent("block.flint_block.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_FALL = SOUNDS.createSoundEvent("block.flint_block.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_HIT = SOUNDS.createSoundEvent("block.flint_block.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_PLACE = SOUNDS.createSoundEvent("block.flint_block.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_STEP = SOUNDS.createSoundEvent("block.flint_block.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_STRIKE = SOUNDS.createSoundEvent("block.flint_block.strike");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_LAND = SOUNDS.createSoundEvent("block.flint_block.land");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLINT_BLOCK_RATTLE = SOUNDS.createSoundEvent("block.flint_block.rattle");

	public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_SILVER_ORE_BREAK = SOUNDS.createSoundEvent("block.soul_silver_ore.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_SILVER_ORE_FALL = SOUNDS.createSoundEvent("block.soul_silver_ore.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_SILVER_ORE_HIT = SOUNDS.createSoundEvent("block.soul_silver_ore.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_SILVER_ORE_PLACE = SOUNDS.createSoundEvent("block.soul_silver_ore.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_SILVER_ORE_STEP = SOUNDS.createSoundEvent("block.soul_silver_ore.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> SPINEL_BREAK = SOUNDS.createSoundEvent("block.spinel.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPINEL_FALL = SOUNDS.createSoundEvent("block.spinel.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPINEL_HIT = SOUNDS.createSoundEvent("block.spinel.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPINEL_PLACE = SOUNDS.createSoundEvent("block.spinel.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPINEL_STEP = SOUNDS.createSoundEvent("block.spinel.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> SUGILITE_BREAK = SOUNDS.createSoundEvent("block.sugilite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SUGILITE_FALL = SOUNDS.createSoundEvent("block.sugilite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SUGILITE_HIT = SOUNDS.createSoundEvent("block.sugilite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SUGILITE_PLACE = SOUNDS.createSoundEvent("block.sugilite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SUGILITE_STEP = SOUNDS.createSoundEvent("block.sugilite.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_BREAK = SOUNDS.createSoundEvent("block.zirconia.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_FALL = SOUNDS.createSoundEvent("block.zirconia.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_HIT = SOUNDS.createSoundEvent("block.zirconia.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_PLACE = SOUNDS.createSoundEvent("block.zirconia.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_STEP = SOUNDS.createSoundEvent("block.zirconia.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> ORNATE_GLASS_BREAK = SOUNDS.createSoundEvent("block.ornate_glass.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ORNATE_GLASS_FALL = SOUNDS.createSoundEvent("block.ornate_glass.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ORNATE_GLASS_HIT = SOUNDS.createSoundEvent("block.ornate_glass.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ORNATE_GLASS_PLACE = SOUNDS.createSoundEvent("block.ornate_glass.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ORNATE_GLASS_STEP = SOUNDS.createSoundEvent("block.ornate_glass.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> TURQUOISE_BREAK = SOUNDS.createSoundEvent("block.turquoise.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TURQUOISE_FALL = SOUNDS.createSoundEvent("block.turquoise.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TURQUOISE_HIT = SOUNDS.createSoundEvent("block.turquoise.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TURQUOISE_PLACE = SOUNDS.createSoundEvent("block.turquoise.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TURQUOISE_STEP = SOUNDS.createSoundEvent("block.turquoise.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_BREAK = SOUNDS.createSoundEvent("block.float_glass.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_FALL = SOUNDS.createSoundEvent("block.float_glass.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_HIT = SOUNDS.createSoundEvent("block.float_glass.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_PLACE = SOUNDS.createSoundEvent("block.float_glass.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_STEP = SOUNDS.createSoundEvent("block.float_glass.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOAT_GLASS_DEFLECT = SOUNDS.createSoundEvent("block.float_glass.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_BREAK = SOUNDS.createSoundEvent("block.necromium.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_FALL = SOUNDS.createSoundEvent("block.necromium.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_HIT = SOUNDS.createSoundEvent("block.necromium.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_PLACE = SOUNDS.createSoundEvent("block.necromium.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_STEP = SOUNDS.createSoundEvent("block.necromium.step");
	
	public static final DeferredHolder<SoundEvent, SoundEvent> CALCITE_BRICKS_BREAK = SOUNDS.createSoundEvent("block.calcite_bricks.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CALCITE_BRICKS_FALL = SOUNDS.createSoundEvent("block.calcite_bricks.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CALCITE_BRICKS_HIT = SOUNDS.createSoundEvent("block.calcite_bricks.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CALCITE_BRICKS_PLACE = SOUNDS.createSoundEvent("block.calcite_bricks.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CALCITE_BRICKS_STEP = SOUNDS.createSoundEvent("block.calcite_bricks.step");
	
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_CALCITE_BREAK = SOUNDS.createSoundEvent("block.polished_calcite.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_CALCITE_FALL = SOUNDS.createSoundEvent("block.polished_calcite.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_CALCITE_HIT = SOUNDS.createSoundEvent("block.polished_calcite.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_CALCITE_PLACE = SOUNDS.createSoundEvent("block.polished_calcite.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_CALCITE_STEP = SOUNDS.createSoundEvent("block.polished_calcite.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_DRIPSTONE_BREAK = SOUNDS.createSoundEvent("block.polished_dripstone.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_DRIPSTONE_FALL = SOUNDS.createSoundEvent("block.polished_dripstone.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_DRIPSTONE_HIT = SOUNDS.createSoundEvent("block.polished_dripstone.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_DRIPSTONE_PLACE = SOUNDS.createSoundEvent("block.polished_dripstone.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> POLISHED_DRIPSTONE_STEP = SOUNDS.createSoundEvent("block.polished_dripstone.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> FALSE_HOPE_BREAK = SOUNDS.createSoundEvent("block.false_hope.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FALSE_HOPE_FALL = SOUNDS.createSoundEvent("block.false_hope.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FALSE_HOPE_HIT = SOUNDS.createSoundEvent("block.false_hope.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FALSE_HOPE_PLACE = SOUNDS.createSoundEvent("block.false_hope.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FALSE_HOPE_STEP = SOUNDS.createSoundEvent("block.false_hope.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> CAVE_GROWTHS_BREAK = SOUNDS.createSoundEvent("block.cave_growths.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVE_GROWTHS_FALL = SOUNDS.createSoundEvent("block.cave_growths.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVE_GROWTHS_HIT = SOUNDS.createSoundEvent("block.cave_growths.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVE_GROWTHS_PLACE = SOUNDS.createSoundEvent("block.cave_growths.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVE_GROWTHS_STEP = SOUNDS.createSoundEvent("block.cave_growths.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> MOSCHATEL_BREAK = SOUNDS.createSoundEvent("block.moschatel.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> MOSCHATEL_FALL = SOUNDS.createSoundEvent("block.moschatel.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> MOSCHATEL_HIT = SOUNDS.createSoundEvent("block.moschatel.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> MOSCHATEL_PLACE = SOUNDS.createSoundEvent("block.moschatel.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> MOSCHATEL_STEP = SOUNDS.createSoundEvent("block.moschatel.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> TMT_BREAK = SOUNDS.createSoundEvent("block.tmt.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TMT_FALL = SOUNDS.createSoundEvent("block.tmt.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TMT_HIT = SOUNDS.createSoundEvent("block.tmt.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TMT_PLACE = SOUNDS.createSoundEvent("block.tmt.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TMT_STEP = SOUNDS.createSoundEvent("block.tmt.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_BREAK = SOUNDS.createSoundEvent("block.sanguine.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_FALL = SOUNDS.createSoundEvent("block.sanguine.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_HIT = SOUNDS.createSoundEvent("block.sanguine.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_PLACE = SOUNDS.createSoundEvent("block.sanguine.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_STEP = SOUNDS.createSoundEvent("block.sanguine.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> FORTIFIED_SANGUINE_BREAK = SOUNDS.createSoundEvent("block.fortified_sanguine.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FORTIFIED_SANGUINE_FALL = SOUNDS.createSoundEvent("block.fortified_sanguine.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FORTIFIED_SANGUINE_HIT = SOUNDS.createSoundEvent("block.fortified_sanguine.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FORTIFIED_SANGUINE_PLACE = SOUNDS.createSoundEvent("block.fortified_sanguine.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FORTIFIED_SANGUINE_STEP = SOUNDS.createSoundEvent("block.fortified_sanguine.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> ROTTEN_FLESH_BREAK = SOUNDS.createSoundEvent("block.rotten_flesh.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROTTEN_FLESH_FALL = SOUNDS.createSoundEvent("block.rotten_flesh.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROTTEN_FLESH_HIT = SOUNDS.createSoundEvent("block.rotten_flesh.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROTTEN_FLESH_PLACE = SOUNDS.createSoundEvent("block.rotten_flesh.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROTTEN_FLESH_STEP = SOUNDS.createSoundEvent("block.rotten_flesh.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_BREAK = SOUNDS.createSoundEvent("block.lava_lamp.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_FALL = SOUNDS.createSoundEvent("block.lava_lamp.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_HIT = SOUNDS.createSoundEvent("block.lava_lamp.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_PLACE = SOUNDS.createSoundEvent("block.lava_lamp.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_STEP = SOUNDS.createSoundEvent("block.lava_lamp.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAVA_LAMP_GLUG = SOUNDS.createSoundEvent("block.lava_lamp.glug");

	public static final DeferredHolder<SoundEvent, SoundEvent> FLOODLIGHT_BREAK = SOUNDS.createSoundEvent("block.floodlight.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOODLIGHT_FALL = SOUNDS.createSoundEvent("block.floodlight.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOODLIGHT_HIT = SOUNDS.createSoundEvent("block.floodlight.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOODLIGHT_PLACE = SOUNDS.createSoundEvent("block.floodlight.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> FLOODLIGHT_STEP = SOUNDS.createSoundEvent("block.floodlight.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHAIN_BREAK = SOUNDS.createSoundEvent("block.copper_chain.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHAIN_STEP = SOUNDS.createSoundEvent("block.copper_chain.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHAIN_PLACE = SOUNDS.createSoundEvent("block.copper_chain.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHAIN_HIT = SOUNDS.createSoundEvent("block.copper_chain.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHAIN_FALL = SOUNDS.createSoundEvent("block.copper_chain.fall");

	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_LANTERN_BREAK = SOUNDS.createSoundEvent("block.copper_lantern.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_LANTERN_STEP = SOUNDS.createSoundEvent("block.copper_lantern.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_LANTERN_PLACE = SOUNDS.createSoundEvent("block.copper_lantern.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_LANTERN_HIT = SOUNDS.createSoundEvent("block.copper_lantern.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_LANTERN_FALL = SOUNDS.createSoundEvent("block.copper_lantern.fall");

	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_BREAK = SOUNDS.createSoundEvent("block.silver.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_FALL = SOUNDS.createSoundEvent("block.silver.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_HIT = SOUNDS.createSoundEvent("block.silver.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_PLACE = SOUNDS.createSoundEvent("block.silver.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_STEP = SOUNDS.createSoundEvent("block.silver.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> BRAZIER_CRACKLE = SOUNDS.createSoundEvent("block.brazier.crackle");

	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_BREAK = SOUNDS.createSoundEvent("block.saddled_egg.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_FALL = SOUNDS.createSoundEvent("block.saddled_egg.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_HIT = SOUNDS.createSoundEvent("block.saddled_egg.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_PLACE = SOUNDS.createSoundEvent("block.saddled_egg.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_STEP = SOUNDS.createSoundEvent("block.saddled_egg.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_DEFLECT = SOUNDS.createSoundEvent("block.saddled_egg.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> SADDLED_EGG_HATCH = SOUNDS.createSoundEvent("block.saddled_egg.hatch");

	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_BREAK = SOUNDS.createSoundEvent("block.dimmer.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_FALL = SOUNDS.createSoundEvent("block.dimmer.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_HIT = SOUNDS.createSoundEvent("block.dimmer.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_PLACE = SOUNDS.createSoundEvent("block.dimmer.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_STEP = SOUNDS.createSoundEvent("block.dimmer.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_DEFLECT = SOUNDS.createSoundEvent("block.dimmer.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMMER_BUZZ = SOUNDS.createSoundEvent("block.dimmer.buzz");

	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_BREAK = SOUNDS.createSoundEvent("block.refractor.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_FALL = SOUNDS.createSoundEvent("block.refractor.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_HIT = SOUNDS.createSoundEvent("block.refractor.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_PLACE = SOUNDS.createSoundEvent("block.refractor.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_STEP = SOUNDS.createSoundEvent("block.refractor.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_DEFLECT = SOUNDS.createSoundEvent("block.refractor.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> REFRACTOR_REFRACT = SOUNDS.createSoundEvent("block.refractor.refract");

	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_BREAK = SOUNDS.createSoundEvent("block.bouncer.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_FALL = SOUNDS.createSoundEvent("block.bouncer.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_HIT = SOUNDS.createSoundEvent("block.bouncer.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_PLACE = SOUNDS.createSoundEvent("block.bouncer.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_STEP = SOUNDS.createSoundEvent("block.bouncer.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_DEFLECT = SOUNDS.createSoundEvent("block.bouncer.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> BOUNCER_BOOST = SOUNDS.createSoundEvent("block.bouncer.boost");

	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_BREAK = SOUNDS.createSoundEvent("block.tinplate.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_FALL = SOUNDS.createSoundEvent("block.tinplate.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_HIT = SOUNDS.createSoundEvent("block.tinplate.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_PLACE = SOUNDS.createSoundEvent("block.tinplate.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_STEP = SOUNDS.createSoundEvent("block.tinplate.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_DEFLECT = SOUNDS.createSoundEvent("block.tinplate.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_SECOND_DEFLECT = SOUNDS.createSoundEvent("block.tinplate.second_deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BREAK = SOUNDS.createSoundEvent("block.tin.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_FALL = SOUNDS.createSoundEvent("block.tin.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_HIT = SOUNDS.createSoundEvent("block.tin.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_PLACE = SOUNDS.createSoundEvent("block.tin.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_STEP = SOUNDS.createSoundEvent("block.tin.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_DEFLECT = SOUNDS.createSoundEvent("block.tin.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_BREAK = SOUNDS.createSoundEvent("block.tin_bulb.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_FALL = SOUNDS.createSoundEvent("block.tin_bulb.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_HIT = SOUNDS.createSoundEvent("block.tin_bulb.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_PLACE = SOUNDS.createSoundEvent("block.tin_bulb.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_STEP = SOUNDS.createSoundEvent("block.tin_bulb.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_DEFLECT = SOUNDS.createSoundEvent("block.tin_bulb.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_TURN_ON = SOUNDS.createSoundEvent("block.tin_bulb.turn_on");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BULB_TURN_OFF = SOUNDS.createSoundEvent("block.tin_bulb.turn_off");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_BREAK = SOUNDS.createSoundEvent("block.tin_chain.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_FALL = SOUNDS.createSoundEvent("block.tin_chain.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_HIT = SOUNDS.createSoundEvent("block.tin_chain.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_PLACE = SOUNDS.createSoundEvent("block.tin_chain.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_STEP = SOUNDS.createSoundEvent("block.tin_chain.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_CHAIN_DEFLECT = SOUNDS.createSoundEvent("block.tin_chain.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_BREAK = SOUNDS.createSoundEvent("block.tin_ore.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_FALL = SOUNDS.createSoundEvent("block.tin_ore.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_HIT = SOUNDS.createSoundEvent("block.tin_ore.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_PLACE = SOUNDS.createSoundEvent("block.tin_ore.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_STEP = SOUNDS.createSoundEvent("block.tin_ore.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_ORE_DEFLECT = SOUNDS.createSoundEvent("block.tin_ore.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_BREAK = SOUNDS.createSoundEvent("block.deepslate_tin_ore.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_FALL = SOUNDS.createSoundEvent("block.deepslate_tin_ore.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_HIT = SOUNDS.createSoundEvent("block.deepslate_tin_ore.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_PLACE = SOUNDS.createSoundEvent("block.deepslate_tin_ore.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_STEP = SOUNDS.createSoundEvent("block.deepslate_tin_ore.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPSLATE_TIN_ORE_DEFLECT = SOUNDS.createSoundEvent("block.deepslate_tin_ore.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_BREAK = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_FALL = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_HIT = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_PLACE = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_STEP = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> CYLINDRITE_TIN_ORE_DEFLECT = SOUNDS.createSoundEvent("block.cylindrite_tin_ore.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_BREAK = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_FALL = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_HIT = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_PLACE = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_STEP = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> CASSITERITE_TIN_ORE_DEFLECT = SOUNDS.createSoundEvent("block.cassiterite_tin_ore.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_BREAK = SOUNDS.createSoundEvent("block.storage_duct.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_FALL = SOUNDS.createSoundEvent("block.storage_duct.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_HIT = SOUNDS.createSoundEvent("block.storage_duct.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_PLACE = SOUNDS.createSoundEvent("block.storage_duct.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_STEP = SOUNDS.createSoundEvent("block.storage_duct.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_DEFLECT = SOUNDS.createSoundEvent("block.storage_duct.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_BREAK = SOUNDS.createSoundEvent("block.caviar.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_FALL = SOUNDS.createSoundEvent("block.caviar.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_HIT = SOUNDS.createSoundEvent("block.caviar.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_PLACE = SOUNDS.createSoundEvent("block.caviar.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_STEP = SOUNDS.createSoundEvent("block.caviar.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_HATCH_OPEN = SOUNDS.createSoundEvent("block.storage_duct_hatch.open");
	public static final DeferredHolder<SoundEvent, SoundEvent> STORAGE_DUCT_HATCH_CLOSE = SOUNDS.createSoundEvent("block.storage_duct_hatch.close");

	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_BREAK = SOUNDS.createSoundEvent("block.roller_door.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_FALL = SOUNDS.createSoundEvent("block.roller_door.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_HIT = SOUNDS.createSoundEvent("block.roller_door.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_PLACE = SOUNDS.createSoundEvent("block.roller_door.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_STEP = SOUNDS.createSoundEvent("block.roller_door.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_DEFLECT = SOUNDS.createSoundEvent("block.roller_door.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_START_ROLL = SOUNDS.createSoundEvent("block.roller_door.start_roll");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_ROLL = SOUNDS.createSoundEvent("block.roller_door.roll");
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLER_DOOR_STOP_ROLL = SOUNDS.createSoundEvent("block.roller_door.stop_roll");

	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_BREAK = SOUNDS.createSoundEvent("block.atoning_table.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_FALL = SOUNDS.createSoundEvent("block.atoning_table.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_HIT = SOUNDS.createSoundEvent("block.atoning_table.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_PLACE = SOUNDS.createSoundEvent("block.atoning_table.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_STEP = SOUNDS.createSoundEvent("block.atoning_table.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_USE = SOUNDS.createSoundEvent("block.atoning_table.use");
	public static final DeferredHolder<SoundEvent, SoundEvent> ATONING_TABLE_WHISPERS = SOUNDS.createSoundEvent("block.atoning_table.whispers");

	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_BREAK = SOUNDS.createSoundEvent("block.bejeweled_anvil.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_FALL = SOUNDS.createSoundEvent("block.bejeweled_anvil.fall");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_HIT = SOUNDS.createSoundEvent("block.bejeweled_anvil.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_PLACE = SOUNDS.createSoundEvent("block.bejeweled_anvil.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_STEP = SOUNDS.createSoundEvent("block.bejeweled_anvil.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_USE = SOUNDS.createSoundEvent("block.bejeweled_anvil.use");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_LAND = SOUNDS.createSoundEvent("block.bejeweled_anvil.land");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_ANVIL_SHATTER = SOUNDS.createSoundEvent("block.bejeweled_anvil.shatter");

	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_BUTTON_CLICK_OFF = SOUNDS.createSoundEvent("block.copper_button.click_off");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_BUTTON_CLICK_ON = SOUNDS.createSoundEvent("block.copper_button.click_on");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BUTTON_CLICK_OFF = SOUNDS.createSoundEvent("block.tin_button.click_off");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BUTTON_CLICK_ON = SOUNDS.createSoundEvent("block.tin_button.click_on");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_BUTTON_HOLD = SOUNDS.createSoundEvent("block.tin_button.hold");

	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_PRESSURE_PLATE_CLICK_OFF = SOUNDS.createSoundEvent("block.tin_pressure_plate.click_off");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_PRESSURE_PLATE_CLICK_ON = SOUNDS.createSoundEvent("block.tin_pressure_plate.click_on");
	public static final DeferredHolder<SoundEvent, SoundEvent> TIN_PRESSURE_PLATE_HOLD = SOUNDS.createSoundEvent("block.tin_pressure_plate.hold");

	public static final DeferredHolder<SoundEvent, SoundEvent> SCATTERER_SCATTER = SOUNDS.createSoundEvent("block.scatterer.scatter");
	public static final DeferredHolder<SoundEvent, SoundEvent> SCATTERER_SPLURT = SOUNDS.createSoundEvent("block.scatterer.splurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> SCATTERER_FAIL = SOUNDS.createSoundEvent("block.scatterer.fail");

	public static final DeferredHolder<SoundEvent, SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_OFF = SOUNDS.createSoundEvent("block.medium_weighted_pressure_plate.click_off");
	public static final DeferredHolder<SoundEvent, SoundEvent> MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_ON = SOUNDS.createSoundEvent("block.medium_weighted_pressure_plate.click_on");

	public static final DeferredHolder<SoundEvent, SoundEvent> WINCH_WIND = SOUNDS.createSoundEvent("block.winch.wind");
	public static final DeferredHolder<SoundEvent, SoundEvent> WINCH_LOCK = SOUNDS.createSoundEvent("block.winch.lock");
	public static final DeferredHolder<SoundEvent, SoundEvent> HOOP_SCORE = SOUNDS.createSoundEvent("block.hoop.score");
	public static final DeferredHolder<SoundEvent, SoundEvent> HOOP_SHRINK = SOUNDS.createSoundEvent("block.hoop.shrink");
	public static final DeferredHolder<SoundEvent, SoundEvent> HOOP_EXPAND = SOUNDS.createSoundEvent("block.hoop.expand");
	public static final DeferredHolder<SoundEvent, SoundEvent> RESISTOR_BUZZ = SOUNDS.createSoundEvent("block.resistor.buzz");
	public static final DeferredHolder<SoundEvent, SoundEvent> RESISTOR_TOGGLE = SOUNDS.createSoundEvent("block.resistor.toggle");

	public static final DeferredHolder<SoundEvent, SoundEvent> HALT_RAIL_HALT = SOUNDS.createSoundEvent("block.halt_rail.halt");
	public static final DeferredHolder<SoundEvent, SoundEvent> HALT_RAIL_EXTEND = SOUNDS.createSoundEvent("block.halt_rail.extend");
	public static final DeferredHolder<SoundEvent, SoundEvent> HALT_RAIL_CONTRACT = SOUNDS.createSoundEvent("block.halt_rail.contract");
	public static final DeferredHolder<SoundEvent, SoundEvent> SLAUGHTER_RAIL_EXTEND = SOUNDS.createSoundEvent("block.slaughter_rail.extend");
	public static final DeferredHolder<SoundEvent, SoundEvent> SLAUGHTER_RAIL_CONTRACT = SOUNDS.createSoundEvent("block.slaughter_rail.contract");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPIKED_RAIL_EXTEND = SOUNDS.createSoundEvent("block.spiked_rail.extend");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPIKED_RAIL_CONTRACT = SOUNDS.createSoundEvent("block.spiked_rail.contract");

	public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_OPEN = SOUNDS.createSoundEvent("block.toolbox.open");
	public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_CLOSE = SOUNDS.createSoundEvent("block.toolbox.close");

	public static final DeferredHolder<SoundEvent, SoundEvent> DISMANTLING_TABLE_USE = SOUNDS.createSoundEvent("block.dismantling_table.use");

	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_IMITATE_MIME = SOUNDS.createSoundEvent("block.note_block.imitate.mime");
	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_IMITATE_DEEPER = SOUNDS.createSoundEvent("block.note_block.imitate.deeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_IMITATE_EVENDEEPER = SOUNDS.createSoundEvent("block.note_block.imitate.evendeeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_IMITATE_PEEPER = SOUNDS.createSoundEvent("block.note_block.imitate.peeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_IMITATE_WARDEN = SOUNDS.createSoundEvent("block.note_block.imitate.warden");

	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_EAT = SOUNDS.createSoundEvent("item.caviar.eat");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVIAR_BURP = SOUNDS.createSoundEvent("item.caviar.burp");

	public static final DeferredHolder<SoundEvent, SoundEvent> TINPLATE_WAX = SOUNDS.createSoundEvent("item.tinplate.wax");

	public static final DeferredHolder<SoundEvent, SoundEvent> TUNING_FORK_VIBRATE = SOUNDS.createSoundEvent("item.tuning_fork.vibrate");

	public static final DeferredHolder<SoundEvent, SoundEvent> TETHER_POTION_EQUIP = SOUNDS.createSoundEvent("item.tether_potion.equip");
	public static final DeferredHolder<SoundEvent, SoundEvent> TETHER_POTION_BREAK = SOUNDS.createSoundEvent("item.tether_potion.break");

	public static final DeferredHolder<SoundEvent, SoundEvent> MONOCLE_USE = SOUNDS.createSoundEvent("item.monocle.use");
	public static final DeferredHolder<SoundEvent, SoundEvent> MONOCLE_STOP_USING = SOUNDS.createSoundEvent("item.monocle.stop_using");
	public static final DeferredHolder<SoundEvent, SoundEvent> MONOCLE_EQUIP = SOUNDS.createSoundEvent("item.monocle.equip");

	public static final DeferredHolder<SoundEvent, SoundEvent> AEGIS_DEFLECT = SOUNDS.createSoundEvent("item.aegis.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> AEGIS_STUN = SOUNDS.createSoundEvent("item.aegis.stun");

	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_COPPER = SOUNDS.createSoundEvent("item.armor.equip_copper");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_SILVER = SOUNDS.createSoundEvent("item.armor.equip_silver");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_NECROMIUM = SOUNDS.createSoundEvent("item.armor.equip_necromium");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_SANGUINE = SOUNDS.createSoundEvent("item.armor.equip_sanguine");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_COWL = SOUNDS.createSoundEvent("item.armor.equip_cowl");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_TOOLBELT = SOUNDS.createSoundEvent("item.armor.equip_toolbelt");

	public static final DeferredHolder<SoundEvent, SoundEvent> NECROMIUM_INFLICT = SOUNDS.createSoundEvent("item.armor.necromium_inflict");
	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_RESIST = SOUNDS.createSoundEvent("item.armor.silver_resist");
	public static final DeferredHolder<SoundEvent, SoundEvent> SANGUINE_HEAL = SOUNDS.createSoundEvent("item.armor.sanguine_heal");

	public static final DeferredHolder<SoundEvent, SoundEvent> UNICORN_HORN_EQUIP = SOUNDS.createSoundEvent("item.unicorn_horn.equip");
	public static final DeferredHolder<SoundEvent, SoundEvent> UNICORN_HORN_UNEQUIP = SOUNDS.createSoundEvent("item.unicorn_horn.unequip");

	public static final DeferredHolder<SoundEvent, SoundEvent> SILVER_STRIKE = SOUNDS.createSoundEvent("item.silver.strike");

	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_APPLE_EAT = SOUNDS.createSoundEvent("item.bejeweled_apple.eat");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_APPLE_BURP = SOUNDS.createSoundEvent("item.bejeweled_apple.burp");

	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_PEARL_TELEPORT = SOUNDS.createSoundEvent("item.bejeweled_pearl.teleport");
	public static final DeferredHolder<SoundEvent, SoundEvent> BEJEWELED_PEARL_CRUMBLE = SOUNDS.createSoundEvent("item.bejeweled_pearl.crumble");

	public static final DeferredHolder<SoundEvent, SoundEvent> ZIRCONIA_ANVIL_USE = SOUNDS.createSoundEvent("item.zirconia.anvil_use");

	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPER_DEATH = SOUNDS.createSoundEvent("entity.deeper.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPER_HURT = SOUNDS.createSoundEvent("entity.deeper.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPER_PRIMED = SOUNDS.createSoundEvent("entity.deeper.primed");
	public static final DeferredHolder<SoundEvent, SoundEvent> DEEPER_EXPLODE = SOUNDS.createSoundEvent("entity.deeper.explode");

	public static final DeferredHolder<SoundEvent, SoundEvent> EVENDEEPER_DEATH = SOUNDS.createSoundEvent("entity.evendeeper.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> EVENDEEPER_HURT = SOUNDS.createSoundEvent("entity.evendeeper.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> EVENDEEPER_PRIMED = SOUNDS.createSoundEvent("entity.evendeeper.primed");
	public static final DeferredHolder<SoundEvent, SoundEvent> EVENDEEPER_EXPLODE = SOUNDS.createSoundEvent("entity.evendeeper.explode");

	public static final DeferredHolder<SoundEvent, SoundEvent> PEEPER_DEATH = SOUNDS.createSoundEvent("entity.peeper.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> PEEPER_HURT = SOUNDS.createSoundEvent("entity.peeper.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> PEEPER_PULSE = SOUNDS.createSoundEvent("entity.peeper.pulse");
	public static final DeferredHolder<SoundEvent, SoundEvent> PEEPER_PRIMED = SOUNDS.createSoundEvent("entity.peeper.primed");
	public static final DeferredHolder<SoundEvent, SoundEvent> PEEPER_EXPLODE = SOUNDS.createSoundEvent("entity.peeper.explode");

	public static final DeferredHolder<SoundEvent, SoundEvent> MIME_DEATH = SOUNDS.createSoundEvent("entity.mime.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> MIME_HURT = SOUNDS.createSoundEvent("entity.mime.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> MIME_MIME = SOUNDS.createSoundEvent("entity.mime.mime");
	public static final DeferredHolder<SoundEvent, SoundEvent> MIME_IMPERSONATE = SOUNDS.createSoundEvent("entity.mime.impersonate");
	public static final DeferredHolder<SoundEvent, SoundEvent> MIME_CONVERT = SOUNDS.createSoundEvent("entity.mime.convert");

	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DEATH = SOUNDS.createSoundEvent("entity.copper_golem.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HURT = SOUNDS.createSoundEvent("entity.copper_golem.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_REPAIR = SOUNDS.createSoundEvent("entity.copper_golem.repair");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DAMAGE = SOUNDS.createSoundEvent("entity.copper_golem.damage");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_GEAR = SOUNDS.createSoundEvent("entity.copper_golem.gear");
	public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_STEP = SOUNDS.createSoundEvent("entity.copper_golem.step");

	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_DEATH = SOUNDS.createSoundEvent("entity.glare.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_ANGRY = SOUNDS.createSoundEvent("entity.glare.angry");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_EAT = SOUNDS.createSoundEvent("entity.glare.eat");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_HURT = SOUNDS.createSoundEvent("entity.glare.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_AMBIENT = SOUNDS.createSoundEvent("entity.glare.ambient");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_TAME = SOUNDS.createSoundEvent("entity.glare.tame");
	public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_UNTAME = SOUNDS.createSoundEvent("entity.glare.untame");

	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_AMBIENT = SOUNDS.createSoundEvent("entity.grazer.ambient");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_HURT = SOUNDS.createSoundEvent("entity.grazer.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_DEATH = SOUNDS.createSoundEvent("entity.grazer.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_DEFLECT = SOUNDS.createSoundEvent("entity.grazer.deflect");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_RICOCHET = SOUNDS.createSoundEvent("entity.grazer.ricochet");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_CHARGE = SOUNDS.createSoundEvent("entity.grazer.charge");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_STRUGGLE = SOUNDS.createSoundEvent("entity.grazer.struggle");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_STEP = SOUNDS.createSoundEvent("entity.grazer.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_MOUNT = SOUNDS.createSoundEvent("entity.grazer.mount");
	public static final DeferredHolder<SoundEvent, SoundEvent> GRAZER_DISMOUNT = SOUNDS.createSoundEvent("entity.grazer.dismount");

	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_DEATH = SOUNDS.createSoundEvent("entity.rat.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_HURT = SOUNDS.createSoundEvent("entity.rat.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_AMBIENT = SOUNDS.createSoundEvent("entity.rat.ambient");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_STEP = SOUNDS.createSoundEvent("entity.rat.step");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_ANGRY = SOUNDS.createSoundEvent("entity.rat.angry");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_HAPPY = SOUNDS.createSoundEvent("entity.rat.happy");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_EAT = SOUNDS.createSoundEvent("entity.rat.eat");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_ATTACK = SOUNDS.createSoundEvent("entity.rat.attack");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_LATCH = SOUNDS.createSoundEvent("entity.rat.latch");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_SPIT = SOUNDS.createSoundEvent("entity.rat.spit");
	public static final DeferredHolder<SoundEvent, SoundEvent> RAT_WOUNDED = SOUNDS.createSoundEvent("entity.rat.wounded");

	public static final DeferredHolder<SoundEvent, SoundEvent> CAVEFISH_DEATH = SOUNDS.createSoundEvent("entity.cavefish.death");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVEFISH_HURT = SOUNDS.createSoundEvent("entity.cavefish.hurt");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVEFISH_AMBIENT = SOUNDS.createSoundEvent("entity.cavefish.ambient");
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVEFISH_FLOP = SOUNDS.createSoundEvent("entity.cavefish.flop");

	public static final DeferredHolder<SoundEvent, SoundEvent> BLUNT_ARROW_HIT = SOUNDS.createSoundEvent("entity.blunt_arrow.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_ARROW_HIT = SOUNDS.createSoundEvent("entity.large_arrow.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> RICOCHET_ARROW_HIT = SOUNDS.createSoundEvent("entity.ricochet_arrow.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> RICOCHET_ARROW_DEFLECT = SOUNDS.createSoundEvent("entity.ricochet_arrow.deflect");

	public static final DeferredHolder<SoundEvent, SoundEvent> KUNAI_HIT = SOUNDS.createSoundEvent("entity.kunai.hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> KUNAI_THROW = SOUNDS.createSoundEvent("entity.kunai.throw");

	public static final DeferredHolder<SoundEvent, SoundEvent> REWIND = SOUNDS.createSoundEvent("effect.rewind.rewind");
	public static final DeferredHolder<SoundEvent, SoundEvent> DRAIN = SOUNDS.createSoundEvent("effect.vampirism.drain");

	public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_DEEPER = SOUNDS.createSoundEvent("entity.parrot.imitate.deeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_EVENDEEPER = SOUNDS.createSoundEvent("entity.parrot.imitate.evendeeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_PEEPER = SOUNDS.createSoundEvent("entity.parrot.imitate.peeper");
	public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_MIME = SOUNDS.createSoundEvent("entity.parrot.imitate.mime");
	public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_GRAZER = SOUNDS.createSoundEvent("entity.parrot.imitate.grazer");

	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_STATIC = SOUNDS.createSoundEvent("block.note_block.static");
	public static final DeferredHolder<SoundEvent, SoundEvent> NOTE_BLOCK_ALARM = SOUNDS.createSoundEvent("block.note_block.alarm");

	public static final ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> GOAT_HORN_SOUND_VARIANTS = registerGoatHornSoundVariants();

	public static final ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> LOST_GOAT_HORN_SOUND_VARIANTS = registerLostGoatHornSoundVariants();

	public static final ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> COPPER_HORN_HARMONY_SOUND_VARIANTS = registerCopperHornSoundVariants("harmony");
	public static final ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> COPPER_HORN_MELODY_SOUND_VARIANTS = registerCopperHornSoundVariants("melody");
	public static final ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> COPPER_HORN_BASS_SOUND_VARIANTS = registerCopperHornSoundVariants("bass");

	public static final DeferredHolder<SoundEvent, SoundEvent> BONE_FLUTE_SIT = SOUNDS.createSoundEvent("item.bone_flute.sit");
	public static final DeferredHolder<SoundEvent, SoundEvent> BONE_FLUTE_RECALL = SOUNDS.createSoundEvent("item.bone_flute.recall");
	public static final DeferredHolder<SoundEvent, SoundEvent> BONE_FLUTE_MOVE = SOUNDS.createSoundEvent("item.bone_flute.move");
	public static final DeferredHolder<SoundEvent, SoundEvent> BONE_FLUTE_ATTACK = SOUNDS.createSoundEvent("item.bone_flute.attack");

	public static final DeferredHolder<SoundEvent, SoundEvent> PACKING_CONTAINER_DROP_CONTENTS = SOUNDS.createSoundEvent("item.packing_container.drop_contents");
	public static final DeferredHolder<SoundEvent, SoundEvent> PACKING_CONTAINER_INSERT = SOUNDS.createSoundEvent("item.packing_container.insert");
	public static final DeferredHolder<SoundEvent, SoundEvent> PACKING_CONTAINER_INSERT_FAIL = SOUNDS.createSoundEvent("item.packing_container.insert_fail");
	public static final DeferredHolder<SoundEvent, SoundEvent> PACKING_CONTAINER_REMOVE_ONE = SOUNDS.createSoundEvent("item.packing_container.remove_one");

	private static ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> registerGoatHornSoundVariants() {
		return IntStream.range(8, 10).mapToObj((suffix) -> SOUNDS.createSoundEvent("item.goat_horn.sound." + suffix)).collect(ImmutableList.toImmutableList());
	}

	private static ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> registerLostGoatHornSoundVariants() {
		return IntStream.range(0, 2).mapToObj((suffix) -> SOUNDS.createSoundEvent("item.lost_goat_horn.sound." + suffix)).collect(ImmutableList.toImmutableList());
	}

	private static ImmutableList<DeferredHolder<SoundEvent, SoundEvent>> registerCopperHornSoundVariants(String variant) {
		return IntStream.range(0, 10).mapToObj((suffix) -> SOUNDS.createSoundEvent("item.copper_horn.sound." + variant + "." + suffix)).collect(ImmutableList.toImmutableList());
	}

	public static class CCSoundTypes {
		public static final DeferredSoundType ROCKY_DIRT = new DeferredSoundType(1.0F, 1.0F, ROCKY_DIRT_BREAK, ROCKY_DIRT_STEP, ROCKY_DIRT_PLACE, ROCKY_DIRT_HIT, ROCKY_DIRT_FALL);
		public static final DeferredSoundType FRAGILE_STONE = new DeferredSoundType(1.0F, 1.0F, FRAGILE_STONE_BREAK, FRAGILE_STONE_STEP, FRAGILE_STONE_PLACE, FRAGILE_STONE_HIT, FRAGILE_STONE_FALL);
		public static final DeferredSoundType FRAGILE_DEEPSLATE = new DeferredSoundType(1.0F, 1.0F, FRAGILE_DEEPSLATE_BREAK, FRAGILE_DEEPSLATE_STEP, FRAGILE_DEEPSLATE_PLACE, FRAGILE_DEEPSLATE_HIT, FRAGILE_DEEPSLATE_FALL);
		public static final DeferredSoundType CHARCOAL = new DeferredSoundType(1.0F, 1.0F, CHARCOAL_BREAK, CHARCOAL_STEP, CHARCOAL_PLACE, CHARCOAL_HIT, CHARCOAL_FALL);
		public static final DeferredSoundType ECHO_BLOCK = new DeferredSoundType(1.0F, 1.0F, ECHO_BLOCK_BREAK, ECHO_BLOCK_STEP, ECHO_BLOCK_PLACE, ECHO_BLOCK_HIT, ECHO_BLOCK_FALL);
		public static final DeferredSoundType GUNPOWDER = new DeferredSoundType(1.0F, 1.0F, GUNPOWDER_BREAK, GUNPOWDER_STEP, GUNPOWDER_PLACE, GUNPOWDER_HIT, GUNPOWDER_FALL);
		public static final DeferredSoundType SPARKLER = new DeferredSoundType(1.0F, 1.0F, SPARKLER_BREAK, SPARKLER_STEP, SPARKLER_PLACE, SPARKLER_HIT, SPARKLER_FALL);
		public static final DeferredSoundType SHALE = new DeferredSoundType(1.0F, 1.0F, SHALE_BREAK, SHALE_STEP, SHALE_PLACE, SHALE_HIT, SHALE_FALL);
		public static final DeferredSoundType POLISHED_SHALE = new DeferredSoundType(1.0F, 1.0F, POLISHED_SHALE_BREAK, POLISHED_SHALE_STEP, POLISHED_SHALE_PLACE, POLISHED_SHALE_HIT, POLISHED_SHALE_FALL);
		public static final DeferredSoundType SHALE_BRICKS = new DeferredSoundType(1.0F, 1.0F, SHALE_BRICKS_BREAK, SHALE_BRICKS_STEP, SHALE_BRICKS_PLACE, SHALE_BRICKS_HIT, SHALE_BRICKS_FALL);
		public static final DeferredSoundType RHYOLITE = new DeferredSoundType(1.0F, 1.0F, RHYOLITE_BREAK, RHYOLITE_STEP, RHYOLITE_PLACE, RHYOLITE_HIT, RHYOLITE_FALL);
		public static final DeferredSoundType MAGMATIC_RHYOLITE = new DeferredSoundType(1.0F, 1.0F, MAGMATIC_RHYOLITE_BREAK, MAGMATIC_RHYOLITE_STEP, MAGMATIC_RHYOLITE_PLACE, MAGMATIC_RHYOLITE_HIT, MAGMATIC_RHYOLITE_FALL);
		public static final DeferredSoundType CASSITERITE = new TinSoundType(1.0F, 1.0F, CASSITERITE_BREAK, CASSITERITE_STEP, CASSITERITE_PLACE, CASSITERITE_HIT, CASSITERITE_FALL, CASSITERITE_DEFLECT);
		public static final DeferredSoundType CYLINDRITE = new TinSoundType(1.0F, 1.0F, CYLINDRITE_BREAK, CYLINDRITE_STEP, CYLINDRITE_PLACE, CYLINDRITE_HIT, CYLINDRITE_FALL, CYLINDRITE_DEFLECT);
		public static final DeferredSoundType FLINT_BLOCK = new DeferredSoundType(1.0F, 1.0F, FLINT_BLOCK_BREAK, FLINT_BLOCK_STEP, FLINT_BLOCK_PLACE, FLINT_BLOCK_HIT, FLINT_BLOCK_FALL);
		public static final DeferredSoundType SOUL_SILVER_ORE = new DeferredSoundType(1.0F, 1.0F, SOUL_SILVER_ORE_BREAK, SOUL_SILVER_ORE_STEP, SOUL_SILVER_ORE_PLACE, SOUL_SILVER_ORE_HIT, SOUL_SILVER_ORE_FALL);
		public static final DeferredSoundType SPINEL = new DeferredSoundType(1.0F, 1.0F, SPINEL_BREAK, SPINEL_STEP, SPINEL_PLACE, SPINEL_HIT, SPINEL_FALL);
		public static final DeferredSoundType SUGILITE = new DeferredSoundType(1.0F, 1.0F, SUGILITE_BREAK, SUGILITE_STEP, SUGILITE_PLACE, SUGILITE_HIT, SUGILITE_FALL);
		public static final DeferredSoundType ZIRCONIA = new DeferredSoundType(1.0F, 1.0F, ZIRCONIA_BREAK, ZIRCONIA_STEP, ZIRCONIA_PLACE, ZIRCONIA_HIT, ZIRCONIA_FALL);
		public static final DeferredSoundType TURQUOISE = new DeferredSoundType(1.0F, 1.0F, TURQUOISE_BREAK, TURQUOISE_STEP, TURQUOISE_PLACE, TURQUOISE_HIT, TURQUOISE_FALL);
		public static final DeferredSoundType CAVIAR = new DeferredSoundType(1.0F, 1.0F, CAVIAR_BREAK, CAVIAR_STEP, CAVIAR_PLACE, CAVIAR_HIT, CAVIAR_FALL);
		public static final DeferredSoundType ORNATE_GLASS = new DeferredSoundType(1.0F, 1.0F, ORNATE_GLASS_BREAK, ORNATE_GLASS_STEP, ORNATE_GLASS_PLACE, ORNATE_GLASS_HIT, ORNATE_GLASS_FALL);
		public static final DeferredSoundType FLOAT_GLASS = new TinSoundType(1.0F, 1.0F, FLOAT_GLASS_BREAK, FLOAT_GLASS_STEP, FLOAT_GLASS_PLACE, FLOAT_GLASS_HIT, FLOAT_GLASS_FALL, FLOAT_GLASS_DEFLECT);
		public static final DeferredSoundType NECROMIUM = new DeferredSoundType(1.0F, 1.0F, NECROMIUM_BREAK, NECROMIUM_STEP, NECROMIUM_PLACE, NECROMIUM_HIT, NECROMIUM_FALL);
		public static final DeferredSoundType CALCITE_BRICKS = new DeferredSoundType(1.0F, 1.0F, CALCITE_BRICKS_BREAK, CALCITE_BRICKS_STEP, CALCITE_BRICKS_PLACE, CALCITE_BRICKS_HIT, CALCITE_BRICKS_FALL);
		public static final DeferredSoundType POLISHED_CALCITE = new DeferredSoundType(1.0F, 1.0F, POLISHED_CALCITE_BREAK, POLISHED_CALCITE_STEP, POLISHED_CALCITE_PLACE, POLISHED_CALCITE_HIT, POLISHED_CALCITE_FALL);
		public static final DeferredSoundType POLISHED_DRIPSTONE = new DeferredSoundType(1.0F, 1.0F, POLISHED_DRIPSTONE_BREAK, POLISHED_DRIPSTONE_STEP, POLISHED_DRIPSTONE_PLACE, POLISHED_DRIPSTONE_HIT, POLISHED_DRIPSTONE_FALL);
		public static final DeferredSoundType FALSE_HOPE = new DeferredSoundType(1.0F, 1.0F, FALSE_HOPE_BREAK, FALSE_HOPE_STEP, FALSE_HOPE_PLACE, FALSE_HOPE_HIT, FALSE_HOPE_FALL);
		public static final DeferredSoundType CAVE_GROWTHS = new DeferredSoundType(1.0F, 1.0F, CAVE_GROWTHS_BREAK, CAVE_GROWTHS_STEP, CAVE_GROWTHS_PLACE, CAVE_GROWTHS_HIT, CAVE_GROWTHS_FALL);
		public static final DeferredSoundType MOSCHATEL = new DeferredSoundType(1.0F, 1.0F, MOSCHATEL_BREAK, MOSCHATEL_STEP, MOSCHATEL_PLACE, MOSCHATEL_HIT, MOSCHATEL_FALL);
		public static final DeferredSoundType TMT = new DeferredSoundType(1.0F, 1.0F, TMT_BREAK, TMT_STEP, TMT_PLACE, TMT_HIT, TMT_FALL);
		public static final DeferredSoundType SANGUINE = new DeferredSoundType(1.0F, 1.0F, SANGUINE_BREAK, SANGUINE_STEP, SANGUINE_PLACE, SANGUINE_HIT, SANGUINE_FALL);
		public static final DeferredSoundType FORTIFIED_SANGUINE = new DeferredSoundType(1.0F, 1.0F, FORTIFIED_SANGUINE_BREAK, FORTIFIED_SANGUINE_STEP, FORTIFIED_SANGUINE_PLACE, FORTIFIED_SANGUINE_HIT, FORTIFIED_SANGUINE_FALL);
		public static final DeferredSoundType ROTTEN_FLESH = new DeferredSoundType(1.0F, 1.0F, ROTTEN_FLESH_BREAK, ROTTEN_FLESH_STEP, ROTTEN_FLESH_PLACE, ROTTEN_FLESH_HIT, ROTTEN_FLESH_FALL);
		public static final DeferredSoundType LAVA_LAMP = new DeferredSoundType(1.0F, 1.0F, LAVA_LAMP_BREAK, LAVA_LAMP_STEP, LAVA_LAMP_PLACE, LAVA_LAMP_HIT, LAVA_LAMP_FALL);
		public static final DeferredSoundType FLOODLIGHT = new DeferredSoundType(1.0F, 1.0F, FLOODLIGHT_BREAK, FLOODLIGHT_STEP, FLOODLIGHT_PLACE, FLOODLIGHT_HIT, FLOODLIGHT_FALL);
		public static final DeferredSoundType COPPER_LANTERN = new DeferredSoundType(1.0F, 1.0F, COPPER_LANTERN_BREAK, COPPER_LANTERN_STEP, COPPER_LANTERN_PLACE, COPPER_LANTERN_HIT, COPPER_LANTERN_FALL);
		public static final DeferredSoundType COPPER_CHAIN = new DeferredSoundType(1.0F, 1.0F, COPPER_CHAIN_BREAK, COPPER_CHAIN_STEP, COPPER_CHAIN_PLACE, COPPER_CHAIN_HIT, COPPER_CHAIN_FALL);
		public static final DeferredSoundType SILVER = new DeferredSoundType(1.0F, 1.0F, SILVER_BREAK, SILVER_STEP, SILVER_PLACE, SILVER_HIT, SILVER_FALL);
		public static final DeferredSoundType TIN = new TinSoundType(1.0F, 1.0F, TIN_BREAK, TIN_STEP, TIN_PLACE, TIN_HIT, TIN_FALL, TIN_DEFLECT);
		public static final DeferredSoundType TINPLATE = new TinSoundType(1.0F, 1.0F, TINPLATE_BREAK, TINPLATE_STEP, TINPLATE_PLACE, TINPLATE_HIT, TINPLATE_FALL, TINPLATE_DEFLECT);
		public static final DeferredSoundType DIMMER = new TinSoundType(1.0F, 1.0F, DIMMER_BREAK, DIMMER_STEP, DIMMER_PLACE, DIMMER_HIT, DIMMER_FALL, DIMMER_DEFLECT);
		public static final DeferredSoundType REFRACTOR = new TinSoundType(1.0F, 1.0F, REFRACTOR_BREAK, REFRACTOR_STEP, REFRACTOR_PLACE, REFRACTOR_HIT, REFRACTOR_FALL, REFRACTOR_DEFLECT);
		public static final DeferredSoundType BOUNCER = new TinSoundType(1.0F, 1.0F, BOUNCER_BREAK, BOUNCER_STEP, BOUNCER_PLACE, BOUNCER_HIT, BOUNCER_FALL, BOUNCER_DEFLECT);
		public static final DeferredSoundType SADDLED_EGG = new TinSoundType(1.0F, 1.0F, SADDLED_EGG_BREAK, SADDLED_EGG_STEP, SADDLED_EGG_PLACE, SADDLED_EGG_HIT, SADDLED_EGG_FALL, SADDLED_EGG_DEFLECT);
		public static final DeferredSoundType TIN_BULB = new TinSoundType(1.0F, 1.0F, TIN_BULB_BREAK, TIN_BULB_STEP, TIN_BULB_PLACE, TIN_BULB_HIT, TIN_BULB_FALL, TIN_BULB_DEFLECT);
		public static final DeferredSoundType TIN_CHAIN = new TinSoundType(1.0F, 1.0F, TIN_CHAIN_BREAK, TIN_CHAIN_STEP, TIN_CHAIN_PLACE, TIN_CHAIN_HIT, TIN_CHAIN_FALL, TIN_CHAIN_DEFLECT);
		public static final DeferredSoundType TIN_ORE = new TinSoundType(1.0F, 1.0F, TIN_ORE_BREAK, TIN_ORE_STEP, TIN_ORE_PLACE, TIN_ORE_HIT, TIN_ORE_FALL, TIN_ORE_DEFLECT);
		public static final DeferredSoundType DEEPSLATE_TIN_ORE = new TinSoundType(1.0F, 1.0F, DEEPSLATE_TIN_ORE_BREAK, DEEPSLATE_TIN_ORE_STEP, DEEPSLATE_TIN_ORE_PLACE, DEEPSLATE_TIN_ORE_HIT, DEEPSLATE_TIN_ORE_FALL, DEEPSLATE_TIN_ORE_DEFLECT);
		public static final DeferredSoundType CYLINDRITE_TIN_ORE = new TinSoundType(1.0F, 1.0F, CYLINDRITE_TIN_ORE_BREAK, CYLINDRITE_TIN_ORE_STEP, CYLINDRITE_TIN_ORE_PLACE, CYLINDRITE_TIN_ORE_HIT, CYLINDRITE_TIN_ORE_FALL, CYLINDRITE_TIN_ORE_DEFLECT);
		public static final DeferredSoundType CASSITERITE_TIN_ORE = new TinSoundType(1.0F, 1.0F, CASSITERITE_TIN_ORE_BREAK, CASSITERITE_TIN_ORE_STEP, CASSITERITE_TIN_ORE_PLACE, CASSITERITE_TIN_ORE_HIT, CASSITERITE_TIN_ORE_FALL, CASSITERITE_TIN_ORE_DEFLECT);
		public static final DeferredSoundType STORAGE_DUCT = new TinSoundType(1.0F, 1.0F, STORAGE_DUCT_BREAK, STORAGE_DUCT_STEP, STORAGE_DUCT_PLACE, STORAGE_DUCT_HIT, STORAGE_DUCT_FALL, STORAGE_DUCT_DEFLECT);
		public static final DeferredSoundType ROLLER_DOOR = new TinSoundType(1.0F, 1.0F, ROLLER_DOOR_BREAK, ROLLER_DOOR_STEP, ROLLER_DOOR_PLACE, ROLLER_DOOR_HIT, ROLLER_DOOR_FALL, ROLLER_DOOR_DEFLECT);
		public static final DeferredSoundType ATONING_TABLE = new DeferredSoundType(1.0F, 1.0F, ATONING_TABLE_BREAK, ATONING_TABLE_STEP, ATONING_TABLE_PLACE, ATONING_TABLE_HIT, ATONING_TABLE_FALL);
		public static final DeferredSoundType BEJEWELED_ANVIL = new DeferredSoundType(1.0F, 1.0F, BEJEWELED_ANVIL_BREAK, BEJEWELED_ANVIL_STEP, BEJEWELED_ANVIL_PLACE, BEJEWELED_ANVIL_HIT, BEJEWELED_ANVIL_FALL);
	}

	public static void registerNoteBlocks() {
		registerHeadInstrument(CCBlocks.MIME_HEAD, NOTE_BLOCK_IMITATE_MIME);
		registerHeadInstrument(CCBlocks.DEEPER_HEAD, NOTE_BLOCK_IMITATE_DEEPER);
		registerHeadInstrument(CCBlocks.EVENDEEPER_HEAD, NOTE_BLOCK_IMITATE_EVENDEEPER);
		registerHeadInstrument(CCBlocks.PEEPER_HEAD, NOTE_BLOCK_IMITATE_PEEPER);
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.state().is(CCBlockTags.WARDEN_NOTE_BLOCKS), NOTE_BLOCK_IMITATE_WARDEN.get(), false));
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.state().is(CCBlockTags.STATIC_NOTE_BLOCKS), NOTE_BLOCK_STATIC.get(), false));
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.state().is(CCBlockTags.ALARM_NOTE_BLOCKS), NOTE_BLOCK_ALARM.get(), false));
	}

	public static void registerHeadInstrument(DeferredBlock<Block> block, DeferredHolder<SoundEvent, SoundEvent> soundEvent) {
		DataUtil.registerNoteBlockInstrument(new CustomNoteBlockInstrument(CavernsAndChasms.MOD_ID, source -> source.state().is(block.get()), soundEvent.get(), true));
	}
}