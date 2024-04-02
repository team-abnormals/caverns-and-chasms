package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public class CCBannerPatternTags {
	public static final TagKey<BannerPattern> PATTERN_ITEM_ABNORMALS = bannerPatternTag("pattern_item/abnormals");

	private static TagKey<BannerPattern> bannerPatternTag(String name) {
		return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
	}
}