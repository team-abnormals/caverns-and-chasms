package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
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
import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.NECROMIUM_BOOTS;

public class CCItemTagsProvider extends ItemTagsProvider {

	public CCItemTagsProvider(PackOutput output, CompletableFuture<Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, ExistingFileHelper helper) {
		super(output, provider, lookup, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
		this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
		this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
		this.copy(BlockTags.DIRT, ItemTags.DIRT);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(CCItems.SILVER_INGOT.get(), CCItems.NECROMIUM_INGOT.get());
		this.tag(ItemTags.MUSIC_DISCS).add(CCItems.MUSIC_DISC_EPILOGUE.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(CCBlocks.LAVA_LAMP.get().asItem(), CCBlocks.GOLDEN_BARS.get().asItem(), CCItems.GOLDEN_BUCKET.get(), CCItems.GOLDEN_WATER_BUCKET.get(), CCItems.GOLDEN_LAVA_BUCKET.get(), CCItems.GOLDEN_MILK_BUCKET.get(), CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
		this.tag(ItemTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get().asItem());
		this.tag(ItemTags.BOATS).add(CCItems.AZALEA_BOAT.getFirst().get());
		this.tag(ItemTags.CHEST_BOATS).add(CCItems.AZALEA_BOAT.getSecond().get());
		this.tag(BlueprintItemTags.FURNACE_BOATS).add(CCItems.AZALEA_FURNACE_BOAT.get());
		this.tag(BlueprintItemTags.LARGE_BOATS).add(CCItems.LARGE_AZALEA_BOAT.get());
		this.tag(ItemTags.ARROWS).add(CCItems.LARGE_ARROW.get(), CCItems.BLUNT_ARROW.get());
		this.tag(ItemTags.TRIMMABLE_ARMOR).add(SILVER_HELMET.get(), SILVER_CHESTPLATE.get(), SILVER_LEGGINGS.get(), SILVER_BOOTS.get(), NECROMIUM_HELMET.get(), NECROMIUM_CHESTPLATE.get(), NECROMIUM_LEGGINGS.get(), NECROMIUM_BOOTS.get(), SANGUINE_HELMET.get(), SANGUINE_CHESTPLATE.get(), SANGUINE_LEGGINGS.get(), SANGUINE_BOOTS.get());
		this.tag(ItemTags.TRIM_MATERIALS).add(SILVER_INGOT.get(), NECROMIUM_INGOT.get(), SPINEL.get(), LIVING_FLESH.get());

		this.copy(CCBlockTags.SILVER_ORES, CCItemTags.SILVER_ORES);
		this.copy(CCBlockTags.SPINEL_ORES, CCItemTags.SPINEL_ORES);
		this.copy(CCBlockTags.AZALEA_LOGS, CCItemTags.AZALEA_LOGS);
		this.tag(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).add(Blocks.COPPER_BLOCK.asItem(), Blocks.EXPOSED_COPPER.asItem(), Blocks.WEATHERED_COPPER.asItem(), Blocks.OXIDIZED_COPPER.asItem(), Blocks.WAXED_COPPER_BLOCK.asItem(), Blocks.WAXED_EXPOSED_COPPER.asItem(), Blocks.WAXED_WEATHERED_COPPER.asItem(), Blocks.WAXED_OXIDIZED_COPPER.asItem());
		this.tag(CCItemTags.MAGIC_DAMAGE_ITEMS).add(CCItems.SILVER_SWORD.get(), CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "silver_knife"));
		this.tag(CCItemTags.EXPERIENCE_BOOST_ITEMS).add(Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE).addOptional(new ResourceLocation("farmersdelight", "golden_knife"));
		this.tag(CCItemTags.SLOWNESS_INFLICTING_ITEMS).add(CCItems.NECROMIUM_SWORD.get(), CCItems.NECROMIUM_PICKAXE.get(), CCItems.NECROMIUM_AXE.get(), CCItems.NECROMIUM_SHOVEL.get(), CCItems.NECROMIUM_HOE.get()).addOptional(new ResourceLocation("abnormals_delight", "necromium_knife"));
		this.tag(CCItemTags.IGNORE_RAIL_PLACEMENT).addOptional(new ResourceLocation("create", "cart_assembler"));
		this.tag(CCItemTags.GLARE_FOOD).add(Items.GLOW_BERRIES);
		this.tag(CCItemTags.RAT_FOOD).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.RAT_TAME_ITEMS).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.ADDITIONAL_TOOLBOX_TOOLS).add(Items.SPYGLASS, Items.TOTEM_OF_UNDYING, CCItems.TUNING_FORK.get());

		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
		this.copy(CCBlockTags.ORES_AMETHYST, CCItemTags.ORES_AMETHYST);
		this.copy(CCBlockTags.ORES_SILVER, CCItemTags.ORES_SILVER);
		this.copy(CCBlockTags.ORES_SPINEL, CCItemTags.ORES_SPINEL);
		this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
		this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
		this.copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
		this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
		this.copy(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SILVER, CCItemTags.STORAGE_BLOCKS_SILVER);
		this.copy(CCBlockTags.STORAGE_BLOCKS_SPINEL, CCItemTags.STORAGE_BLOCKS_SPINEL);
		this.copy(CCBlockTags.STORAGE_BLOCKS_NECROMIUM, CCItemTags.STORAGE_BLOCKS_NECROMIUM);
		this.copy(CCBlockTags.STORAGE_BLOCKS_RAW_SILVER, CCItemTags.STORAGE_BLOCKS_RAW_SILVER);
		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
		this.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
		this.copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
		this.tag(BlueprintItemTags.BUCKETS_EMPTY).add(CCItems.GOLDEN_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_WATER).add(CCItems.GOLDEN_WATER_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_LAVA).add(CCItems.GOLDEN_LAVA_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_MILK).add(CCItems.GOLDEN_MILK_BUCKET.get());
		this.tag(BlueprintItemTags.BUCKETS_POWDER_SNOW).add(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
		this.tag(CCItemTags.INGOTS_SILVER).add(CCItems.SILVER_INGOT.get());
		this.tag(CCItemTags.INGOTS_NECROMIUM).add(CCItems.NECROMIUM_INGOT.get());
		this.tag(Tags.Items.INGOTS).addTag(CCItemTags.INGOTS_SILVER).addTag(CCItemTags.INGOTS_NECROMIUM);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(CCItemTags.RAW_MATERIALS_SILVER);
		this.tag(CCItemTags.RAW_MATERIALS_SILVER).add(CCItems.RAW_SILVER.get());
		this.tag(CCItemTags.NUGGETS_COPPER).add(CCItems.COPPER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NETHERITE).add(CCItems.NETHERITE_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_SILVER).add(CCItems.SILVER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NECROMIUM).add(CCItems.NECROMIUM_NUGGET.get());
		this.tag(Tags.Items.NUGGETS).addTag(CCItemTags.NUGGETS_SILVER).addTag(CCItemTags.NUGGETS_NECROMIUM);
		this.tag(Tags.Items.HEADS).add(CCItems.DEEPER_HEAD.get(), CCItems.PEEPER_HEAD.get(), CCItems.MIME_HEAD.get());
		this.copy(BlueprintBlockTags.NOTE_BLOCK_TOP_INSTRUMENTS, ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS);

		this.tag(ItemTags.SWORDS).add(CCItems.SILVER_SWORD.get(), CCItems.NECROMIUM_SWORD.get());
		this.tag(ItemTags.PICKAXES).add(CCItems.SILVER_PICKAXE.get(), CCItems.NECROMIUM_PICKAXE.get());
		this.tag(ItemTags.AXES).add(CCItems.SILVER_AXE.get(), CCItems.NECROMIUM_AXE.get());
		this.tag(ItemTags.SHOVELS).add(CCItems.SILVER_SHOVEL.get(), CCItems.NECROMIUM_SHOVEL.get());
		this.tag(ItemTags.HOES).add(CCItems.SILVER_HOE.get(), CCItems.NECROMIUM_HOE.get());
		this.tag(Tags.Items.ARMORS_HELMETS).add(CCItems.SILVER_HELMET.get(), CCItems.NECROMIUM_HELMET.get(), CCItems.SANGUINE_HELMET.get());
		this.tag(Tags.Items.ARMORS_CHESTPLATES).add(CCItems.SILVER_CHESTPLATE.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.SANGUINE_CHESTPLATE.get());
		this.tag(Tags.Items.ARMORS_LEGGINGS).add(CCItems.SILVER_LEGGINGS.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.SANGUINE_LEGGINGS.get());
		this.tag(Tags.Items.ARMORS_BOOTS).add(CCItems.SILVER_BOOTS.get(), CCItems.NECROMIUM_BOOTS.get(), CCItems.SANGUINE_BOOTS.get());

		this.tag(CCItemTags.BOTTLES_MILK);
		this.tag(CCItemTags.ENDER_FIRE_BASE_BLOCKS);
	}
}