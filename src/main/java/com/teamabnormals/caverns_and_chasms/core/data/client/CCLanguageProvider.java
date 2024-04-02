package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class CCLanguageProvider extends LanguageProvider {

	public CCLanguageProvider(PackOutput output) {
		super(output, CavernsAndChasms.MOD_ID, "en_us");
	}

	@Override
	public void addTranslations() {
		this.add(CCBlocks.SILVER_ORE.get(), CCBlocks.DEEPSLATE_SILVER_ORE.get(), CCBlocks.SOUL_SILVER_ORE.get());
		this.add(CCBlocks.SILVER_BARS.get(), CCBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), CCBlocks.SILVER_BUTTON.get(), CCBlocks.SPIKED_RAIL.get());
		this.add(CCBlocks.SANGUINE_PLATES.get(), CCBlocks.SANGUINE_SLAB.get(), CCBlocks.SANGUINE_STAIRS.get());
		this.add(CCBlocks.SPINEL_ORE.get(), CCBlocks.DEEPSLATE_SPINEL_ORE.get());
		this.add(CCBlocks.SPINEL_BRICKS.get(), CCBlocks.SPINEL_BRICK_SLAB.get(), CCBlocks.SPINEL_BRICK_STAIRS.get(), CCBlocks.SPINEL_BRICK_WALL.get(), CCBlocks.SPINEL_PILLAR.get(), CCBlocks.SPINEL_LAMP.get());
		this.addStorageBlock(CCBlocks.RAW_SILVER_BLOCK.get(), CCBlocks.SILVER_BLOCK.get(), CCBlocks.SPINEL_BLOCK.get(), CCBlocks.NECROMIUM_BLOCK.get());
		this.add(CCBlocks.LAPIS_LAZULI_BRICKS.get(), CCBlocks.LAPIS_LAZULI_BRICK_SLAB.get(), CCBlocks.LAPIS_LAZULI_BRICK_STAIRS.get(), CCBlocks.LAPIS_LAZULI_BRICK_WALL.get(), CCBlocks.LAPIS_LAZULI_PILLAR.get(), CCBlocks.LAPIS_LAZULI_LAMP.get());
		this.add(CCBlocks.COPPER_BARS.get(), CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.OXIDIZED_COPPER_BARS.get());
		this.add(CCBlocks.WAXED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
		this.add(CCBlocks.COPPER_BUTTON.get(), CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.OXIDIZED_COPPER_BUTTON.get());
		this.add(CCBlocks.WAXED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
		// CCBlocks.INDUCTOR.get()
		this.add(CCBlocks.FLOODLIGHT.get());
		this.add(CCBlocks.LAVA_LAMP.get(), CCBlocks.GOLDEN_BARS.get());
		this.add(CCBlocks.ROTTEN_FLESH_BLOCK.get());
		this.add(CCBlocks.BRAZIER.get(), CCBlocks.SOUL_BRAZIER.get(), CCBlocks.ENDER_BRAZIER.get(), CCBlocks.CUPRIC_BRAZIER.get());
		this.add(CCBlocks.CUPRIC_FIRE.get(), CCBlocks.CUPRIC_TORCH.get(), CCBlocks.CUPRIC_LANTERN.get(), CCBlocks.CUPRIC_CAMPFIRE.get());
		this.add(CCBlocks.FRAGILE_STONE.get(), CCBlocks.FRAGILE_DEEPSLATE.get(), CCBlocks.ROCKY_DIRT.get());
		this.add(CCBlocks.COBBLESTONE_BRICKS.get(), CCBlocks.COBBLESTONE_BRICK_SLAB.get(), CCBlocks.COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.COBBLESTONE_BRICK_WALL.get());
		this.add(CCBlocks.COBBLESTONE_TILES.get(), CCBlocks.COBBLESTONE_TILE_SLAB.get(), CCBlocks.COBBLESTONE_TILE_STAIRS.get(), CCBlocks.COBBLESTONE_TILE_WALL.get());
		this.add(CCBlocks.MOSSY_COBBLESTONE_BRICKS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get());
		this.add(CCBlocks.MOSSY_COBBLESTONE_TILES.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_SLAB.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_STAIRS.get(), CCBlocks.MOSSY_COBBLESTONE_TILE_WALL.get());

		this.add(CCItems.RAW_SILVER.get(), CCItems.SILVER_INGOT.get(), CCItems.SILVER_NUGGET.get());
		this.add(CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get());
		this.add(CCItems.NECROMIUM_INGOT.get(), CCItems.NECROMIUM_NUGGET.get());
		this.add(CCItems.NECROMIUM_HELMET.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.NECROMIUM_BOOTS.get());
		this.add(CCItems.COPPER_NUGGET.get(), CCItems.BAROMETER.get(), CCItems.TUNING_FORK.get(), CCItems.OXIDIZED_COPPER_GOLEM.get());
		this.add(CCItems.GOLDEN_BUCKET.get(), CCItems.GOLDEN_WATER_BUCKET.get(), CCItems.GOLDEN_LAVA_BUCKET.get(), CCItems.GOLDEN_MILK_BUCKET.get(), CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
		this.add(CCItems.SANGUINE_PLATING.get(), CCItems.SANGUINE_HELMET.get(), CCItems.SANGUINE_CHESTPLATE.get(), CCItems.SANGUINE_LEGGINGS.get(), CCItems.SANGUINE_BOOTS.get());
		this.add(CCItems.DEEPER_SPAWN_EGG.get(), CCItems.MIME_SPAWN_EGG.get());//, CCItems.RAT_SPAWN_EGG.get());
	}

	private void add(Block... blocks) {
		List.of(blocks).forEach((block -> this.add(block, format(ForgeRegistries.BLOCKS.getKey(block)))));
	}

	private void add(Item... items) {
		List.of(items).forEach((item -> this.add(item, format(ForgeRegistries.ITEMS.getKey(item)))));
	}

	private void addStorageBlock(Block... blocks) {
		List.of(blocks).forEach((block -> this.add(block, "Block of " + format(ForgeRegistries.BLOCKS.getKey(block)).replace(" Block", ""))));
	}

	private String format(ResourceLocation registryName) {
		return WordUtils.capitalizeFully(registryName.getPath().replace("_", " "));
	}
}