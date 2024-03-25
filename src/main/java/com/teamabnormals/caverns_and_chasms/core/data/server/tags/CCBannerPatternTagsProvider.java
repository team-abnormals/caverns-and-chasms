package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBannerPatternTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBannerPatterns;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCBannerPatternTagsProvider extends BannerPatternTagsProvider {

	public CCBannerPatternTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, fileHelper);
	}

	@Override
	public void addTags() {
		this.tag(CCBannerPatternTags.PATTERN_ITEM_ABNORMALS).add(CCBannerPatterns.ABNORMALS.get());
	}
}