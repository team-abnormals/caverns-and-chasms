package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.item.BlueprintRecordItem;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.item.*;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCItemTiers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBannerPatternTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import com.teamabnormals.caverns_and_chasms.integration.boatload.CCBoatTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

import static com.teamabnormals.blueprint.core.util.item.ItemStackUtil.is;
import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCItems {
	public static final ItemSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> TUNING_FORK = HELPER.createItem("tuning_fork", () -> new TuningForkItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BAROMETER = HELPER.createItem("barometer", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> OXIDIZED_COPPER_GOLEM = HELPER.createItem("oxidized_copper_golem", () -> new OxidizedCopperGolemItem(new Item.Properties().stacksTo(1), false));
	public static final RegistryObject<Item> WAXED_OXIDIZED_COPPER_GOLEM = HELPER.createItem("waxed_oxidized_copper_golem", () -> new OxidizedCopperGolemItem(new Item.Properties().stacksTo(1), true));
	public static final RegistryObject<Item> COPPER_NUGGET = HELPER.createItem("copper_nugget", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> NETHERITE_NUGGET = HELPER.createItem("netherite_nugget", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = HELPER.createItem("netherite_horse_armor", () -> new NetheriteHorseArmorItem(12, "netherite", new Item.Properties().fireResistant().stacksTo(1)));

	public static final RegistryObject<Item> RAW_SILVER = HELPER.createItem("raw_silver", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SILVER_INGOT = HELPER.createItem("silver_ingot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SILVER_NUGGET = HELPER.createItem("silver_nugget", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KUNAI = HELPER.createItem("kunai", () -> new KunaiItem(new Item.Properties()));
	public static final RegistryObject<Item> SILVER_SWORD = HELPER.createItem("silver_sword", () -> new SwordItem(CCItemTiers.SILVER, 1, -2.4F, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_SHOVEL = HELPER.createItem("silver_shovel", () -> new ShovelItem(CCItemTiers.SILVER, 1.5F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_PICKAXE = HELPER.createItem("silver_pickaxe", () -> new PickaxeItem(CCItemTiers.SILVER, 1, -2.8F, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_AXE = HELPER.createItem("silver_axe", () -> new AxeItem(CCItemTiers.SILVER, 4.0F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_HOE = HELPER.createItem("silver_hoe", () -> new HoeItem(CCItemTiers.SILVER, 0, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_HELMET = HELPER.createItem("silver_helmet", () -> new SilverArmorItem(CCArmorMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_CHESTPLATE = HELPER.createItem("silver_chestplate", () -> new SilverArmorItem(CCArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_LEGGINGS = HELPER.createItem("silver_leggings", () -> new SilverArmorItem(CCArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_BOOTS = HELPER.createItem("silver_boots", () -> new SilverArmorItem(CCArmorMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_HORSE_ARMOR = HELPER.createItem("silver_horse_armor", () -> new SilverHorseArmorItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DEPTH_GAUGE = HELPER.createItem("depth_gauge", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LARGE_ARROW = HELPER.createItem("large_arrow", () -> new LargeArrowItem(new Item.Properties()));

	public static final RegistryObject<Item> NECROMIUM_INGOT = HELPER.createItem("necromium_ingot", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_NUGGET = HELPER.createItem("necromium_nugget", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_SWORD = HELPER.createItem("necromium_sword", () -> new SwordItem(CCItemTiers.NECROMIUM, 3, -2.4F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_SHOVEL = HELPER.createItem("necromium_shovel", () -> new ShovelItem(CCItemTiers.NECROMIUM, 1.5F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_PICKAXE = HELPER.createItem("necromium_pickaxe", () -> new PickaxeItem(CCItemTiers.NECROMIUM, 1, -2.8F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_AXE = HELPER.createItem("necromium_axe", () -> new AxeItem(CCItemTiers.NECROMIUM, 5.0F, -3.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_HOE = HELPER.createItem("necromium_hoe", () -> new HoeItem(CCItemTiers.NECROMIUM, -3, 0.0F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_HELMET = HELPER.createItem("necromium_helmet", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_CHESTPLATE = HELPER.createItem("necromium_chestplate", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_LEGGINGS = HELPER.createItem("necromium_leggings", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_BOOTS = HELPER.createItem("necromium_boots", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMIUM_HORSE_ARMOR = HELPER.createItem("necromium_horse_armor", () -> new NecromiumHorseArmorItem(12, "necromium", new Item.Properties().stacksTo(1).fireResistant()));

	public static final RegistryObject<Item> LIVING_FLESH = HELPER.createItem("living_flesh", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SANGUINE_HELMET = HELPER.createItem("sanguine_helmet", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> SANGUINE_CHESTPLATE = HELPER.createItem("sanguine_chestplate", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> SANGUINE_LEGGINGS = HELPER.createItem("sanguine_leggings", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> SANGUINE_BOOTS = HELPER.createItem("sanguine_boots", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, ArmorItem.Type.BOOTS, new Item.Properties()));

	public static final RegistryObject<Item> RAW_TIN = HELPER.createItem("raw_tin", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> TIN_INGOT = HELPER.createItem("tin_ingot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> TIN_NUGGET = HELPER.createItem("tin_nugget", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> SPINEL = HELPER.createItem("spinel", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BEJEWELED_PEARL = HELPER.createItem("bejeweled_pearl", () -> new BejeweledPearlItem(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> BEJEWELED_APPLE = HELPER.createItem("bejeweled_apple", () -> new BejeweledAppleItem(new Item.Properties().food(CCFoods.BEJEWELED_APPLE).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> TETHER_POTION = HELPER.createItem("tether_potion", () -> new TetherPotionItem((new Item.Properties()).stacksTo(1)));
	public static final RegistryObject<Item> BLUNT_ARROW = HELPER.createItem("blunt_arrow", () -> new BluntArrowItem(new Item.Properties()));

	public static final RegistryObject<Item> ZIRCONIA = HELPER.createItem("zirconia", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> DEEPER_HEAD = HELPER.createItem("deeper_head", () -> new StandingAndWallBlockItem(CCBlocks.DEEPER_HEAD.get(), CCBlocks.DEEPER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
	public static final RegistryObject<Item> PEEPER_HEAD = HELPER.createItem("peeper_head", () -> new StandingAndWallBlockItem(CCBlocks.PEEPER_HEAD.get(), CCBlocks.PEEPER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
	public static final RegistryObject<Item> MIME_HEAD = HELPER.createItem("mime_head", () -> new StandingAndWallBlockItem(CCBlocks.MIME_HEAD.get(), CCBlocks.MIME_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));

	public static final RegistryObject<Item> GOLDEN_BUCKET = HELPER.createItem("golden_bucket", () -> new GoldenBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> GOLDEN_WATER_BUCKET = HELPER.createItem("golden_water_bucket", () -> new GoldenBucketItem(() -> Fluids.WATER, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_LAVA_BUCKET = HELPER.createItem("golden_lava_bucket", () -> new GoldenBucketItem(() -> Fluids.LAVA, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_POWDER_SNOW_BUCKET = HELPER.createItem("golden_powder_snow_bucket", () -> new GoldenSolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.POWDER_SNOW_PLACE, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_MILK_BUCKET = HELPER.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));

	public static final Pair<RegistryObject<Item>, RegistryObject<Item>> AZALEA_BOAT = HELPER.createBoatAndChestBoatItem("azalea", CCBlocks.AZALEA_PLANKS);
	public static final RegistryObject<Item> AZALEA_FURNACE_BOAT = HELPER.createItem("azalea_furnace_boat", ModList.get().isLoaded("boatload") ? CCBoatTypes.AZALEA_FURNACE_BOAT : () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LARGE_AZALEA_BOAT = HELPER.createItem("large_azalea_boat", ModList.get().isLoaded("boatload") ? CCBoatTypes.LARGE_AZALEA_BOAT : () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> COPPER_HORN = HELPER.createItem("copper_horn", () -> new CopperHornItem((new Item.Properties()).stacksTo(1), CCInstrumentTags.HARMONY_COPPER_HORNS, CCInstrumentTags.MELODY_COPPER_HORNS, CCInstrumentTags.BASS_COPPER_HORNS));
	public static final RegistryObject<Item> LOST_GOAT_HORN = HELPER.createItem("lost_goat_horn", () -> new InstrumentItem((new Item.Properties()).stacksTo(1), CCInstrumentTags.LOST_GOAT_HORNS));

	public static final RegistryObject<Item> MUSIC_DISC_EPILOGUE = HELPER.createItem("music_disc_epilogue", () -> new BlueprintRecordItem(11, CCSoundEvents.EPILOGUE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 77));
	public static final RegistryObject<Item> ABNORMALS_BANNER_PATTERN = HELPER.createItem("abnormals_banner_pattern", () -> new BannerPatternItem(CCBannerPatternTags.PATTERN_ITEM_ABNORMALS, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> EXILE_ARMOR_TRIM_SMITHING_TEMPLATE = HELPER.createItem("exile_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.EXILE));

	public static final RegistryObject<ForgeSpawnEggItem> DEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("deeper", CCEntityTypes.DEEPER::get, 8355711, 13717260);
	public static final RegistryObject<ForgeSpawnEggItem> PEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("peeper", CCEntityTypes.PEEPER::get, 0x3E3434, 0x694242);
	//	public static final RegistryObject<ForgeSpawnEggItem> FLY_SPAWN_EGG = HELPER.createSpawnEggItem("fly", CCEntityTypes.FLY::get, 920336, 7080720);
	public static final RegistryObject<ForgeSpawnEggItem> MIME_SPAWN_EGG = HELPER.createSpawnEggItem("mime", CCEntityTypes.MIME::get, 0x5A5050, 0x969964);
	// public static final RegistryObject<ForgeSpawnEggItem> RAT_SPAWN_EGG = HELPER.createSpawnEggItem("rat", CCEntityTypes.RAT::get, 0x3B4248, 0xA76E6C);
	public static final RegistryObject<ForgeSpawnEggItem> GLARE_SPAWN_EGG = HELPER.createSpawnEggItem("glare", CCEntityTypes.GLARE::get, 0x72942F, 0x516F2C);
	public static final RegistryObject<ForgeSpawnEggItem> COPPER_GOLEM_SPAWN_EGG = HELPER.createSpawnEggItem("copper_golem", CCEntityTypes.COPPER_GOLEM::get, 0xDE7D65, 0x8A4129);

	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(CavernsAndChasms.MOD_ID)
				.tab(FOOD_AND_DRINKS)
				.addItemsBefore(of(Items.GOLDEN_APPLE), BEJEWELED_APPLE)
				.addItemsAfter(of(Items.MILK_BUCKET), GOLDEN_MILK_BUCKET)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsAfter(of(Items.ARMOR_STAND), OXIDIZED_COPPER_GOLEM, WAXED_OXIDIZED_COPPER_GOLEM)
				.addItemsAfter(of(Items.CREEPER_HEAD), DEEPER_HEAD, PEEPER_HEAD, MIME_HEAD)
				.tab(INGREDIENTS)
				.addItemsAfter(of(Items.RAW_COPPER), RAW_TIN)
				.addItemsAfter(of(Items.RAW_GOLD), RAW_SILVER)
				.addItemsAfter(of(Items.LAPIS_LAZULI), SPINEL, ZIRCONIA)
				.addItemsBefore(of(Items.GOLD_NUGGET), COPPER_NUGGET, TIN_NUGGET)
				.addItemsAfter(of(Items.GOLD_NUGGET), SILVER_NUGGET, NETHERITE_NUGGET, NECROMIUM_NUGGET)
				.addItemsAfter(of(Items.COPPER_INGOT), TIN_INGOT)
				.addItemsAfter(of(Items.GOLD_INGOT), SILVER_INGOT)
				.addItemsAfter(of(Items.NETHERITE_INGOT), NECROMIUM_INGOT, LIVING_FLESH)
				.addItemsAfter(of(Items.MOJANG_BANNER_PATTERN), ABNORMALS_BANNER_PATTERN)
				.addItemsAfter(of(Items.ENDER_EYE), BEJEWELED_PEARL)
				.addItemsAfter(of(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE), EXILE_ARMOR_TRIM_SMITHING_TEMPLATE)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsAfter(of(Items.GOLDEN_HOE), SILVER_SHOVEL, SILVER_PICKAXE, SILVER_AXE, SILVER_HOE)
				.addItemsAfter(of(Items.NETHERITE_HOE), NECROMIUM_SHOVEL, NECROMIUM_PICKAXE, NECROMIUM_AXE, NECROMIUM_HOE)
				.addItemsBefore(of(Items.CLOCK), BAROMETER, TUNING_FORK)
				.addItemsAfter(of(Items.SPYGLASS), DEPTH_GAUGE)
				.addItemsBefore(of(Items.FISHING_ROD), GOLDEN_BUCKET, GOLDEN_WATER_BUCKET, GOLDEN_LAVA_BUCKET, GOLDEN_POWDER_SNOW_BUCKET, GOLDEN_MILK_BUCKET, () -> Items.BUNDLE)
				.addItemsAfter(of(Items.ENDER_EYE), BEJEWELED_PEARL)
				.addItemsBefore(of(Items.MUSIC_DISC_PIGSTEP), MUSIC_DISC_EPILOGUE)
				.addItemsBefore(of(Items.BAMBOO_RAFT), AZALEA_BOAT.getFirst(), AZALEA_BOAT.getSecond())
				.addItemsBefore(modLoaded(Items.BAMBOO_RAFT, "boatload"), AZALEA_FURNACE_BOAT, LARGE_AZALEA_BOAT)
				.editor(event -> event.getParameters().holders().lookup(Registries.INSTRUMENT).ifPresent(registry -> {
					generateInstrumentTypes(event, of(Items.GOAT_HORN), registry, COPPER_HORN.get(), CCInstrumentTags.HARMONY_COPPER_HORNS, CCInstrumentTags.MELODY_COPPER_HORNS, CCInstrumentTags.BASS_COPPER_HORNS);
				}))
				.tab(COMBAT)
				.addItemsAfter(of(Items.GOLDEN_SWORD), SILVER_SWORD)
				.addItemsAfter(of(Items.GOLDEN_AXE), SILVER_AXE)
				.addItemsAfter(of(Items.GOLDEN_BOOTS), SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS)
				.addItemsAfter(of(Items.GOLDEN_HORSE_ARMOR), SILVER_HORSE_ARMOR)
				.addItemsAfter(of(Items.NETHERITE_SWORD), NECROMIUM_SWORD)
				.addItemsAfter(of(Items.NETHERITE_AXE), NECROMIUM_AXE)
				.addItemsAfter(of(Items.NETHERITE_BOOTS),
						NECROMIUM_HELMET, NECROMIUM_CHESTPLATE, NECROMIUM_LEGGINGS, NECROMIUM_BOOTS,
						SANGUINE_HELMET, SANGUINE_CHESTPLATE, SANGUINE_LEGGINGS, SANGUINE_BOOTS
				)
				.addItemsAfter(of(Items.DIAMOND_HORSE_ARMOR), NETHERITE_HORSE_ARMOR, NECROMIUM_HORSE_ARMOR)
				.addItemsBefore(of(Items.SNOWBALL), KUNAI)
				.addItemsAfter(of(Items.SPECTRAL_ARROW), BLUNT_ARROW, LARGE_ARROW)
				.editor(event -> event.getParameters().holders().lookup(Registries.POTION).ifPresent(registry -> {
					generatePotionEffectTypes(event, of(Items.TIPPED_ARROW), registry, TETHER_POTION.get());
				}))
				.tab(SPAWN_EGGS)
				.addItemsAlphabetically(is(SpawnEggItem.class), DEEPER_SPAWN_EGG, PEEPER_SPAWN_EGG, MIME_SPAWN_EGG, GLARE_SPAWN_EGG, COPPER_GOLEM_SPAWN_EGG);
	}

	public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
		return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
	}

	private static void generatePotionEffectTypes(BuildCreativeModeTabContentsEvent event, Predicate<ItemStack> predicate, HolderLookup<Potion> potion, Item potionItem) {
		TabVisibility visibility = TabVisibility.PARENT_AND_SEARCH_TABS;
		List<ItemStack> items = potion.listElements().filter((p_270012_) -> {
			return !p_270012_.is(Potions.EMPTY_ID);
		}).map((p_269986_) -> {
			return PotionUtils.setPotion(new ItemStack(potionItem), p_269986_.value());
		}).toList();

		MutableHashedLinkedMap<ItemStack, TabVisibility> entries = event.getEntries();
		for (Entry<ItemStack, TabVisibility> entry : entries) {
			ItemStack stack = entry.getKey();
			if (predicate.test(stack)) {
				for (ItemStack itemValue : items) {
					entries.put(itemValue, visibility);
				}
				return;
			}
		}
	}

	private static void generateInstrumentTypes(BuildCreativeModeTabContentsEvent event, Predicate<ItemStack> predicate, HolderLookup<Instrument> lookup, Item item, TagKey<Instrument> harmonyTag, TagKey<Instrument> melodyTag, TagKey<Instrument> bassTag) {
		TabVisibility visibility = TabVisibility.PARENT_AND_SEARCH_TABS;

		Optional<Named<Instrument>> harmonyOptional = lookup.get(harmonyTag);
		Optional<Named<Instrument>> melodyOptional = lookup.get(melodyTag);
		Optional<Named<Instrument>> bassOptional = lookup.get(bassTag);

		MutableHashedLinkedMap<ItemStack, TabVisibility> entries = event.getEntries();
		if (harmonyOptional.isPresent() && melodyOptional.isPresent() && bassOptional.isPresent()) {
			for (Entry<ItemStack, TabVisibility> entry : entries) {
				ItemStack stack = entry.getKey();
				if (predicate.test(stack) && lookup.get(harmonyTag).isPresent()) {
					for (int i = 0; i < lookup.get(harmonyTag).get().size(); i++) {
						ItemStack horn = CopperHornItem.create(item, harmonyOptional.get().get(i), melodyOptional.get().get(i), bassOptional.get().get(i));
						entries.put(horn, visibility);
					}
					return;
				}
			}
		}
	}

	public static class CCFoods {
		public static final FoodProperties BEJEWELED_APPLE = new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build();
	}
}
