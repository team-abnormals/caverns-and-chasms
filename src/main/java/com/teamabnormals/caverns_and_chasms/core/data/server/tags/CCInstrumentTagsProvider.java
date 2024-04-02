package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCInstruments;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.InstrumentTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCInstrumentTagsProvider extends InstrumentTagsProvider {

	public CCInstrumentTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(CCInstrumentTags.LOST_GOAT_HORNS).add(CCInstruments.WARN_LOST_GOAT_HORN.getKey(), CCInstruments.BELLOW_LOST_GOAT_HORN.getKey());
	}
}