package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Instrument;

public class CCInstrumentTags {
	public static final TagKey<Instrument> LOST_GOAT_HORNS = instrumentTag("lost_goat_horns");

	private static TagKey<Instrument> instrumentTag(String name) {
		return instrumentTag(CavernsAndChasms.MOD_ID, name);
	}

	private static TagKey<Instrument> instrumentTag(String modid, String name) {
		return TagKey.create(Registry.INSTRUMENT_REGISTRY, new ResourceLocation(modid, name));
	}
}