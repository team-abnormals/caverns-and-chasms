package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCInstruments {
	public static final DeferredRegister<Instrument> INSTRUMENTS = DeferredRegister.create(Registries.INSTRUMENT, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Instrument> FLY_GOAT_HORN = INSTRUMENTS.register("fly_goat_horn", () -> new Instrument(CCSoundEvents.GOAT_HORN_SOUND_VARIANTS.get(0).getHolder().get(), 140, 256.0F));
	public static final RegistryObject<Instrument> RESIST_GOAT_HORN = INSTRUMENTS.register("resist_goat_horn", () -> new Instrument(CCSoundEvents.GOAT_HORN_SOUND_VARIANTS.get(1).getHolder().get(), 140, 256.0F));

	public static final RegistryObject<Instrument> WARN_LOST_GOAT_HORN = INSTRUMENTS.register("warn_lost_goat_horn", () -> new Instrument(CCSoundEvents.LOST_GOAT_HORN_SOUND_VARIANTS.get(0).getHolder().get(), 140, 256.0F));
	public static final RegistryObject<Instrument> BELLOW_LOST_GOAT_HORN = INSTRUMENTS.register("bellow_lost_goat_horn", () -> new Instrument(CCSoundEvents.LOST_GOAT_HORN_SOUND_VARIANTS.get(1).getHolder().get(), 140, 256.0F));

	public static final ImmutableList<RegistryObject<Instrument>> GREAT_SKY_FALLING_COPPER_HORN = registerCopperHorn("great", "sky", "falling", 0);
	public static final ImmutableList<RegistryObject<Instrument>> OLD_HYMN_RESTING_COPPER_HORN = registerCopperHorn("old", "hymn", "resting", 1);
	public static final ImmutableList<RegistryObject<Instrument>> PURE_WATER_DESIRE_COPPER_HORN = registerCopperHorn("pure", "water", "desire", 2);
	public static final ImmutableList<RegistryObject<Instrument>> HUMBLE_FIRE_MEMORY_COPPER_HORN = registerCopperHorn("humble", "fire", "memory", 3);
	public static final ImmutableList<RegistryObject<Instrument>> DRY_URGE_ANGER_COPPER_HORN = registerCopperHorn("dry", "urge", "anger", 4);
	public static final ImmutableList<RegistryObject<Instrument>> CLEAR_TEMPER_JOURNEY_COPPER_HORN = registerCopperHorn("clear", "temper", "journey", 5);
	public static final ImmutableList<RegistryObject<Instrument>> FRESH_NEST_THOUGHT_COPPER_HORN = registerCopperHorn("fresh", "nest", "thought", 6);
	public static final ImmutableList<RegistryObject<Instrument>> SECRET_LAKE_TEAR_COPPER_HORN = registerCopperHorn("secret", "lake", "tear", 7);
	public static final ImmutableList<RegistryObject<Instrument>> FEARLESS_RIVER_GIFT_COPPER_HORN = registerCopperHorn("fearless", "river", "gift", 8);
	public static final ImmutableList<RegistryObject<Instrument>> SWEET_MOON_LOVE_COPPER_HORN = registerCopperHorn("sweet", "moon", "love", 9);

	public static ImmutableList<RegistryObject<Instrument>> registerCopperHorn(String harmonyName, String melodyName, String bassName, int id) {
		RegistryObject<Instrument> harmony = INSTRUMENTS.register(harmonyName + "_copper_horn", () -> new Instrument(CCSoundEvents.COPPER_HORN_HARMONY_SOUND_VARIANTS.get(id).getHolder().get(), 80, 64.0F));
		RegistryObject<Instrument> melody = INSTRUMENTS.register(melodyName + "_copper_horn", () -> new Instrument(CCSoundEvents.COPPER_HORN_MELODY_SOUND_VARIANTS.get(id).getHolder().get(), 80, 64.0F));
		RegistryObject<Instrument> bass = INSTRUMENTS.register(bassName + "_copper_horn", () -> new Instrument(CCSoundEvents.COPPER_HORN_BASS_SOUND_VARIANTS.get(id).getHolder().get(), 80, 64.0F));
		return ImmutableList.of(harmony, melody, bass);
	}

}