package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.common.items.AbnormalsSpawnEggItem;
import com.minecraftabnormals.abnormals_core.core.util.registry.ItemSubRegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.common.item.*;
import com.minecraftabnormals.caverns_and_chasms.common.item.necromium.*;
import com.minecraftabnormals.caverns_and_chasms.common.item.silver.*;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCItems {
	public static final ItemSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> SILVER_INGOT = HELPER.createItem("silver_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> SILVER_NUGGET = HELPER.createItem("silver_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> SILVER_ARROW = HELPER.createItem("silver_arrow", () -> new SilverArrowItem(new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> ORE_DETECTOR = HELPER.createItem("ore_detector", () -> new OreDetectorItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_SWORD = HELPER.createItem("silver_sword", () -> new SilverSwordItem(CCTiers.Tools.SILVER, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_PICKAXE = HELPER.createItem("silver_pickaxe", () -> new SilverPickaxeItem(CCTiers.Tools.SILVER, 1, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_SHOVEL = HELPER.createItem("silver_shovel", () -> new SilverShovelItem(CCTiers.Tools.SILVER, 1.5F, -3.0F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_AXE = HELPER.createItem("silver_axe", () -> new SilverAxeItem(CCTiers.Tools.SILVER, 6.0F, -3.0F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_HOE = HELPER.createItem("silver_hoe", () -> new SilverHoeItem(CCTiers.Tools.SILVER, 0, -3.0F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_HELMET = HELPER.createItem("silver_helmet", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_CHESTPLATE = HELPER.createItem("silver_chestplate", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_LEGGINGS = HELPER.createItem("silver_leggings", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_BOOTS = HELPER.createItem("silver_boots", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_HORSE_ARMOR = HELPER.createItem("silver_horse_armor", () -> new HorseArmorItem(6, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_silver.png"), (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> NECROMIUM_INGOT = HELPER.createItem("necromium_ingot", () -> new Item(new Item.Properties().isImmuneToFire().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> NECROMIUM_NUGGET = HELPER.createCompatItem("nether_extension", "necromium_nugget", new Item.Properties().isImmuneToFire(), ItemGroup.MATERIALS);
	public static final RegistryObject<Item> NECROMIUM_SWORD = HELPER.createItem("necromium_sword", () -> new NecromiumSwordItem(CCTiers.Tools.NECROMIUM, 3, -2.4F, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_PICKAXE = HELPER.createItem("necromium_pickaxe", () -> new NecromiumPickaxeItem(CCTiers.Tools.NECROMIUM, 1, -2.8F, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_SHOVEL = HELPER.createItem("necromium_shovel", () -> new NecromiumShovelItem(CCTiers.Tools.NECROMIUM, 1.5F, -3.0F, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_AXE = HELPER.createItem("necromium_axe", () -> new NecromiumAxeItem(CCTiers.Tools.NECROMIUM, 5.0F, -3.0F, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_HOE = HELPER.createItem("necromium_hoe", () -> new NecromiumHoeItem(CCTiers.Tools.NECROMIUM, -4, -0.0F, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> NECROMIUM_HELMET = HELPER.createItem("necromium_helmet", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlotType.HEAD, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_CHESTPLATE = HELPER.createItem("necromium_chestplate", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlotType.CHEST, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_LEGGINGS = HELPER.createItem("necromium_leggings", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlotType.LEGS, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_BOOTS = HELPER.createItem("necromium_boots", () -> new NecromiumArmorItem(CCTiers.Armor.NECROMIUM, EquipmentSlotType.FEET, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> NECROMIUM_HORSE_ARMOR = HELPER.createItem("necromium_horse_armor", () -> new HorseArmorItem(9, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_necromium.png"), (new Item.Properties()).maxStackSize(1).isImmuneToFire().group(createCompatGroup("nether_extension", ItemGroup.MISC))));

	public static final RegistryObject<Item> ROTTEN_EGG = HELPER.createItem("rotten_egg", () -> new RottenEggItem(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> SUGILITE = HELPER.createItem("sugilite", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

	public static final RegistryObject<Item> CAVEFISH = HELPER.createItem("cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.CAVEFISH)));
	public static final RegistryObject<Item> COOKED_CAVEFISH = HELPER.createItem("cooked_cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.COOKED_CAVEFISH)));
	public static final RegistryObject<Item> CAVEFISH_BUCKET = HELPER.createItem("cavefish_bucket", () -> new FishBucketItem(CCEntities.CAVEFISH, () -> Fluids.WATER, new Item.Properties().maxStackSize(1).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> GOLDEN_BUCKET = HELPER.createItem("golden_bucket", () -> new GoldenBucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(16)));
	public static final RegistryObject<Item> GOLDEN_WATER_BUCKET = HELPER.createItem("golden_water_bucket", () -> new GoldenBucketItem(() -> Fluids.WATER, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_LAVA_BUCKET = HELPER.createItem("golden_lava_bucket", () -> new GoldenBucketItem(() -> Fluids.LAVA, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_MILK_BUCKET = HELPER.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));

	public static final RegistryObject<AbnormalsSpawnEggItem> CAVEFISH_SPAWN_EGG = HELPER.createSpawnEggItem("cavefish", CCEntities.CAVEFISH::get, 14145236, 11251356);
	public static final RegistryObject<AbnormalsSpawnEggItem> DEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("deeper", CCEntities.DEEPER::get, 8355711, 13717260);
	public static final RegistryObject<AbnormalsSpawnEggItem> FLY_SPAWN_EGG = HELPER.createSpawnEggItem("fly", CCEntities.FLY::get, 920336, 7080720);
	public static final RegistryObject<AbnormalsSpawnEggItem> ZOMBIE_CHICKEN_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_chicken", CCEntities.ZOMBIE_CHICKEN::get, 3430940, 9349983);
	public static final RegistryObject<AbnormalsSpawnEggItem> MIME_SPAWN_EGG = HELPER.createSpawnEggItem("mime", CCEntities.MIME::get, 0x5A5050, 0x969964);

	static class Foods {
		public static final Food CAVEFISH = new Food.Builder().hunger(1).saturation(0.3F).build();
		public static final Food COOKED_CAVEFISH = new Food.Builder().hunger(4).saturation(0.25F).build();
	}

	public static void registerItemProperties() {
		ItemModelsProperties.registerProperty(GOLDEN_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemModelsProperties.registerProperty(GOLDEN_WATER_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemModelsProperties.registerProperty(GOLDEN_LAVA_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemModelsProperties.registerProperty(GOLDEN_MILK_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemModelsProperties.registerProperty(ORE_DETECTOR.get(), new ResourceLocation("detect"), (stack, world, entity) -> {
			if (stack.hasTag() && stack.getTag().contains("Detecting") && entity instanceof PlayerEntity) {
				return OreDetectorItem.getDetectionData(stack) ? 1 : 0;
			} else {
				return 0;
			}
		});
	}

	public static ItemGroup createCompatGroup(String modid, ItemGroup group) {
		return ModList.get().isLoaded(modid) ? group : null;
	}
}
