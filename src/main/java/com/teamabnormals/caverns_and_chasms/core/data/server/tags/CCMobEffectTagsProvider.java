package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCMobEffectTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCMobEffectTagsProvider extends TagsProvider<MobEffect> {

	public CCMobEffectTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(generator, Registry.MOB_EFFECT, CavernsAndChasms.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(CCMobEffectTags.BEJEWELED_APPLE_CANNOT_INFLICT).add(MobEffects.BAD_OMEN, MobEffects.HERO_OF_THE_VILLAGE).addOptional(new ResourceLocation("environmental", "serenity"));
	}

	@Override
	public String getName() {
		return "Mob Effects";
	}
}