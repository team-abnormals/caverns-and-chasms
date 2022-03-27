package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
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
		this.generated(CCItems.COPPER_NUGGET.get());
		this.generated(CCItems.RAW_SILVER.get());
		this.generated(CCItems.BEJEWELED_PEARL.get());
		this.generated(CCItems.BEJEWELED_APPLE.get());
		this.generated(CCItems.SPINEL_CROWN.get());
		this.generated(CCBlocks.CUPRIC_CANDLE.get());
		this.animatedModel(CCItems.DEPTH_GAUGE.get(), 48);
		this.generated(CCItems.BAROMETER.get());
		this.generated(CCItems.OXIDIZED_COPPER_GOLEM.get());
		this.generated(CCItems.AZALEA_BOAT.get());
		this.generated(CCItems.AZALEA_CHEST_BOAT.get());
		this.generated(CCItems.AZALEA_FURNACE_BOAT.get());
		this.generated(CCItems.LARGE_AZALEA_BOAT.get());
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

	private void animatedModel(ItemLike item, int count) {
		for (int i = 0; i < count; i++) {
			String path = item.asItem().getRegistryName().getPath() + "_" + String.format("%02d", i);
			withExistingParent(path, "item/generated").texture("layer0", new ResourceLocation(this.modid, "item/" + path));
		}
	}
}