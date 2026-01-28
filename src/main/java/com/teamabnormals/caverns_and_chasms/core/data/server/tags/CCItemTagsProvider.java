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
		this.copy(BlockTags.DOORS, ItemTags.DOORS);
		this.copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
		this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(SILVER_INGOT.get(), TIN_INGOT.get(), NECROMIUM_INGOT.get(), TURQUOISE.get(), ZIRCONIA.get());
		this.tag(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_EPILOGUE.get(), MUSIC_DISC_COPY.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(CCBlocks.LAVA_LAMP.get().asItem(), CCBlocks.GOLDEN_BARS.get().asItem(), GOLDEN_BUCKET.get(), GOLDEN_WATER_BUCKET.get(), GOLDEN_LAVA_BUCKET.get(), GOLDEN_MILK_BUCKET.get(), GOLDEN_POWDER_SNOW_BUCKET.get(), CCBlocks.GOLD_BRICKS.get().asItem(), CCBlocks.GOLD_BRICK_STAIRS.get().asItem(), CCBlocks.GOLD_BRICK_SLAB.get().asItem(), CCBlocks.GOLD_BRICK_WALL.get().asItem(), CCBlocks.CHISELED_GOLD_BRICKS.get().asItem());
		this.tag(ItemTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get().asItem());
		this.tag(ItemTags.BOATS).add(AZALEA_BOAT.getFirst().get());
		this.tag(ItemTags.CHEST_BOATS).add(AZALEA_BOAT.getSecond().get());
		this.tag(BlueprintItemTags.FURNACE_BOATS).add(AZALEA_FURNACE_BOAT.get());
		this.tag(BlueprintItemTags.LARGE_BOATS).add(LARGE_AZALEA_BOAT.get());
		this.tag(ItemTags.ARROWS).add(LARGE_ARROW.get(), BLUNT_ARROW.get());
		this.tag(ItemTags.TRIMMABLE_ARMOR).addTag(CCItemTags.COPPER_HELMETS).addTag(CCItemTags.COPPER_CHESTPLATES).addTag(CCItemTags.COPPER_LEGGINGS).addTag(CCItemTags.COPPER_BOOTS).add(
				SILVER_HELMET.get(), SILVER_CHESTPLATE.get(), SILVER_LEGGINGS.get(), SILVER_BOOTS.get(),
				NECROMIUM_HELMET.get(), NECROMIUM_CHESTPLATE.get(), NECROMIUM_LEGGINGS.get(), NECROMIUM_BOOTS.get(),
				SANGUINE_HELMET.get(), SANGUINE_CHESTPLATE.get(), SANGUINE_LEGGINGS.get(), SANGUINE_BOOTS.get()
		);
		this.tag(ItemTags.TRIM_MATERIALS).add(SILVER_INGOT.get(), TIN_INGOT.get(), NECROMIUM_INGOT.get(), SPINEL.get(), ZIRCONIA.get(), TURQUOISE.get(), LIVING_FLESH.get(),
				EXPOSED_COPPER_INGOT.get(), WEATHERED_COPPER_INGOT.get(), OXIDIZED_COPPER_INGOT.get(), WAXED_COPPER_INGOT.get(), WAXED_EXPOSED_COPPER_INGOT.get(), WAXED_WEATHERED_COPPER_INGOT.get(), WAXED_OXIDIZED_COPPER_INGOT.get());
		this.tag(ItemTags.TRIM_TEMPLATES).add(EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), FORGER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), RIM_ARMOR_TRIM_SMITHING_TEMPLATE.get(), PLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CORE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
		this.tag(ItemTags.DECORATED_POT_SHERDS).add(BOOM_POTTERY_SHERD.get(), CAST_POTTERY_SHERD.get(), RIDE_POTTERY_SHERD.get(), STALKER_POTTERY_SHERD.get());

		this.copy(CCBlockTags.SILVER_ORES, CCItemTags.SILVER_ORES);
		this.copy(CCBlockTags.TIN_ORES, CCItemTags.TIN_ORES);
		this.copy(CCBlockTags.SPINEL_ORES, CCItemTags.SPINEL_ORES);
		this.copy(CCBlockTags.TURQUOISE_ORES, CCItemTags.TURQUOISE_ORES);
		this.copy(CCBlockTags.AZALEA_LOGS, CCItemTags.AZALEA_LOGS);
		this.tag(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).addTag(Tags.Items.INGOTS_COPPER).addTag(CCItemTags.INGOTS_EXPOSED_COPPER).addTag(CCItemTags.INGOTS_WEATHERED_COPPER).addTag(CCItemTags.INGOTS_OXIDIZED_COPPER).addTag(Tags.Items.RAW_MATERIALS_COPPER);
		this.tag(CCItemTags.MAGIC_DAMAGE_ITEMS).add(SILVER_SWORD.get(), SILVER_PICKAXE.get(), SILVER_AXE.get(), SILVER_SHOVEL.get(), SILVER_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "silver_knife"));
		this.tag(CCItemTags.EXPERIENCE_BOOST_ITEMS).add(Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE).addOptional(new ResourceLocation("farmersdelight", "golden_knife"));
		this.tag(CCItemTags.SLOWNESS_INFLICTING_ITEMS).add(NECROMIUM_SWORD.get(), NECROMIUM_PICKAXE.get(), NECROMIUM_AXE.get(), NECROMIUM_SHOVEL.get(), NECROMIUM_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "necromium_knife"));
		this.tag(CCItemTags.IGNORE_RAIL_PLACEMENT).addOptional(new ResourceLocation("create", "cart_assembler"));
		this.tag(CCItemTags.GLARE_FOOD).add(Items.GLOW_BERRIES);
		this.tag(CCItemTags.RAT_FOOD).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.RAT_TAME_ITEMS).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.ADDITIONAL_TOOLBOX_TOOLS).add(Items.SPYGLASS, Items.TOTEM_OF_UNDYING, TUNING_FORK.get());
		this.tag(CCItemTags.DISMANTLING_FUELS).addTag(CCItemTags.GEMS_SPINEL);
		this.tag(CCItemTags.ATONING_FUELS).addTag(CCItemTags.GEMS_SPINEL);
		this.tag(CCItemTags.FADED_TRIM_MODIFIERS).addTag(CCItemTags.GEMS_SPINEL);
		this.tag(CCItemTags.EMISSIVE_TRIM_MODIFIERS).add(Items.BLAZE_POWDER);
		this.tag(CCItemTags.UNREPAIRABLE_BY_ZIRCONIA);
		this.tag(CCItemTags.CHANGES_HOOP_SIZE).add(TUNING_FORK.get());
		this.tag(CCItemTags.PLACEABLE_ITEMS).add(
				Items.COAL, Items.CHARCOAL, Items.BRICK, Items.NETHER_BRICK,
				Items.COPPER_INGOT, EXPOSED_COPPER_INGOT.get(), WEATHERED_COPPER_INGOT.get(), OXIDIZED_COPPER_INGOT.get(),
				WAXED_COPPER_INGOT.get(), WAXED_EXPOSED_COPPER_INGOT.get(), WAXED_WEATHERED_COPPER_INGOT.get(), WAXED_OXIDIZED_COPPER_INGOT.get(),
				Items.IRON_INGOT, Items.GOLD_INGOT, Items.NETHERITE_INGOT, SILVER_INGOT.get(), TIN_INGOT.get(), NECROMIUM_INGOT.get()
		).addOptional(new ResourceLocation("endergetic", "eumus_brick"));

		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
		this.copy(CCBlockTags.ORES_AMETHYST, CCItemTags.ORES_AMETHYST);
		this.copy(CCBlockTags.ORES_SILVER, CCItemTags.ORES_SILVER);
		this.copy(CCBlockTags.ORES_TIN, CCItemTags.ORES_TIN);
		this.copy(CCBlockTags.ORES_SPINEL, CCItemTags.ORES_SPINEL);
		this.copy(CCBlockTags.ORES_TURQUOISE, CCItemTags.ORES_TURQUOISE);
		this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
		this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
		this.copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
		this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
		this.copy(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SILVER, CCItemTags.STORAGE_BLOCKS_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_TIN, CCItemTags.STORAGE_BLOCKS_TIN);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SPINEL, CCItemTags.STORAGE_BLOCKS_SPINEL);
		this.copy(CCBlockTags.STORAGE_BLOCKS_TURQUOISE, CCItemTags.STORAGE_BLOCKS_TURQUOISE);
		this.copy(CCBlockTags.STORAGE_BLOCKS_ZIRCONIA, CCItemTags.STORAGE_BLOCKS_ZIRCONIA);
		this.copy(CCBlockTags.STORAGE_BLOCKS_NECROMIUM, CCItemTags.STORAGE_BLOCKS_NECROMIUM);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER, CCItemTags.STORAGE_BLOCKS_RAW_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_TIN, CCItemTags.STORAGE_BLOCKS_RAW_TIN);
		this.copy(CCBlockTags.STORAGE_BLOCKS_CHARCOAL, CCItemTags.STORAGE_BLOCKS_CHARCOAL);
		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
		this.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
		this.copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
		this.copy(Tags.Blocks.GLASS, Tags.Items.GLASS);
		this.copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		this.tag(BlueprintItemTags.BUCKETS_EMPTY).add(GOLDEN_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_WATER).add(GOLDEN_WATER_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_LAVA).add(GOLDEN_LAVA_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_MILK).add(GOLDEN_MILK_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_POWDER_SNOW).add(GOLDEN_POWDER_SNOW_BUCKET.get());
		this.tag(CCItemTags.GEMS_SPINEL).add(SPINEL.get());
		this.tag(CCItemTags.GEMS_TURQUOISE).add(TURQUOISE.get());
		this.tag(CCItemTags.GEMS_ZIRCONIA).add(ZIRCONIA.get());
		this.tag(Tags.Items.GEMS).addTag(CCItemTags.GEMS_SPINEL).addTag(CCItemTags.GEMS_TURQUOISE).addTag(CCItemTags.GEMS_ZIRCONIA);
		this.tag(Tags.Items.INGOTS_COPPER).add(WAXED_COPPER_INGOT.get());
		this.tag(CCItemTags.INGOTS_EXPOSED_COPPER).add(EXPOSED_COPPER_INGOT.get(), WAXED_EXPOSED_COPPER_INGOT.get());
		this.tag(CCItemTags.INGOTS_WEATHERED_COPPER).add(WEATHERED_COPPER_INGOT.get(), WAXED_WEATHERED_COPPER_INGOT.get());
		this.tag(CCItemTags.INGOTS_OXIDIZED_COPPER).add(OXIDIZED_COPPER_INGOT.get(), WAXED_OXIDIZED_COPPER_INGOT.get());
		this.tag(CCItemTags.INGOTS_SILVER).add(SILVER_INGOT.get());
		this.tag(CCItemTags.INGOTS_TIN).add(TIN_INGOT.get());
		this.tag(CCItemTags.INGOTS_NECROMIUM).add(NECROMIUM_INGOT.get());
		this.tag(Tags.Items.INGOTS).addTag(CCItemTags.INGOTS_EXPOSED_COPPER).addTag(CCItemTags.INGOTS_WEATHERED_COPPER).addTag(CCItemTags.INGOTS_EXPOSED_COPPER).addTag(CCItemTags.INGOTS_SILVER).addTag(CCItemTags.INGOTS_TIN).addTag(CCItemTags.INGOTS_NECROMIUM);
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

		this.tag(CCItemTags.COPPER_SWORDS).add(COPPER_SWORD.get(), EXPOSED_COPPER_SWORD.get(), WEATHERED_COPPER_SWORD.get(), OXIDIZED_COPPER_SWORD.get(), WAXED_COPPER_SWORD.get(), WAXED_EXPOSED_COPPER_SWORD.get(), WAXED_WEATHERED_COPPER_SWORD.get(), WAXED_OXIDIZED_COPPER_SWORD.get());
		this.tag(CCItemTags.COPPER_PICKAXES).add(COPPER_PICKAXE.get(), EXPOSED_COPPER_PICKAXE.get(), WEATHERED_COPPER_PICKAXE.get(), OXIDIZED_COPPER_PICKAXE.get(), WAXED_COPPER_PICKAXE.get(), WAXED_EXPOSED_COPPER_PICKAXE.get(), WAXED_WEATHERED_COPPER_PICKAXE.get(), WAXED_OXIDIZED_COPPER_PICKAXE.get());
		this.tag(CCItemTags.COPPER_AXES).add(COPPER_AXE.get(), EXPOSED_COPPER_AXE.get(), WEATHERED_COPPER_AXE.get(), OXIDIZED_COPPER_AXE.get(), WAXED_COPPER_AXE.get(), WAXED_EXPOSED_COPPER_AXE.get(), WAXED_WEATHERED_COPPER_AXE.get(), WAXED_OXIDIZED_COPPER_AXE.get());
		this.tag(CCItemTags.COPPER_HOES).add(COPPER_HOE.get(), EXPOSED_COPPER_HOE.get(), WEATHERED_COPPER_HOE.get(), OXIDIZED_COPPER_HOE.get(), WAXED_COPPER_HOE.get(), WAXED_EXPOSED_COPPER_HOE.get(), WAXED_WEATHERED_COPPER_HOE.get(), WAXED_OXIDIZED_COPPER_HOE.get());
		this.tag(CCItemTags.COPPER_SHOVELS).add(COPPER_SHOVEL.get(), EXPOSED_COPPER_SHOVEL.get(), WEATHERED_COPPER_SHOVEL.get(), OXIDIZED_COPPER_SHOVEL.get(), WAXED_COPPER_SHOVEL.get(), WAXED_EXPOSED_COPPER_SHOVEL.get(), WAXED_WEATHERED_COPPER_SHOVEL.get(), WAXED_OXIDIZED_COPPER_SHOVEL.get());

		this.tag(CCItemTags.COPPER_HELMETS).add(COPPER_HELMET.get(), EXPOSED_COPPER_HELMET.get(), WEATHERED_COPPER_HELMET.get(), OXIDIZED_COPPER_HELMET.get(), WAXED_COPPER_HELMET.get(), WAXED_EXPOSED_COPPER_HELMET.get(), WAXED_WEATHERED_COPPER_HELMET.get(), WAXED_OXIDIZED_COPPER_HELMET.get());
		this.tag(CCItemTags.COPPER_CHESTPLATES).add(COPPER_CHESTPLATE.get(), EXPOSED_COPPER_CHESTPLATE.get(), WEATHERED_COPPER_CHESTPLATE.get(), OXIDIZED_COPPER_CHESTPLATE.get(), WAXED_COPPER_CHESTPLATE.get(), WAXED_EXPOSED_COPPER_CHESTPLATE.get(), WAXED_WEATHERED_COPPER_CHESTPLATE.get(), WAXED_OXIDIZED_COPPER_CHESTPLATE.get());
		this.tag(CCItemTags.COPPER_LEGGINGS).add(COPPER_LEGGINGS.get(), EXPOSED_COPPER_LEGGINGS.get(), WEATHERED_COPPER_LEGGINGS.get(), OXIDIZED_COPPER_LEGGINGS.get(), WAXED_COPPER_LEGGINGS.get(), WAXED_EXPOSED_COPPER_LEGGINGS.get(), WAXED_WEATHERED_COPPER_LEGGINGS.get(), WAXED_OXIDIZED_COPPER_LEGGINGS.get());
		this.tag(CCItemTags.COPPER_BOOTS).add(COPPER_BOOTS.get(), EXPOSED_COPPER_BOOTS.get(), WEATHERED_COPPER_BOOTS.get(), OXIDIZED_COPPER_BOOTS.get(), WAXED_COPPER_BOOTS.get(), WAXED_EXPOSED_COPPER_BOOTS.get(), WAXED_WEATHERED_COPPER_BOOTS.get(), WAXED_OXIDIZED_COPPER_BOOTS.get());

		this.tag(ItemTags.SWORDS).addTag(CCItemTags.COPPER_SWORDS).add(SILVER_SWORD.get(), NECROMIUM_SWORD.get());
		this.tag(ItemTags.PICKAXES).addTag(CCItemTags.COPPER_PICKAXES).add(SILVER_PICKAXE.get(), NECROMIUM_PICKAXE.get());
		this.tag(ItemTags.AXES).addTag(CCItemTags.COPPER_AXES).add(SILVER_AXE.get(), NECROMIUM_AXE.get());
		this.tag(ItemTags.HOES).addTag(CCItemTags.COPPER_HOES).add(SILVER_HOE.get(), NECROMIUM_HOE.get());
		this.tag(ItemTags.SHOVELS).addTag(CCItemTags.COPPER_SHOVELS).add(SILVER_SHOVEL.get(), NECROMIUM_SHOVEL.get());

		this.tag(Tags.Items.ARMORS_HELMETS).addTag(CCItemTags.COPPER_HELMETS).add(SILVER_HELMET.get(), NECROMIUM_HELMET.get(), COWL.get());
		this.tag(Tags.Items.ARMORS_CHESTPLATES).addTag(CCItemTags.COPPER_CHESTPLATES).add(SILVER_CHESTPLATE.get(), NECROMIUM_CHESTPLATE.get());
		this.tag(Tags.Items.ARMORS_LEGGINGS).addTag(CCItemTags.COPPER_LEGGINGS).add(SILVER_LEGGINGS.get(), NECROMIUM_LEGGINGS.get());
		this.tag(Tags.Items.ARMORS_BOOTS).addTag(CCItemTags.COPPER_BOOTS).add(SILVER_BOOTS.get(), NECROMIUM_BOOTS.get());

		this.tag(CCItemTags.BOTTLES_MILK);
		this.tag(CCItemTags.ENDER_FIRE_BASE_BLOCKS);
	}
}