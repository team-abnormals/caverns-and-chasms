package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags.SPAWNS_FROM_CAVE_GROWTHS;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes.*;

public class CCEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public CCEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		// If we add rats and flies back they need to be in the FALL_DAMAGE_IMMUNE tag.
		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(COPPER_GOLEM.get(), GLARE.get());
		this.tag(EntityTypeTags.ARROWS).add(LARGE_ARROW.get(), BLUNT_ARROW.get());
		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(KUNAI.get());

		this.tag(SPAWNS_FROM_CAVE_GROWTHS).add(EntityType.BAT, EntityType.SILVERFISH);
	}
}