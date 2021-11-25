package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CCItemTagsProvider extends ItemTagsProvider {

	public CCItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generator, blockTagsProvider, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags() {
		this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);

		this.copy(CCBlockTags.SILVER_ORES, CCItemTags.SILVER_ORES);
		this.copy(CCBlockTags.SPINEL_ORES, CCItemTags.SPINEL_ORES);
		this.copy(CCBlockTags.CURSED_FIRE_BASE_BLOCKS, CCItemTags.CURSED_FIRE_BASE_BLOCKS);

		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
		this.copy(CCBlockTags.ORES_COPPER, CCItemTags.ORES_COPPER);
		this.copy(CCBlockTags.ORES_AMETHYST, CCItemTags.ORES_AMETHYST);
		this.copy(CCBlockTags.ORES_SILVER, CCItemTags.ORES_SILVER);
		this.copy(CCBlockTags.ORES_SPINEL, CCItemTags.ORES_SPINEL);
		this.tag(Tags.Items.ORES_COAL).remove(Items.COAL_ORE).addTag(ItemTags.COAL_ORES);
		this.tag(Tags.Items.ORES_EMERALD).remove(Items.EMERALD_ORE).addTag(ItemTags.EMERALD_ORES);
		this.tag(Tags.Items.ORES_DIAMOND).remove(Items.DIAMOND_ORE).addTag(ItemTags.DIAMOND_ORES);
		this.tag(Tags.Items.ORES_IRON).remove(Items.IRON_ORE).addTag(ItemTags.IRON_ORES);
		this.tag(Tags.Items.ORES_LAPIS).remove(Items.LAPIS_ORE).addTag(ItemTags.LAPIS_ORES);
		this.tag(Tags.Items.ORES_REDSTONE).remove(Items.REDSTONE_ORE).addTag(ItemTags.REDSTONE_ORES);

		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(CCBlockTags.STORAGE_BLOCKS_COPPER, CCItemTags.STORAGE_BLOCKS_COPPER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SILVER, CCItemTags.STORAGE_BLOCKS_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SPINEL, CCItemTags.STORAGE_BLOCKS_SPINEL);
		this.copy(CCBlockTags.STORAGE_BLOCKS_NECROMIUM, CCItemTags.STORAGE_BLOCKS_NECROMIUM);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_COPPER, CCItemTags.STORAGE_BLOCKS_RAW_COPPER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_IRON, CCItemTags.STORAGE_BLOCKS_RAW_IRON);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_GOLD, CCItemTags.STORAGE_BLOCKS_RAW_GOLD);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER, CCItemTags.STORAGE_BLOCKS_RAW_SILVER);

		this.copy(BlueprintBlockTags.VERTICAL_SLABS, BlueprintItemTags.VERTICAL_SLABS);

		this.tag(ItemTags.ARROWS).add(CCItems.SILVER_ARROW.get());
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(CCItems.SILVER_INGOT.get(), CCItems.NECROMIUM_INGOT.get());
		this.tag(ItemTags.FISHES).add(CCItems.CAVEFISH.get(), CCItems.COOKED_CAVEFISH.get());
		this.tag(ItemTags.MUSIC_DISCS).add(CCItems.MUSIC_DISC_EPILOGUE.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(CCBlocks.GOLDEN_LANTERN.get().asItem(), CCBlocks.GOLDEN_BARS.get().asItem(), CCItems.GOLDEN_BUCKET.get(), CCItems.GOLDEN_WATER_BUCKET.get(), CCItems.GOLDEN_LAVA_BUCKET.get(), CCItems.GOLDEN_MILK_BUCKET.get());
		this.tag(ItemTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get().asItem());

		this.tag(CCItemTags.AFFLICTION_ITEMS).add(CCItems.SILVER_SWORD.get(), CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_HOE.get());
		this.tag(CCItemTags.EXPERIENCE_BOOST_ITEMS).add(Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE);
		this.tag(CCItemTags.SILVER_GEAR).add(CCItems.SILVER_SWORD.get(), CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get());
		this.tag(CCItemTags.NECROMIUM_GEAR).add(CCItems.NECROMIUM_SWORD.get(), CCItems.NECROMIUM_PICKAXE.get(), CCItems.NECROMIUM_AXE.get(), CCItems.NECROMIUM_SHOVEL.get(), CCItems.NECROMIUM_HOE.get(), CCItems.NECROMIUM_HELMET.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.NECROMIUM_BOOTS.get(), CCItems.NECROMIUM_HORSE_ARMOR.get());
		this.tag(CCItemTags.IGNORE_RAIL_PLACEMENT).addOptional(new ResourceLocation("create", "cart_assembler"));

		this.tag(CCItemTags.BUCKETS_WATER).add(Items.WATER_BUCKET, CCItems.GOLDEN_WATER_BUCKET.get());
		this.tag(CCItemTags.BUCKETS_LAVA).add(Items.LAVA_BUCKET, CCItems.GOLDEN_LAVA_BUCKET.get());
		this.tag(CCItemTags.BUCKETS_MILK).add(Items.MILK_BUCKET, CCItems.GOLDEN_MILK_BUCKET.get());
		this.tag(CCItemTags.BUCKETS_POWDER_SNOW).add(Items.POWDER_SNOW_BUCKET, CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());

		this.tag(CCItemTags.INGOTS_COPPER).add(Items.COPPER_INGOT);
		this.tag(CCItemTags.INGOTS_SILVER).add(CCItems.SILVER_INGOT.get());
		this.tag(CCItemTags.INGOTS_NECROMIUM).add(CCItems.NECROMIUM_INGOT.get());
		this.tag(Tags.Items.INGOTS).addTag(CCItemTags.INGOTS_COPPER).addTag(CCItemTags.INGOTS_SILVER).addTag(CCItemTags.INGOTS_NECROMIUM);

		this.tag(CCItemTags.NUGGETS_SILVER).add(CCItems.SILVER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NECROMIUM).add(CCItems.NECROMIUM_NUGGET.get());
		this.tag(Tags.Items.NUGGETS).addTag(CCItemTags.NUGGETS_SILVER).addTag(CCItemTags.NUGGETS_NECROMIUM);

		this.tag(CCItemTags.RAW_FISHES).addTag(CCItemTags.RAW_FISHES_CAVEFISH);
		this.tag(CCItemTags.RAW_FISHES_CAVEFISH).add(CCItems.CAVEFISH.get());
		this.tag(CCItemTags.COOKED_FISHES).addTag(CCItemTags.COOKED_FISHES_CAVEFISH);
		this.tag(CCItemTags.COOKED_FISHES_CAVEFISH).add(CCItems.COOKED_CAVEFISH.get());
	}
}