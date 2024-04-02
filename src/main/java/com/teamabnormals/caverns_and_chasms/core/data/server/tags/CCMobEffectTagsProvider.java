package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCMobEffectTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class CCMobEffectTagsProvider extends IntrinsicHolderTagsProvider<MobEffect> {

	public CCMobEffectTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, Registries.MOB_EFFECT, provider, effect -> ForgeRegistries.MOB_EFFECTS.getResourceKey(effect).get(), CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(CCMobEffectTags.BEJEWELED_APPLE_CANNOT_INFLICT).add(MobEffects.BAD_OMEN, MobEffects.HERO_OF_THE_VILLAGE).addOptional(new ResourceLocation("environmental", "serenity"));
	}

	@Override
	public String getName() {
		return "Mob Effects";
	}
}