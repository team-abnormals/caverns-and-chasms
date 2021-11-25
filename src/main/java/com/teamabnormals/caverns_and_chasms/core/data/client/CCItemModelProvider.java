package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;


public class CCItemModelProvider extends ItemModelProvider {

	public CCItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.generated(CCItems.RAW_SILVER.get());
		this.generated(CCItems.SPINEL_PEARL.get());
		this.handheld(CCItems.TUNING_FORK.get());
	}

	private void generated(ItemLike item) {
		basicModel(item, "generated");
	}

	private void handheld(ItemLike item) {
		basicModel(item, "handheld");
	}

	private void basicModel(ItemLike item, String type) {
		ResourceLocation itemName = item.asItem().getRegistryName();
		withExistingParent(itemName.getPath(), "item/" + type).texture("layer0", new ResourceLocation(this.modid, "item/" + itemName.getPath()));
	}

	private void blockItem(Block block) {
		ResourceLocation name = block.getRegistryName();
		this.getBuilder(name.getPath()).parent(new UncheckedModelFile(new ResourceLocation(this.modid, "block/" + name.getPath())));
	}
}