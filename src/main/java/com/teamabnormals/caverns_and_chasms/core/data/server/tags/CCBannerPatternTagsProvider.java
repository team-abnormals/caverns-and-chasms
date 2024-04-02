package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBannerPatternTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBannerPatterns;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCBannerPatternTagsProvider extends BannerPatternTagsProvider {

	public CCBannerPatternTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(CCBannerPatternTags.PATTERN_ITEM_ABNORMALS).add(CCBannerPatterns.ABNORMALS.getKey());
	}
}