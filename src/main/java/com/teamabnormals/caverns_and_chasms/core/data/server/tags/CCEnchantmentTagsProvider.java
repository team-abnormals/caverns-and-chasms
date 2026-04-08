package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEnchantmentTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCEnchantments.*;

public class CCEnchantmentTagsProvider extends EnchantmentTagsProvider {

	public CCEnchantmentTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(EnchantmentTags.NON_TREASURE).add(EXTENDING, CONCEAL, OBSCURITY);
		this.tag(CCEnchantmentTags.COWL_EXCLUSIVE).add(CONCEAL, OBSCURITY);
	}
}