package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.tags.GameEventTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCGameEventTagsProvider extends GameEventTagsProvider {

	public CCGameEventTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags() {
		this.tag(GameEventTags.VIBRATIONS).add(CCGameEvents.TUNING_FORK_VIBRATE.get());
		this.tag(GameEventTags.WARDEN_CAN_LISTEN).add(CCGameEvents.TUNING_FORK_VIBRATE.get());
	}
}