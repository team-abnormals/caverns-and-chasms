package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.boatload.core.data.server.BoatloadRecipeProvider;
import com.teamabnormals.caverns_and_chasms.common.block.FloodlightBlock;
import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.item.copper.CopperHornItem;
import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.common.recipe.CCShapedRecipeBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCInstruments;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.integration.boatload.CCBoatTypes;
import com.teamabnormals.clayworks.core.data.server.ClayworksRecipeProvider;
import com.teamabnormals.woodworks.core.data.server.WoodworksRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

import static com.teamabnormals.caverns_and_chasms.core.other.CCBlockFamilies.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static net.minecraft.data.recipes.RecipeCategory.*;

public class CCRecipeProvider extends BlueprintRecipeProvider {
	public static final ModLoadedCondition ENDERGETIC_LOADED = new ModLoadedCondition("endergetic");

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(SILVER_ORE.get(), DEEPSLATE_SILVER_ORE.get(), SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> TIN_SMELTABLES = ImmutableList.of(TIN_ORE.get(), DEEPSLATE_TIN_ORE.get(), CASSITERITE_TIN_ORE.get(), CCItems.RAW_TIN.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(SPINEL_ORE.get(), DEEPSLATE_SPINEL_ORE.get());
	private static final ImmutableList<ItemLike> TURQUOISE_SMELTABLES = ImmutableList.of(TURQUOISE_ORE.get(), DEEPSLATE_TURQUOISE_ORE.get());

	public CCRecipeProvider(PackOutput output) {
		super(CavernsAndChasms.MOD_ID, output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(TOOLS, Items.BUNDLE).define('R', Items.LEATHER).define('S', Items.STRING).pattern("S").pattern("R").unlockedBy("has_leather", has(Items.LEATHER)).save(consumer, CavernsAndChasms.location(getItemName(Items.BUNDLE)));
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.LODESTONE).define('S', Items.CHISELED_STONE_BRICKS).define('#', Tags.Items.INGOTS_IRON).pattern("SSS").pattern("S#S").pattern("SSS").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(consumer);

		ShapedRecipeBuilder.shaped(TRANSPORTATION, COPPER_RAIL.get(), 3).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_COPPER).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, COPPER_CHAIN.get()).define('#', CCItemTags.NUGGETS_COPPER).pattern("#").pattern("#").pattern("#").unlockedBy("has_copper_nugget", has(CCItemTags.NUGGETS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, COPPER_LANTERN.get()).define('#', Items.TORCH).define('X', CCItemTags.NUGGETS_COPPER).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper_nugget", has(CCItemTags.NUGGETS_COPPER)).unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

		copperHornRecipe(consumer, Instruments.PONDER_GOAT_HORN, CCInstruments.GREAT_SKY_FALLING_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.SING_GOAT_HORN, CCInstruments.OLD_HYMN_RESTING_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.SEEK_GOAT_HORN, CCInstruments.PURE_WATER_DESIRE_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.FEEL_GOAT_HORN, CCInstruments.HUMBLE_FIRE_MEMORY_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.ADMIRE_GOAT_HORN, CCInstruments.DRY_URGE_ANGER_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.CALL_GOAT_HORN, CCInstruments.CLEAR_TEMPER_JOURNEY_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.YEARN_GOAT_HORN, CCInstruments.FRESH_NEST_THOUGHT_COPPER_HORN);
		copperHornRecipe(consumer, Instruments.DREAM_GOAT_HORN, CCInstruments.SECRET_LAKE_TEAR_COPPER_HORN);
		copperHornRecipe(consumer, CCInstruments.FLY_GOAT_HORN.getKey(), CCInstruments.FEARLESS_RIVER_GIFT_COPPER_HORN);
		copperHornRecipe(consumer, CCInstruments.RESIST_GOAT_HORN.getKey(), CCInstruments.SWEET_MOON_LOVE_COPPER_HORN);

		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.RAIL, 3).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.ACTIVATOR_RAIL).define('#', Blocks.REDSTONE_TORCH).define('S', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).pattern("XSX").pattern("X#X").pattern("XSX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.DETECTOR_RAIL).define('R', Items.REDSTONE).define('#', Blocks.STONE_PRESSURE_PLATE).define('X', Tags.Items.NUGGETS_IRON).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.POWERED_RAIL).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_GOLD).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, SPIKED_RAIL.get()).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_SILVER).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, SLAUGHTER_RAIL.get()).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.NUGGETS_SILVER).define('Y', CCItemTags.INGOTS_SILVER).pattern("XYX").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, HALT_RAIL.get()).define('R', Items.REDSTONE).define('#', Tags.Items.RODS_WOODEN).define('X', Tags.Items.NUGGETS_IRON).define('Y', Tags.Items.INGOTS_IRON).pattern("XYX").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.CHAIN).define('#', Tags.Items.NUGGETS_IRON).pattern("#").pattern("#").pattern("#").unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FRAGILE_STONE.get(), 4).define('D', Blocks.STONE).define('G', Blocks.GRAVEL).pattern("DG").pattern("GD").unlockedBy("has_stone", has(Blocks.STONE)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FRAGILE_DEEPSLATE.get(), 4).define('D', Blocks.DEEPSLATE).define('G', Blocks.GRAVEL).pattern("DG").pattern("GD").unlockedBy("has_deepslate", has(Blocks.DEEPSLATE)).save(consumer);

		ShapedRecipeBuilder.shaped(MISC, CCItems.BEJEWELED_PEARL.get(), 2).define('P', Items.ENDER_PEARL).define('S', CCItemTags.GEMS_SPINEL).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(FOOD, CCItems.BEJEWELED_APPLE.get(), 2).define('A', Items.GOLDEN_APPLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, TMT.get(), 4).define('T', Items.TNT).define('S', CCItemTags.GEMS_SPINEL).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, SPLURTER.get()).define('T', CCItems.TIN_INGOT.get()).define('D', Items.DROPPER).pattern("TTT").pattern("TDT").pattern("TTT").unlockedBy("has_tin_ingot", has(CCItems.TIN_INGOT.get())).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, SCATTERER.get()).define('T', CCItems.TIN_INGOT.get()).define('D', Items.DISPENSER).pattern("TTT").pattern("TDT").pattern("TTT").unlockedBy("has_tin_ingot", has(CCItems.TIN_INGOT.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(TRANSPORTATION, CCItems.TMT_MINECART.get()).requires(TMT.get()).requires(Items.MINECART).unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapelessRecipeBuilder.shapeless(COMBAT, CCItems.BLUNT_ARROW.get(), 4).requires(Items.ARROW).requires(CCItemTags.GEMS_SPINEL).unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.LARGE_ARROW.get(), 4).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).define('Y', Items.FEATHER).pattern("X").pattern("#").pattern("Y").unlockedBy("has_feather", has(Items.FEATHER)).unlockedBy("has_silver", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.FOIL.get()).define('#', Tags.Items.RODS_WOODEN).define('S', CCItemTags.INGOTS_SILVER).define('L', Items.LEATHER).pattern("  L").pattern("SL ").pattern("#S ").unlockedBy("has_silver", has(CCItemTags.INGOTS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, DISMANTLING_TABLE.get()).define('T', Items.SMITHING_TABLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_smithing_table", has(Items.SMITHING_TABLE)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, BEJEWELED_ANVIL.get(), 2).define('T', Items.ANVIL).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_anvil", has(Items.ANVIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, ATONING_TABLE.get()).define('T', Items.ENCHANTING_TABLE).define('S', CCItemTags.GEMS_SPINEL).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).unlockedBy("has_enchanting_table", has(Items.ENCHANTING_TABLE)).save(consumer);

		ShapelessRecipeBuilder.shapeless(FOOD, CCItems.CAVIAR.get()).requires(Items.SALMON_BUCKET).requires(CCItems.TURQUOISE.get()).unlockedBy("has_turquoise", has(CCItems.TURQUOISE.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(TOOLS, CCItems.MONOCLE.get()).requires(Items.SPYGLASS).requires(CCItems.TURQUOISE.get()).unlockedBy("has_turquoise", has(CCItems.TURQUOISE.get())).save(consumer);
		conversionRecipe(consumer, Items.CYAN_DYE, CCItems.TURQUOISE.get(), "cyan_dye", 16);

		copperGearRecipes(consumer, Items.COPPER_INGOT, Blocks.COPPER_BLOCK, CCItems.COPPER_HELMET.get(), CCItems.COPPER_CHESTPLATE.get(), CCItems.COPPER_LEGGINGS.get(), CCItems.COPPER_BOOTS.get(), CCItems.COPPER_SWORD.get(), CCItems.COPPER_PICKAXE.get(), CCItems.COPPER_AXE.get(), CCItems.COPPER_SHOVEL.get(), CCItems.COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.EXPOSED_COPPER_INGOT.get(), Blocks.EXPOSED_COPPER, CCItems.EXPOSED_COPPER_HELMET.get(), CCItems.EXPOSED_COPPER_CHESTPLATE.get(), CCItems.EXPOSED_COPPER_LEGGINGS.get(), CCItems.EXPOSED_COPPER_BOOTS.get(), CCItems.EXPOSED_COPPER_SWORD.get(), CCItems.EXPOSED_COPPER_PICKAXE.get(), CCItems.EXPOSED_COPPER_AXE.get(), CCItems.EXPOSED_COPPER_SHOVEL.get(), CCItems.EXPOSED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.WEATHERED_COPPER_INGOT.get(), Blocks.WEATHERED_COPPER, CCItems.WEATHERED_COPPER_HELMET.get(), CCItems.WEATHERED_COPPER_CHESTPLATE.get(), CCItems.WEATHERED_COPPER_LEGGINGS.get(), CCItems.WEATHERED_COPPER_BOOTS.get(), CCItems.WEATHERED_COPPER_SWORD.get(), CCItems.WEATHERED_COPPER_PICKAXE.get(), CCItems.WEATHERED_COPPER_AXE.get(), CCItems.WEATHERED_COPPER_SHOVEL.get(), CCItems.WEATHERED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.OXIDIZED_COPPER_INGOT.get(), Blocks.OXIDIZED_COPPER, CCItems.OXIDIZED_COPPER_HELMET.get(), CCItems.OXIDIZED_COPPER_CHESTPLATE.get(), CCItems.OXIDIZED_COPPER_LEGGINGS.get(), CCItems.OXIDIZED_COPPER_BOOTS.get(), CCItems.OXIDIZED_COPPER_SWORD.get(), CCItems.OXIDIZED_COPPER_PICKAXE.get(), CCItems.OXIDIZED_COPPER_AXE.get(), CCItems.OXIDIZED_COPPER_SHOVEL.get(), CCItems.OXIDIZED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.WAXED_COPPER_INGOT.get(), Blocks.WAXED_COPPER_BLOCK, CCItems.WAXED_COPPER_HELMET.get(), CCItems.WAXED_COPPER_CHESTPLATE.get(), CCItems.WAXED_COPPER_LEGGINGS.get(), CCItems.WAXED_COPPER_BOOTS.get(), CCItems.WAXED_COPPER_SWORD.get(), CCItems.WAXED_COPPER_PICKAXE.get(), CCItems.WAXED_COPPER_AXE.get(), CCItems.WAXED_COPPER_SHOVEL.get(), CCItems.WAXED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.WAXED_EXPOSED_COPPER_INGOT.get(), Blocks.WAXED_EXPOSED_COPPER, CCItems.WAXED_EXPOSED_COPPER_HELMET.get(), CCItems.WAXED_EXPOSED_COPPER_CHESTPLATE.get(), CCItems.WAXED_EXPOSED_COPPER_LEGGINGS.get(), CCItems.WAXED_EXPOSED_COPPER_BOOTS.get(), CCItems.WAXED_EXPOSED_COPPER_SWORD.get(), CCItems.WAXED_EXPOSED_COPPER_PICKAXE.get(), CCItems.WAXED_EXPOSED_COPPER_AXE.get(), CCItems.WAXED_EXPOSED_COPPER_SHOVEL.get(), CCItems.WAXED_EXPOSED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.WAXED_WEATHERED_COPPER_INGOT.get(), Blocks.WAXED_WEATHERED_COPPER, CCItems.WAXED_WEATHERED_COPPER_HELMET.get(), CCItems.WAXED_WEATHERED_COPPER_CHESTPLATE.get(), CCItems.WAXED_WEATHERED_COPPER_LEGGINGS.get(), CCItems.WAXED_WEATHERED_COPPER_BOOTS.get(), CCItems.WAXED_WEATHERED_COPPER_SWORD.get(), CCItems.WAXED_WEATHERED_COPPER_PICKAXE.get(), CCItems.WAXED_WEATHERED_COPPER_AXE.get(), CCItems.WAXED_WEATHERED_COPPER_SHOVEL.get(), CCItems.WAXED_WEATHERED_COPPER_HOE.get());
		copperGearRecipes(consumer, CCItems.WAXED_OXIDIZED_COPPER_INGOT.get(), Blocks.WAXED_OXIDIZED_COPPER, CCItems.WAXED_OXIDIZED_COPPER_HELMET.get(), CCItems.WAXED_OXIDIZED_COPPER_CHESTPLATE.get(), CCItems.WAXED_OXIDIZED_COPPER_LEGGINGS.get(), CCItems.WAXED_OXIDIZED_COPPER_BOOTS.get(), CCItems.WAXED_OXIDIZED_COPPER_SWORD.get(), CCItems.WAXED_OXIDIZED_COPPER_PICKAXE.get(), CCItems.WAXED_OXIDIZED_COPPER_AXE.get(), CCItems.WAXED_OXIDIZED_COPPER_SHOVEL.get(), CCItems.WAXED_OXIDIZED_COPPER_HOE.get());

		copperIngotRecipes(consumer, Items.COPPER_INGOT, Blocks.COPPER_BLOCK, COPPER_DOOR.get(), COPPER_TRAPDOOR.get(), COPPER_BARS.get(), COPPER_BUTTON.get(), Blocks.LIGHTNING_ROD, FLOODLIGHT.get(), TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.EXPOSED_COPPER_INGOT.get(), Blocks.EXPOSED_COPPER, EXPOSED_COPPER_DOOR.get(), EXPOSED_COPPER_TRAPDOOR.get(), EXPOSED_COPPER_BARS.get(), EXPOSED_COPPER_BUTTON.get(), EXPOSED_LIGHTNING_ROD.get(), EXPOSED_FLOODLIGHT.get(), EXPOSED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.WEATHERED_COPPER_INGOT.get(), Blocks.WEATHERED_COPPER, WEATHERED_COPPER_DOOR.get(), WEATHERED_COPPER_TRAPDOOR.get(), WEATHERED_COPPER_BARS.get(), WEATHERED_COPPER_BUTTON.get(), WEATHERED_LIGHTNING_ROD.get(), WEATHERED_FLOODLIGHT.get(), WEATHERED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.OXIDIZED_COPPER_INGOT.get(), Blocks.OXIDIZED_COPPER, OXIDIZED_COPPER_DOOR.get(), OXIDIZED_COPPER_TRAPDOOR.get(), OXIDIZED_COPPER_BARS.get(), OXIDIZED_COPPER_BUTTON.get(), OXIDIZED_LIGHTNING_ROD.get(), OXIDIZED_FLOODLIGHT.get(), OXIDIZED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.WAXED_COPPER_INGOT.get(), Blocks.WAXED_COPPER_BLOCK, WAXED_COPPER_DOOR.get(), WAXED_COPPER_TRAPDOOR.get(), WAXED_COPPER_BARS.get(), WAXED_COPPER_BUTTON.get(), WAXED_LIGHTNING_ROD.get(), WAXED_FLOODLIGHT.get(), WAXED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.WAXED_EXPOSED_COPPER_INGOT.get(), Blocks.WAXED_EXPOSED_COPPER, WAXED_EXPOSED_COPPER_DOOR.get(), WAXED_EXPOSED_COPPER_TRAPDOOR.get(), WAXED_EXPOSED_COPPER_BARS.get(), WAXED_EXPOSED_COPPER_BUTTON.get(), WAXED_EXPOSED_LIGHTNING_ROD.get(), WAXED_EXPOSED_FLOODLIGHT.get(), WAXED_EXPOSED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.WAXED_WEATHERED_COPPER_INGOT.get(), Blocks.WAXED_WEATHERED_COPPER, WAXED_WEATHERED_COPPER_DOOR.get(), WAXED_WEATHERED_COPPER_TRAPDOOR.get(), WAXED_WEATHERED_COPPER_BARS.get(), WAXED_WEATHERED_COPPER_BUTTON.get(), WAXED_WEATHERED_LIGHTNING_ROD.get(), WAXED_WEATHERED_FLOODLIGHT.get(), WAXED_WEATHERED_TOOLBOX.get());
		copperIngotRecipes(consumer, CCItems.WAXED_OXIDIZED_COPPER_INGOT.get(), Blocks.WAXED_OXIDIZED_COPPER, WAXED_OXIDIZED_COPPER_DOOR.get(), WAXED_OXIDIZED_COPPER_TRAPDOOR.get(), WAXED_OXIDIZED_COPPER_BARS.get(), WAXED_OXIDIZED_COPPER_BUTTON.get(), WAXED_OXIDIZED_LIGHTNING_ROD.get(), WAXED_OXIDIZED_FLOODLIGHT.get(), WAXED_OXIDIZED_TOOLBOX.get());

		SpecialRecipeBuilder.special(CCRecipeSerializers.MUSIC_DISC_COPYING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":music_disc_copying");
		SpecialRecipeBuilder.special(CCRecipeSerializers.TOOLBOX_WAXING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":toolbox_waxing");
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.TUNING_FORK.get()).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.BAROMETER.get()).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCItems.OXIDIZED_COPPER_GOLEM.get(), CCItems.WAXED_OXIDIZED_COPPER_GOLEM.get());
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		mimingRecipe(consumer, MISC, Items.MUSIC_DISC_11, CCItems.MUSIC_DISC_EPILOGUE.get());
		mimingRecipe(consumer, MISC, Items.MOJANG_BANNER_PATTERN, CCItems.ABNORMALS_BANNER_PATTERN.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, ROCKY_DIRT.get(), 4).define('D', Blocks.DIRT).define('C', Blocks.COBBLESTONE).pattern("DC").pattern("CD").unlockedBy("has_dirt", has(Blocks.DIRT)).unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FLINT_BLOCK.get()).define('#', Items.FLINT).pattern("##").pattern("##").unlockedBy("has_flint", has(Items.FLINT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.ROOTED_DIRT).requires(Blocks.DIRT).requires(Blocks.HANGING_ROOTS).unlockedBy("has_hanging_roots", has(Blocks.HANGING_ROOTS)).save(consumer, CavernsAndChasms.location(getItemName(Blocks.ROOTED_DIRT)));

		ShapedRecipeBuilder.shaped(DECORATIONS, LAVA_LAMP.get()).define('G', Tags.Items.INGOTS_GOLD).define('B', BlueprintItemTags.BUCKETS_LAVA).pattern("GGG").pattern(" B ").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);

		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_COPPER.get(), Blocks.COPPER_BLOCK, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EXPOSED_CHISELED_COPPER.get(), Blocks.EXPOSED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WEATHERED_CHISELED_COPPER.get(), Blocks.WEATHERED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, OXIDIZED_CHISELED_COPPER.get(), Blocks.OXIDIZED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_CHISELED_COPPER.get(), Blocks.WAXED_COPPER_BLOCK, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_EXPOSED_CHISELED_COPPER.get(), Blocks.WAXED_EXPOSED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_WEATHERED_CHISELED_COPPER.get(), Blocks.WAXED_WEATHERED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_OXIDIZED_CHISELED_COPPER.get(), Blocks.WAXED_OXIDIZED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_COPPER.get(), Blocks.CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EXPOSED_CHISELED_COPPER.get(), Blocks.EXPOSED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WEATHERED_CHISELED_COPPER.get(), Blocks.WEATHERED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, OXIDIZED_CHISELED_COPPER.get(), Blocks.OXIDIZED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_CHISELED_COPPER.get(), Blocks.WAXED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_EXPOSED_CHISELED_COPPER.get(), Blocks.WAXED_EXPOSED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_WEATHERED_CHISELED_COPPER.get(), Blocks.WAXED_WEATHERED_CUT_COPPER, 1);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_OXIDIZED_CHISELED_COPPER.get(), Blocks.WAXED_OXIDIZED_CUT_COPPER, 1);
		grate(consumer, COPPER_GRATE.get(), Blocks.COPPER_BLOCK);
		grate(consumer, EXPOSED_COPPER_GRATE.get(), Blocks.EXPOSED_COPPER);
		grate(consumer, WEATHERED_COPPER_GRATE.get(), Blocks.WEATHERED_COPPER);
		grate(consumer, OXIDIZED_COPPER_GRATE.get(), Blocks.OXIDIZED_COPPER);
		grate(consumer, WAXED_COPPER_GRATE.get(), Blocks.WAXED_COPPER_BLOCK);
		grate(consumer, WAXED_EXPOSED_COPPER_GRATE.get(), Blocks.WAXED_EXPOSED_COPPER);
		grate(consumer, WAXED_WEATHERED_COPPER_GRATE.get(), Blocks.WAXED_WEATHERED_COPPER);
		grate(consumer, WAXED_OXIDIZED_COPPER_GRATE.get(), Blocks.WAXED_OXIDIZED_COPPER);
		copperBulb(consumer, COPPER_BULB.get(), Blocks.COPPER_BLOCK);
		copperBulb(consumer, EXPOSED_COPPER_BULB.get(), Blocks.EXPOSED_COPPER);
		copperBulb(consumer, WEATHERED_COPPER_BULB.get(), Blocks.WEATHERED_COPPER);
		copperBulb(consumer, OXIDIZED_COPPER_BULB.get(), Blocks.OXIDIZED_COPPER);
		copperBulb(consumer, WAXED_COPPER_BULB.get(), Blocks.WAXED_COPPER_BLOCK);
		copperBulb(consumer, WAXED_EXPOSED_COPPER_BULB.get(), Blocks.WAXED_EXPOSED_COPPER);
		copperBulb(consumer, WAXED_WEATHERED_COPPER_BULB.get(), Blocks.WAXED_WEATHERED_COPPER);
		copperBulb(consumer, WAXED_OXIDIZED_COPPER_BULB.get(), Blocks.WAXED_OXIDIZED_COPPER);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, COPPER_GRATE.get(), Blocks.COPPER_BLOCK, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EXPOSED_COPPER_GRATE.get(), Blocks.EXPOSED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WEATHERED_COPPER_GRATE.get(), Blocks.WEATHERED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, OXIDIZED_COPPER_GRATE.get(), Blocks.OXIDIZED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_COPPER_GRATE.get(), Blocks.WAXED_COPPER_BLOCK, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_EXPOSED_COPPER_GRATE.get(), Blocks.WAXED_EXPOSED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_WEATHERED_COPPER_GRATE.get(), Blocks.WAXED_WEATHERED_COPPER, 4);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, WAXED_OXIDIZED_COPPER_GRATE.get(), Blocks.WAXED_OXIDIZED_COPPER, 4);

		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_TORCH.get(), 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Tags.Items.RODS_WOODEN).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_CAMPFIRE.get()).define('L', ItemTags.LOGS).define('S', Tags.Items.RODS_WOODEN).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_LANTERN.get()).define('#', CUPRIC_TORCH.get()).define('X', Tags.Items.NUGGETS_IRON).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper", has(CUPRIC_TORCH.get())).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, BRAZIER.get()).define('#', ItemTags.COALS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_coal", has(ItemTags.COALS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SOUL_BRAZIER.get()).define('#', ItemTags.SOUL_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_BRAZIER.get()).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		conditionalRecipe(consumer, ENDERGETIC_LOADED, DECORATIONS, ShapedRecipeBuilder.shaped(DECORATIONS, ENDER_BRAZIER.get()).define('#', CCItemTags.ENDER_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_end_stone", has(CCItemTags.ENDER_FIRE_BASE_BLOCKS)));

		storageRecipes(consumer, MISC, Items.CHARCOAL, BUILDING_BLOCKS, CHARCOAL_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.SPINEL.get(), BUILDING_BLOCKS, SPINEL_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.RAW_SILVER.get(), BUILDING_BLOCKS, RAW_SILVER_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.RAW_TIN.get(), BUILDING_BLOCKS, RAW_TIN_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.ZIRCONIA.get(), BUILDING_BLOCKS, ZIRCONIA_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.TURQUOISE.get(), BUILDING_BLOCKS, TURQUOISE_BLOCK.get());
		storageRecipes(consumer, FOOD, Items.ROTTEN_FLESH, BUILDING_BLOCKS, ROTTEN_FLESH_BLOCK.get());
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.SILVER_INGOT.get(), BUILDING_BLOCKS, SILVER_BLOCK.get(), "silver_ingot_from_silver_block", "silver_ingot");
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.TIN_INGOT.get(), BUILDING_BLOCKS, TIN_BLOCK.get(), "tin_ingot_from_tin_block", "tin_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.SILVER_NUGGET.get(), MISC, CCItems.SILVER_INGOT.get(), "silver_ingot_from_nuggets", "silver_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.TIN_NUGGET.get(), MISC, CCItems.TIN_INGOT.get(), "tin_ingot_from_nuggets", "tin_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.COPPER_NUGGET.get(), MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.COPPER_NUGGET.get(), 9).requires(WAXED_COPPER_INGOT.get()).unlockedBy(getHasName(WAXED_COPPER_INGOT.get()), has(CCItems.WAXED_COPPER_INGOT.get())).save(consumer, getConversionRecipeName(CCItems.COPPER_NUGGET.get(), WAXED_COPPER_INGOT.get()));

		oreRecipes(consumer, SILVER_SMELTABLES, MISC, CCItems.SILVER_INGOT.get(), 1.0F, 200, 1.0F, 100, "silver_ingot");
		oreRecipes(consumer, TIN_SMELTABLES, MISC, CCItems.TIN_INGOT.get(), 0.7F, 200, 0.7F, 100, "tin_ingot");
		oreRecipes(consumer, SPINEL_SMELTABLES, MISC, CCItems.SPINEL.get(), 0.2F, 200, 0.2F, 100, "spinel");
		oreRecipes(consumer, TURQUOISE_SMELTABLES, MISC, CCItems.TURQUOISE.get(), 1.0F, 200, 1.0F, 100, "turquoise");

		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_AXE.get()).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_BOOTS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_CHESTPLATE.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_HELMET.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_HOE.get()).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_LEGGINGS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_PICKAXE.get()).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_SHOVEL.get()).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("#").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_SWORD.get()).define('#', Tags.Items.RODS_WOODEN).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("X").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		pressurePlateBuilder(REDSTONE, MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), Ingredient.of(CCItemTags.INGOTS_SILVER)).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SILVER_BARS.get(), 16).define('#', CCItemTags.INGOTS_SILVER).pattern("###").pattern("###").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 200).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, CavernsAndChasms.location(getSmeltingRecipeName(CCItems.SILVER_NUGGET.get())));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 100).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, CavernsAndChasms.location(getBlastingRecipeName(CCItems.SILVER_NUGGET.get())));
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.KUNAI.get(), 3).define('#', CCItemTags.NUGGETS_SILVER).define('S', CCItemTags.INGOTS_SILVER).pattern(" S").pattern("# ").unlockedBy("has_silver_nugget", has(CCItemTags.NUGGETS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(BREWING, Blocks.BREWING_STAND).define('B', Items.BLAZE_ROD).define('#', CCItemTags.INGOTS_SILVER).pattern(" B ").pattern("###").unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, GOLDEN_BARS.get(), 16).define('#', Items.GOLD_INGOT).pattern("###").pattern("###").unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(MISC, CCItems.GOLDEN_BUCKET.get()).define('#', Blocks.GOLD_BLOCK).pattern("# #").pattern(" # ").unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK)).save(consumer);
		conditionalRecipe(consumer, new NotCondition(new ModLoadedCondition("environmental")), FOOD, ShapedRecipeBuilder.shaped(FOOD, Blocks.CAKE).define('A', CCItems.GOLDEN_MILK_BUCKET.get()).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Items.EGG).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Items.EGG)), CavernsAndChasms.location(getSimpleRecipeName(Blocks.CAKE)));
		conditionalRecipe(consumer, new NotCondition(new TagEmptyCondition(CCItemTags.BOTTLES_MILK.location())), MISC, ShapelessRecipeBuilder.shapeless(MISC, CCItems.GOLDEN_MILK_BUCKET.get()).requires(CCItems.GOLDEN_BUCKET.get()).requires(Ingredient.of(CCItemTags.BOTTLES_MILK), 3).unlockedBy("has_milk_bottle", has(CCItemTags.BOTTLES_MILK)));

		ShapedRecipeBuilder.shaped(DECORATIONS, TIN_BARS.get(), 16).define('#', CCItemTags.INGOTS_TIN).pattern("###").pattern("###").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		pressurePlateBuilder(REDSTONE, HOLD_PLATE.get(), Ingredient.of(CCItemTags.INGOTS_TIN)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapelessRecipeBuilder.shapeless(REDSTONE, HOLD_BUTTON.get()).requires(ItemTags.WOODEN_BUTTONS).requires(CCItemTags.INGOTS_TIN).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, DIMMER.get()).define('T', CCItemTags.INGOTS_TIN).define('N', CCItemTags.NUGGETS_TIN).define('R', Items.REDSTONE_TORCH).pattern("NNN").pattern("NRN").pattern(" T ").unlockedBy("has_tin_nuggets", has(CCItemTags.NUGGETS_TIN)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, BOUNCER.get()).define('T', CCItemTags.STORAGE_BLOCKS_TIN).define('S', Items.SLIME_BALL).define('R', Tags.Items.DUSTS_REDSTONE).pattern("SRS").pattern("STS").pattern("SRS").unlockedBy("has_slime_ball", has(Items.SLIME_BALL)).unlockedBy("has_tin", has(CCItemTags.STORAGE_BLOCKS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, HOOP.get()).define('T', CCItemTags.INGOTS_TIN).define('Q', Items.QUARTZ).pattern("TTT").pattern("TQT").pattern("TTT").unlockedBy("has_quartz", has(Items.QUARTZ)).unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, STORAGE_DUCT.get()).define('I', CCItemTags.INGOTS_TIN).define('B', CCItemTags.STORAGE_BLOCKS_TIN).pattern("I I").pattern("B B").pattern("I I").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, STORAGE_DUCT_HATCH.get()).define('I', CCItemTags.INGOTS_TIN).pattern("I I").pattern("I I").pattern("I I").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FLOAT_GLASS.get(), 2).define('G', Blocks.GLASS).define('S', Items.AMETHYST_SHARD).define('T', CCItemTags.INGOTS_TIN).pattern(" T ").pattern("SGS").pattern(" T ").unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).unlockedBy("has_tin", has(CCItemTags.INGOTS_TIN)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, FLOAT_GLASS_PANE.get(), 16).define('#', FLOAT_GLASS.get()).pattern("###").pattern("###").unlockedBy("has_float_glass", has(FLOAT_GLASS.get())).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, ROLLER_DOOR.get(), 2).define('T', CCItemTags.INGOTS_TIN).pattern("TT").pattern("TT").unlockedBy("has_tin_ingot", has(CCItemTags.INGOTS_TIN)).save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RESISTOR.get()).define('#', Blocks.REDSTONE_TORCH).define('X', Items.REDSTONE).define('I', CCItemTags.INGOTS_TIN).pattern("#X#").pattern("III").unlockedBy("has_redstone_torch", has(Blocks.REDSTONE_TORCH)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, REFRACTOR.get()).define('#', Blocks.REDSTONE_TORCH).define('X', Items.AMETHYST_BLOCK).define('I', CCItemTags.INGOTS_TIN).pattern(" # ").pattern("#X#").pattern("III").unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK)).save(consumer);

		ShapelessRecipeBuilder.shapeless(MISC, CCItems.LIVING_FLESH.get(), 2).requires(Items.ROTTEN_FLESH, 3).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 2).requires(Items.GHAST_TEAR, 2).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_HELMET.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("XXX").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_CHESTPLATE.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_LEGGINGS.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_BOOTS.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.LIVING_FLESH.get(), BUILDING_BLOCKS, SANGUINE_BLOCK.get(), "living_flesh_from_sanguine_block", "living_flesh");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SANGUINE_TILES.get(), 8).define('X', CCItems.LIVING_FLESH.get()).define('#', Blocks.NETHERRACK).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILES.get(), 8).define('X', CCItemTags.INGOTS_SILVER).define('#', SANGUINE_TILES.get()).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_sanguine_tiles", has(SANGUINE_TILES.get())).save(consumer);
		generateRecipes(consumer, SANGUINE_TILES_FAMILY);
		generateRecipes(consumer, FORTIFIED_SANGUINE_TILES_FAMILY);
		stonecutterRecipes(consumer, SANGUINE_TILES_FAMILY);
		stonecutterRecipes(consumer, FORTIFIED_SANGUINE_TILES_FAMILY);

		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.NECROMIUM_INGOT.get(), BUILDING_BLOCKS, NECROMIUM_BLOCK.get(), "necromium_ingot_from_necromium_block", "necromium_ingot");
		ShapelessRecipeBuilder.shapeless(MISC, CCItems.NECROMIUM_INGOT.get()).requires(Items.NETHERITE_SCRAP, 4).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 4).group("necromium_ingot").unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(consumer);
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.NECROMIUM_NUGGET.get(), MISC, CCItems.NECROMIUM_INGOT.get(), "necromium_ingot_from_nuggets", "necromium_ingot");
		necromiumSmithingRecipe(consumer, Items.DIAMOND_CHESTPLATE, COMBAT, CCItems.NECROMIUM_CHESTPLATE.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_LEGGINGS, COMBAT, CCItems.NECROMIUM_LEGGINGS.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HELMET, COMBAT, CCItems.NECROMIUM_HELMET.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_BOOTS, COMBAT, CCItems.NECROMIUM_BOOTS.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_SWORD, COMBAT, CCItems.NECROMIUM_SWORD.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_AXE, TOOLS, CCItems.NECROMIUM_AXE.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_PICKAXE, TOOLS, CCItems.NECROMIUM_PICKAXE.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HOE, TOOLS, CCItems.NECROMIUM_HOE.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_SHOVEL, TOOLS, CCItems.NECROMIUM_SHOVEL.get());
		necromiumSmithingRecipe(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NECROMIUM_HORSE_ARMOR.get());
		netheriteSmithingRecipe(consumer, Items.DIAMOND_HORSE_ARMOR, COMBAT, CCItems.NETHERITE_HORSE_ARMOR.get());

		storageRecipesWithCustomPacking(consumer, MISC, CCItems.NETHERITE_NUGGET.get(), MISC, Items.NETHERITE_INGOT, "netherite_ingot_from_nuggets", "netherite_ingot");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, LAPIS_LAZULI_BRICKS.get()).define('#', Items.LAPIS_LAZULI).pattern("##").pattern("##").unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(consumer);
		generateRecipes(consumer, LAPIS_LAZULI_BRICKS_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, LAPIS_LAZULI_PILLAR.get(), 2).define('#', LAPIS_LAZULI_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(LAPIS_LAZULI_BRICKS.get()), has(LAPIS_LAZULI_BRICKS.get())).unlockedBy(getHasName(LAPIS_LAZULI_PILLAR.get()), has(LAPIS_LAZULI_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, LAPIS_LAZULI_LAMP.get()).define('#', Items.LAPIS_LAZULI).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		stonecutterRecipes(consumer, LAPIS_LAZULI_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, LAPIS_LAZULI_PILLAR.get(), LAPIS_LAZULI_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_BRICKS.get()).define('#', CCItemTags.GEMS_SPINEL).pattern("##").pattern("##").unlockedBy(getHasName(CCItems.SPINEL.get()), has(CCItemTags.GEMS_SPINEL)).save(consumer);
		generateRecipes(consumer, SPINEL_BRICKS_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_PILLAR.get(), 2).define('#', SPINEL_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(SPINEL_BRICKS.get()), has(SPINEL_BRICKS.get())).unlockedBy(getHasName(SPINEL_PILLAR.get()), has(SPINEL_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_LAMP.get()).define('#', CCItemTags.GEMS_SPINEL).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		stonecutterRecipes(consumer, SPINEL_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SPINEL_PILLAR.get(), SPINEL_BRICKS.get());

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TURQUOISE_TILES.get(), 8).define('#', Blocks.STONE_BRICKS).define('S', CCItemTags.GEMS_TURQUOISE).pattern("###").pattern("#S#").pattern("###").unlockedBy("has_turquoise", has(CCItemTags.GEMS_TURQUOISE)).save(consumer);
		generateRecipes(consumer, TURQUOISE_TILES_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, TURQUOISE_PILLAR.get(), 2).define('#', TURQUOISE_TILES.get()).pattern("#").pattern("#").unlockedBy(getHasName(TURQUOISE_TILES.get()), has(TURQUOISE_TILES.get())).unlockedBy(getHasName(TURQUOISE_PILLAR.get()), has(TURQUOISE_PILLAR.get())).save(consumer);
		stonecutterRecipes(consumer, TURQUOISE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TURQUOISE_PILLAR.get(), TURQUOISE_TILES.get());

		platedBricksRecipe(consumer, IRON_BRICKS.get(), Tags.Items.INGOTS_IRON, "iron");
		platedBricksRecipes(consumer, IRON_BRICKS_FAMILY);

		platedBricksRecipe(consumer, TIN_BRICKS.get(), CCItemTags.INGOTS_TIN, "tin");
		platedBricksRecipes(consumer, TIN_BRICKS_FAMILY);

		platedBricksRecipe(consumer, GOLD_BRICKS.get(), Tags.Items.INGOTS_GOLD, "gold");
		platedBricksRecipes(consumer, GOLD_BRICKS_FAMILY);

		platedBricksRecipe(consumer, SILVER_BRICKS.get(), CCItemTags.INGOTS_SILVER, "silver");
		platedBricksRecipes(consumer, SILVER_BRICKS_FAMILY);

		platedBricksRecipe(consumer, COPPER_BRICKS.get(), Items.COPPER_INGOT);
		platedBricksRecipe(consumer, EXPOSED_COPPER_BRICKS.get(), EXPOSED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, WEATHERED_COPPER_BRICKS.get(), WEATHERED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, OXIDIZED_COPPER_BRICKS.get(), OXIDIZED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, WAXED_COPPER_BRICKS.get(), WAXED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, WAXED_EXPOSED_COPPER_BRICKS.get(), WAXED_EXPOSED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, WAXED_WEATHERED_COPPER_BRICKS.get(), WAXED_WEATHERED_COPPER_INGOT.get());
		platedBricksRecipe(consumer, WAXED_OXIDIZED_COPPER_BRICKS.get(), WAXED_OXIDIZED_COPPER_INGOT.get());
		platedBricksRecipes(consumer, COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, EXPOSED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WEATHERED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, OXIDIZED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_EXPOSED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_WEATHERED_COPPER_BRICKS_FAMILY);
		platedBricksRecipes(consumer, WAXED_OXIDIZED_COPPER_BRICKS_FAMILY);

		wall(consumer, DECORATIONS, STONE_WALL.get(), Blocks.STONE);
		wall(consumer, DECORATIONS, POLISHED_GRANITE_WALL.get(), Blocks.POLISHED_GRANITE);
		wall(consumer, DECORATIONS, POLISHED_DIORITE_WALL.get(), Blocks.POLISHED_DIORITE);
		wall(consumer, DECORATIONS, POLISHED_ANDESITE_WALL.get(), Blocks.POLISHED_ANDESITE);

		stonecutterRecipe(consumer, DECORATIONS, STONE_WALL.get(), Blocks.STONE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_GRANITE_WALL.get(), Blocks.POLISHED_GRANITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_GRANITE_WALL.get(), Blocks.GRANITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_DIORITE_WALL.get(), Blocks.POLISHED_DIORITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_DIORITE_WALL.get(), Blocks.DIORITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_ANDESITE_WALL.get(), Blocks.POLISHED_ANDESITE);
		stonecutterRecipe(consumer, DECORATIONS, POLISHED_ANDESITE_WALL.get(), Blocks.ANDESITE);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.CALCITE).requires(Blocks.DIORITE).requires(Items.AMETHYST_SHARD).unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(consumer, CavernsAndChasms.location(RecipeBuilder.getDefaultRecipeId(Blocks.CALCITE).getPath()));
		generateRecipes(consumer, CALCITE_FAMILY);
		generateRecipes(consumer, POLISHED_CALCITE_FAMILY);
		generateRecipes(consumer, CALCITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CALCITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_CALCITE_FAMILY, Blocks.CALCITE, POLISHED_CALCITE.get());
		stonecutterRecipes(consumer, CALCITE_BRICKS_FAMILY, Blocks.CALCITE, POLISHED_CALCITE.get(), CALCITE_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CALCITE_PILLAR.get(), 2).define('#', POLISHED_CALCITE.get()).pattern("#").pattern("#").unlockedBy(getHasName(POLISHED_CALCITE.get()), has(POLISHED_CALCITE.get())).unlockedBy(getHasName(CALCITE_PILLAR.get()), has(CALCITE_PILLAR.get())).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_PILLAR.get(), Blocks.CALCITE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_PILLAR.get(), POLISHED_CALCITE.get(), 2);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.CALCITE), RecipeCategory.BUILDING_BLOCKS, SMOOTH_CALCITE.get(), 0.1F, 200).unlockedBy("has_calcite", has(Blocks.CALCITE)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, Blocks.CALCITE, SMOOTH_CALCITE.get(), 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_CALCITE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_CALCITE_FAMILY);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.TUFF, 2).requires(Blocks.BASALT).requires(Blocks.COBBLESTONE).unlockedBy("has_stone", has(Blocks.BASALT)).save(consumer, CavernsAndChasms.location(RecipeBuilder.getDefaultRecipeId(Blocks.TUFF).getPath()));
		generateRecipes(consumer, TUFF_FAMILY);
		generateRecipes(consumer, POLISHED_TUFF_FAMILY);
		generateRecipes(consumer, TUFF_BRICKS_FAMILY);
		stonecutterRecipes(consumer, TUFF_FAMILY);
		stonecutterRecipes(consumer, POLISHED_TUFF_FAMILY, Blocks.TUFF, POLISHED_TUFF.get());
		stonecutterRecipes(consumer, TUFF_BRICKS_FAMILY, Blocks.TUFF, POLISHED_TUFF.get(), TUFF_BRICKS.get());

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.TUFF), RecipeCategory.BUILDING_BLOCKS, SMOOTH_TUFF.get(), 0.1F, 200).unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF, SMOOTH_TUFF.get(), 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_TUFF_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_TUFF_FAMILY);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, SUGILITE.get()).requires(Blocks.GRANITE).requires(CCItemTags.GEMS_SPINEL).unlockedBy("has_spinel", has(CCItemTags.GEMS_SPINEL)).save(consumer);
		generateRecipes(consumer, SUGILITE_FAMILY);
		generateRecipes(consumer, POLISHED_SUGILITE_FAMILY);
		stonecutterRecipes(consumer, SUGILITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_SUGILITE_FAMILY, SUGILITE.get(), POLISHED_SUGILITE.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, CASSITERITE.get()).requires(Blocks.GRANITE).requires(CCItemTags.RAW_MATERIALS_TIN).unlockedBy("has_raw_tin", has(CCItemTags.RAW_MATERIALS_TIN)).save(consumer);
		generateRecipes(consumer, CASSITERITE_FAMILY);
		generateRecipes(consumer, POLISHED_CASSITERITE_FAMILY);
		generateRecipes(consumer, CASSITERITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CASSITERITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_CASSITERITE_FAMILY, CASSITERITE.get(), POLISHED_CASSITERITE.get());
		stonecutterRecipes(consumer, CASSITERITE_BRICKS_FAMILY, CASSITERITE.get(), POLISHED_CASSITERITE.get(), CASSITERITE_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CASSITERITE_PILLAR.get(), 2).define('#', CASSITERITE_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(CASSITERITE_BRICKS.get()), has(CASSITERITE_BRICKS.get())).unlockedBy(getHasName(CASSITERITE_PILLAR.get()), has(CASSITERITE_PILLAR.get())).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR.get(), CASSITERITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR.get(), POLISHED_CASSITERITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CASSITERITE_PILLAR.get(), CASSITERITE_BRICKS.get(), 2);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CASSITERITE.get()), RecipeCategory.BUILDING_BLOCKS, SMOOTH_CASSITERITE.get(), 0.1F, 200).unlockedBy("has_cassiterite", has(CASSITERITE.get())).save(consumer);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CASSITERITE.get(), SMOOTH_CASSITERITE.get(), 0.1F, 100, CavernsAndChasms.MOD_ID);
		generateRecipes(consumer, SMOOTH_CASSITERITE_FAMILY);
		stonecutterRecipes(consumer, SMOOTH_CASSITERITE_FAMILY);

		generateRecipes(consumer, RHYOLITE_FAMILY);
		generateRecipes(consumer, POLISHED_RHYOLITE_FAMILY);
		generateRecipes(consumer, RHYOLITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, RHYOLITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_RHYOLITE_FAMILY, RHYOLITE.get(), POLISHED_RHYOLITE.get());
		stonecutterRecipes(consumer, RHYOLITE_BRICKS_FAMILY, RHYOLITE.get(), POLISHED_RHYOLITE.get(), RHYOLITE_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MAGMATIC_RHYOLITE.get(), 4).define('#', RHYOLITE.get()).define('X', Blocks.MAGMA_BLOCK).pattern("#X").pattern("X#").unlockedBy("has_magma_block", has(Blocks.MAGMA_BLOCK)).save(consumer);
		generateRecipes(consumer, MAGMATIC_RHYOLITE_FAMILY);
		generateRecipes(consumer, POLISHED_MAGMATIC_RHYOLITE_FAMILY);
		generateRecipes(consumer, MAGMATIC_RHYOLITE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, MAGMATIC_RHYOLITE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_MAGMATIC_RHYOLITE_FAMILY, MAGMATIC_RHYOLITE.get(), POLISHED_MAGMATIC_RHYOLITE.get());
		stonecutterRecipes(consumer, MAGMATIC_RHYOLITE_BRICKS_FAMILY, MAGMATIC_RHYOLITE.get(), POLISHED_MAGMATIC_RHYOLITE.get(), MAGMATIC_RHYOLITE_BRICKS.get());

		generateRecipes(consumer, DRIPSTONE_FAMILY);
		generateRecipes(consumer, POLISHED_DRIPSTONE_FAMILY);
		generateRecipes(consumer, DRIPSTONE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, DRIPSTONE_FAMILY);
		stonecutterRecipes(consumer, POLISHED_DRIPSTONE_FAMILY, Blocks.DRIPSTONE_BLOCK, POLISHED_DRIPSTONE.get());
		stonecutterRecipes(consumer, DRIPSTONE_BRICKS_FAMILY, Blocks.DRIPSTONE_BLOCK, POLISHED_DRIPSTONE.get(), DRIPSTONE_BRICKS.get());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DRIPSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, CRACKED_DRIPSTONE_BRICKS.get(), 0.1F, 200).unlockedBy("has_dripstone_bricks", has(DRIPSTONE_BRICKS.get())).save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.DRIPSTONE_BLOCK), RecipeCategory.BUILDING_BLOCKS, SMOOTH_DRIPSTONE.get(), 0.1F, 200).unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		stonecutterRecipes(consumer, SMOOTH_DRIPSTONE_FAMILY);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, DRIPSTONE_SHINGLES.get()).define('#', DRIPSTONE_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES_FAMILY);
		stonecutterRecipes(consumer, DRIPSTONE_SHINGLES_FAMILY, Blocks.DRIPSTONE_BLOCK, DRIPSTONE_SHINGLES.get());
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, FLOODED_DRIPSTONE_SHINGLES.get(), 8).requires(BlueprintItemTags.BUCKETS_WATER).requires(DRIPSTONE_SHINGLES.get(), 8).unlockedBy("has_dripstone_shingles", has(DRIPSTONE_SHINGLES.get())).save(consumer);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.AMETHYST_SHARD, BUILDING_BLOCKS, AMETHYST_BLOCK.get(), "amethyst_from_amethyst_block", "amethyst_shard");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST.get(), 4).define('#', Blocks.AMETHYST_BLOCK).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.AMETHYST_BLOCK), has(Blocks.AMETHYST_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST_BRICKS.get(), 4).define('#', CUT_AMETHYST.get()).pattern("##").pattern("##").unlockedBy(getHasName(CUT_AMETHYST.get()), has(CUT_AMETHYST.get())).save(consumer);
		generateRecipes(consumer, CUT_AMETHYST_BRICKS_FAMILY);
		stonecutterRecipes(consumer, CUT_AMETHYST_BRICKS_FAMILY, Blocks.AMETHYST_BLOCK, CUT_AMETHYST.get(), CUT_AMETHYST_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST.get(), Blocks.AMETHYST_BLOCK);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.ECHO_SHARD, BUILDING_BLOCKS, ECHO_BLOCK.get(), "echo_shard_from_echo_block", "echo_shard");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE)).save(consumer);
		generateRecipes(consumer, COBBLESTONE_BRICKS_FAMILY);
		generateRecipes(consumer, COBBLESTONE_TILES_FAMILY);
		stonecutterRecipes(consumer, COBBLESTONE_BRICKS_FAMILY, Blocks.COBBLESTONE, COBBLESTONE_BRICKS.get());
		stonecutterRecipes(consumer, COBBLESTONE_TILES_FAMILY, Blocks.COBBLESTONE, COBBLESTONE_BRICKS.get(), COBBLESTONE_TILES.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.MOSSY_COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.MOSSY_COBBLESTONE), has(Blocks.MOSSY_COBBLESTONE)).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_BRICKS_FAMILY);
		generateRecipes(consumer, MOSSY_COBBLESTONE_TILES_FAMILY);
		stonecutterRecipes(consumer, MOSSY_COBBLESTONE_BRICKS_FAMILY, Blocks.MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipes(consumer, MOSSY_COBBLESTONE_TILES_FAMILY, Blocks.MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_BRICKS.get(), MOSSY_COBBLESTONE_TILES.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get()).requires(COBBLESTONE_BRICKS.get()).requires(Blocks.VINE).group("mossy_cobblestone_bricks").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get()).requires(COBBLESTONE_TILES.get()).requires(Blocks.VINE).group("mossy_cobblestone_tiles").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get()).requires(COBBLESTONE_BRICKS.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_bricks").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSS_BLOCK));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get()).requires(COBBLESTONE_TILES.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_tiles").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSS_BLOCK));

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE, 4).define('#', Blocks.DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.DEEPSLATE), has(Blocks.DEEPSLATE)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICKS.get(), 4).define('#', Blocks.COBBLED_DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE)).save(consumer);
		generateRecipes(consumer, COBBLED_DEEPSLATE_BRICKS_FAMILY);
		stonecutterRecipes(consumer, COBBLED_DEEPSLATE_BRICKS_FAMILY, Blocks.COBBLED_DEEPSLATE, COBBLED_DEEPSLATE_BRICKS.get());

		chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Ingredient.of(Blocks.DEEPSLATE_BRICK_SLAB)).unlockedBy("has_deepslate_brick_slab", has(Blocks.DEEPSLATE_BRICK_SLAB)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS);

		generateRecipes(consumer, COBBLED_DEEPSLATE_TILES_FAMILY);
		stonecutterRecipes(consumer, COBBLED_DEEPSLATE_TILES_FAMILY, Blocks.COBBLED_DEEPSLATE, COBBLED_DEEPSLATE_BRICKS.get(), COBBLED_DEEPSLATE_TILES.get());

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
		planksFromLogs(consumer, AZALEA_PLANKS.get(), CCItemTags.AZALEA_LOGS, 4);
		woodFromLogs(consumer, AZALEA_WOOD.get(), AZALEA_LOG.get());
		woodFromLogs(consumer, STRIPPED_AZALEA_WOOD.get(), STRIPPED_AZALEA_LOG.get());
		hangingSign(consumer, AZALEA_HANGING_SIGNS.getFirst().get(), STRIPPED_AZALEA_LOG.get());
		BoatloadRecipeProvider.boatRecipes(consumer, CCBoatTypes.AZALEA);
		WoodworksRecipeProvider.baseRecipes(consumer, AZALEA_PLANKS.get(), AZALEA_SLAB.get(), AZALEA_BOARDS.get(), AZALEA_BOOKSHELF.get(), CHISELED_AZALEA_BOOKSHELF.get(), AZALEA_LADDER.get(), AZALEA_BEEHIVE.get(), AZALEA_CHEST.get(), TRAPPED_AZALEA_CHEST.get(), CavernsAndChasms.MOD_ID);
		WoodworksRecipeProvider.sawmillRecipes(consumer, AZALEA_PLANKS_FAMILY, CCItemTags.AZALEA_LOGS, AZALEA_BOARDS.get(), AZALEA_LADDER.get(), CavernsAndChasms.MOD_ID);

		conversionRecipe(consumer, Items.YELLOW_DYE, FALSE_HOPE.get(), "yellow_dye");
		conversionRecipe(consumer, Items.GREEN_DYE, MOSCHATEL.get(), "green_dye");

		trimRecipes(consumer, CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.NETHERRACK);
		trimRecipes(consumer, CCItems.FORGER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.RIM_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.PLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.COAL_BLOCK);
		trimRecipes(consumer, CCItems.CORE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.COAL_BLOCK);
		SpecialRecipeBuilder.special(CCRecipeSerializers.FADED_TRIM_DUPING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":faded_trim_smithing_template_duping");
		SpecialRecipeBuilder.special(CCRecipeSerializers.EMISSIVE_TRIM_DUPING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":emissive_trim_smithing_template_duping");

		ccWaxRecipes(consumer);
	}

	public static void copperGearRecipes(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike block, Item helmet, Item chestplate, Item leggings, Item boots, Item sword, Item pickaxe, Item axe, Item shovel, Item hoe) {
		ShapedRecipeBuilder.shaped(COMBAT, boots).define('X', block).pattern("X X").pattern("X X").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, chestplate).define('X', block).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, helmet).define('X', block).pattern("XXX").pattern("X X").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, leggings).define('X', block).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_copper_block", has(block)).save(consumer);

		ShapedRecipeBuilder.shaped(TOOLS, axe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, hoe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, pickaxe).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, shovel).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("X").pattern("#").pattern("#").unlockedBy("has_copper_block", has(block)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, sword).define('#', Tags.Items.RODS_WOODEN).define('X', block).pattern("X").pattern("X").pattern("#").unlockedBy("has_copper_block", has(block)).save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(pickaxe, shovel, axe, hoe, sword, helmet, chestplate, leggings, boots), MISC, ingot, 0.1F, 200).unlockedBy(getHasName(pickaxe), has(pickaxe)).unlockedBy(getHasName(shovel), has(shovel)).unlockedBy(getHasName(axe), has(axe)).unlockedBy(getHasName(hoe), has(hoe)).unlockedBy(getHasName(sword), has(sword)).unlockedBy(getHasName(helmet), has(helmet)).unlockedBy(getHasName(chestplate), has(chestplate)).unlockedBy(getHasName(leggings), has(leggings)).unlockedBy(getHasName(boots), has(boots)).save(consumer, CavernsAndChasms.location(getSmeltingRecipeName(ingot)));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(pickaxe, shovel, axe, hoe, sword, helmet, chestplate, leggings, boots), MISC, ingot, 0.1F, 100).unlockedBy(getHasName(pickaxe), has(pickaxe)).unlockedBy(getHasName(shovel), has(shovel)).unlockedBy(getHasName(axe), has(axe)).unlockedBy(getHasName(hoe), has(hoe)).unlockedBy(getHasName(sword), has(sword)).unlockedBy(getHasName(helmet), has(helmet)).unlockedBy(getHasName(chestplate), has(chestplate)).unlockedBy(getHasName(leggings), has(leggings)).unlockedBy(getHasName(boots), has(boots)).save(consumer, CavernsAndChasms.location(getBlastingRecipeName(ingot)));
	}

	public void copperIngotRecipes(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike block, ItemLike door, ItemLike trapdoor, ItemLike bars, ItemLike button, ItemLike lightningRod, ItemLike floodlight, ItemLike toolbox) {
		if (ingot != Items.COPPER_INGOT) {
			storageRecipesWithCustomUnpacking(consumer, RecipeCategory.MISC, ingot, RecipeCategory.BUILDING_BLOCKS, block, getSimpleRecipeName(ingot), getItemName(ingot));
			ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, lightningRod).define('#', ingot).pattern("#").pattern("#").pattern("#").unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		}

		doorBuilder(door, Ingredient.of(ingot)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		trapdoorBuilder(trapdoor, Ingredient.of(ingot)).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, bars, 16).define('#', ingot).pattern("###").pattern("###").unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapelessRecipeBuilder.shapeless(REDSTONE, button).requires(ItemTags.WOODEN_BUTTONS).requires(ingot).unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, floodlight).define('C', ingot).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, toolbox).define('C', block).define('I', ingot).pattern(" I ").pattern("I I").pattern("CCC").unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, ItemLike inputName) {
		stonecutterRecipe(consumer, category, output, input, 1, inputName);
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, int count, ItemLike inputName) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input)).save(consumer, getConversionRecipeName(output, inputName) + "_stonecutting");
	}

	public void stonecutterRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family) {
		stonecutterRecipes(consumer, family, family.getBaseBlock());
	}

	public void stonecutterRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family, ItemLike... inputs) {
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

	protected void necromiumSmithingRecipe(Consumer<FinishedRecipe> consumer, Item input, RecipeCategory category, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(CCItemTags.INGOTS_NECROMIUM), category, output).unlocks("has_necromium_ingot", has(CCItemTags.INGOTS_NECROMIUM)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_smithing"));
	}

	public static void copperHornRecipe(Consumer<FinishedRecipe> consumer, ResourceKey<Instrument> input, ImmutableList<RegistryObject<Instrument>> output) {
		CompoundTag inputTag = new CompoundTag();
		inputTag.putString("instrument", input.location().toString());

		CompoundTag outputTag = new CompoundTag();
		String harmonyID = output.get(0).getId().toString();
		String melodyID = output.get(1).getId().toString();
		String bassID = output.get(2).getId().toString();
		outputTag.putString(CopperHornItem.HARMONY, harmonyID);
		outputTag.putString(CopperHornItem.MELODY, melodyID);
		outputTag.putString(CopperHornItem.BASS, bassID);

		String recipeName = (harmonyID + melodyID + bassID).replace("caverns_and_chasms:", "").replace("copper_horn", "");
		CCShapedRecipeBuilder.shaped(TOOLS, PartialNBTIngredient.of(CCItems.COPPER_HORN.get(), outputTag)).define('#', PartialNBTIngredient.of(Items.GOAT_HORN, inputTag)).define('C', Tags.Items.INGOTS_COPPER).pattern("C#C").pattern(" C ").unlockedBy("has_goat_horn", has(Items.GOAT_HORN)).save(consumer, CavernsAndChasms.location(recipeName + "copper_horn"));
	}

	public void platedBricksRecipe(Consumer<FinishedRecipe> consumer, ItemLike block, ItemLike ingot) {
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, block, 4).define('#', ingot).define('X', Blocks.DEEPSLATE).pattern("#X").pattern("X#").unlockedBy(getHasName(ingot), has(ingot)).save(consumer);
	}

	public void platedBricksRecipe(Consumer<FinishedRecipe> consumer, ItemLike block, TagKey<Item> ingotTag, String hasName) {
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, block, 4).define('#', ingotTag).define('X', Blocks.DEEPSLATE).pattern("#X").pattern("X#").unlockedBy("has_" + hasName, has(ingotTag)).save(consumer);
	}

	public void platedBricksRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family) {
		generateRecipes(consumer, family);
		stonecutterRecipes(consumer, family);
	}

	protected static void grate(Consumer<FinishedRecipe> consumer, Block grateBlock, Block material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, grateBlock, 4).define('M', material).pattern(" M ").pattern("M M").pattern(" M ").unlockedBy(getHasName(material), has(material)).save(consumer);
	}

	protected static void copperBulb(Consumer<FinishedRecipe> consumer, Block bulbBlock, Block material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, bulbBlock, 4).define('C', material).define('R', Items.REDSTONE).define('B', Items.BLAZE_ROD).pattern(" C ").pattern("CBC").pattern(" R ").unlockedBy(getHasName(material), has(material)).save(consumer);
	}

	public static void mimingRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output) {
		mimingRecipeBuilder(category, Ingredient.of(input), output, 1).unlockedBy(getHasName(input), has(input)).save(consumer);
	}

	protected void ccWaxRecipes(Consumer<FinishedRecipe> consumer) {
		CCCompat.registerWaxables();
		HoneycombItem.WAXABLES.get().forEach((base, waxed) -> {
			if (BuiltInRegistries.BLOCK.getKey(waxed).getNamespace().equals(this.getModID()) && !(waxed instanceof ToolboxBlock) && !(waxed instanceof IngotBlock)) {
				RecipeCategory category = (waxed instanceof BaseRailBlock || waxed instanceof IronBarsBlock || waxed instanceof FloodlightBlock || waxed instanceof LightningRodBlock) ? DECORATIONS : waxed instanceof ButtonBlock ? REDSTONE : BUILDING_BLOCKS;
				ShapelessRecipeBuilder.shapeless(category, waxed).requires(base).requires(Items.HONEYCOMB).group(getItemName(waxed)).unlockedBy(getHasName(base), has(base)).save(consumer, getModConversionRecipeName(waxed, Items.HONEYCOMB));
			}
		});

		WeatheringCopperItem.WAXABLES.get().forEach((base, waxed) -> {
			if (BuiltInRegistries.ITEM.getKey(waxed).getNamespace().equals(this.getModID())) {
				RecipeCategory category = (waxed instanceof ArmorItem || waxed instanceof SwordItem) ? COMBAT : TOOLS;
				ShapelessRecipeBuilder.shapeless(category, waxed).requires(base).requires(Items.HONEYCOMB).group(getItemName(waxed)).unlockedBy(getHasName(base), has(base)).save(consumer, getModConversionRecipeName(waxed, Items.HONEYCOMB));
			}
		});
	}

	public static SingleItemRecipeBuilder mimingRecipeBuilder(RecipeCategory category, Ingredient input, ItemLike output, int count) {
		return new SingleItemRecipeBuilder(category, CCRecipeSerializers.MIMING.get(), input, output, count);
	}
}