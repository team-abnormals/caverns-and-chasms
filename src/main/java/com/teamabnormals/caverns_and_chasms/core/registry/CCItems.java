package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.common.item.BlueprintRecordItem;
import com.teamabnormals.blueprint.common.item.BlueprintSpawnEggItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.item.*;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.*;
import com.teamabnormals.caverns_and_chasms.common.item.silver.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCItems {
	public static final ItemSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> SILVER_INGOT = HELPER.createItem("silver_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SILVER_NUGGET = HELPER.createItem("silver_nugget", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> RAW_SILVER = HELPER.createItem("raw_silver", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SILVER_ARROW = HELPER.createItem("silver_arrow", () -> new SilverArrowItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> ORE_DETECTOR = HELPER.createItem("ore_detector", () -> new OreDetectorItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> KUNAI = HELPER.createItem("kunai", () -> new KunaiItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_SWORD = HELPER.createItem("silver_sword", () -> new SilverSwordItem(CCTiers.Tools.SILVER, 3, -2.4F, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_SHOVEL = HELPER.createItem("silver_shovel", () -> new SilverShovelItem(CCTiers.Tools.SILVER, 1.5F, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> SILVER_PICKAXE = HELPER.createItem("silver_pickaxe", () -> new SilverPickaxeItem(CCTiers.Tools.SILVER, 1, -2.8F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> SILVER_AXE = HELPER.createItem("silver_axe", () -> new SilverAxeItem(CCTiers.Tools.SILVER, 6.0F, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> SILVER_HOE = HELPER.createItem("silver_hoe", () -> new SilverHoeItem(CCTiers.Tools.SILVER, 0, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> SILVER_HELMET = HELPER.createItem("silver_helmet", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_CHESTPLATE = HELPER.createItem("silver_chestplate", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_LEGGINGS = HELPER.createItem("silver_leggings", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_BOOTS = HELPER.createItem("silver_boots", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SILVER_HORSE_ARMOR = HELPER.createItem("silver_horse_armor", () -> new HorseArmorItem(6, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_silver.png"), new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> NECROMIUM_INGOT = HELPER.createItem("necromium_ingot", () -> new Item(new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> NECROMIUM_NUGGET = HELPER.createCompatItem("nether_extension", "necromium_nugget", new Item.Properties().fireResistant(), CreativeModeTab.TAB_MATERIALS);
	public static final RegistryObject<Item> NECROMIUM_SWORD = HELPER.createItem("necromium_sword", () -> new NecromiumSwordItem(CCTiers.Tools.NECROMIUM, 3, -2.4F, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_SHOVEL = HELPER.createItem("necromium_shovel", () -> new NecromiumShovelItem(CCTiers.Tools.NECROMIUM, 1.5F, -3.0F, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_PICKAXE = HELPER.createItem("necromium_pickaxe", () -> new NecromiumPickaxeItem(CCTiers.Tools.NECROMIUM, 1, -2.8F, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_AXE = HELPER.createItem("necromium_axe", () -> new NecromiumAxeItem(CCTiers.Tools.NECROMIUM, 5.0F, -3.0F, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_HOE = HELPER.createItem("necromium_hoe", () -> new NecromiumHoeItem(CCTiers.Tools.NECROMIUM, -3, 0.0F, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_HELMET = HELPER.createItem("necromium_helmet", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlot.HEAD, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_CHESTPLATE = HELPER.createItem("necromium_chestplate", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlot.CHEST, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_LEGGINGS = HELPER.createItem("necromium_leggings", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlot.LEGS, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_BOOTS = HELPER.createItem("necromium_boots", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlot.FEET, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_HORSE_ARMOR = HELPER.createItem("necromium_horse_armor", () -> new NecromiumHorseArmorItem(9, "necromium", new Item.Properties().stacksTo(1).fireResistant().tab(createCompatGroup("nether_extension", CreativeModeTab.TAB_MISC))));

	public static final RegistryObject<Item> SANGUINE_PLATING = HELPER.createItem("sanguine_plating", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SANGUINE_HELMET = HELPER.createItem("sanguine_helmet", () -> new SanguineArmorItem(CCTiers.Armor.SANGUINE, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SANGUINE_CHESTPLATE = HELPER.createItem("sanguine_chestplate", () -> new SanguineArmorItem(CCTiers.Armor.SANGUINE, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SANGUINE_LEGGINGS = HELPER.createItem("sanguine_leggings", () -> new SanguineArmorItem(CCTiers.Armor.SANGUINE, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> SANGUINE_BOOTS = HELPER.createItem("sanguine_boots", () -> new SanguineArmorItem(CCTiers.Armor.SANGUINE, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<Item> SPINEL = HELPER.createItem("spinel", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> FORGOTTEN_COLLAR = HELPER.createItem("forgotten_collar", () -> new ForgottenCollarItem(new Item.Properties().stacksTo(1).fireResistant().tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> CAVEFISH = HELPER.createItem("cavefish", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(Foods.CAVEFISH)));
	public static final RegistryObject<Item> COOKED_CAVEFISH = HELPER.createItem("cooked_cavefish", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(Foods.COOKED_CAVEFISH)));
	public static final RegistryObject<Item> CAVEFISH_BUCKET = HELPER.createItem("cavefish_bucket", () -> new MobBucketItem(CCEntityTypes.CAVEFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> GOLDEN_BUCKET = HELPER.createItem("golden_bucket", () -> new GoldenBucketItem(() -> Fluids.EMPTY, new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)));
	public static final RegistryObject<Item> GOLDEN_WATER_BUCKET = HELPER.createItem("golden_water_bucket", () -> new GoldenBucketItem(() -> Fluids.WATER, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_LAVA_BUCKET = HELPER.createItem("golden_lava_bucket", () -> new GoldenBucketItem(() -> Fluids.LAVA, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_MILK_BUCKET = HELPER.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_POWDER_SNOW_BUCKET = HELPER.createItem("golden_powder_snow_bucket", () -> new GoldenSolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.POWDER_SNOW_PLACE, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).tab(CreativeModeTab.TAB_MISC).stacksTo(1)));

	public static final RegistryObject<Item> MUSIC_DISC_EPILOGUE = HELPER.createItem("music_disc_epilogue", () -> new BlueprintRecordItem(11, CCSoundEvents.EPILOGUE, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE)));

	public static final RegistryObject<BlueprintSpawnEggItem> CAVEFISH_SPAWN_EGG = HELPER.createSpawnEggItem("cavefish", CCEntityTypes.CAVEFISH::get, 14145236, 11251356);
	public static final RegistryObject<BlueprintSpawnEggItem> DEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("deeper", CCEntityTypes.DEEPER::get, 8355711, 13717260);
	public static final RegistryObject<BlueprintSpawnEggItem> FLY_SPAWN_EGG = HELPER.createSpawnEggItem("fly", CCEntityTypes.FLY::get, 920336, 7080720);
	public static final RegistryObject<BlueprintSpawnEggItem> MIME_SPAWN_EGG = HELPER.createSpawnEggItem("mime", CCEntityTypes.MIME::get, 0x5A5050, 0x969964);
	public static final RegistryObject<BlueprintSpawnEggItem> RAT_SPAWN_EGG = HELPER.createSpawnEggItem("rat", CCEntityTypes.RAT::get, 0x3B4248, 0xA76E6C);

	public static final RegistryObject<BlueprintSpawnEggItem> ZOMBIE_WOLF_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_wolf", CCEntityTypes.ZOMBIE_WOLF::get, 0x6A9D5A, 0x364430);
	public static final RegistryObject<BlueprintSpawnEggItem> ZOMBIE_CAT_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_cat", CCEntityTypes.ZOMBIE_CAT::get, 0x4A7D52, 0x79AD69);
	public static final RegistryObject<BlueprintSpawnEggItem> ZOMBIE_PARROT_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_parrot", CCEntityTypes.ZOMBIE_PARROT::get, 0x315D39, 0x5A8D52);

	public static final RegistryObject<BlueprintSpawnEggItem> SKELETON_WOLF_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_wolf", CCEntityTypes.SKELETON_WOLF::get, 0x979797, 0x494949);
	public static final RegistryObject<BlueprintSpawnEggItem> SKELETON_CAT_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_cat", CCEntityTypes.SKELETON_CAT::get, 0xD3D3D3, 0x979797);
	public static final RegistryObject<BlueprintSpawnEggItem> SKELETON_PARROT_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_parrot", CCEntityTypes.SKELETON_PARROT::get, 0x979797, 0xADABAD);

	public static class Foods {
		public static final FoodProperties CAVEFISH = new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build();
		public static final FoodProperties COOKED_CAVEFISH = new FoodProperties.Builder().nutrition(4).saturationMod(0.25F).build();
	}

	public static CreativeModeTab createCompatGroup(String modid, CreativeModeTab group) {
		return ModList.get().isLoaded(modid) ? group : null;
	}
}
