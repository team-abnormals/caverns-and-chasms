package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCInstruments;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.InstrumentTagsProvider;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.item.Instrument;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;

public class CCInstrumentTagsProvider extends InstrumentTagsProvider {

	public CCInstrumentTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(InstrumentTags.REGULAR_GOAT_HORNS).add(CCInstruments.FLY_GOAT_HORN.getKey());
		this.tag(InstrumentTags.SCREAMING_GOAT_HORNS).add(CCInstruments.RESIST_GOAT_HORN.getKey());
		this.tag(CCInstrumentTags.LOST_GOAT_HORNS).add(CCInstruments.WARN_LOST_GOAT_HORN.getKey(), CCInstruments.BELLOW_LOST_GOAT_HORN.getKey());
		this.tagCopperHornInstruments(
				CCInstruments.GREAT_SKY_FALLING_COPPER_HORN,
				CCInstruments.OLD_HYMN_RESTING_COPPER_HORN,
				CCInstruments.PURE_WATER_DESIRE_COPPER_HORN,
				CCInstruments.HUMBLE_FIRE_MEMORY_COPPER_HORN,
				CCInstruments.DRY_URGE_ANGER_COPPER_HORN,
				CCInstruments.CLEAR_TEMPER_JOURNEY_COPPER_HORN,
				CCInstruments.FRESH_NEST_THOUGHT_COPPER_HORN,
				CCInstruments.SECRET_LAKE_TEAR_COPPER_HORN,
				CCInstruments.FEARLESS_RIVER_GIFT_COPPER_HORN,
				CCInstruments.SWEET_MOON_LOVE_COPPER_HORN
		);
	}

	@SafeVarargs
	public final void tagCopperHornInstruments(ImmutableList<RegistryObject<Instrument>>... instruments) {
		for (ImmutableList<RegistryObject<Instrument>> instrumentList : instruments) {
			this.tag(CCInstrumentTags.HARMONY_COPPER_HORNS).add(instrumentList.get(0).getKey());
			this.tag(CCInstrumentTags.MELODY_COPPER_HORNS).add(instrumentList.get(1).getKey());
			this.tag(CCInstrumentTags.BASS_COPPER_HORNS).add(instrumentList.get(2).getKey());
		}
	}
}