package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Instrument;

public class CCInstrumentTags {
	public static final TagKey<Instrument> LOST_GOAT_HORNS = instrumentTag("lost_goat_horns");

	public static final TagKey<Instrument> HARMONY_COPPER_HORNS = instrumentTag("harmony_copper_horns");
	public static final TagKey<Instrument> MELODY_COPPER_HORNS = instrumentTag("melody_copper_horns");
	public static final TagKey<Instrument> BASS_COPPER_HORNS = instrumentTag("bass_copper_horns");

	private static TagKey<Instrument> instrumentTag(String name) {
		return instrumentTag(CavernsAndChasms.MOD_ID, name);
	}

	private static TagKey<Instrument> instrumentTag(String modid, String name) {
		return TagKey.create(Registries.INSTRUMENT, new ResourceLocation(modid, name));
	}
}