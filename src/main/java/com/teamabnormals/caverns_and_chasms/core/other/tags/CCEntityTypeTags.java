package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class CCEntityTypeTags {
	public static final TagKey<EntityType<?>> SPAWNS_FROM_CAVE_GROWTHS = entityTypeTag("spawns_from_cave_growths");

	private static TagKey<EntityType<?>> entityTypeTag(String tagName) {
		return TagUtil.entityTypeTag(CavernsAndChasms.MOD_ID, tagName);
	}
}