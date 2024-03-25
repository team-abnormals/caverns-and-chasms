package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public class CCBannerPatternTags {
	public static final TagKey<BannerPattern> PATTERN_ITEM_ABNORMALS = bannerPatternTag("pattern_item/abnormals");

	private static TagKey<BannerPattern> bannerPatternTag(String name) {
		return TagKey.create(Registry.BANNER_PATTERN_REGISTRY, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
	}
}