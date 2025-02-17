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

	public static final RegistryObject<SoundEvent> SILVER_BREAK = HELPER.createSoundEvent("block.silver.break");
	public static final RegistryObject<SoundEvent> SILVER_FALL = HELPER.createSoundEvent("block.silver.fall");
	public static final RegistryObject<SoundEvent> SILVER_HIT = HELPER.createSoundEvent("block.silver.hit");
	public static final RegistryObject<SoundEvent> SILVER_PLACE = HELPER.createSoundEvent("block.silver.place");
	public static final RegistryObject<SoundEvent> SILVER_STEP = HELPER.createSoundEvent("block.silver.step");

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

	public static final RegistryObject<SoundEvent> FLINT_BLOCK_STRIKE = HELPER.createSoundEvent("block.flint_block.strike");

	public static final RegistryObject<SoundEvent> TIN_DEFLECT = HELPER.createSoundEvent("block.tin.deflect");
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

	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SILVER = HELPER.createSoundEvent("item.armor.equip_silver");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_NECROMIUM = HELPER.createSoundEvent("item.armor.equip_necromium");
	public static final RegistryObject<SoundEvent> ARMOR_NECROMIUM_INFLICT = HELPER.createSoundEvent("item.armor.necromium_inflict");
	public static final RegistryObject<SoundEvent> ARMOR_EQUIP_SANGUINE = HELPER.createSoundEvent("item.armor.equip_sanguine");

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
		public static final ForgeSoundType ROCKY_DIRT = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.ROCKY_DIRT_BREAK, CCSoundEvents.ROCKY_DIRT_STEP, CCSoundEvents.ROCKY_DIRT_PLACE, CCSoundEvents.ROCKY_DIRT_HIT, CCSoundEvents.ROCKY_DIRT_FALL);
		public static final ForgeSoundType SOUL_SILVER_ORE = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.SOUL_SILVER_ORE_BREAK, CCSoundEvents.SOUL_SILVER_ORE_STEP, CCSoundEvents.SOUL_SILVER_ORE_PLACE, CCSoundEvents.SOUL_SILVER_ORE_HIT, CCSoundEvents.SOUL_SILVER_ORE_FALL);
		public static final ForgeSoundType SPINEL = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.SPINEL_BREAK, CCSoundEvents.SPINEL_STEP, CCSoundEvents.SPINEL_PLACE, CCSoundEvents.SPINEL_HIT, CCSoundEvents.SPINEL_FALL);
		public static final ForgeSoundType SUGILITE = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.SUGILITE_BREAK, CCSoundEvents.SUGILITE_STEP, CCSoundEvents.SUGILITE_PLACE, CCSoundEvents.SUGILITE_HIT, CCSoundEvents.SUGILITE_FALL);
		public static final ForgeSoundType ZIRCONIA = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.ZIRCONIA_BREAK, CCSoundEvents.ZIRCONIA_STEP, CCSoundEvents.ZIRCONIA_PLACE, CCSoundEvents.ZIRCONIA_HIT, CCSoundEvents.ZIRCONIA_FALL);
		public static final ForgeSoundType NECROMIUM = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.NECROMIUM_BREAK, CCSoundEvents.NECROMIUM_STEP, CCSoundEvents.NECROMIUM_PLACE, CCSoundEvents.NECROMIUM_HIT, CCSoundEvents.NECROMIUM_FALL);
		public static final ForgeSoundType FALSE_HOPE = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.FALSE_HOPE_BREAK, CCSoundEvents.FALSE_HOPE_STEP, CCSoundEvents.FALSE_HOPE_PLACE, CCSoundEvents.FALSE_HOPE_HIT, CCSoundEvents.FALSE_HOPE_FALL);
		public static final ForgeSoundType CAVE_GROWTHS = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.CAVE_GROWTHS_BREAK, CCSoundEvents.CAVE_GROWTHS_STEP, CCSoundEvents.CAVE_GROWTHS_PLACE, CCSoundEvents.CAVE_GROWTHS_HIT, CCSoundEvents.CAVE_GROWTHS_FALL);
		public static final ForgeSoundType MOSCHATEL = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.MOSCHATEL_BREAK, CCSoundEvents.MOSCHATEL_STEP, CCSoundEvents.MOSCHATEL_PLACE, CCSoundEvents.MOSCHATEL_HIT, CCSoundEvents.MOSCHATEL_FALL);
		public static final ForgeSoundType TMT = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.TMT_BREAK, CCSoundEvents.TMT_STEP, CCSoundEvents.TMT_PLACE, CCSoundEvents.TMT_HIT, CCSoundEvents.TMT_FALL);
		public static final ForgeSoundType SANGUINE = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.SANGUINE_BREAK, CCSoundEvents.SANGUINE_STEP, CCSoundEvents.SANGUINE_PLACE, CCSoundEvents.SANGUINE_HIT, CCSoundEvents.SANGUINE_FALL);
		public static final ForgeSoundType LAVA_LAMP = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.LAVA_LAMP_BREAK, CCSoundEvents.LAVA_LAMP_STEP, CCSoundEvents.LAVA_LAMP_PLACE, CCSoundEvents.LAVA_LAMP_HIT, CCSoundEvents.LAVA_LAMP_FALL);
		public static final ForgeSoundType FLOODLIGHT = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.FLOODLIGHT_BREAK, CCSoundEvents.FLOODLIGHT_STEP, CCSoundEvents.FLOODLIGHT_PLACE, CCSoundEvents.FLOODLIGHT_HIT, CCSoundEvents.FLOODLIGHT_FALL);
		public static final ForgeSoundType SILVER = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.SILVER_BREAK, CCSoundEvents.SILVER_STEP, CCSoundEvents.SILVER_PLACE, CCSoundEvents.SILVER_HIT, CCSoundEvents.SILVER_FALL);
		public static final ForgeSoundType ATONING_TABLE = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.ATONING_TABLE_BREAK, CCSoundEvents.ATONING_TABLE_STEP, CCSoundEvents.ATONING_TABLE_PLACE, CCSoundEvents.ATONING_TABLE_HIT, CCSoundEvents.ATONING_TABLE_FALL);
		public static final ForgeSoundType BEJEWELED_ANVIL = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.BEJEWELED_ANVIL_BREAK, CCSoundEvents.BEJEWELED_ANVIL_STEP, CCSoundEvents.BEJEWELED_ANVIL_PLACE, CCSoundEvents.BEJEWELED_ANVIL_HIT, CCSoundEvents.BEJEWELED_ANVIL_FALL);
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