package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.tags.GameEventTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCGameEventTagsProvider extends GameEventTagsProvider {

	public CCGameEventTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(GameEventTags.VIBRATIONS).add(CCGameEvents.TUNING_FORK_VIBRATE.get());
		this.tag(GameEventTags.WARDEN_CAN_LISTEN).add(CCGameEvents.TUNING_FORK_VIBRATE.get());
	}
}