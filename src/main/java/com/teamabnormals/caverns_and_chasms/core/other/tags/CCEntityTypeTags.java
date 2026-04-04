package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class CCEntityTypeTags {
	public static final TagKey<EntityType<?>> SPAWNS_FROM_CAVE_GROWTHS = entityTypeTag("spawns_from_cave_growths");
	public static final TagKey<EntityType<?>> NOT_DEFLECTED_BY_TIN = entityTypeTag("not_deflected_by_tin");
	public static final TagKey<EntityType<?>> SILVER_HURTS_EXTRA_TYPES = entityTypeTag("silver_hurts_extra_types");
	public static final TagKey<EntityType<?>> RATS_CANNOT_ATTACH_EXTRA_TYPES = entityTypeTag("rats_cannot_attach_extra_types");

	private static TagKey<EntityType<?>> entityTypeTag(String tagName) {
		return TagUtil.entityTypeTag(CavernsAndChasms.MOD_ID, tagName);
	}
}