package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;


public class CCItemModelProvider extends ItemModelProvider {

	public CCItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.generatedItem(CCBlocks.CUPRIC_CANDLE.get());
		this.animatedModel(DEPTH_GAUGE.get(), 48);
		this.animatedModel(BAROMETER.get(), 21);
		this.generatedItem(
				COPPER_NUGGET.get(), OXIDIZED_COPPER_GOLEM.get(),
				RAW_SILVER.get(),
				BEJEWELED_APPLE.get(),
				AZALEA_BOAT.getFirst().get(), AZALEA_BOAT.getSecond().get(), AZALEA_FURNACE_BOAT.get(), LARGE_AZALEA_BOAT.get()
		);
		this.handheldItem(
				KUNAI.get(),
				BLUNT_ARROW.get()
		);
	}

	private void generatedItem(ItemLike... items) {
		for (ItemLike item : items)
			item(item, "generated");
	}

	private void handheldItem(ItemLike... items) {
		for (ItemLike item : items)
			item(item, "handheld");
	}

	private void item(ItemLike item, String type) {
		ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(item.asItem());
		withExistingParent(itemName.getPath(), "item/" + type).texture("layer0", new ResourceLocation(this.modid, "item/" + itemName.getPath()));
	}

	private void blockItem(Block block) {
		ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
		this.getBuilder(name.getPath()).parent(new UncheckedModelFile(new ResourceLocation(this.modid, "block/" + name.getPath())));
	}

	private void animatedModel(ItemLike item, int count) {
		for (int i = 0; i < count; i++) {
			String path = ForgeRegistries.ITEMS.getKey(item.asItem()).getPath() + "_" + String.format("%02d", i);
			withExistingParent(path, "item/generated").texture("layer0", new ResourceLocation(this.modid, "item/" + path));
		}
	}
}