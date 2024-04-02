package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;


public class CCItemModelProvider extends ItemModelProvider {

	public CCItemModelProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		this.animatedModel(DEPTH_GAUGE.get(), 48);
		this.animatedModel(BAROMETER.get(), 21);
		this.generatedItem(
				ABNORMALS_BANNER_PATTERN.get(),
				COPPER_NUGGET.get(), OXIDIZED_COPPER_GOLEM.get(),
				RAW_SILVER.get(), LARGE_ARROW.get(),
				BEJEWELED_APPLE.get(), BLUNT_ARROW.get(),
				AZALEA_BOAT.getFirst().get(), AZALEA_BOAT.getSecond().get(), AZALEA_FURNACE_BOAT.get(), LARGE_AZALEA_BOAT.get()
		);
		this.item(WAXED_OXIDIZED_COPPER_GOLEM.get(), "oxidized_copper_golem", "generated");
		this.handheldItem(KUNAI.get());
		this.spawnEggItem(PEEPER_SPAWN_EGG.get());
	}

	private void generatedItem(ItemLike... items) {
		for (ItemLike item : items)
			item(item, "generated");
	}

	private void handheldItem(ItemLike... items) {
		for (ItemLike item : items)
			item(item, "handheld");
	}

	private void spawnEggItem(ItemLike... items) {
		for (ItemLike item : items) {
			ResourceLocation name = ForgeRegistries.ITEMS.getKey(item.asItem());
			if (name != null)
				withExistingParent(name.getPath(), "item/template_spawn_egg");
		}
	}

	private void item(ItemLike item, String type) {
		ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(item.asItem());
		withExistingParent(itemName.getPath(), "item/" + type).texture("layer0", new ResourceLocation(this.modid, "item/" + itemName.getPath()));
	}

	private void item(ItemLike item, String path, String type) {
		ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(item.asItem());
		withExistingParent(itemName.getPath(), "item/" + type).texture("layer0", new ResourceLocation(this.modid, "item/" + path));
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