package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCInstruments;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.InstrumentTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCInstrumentTagsProvider extends InstrumentTagsProvider {

	public CCInstrumentTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags() {
		this.tag(CCInstrumentTags.LOST_GOAT_HORNS).add(CCInstruments.WARN_LOST_GOAT_HORN.get(), CCInstruments.BELLOW_LOST_GOAT_HORN.get());
	}
}