package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

public class CCLanguageProvider extends LanguageProvider {

	public CCLanguageProvider(DataGenerator gen) {
		super(gen, CavernsAndChasms.MOD_ID, "en_us");
	}

	@Override
	public void addTranslations() {
		this.add(CCItems.RAW_SILVER.get());
		this.add(CCBlocks.RAW_SILVER_BLOCK.get());
		this.add(CCBlocks.DEEPSLATE_SILVER_ORE.get());
		this.add(CCBlocks.DEEPSLATE_SPINEL_ORE.get());

		this.add(CCBlocks.COPPER_BARS.get());
		this.add(CCBlocks.EXPOSED_COPPER_BARS.get());
		this.add(CCBlocks.WEATHERED_COPPER_BARS.get());
		this.add(CCBlocks.OXIDIZED_COPPER_BARS.get());
		this.add(CCBlocks.WAXED_COPPER_BARS.get());
		this.add(CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		this.add(CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		this.add(CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
	}

	private void add(Item item) {
		if (item.getRegistryName() != null)
			this.add(item, format(item.getRegistryName()));
	}

	private void add(Block block) {
		if (block.getRegistryName() != null)
			this.add(block, format(block.getRegistryName()));
	}

	private String format(ResourceLocation registryName) {
		return WordUtils.capitalizeFully(registryName.getPath().replace("_", " "));
	}
}