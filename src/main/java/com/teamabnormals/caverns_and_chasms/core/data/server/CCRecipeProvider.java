package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.boatload.core.data.server.BoatloadRecipeProvider;
import com.teamabnormals.caverns_and_chasms.common.block.FloodlightBlock;
import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.common.recipe.MusicDiscCopying;
import com.teamabnormals.caverns_and_chasms.common.recipe.NBTWaxing;
import com.teamabnormals.caverns_and_chasms.common.recipe.SmithingModifierRecipeBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCInstruments;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.integration.boatload.CCBoatTypes;
import com.teamabnormals.clayworks.core.data.server.ClayworksRecipeProvider;
import com.teamabnormals.woodworks.core.data.server.WoodworksRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.other.CCBlockFamilies.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static net.minecraft.data.recipes.RecipeCategory.*;

public class CCRecipeProvider extends BlueprintRecipeProvider {
	public static final ModLoadedCondition ENDERGETIC_LOADED = new ModLoadedCondition("endergetic");

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(SILVER_ORE, DEEPSLATE_SILVER_ORE, SOUL_SILVER_ORE, CCItems.RAW_SILVER);
	private static final ImmutableList<ItemLike> TIN_SMELTABLES = ImmutableList.of(TIN_ORE, DEEPSLATE_TIN_ORE, CYLINDRITE_TIN_ORE, CASSITERITE_TIN_ORE, CCItems.RAW_TIN, SADDLED_EGG);
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(SPINEL_ORE, DEEPSLATE_SPINEL_ORE);
	private static final ImmutableList<ItemLike> TURQUOISE_SMELTABLES = ImmutableList.of(TURQUOISE_ORE, DEEPSLATE_TURQUOISE_ORE);

