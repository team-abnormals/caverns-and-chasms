package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.item.*;
import com.minecraftabnormals.caverns_and_chasms.common.item.silver.*;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCTiers;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCItems {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

	public static final RegistryObject<Item> SILVER_INGOT = HELPER.createItem("silver_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> SILVER_NUGGET = HELPER.createItem("silver_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> SILVER_ARROW = HELPER.createItem("silver_arrow", () -> new SilverArrowItem(new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> ORE_DETECTOR = HELPER.createItem("ore_detector", () -> new OreDetectorItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_SWORD = HELPER.createItem("silver_sword", () -> new SilverSwordItem(CCTiers.Tools.SILVER, 5, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_PICKAXE = HELPER.createItem("silver_pickaxe", () -> new SilverPickaxeItem(CCTiers.Tools.SILVER, 3, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_SHOVEL = HELPER.createItem("silver_shovel", () -> new SilverShovelItem(CCTiers.Tools.SILVER, 3.5F, -3.0F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_AXE = HELPER.createItem("silver_axe", () -> new SilverAxeItem(CCTiers.Tools.SILVER, 8, -3.1F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_HOE = HELPER.createItem("silver_hoe", () -> new SilverHoeItem(CCTiers.Tools.SILVER, 0, -0.5F, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> SILVER_HELMET = HELPER.createItem("silver_helmet", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_CHESTPLATE = HELPER.createItem("silver_chestplate", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_LEGGINGS = HELPER.createItem("silver_leggings", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_BOOTS = HELPER.createItem("silver_boots", () -> new SilverArmorItem(CCTiers.Armor.SILVER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> SILVER_HORSE_ARMOR = HELPER.createItem("silver_horse_armor", () -> new HorseArmorItem(6, new ResourceLocation(CavernsAndChasms.MODID, "textures/entity/horse/armor/horse_armor_silver.png"), (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> WARPITE_INGOT = HELPER.createItem("warpite_ingot", () -> new Item(new Item.Properties().isBurnable().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> WARPITE_NUGGET = HELPER.createCompatItem("nether_extension", "warpite_nugget", new Item.Properties().isBurnable(), ItemGroup.MATERIALS);
	public static final RegistryObject<Item> WARPITE_SWORD = HELPER.createItem("warpite_sword", () -> new SilverSwordItem(CCTiers.Tools.WARPITE, 5, -2.4F, new Item.Properties().isBurnable().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> WARPITE_PICKAXE = HELPER.createItem("warpite_pickaxe", () -> new SilverPickaxeItem(CCTiers.Tools.WARPITE, 3, -2.8F, new Item.Properties().isBurnable().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> WARPITE_SHOVEL = HELPER.createItem("warpite_shovel", () -> new SilverShovelItem(CCTiers.Tools.WARPITE, 3.5F, -3.0F, new Item.Properties().isBurnable().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> WARPITE_AXE = HELPER.createItem("warpite_axe", () -> new SilverAxeItem(CCTiers.Tools.WARPITE, 8, -3.1F, new Item.Properties().isBurnable().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> WARPITE_HOE = HELPER.createItem("warpite_hoe", () -> new SilverHoeItem(CCTiers.Tools.WARPITE, 0, -0.5F, new Item.Properties().isBurnable().group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> WARPITE_HELMET = HELPER.createItem("warpite_helmet", () -> new ArmorItem(CCTiers.Armor.WARPITE, EquipmentSlotType.HEAD, new Item.Properties().isBurnable().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> WARPITE_CHESTPLATE = HELPER.createItem("warpite_chestplate", () -> new ArmorItem(CCTiers.Armor.WARPITE, EquipmentSlotType.CHEST, new Item.Properties().isBurnable().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> WARPITE_LEGGINGS = HELPER.createItem("warpite_leggings", () -> new ArmorItem(CCTiers.Armor.WARPITE, EquipmentSlotType.LEGS, new Item.Properties().isBurnable().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> WARPITE_BOOTS = HELPER.createItem("warpite_boots", () -> new ArmorItem(CCTiers.Armor.WARPITE, EquipmentSlotType.FEET, new Item.Properties().isBurnable().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> WARPITE_HORSE_ARMOR = HELPER.createItem("warpite_horse_armor", () -> new HorseArmorItem(9, new ResourceLocation(CavernsAndChasms.MODID, "textures/entity/horse/armor/horse_armor_warpite.png"), (new Item.Properties()).maxStackSize(1).isBurnable().group(createCompatGroup("nether_extension", ItemGroup.MISC))));

	public static final RegistryObject<Item> ROTTEN_EGG = HELPER.createItem("rotten_egg", () -> new RottenEggItem(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> SUGILITE = HELPER.createItem("sugilite", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

	public static final RegistryObject<Item> CAVEFISH = HELPER.createItem("cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.CAVEFISH)));
	public static final RegistryObject<Item> COOKED_CAVEFISH = HELPER.createItem("cooked_cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.COOKED_CAVEFISH)));
	public static final RegistryObject<Item> CAVEFISH_BUCKET = HELPER.createItem("cavefish_bucket", () -> new FishBucketItem(CCEntities.CAVEFISH::get, () -> Fluids.WATER, new Item.Properties().maxStackSize(1).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> GOLDEN_BUCKET = HELPER.createItem("golden_bucket", () -> new GoldenBucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(16)));
	public static final RegistryObject<Item> GOLDEN_WATER_BUCKET = HELPER.createItem("golden_water_bucket", () -> new GoldenBucketItem(() -> Fluids.WATER, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_LAVA_BUCKET = HELPER.createItem("golden_lava_bucket", () -> new GoldenBucketItem(() -> Fluids.LAVA, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_MILK_BUCKET = HELPER.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));

	public static final RegistryObject<Item> CAVEFISH_SPAWN_EGG = HELPER.createSpawnEggItem("cavefish", CCEntities.CAVEFISH::get, 14145236, 11251356);
	public static final RegistryObject<Item> DEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("deeper", CCEntities.DEEPER::get, 8355711, 13717260);
	public static final RegistryObject<Item> FLY_SPAWN_EGG = HELPER.createSpawnEggItem("fly", CCEntities.FLY::get, 920336, 7080720);
	public static final RegistryObject<Item> ZOMBIE_CHICKEN_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_chicken", CCEntities.ZOMBIE_CHICKEN::get, 3430940, 9349983);

	static class Foods {
		public static final Food CAVEFISH = new Food.Builder().hunger(1).saturation(0.3F).build();
		public static final Food COOKED_CAVEFISH = new Food.Builder().hunger(4).saturation(0.25F).build();
	}

	public static void registerItemProperties() {
		ItemModelsProperties.func_239418_a_(GOLDEN_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_WATER_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_LAVA_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_MILK_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(ORE_DETECTOR.get(), new ResourceLocation("detect"), (stack, world, entity) -> {
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
