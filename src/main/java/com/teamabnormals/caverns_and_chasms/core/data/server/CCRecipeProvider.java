package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.boatload.core.data.server.BoatloadRecipeProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.integration.boatload.CCBoatTypes;
import com.teamabnormals.woodworks.core.data.server.WoodworksRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import java.util.function.Consumer;

import static com.teamabnormals.caverns_and_chasms.core.other.CCBlockFamilies.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static net.minecraft.data.recipes.RecipeCategory.*;

public class CCRecipeProvider extends BlueprintRecipeProvider {
	public static final ModLoadedCondition ENDERGETIC_LOADED = new ModLoadedCondition("endergetic");

	private static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(SILVER_ORE.get(), DEEPSLATE_SILVER_ORE.get(), SOUL_SILVER_ORE.get(), CCItems.RAW_SILVER.get());
	private static final ImmutableList<ItemLike> SPINEL_SMELTABLES = ImmutableList.of(SPINEL_ORE.get(), DEEPSLATE_SPINEL_ORE.get());

	public CCRecipeProvider(PackOutput output) {
		super(CavernsAndChasms.MOD_ID, output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(TOOLS, Items.BUNDLE).define('R', Items.LEATHER).define('S', Items.STRING).pattern("S").pattern("R").unlockedBy("has_leather", has(Items.LEATHER)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Items.BUNDLE)));

		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.RAIL, 3).define('#', Items.STICK).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("X X").unlockedBy("has_minecart", has(Items.MINECART)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.ACTIVATOR_RAIL, 3).define('#', Blocks.REDSTONE_TORCH).define('S', Items.STICK).define('X', Items.IRON_NUGGET).pattern("XSX").pattern("X#X").pattern("XSX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.DETECTOR_RAIL, 3).define('R', Items.REDSTONE).define('#', Blocks.STONE_PRESSURE_PLATE).define('X', Items.IRON_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, Blocks.POWERED_RAIL, 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', Items.GOLD_NUGGET).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(TRANSPORTATION, SPIKED_RAIL.get(), 1).define('R', Items.REDSTONE).define('#', Items.STICK).define('X', CCItemTags.NUGGETS_SILVER).pattern("X X").pattern("X#X").pattern("XRX").unlockedBy("has_rail", has(Blocks.RAIL)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, Blocks.CHAIN).define('#', Items.IRON_NUGGET).pattern("#").pattern("#").pattern("#").unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET)).save(consumer);

		ShapedRecipeBuilder.shaped(MISC, CCItems.BEJEWELED_PEARL.get(), 2).define('P', Items.ENDER_PEARL).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("SPS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL)).save(consumer);
		ShapedRecipeBuilder.shaped(FOOD, CCItems.BEJEWELED_APPLE.get(), 2).define('A', Items.GOLDEN_APPLE).define('S', CCItems.SPINEL.get()).pattern("SSS").pattern("SAS").pattern("SSS").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE)).save(consumer);
		ShapedRecipeBuilder.shaped(REDSTONE, TMT.get(), 4).define('T', Items.TNT).define('S', CCItems.SPINEL.get()).pattern(" S ").pattern("STS").pattern(" S ").unlockedBy("has_spinel", has(CCItems.SPINEL.get())).unlockedBy("has_tnt", has(Items.TNT)).save(consumer);
		ShapelessRecipeBuilder.shapeless(COMBAT, CCItems.BLUNT_ARROW.get(), 4).requires(Items.ARROW).requires(CCItems.SPINEL.get()).unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.LARGE_ARROW.get(), 4).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).define('Y', Items.FEATHER).pattern("X").pattern("#").pattern("Y").unlockedBy("has_feather", has(Items.FEATHER)).unlockedBy("has_silver", has(CCItemTags.INGOTS_SILVER)).save(consumer);

		SpecialRecipeBuilder.special(CCRecipeSerializers.TOOLBOX_WAXING.get()).save(consumer, CavernsAndChasms.MOD_ID + ":toolbox_waxing");
		ShapedRecipeBuilder.shaped(DECORATIONS, TOOLBOX.get()).define('C', Blocks.COPPER_BLOCK).define('I', Tags.Items.INGOTS_COPPER).pattern(" I ").pattern("I I").pattern("CCC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.TUNING_FORK.get()).define('#', Tags.Items.INGOTS_COPPER).pattern(" # ").pattern(" ##").pattern("#  ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.BAROMETER.get()).define('#', Tags.Items.INGOTS_COPPER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		waxRecipe(consumer, DECORATIONS, CCItems.OXIDIZED_COPPER_GOLEM.get(), CCItems.WAXED_OXIDIZED_COPPER_GOLEM.get());
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.DEPTH_GAUGE.get()).define('#', CCItemTags.INGOTS_SILVER).define('X', Items.REDSTONE).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);
		mimingRecipe(consumer, MISC, Items.MUSIC_DISC_11, CCItems.MUSIC_DISC_EPILOGUE.get());
		mimingRecipe(consumer, MISC, Items.MOJANG_BANNER_PATTERN, CCItems.ABNORMALS_BANNER_PATTERN.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, ROCKY_DIRT.get(), 4).define('D', Blocks.DIRT).define('C', Blocks.COBBLESTONE).pattern("DC").pattern("CD").unlockedBy("has_dirt", has(Blocks.DIRT)).unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE)).save(consumer);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.ROOTED_DIRT).requires(Blocks.DIRT).requires(Blocks.HANGING_ROOTS).unlockedBy("has_hanging_roots", has(Blocks.HANGING_ROOTS)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getItemName(Blocks.ROOTED_DIRT)));

		ShapedRecipeBuilder.shaped(DECORATIONS, LAVA_LAMP.get()).define('G', Tags.Items.INGOTS_GOLD).define('B', BlueprintItemTags.BUCKETS_LAVA).pattern("GGG").pattern("GBG").pattern("GGG").unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, FLOODLIGHT.get()).define('C', Tags.Items.INGOTS_COPPER).define('A', Items.AMETHYST_SHARD).pattern(" C ").pattern("CCC").pattern(" A ").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, DECORATIONS, FLOODLIGHT.get(), WAXED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, EXPOSED_FLOODLIGHT.get(), WAXED_EXPOSED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, WEATHERED_FLOODLIGHT.get(), WAXED_WEATHERED_FLOODLIGHT.get());
		waxRecipe(consumer, DECORATIONS, OXIDIZED_FLOODLIGHT.get(), WAXED_OXIDIZED_FLOODLIGHT.get());
