package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;

public class CCEntityTypeTags {
	public static final Tag.Named<EntityType<?>> COLLAR_DROP_MOBS = entityTypeTag("collar_drop_mobs");
	public static final Tag.Named<EntityType<?>> MILKABLE = TagUtil.forgeEntityTypeTag("milkable");

	private static Tag.Named<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(CavernsAndChasms.MOD_ID, name);
	}
}