package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCInstruments {
	public static final DeferredRegister<Instrument> INSTRUMENTS = DeferredRegister.create(Registries.INSTRUMENT, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Instrument> WARN_LOST_GOAT_HORN = INSTRUMENTS.register("warn_lost_goat_horn", () -> new Instrument(CCSoundEvents.LOST_GOAT_HORN_SOUND_VARIANTS.get(0).getHolder().get(), 140, 256.0F));
	public static final RegistryObject<Instrument> BELLOW_LOST_GOAT_HORN = INSTRUMENTS.register("bellow_lost_goat_horn", () -> new Instrument(CCSoundEvents.LOST_GOAT_HORN_SOUND_VARIANTS.get(1).getHolder().get(), 140, 256.0F));
}