package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCDamageTypeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCDamageTypes.*;

public class CCDamageTypeTagsProvider extends DamageTypeTagsProvider {

	public CCDamageTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(DamageTypeTags.WITCH_RESISTANT_TO).add(KUNAI, SPIKED_RAIL, DRAINING);
		this.tag(DamageTypeTags.BYPASSES_ARMOR).add(KUNAI, SPIKED_RAIL, DRAINING);
		this.tag(Tags.DamageTypes.IS_MAGIC).add(KUNAI, SPIKED_RAIL, DRAINING, DamageTypes.SONIC_BOOM);

		this.tag(DamageTypeTags.IS_FIRE).add(LAVA_LAMP);
		this.tag(DamageTypeTags.IS_PROJECTILE).add(KUNAI);
		this.tag(DamageTypeTags.NO_ANGER).add(GRAZER);
		this.tag(DamageTypeTags.NO_KNOCKBACK).add(LAVA_LAMP, SPIKED_RAIL, DRAINING);

		this.tag(CCDamageTypeTags.SILVER_RESISTANT_TO).addTag(Tags.DamageTypes.IS_MAGIC);
		this.tag(CCDamageTypeTags.BYPASSES_TETHER_POTIONS);
		this.tag(CCDamageTypeTags.DRAINS_ENEMIES).add(DRAINING);
	}
}