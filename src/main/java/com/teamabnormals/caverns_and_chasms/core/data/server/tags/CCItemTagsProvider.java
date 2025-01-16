package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.data.server.tags.BlueprintItemTagsProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;

public class CCItemTagsProvider extends BlueprintItemTagsProvider {

	public CCItemTagsProvider(PackOutput output, CompletableFuture<Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, ExistingFileHelper helper) {
		super(CavernsAndChasms.MOD_ID, output, provider, lookup, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.copyWoodenTags();
		this.copyWoodworksTags();

		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.DIRT, ItemTags.DIRT);
		this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(SILVER_INGOT.get(), TIN_INGOT.get(), NECROMIUM_INGOT.get());
		this.tag(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_EPILOGUE.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(CCBlocks.LAVA_LAMP.get().asItem(), CCBlocks.GOLDEN_BARS.get().asItem(), GOLDEN_BUCKET.get(), GOLDEN_WATER_BUCKET.get(), GOLDEN_LAVA_BUCKET.get(), GOLDEN_MILK_BUCKET.get(), GOLDEN_POWDER_SNOW_BUCKET.get());
		this.tag(ItemTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get().asItem());
		this.tag(ItemTags.BOATS).add(AZALEA_BOAT.getFirst().get());
		this.tag(ItemTags.CHEST_BOATS).add(AZALEA_BOAT.getSecond().get());
		this.tag(BlueprintItemTags.FURNACE_BOATS).add(AZALEA_FURNACE_BOAT.get());
		this.tag(BlueprintItemTags.LARGE_BOATS).add(LARGE_AZALEA_BOAT.get());
		this.tag(ItemTags.ARROWS).add(LARGE_ARROW.get(), BLUNT_ARROW.get());
		this.tag(ItemTags.TRIMMABLE_ARMOR).add(SILVER_HELMET.get(), SILVER_CHESTPLATE.get(), SILVER_LEGGINGS.get(), SILVER_BOOTS.get(), NECROMIUM_HELMET.get(), NECROMIUM_CHESTPLATE.get(), NECROMIUM_LEGGINGS.get(), NECROMIUM_BOOTS.get(), SANGUINE_HELMET.get(), SANGUINE_CHESTPLATE.get(), SANGUINE_LEGGINGS.get(), SANGUINE_BOOTS.get());
		this.tag(ItemTags.TRIM_MATERIALS).add(SILVER_INGOT.get(), TIN_INGOT.get(), NECROMIUM_INGOT.get(), SPINEL.get(), ZIRCONIA.get(), LIVING_FLESH.get());
		this.tag(ItemTags.TRIM_TEMPLATES).add(EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get());

		this.copy(CCBlockTags.SILVER_ORES, CCItemTags.SILVER_ORES);
		this.copy(CCBlockTags.TIN_ORES, CCItemTags.TIN_ORES);
		this.copy(CCBlockTags.SPINEL_ORES, CCItemTags.SPINEL_ORES);
		this.copy(CCBlockTags.AZALEA_LOGS, CCItemTags.AZALEA_LOGS);
		this.tag(CCItemTags.CUPRIC_FIRE_BASE_ITEMS).addTag(Tags.Items.INGOTS_COPPER).addTag(Tags.Items.RAW_MATERIALS_COPPER);
		this.tag(CCItemTags.MAGIC_DAMAGE_ITEMS).add(SILVER_SWORD.get(), SILVER_PICKAXE.get(), SILVER_AXE.get(), SILVER_SHOVEL.get(), SILVER_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "silver_knife"));
		this.tag(CCItemTags.EXPERIENCE_BOOST_ITEMS).add(Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE).addOptional(new ResourceLocation("farmersdelight", "golden_knife"));
		this.tag(CCItemTags.SLOWNESS_INFLICTING_ITEMS).add(NECROMIUM_SWORD.get(), NECROMIUM_PICKAXE.get(), NECROMIUM_AXE.get(), NECROMIUM_SHOVEL.get(), NECROMIUM_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "necromium_knife"));
		this.tag(CCItemTags.IGNORE_RAIL_PLACEMENT).addOptional(new ResourceLocation("create", "cart_assembler"));
		this.tag(CCItemTags.GLARE_FOOD).add(Items.GLOW_BERRIES);
		this.tag(CCItemTags.RAT_FOOD).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.RAT_TAME_ITEMS).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.ADDITIONAL_TOOLBOX_TOOLS).add(Items.SPYGLASS, Items.TOTEM_OF_UNDYING, TUNING_FORK.get());
		this.tag(CCItemTags.DISMANTLING_FUELS).addTag(CCItemTags.GEMS_SPINEL);
		this.tag(CCItemTags.FADED_TRIM_MODIFIERS).addTag(CCItemTags.GEMS_SPINEL);
		this.tag(CCItemTags.EMISSIVE_TRIM_MODIFIERS).add(Items.BLAZE_POWDER);

		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
		this.copy(CCBlockTags.ORES_AMETHYST, CCItemTags.ORES_AMETHYST);
		this.copy(CCBlockTags.ORES_SILVER, CCItemTags.ORES_SILVER);
		this.copy(CCBlockTags.ORES_TIN, CCItemTags.ORES_TIN);
		this.copy(CCBlockTags.ORES_SPINEL, CCItemTags.ORES_SPINEL);
		this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
		this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
		this.copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
		this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
		this.copy(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SILVER, CCItemTags.STORAGE_BLOCKS_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_TIN, CCItemTags.STORAGE_BLOCKS_TIN);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SPINEL, CCItemTags.STORAGE_BLOCKS_SPINEL);
		this.copy(CCBlockTags.STORAGE_BLOCKS_ZIRCONIA, CCItemTags.STORAGE_BLOCKS_ZIRCONIA);
		this.copy(CCBlockTags.STORAGE_BLOCKS_NECROMIUM, CCItemTags.STORAGE_BLOCKS_NECROMIUM);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER, CCItemTags.STORAGE_BLOCKS_RAW_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_TIN, CCItemTags.STORAGE_BLOCKS_RAW_TIN);
		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
		this.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
		this.copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
		this.copy(Tags.Blocks.GLASS, Tags.Items.GLASS);
		this.tag(BlueprintItemTags.BUCKETS_EMPTY).add(GOLDEN_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_WATER).add(GOLDEN_WATER_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_LAVA).add(GOLDEN_LAVA_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_MILK).add(GOLDEN_MILK_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_POWDER_SNOW).add(GOLDEN_POWDER_SNOW_BUCKET.get());
		this.tag(CCItemTags.GEMS_SPINEL).add(SPINEL.get());
		this.tag(CCItemTags.GEMS_ZIRCONIA).add(ZIRCONIA.get());
		this.tag(CCItemTags.INGOTS_SILVER).add(SILVER_INGOT.get());
		this.tag(CCItemTags.INGOTS_TIN).add(TIN_INGOT.get());
		this.tag(CCItemTags.INGOTS_NECROMIUM).add(NECROMIUM_INGOT.get());
		this.tag(Tags.Items.INGOTS).addTag(CCItemTags.INGOTS_SILVER).addTag(CCItemTags.INGOTS_TIN).addTag(CCItemTags.INGOTS_NECROMIUM);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(CCItemTags.RAW_MATERIALS_SILVER).addTag(CCItemTags.RAW_MATERIALS_TIN);
		this.tag(CCItemTags.RAW_MATERIALS_SILVER).add(RAW_SILVER.get());
		this.tag(CCItemTags.RAW_MATERIALS_TIN).add(RAW_TIN.get());
		this.tag(CCItemTags.NUGGETS_COPPER).add(COPPER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NETHERITE).add(NETHERITE_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_SILVER).add(SILVER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_TIN).add(TIN_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NECROMIUM).add(NECROMIUM_NUGGET.get());
		this.tag(Tags.Items.NUGGETS).addTag(CCItemTags.NUGGETS_SILVER).addTag(CCItemTags.NUGGETS_TIN).addTag(CCItemTags.NUGGETS_COPPER).addTag(CCItemTags.NUGGETS_NETHERITE).addTag(CCItemTags.NUGGETS_NECROMIUM);
		this.tag(Tags.Items.HEADS).add(DEEPER_HEAD.get(), PEEPER_HEAD.get(), MIME_HEAD.get());
		this.copy(BlueprintBlockTags.NOTE_BLOCK_TOP_INSTRUMENTS, ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS);

		this.tag(ItemTags.SWORDS).add(SILVER_SWORD.get(), NECROMIUM_SWORD.get());
		this.tag(ItemTags.PICKAXES).add(SILVER_PICKAXE.get(), NECROMIUM_PICKAXE.get());
		this.tag(ItemTags.AXES).add(SILVER_AXE.get(), NECROMIUM_AXE.get());
		this.tag(ItemTags.SHOVELS).add(SILVER_SHOVEL.get(), NECROMIUM_SHOVEL.get());
		this.tag(ItemTags.HOES).add(SILVER_HOE.get(), NECROMIUM_HOE.get());
		this.tag(Tags.Items.ARMORS_HELMETS).add(SILVER_HELMET.get(), NECROMIUM_HELMET.get(), SANGUINE_HELMET.get());
		this.tag(Tags.Items.ARMORS_CHESTPLATES).add(SILVER_CHESTPLATE.get(), NECROMIUM_CHESTPLATE.get(), SANGUINE_CHESTPLATE.get());
		this.tag(Tags.Items.ARMORS_LEGGINGS).add(SILVER_LEGGINGS.get(), NECROMIUM_LEGGINGS.get(), SANGUINE_LEGGINGS.get());
		this.tag(Tags.Items.ARMORS_BOOTS).add(SILVER_BOOTS.get(), NECROMIUM_BOOTS.get(), SANGUINE_BOOTS.get());

		this.tag(CCItemTags.BOTTLES_MILK);
		this.tag(CCItemTags.ENDER_FIRE_BASE_BLOCKS);
	}
}