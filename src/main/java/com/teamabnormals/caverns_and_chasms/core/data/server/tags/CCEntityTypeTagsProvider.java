package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public CCEntityTypeTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags() {
		this.tag(EntityTypeTags.ARROWS).add(CCEntityTypes.SILVER_ARROW.get());
	}
}