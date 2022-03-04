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
		this.tag(ItemTags.ARROWS).add(CCItems.SILVER_ARROW.get());
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(CCItems.SILVER_INGOT.get(), CCItems.NECROMIUM_INGOT.get());
		this.tag(ItemTags.FISHES).add(CCItems.CAVEFISH.get(), CCItems.COOKED_CAVEFISH.get());
		this.tag(ItemTags.MUSIC_DISCS).add(CCItems.MUSIC_DISC_EPILOGUE.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(CCBlocks.LAVA_LAMP.get().asItem(), CCBlocks.GOLDEN_BARS.get().asItem(), CCItems.GOLDEN_BUCKET.get(), CCItems.GOLDEN_WATER_BUCKET.get(), CCItems.GOLDEN_LAVA_BUCKET.get(), CCItems.GOLDEN_MILK_BUCKET.get(), CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), CCItems.SPINEL_CROWN.get());
		this.tag(ItemTags.PIGLIN_REPELLENTS).add(CCBlocks.SOUL_BRAZIER.get().asItem());
		this.tag(ItemTags.BOATS).add(CCItems.AZALEA_BOAT.get());

		this.copy(CCBlockTags.SILVER_ORES, CCItemTags.SILVER_ORES);
		this.copy(CCBlockTags.SPINEL_ORES, CCItemTags.SPINEL_ORES);
		this.copy(CCBlockTags.CURSED_FIRE_BASE_BLOCKS, CCItemTags.CURSED_FIRE_BASE_BLOCKS);
		this.copy(CCBlockTags.AZALEA_LOGS, CCItemTags.AZALEA_LOGS);
		this.tag(CCItemTags.AFFLICTION_ITEMS).add(CCItems.SILVER_SWORD.get(), CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_HOE.get());
		this.tag(CCItemTags.EXPERIENCE_BOOST_ITEMS).add(Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE);
		this.tag(CCItemTags.SILVER_GEAR).add(CCItems.SILVER_SWORD.get(), CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get());
		this.tag(CCItemTags.NECROMIUM_GEAR).add(CCItems.NECROMIUM_SWORD.get(), CCItems.NECROMIUM_PICKAXE.get(), CCItems.NECROMIUM_AXE.get(), CCItems.NECROMIUM_SHOVEL.get(), CCItems.NECROMIUM_HOE.get(), CCItems.NECROMIUM_HELMET.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.NECROMIUM_BOOTS.get(), CCItems.NECROMIUM_HORSE_ARMOR.get());
		this.tag(CCItemTags.IGNORE_RAIL_PLACEMENT).addOptional(new ResourceLocation("create", "cart_assembler"));
		this.tag(CCItemTags.RAT_FOOD).add(Items.ROTTEN_FLESH);
		this.tag(CCItemTags.RAT_TAME_ITEMS).add(Items.ROTTEN_FLESH);

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
		this.tag(CCItemTags.NUGGETS_SILVER).add(CCItems.SILVER_NUGGET.get());
		this.tag(CCItemTags.NUGGETS_NECROMIUM).add(CCItems.NECROMIUM_NUGGET.get());
		this.tag(Tags.Items.NUGGETS).addTag(CCItemTags.NUGGETS_SILVER).addTag(CCItemTags.NUGGETS_NECROMIUM);
		this.tag(CCItemTags.RAW_FISHES).addTag(CCItemTags.RAW_FISHES_CAVEFISH);
		this.tag(CCItemTags.RAW_FISHES_CAVEFISH).add(CCItems.CAVEFISH.get());
		this.tag(CCItemTags.COOKED_FISHES).addTag(CCItemTags.COOKED_FISHES_CAVEFISH);
		this.tag(CCItemTags.COOKED_FISHES_CAVEFISH).add(CCItems.COOKED_CAVEFISH.get());
		this.tag(CCItemTags.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE, CCItems.SILVER_PICKAXE.get(), CCItems.NECROMIUM_PICKAXE.get());
		this.tag(CCItemTags.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE, CCItems.SILVER_AXE.get(), CCItems.NECROMIUM_AXE.get());
		this.tag(CCItemTags.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL, CCItems.SILVER_SHOVEL.get(), CCItems.NECROMIUM_SHOVEL.get());

		this.copy(BlueprintBlockTags.LADDERS, BlueprintItemTags.LADDERS);
		this.copy(BlueprintBlockTags.VERTICAL_SLABS, BlueprintItemTags.VERTICAL_SLABS);
		this.copy(BlueprintBlockTags.WOODEN_VERTICAL_SLABS, BlueprintItemTags.WOODEN_VERTICAL_SLABS);
		this.tag(BlueprintItemTags.BOATABLE_CHESTS).add(CCBlocks.AZALEA_CHEST.getFirst().get().asItem());
	}
}