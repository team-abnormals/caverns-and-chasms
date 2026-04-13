package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCArmorMaterialTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCArmorMaterialTagsProvider extends TagsProvider<ArmorMaterial> {

	public CCArmorMaterialTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, Registries.ARMOR_MATERIAL, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(CCArmorMaterialTags.COPPER).add(
				CCArmorMaterials.COPPER.getKey(),
				CCArmorMaterials.EXPOSED_COPPER.getKey(),
				CCArmorMaterials.WEATHERED_COPPER.getKey(),
				CCArmorMaterials.OXIDIZED_COPPER.getKey()
		);
	}
}