//		ShapedRecipeBuilder.shaped(INDUCTOR.get()).define('C', Tags.Items.INGOTS_COPPER).define('I', Items.IRON_BLOCK).define('R', Items.REDSTONE).pattern("CIC").pattern("CRC").pattern("CIC").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_TORCH.get(), 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Items.STICK).define('C', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("C").unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_CAMPFIRE.get()).define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_stick", has(Items.STICK)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_LANTERN.get()).define('#', CUPRIC_TORCH.get()).define('X', Items.IRON_NUGGET).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_copper", has(CUPRIC_TORCH.get())).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, BRAZIER.get()).define('#', ItemTags.COALS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_coal", has(ItemTags.COALS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SOUL_BRAZIER.get()).define('#', ItemTags.SOUL_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, CUPRIC_BRAZIER.get()).define('#', CCItemTags.CUPRIC_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_copper", has(CCItemTags.CUPRIC_FIRE_BASE_BLOCKS)).save(consumer);
		conditionalRecipe(consumer, ENDERGETIC_LOADED, DECORATIONS, ShapedRecipeBuilder.shaped(DECORATIONS, ENDER_BRAZIER.get()).define('#', CCItemTags.ENDER_FIRE_BASE_BLOCKS).define('S', CCItemTags.INGOTS_SILVER).pattern("S#S").pattern(" S ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).unlockedBy("has_end_stone", has(CCItemTags.ENDER_FIRE_BASE_BLOCKS)));

		ShapedRecipeBuilder.shaped(DECORATIONS, COPPER_BARS.get(), 16).define('#', Tags.Items.INGOTS_COPPER).pattern("###").pattern("###").unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, DECORATIONS, COPPER_BARS.get(), WAXED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, EXPOSED_COPPER_BARS.get(), WAXED_EXPOSED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, WEATHERED_COPPER_BARS.get(), WAXED_WEATHERED_COPPER_BARS.get());
		waxRecipe(consumer, DECORATIONS, OXIDIZED_COPPER_BARS.get(), WAXED_OXIDIZED_COPPER_BARS.get());

		ShapelessRecipeBuilder.shapeless(REDSTONE, COPPER_BUTTON.get()).requires(ItemTags.WOODEN_BUTTONS).requires(Tags.Items.INGOTS_COPPER).unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER)).save(consumer);
		waxRecipe(consumer, REDSTONE, COPPER_BUTTON.get(), WAXED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, EXPOSED_COPPER_BUTTON.get(), WAXED_EXPOSED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, WEATHERED_COPPER_BUTTON.get(), WAXED_WEATHERED_COPPER_BUTTON.get());
		waxRecipe(consumer, REDSTONE, OXIDIZED_COPPER_BUTTON.get(), WAXED_OXIDIZED_COPPER_BUTTON.get());

		waxRecipe(consumer, DECORATIONS, Blocks.LIGHTNING_ROD, WAXED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, EXPOSED_LIGHTNING_ROD.get(), WAXED_EXPOSED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, WEATHERED_LIGHTNING_ROD.get(), WAXED_WEATHERED_LIGHTNING_ROD.get());
		waxRecipe(consumer, DECORATIONS, OXIDIZED_LIGHTNING_ROD.get(), WAXED_OXIDIZED_LIGHTNING_ROD.get());

		storageRecipes(consumer, MISC, CCItems.SPINEL.get(), BUILDING_BLOCKS, SPINEL_BLOCK.get());
		storageRecipes(consumer, MISC, CCItems.RAW_SILVER.get(), BUILDING_BLOCKS, RAW_SILVER_BLOCK.get());
		storageRecipes(consumer, FOOD, Items.ROTTEN_FLESH, BUILDING_BLOCKS, ROTTEN_FLESH_BLOCK.get());
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.SILVER_INGOT.get(), BUILDING_BLOCKS, SILVER_BLOCK.get(), "silver_ingot_from_silver_block", "silver_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.SILVER_NUGGET.get(), MISC, CCItems.SILVER_INGOT.get(), "silver_ingot_from_nuggets", "silver_ingot");
		storageRecipesWithCustomPacking(consumer, MISC, CCItems.COPPER_NUGGET.get(), MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");

		oreRecipes(consumer, SILVER_SMELTABLES, MISC, CCItems.SILVER_INGOT.get(), 1.0F, 200, 1.0F, 100, "silver_ingot");
		oreRecipes(consumer, SPINEL_SMELTABLES, MISC, CCItems.SPINEL.get(), 0.2F, 200, 0.2F, 100, "spinel");

		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_AXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_BOOTS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_CHESTPLATE.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_HELMET.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_HOE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_LEGGINGS.get()).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_PICKAXE.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(TOOLS, CCItems.SILVER_SHOVEL.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("#").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SILVER_SWORD.get()).define('#', Items.STICK).define('X', CCItemTags.INGOTS_SILVER).pattern("X").pattern("X").pattern("#").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		pressurePlateBuilder(REDSTONE, MEDIUM_WEIGHTED_PRESSURE_PLATE.get(), Ingredient.of(CCItemTags.INGOTS_SILVER)).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(DECORATIONS, SILVER_BARS.get(), 16).define('#', CCItemTags.INGOTS_SILVER).pattern("###").pattern("###").unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 200).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getSmeltingRecipeName(CCItems.SILVER_NUGGET.get())));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(CCItems.SILVER_PICKAXE.get(), CCItems.SILVER_SHOVEL.get(), CCItems.SILVER_AXE.get(), CCItems.SILVER_HOE.get(), CCItems.SILVER_SWORD.get(), CCItems.SILVER_HELMET.get(), CCItems.SILVER_CHESTPLATE.get(), CCItems.SILVER_LEGGINGS.get(), CCItems.SILVER_BOOTS.get(), CCItems.SILVER_HORSE_ARMOR.get()), MISC, CCItems.SILVER_NUGGET.get(), 0.1F, 100).unlockedBy("has_silver_pickaxe", has(CCItems.SILVER_PICKAXE.get())).unlockedBy("has_silver_shovel", has(CCItems.SILVER_SHOVEL.get())).unlockedBy("has_silver_axe", has(CCItems.SILVER_AXE.get())).unlockedBy("has_silver_hoe", has(CCItems.SILVER_HOE.get())).unlockedBy("has_silver_sword", has(CCItems.SILVER_SWORD.get())).unlockedBy("has_silver_helmet", has(CCItems.SILVER_HELMET.get())).unlockedBy("has_silver_chestplate", has(CCItems.SILVER_CHESTPLATE.get())).unlockedBy("has_silver_leggings", has(CCItems.SILVER_LEGGINGS.get())).unlockedBy("has_silver_boots", has(CCItems.SILVER_BOOTS.get())).unlockedBy("has_silver_horse_armor", has(CCItems.SILVER_HORSE_ARMOR.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, getBlastingRecipeName(CCItems.SILVER_NUGGET.get())));
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.KUNAI.get()).define('#', Tags.Items.RODS_WOODEN).define('S', CCItemTags.NUGGETS_SILVER).pattern(" S").pattern("# ").unlockedBy("has_silver_nugget", has(CCItemTags.NUGGETS_SILVER)).save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, Blocks.BREWING_STAND).define('B', Items.BLAZE_ROD).define('#', CCItemTags.INGOTS_SILVER).pattern(" B ").pattern("###").unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD)).save(consumer);

		ShapedRecipeBuilder.shaped(DECORATIONS, GOLDEN_BARS.get(), 16).define('#', Items.GOLD_INGOT).pattern("###").pattern("###").unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(MISC, CCItems.GOLDEN_BUCKET.get()).define('#', Blocks.GOLD_BLOCK).pattern("# #").pattern(" # ").unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK)).save(consumer);
		conditionalRecipe(consumer, new NotCondition(new ModLoadedCondition("environmental")), FOOD, ShapedRecipeBuilder.shaped(FOOD, Blocks.CAKE).define('A', CCItems.GOLDEN_MILK_BUCKET.get()).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Items.EGG).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Items.EGG)), new ResourceLocation(CavernsAndChasms.MOD_ID, getSimpleRecipeName(Blocks.CAKE)));
		conditionalRecipe(consumer, new NotCondition(new TagEmptyCondition(CCItemTags.BOTTLES_MILK.location())), MISC, ShapelessRecipeBuilder.shapeless(MISC, CCItems.GOLDEN_MILK_BUCKET.get()).requires(CCItems.GOLDEN_BUCKET.get()).requires(Ingredient.of(CCItemTags.BOTTLES_MILK), 3).unlockedBy("has_milk_bottle", has(CCItemTags.BOTTLES_MILK)));

		ShapelessRecipeBuilder.shapeless(MISC, CCItems.LIVING_FLESH.get(), 2).requires(Items.ROTTEN_FLESH, 3).requires(Ingredient.of(CCItemTags.INGOTS_SILVER), 2).requires(Items.GHAST_TEAR, 2).unlockedBy("has_silver_ingot", has(CCItemTags.INGOTS_SILVER)).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_HELMET.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("XXX").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_CHESTPLATE.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_LEGGINGS.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		ShapedRecipeBuilder.shaped(COMBAT, CCItems.SANGUINE_BOOTS.get()).define('X', CCItems.LIVING_FLESH.get()).pattern("X X").pattern("X X").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		storageRecipesWithCustomUnpacking(consumer, MISC, CCItems.LIVING_FLESH.get(), BUILDING_BLOCKS, SANGUINE_BLOCK.get(), "living_flesh_from_sanguine_block", "living_flesh");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SANGUINE_TILES.get(), 8).define('X', CCItems.LIVING_FLESH.get()).define('#', Blocks.NETHERRACK).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_living_flesh", has(CCItems.LIVING_FLESH.get())).save(consumer);
		generateRecipes(consumer, SANGUINE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SANGUINE_TILE_SLAB.get(), SANGUINE_TILES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SANGUINE_TILE_STAIRS.get(), SANGUINE_TILES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SANGUINE_TILE_WALL.get(), SANGUINE_TILES.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILES.get(), 8).define('X', CCItemTags.INGOTS_SILVER).define('#', SANGUINE_TILES.get()).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_sanguine_tiles", has(SANGUINE_TILES.get())).save(consumer);
		generateRecipes(consumer, FORTIFIED_SANGUINE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILE_SLAB.get(), FORTIFIED_SANGUINE_TILES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILE_STAIRS.get(), FORTIFIED_SANGUINE_TILES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, FORTIFIED_SANGUINE_TILE_WALL.get(), FORTIFIED_SANGUINE_TILES.get());

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
		stonecutterRecipe(consumer, BUILDING_BLOCKS, LAPIS_LAZULI_BRICK_SLAB.get(), LAPIS_LAZULI_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, LAPIS_LAZULI_BRICK_STAIRS.get(), LAPIS_LAZULI_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, LAPIS_LAZULI_BRICK_WALL.get(), LAPIS_LAZULI_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, LAPIS_LAZULI_PILLAR.get(), LAPIS_LAZULI_BRICKS.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_BRICKS.get()).define('#', CCItems.SPINEL.get()).pattern("##").pattern("##").unlockedBy(getHasName(CCItems.SPINEL.get()), has(CCItems.SPINEL.get())).save(consumer);
		generateRecipes(consumer, SPINEL_BRICKS_FAMILY);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_PILLAR.get(), 2).define('#', SPINEL_BRICKS.get()).pattern("#").pattern("#").unlockedBy(getHasName(SPINEL_BRICKS.get()), has(SPINEL_BRICKS.get())).unlockedBy(getHasName(SPINEL_PILLAR.get()), has(SPINEL_PILLAR.get())).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SPINEL_LAMP.get()).define('#', CCItems.SPINEL.get()).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SPINEL_BRICK_SLAB.get(), SPINEL_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SPINEL_BRICK_STAIRS.get(), SPINEL_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, SPINEL_BRICK_WALL.get(), SPINEL_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SPINEL_PILLAR.get(), SPINEL_BRICKS.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.CALCITE).requires(Blocks.DIORITE).requires(Items.AMETHYST_SHARD).unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.CALCITE).getPath()));
		generateRecipes(consumer, CALCITE_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CALCITE_STAIRS.get(), Blocks.CALCITE);
		stonecutterRecipe(consumer, DECORATIONS, CALCITE_WALL.get(), Blocks.CALCITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_CALCITE.get(), Blocks.CALCITE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_CALCITE_SLAB.get(), Blocks.CALCITE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_CALCITE_STAIRS.get(), Blocks.CALCITE);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, POLISHED_CALCITE.get(), 4).define('#', Blocks.CALCITE).pattern("##").pattern("##").unlockedBy("has_calcite", has(Blocks.CALCITE)).save(consumer);
		generateRecipes(consumer, POLISHED_CALCITE_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_CALCITE_SLAB.get(), POLISHED_CALCITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_CALCITE_STAIRS.get(), POLISHED_CALCITE.get());

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, Blocks.TUFF, 2).requires(Blocks.BASALT).requires(Blocks.COBBLESTONE).unlockedBy("has_stone", has(Blocks.BASALT)).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(Blocks.TUFF).getPath()));
		generateRecipes(consumer, TUFF_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, TUFF_STAIRS.get(), Blocks.TUFF);
		stonecutterRecipe(consumer, DECORATIONS, TUFF_WALL.get(), Blocks.TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_TUFF.get(), Blocks.TUFF);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_TUFF_SLAB.get(), Blocks.TUFF, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_TUFF_STAIRS.get(), Blocks.TUFF);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, POLISHED_TUFF.get(), 4).define('#', Blocks.TUFF).pattern("##").pattern("##").unlockedBy("has_tuff", has(Blocks.TUFF)).save(consumer);
		generateRecipes(consumer, POLISHED_TUFF_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_TUFF_SLAB.get(), POLISHED_TUFF.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_TUFF_STAIRS.get(), POLISHED_TUFF.get());

		generateRecipes(consumer, SUGILITE_FAMILY);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, SUGILITE.get()).requires(Blocks.GRANITE).requires(CCItems.SPINEL.get()).unlockedBy("has_spinel", has(CCItems.SPINEL.get())).save(consumer, new ResourceLocation(CavernsAndChasms.MOD_ID, RecipeBuilder.getDefaultRecipeId(SUGILITE.get()).getPath()));
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SUGILITE_SLAB.get(), SUGILITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, SUGILITE_STAIRS.get(), SUGILITE.get());
		stonecutterRecipe(consumer, DECORATIONS, SUGILITE_WALL.get(), SUGILITE.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_SUGILITE.get(), SUGILITE.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_SUGILITE_SLAB.get(), SUGILITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_SUGILITE_STAIRS.get(), SUGILITE.get());
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, POLISHED_SUGILITE.get(), 4).define('#', SUGILITE.get()).pattern("##").pattern("##").unlockedBy("has_sugilite", has(SUGILITE.get())).save(consumer);
		generateRecipes(consumer, POLISHED_SUGILITE_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_SUGILITE_SLAB.get(), POLISHED_SUGILITE.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, POLISHED_SUGILITE_STAIRS.get(), POLISHED_SUGILITE.get());

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, DRIPSTONE_SHINGLES.get(), 4).define('#', Blocks.DRIPSTONE_BLOCK).pattern("##").pattern("##").unlockedBy("has_dripstone", has(Blocks.DRIPSTONE_BLOCK)).save(consumer);
		generateRecipes(consumer, DRIPSTONE_SHINGLES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DRIPSTONE_SHINGLE_SLAB.get(), DRIPSTONE_SHINGLES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DRIPSTONE_SHINGLE_STAIRS.get(), DRIPSTONE_SHINGLES.get());
		stonecutterRecipe(consumer, DECORATIONS, DRIPSTONE_SHINGLE_WALL.get(), DRIPSTONE_SHINGLES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CHISELED_DRIPSTONE_SHINGLES.get(), DRIPSTONE_SHINGLES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DRIPSTONE_SHINGLE_SLAB.get(), Blocks.DRIPSTONE_BLOCK, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, DRIPSTONE_SHINGLE_STAIRS.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterRecipe(consumer, DECORATIONS, DRIPSTONE_SHINGLE_WALL.get(), Blocks.DRIPSTONE_BLOCK);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CHISELED_DRIPSTONE_SHINGLES.get(), Blocks.DRIPSTONE_BLOCK);
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, FLOODED_DRIPSTONE_SHINGLES.get(), 8).requires(BlueprintItemTags.BUCKETS_WATER).requires(DRIPSTONE_SHINGLES.get(), 8).unlockedBy("has_dripstone_shingles", has(DRIPSTONE_SHINGLES.get())).save(consumer);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.AMETHYST_SHARD, BUILDING_BLOCKS, AMETHYST_BLOCK.get(), "amethyst_from_amethyst_block", "amethyst_shard");
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST.get(), 4).define('#', Blocks.AMETHYST_BLOCK).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.AMETHYST_BLOCK), has(Blocks.AMETHYST_BLOCK)).save(consumer);
		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, CUT_AMETHYST_BRICKS.get(), 4).define('#', CUT_AMETHYST.get()).pattern("##").pattern("##").unlockedBy(getHasName(CUT_AMETHYST.get()), has(CUT_AMETHYST.get())).save(consumer);
		generateRecipes(consumer, CUT_AMETHYST_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_SLAB.get(), CUT_AMETHYST_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_STAIRS.get(), CUT_AMETHYST_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, CUT_AMETHYST_BRICK_WALL.get(), CUT_AMETHYST_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICKS.get(), CUT_AMETHYST.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_SLAB.get(), CUT_AMETHYST.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_STAIRS.get(), CUT_AMETHYST.get());
		stonecutterRecipe(consumer, DECORATIONS, CUT_AMETHYST_BRICK_WALL.get(), CUT_AMETHYST.get());

		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST.get(), Blocks.AMETHYST_BLOCK);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICKS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_SLAB.get(), Blocks.AMETHYST_BLOCK, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, CUT_AMETHYST_BRICK_STAIRS.get(), Blocks.AMETHYST_BLOCK);
		stonecutterRecipe(consumer, DECORATIONS, CUT_AMETHYST_BRICK_WALL.get(), Blocks.AMETHYST_BLOCK);

		storageRecipesWithCustomUnpacking(consumer, MISC, Items.ECHO_SHARD, BUILDING_BLOCKS, ECHO_BLOCK.get(), "echo_shard_from_echo_block", "echo_shard");

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE)).save(consumer);
		generateRecipes(consumer, COBBLESTONE_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_BRICK_SLAB.get(), COBBLESTONE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_BRICK_STAIRS.get(), COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLESTONE_BRICK_WALL.get(), COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_BRICKS.get(), Blocks.COBBLESTONE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_BRICK_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_BRICK_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterRecipe(consumer, DECORATIONS, COBBLESTONE_BRICK_WALL.get(), Blocks.COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLESTONE_TILES.get(), 4).define('#', COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(COBBLESTONE_BRICKS.get()), has(COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, COBBLESTONE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_SLAB.get(), COBBLESTONE_TILES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_STAIRS.get(), COBBLESTONE_TILES.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLESTONE_TILE_WALL.get(), COBBLESTONE_TILES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILES.get(), COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_SLAB.get(), COBBLESTONE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_STAIRS.get(), COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLESTONE_TILE_WALL.get(), COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILES.get(), Blocks.COBBLESTONE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_SLAB.get(), Blocks.COBBLESTONE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLESTONE_TILE_STAIRS.get(), Blocks.COBBLESTONE);
		stonecutterRecipe(consumer, DECORATIONS, COBBLESTONE_TILE_WALL.get(), Blocks.COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get(), 4).define('#', Blocks.MOSSY_COBBLESTONE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.MOSSY_COBBLESTONE), has(Blocks.MOSSY_COBBLESTONE)).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICK_SLAB.get(), MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICK_STAIRS.get(), MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, MOSSY_COBBLESTONE_BRICK_WALL.get(), MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICK_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICK_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterRecipe(consumer, DECORATIONS, MOSSY_COBBLESTONE_BRICK_WALL.get(), Blocks.MOSSY_COBBLESTONE);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get(), 4).define('#', MOSSY_COBBLESTONE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(MOSSY_COBBLESTONE_BRICKS.get()), has(MOSSY_COBBLESTONE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, MOSSY_COBBLESTONE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_SLAB.get(), MOSSY_COBBLESTONE_TILES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_STAIRS.get(), MOSSY_COBBLESTONE_TILES.get());
		stonecutterRecipe(consumer, DECORATIONS, MOSSY_COBBLESTONE_TILE_WALL.get(), MOSSY_COBBLESTONE_TILES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get(), MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_SLAB.get(), MOSSY_COBBLESTONE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_STAIRS.get(), MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, MOSSY_COBBLESTONE_TILE_WALL.get(), MOSSY_COBBLESTONE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_SLAB.get(), Blocks.MOSSY_COBBLESTONE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILE_STAIRS.get(), Blocks.MOSSY_COBBLESTONE);
		stonecutterRecipe(consumer, DECORATIONS, MOSSY_COBBLESTONE_TILE_WALL.get(), Blocks.MOSSY_COBBLESTONE);

		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get()).requires(COBBLESTONE_BRICKS.get()).requires(Blocks.VINE).group("mossy_cobblestone_bricks").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get()).requires(COBBLESTONE_TILES.get()).requires(Blocks.VINE).group("mossy_cobblestone_tiles").unlockedBy("has_vine", has(Blocks.VINE)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES.get(), Blocks.VINE));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_BRICKS.get()).requires(COBBLESTONE_BRICKS.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_bricks").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_BRICKS.get(), Blocks.MOSS_BLOCK));
		ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, MOSSY_COBBLESTONE_TILES.get()).requires(COBBLESTONE_TILES.get()).requires(Blocks.MOSS_BLOCK).group("mossy_cobblestone_tiles").unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK)).save(consumer, getModConversionRecipeName(MOSSY_COBBLESTONE_TILES.get(), Blocks.MOSS_BLOCK));

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE, 4).define('#', Blocks.DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.DEEPSLATE), has(Blocks.DEEPSLATE)).save(consumer);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICKS.get(), 4).define('#', Blocks.COBBLED_DEEPSLATE).pattern("##").pattern("##").unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE)).save(consumer);
		generateRecipes(consumer, COBBLED_DEEPSLATE_BRICKS_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICK_SLAB.get(), COBBLED_DEEPSLATE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICK_STAIRS.get(), COBBLED_DEEPSLATE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLED_DEEPSLATE_BRICK_WALL.get(), COBBLED_DEEPSLATE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICKS.get(), Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICK_SLAB.get(), Blocks.COBBLED_DEEPSLATE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_BRICK_STAIRS.get(), Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, DECORATIONS, COBBLED_DEEPSLATE_BRICK_WALL.get(), Blocks.COBBLED_DEEPSLATE);

		chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Ingredient.of(Blocks.DEEPSLATE_BRICK_SLAB)).unlockedBy("has_deepslate_brick_slab", has(Blocks.DEEPSLATE_BRICK_SLAB)).save(consumer);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS);

		ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILES.get(), 4).define('#', COBBLED_DEEPSLATE_BRICKS.get()).pattern("##").pattern("##").unlockedBy(getHasName(COBBLED_DEEPSLATE_BRICKS.get()), has(COBBLED_DEEPSLATE_BRICKS.get())).save(consumer);
		generateRecipes(consumer, COBBLED_DEEPSLATE_TILES_FAMILY);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_SLAB.get(), COBBLED_DEEPSLATE_TILES.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_STAIRS.get(), COBBLED_DEEPSLATE_TILES.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLED_DEEPSLATE_TILE_WALL.get(), COBBLED_DEEPSLATE_TILES.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILES.get(), COBBLED_DEEPSLATE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_SLAB.get(), COBBLED_DEEPSLATE_BRICKS.get(), 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_STAIRS.get(), COBBLED_DEEPSLATE_BRICKS.get());
		stonecutterRecipe(consumer, DECORATIONS, COBBLED_DEEPSLATE_TILE_WALL.get(), COBBLED_DEEPSLATE_BRICKS.get());
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILES.get(), Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_SLAB.get(), Blocks.COBBLED_DEEPSLATE, 2);
		stonecutterRecipe(consumer, BUILDING_BLOCKS, COBBLED_DEEPSLATE_TILE_STAIRS.get(), Blocks.COBBLED_DEEPSLATE);
		stonecutterRecipe(consumer, DECORATIONS, COBBLED_DEEPSLATE_TILE_WALL.get(), Blocks.COBBLED_DEEPSLATE);

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

		trimRecipes(consumer, CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Blocks.NETHERRACK);
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, ItemLike inputName) {
		stonecutterRecipe(consumer, category, output, input, 1, inputName);
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, int count, ItemLike inputName) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input)).save(consumer, getConversionRecipeName(output, inputName) + "_stonecutting");
	}

	protected void necromiumSmithingRecipe(Consumer<FinishedRecipe> consumer, Item input, RecipeCategory category, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(CCItemTags.INGOTS_NECROMIUM), category, output).unlocks("has_necromium_ingot", has(CCItemTags.INGOTS_NECROMIUM)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_smithing"));
	}

	public static void mimingRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output) {
		mimingRecipeBuilder(category, Ingredient.of(input), output, 1).unlockedBy(getHasName(input), has(input)).save(consumer);
	}

	public static SingleItemRecipeBuilder mimingRecipeBuilder(RecipeCategory category, Ingredient input, ItemLike output, int count) {
		return new SingleItemRecipeBuilder(category, CCRecipeSerializers.MIMING.get(), input, output, count);
	}
}