	public CCRecipeProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, output, provider);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer, Provider provider) {
		ShapedRecipeBuilder.shaped(TOOLS, Items.BUNDLE).define('R', Items.LEATHER).define('S', Items.STRING).pattern("S").pattern("R").unlockedBy("has_leather", has(Items.LEATHER)).save(consumer, CavernsAndChasms.location(getItemName(Items.BUNDLE)));
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.LODESTONE).define('S', Items.CHISELED_STONE_BRICKS).define('#', Tags.Items.INGOTS_IRON).pattern("SSS").pattern("S#S").pattern("SSS").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(consumer);

		ShapedRecipeBuilder.shaped(TRANSPORTATION, COPPER_RAIL, 3).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_COPPER).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, COPPER_CHAIN).define('#', CCItemTags.NUGGETS_COPPER).pattern("#").pattern("#").pattern("#").unlockedBy("has_copper_nugget", has(CCItemTags.NUGGETS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, COPPER_LANTERN).define('#', Items.TORCH).define('X', CCItemTags.NUGGETS_COPPER).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper_nugget", has(CCItemTags.NUGGETS_COPPER)).unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

		copperHornRecipe(consumer, provider, Instruments.PONDER_GOAT_HORN, CCInstruments.GREAT_SKY_FALLING_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.SING_GOAT_HORN, CCInstruments.OLD_HYMN_RESTING_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.SEEK_GOAT_HORN, CCInstruments.PURE_WATER_DESIRE_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.FEEL_GOAT_HORN, CCInstruments.HUMBLE_FIRE_MEMORY_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.ADMIRE_GOAT_HORN, CCInstruments.DRY_URGE_ANGER_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.CALL_GOAT_HORN, CCInstruments.CLEAR_TEMPER_JOURNEY_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.YEARN_GOAT_HORN, CCInstruments.FRESH_NEST_THOUGHT_COPPER_HORN);
		copperHornRecipe(consumer, provider, Instruments.DREAM_GOAT_HORN, CCInstruments.SECRET_LAKE_TEAR_COPPER_HORN);
		copperHornRecipe(consumer, provider, CCInstruments.FLY_GOAT_HORN.getKey(), CCInstruments.FEARLESS_RIVER_GIFT_COPPER_HORN);
		copperHornRecipe(consumer, provider, CCInstruments.RESIST_GOAT_HORN.getKey(), CCInstruments.SWEET_MOON_LOVE_COPPER_HORN);

		ShapelessRecipeBuilder.shapeless(MISC, Items.BONE_MEAL, 3).requires(CCItems.BONE_FLUTE).group("bonemeal").unlockedBy("has_bone_flute", has(CCItems.BONE_FLUTE)).save(consumer, getModConversionRecipeName(Items.BONE_MEAL, CCItems.BONE_FLUTE));

		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.RAIL, 3).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.ACTIVATOR_RAIL).define('#', Blocks.REDSTONE_TORCH).define('S', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).pattern("XSX").pattern("X#X").pattern("XSX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.DETECTOR_RAIL).define('R', Items.REDSTONE).define('#', Blocks.STONE_PRESSURE_PLATE).define('X', Tags.Items.NUGGETS_IRON).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.POWERED_RAIL).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_GOLD).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, SPIKED_RAIL).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_SILVER).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, SLAUGHTER_RAIL).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_SILVER).define('Y', CCItemTags.INGOTS_SILVER).pattern("XYX").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, HALT_RAIL).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).define('Y', Tags.Items.INGOTS_IRON).pattern("XYX").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.CHAIN).define('#', Tags.Items.NUGGETS_IRON).pattern("#").pattern("#").pattern("#").unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FRAGILE_STONE, 4).define('D', Blocks.STONE).define('G', Blocks.GRAVEL).pattern("DG").pattern("GD").unlockedBy("has_stone", has(Blocks.STONE)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FRAGILE_DEEPSLATE, 4).define('D', Blocks.DEEPSLATE).define('G', Blocks.GRAVEL).pattern("DG").pattern("GD").unlockedBy("has_deepslate", has(Blocks.DEEPSLATE)).save(consumer);

		ShapedRecipeBuilder.shaped(MISC, CCItems.BEJEWELED_PEARL, 2).define('P', Items.ENDER_PEARL).define('S', CCItemTags.GEMS_SPINEL).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(FOOD, CCItems.BEJEWELED_APPLE, 2).define('A', Items.GOLDEN_APPLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, TMT, 4).define('T', Items.TNT).define('S', CCItemTags.GEMS_SPINEL).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, SPLURTER).define('T', CCItems.TIN_INGOT).define('D', Items.DROPPER).pattern("TTT").pattern("TDT").pattern("TTT").unlockedBy("has_tin_ingot", has(CCItems.TIN_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, SCATTERER).define('T', CCItems.TIN_INGOT).define('D', Items.DISPENSER).pattern("TTT").pattern("TDT").pattern("TTT").unlockedBy("has_tin_ingot", has(CCItems.TIN_INGOT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(TRANSPORTATION, CCItems.TMT_MINECART).requires(TMT).requires(Items.MINECART).unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapelessRecipeBuilder.shapeless(COMBAT, CCItems.BLUNT_ARROW, 4).requires(Items.ARROW).requires(CCItemTags.GEMS_SPINEL).unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.LARGE_ARROW, 4).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).define('Y', Items.FEATHER).pattern("X").pattern("#").pattern("Y").unlockedBy("has_feather", has(Items.FEATHER)).unlockedBy("has_silver", has(CCItemTags.INGOTS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, DISMANTLING_TABLE).define('T', Items.SMITHING_TABLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_smithing_table", has(Items.SMITHING_TABLE)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, BEJEWELED_ANVIL, 2).define('T', Items.ANVIL).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_anvil", has(Items.ANVIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, ATONING_TABLE).define('T', Items.ENCHANTING_TABLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_enchanting_table", has(Items.ENCHANTING_TABLE)).save(consumer);

		ShapedRecipeBuilder.shaped(MISC, CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE).define('#', CCItemTags.GEMS_TURQUOISE).define('X', ItemTags.TRIM_TEMPLATES).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer, RecipeBuilder.getDefaultRecipeId(CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE).withSuffix("_from_smithing_template"));
		ShapedRecipeBuilder.shaped(FOOD, CCItems.CAVIAR).define('#', CCItemTags.GEMS_TURQUOISE).define('X', Items.SALMON_BUCKET).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.MONOCLE).define('#', CCItemTags.GEMS_TURQUOISE).define('X', Items.SPYGLASS).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.UNICORN_HORN).define('#', CCItemTags.GEMS_TURQUOISE).define('X', Items.END_ROD).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer);

		conversionRecipe(consumer, Items.CYAN_DYE, CCItems.TURQUOISE, "cyan_dye", 16);

		copperGearRecipes(consumer, Items.COPPER_INGOT, Blocks.COPPER_BLOCK, CCItems.COPPER_HELMET, CCItems.COPPER_CHESTPLATE, CCItems.COPPER_LEGGINGS, CCItems.COPPER_BOOTS, CCItems.COPPER_SWORD, CCItems.COPPER_PICKAXE, CCItems.COPPER_AXE, CCItems.COPPER_SHOVEL, CCItems.COPPER_HOE);
		copperGearRecipes(consumer, CCItems.EXPOSED_COPPER_INGOT, Blocks.EXPOSED_COPPER, CCItems.EXPOSED_COPPER_HELMET, CCItems.EXPOSED_COPPER_CHESTPLATE, CCItems.EXPOSED_COPPER_LEGGINGS, CCItems.EXPOSED_COPPER_BOOTS, CCItems.EXPOSED_COPPER_SWORD, CCItems.EXPOSED_COPPER_PICKAXE, CCItems.EXPOSED_COPPER_AXE, CCItems.EXPOSED_COPPER_SHOVEL, CCItems.EXPOSED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.WEATHERED_COPPER_INGOT, Blocks.WEATHERED_COPPER, CCItems.WEATHERED_COPPER_HELMET, CCItems.WEATHERED_COPPER_CHESTPLATE, CCItems.WEATHERED_COPPER_LEGGINGS, CCItems.WEATHERED_COPPER_BOOTS, CCItems.WEATHERED_COPPER_SWORD, CCItems.WEATHERED_COPPER_PICKAXE, CCItems.WEATHERED_COPPER_AXE, CCItems.WEATHERED_COPPER_SHOVEL, CCItems.WEATHERED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.OXIDIZED_COPPER_INGOT, Blocks.OXIDIZED_COPPER, CCItems.OXIDIZED_COPPER_HELMET, CCItems.OXIDIZED_COPPER_CHESTPLATE, CCItems.OXIDIZED_COPPER_LEGGINGS, CCItems.OXIDIZED_COPPER_BOOTS, CCItems.OXIDIZED_COPPER_SWORD, CCItems.OXIDIZED_COPPER_PICKAXE, CCItems.OXIDIZED_COPPER_AXE, CCItems.OXIDIZED_COPPER_SHOVEL, CCItems.OXIDIZED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.WAXED_COPPER_INGOT, Blocks.WAXED_COPPER_BLOCK, CCItems.WAXED_COPPER_HELMET, CCItems.WAXED_COPPER_CHESTPLATE, CCItems.WAXED_COPPER_LEGGINGS, CCItems.WAXED_COPPER_BOOTS, CCItems.WAXED_COPPER_SWORD, CCItems.WAXED_COPPER_PICKAXE, CCItems.WAXED_COPPER_AXE, CCItems.WAXED_COPPER_SHOVEL, CCItems.WAXED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.WAXED_EXPOSED_COPPER_INGOT, Blocks.WAXED_EXPOSED_COPPER, CCItems.WAXED_EXPOSED_COPPER_HELMET, CCItems.WAXED_EXPOSED_COPPER_CHESTPLATE, CCItems.WAXED_EXPOSED_COPPER_LEGGINGS, CCItems.WAXED_EXPOSED_COPPER_BOOTS, CCItems.WAXED_EXPOSED_COPPER_SWORD, CCItems.WAXED_EXPOSED_COPPER_PICKAXE, CCItems.WAXED_EXPOSED_COPPER_AXE, CCItems.WAXED_EXPOSED_COPPER_SHOVEL, CCItems.WAXED_EXPOSED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.WAXED_WEATHERED_COPPER_INGOT, Blocks.WAXED_WEATHERED_COPPER, CCItems.WAXED_WEATHERED_COPPER_HELMET, CCItems.WAXED_WEATHERED_COPPER_CHESTPLATE, CCItems.WAXED_WEATHERED_COPPER_LEGGINGS, CCItems.WAXED_WEATHERED_COPPER_BOOTS, CCItems.WAXED_WEATHERED_COPPER_SWORD, CCItems.WAXED_WEATHERED_COPPER_PICKAXE, CCItems.WAXED_WEATHERED_COPPER_AXE, CCItems.WAXED_WEATHERED_COPPER_SHOVEL, CCItems.WAXED_WEATHERED_COPPER_HOE);
		copperGearRecipes(consumer, CCItems.WAXED_OXIDIZED_COPPER_INGOT, Blocks.WAXED_OXIDIZED_COPPER, CCItems.WAXED_OXIDIZED_COPPER_HELMET, CCItems.WAXED_OXIDIZED_COPPER_CHESTPLATE, CCItems.WAXED_OXIDIZED_COPPER_LEGGINGS, CCItems.WAXED_OXIDIZED_COPPER_BOOTS, CCItems.WAXED_OXIDIZED_COPPER_SWORD, CCItems.WAXED_OXIDIZED_COPPER_PICKAXE, CCItems.WAXED_OXIDIZED_COPPER_AXE, CCItems.WAXED_OXIDIZED_COPPER_SHOVEL, CCItems.WAXED_OXIDIZED_COPPER_HOE);

		copperIngotRecipes(consumer, Items.COPPER_INGOT, Blocks.COPPER_BLOCK, Blocks.COPPER_DOOR, Blocks.COPPER_TRAPDOOR, COPPER_BARS, COPPER_BUTTON, Blocks.LIGHTNING_ROD, FLOODLIGHT, TOOLBOX);
		copperIngotRecipes(consumer, CCItems.EXPOSED_COPPER_INGOT, Blocks.EXPOSED_COPPER, Blocks.EXPOSED_COPPER_DOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, EXPOSED_COPPER_BARS, EXPOSED_COPPER_BUTTON, EXPOSED_LIGHTNING_ROD, EXPOSED_FLOODLIGHT, EXPOSED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.WEATHERED_COPPER_INGOT, Blocks.WEATHERED_COPPER, Blocks.WEATHERED_COPPER_DOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, WEATHERED_COPPER_BARS, WEATHERED_COPPER_BUTTON, WEATHERED_LIGHTNING_ROD, WEATHERED_FLOODLIGHT, WEATHERED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.OXIDIZED_COPPER_INGOT, Blocks.OXIDIZED_COPPER, Blocks.OXIDIZED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR, OXIDIZED_COPPER_BARS, OXIDIZED_COPPER_BUTTON, OXIDIZED_LIGHTNING_ROD, OXIDIZED_FLOODLIGHT, OXIDIZED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.WAXED_COPPER_INGOT, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_COPPER_DOOR, Blocks.WAXED_COPPER_TRAPDOOR, WAXED_COPPER_BARS, WAXED_COPPER_BUTTON, WAXED_LIGHTNING_ROD, WAXED_FLOODLIGHT, WAXED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.WAXED_EXPOSED_COPPER_INGOT, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, WAXED_EXPOSED_COPPER_BARS, WAXED_EXPOSED_COPPER_BUTTON, WAXED_EXPOSED_LIGHTNING_ROD, WAXED_EXPOSED_FLOODLIGHT, WAXED_EXPOSED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.WAXED_WEATHERED_COPPER_INGOT, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, WAXED_WEATHERED_COPPER_BARS, WAXED_WEATHERED_COPPER_BUTTON, WAXED_WEATHERED_LIGHTNING_ROD, WAXED_WEATHERED_FLOODLIGHT, WAXED_WEATHERED_TOOLBOX);
		copperIngotRecipes(consumer, CCItems.WAXED_OXIDIZED_COPPER_INGOT, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, WAXED_OXIDIZED_COPPER_BARS, WAXED_OXIDIZED_COPPER_BUTTON, WAXED_OXIDIZED_LIGHTNING_ROD, WAXED_OXIDIZED_FLOODLIGHT, WAXED_OXIDIZED_TOOLBOX);

		SpecialRecipeBuilder.special(MusicDiscCopying::new).save(consumer, CavernsAndChasms.MOD_ID + ":music_disc_copying");
		SpecialRecipeBuilder.special(NBTWaxing::new).save(consumer, CavernsAndChasms.MOD_ID + ":nbt_waxing");
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.TUNING_FORK).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.BAROMETER).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCItems.OXIDIZED_COPPER_GOLEM, CCItems.WAXED_OXIDIZED_COPPER_GOLEM);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.DEPTH_GAUGE).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		mimingRecipe(consumer, MISC, Items.MUSIC_DISC_11, CCItems.MUSIC_DISC_EPILOGUE);
		mimingRecipe(consumer, MISC, Items.MOJANG_BANNER_PATTERN, CCItems.ABNORMALS_BANNER_PATTERN);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, ROCKY_DIRT, 4).define('D', Blocks.DIRT).define('C', Blocks.COBBLESTONE).pattern("DC").pattern("CD").unlockedBy("has_dirt", has(Blocks.DIRT)).unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FLINT_BLOCK).define('#', Items.FLINT).pattern("##").pattern("##").unlockedBy("has_flint", has(Items.FLINT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.ROOTED_DIRT).requires(Blocks.DIRT).requires(Blocks.HANGING_ROOTS).unlockedBy("has_hanging_roots", has(Blocks.HANGING_ROOTS)).save(consumer, CavernsAndChasms.location(getItemName(Blocks.ROOTED_DIRT)));

		ShapedRecipeBuilder.shaped(DECORATIONS, LAVA_LAMP).define('G', Tags.Items.INGOTS_GOLD).define('B', Tags.Items.BUCKETS_LAVA).pattern("GGG").pattern(" B ").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);


		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_TORCH, 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Tags.Items.RODS_WOODEN).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_CAMPFIRE).define('L', ItemTags.LOGS).define('S', Tags.Items.RODS_WOODEN).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_LANTERN).define('#', CUPRIC_TORCH).define('X', Tags.Items.NUGGETS_IRON).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_cupric_torch", has(CUPRIC_TORCH)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, BRAZIER).define('#', ItemTags.COALS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_coal", has(ItemTags.COALS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SOUL_BRAZIER).define('#', ItemTags.SOUL_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_BRAZIER).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, ENDER_BRAZIER).define('#', CCItemTags.ENDER_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_end_stone", has(CCItemTags.ENDER_FIRE_BASE_BLOCKS)).save(consumer.withConditions(ENDERGETIC_LOADED));

		ShapedRecipeBuilder.shaped(REDSTONE, Blocks.TNT).define('#', Tags.Items.SANDS).define('X', Items.GUNPOWDER).pattern("X#").pattern("#X").unlockedBy("has_gunpowder", has(Items.GUNPOWDER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SPARKLER.getFirst(), 4).define('X', Items.GUNPOWDER).define('#', Tags.Items.RODS_WOODEN).pattern("X").pattern("#").unlockedBy("has_gunpowder", has(Items.GUNPOWDER)).save(consumer);
		sparkler(consumer, WHITE_SPARKLER.getFirst(), Items.WHITE_DYE);
		sparkler(consumer, LIGHT_GRAY_SPARKLER.getFirst(), Items.LIGHT_GRAY_DYE);
		sparkler(consumer, GRAY_SPARKLER.getFirst(), Items.GRAY_DYE);
		sparkler(consumer, BLACK_SPARKLER.getFirst(), Items.BLACK_DYE);
		sparkler(consumer, BROWN_SPARKLER.getFirst(), Items.BROWN_DYE);
		sparkler(consumer, RED_SPARKLER.getFirst(), Items.RED_DYE);
		sparkler(consumer, ORANGE_SPARKLER.getFirst(), Items.ORANGE_DYE);
		sparkler(consumer, YELLOW_SPARKLER.getFirst(), Items.YELLOW_DYE);
		sparkler(consumer, LIME_SPARKLER.getFirst(), Items.LIME_DYE);
		sparkler(consumer, GREEN_SPARKLER.getFirst(), Items.GREEN_DYE);
		sparkler(consumer, CYAN_SPARKLER.getFirst(), Items.CYAN_DYE);
		sparkler(consumer, LIGHT_BLUE_SPARKLER.getFirst(), Items.LIGHT_BLUE_DYE);
		sparkler(consumer, BLUE_SPARKLER.getFirst(), Items.BLUE_DYE);
		sparkler(consumer, PURPLE_SPARKLER.getFirst(), Items.PURPLE_DYE);
		sparkler(consumer, MAGENTA_SPARKLER.getFirst(), Items.MAGENTA_DYE);
		sparkler(consumer, PINK_SPARKLER.getFirst(), Items.PINK_DYE);

		storageRecipes(consumer, MISC, Items.CHARCOAL, BUILDING_BLOCKS, CHARCOAL_BLOCK);
		storageRecipes(consumer, MISC, CCItems.SPINEL, BUILDING_BLOCKS, SPINEL_BLOCK);
		storageRecipes(consumer, MISC, CCItems.RAW_SILVER, BUILDING_BLOCKS, RAW_SILVER_BLOCK);
		storageRecipes(consumer, MISC, CCItems.RAW_TIN, BUILDING_BLOCKS, RAW_TIN_BLOCK);
		storageRecipes(consumer, MISC, CCItems.ZIRCONIA, BUILDING_BLOCKS, ZIRCONIA_BLOCK);
		storageRecipes(consumer, MISC, CCItems.TURQUOISE, BUILDING_BLOCKS, TURQUOISE_BLOCK);
		storageRecipes(consumer, FOOD, Items.ROTTEN_FLESH, BUILDING_BLOCKS, ROTTEN_FLESH_BLOCK);
		storageRecipes(consumer, MISC, Items.GUNPOWDER, BUILDING_BLOCKS, GUNPOWDER_BLOCK);
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.SILVER_INGOT, BUILDING_BLOCKS, SILVER_BLOCK, "silver_ingot_from_silver_block", "silver_ingot");
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.TIN_INGOT, BUILDING_BLOCKS, TIN_BLOCK, "tin_ingot_from_tin_block", "tin_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.SILVER_NUGGET, MISC, CCItems.SILVER_INGOT, "silver_ingot_from_nuggets", "silver_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.TIN_NUGGET, MISC, CCItems.TIN_INGOT, "tin_ingot_from_nuggets", "tin_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.COPPER_NUGGET, MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.COPPER_NUGGET, 9).requires(WAXED_COPPER_INGOT).unlockedBy(getHasName(WAXED_COPPER_INGOT), has(CCItems.WAXED_COPPER_INGOT)).save(consumer, getConversionRecipeName(CCItems.COPPER_NUGGET, WAXED_COPPER_INGOT));

		oreRecipes(consumer, SILVER_SMELTABLES, MISC, CCItems.SILVER_INGOT, 1.0F, 200, 1.0F, 100, "silver_ingot");
		oreRecipes(consumer, TIN_SMELTABLES, MISC, CCItems.TIN_INGOT, 0.7F, 200, 0.7F, 100, "tin_ingot");
		oreRecipes(consumer, SPINEL_SMELTABLES, MISC, CCItems.SPINEL, 0.2F, 200, 0.2F, 100, "spinel");
		oreRecipes(consumer, TURQUOISE_SMELTABLES, MISC, CCItems.TURQUOISE, 1.0F, 200, 1.0F, 100, "turquoise");

		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_AXE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_BOOTS).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_CHESTPLATE).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_HELMET).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_HOE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_LEGGINGS).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_PICKAXE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_SHOVEL).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("#").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_SWORD).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("X").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		pressurePlateBuilder(REDSTONE, MEDIUM_WEIGHTED_PRESSURE_PLATE, Ingredient.of(CCItemTags.INGOTS_SILVER)).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SILVER_BARS, 16).define('#', CCItemTags.INGOTS_SILVER).pattern("###").pattern("###").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CCItems.SILVER_PICKAXE, CCItems.SILVER_SHOVEL, CCItems.SILVER_AXE, CCItems.SILVER_HOE, CCItems.SILVER_SWORD, CCItems.SILVER_HELMET, CCItems.SILVER_CHESTPLATE, CCItems.SILVER_LEGGINGS, CCItems.SILVER_BOOTS, CCItems.SILVER_HORSE_ARMOR), MISC, CCItems.SILVER_NUGGET, 0.1F, 200).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE)).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL)).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE)).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE)).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD)).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET)).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE)).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS)).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS)).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR)).save(consumer, CavernsAndChasms.location(getSmeltingRecipeName(CCItems.SILVER_NUGGET)));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(CCItems.SILVER_PICKAXE, CCItems.SILVER_SHOVEL, CCItems.SILVER_AXE, CCItems.SILVER_HOE, CCItems.SILVER_SWORD, CCItems.SILVER_HELMET, CCItems.SILVER_CHESTPLATE, CCItems.SILVER_LEGGINGS, CCItems.SILVER_BOOTS, CCItems.SILVER_HORSE_ARMOR), MISC, CCItems.SILVER_NUGGET, 0.1F, 100).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE)).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL)).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE)).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE)).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD)).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET)).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE)).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS)).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS)).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR)).save(consumer, CavernsAndChasms.location(getBlastingRecipeName(CCItems.SILVER_NUGGET)));
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.KUNAI, 3).define('#', CCItemTags.NUGGETS_SILVER).define('S', CCItemTags.INGOTS_SILVER).pattern(" S").pattern("# ").unlockedBy("has_silver_nugget", has(CCItemTags.NUGGETS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(BREWING, Blocks.BREWING_STAND).define('B', Items.BLAZE_ROD).define('#', CCItemTags.INGOTS_SILVER).pattern(" B ").pattern("###").unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, GOLDEN_BARS, 16).define('#', Items.GOLD_INGOT).pattern("###").pattern("###").unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(MISC, CCItems.GOLDEN_BUCKET).define('#', Blocks.GOLD_BLOCK).pattern("# #").pattern(" # ").unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(FOOD, Blocks.CAKE).define('A', CCItems.GOLDEN_MILK_BUCKET).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Items.EGG).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Items.EGG)).save(consumer.withConditions(new NotCondition(new ModLoadedCondition("environmental"))), CavernsAndChasms.location(getSimpleRecipeName(Blocks.CAKE)));
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.GOLDEN_MILK_BUCKET).requires(CCItems.GOLDEN_BUCKET).requires(Ingredient.of(CCItemTags.BOTTLES_MILK), 3).unlockedBy("has_milk_bottle", has(CCItemTags.BOTTLES_MILK)).save(consumer.withConditions(new NotCondition(new TagEmptyCondition(CCItemTags.BOTTLES_MILK.location()))));

		ShapedRecipeBuilder.shaped(DECORATIONS, TIN_BARS, 16).define('#', CCItemTags.INGOTS_TIN).pattern("###").pattern("###").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		pressurePlateBuilder(REDSTONE, HOLD_PLATE, Ingredient.of(CCItemTags.INGOTS_TIN)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapelessRecipeBuilder.shapeless(REDSTONE, HOLD_BUTTON).requires(ItemTags.WOODEN_BUTTONS).requires(CCItemTags.INGOTS_TIN).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, WINCH).define('T', CCItemTags.INGOTS_TIN).define('L', Items.LEVER).pattern(" T ").pattern("TLT").pattern(" T ").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, DIMMER).define('T', CCItemTags.INGOTS_TIN).define('N', CCItemTags.NUGGETS_TIN).define('R', Items.REDSTONE_TORCH).pattern("NNN").pattern("NRN").pattern(" T ").unlockedBy("has_tin_nuggets", has(CCItemTags.NUGGETS_TIN)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, BOUNCER).define('T', CCItemTags.STORAGE_BLOCKS_TIN).define('S', Items.SLIME_BALL).define('R', Tags.Items.DUSTS_REDSTONE).pattern("SRS").pattern("STS").pattern("SRS").unlockedBy("has_slime_ball", has(Items.SLIME_BALL)).unlockedBy("has_tin", has(CCItemTags.STORAGE_BLOCKS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, HOOP).define('T', CCItemTags.INGOTS_TIN).define('Q', Items.QUARTZ).pattern("TTT").pattern("TQT").pattern("TTT").unlockedBy("has_quartz", has(Items.QUARTZ)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, STORAGE_DUCT).define('I', CCItemTags.INGOTS_TIN).define('B', CCItemTags.STORAGE_BLOCKS_TIN).pattern("I I").pattern("B B").pattern("I I").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, STORAGE_DUCT_HATCH).define('I', CCItemTags.INGOTS_TIN).pattern("I I").pattern("I I").pattern("I I").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FLOAT_GLASS, 2).define('G', Blocks.GLASS).define('S', Items.AMETHYST_SHARD).define('T', CCItemTags.INGOTS_TIN).pattern(" T ").pattern("SGS").pattern(" T ").unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).unlockedBy("has_tin", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, FLOAT_GLASS_PANE, 16).define('#', FLOAT_GLASS).pattern("###").pattern("###").unlockedBy("has_float_glass", has(FLOAT_GLASS)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, CCItems.ROLLER_DOOR, 2).define('T', CCItemTags.INGOTS_TIN).pattern("TT").pattern("TT").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapelessRecipeBuilder.shapeless(REDSTONE, CCItems.ROLLER_WINDOW).requires(CCItems.ROLLER_DOOR).requires(FLOAT_GLASS_PANE).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.TINPLATE, 16).requires(CCItemTags.INGOTS_TIN).requires(Items.HONEYCOMB).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.TINPLATE, BUILDING_BLOCKS, TINPLATE_BLOCK, "tinplate_from_tinplate_block", "tinplate");
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.RICOCHET_ARROW, 4).define('#', Tags.Items.RODS_WOODEN).define('X', CCItems.TIN_INGOT).define('Y', Items.FEATHER).pattern("X").pattern("#").pattern("Y").unlockedBy("has_feather", has(Items.FEATHER)).unlockedBy("has_tin", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.PACKING_CONTAINER).define('#', CCItemTags.INGOTS_TIN).pattern("# #").pattern("# #").pattern("###").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.AEGIS).define('I', CCItemTags.INGOTS_TIN).define('B', CCItemTags.STORAGE_BLOCKS_TIN).define('L', Items.LEATHER).pattern("ILI").pattern("IBI").pattern(" I ").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);

		copperBulb(consumer, TIN_BULB, CCItems.TIN_INGOT);
		ShapedRecipeBuilder.shaped(DECORATIONS, TIN_CHAIN).define('#', CCItemTags.NUGGETS_TIN).pattern("#").pattern("#").pattern("#").unlockedBy("has_tin_nugget", has(CCItemTags.NUGGETS_TIN)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FROSTED_GLASS, 2).define('G', Blocks.GLASS).define('S', Items.AMETHYST_SHARD).define('T', Items.QUARTZ).pattern(" T ").pattern("SGS").pattern(" T ").unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).unlockedBy("has_quartz", has(Items.QUARTZ)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, FROSTED_GLASS_PANE, 16).define('#', FROSTED_GLASS).pattern("###").pattern("###").unlockedBy("has_frosted_glass", has(FROSTED_GLASS)).save(consumer);

		ShapedRecipeBuilder.shaped(REDSTONE, RESISTOR).define('#', Blocks.REDSTONE_TORCH).define('X', Items.REDSTONE).define('I', CCItemTags.INGOTS_TIN).pattern("#X#").pattern("III").unlockedBy("has_redstone_torch", has(Blocks.REDSTONE_TORCH)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, REFRACTOR).define('#', Blocks.REDSTONE_TORCH).define('X', Items.AMETHYST_BLOCK).define('I', CCItemTags.INGOTS_TIN).pattern(" # ").pattern("#X#").pattern("III").unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK)).save(consumer);

		ShapelessRecipeBuilder.shapeless(MISC, CCItems.LIVING_FLESH, 2).requires(Items.ROTTEN_FLESH, 3).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 2).requires(Items.GHAST_TEAR, 2).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_HELMET).define('X', CCItems.LIVING_FLESH).pattern("XXX").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_CHESTPLATE).define('X', CCItems.LIVING_FLESH).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_LEGGINGS).define('X', CCItems.LIVING_FLESH).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_BOOTS).define('X', CCItems.LIVING_FLESH).pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH)).save(consumer);
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.LIVING_FLESH, BUILDING_BLOCKS, SANGUINE_BLOCK, "living_flesh_from_sanguine_block", "living_flesh");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SANGUINE_TILES, 8).define('X', CCItems.LIVING_FLESH).define('#', Blocks.NETHERRACK).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILES, 8).define('X', CCItemTags.INGOTS_SILVER).define('#', SANGUINE_TILES).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_sanguine_tiles", has(SANGUINE_TILES)).save(consumer);
		generateRecipes(consumer, SANGUINE_TILES_FAMILY);
		generateRecipes(consumer, FORTIFIED_SANGUINE_TILES_FAMILY);
		stonecutterRecipes(consumer, SANGUINE_TILES_FAMILY);
		stonecutterRecipes(consumer, FORTIFIED_SANGUINE_TILES_FAMILY);

		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.NECROMIUM_INGOT, BUILDING_BLOCKS, NECROMIUM_BLOCK, "necromium_ingot_from_necromium_block", "necromium_ingot");
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.NECROMIUM_INGOT).requires(Items.NETHERITE_SCRAP, 4).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 4).group("necromium_ingot").unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(consumer);
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.NECROMIUM_NUGGET, MISC, CCItems.NECROMIUM_INGOT, "necromium_ingot_from_nuggets", "necromium_ingot");
		necromiumSmithingRecipe(consumer, Items.DIAMOND_CHESTPLATE, COMBAT, CCItems.NECROMIUM_CHESTPLATE);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_LEGGINGS, COMBAT, CCItems.NECROMIUM_LEGGINGS);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HELMET, COMBAT, CCItems.NECROMIUM_HELMET);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_BOOTS, COMBAT, CCItems.NECROMIUM_BOOTS);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_SWORD, COMBAT, CCItems.NECROMIUM_SWORD);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_AXE, TOOLS, CCItems.NECROMIUM_AXE);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_PICKAXE, TOOLS, CCItems.NECROMIUM_PICKAXE);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HOE, TOOLS, CCItems.NECROMIUM_HOE);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_SHOVEL, TOOLS, CCItems.NECROMIUM_SHOVEL);
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NECROMIUM_HORSE_ARMOR);
		netheriteSmithingRecipe(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NETHERITE_HORSE_ARMOR.get());

		storageRecipesWithCustomPacking(consumer, MISC, CCItems.NETHERITE_NUGGET, MISC, Items.NETHERITE_INGOT, "netherite_ingot_from_nuggets", "netherite_ingot");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, LAPIS_LAZULI_BRICKS).define('#', Items.LAPIS_LAZULI).pattern("##").pattern("##").unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(consumer);
		generateRecipes(consumer, LAPIS_LAZULI_BRICKS_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, LAPIS_LAZULI_PILLAR, 2).define('#', LAPIS_LAZULI_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(LAPIS_LAZULI_BRICKS), has(LAPIS_LAZULI_BRICKS)).unlockedBy(getHasName(LAPIS_LAZULI_PILLAR), has(LAPIS_LAZULI_PILLAR)).save(consumer);
		lampRecipe(consumer, LAPIS_LAZULI_LAMP, Tags.Items.GEMS_LAPIS);
		stonecutterRecipes(consumer, LAPIS_LAZULI_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, LAPIS_LAZULI_PILLAR, LAPIS_LAZULI_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_BRICKS).define('#', CCItemTags.GEMS_SPINEL).pattern("##").pattern("##").unlockedBy(getHasName(CCItems.SPINEL), has(CCItemTags.GEMS_SPINEL)).save(consumer);
		generateRecipes(consumer, SPINEL_BRICKS_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_PILLAR, 2).define('#', SPINEL_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(SPINEL_BRICKS), has(SPINEL_BRICKS)).unlockedBy(getHasName(SPINEL_PILLAR), has(SPINEL_PILLAR)).save(consumer);
		lampRecipe(consumer, SPINEL_LAMP, CCItemTags.GEMS_SPINEL);
		stonecutterRecipes(consumer, SPINEL_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SPINEL_PILLAR, SPINEL_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, TURQUOISE_TILES, 8).define('#', Blocks.STONE_BRICKS).define('S', CCItemTags.GEMS_TURQUOISE).pattern("###").pattern("#S#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer);
		generateRecipes(consumer, TURQUOISE_TILES_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, TURQUOISE_PILLAR, 2).define('#', TURQUOISE_TILES).pattern("#").pattern("#").unlockedBy(getHasName(TURQUOISE_TILES), has(TURQUOISE_TILES)).unlockedBy(getHasName(TURQUOISE_PILLAR), has(TURQUOISE_PILLAR)).save(consumer);
		lampRecipe(consumer, TURQUOISE_LAMP, CCItemTags.GEMS_TURQUOISE);
		stonecutterRecipes(consumer, TURQUOISE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TURQUOISE_PILLAR, TURQUOISE_TILES);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, ORNATE_GLASS, 8).define('G', Tags.Items.GLASS_BLOCKS).define('Z', CCItemTags.GEMS_ZIRCONIA).pattern("GGG").pattern("GZG").pattern("GGG").unlockedBy("has_zirconia", has(CCItemTags.GEMS_ZIRCONIA)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, ORNATE_GLASS_PANE, 16).define('#', ORNATE_GLASS).pattern("###").pattern("###").unlockedBy("has_ornate_glass", has(ORNATE_GLASS)).save(consumer);
		lampRecipe(consumer, ZIRCONIA_LAMP, CCItemTags.GEMS_ZIRCONIA);

		lampRecipe(consumer, AMETHYST_LAMP, Tags.Items.GEMS_AMETHYST);
		lampRecipe(consumer, QUARTZ_LAMP, Tags.Items.GEMS_QUARTZ);
		lampRecipe(consumer, DIAMOND_LAMP, Tags.Items.GEMS_DIAMOND);
		lampRecipe(consumer, EMERALD_LAMP, Tags.Items.GEMS_EMERALD);

		platedBricksRecipe(consumer, IRON_BRICKS, Tags.Items.INGOTS_IRON, "iron");
		platedBricksRecipes(consumer, IRON_BRICKS_FAMILY);

		platedBricksRecipe(consumer, TIN_BRICKS, CCItemTags.INGOTS_TIN, "tin");
		platedBricksRecipes(consumer, TIN_BRICKS_FAMILY);

		platedBricksRecipe(consumer, GOLD_BRICKS, Tags.Items.INGOTS_GOLD, "gold");
		platedBricksRecipes(consumer, GOLD_BRICKS_FAMILY);

		platedBricksRecipe(consumer, SILVER_BRICKS, CCItemTags.INGOTS_SILVER, "silver");
		platedBricksRecipes(consumer, SILVER_BRICKS_FAMILY);

		platedBricksRecipe(consumer, COPPER_BRICKS, Items.COPPER_INGOT);
		platedBricksRecipe(consumer, EXPOSED_COPPER_BRICKS, EXPOSED_COPPER_INGOT);
		platedBricksRecipe(consumer, WEATHERED_COPPER_BRICKS, WEATHERED_COPPER_INGOT);
		platedBricksRecipe(consumer, OXIDIZED_COPPER_BRICKS, OXIDIZED_COPPER_INGOT);
		platedBricksRecipe(consumer, WAXED_COPPER_BRICKS, WAXED_COPPER_INGOT);
		platedBricksRecipe(consumer, WAXED_EXPOSED_COPPER_BRICKS, WAXED_EXPOSED_COPPER_INGOT);
		platedBricksRecipe(consumer, WAXED_WEATHERED_COPPER_BRICKS, WAXED_WEATHERED_COPPER_INGOT);
		platedBricksRecipe(consumer, WAXED_OXIDIZED_COPPER_BRICKS, WAXED_OXIDIZED_COPPER_INGOT);
		platedBricksRecipes(consumer, COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, EXPOSED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WEATHERED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, OXIDIZED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_EXPOSED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_WEATHERED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_OXIDIZED_COPPER_BRICKS_FAMILY);

		wall(consumer, DECORATIONS, STONE_WALL, Blocks.STONE);
		stonecutterRecipe(consumer, DECORATIONS, STONE_WALL, Blocks.STONE);

		wall(consumer, DECORATIONS, POLISHED_GRANITE_WALL, Blocks.POLISHED_GRANITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_GRANITE_WALL, Blocks.POLISHED_GRANITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_GRANITE_WALL, Blocks.GRANITE);
		chiseled(consumer, BUILDING_BLOCKS, CHISELED_POLISHED_GRANITE, Blocks.POLISHED_GRANITE_SLAB);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_GRANITE, Blocks.POLISHED_GRANITE);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_GRANITE, Blocks.GRANITE);
		polished(consumer, BUILDING_BLOCKS, GRANITE_BRICKS, Blocks.POLISHED_GRANITE);
		generateRecipes(consumer, GRANITE_BRICKS_FAMILY);
		generateRecipes(consumer, GRANITE_TILES_FAMILY);
		stonecutterRecipes(consumer, GRANITE_BRICKS_FAMILY, Blocks.GRANITE, Blocks.POLISHED_GRANITE, GRANITE_BRICKS);
		stonecutterRecipes(consumer, GRANITE_TILES_FAMILY, Blocks.GRANITE, Blocks.POLISHED_GRANITE, GRANITE_BRICKS, GRANITE_TILES);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, GRANITE_PILLAR, 2).define('#', Blocks.POLISHED_GRANITE).pattern("#").pattern("#").unlockedBy(getHasName(Blocks.POLISHED_GRANITE), has(Blocks.POLISHED_GRANITE)).unlockedBy(getHasName(GRANITE_PILLAR), has(GRANITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, GRANITE_PILLAR, Blocks.GRANITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, GRANITE_PILLAR, Blocks.POLISHED_GRANITE);

		wall(consumer, DECORATIONS, POLISHED_DIORITE_WALL, Blocks.POLISHED_DIORITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_DIORITE_WALL, Blocks.POLISHED_DIORITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_DIORITE_WALL, Blocks.DIORITE);
		chiseled(consumer, BUILDING_BLOCKS, CHISELED_POLISHED_DIORITE, Blocks.POLISHED_DIORITE_SLAB);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_DIORITE, Blocks.POLISHED_DIORITE);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_DIORITE, Blocks.DIORITE);
		polished(consumer, BUILDING_BLOCKS, DIORITE_BRICKS, Blocks.POLISHED_DIORITE);
		generateRecipes(consumer, DIORITE_BRICKS_FAMILY);
		generateRecipes(consumer, DIORITE_TILES_FAMILY);
		stonecutterRecipes(consumer, DIORITE_BRICKS_FAMILY, Blocks.DIORITE, Blocks.POLISHED_DIORITE, DIORITE_BRICKS);
		stonecutterRecipes(consumer, DIORITE_TILES_FAMILY, Blocks.DIORITE, Blocks.POLISHED_DIORITE, DIORITE_BRICKS, DIORITE_TILES);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, DIORITE_PILLAR, 2).define('#', Blocks.POLISHED_DIORITE).pattern("#").pattern("#").unlockedBy(getHasName(Blocks.POLISHED_DIORITE), has(Blocks.POLISHED_DIORITE)).unlockedBy(getHasName(DIORITE_PILLAR), has(DIORITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DIORITE_PILLAR, Blocks.DIORITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DIORITE_PILLAR, Blocks.POLISHED_DIORITE);

		wall(consumer, DECORATIONS, POLISHED_ANDESITE_WALL, Blocks.POLISHED_ANDESITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_ANDESITE_WALL, Blocks.POLISHED_ANDESITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_ANDESITE_WALL, Blocks.ANDESITE);
		chiseled(consumer, BUILDING_BLOCKS, CHISELED_POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE_SLAB);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE);
		stonecutterRecipe(consumer, DECORATIONS, CHISELED_POLISHED_ANDESITE, Blocks.ANDESITE);
		polished(consumer, BUILDING_BLOCKS, ANDESITE_BRICKS, Blocks.POLISHED_ANDESITE);
		generateRecipes(consumer, ANDESITE_BRICKS_FAMILY);
		generateRecipes(consumer, ANDESITE_TILES_FAMILY);
		stonecutterRecipes(consumer, ANDESITE_BRICKS_FAMILY, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, ANDESITE_BRICKS);
		stonecutterRecipes(consumer, ANDESITE_TILES_FAMILY, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, ANDESITE_BRICKS, ANDESITE_TILES);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, ANDESITE_PILLAR, 2).define('#', Blocks.POLISHED_ANDESITE).pattern("#").pattern("#").unlockedBy(getHasName(Blocks.POLISHED_ANDESITE), has(Blocks.POLISHED_ANDESITE)).unlockedBy(getHasName(ANDESITE_PILLAR), has(ANDESITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, ANDESITE_PILLAR, Blocks.ANDESITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, ANDESITE_PILLAR, Blocks.POLISHED_ANDESITE);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.CALCITE).requires(Blocks.DIORITE).requires(Items.AMETHYST_SHARD).unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(consumer, CavernsAndChasms.location(RecipeBuilder.getDefaultRecipeId(Blocks.CALCITE).getPath()));
		generateRecipes(consumer, CALCITE_FAMILY);
		generateRecipes(consumer, POLISHED_CALCITE_FAMILY);
		generateRecipes(consumer, CALCITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CALCITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_CALCITE_FAMILY, Blocks.CALCITE, POLISHED_CALCITE);
		stonecutterRecipes(consumer, CALCITE_BRICKS_FAMILY, Blocks.CALCITE, POLISHED_CALCITE, CALCITE_BRICKS);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CALCITE_PILLAR, 2).define('#', POLISHED_CALCITE).pattern("#").pattern("#").unlockedBy(getHasName(POLISHED_CALCITE), has(POLISHED_CALCITE)).unlockedBy(getHasName(CALCITE_PILLAR), has(CALCITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_PILLAR, Blocks.CALCITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_PILLAR, POLISHED_CALCITE);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.TUFF, 2).requires(Blocks.BASALT).requires(Blocks.COBBLESTONE).unlockedBy("has_stone", has(Blocks.BASALT)).save(consumer, CavernsAndChasms.location(RecipeBuilder.getDefaultRecipeId(Blocks.TUFF).getPath()));
		generateRecipes(consumer, TUFF_FAMILY);
		generateRecipes(consumer, POLISHED_TUFF_FAMILY);
		generateRecipes(consumer, TUFF_BRICKS_FAMILY);
		generateRecipes(consumer, TUFF_TILES_FAMILY);
		stonecutterRecipes(consumer, TUFF_FAMILY);
		stonecutterRecipes(consumer, POLISHED_TUFF_FAMILY, Blocks.TUFF, POLISHED_TUFF);
		stonecutterRecipes(consumer, TUFF_BRICKS_FAMILY, Blocks.TUFF, POLISHED_TUFF, TUFF_BRICKS);
		stonecutterRecipes(consumer, TUFF_TILES_FAMILY, Blocks.TUFF, POLISHED_TUFF, TUFF_BRICKS, TUFF_TILES);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, TUFF_PILLAR, 2).define('#', TUFF_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(TUFF_BRICKS), has(TUFF_BRICKS)).unlockedBy(getHasName(TUFF_PILLAR), has(TUFF_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TUFF_PILLAR, Blocks.TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TUFF_PILLAR, POLISHED_TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TUFF_PILLAR, TUFF_BRICKS);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.TUFF), BUILDING_BLOCKS, SMOOTH_TUFF, 0.1F, 200).unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, BUILDING_BLOCKS, Blocks.TUFF, SMOOTH_TUFF, 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_TUFF_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_TUFF_FAMILY);

		generateRecipes(consumer, SHALE_FAMILY);
		stonecutterRecipes(consumer, SHALE_FAMILY);
		stonecutterRecipes(consumer, BlockFamilies.POLISHED_TUFF, SHALE.get());
		stonecutterRecipes(consumer, BlockFamilies.TUFF_BRICKS, SHALE.get());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(SHALE), BUILDING_BLOCKS, SMOOTH_SHALE, 0.1F, 200).unlockedBy("has_shale", has(SHALE)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, BUILDING_BLOCKS, SHALE, SMOOTH_SHALE, 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_SHALE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_SHALE_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SHALE_PILLAR, 2).define('#', Blocks.TUFF_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(Blocks.TUFF_BRICKS), has(Blocks.TUFF_BRICKS)).unlockedBy(getHasName(SHALE_PILLAR), has(SHALE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SHALE_PILLAR, Blocks.TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SHALE_PILLAR, Blocks.POLISHED_TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SHALE_PILLAR, Blocks.TUFF_BRICKS);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, SUGILITE).requires(Blocks.GRANITE).requires(CCItemTags.GEMS_SPINEL).unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).save(consumer);
		generateRecipes(consumer, SUGILITE_FAMILY);
		generateRecipes(consumer, POLISHED_SUGILITE_FAMILY);
		generateRecipes(consumer, SUGILITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, SUGILITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_SUGILITE_FAMILY, SUGILITE, POLISHED_SUGILITE);
		stonecutterRecipes(consumer, SUGILITE_BRICKS_FAMILY, SUGILITE, POLISHED_SUGILITE, SUGILITE_BRICKS);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SUGILITE_PILLAR, 2).define('#', SUGILITE_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(SUGILITE_BRICKS), has(SUGILITE_BRICKS)).unlockedBy(getHasName(SUGILITE_PILLAR), has(SUGILITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SUGILITE_PILLAR, SUGILITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SUGILITE_PILLAR, POLISHED_SUGILITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SUGILITE_PILLAR, SUGILITE_BRICKS);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CYLINDRITE).requires(Blocks.DEEPSLATE).requires(CCItemTags.RAW_MATERIALS_TIN).unlockedBy("has_raw_tin", has(CCItemTags.RAW_MATERIALS_TIN)).save(consumer);
		generateRecipes(consumer, CYLINDRITE_FAMILY);
		generateRecipes(consumer, POLISHED_CYLINDRITE_FAMILY);
		generateRecipes(consumer, CYLINDRITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CYLINDRITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_CYLINDRITE_FAMILY, CYLINDRITE, POLISHED_CYLINDRITE);
		stonecutterRecipes(consumer, CYLINDRITE_BRICKS_FAMILY, CYLINDRITE, POLISHED_CYLINDRITE, CYLINDRITE_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CYLINDRITE_PILLAR, 2).define('#', CYLINDRITE_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(CYLINDRITE_BRICKS), has(CYLINDRITE_BRICKS)).unlockedBy(getHasName(CYLINDRITE_PILLAR), has(CYLINDRITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CYLINDRITE_PILLAR, CYLINDRITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CYLINDRITE_PILLAR, POLISHED_CYLINDRITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CYLINDRITE_PILLAR, CYLINDRITE_BRICKS);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CYLINDRITE), BUILDING_BLOCKS, SMOOTH_CYLINDRITE, 0.1F, 200).unlockedBy("has_cylindrite", has(CYLINDRITE)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, BUILDING_BLOCKS, CYLINDRITE, SMOOTH_CYLINDRITE, 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_CYLINDRITE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_CYLINDRITE_FAMILY);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CASSITERITE).requires(Blocks.GRANITE).requires(CCItemTags.RAW_MATERIALS_TIN).unlockedBy("has_raw_tin", has(CCItemTags.RAW_MATERIALS_TIN)).save(consumer);
		generateRecipes(consumer, CASSITERITE_FAMILY);
		generateRecipes(consumer, POLISHED_CASSITERITE_FAMILY);
		generateRecipes(consumer, CASSITERITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CASSITERITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_CASSITERITE_FAMILY, CASSITERITE, POLISHED_CASSITERITE);
		stonecutterRecipes(consumer, CASSITERITE_BRICKS_FAMILY, CASSITERITE, POLISHED_CASSITERITE, CASSITERITE_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CASSITERITE_PILLAR, 2).define('#', CASSITERITE_BRICKS).pattern("#").pattern("#").unlockedBy(getHasName(CASSITERITE_BRICKS), has(CASSITERITE_BRICKS)).unlockedBy(getHasName(CASSITERITE_PILLAR), has(CASSITERITE_PILLAR)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR, CASSITERITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR, POLISHED_CASSITERITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR, CASSITERITE_BRICKS);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CASSITERITE), BUILDING_BLOCKS, SMOOTH_CASSITERITE, 0.1F, 200).unlockedBy("has_cassiterite", has(CASSITERITE)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, BUILDING_BLOCKS, CASSITERITE, SMOOTH_CASSITERITE, 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_CASSITERITE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_CASSITERITE_FAMILY);

		generateRecipes(consumer, RHYOLITE_FAMILY);
		generateRecipes(consumer, POLISHED_RHYOLITE_FAMILY);
		generateRecipes(consumer, RHYOLITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, RHYOLITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_RHYOLITE_FAMILY, RHYOLITE, POLISHED_RHYOLITE);
		stonecutterRecipes(consumer, RHYOLITE_BRICKS_FAMILY, RHYOLITE, POLISHED_RHYOLITE, RHYOLITE_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MAGMATIC_RHYOLITE, 4).define('#', RHYOLITE).define('X', Blocks.MAGMA_BLOCK).pattern("#X").pattern("X#").unlockedBy("has_magma_block", has(Blocks.MAGMA_BLOCK)).save(consumer);
		generateRecipes(consumer, MAGMATIC_RHYOLITE_FAMILY);
		generateRecipes(consumer, POLISHED_MAGMATIC_RHYOLITE_FAMILY);
		generateRecipes(consumer, MAGMATIC_RHYOLITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, MAGMATIC_RHYOLITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_MAGMATIC_RHYOLITE_FAMILY, MAGMATIC_RHYOLITE, POLISHED_MAGMATIC_RHYOLITE);
		stonecutterRecipes(consumer, MAGMATIC_RHYOLITE_BRICKS_FAMILY, MAGMATIC_RHYOLITE, POLISHED_MAGMATIC_RHYOLITE, MAGMATIC_RHYOLITE_BRICKS);

		generateRecipes(consumer, DRIPSTONE_FAMILY);
		generateRecipes(consumer, POLISHED_DRIPSTONE_FAMILY);
		generateRecipes(consumer, DRIPSTONE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, DRIPSTONE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_DRIPSTONE_FAMILY, Blocks.DRIPSTONE_BLOCK, POLISHED_DRIPSTONE);
		stonecutterRecipes(consumer, DRIPSTONE_BRICKS_FAMILY, Blocks.DRIPSTONE_BLOCK, POLISHED_DRIPSTONE, DRIPSTONE_BRICKS);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DRIPSTONE_BRICKS), BUILDING_BLOCKS, CRACKED_DRIPSTONE_BRICKS, 0.1F, 200).unlockedBy("has_dripstone_bricks", has(DRIPSTONE_BRICKS)).save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.DRIPSTONE_BLOCK), BUILDING_BLOCKS, SMOOTH_DRIPSTONE, 0.1F, 200).unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, SMOOTH_DRIPSTONE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_DRIPSTONE_FAMILY);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, DRIPSTONE_SHINGLES).define('#', DRIPSTONE_SLAB).pattern("#").pattern("#").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES_FAMILY);
		stonecutterRecipes(consumer, DRIPSTONE_SHINGLES_FAMILY, Blocks.DRIPSTONE_BLOCK, DRIPSTONE_SHINGLES);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, FLOODED_DRIPSTONE_SHINGLES, 8).requires(Tags.Items.BUCKETS_WATER).requires(DRIPSTONE_SHINGLES, 8).unlockedBy("has_dripstone_shingles", has(DRIPSTONE_SHINGLES)).save(consumer);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.AMETHYST_SHARD, BUILDING_BLOCKS, AMETHYST_BLOCK, "amethyst_from_amethyst_block", "amethyst_shard");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST, 4).define('#', Blocks.AMETHYST_BLOCK).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.AMETHYST_BLOCK), has(Blocks.AMETHYST_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST_BRICKS, 4).define('#', CUT_AMETHYST).pattern("##").pattern("##").unlockedBy(getHasName(CUT_AMETHYST), has(CUT_AMETHYST)).save(consumer);
		generateRecipes(consumer, CUT_AMETHYST_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CUT_AMETHYST_BRICKS_FAMILY, Blocks.AMETHYST_BLOCK, CUT_AMETHYST, CUT_AMETHYST_BRICKS);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST, Blocks.AMETHYST_BLOCK);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.ECHO_SHARD, BUILDING_BLOCKS, ECHO_BLOCK, "echo_shard_from_echo_block", "echo_shard");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLESTONE_BRICKS, 4).define('#', Blocks.COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE)).save(consumer);
		generateRecipes(consumer, COBBLESTONE_BRICKS_FAMILY);
		generateRecipes(consumer, COBBLESTONE_TILES_FAMILY);
		stonecutterRecipes(consumer, COBBLESTONE_BRICKS_FAMILY, Blocks.COBBLESTONE, COBBLESTONE_BRICKS);
		stonecutterRecipes(consumer, COBBLESTONE_TILES_FAMILY, Blocks.COBBLESTONE, COBBLESTONE_BRICKS, COBBLESTONE_TILES);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS, 4).define('#', Blocks.MOSSY_COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.MOSSY_COBBLESTONE), has(Blocks.MOSSY_COBBLESTONE)).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_BRICKS_FAMILY);
		generateRecipes(consumer, MOSSY_COBBLESTONE_TILES_FAMILY);
		stonecutterRecipes(consumer, MOSSY_COBBLESTONE_BRICKS_FAMILY, Blocks.MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_BRICKS);
		stonecutterRecipes(consumer, MOSSY_COBBLESTONE_TILES_FAMILY, Blocks.MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_BRICKS, MOSSY_COBBLESTONE_TILES);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS).requires(COBBLESTONE_BRICKS).requires(Blocks.VINE).group("mossy_cobblestone_bricks").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS, Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES).requires(COBBLESTONE_TILES).requires(Blocks.VINE).group("mossy_cobblestone_tiles").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES, Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS).requires(COBBLESTONE_BRICKS).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_bricks").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS, Blocks.MOSS_BLOCK));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES).requires(COBBLESTONE_TILES).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_tiles").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES, Blocks.MOSS_BLOCK));

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE, 4).define('#', Blocks.DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.DEEPSLATE), has(Blocks.DEEPSLATE)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICKS, 4).define('#', Blocks.COBBLED_DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE)).save(consumer);
		generateRecipes(consumer, COBBLED_DEEPSLATE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, COBBLED_DEEPSLATE_BRICKS_FAMILY, Blocks.COBBLED_DEEPSLATE, COBBLED_DEEPSLATE_BRICKS);

		chiseledBuilder(BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Ingredient.of(Blocks.DEEPSLATE_BRICK_SLAB)).unlockedBy("has_deepslate_brick_slab", has(Blocks.DEEPSLATE_BRICK_SLAB)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS);

		generateRecipes(consumer, COBBLED_DEEPSLATE_TILES_FAMILY);
		stonecutterRecipes(consumer, COBBLED_DEEPSLATE_TILES_FAMILY, Blocks.COBBLED_DEEPSLATE, COBBLED_DEEPSLATE_BRICKS, COBBLED_DEEPSLATE_TILES);

		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.DEEPSLATE, 2, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, DECORATIONS, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE, 2, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, DECORATIONS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE, 2, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, DECORATIONS, Blocks.DEEPSLATE_TILE_WALL, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);

		generateRecipes(consumer, AZALEA_PLANKS_FAMILY);
		planksFromLogs(consumer, AZALEA_PLANKS, CCItemTags.AZALEA_LOGS, 4);
		woodFromLogs(consumer, AZALEA_WOOD, AZALEA_LOG);
		woodFromLogs(consumer, STRIPPED_AZALEA_WOOD, STRIPPED_AZALEA_LOG);
		hangingSign(consumer, AZALEA_HANGING_SIGNS.getFirst(), STRIPPED_AZALEA_LOG);
		BoatloadRecipeProvider.boatRecipes(consumer, CCBoatTypes.AZALEA);
		WoodworksRecipeProvider.baseRecipes(consumer, AZALEA_PLANKS, AZALEA_SLAB, AZALEA_BOARDS, AZALEA_BOOKSHELF, CHISELED_AZALEA_BOOKSHELF, AZALEA_LADDER, AZALEA_BEEHIVE, AZALEA_CHEST, TRAPPED_AZALEA_CHEST, CavernsAndChasms.MOD_ID);
		WoodworksRecipeProvider.sawmillRecipes(consumer, AZALEA_PLANKS_FAMILY, CCItemTags.AZALEA_LOGS, AZALEA_BOARDS, AZALEA_LADDER, CavernsAndChasms.MOD_ID);

		conversionRecipe(consumer, Items.YELLOW_DYE, FALSE_HOPE, "yellow_dye");
		conversionRecipe(consumer, Items.GREEN_DYE, MOSCHATEL, "green_dye");

		trimRecipes(consumer, CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.NETHERRACK);
		trimRecipes(consumer, CCItems.FORGER_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.RIM_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.PLATE_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.CORE_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.COAL_BLOCK);

		modifierRecipe(consumer, Items.GLOW_INK_SAC, "emissive");
		modifierRecipe(consumer, Items.PRISMARINE_SHARD, "pulse");
		modifierRecipe(consumer, CCItems.SPINEL, "faded");
		copySmithingTemplate(consumer, CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE, CCItemTags.GEMS_TURQUOISE);

		ccWaxRecipes(consumer, provider);
	}

	public static void copperGearRecipes(RecipeOutput consumer, ItemLike ingot, ItemLike block, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike sword, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe) {
		ShapedRecipeBuilder.shaped(COMBAT, boots).define('X', block).pattern("X X").pattern("X X").group(getItemName(boots)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, chestplate).define('X', block).pattern("X X").pattern("XXX").pattern("XXX").group(getItemName(chestplate)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, helmet).define('X', block).pattern("XXX").pattern("X X").group(getItemName(helmet)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, leggings).define('X', block).pattern("XXX").pattern("X X").pattern("X X").group(getItemName(leggings)).unlockedBy("has_copper_block", has(block)).save(consumer);

		ShapedRecipeBuilder.shaped(TOOLS, axe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XX").pattern("X#").pattern(" #").group(getItemName(axe)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, hoe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XX").pattern(" #").pattern(" #").group(getItemName(hoe)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, pickaxe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XXX").pattern(" # ").pattern(" # ").group(getItemName(pickaxe)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, shovel).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("X").pattern("#").pattern("#").group(getItemName(shovel)).unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, sword).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("X").pattern("X").pattern("#").group(getItemName(sword)).unlockedBy("has_copper_block", has(block)).save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(pickaxe, shovel, axe, hoe, sword, helmet, chestplate, leggings, boots), MISC, ingot, 0.1F, 200).unlockedBy(getHasName(pickaxe), has(pickaxe)).unlockedBy(getHasName(shovel), has(shovel)).unlockedBy(getHasName(axe), has(axe)).unlockedBy(getHasName(hoe), has(hoe)).unlockedBy(getHasName(sword), has(sword)).unlockedBy(getHasName(helmet), has(helmet)).unlockedBy(getHasName(chestplate), has(chestplate)).unlockedBy(getHasName(leggings), has(leggings)).unlockedBy(getHasName(boots), has(boots)).save(consumer, CavernsAndChasms.location(getSmeltingRecipeName(ingot)));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(pickaxe, shovel, axe, hoe, sword, helmet, chestplate, leggings, boots), MISC, ingot, 0.1F, 100).unlockedBy(getHasName(pickaxe), has(pickaxe)).unlockedBy(getHasName(shovel), has(shovel)).unlockedBy(getHasName(axe), has(axe)).unlockedBy(getHasName(hoe), has(hoe)).unlockedBy(getHasName(sword), has(sword)).unlockedBy(getHasName(helmet), has(helmet)).unlockedBy(getHasName(chestplate), has(chestplate)).unlockedBy(getHasName(leggings), has(leggings)).unlockedBy(getHasName(boots), has(boots)).save(consumer, CavernsAndChasms.location(getBlastingRecipeName(ingot)));
	}

	public void copperIngotRecipes(RecipeOutput consumer, ItemLike ingot, ItemLike block, ItemLike door, ItemLike trapdoor, ItemLike bars, ItemLike button, ItemLike lightningRod, ItemLike floodlight, ItemLike toolbox) {
		if (ingot != Items.COPPER_INGOT) {
			storageRecipesWithCustomUnpacking(consumer, MISC, ingot, BUILDING_BLOCKS, block, getSimpleRecipeName(ingot), getItemName(ingot));
			ShapedRecipeBuilder.shaped(REDSTONE, lightningRod).define('#', ingot).pattern("#").pattern("#").pattern("#").group(getItemName(lightningRod)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		}

		doorBuilder(door, Ingredient.of(ingot)).group(getItemName(door)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, trapdoor).define('#', ingot).pattern("##").pattern("##").group(getItemName(trapdoor)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, bars, 16).define('#', ingot).pattern("###").pattern("###").group(getItemName(bars)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapelessRecipeBuilder.shapeless(REDSTONE, button).requires(ItemTags.WOODEN_BUTTONS).requires(ingot).group(getItemName(button)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, floodlight).define('C', ingot).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").group(getItemName(floodlight)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, toolbox).define('C', block).define('I', ingot).pattern(" I ").pattern("I I").pattern("CCC").group(getItemName(toolbox)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
	}

	public void stonecutterRecipe(RecipeOutput consumer, RecipeCategory category, ItemLike output, ItemLike input, ItemLike inputName) {
		stonecutterRecipe(consumer, category, output, input, 1, inputName);
	}

	public void stonecutterRecipe(RecipeOutput consumer, RecipeCategory category, ItemLike output, ItemLike input, int count, ItemLike inputName) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input)).save(consumer, getConversionRecipeName(output, inputName) + "_stonecutting");
	}

	public void stonecutterRecipes(RecipeOutput consumer, BlockFamily family) {
		stonecutterRecipes(consumer, family, family.getBaseBlock());
	}

	public void stonecutterRecipes(RecipeOutput consumer, BlockFamily family, ItemLike... inputs) {
		for (ItemLike input : inputs) {
			if (family.getBaseBlock().asItem() != input.asItem()) {
				stonecutterRecipe(consumer, BUILDING_BLOCKS, family.getBaseBlock(), input);
			}

			if (family.getVariants().containsKey(Variant.STAIRS)) {
				stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.STAIRS), input);
			}

			if (family.getVariants().containsKey(Variant.SLAB)) {
				stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.SLAB), input, 2);
			}

			if (family.getVariants().containsKey(Variant.WALL)) {
				stonecutterRecipe(consumer, DECORATIONS, family.get(Variant.WALL), input);
			}

			if (family.getVariants().containsKey(Variant.CHISELED)) {
				stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.CHISELED), input);
			}
		}
	}

	protected void necromiumSmithingRecipe(RecipeOutput consumer, ItemLike input, RecipeCategory category, ItemLike output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(CCItemTags.INGOTS_NECROMIUM), category, output.asItem()).unlocks("has_necromium_ingot", has(CCItemTags.INGOTS_NECROMIUM)).save(consumer, ResourceLocation.fromNamespaceAndPath(this.getModID(), getItemName(output) + "_smithing"));
	}

	public static void modifierRecipe(RecipeOutput consumer, ItemLike addition, String name) {
		SmithingModifierRecipeBuilder.smithingModifier(Ingredient.of(CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(addition), MISC).unlocks("has_smithing_modifier_template", has(CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE)).save(consumer, suffix(RecipeBuilder.getDefaultRecipeId(CCItems.TRIM_MODIFIER_SMITHING_TEMPLATE), "_smithing_" + name));
	}

	protected static void sparkler(RecipeOutput consumer, ItemLike dyedSparkler, ItemLike dye) {
		ShapedRecipeBuilder.shaped(DECORATIONS, dyedSparkler, 8).define('#', SPARKLER.getFirst()).define('X', dye).pattern("###").pattern("#X#").pattern("###").group("dyed_sparkler").unlockedBy("has_sparkler", has(SPARKLER.getFirst())).save(consumer);
	}

	public static void copperHornRecipe(RecipeOutput consumer, Provider provider, ResourceKey<Instrument> input, ImmutableList<DeferredHolder<Instrument, ?>> output) {
		String harmonyID = output.get(0).getId().getPath();
		String melodyID = output.get(1).getId().getPath();
		String bassID = output.get(2).getId().getPath();

		ItemStack inputStack = new ItemStack(Items.GOAT_HORN);
		inputStack.set(DataComponents.INSTRUMENT, provider.holderOrThrow(input));

		ItemStack outputStack = new ItemStack(CCItems.COPPER_HORN.get());
		outputStack.set(CCDataComponents.HARMONY_INSTRUMENT, output.get(0));
		outputStack.set(CCDataComponents.MELODY_INSTRUMENT, output.get(1));
		outputStack.set(CCDataComponents.BASS_INSTRUMENT, output.get(2));

		String recipeName = (harmonyID + melodyID + bassID).replace("copper_horn", "");
		ShapedRecipeBuilder.shaped(TOOLS, outputStack).define('#', DataComponentIngredient.of(false, inputStack)).define('C', Tags.Items.INGOTS_COPPER).pattern("C#C").pattern(" C ").unlockedBy("has_goat_horn", has(Items.GOAT_HORN)).group("copper_horn").save(consumer, CavernsAndChasms.location(recipeName + "copper_horn"));
	}

	public static void lampRecipe(RecipeOutput consumer, ItemLike output, TagKey<Item> input) {
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, output).define('#', input).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
	}

	public void platedBricksRecipe(RecipeOutput consumer, ItemLike block, ItemLike ingot) {
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, block, 4).define('#', ingot).define('X', Blocks.DEEPSLATE).pattern("#X").pattern("X#").group(getItemName(block)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
	}

	public void platedBricksRecipe(RecipeOutput consumer, ItemLike block, TagKey<Item> ingotTag, String hasName) {
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, block, 4).define('#', ingotTag).define('X', Blocks.DEEPSLATE).pattern("#X").pattern("X#").group(getItemName(block)).unlockedBy("has_" + hasName, has(ingotTag)).save(consumer);
	}

	public void platedBricksRecipes(RecipeOutput consumer, BlockFamily family) {
		generateRecipes(consumer, family);
		stonecutterRecipes(consumer, family);
	}

	protected static void copperBulb(RecipeOutput consumer, ItemLike bulbBlock, ItemLike material) {
		ShapedRecipeBuilder.shaped(REDSTONE, bulbBlock, 4).define('C', material).define('R', Items.REDSTONE).define('B', Items.BLAZE_ROD).pattern(" C ").pattern("CBC").pattern(" R ").group(getItemName(bulbBlock)).unlockedBy(getHasName(material), has(material)).save(consumer);
	}

	public static void mimingRecipe(RecipeOutput consumer, RecipeCategory category, ItemLike input, ItemLike output) {
		mimingRecipeBuilder(category, Ingredient.of(input), output, 1).unlockedBy(getHasName(input), has(input)).save(consumer);
	}

	protected void ccWaxRecipes(RecipeOutput consumer, Provider provider) {
		RegistryLookup<Block> blocks = provider.lookupOrThrow(Registries.BLOCK);
		blocks.listElementIds().forEach(id -> {
			Waxable waxable = blocks.getData(NeoForgeDataMaps.WAXABLES, id);
			if (waxable != null) {
				Block base = BuiltInRegistries.BLOCK.get(id);
				Block waxed = waxable.waxed();
				if (BuiltInRegistries.BLOCK.getKey(waxable.waxed()).getNamespace().equals(this.getModID())) {
					RecipeCategory category = (waxed instanceof BaseRailBlock || waxed instanceof IronBarsBlock || waxed instanceof FloodlightBlock || waxed instanceof LightningRodBlock) ? DECORATIONS : waxed instanceof ButtonBlock ? REDSTONE : BUILDING_BLOCKS;
					ShapelessRecipeBuilder.shapeless(category, waxed).requires(base).requires(CCItemTags.WAX).group(getItemName(waxed)).unlockedBy(getHasName(base), has(base)).save(consumer, getModConversionRecipeName(waxed, Items.HONEYCOMB));
				} else {
					ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, waxed).requires(base).requires(CCItemTags.WAX).group(getItemName(waxed)).unlockedBy(getHasName(base), has(base)).save(consumer, getConversionRecipeName(waxed, Items.HONEYCOMB));
				}
			}
		});

		WeatheringCopperItem.WAXABLES.get().forEach((base, waxed) -> {
			if (!(base instanceof WeatheringCopperItem)) {
				ShapelessRecipeBuilder.shapeless(MISC, waxed).requires(base).requires(CCItemTags.WAX).group(getItemName(waxed)).unlockedBy(getHasName(base), has(base)).save(consumer, getModConversionRecipeName(waxed, Items.HONEYCOMB));
			}
		});
	}

	@Override
	public void smeltingRecipe(RecipeOutput recipeOutput, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float xp, int cookTime, String group) {
		for (ItemLike item : inputs) {
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(item), category, output, xp, cookTime)
					.unlockedBy(getHasName(item), has(item))
					.group(group)
					.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(this.getModID(), getItemName(output) + "_from_smelting_" + getItemName(item)));
		}
	}

	@Override
	public void blastingRecipe(RecipeOutput recipeOutput, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float xp, int cookTime, String group) {
		for (ItemLike item : inputs) {
			SimpleCookingRecipeBuilder.blasting(Ingredient.of(item), category, output, xp, cookTime)
					.unlockedBy(getHasName(item), has(item))
					.group(group)
					.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(this.getModID(), getItemName(output) + "_from_blasting_" + getItemName(item)));
		}
	}

	public static SingleItemRecipeBuilder mimingRecipeBuilder(RecipeCategory category, Ingredient input, ItemLike output, int count) {
		return new SingleItemRecipeBuilder(category, MimingRecipe::new, input, output, count);
	}
}