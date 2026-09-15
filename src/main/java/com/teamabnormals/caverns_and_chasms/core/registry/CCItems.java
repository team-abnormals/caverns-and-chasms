package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.item.BlueprintBoatItem;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.common.item.*;
import com.teamabnormals.caverns_and_chasms.common.item.component.PackingContainerContents;
import com.teamabnormals.caverns_and_chasms.common.item.copper.*;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.KunaiItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.LargeArrowItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCEnums;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCItemTiers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBannerPatternTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCJukeboxSongs;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimPatterns;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCItemSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.integration.boatload.CCBoatTypes;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class CCItems {
	public static final CCItemSubRegistryHelper ITEMS = CavernsAndChasms.REGISTRY_HELPER.getItemSubHelper();

	public static final DeferredItem<Item> TUNING_FORK = ITEMS.createItem("tuning_fork", () -> new TuningForkItem(new Item.Properties().stacksTo(1)));
	public static final DeferredItem<Item> BAROMETER = ITEMS.createItem("barometer", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> OXIDIZED_COPPER_GOLEM = ITEMS.createItem("oxidized_copper_golem", () -> new OxidizedCopperGolemItem(new Item.Properties().stacksTo(1), false));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_GOLEM = ITEMS.createItem("waxed_oxidized_copper_golem", () -> new OxidizedCopperGolemItem(new Item.Properties().stacksTo(1), true));
	public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.createItem("copper_nugget", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> TRIAL_TOKEN = ITEMS.createItem("trial_token", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> OMINOUS_TRIAL_TOKEN = ITEMS.createItem("ominous_trial_token", () -> new Item(new Item.Properties()));

	public static final DeferredItem<Item> EXPOSED_COPPER_INGOT = ITEMS.createItem("exposed_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WEATHERED_COPPER_INGOT = ITEMS.createItem("weathered_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> OXIDIZED_COPPER_INGOT = ITEMS.createItem("oxidized_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WAXED_COPPER_INGOT = ITEMS.createItem("waxed_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_INGOT = ITEMS.createItem("waxed_exposed_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_INGOT = ITEMS.createItem("waxed_weathered_copper_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_INGOT = ITEMS.createItem("waxed_oxidized_copper_ingot", () -> new Item(new Item.Properties()));

	public static final DeferredItem<Item> COPPER_HELMET = ITEMS.createItem("copper_helmet", () -> new WeatheringCopperArmorItem(WeatherState.UNAFFECTED, CCArmorMaterials.COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> COPPER_CHESTPLATE = ITEMS.createItem("copper_chestplate", () -> new WeatheringCopperArmorItem(WeatherState.UNAFFECTED, CCArmorMaterials.COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> COPPER_LEGGINGS = ITEMS.createItem("copper_leggings", () -> new WeatheringCopperArmorItem(WeatherState.UNAFFECTED, CCArmorMaterials.COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> COPPER_BOOTS = ITEMS.createItem("copper_boots", () -> new WeatheringCopperArmorItem(WeatherState.UNAFFECTED, CCArmorMaterials.COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> COPPER_SWORD = ITEMS.createItem("copper_sword", () -> new WeatheringSwordItem(WeatherState.UNAFFECTED, CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> COPPER_SHOVEL = ITEMS.createItem("copper_shovel", () -> new WeatheringShovelItem(WeatherState.UNAFFECTED, CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.createItem("copper_pickaxe", () -> new WeatheringPickaxeItem(WeatherState.UNAFFECTED, CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> COPPER_AXE = ITEMS.createItem("copper_axe", () -> new WeatheringAxeItem(WeatherState.UNAFFECTED, CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> COPPER_HOE = ITEMS.createItem("copper_hoe", () -> new WeatheringHoeItem(WeatherState.UNAFFECTED, CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> COPPER_HORSE_ARMOR = ITEMS.createItem("copper_horse_armor", () -> new WeatheringHorseArmorItem(WeatherState.UNAFFECTED, CCArmorMaterials.COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> EXPOSED_COPPER_HELMET = ITEMS.createItem("exposed_copper_helmet", () -> new WeatheringCopperArmorItem(WeatherState.EXPOSED, CCArmorMaterials.EXPOSED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> EXPOSED_COPPER_CHESTPLATE = ITEMS.createItem("exposed_copper_chestplate", () -> new WeatheringCopperArmorItem(WeatherState.EXPOSED, CCArmorMaterials.EXPOSED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> EXPOSED_COPPER_LEGGINGS = ITEMS.createItem("exposed_copper_leggings", () -> new WeatheringCopperArmorItem(WeatherState.EXPOSED, CCArmorMaterials.EXPOSED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> EXPOSED_COPPER_BOOTS = ITEMS.createItem("exposed_copper_boots", () -> new WeatheringCopperArmorItem(WeatherState.EXPOSED, CCArmorMaterials.EXPOSED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> EXPOSED_COPPER_SWORD = ITEMS.createItem("exposed_copper_sword", () -> new WeatheringSwordItem(WeatherState.EXPOSED, CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> EXPOSED_COPPER_SHOVEL = ITEMS.createItem("exposed_copper_shovel", () -> new WeatheringShovelItem(WeatherState.EXPOSED, CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> EXPOSED_COPPER_PICKAXE = ITEMS.createItem("exposed_copper_pickaxe", () -> new WeatheringPickaxeItem(WeatherState.EXPOSED, CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> EXPOSED_COPPER_AXE = ITEMS.createItem("exposed_copper_axe", () -> new WeatheringAxeItem(WeatherState.EXPOSED, CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> EXPOSED_COPPER_HOE = ITEMS.createItem("exposed_copper_hoe", () -> new WeatheringHoeItem(WeatherState.EXPOSED, CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> EXPOSED_COPPER_HORSE_ARMOR = ITEMS.createItem("exposed_copper_horse_armor", () -> new WeatheringHorseArmorItem(WeatherState.EXPOSED, CCArmorMaterials.EXPOSED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> WEATHERED_COPPER_HELMET = ITEMS.createItem("weathered_copper_helmet", () -> new WeatheringCopperArmorItem(WeatherState.WEATHERED, CCArmorMaterials.WEATHERED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> WEATHERED_COPPER_CHESTPLATE = ITEMS.createItem("weathered_copper_chestplate", () -> new WeatheringCopperArmorItem(WeatherState.WEATHERED, CCArmorMaterials.WEATHERED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> WEATHERED_COPPER_LEGGINGS = ITEMS.createItem("weathered_copper_leggings", () -> new WeatheringCopperArmorItem(WeatherState.WEATHERED, CCArmorMaterials.WEATHERED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> WEATHERED_COPPER_BOOTS = ITEMS.createItem("weathered_copper_boots", () -> new WeatheringCopperArmorItem(WeatherState.WEATHERED, CCArmorMaterials.WEATHERED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> WEATHERED_COPPER_SWORD = ITEMS.createItem("weathered_copper_sword", () -> new WeatheringSwordItem(WeatherState.WEATHERED, CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> WEATHERED_COPPER_SHOVEL = ITEMS.createItem("weathered_copper_shovel", () -> new WeatheringShovelItem(WeatherState.WEATHERED, CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> WEATHERED_COPPER_PICKAXE = ITEMS.createItem("weathered_copper_pickaxe", () -> new WeatheringPickaxeItem(WeatherState.WEATHERED, CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> WEATHERED_COPPER_AXE = ITEMS.createItem("weathered_copper_axe", () -> new WeatheringAxeItem(WeatherState.WEATHERED, CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> WEATHERED_COPPER_HOE = ITEMS.createItem("weathered_copper_hoe", () -> new WeatheringHoeItem(WeatherState.WEATHERED, CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> WEATHERED_COPPER_HORSE_ARMOR = ITEMS.createItem("weathered_copper_horse_armor", () -> new WeatheringHorseArmorItem(WeatherState.WEATHERED, CCArmorMaterials.WEATHERED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> OXIDIZED_COPPER_HELMET = ITEMS.createItem("oxidized_copper_helmet", () -> new WeatheringCopperArmorItem(WeatherState.OXIDIZED, CCArmorMaterials.OXIDIZED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_CHESTPLATE = ITEMS.createItem("oxidized_copper_chestplate", () -> new WeatheringCopperArmorItem(WeatherState.OXIDIZED, CCArmorMaterials.OXIDIZED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_LEGGINGS = ITEMS.createItem("oxidized_copper_leggings", () -> new WeatheringCopperArmorItem(WeatherState.OXIDIZED, CCArmorMaterials.OXIDIZED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_BOOTS = ITEMS.createItem("oxidized_copper_boots", () -> new WeatheringCopperArmorItem(WeatherState.OXIDIZED, CCArmorMaterials.OXIDIZED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_SWORD = ITEMS.createItem("oxidized_copper_sword", () -> new WeatheringSwordItem(WeatherState.OXIDIZED, CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_SHOVEL = ITEMS.createItem("oxidized_copper_shovel", () -> new WeatheringShovelItem(WeatherState.OXIDIZED, CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_PICKAXE = ITEMS.createItem("oxidized_copper_pickaxe", () -> new WeatheringPickaxeItem(WeatherState.OXIDIZED, CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_AXE = ITEMS.createItem("oxidized_copper_axe", () -> new WeatheringAxeItem(WeatherState.OXIDIZED, CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_HOE = ITEMS.createItem("oxidized_copper_hoe", () -> new WeatheringHoeItem(WeatherState.OXIDIZED, CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> OXIDIZED_COPPER_HORSE_ARMOR = ITEMS.createItem("oxidized_copper_horse_armor", () -> new WeatheringHorseArmorItem(WeatherState.OXIDIZED, CCArmorMaterials.OXIDIZED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> WAXED_COPPER_HELMET = ITEMS.createItem("waxed_copper_helmet", () -> new CopperArmorItem(CCArmorMaterials.COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> WAXED_COPPER_CHESTPLATE = ITEMS.createItem("waxed_copper_chestplate", () -> new CopperArmorItem(CCArmorMaterials.COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> WAXED_COPPER_LEGGINGS = ITEMS.createItem("waxed_copper_leggings", () -> new CopperArmorItem(CCArmorMaterials.COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_COPPER_BOOTS = ITEMS.createItem("waxed_copper_boots", () -> new CopperArmorItem(CCArmorMaterials.COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_COPPER_SWORD = ITEMS.createItem("waxed_copper_sword", () -> new SwordItem(CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> WAXED_COPPER_SHOVEL = ITEMS.createItem("waxed_copper_shovel", () -> new ShovelItem(CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> WAXED_COPPER_PICKAXE = ITEMS.createItem("waxed_copper_pickaxe", () -> new PickaxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> WAXED_COPPER_AXE = ITEMS.createItem("waxed_copper_axe", () -> new AxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> WAXED_COPPER_HOE = ITEMS.createItem("waxed_copper_hoe", () -> new HoeItem(CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> WAXED_COPPER_HORSE_ARMOR = ITEMS.createItem("waxed_copper_horse_armor", () -> new CopperHorseArmorItem(CCArmorMaterials.COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_HELMET = ITEMS.createItem("waxed_exposed_copper_helmet", () -> new CopperArmorItem(CCArmorMaterials.EXPOSED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_CHESTPLATE = ITEMS.createItem("waxed_exposed_copper_chestplate", () -> new CopperArmorItem(CCArmorMaterials.EXPOSED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_LEGGINGS = ITEMS.createItem("waxed_exposed_copper_leggings", () -> new CopperArmorItem(CCArmorMaterials.EXPOSED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_BOOTS = ITEMS.createItem("waxed_exposed_copper_boots", () -> new CopperArmorItem(CCArmorMaterials.EXPOSED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_SWORD = ITEMS.createItem("waxed_exposed_copper_sword", () -> new SwordItem(CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_SHOVEL = ITEMS.createItem("waxed_exposed_copper_shovel", () -> new ShovelItem(CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_PICKAXE = ITEMS.createItem("waxed_exposed_copper_pickaxe", () -> new PickaxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_AXE = ITEMS.createItem("waxed_exposed_copper_axe", () -> new AxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_HOE = ITEMS.createItem("waxed_exposed_copper_hoe", () -> new HoeItem(CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> WAXED_EXPOSED_COPPER_HORSE_ARMOR = ITEMS.createItem("waxed_exposed_copper_horse_armor", () -> new CopperHorseArmorItem(CCArmorMaterials.EXPOSED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_HELMET = ITEMS.createItem("waxed_weathered_copper_helmet", () -> new CopperArmorItem(CCArmorMaterials.WEATHERED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_CHESTPLATE = ITEMS.createItem("waxed_weathered_copper_chestplate", () -> new CopperArmorItem(CCArmorMaterials.WEATHERED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_LEGGINGS = ITEMS.createItem("waxed_weathered_copper_leggings", () -> new CopperArmorItem(CCArmorMaterials.WEATHERED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_BOOTS = ITEMS.createItem("waxed_weathered_copper_boots", () -> new CopperArmorItem(CCArmorMaterials.WEATHERED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_SWORD = ITEMS.createItem("waxed_weathered_copper_sword", () -> new SwordItem(CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_SHOVEL = ITEMS.createItem("waxed_weathered_copper_shovel", () -> new ShovelItem(CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_PICKAXE = ITEMS.createItem("waxed_weathered_copper_pickaxe", () -> new PickaxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_AXE = ITEMS.createItem("waxed_weathered_copper_axe", () -> new AxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_HOE = ITEMS.createItem("waxed_weathered_copper_hoe", () -> new HoeItem(CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> WAXED_WEATHERED_COPPER_HORSE_ARMOR = ITEMS.createItem("waxed_weathered_copper_horse_armor", () -> new CopperHorseArmorItem(CCArmorMaterials.WEATHERED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_HELMET = ITEMS.createItem("waxed_oxidized_copper_helmet", () -> new CopperArmorItem(CCArmorMaterials.OXIDIZED_COPPER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(56))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_CHESTPLATE = ITEMS.createItem("waxed_oxidized_copper_chestplate", () -> new CopperArmorItem(CCArmorMaterials.OXIDIZED_COPPER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(56))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_LEGGINGS = ITEMS.createItem("waxed_oxidized_copper_leggings", () -> new CopperArmorItem(CCArmorMaterials.OXIDIZED_COPPER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_BOOTS = ITEMS.createItem("waxed_oxidized_copper_boots", () -> new CopperArmorItem(CCArmorMaterials.OXIDIZED_COPPER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(56))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_SWORD = ITEMS.createItem("waxed_oxidized_copper_sword", () -> new SwordItem(CCItemTiers.COPPER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.COPPER, 3, -2.4F))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_SHOVEL = ITEMS.createItem("waxed_oxidized_copper_shovel", () -> new ShovelItem(CCItemTiers.COPPER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.COPPER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_PICKAXE = ITEMS.createItem("waxed_oxidized_copper_pickaxe", () -> new PickaxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.COPPER, 1, -2.8F))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_AXE = ITEMS.createItem("waxed_oxidized_copper_axe", () -> new AxeItem(CCItemTiers.COPPER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.COPPER, 7.0F, -3.2F))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_HOE = ITEMS.createItem("waxed_oxidized_copper_hoe", () -> new HoeItem(CCItemTiers.COPPER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.COPPER, -1.0F, -2.0F))));
	public static final DeferredItem<Item> WAXED_OXIDIZED_COPPER_HORSE_ARMOR = ITEMS.createItem("waxed_oxidized_copper_horse_armor", () -> new CopperHorseArmorItem(CCArmorMaterials.OXIDIZED_COPPER, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> NETHERITE_NUGGET = ITEMS.createItem("netherite_nugget", () -> new Item(new Item.Properties().fireResistant()));
	public static final DeferredItem<Item> NETHERITE_HORSE_ARMOR = ITEMS.createItem("netherite_horse_armor", () -> new NetheriteHorseArmorItem(ArmorMaterials.NETHERITE, new Item.Properties().fireResistant().stacksTo(1)));

	public static final DeferredItem<Item> RAW_SILVER = ITEMS.createItem("raw_silver", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SILVER_INGOT = ITEMS.createItem("silver_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SILVER_NUGGET = ITEMS.createItem("silver_nugget", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> KUNAI = ITEMS.createItem("kunai", () -> new KunaiItem(new Item.Properties()));
	public static final DeferredItem<Item> SILVER_SWORD = ITEMS.createItem("silver_sword", () -> new SwordItem(CCItemTiers.SILVER, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.SILVER, 1, -2.4F))));
	public static final DeferredItem<Item> SILVER_SHOVEL = ITEMS.createItem("silver_shovel", () -> new ShovelItem(CCItemTiers.SILVER, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.SILVER, 1.5F, -3.0F))));
	public static final DeferredItem<Item> SILVER_PICKAXE = ITEMS.createItem("silver_pickaxe", () -> new PickaxeItem(CCItemTiers.SILVER, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.SILVER, 1, -2.8F))));
	public static final DeferredItem<Item> SILVER_AXE = ITEMS.createItem("silver_axe", () -> new AxeItem(CCItemTiers.SILVER, new Item.Properties().attributes(AxeItem.createAttributes(CCItemTiers.SILVER, 4.0F, -3.0F))));
	public static final DeferredItem<Item> SILVER_HOE = ITEMS.createItem("silver_hoe", () -> new HoeItem(CCItemTiers.SILVER, new Item.Properties().attributes(HoeItem.createAttributes(CCItemTiers.SILVER, 0.0F, -3.0F))));
	public static final DeferredItem<Item> SILVER_HELMET = ITEMS.createItem("silver_helmet", () -> new SilverArmorItem(CCArmorMaterials.SILVER, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(11))));
	public static final DeferredItem<Item> SILVER_CHESTPLATE = ITEMS.createItem("silver_chestplate", () -> new SilverArmorItem(CCArmorMaterials.SILVER, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(11))));
	public static final DeferredItem<Item> SILVER_LEGGINGS = ITEMS.createItem("silver_leggings", () -> new SilverArmorItem(CCArmorMaterials.SILVER, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(11))));
	public static final DeferredItem<Item> SILVER_BOOTS = ITEMS.createItem("silver_boots", () -> new SilverArmorItem(CCArmorMaterials.SILVER, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(11))));
	public static final DeferredItem<Item> SILVER_HORSE_ARMOR = ITEMS.createItem("silver_horse_armor", () -> new SilverHorseArmorItem(CCArmorMaterials.SILVER, new Item.Properties().stacksTo(1)));
	public static final DeferredItem<Item> DEPTH_GAUGE = ITEMS.createItem("depth_gauge", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> LARGE_ARROW = ITEMS.createItem("large_arrow", () -> new LargeArrowItem(new Item.Properties()));

	public static final DeferredItem<Item> NECROMIUM_INGOT = ITEMS.createItem("necromium_ingot", () -> new Item(new Item.Properties().fireResistant()));
	public static final DeferredItem<Item> NECROMIUM_NUGGET = ITEMS.createItem("necromium_nugget", () -> new Item(new Item.Properties().fireResistant()));
	public static final DeferredItem<Item> NECROMIUM_SWORD = ITEMS.createItem("necromium_sword", () -> new SwordItem(CCItemTiers.NECROMIUM, new Item.Properties().attributes(SwordItem.createAttributes(CCItemTiers.NECROMIUM, 3, -2.4F)).fireResistant()));
	public static final DeferredItem<Item> NECROMIUM_SHOVEL = ITEMS.createItem("necromium_shovel", () -> new ShovelItem(CCItemTiers.NECROMIUM, new Item.Properties().attributes(ShovelItem.createAttributes(CCItemTiers.NECROMIUM, 1.5F, -3.0F)).fireResistant()));
	public static final DeferredItem<Item> NECROMIUM_PICKAXE = ITEMS.createItem("necromium_pickaxe", () -> new PickaxeItem(CCItemTiers.NECROMIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(CCItemTiers.NECROMIUM, 1, -2.8F)).fireResistant()));
	public static final DeferredItem<Item> NECROMIUM_AXE = ITEMS.createItem("necromium_axe", () -> new AxeItem(CCItemTiers.NECROMIUM, new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(CCItemTiers.NECROMIUM, 5.0F, -3.0F))));
	public static final DeferredItem<Item> NECROMIUM_HOE = ITEMS.createItem("necromium_hoe", () -> new HoeItem(CCItemTiers.NECROMIUM, new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(CCItemTiers.NECROMIUM, -3.0F, 0.0F))));
	public static final DeferredItem<Item> NECROMIUM_HELMET = ITEMS.createItem("necromium_helmet", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, Type.HELMET, new Item.Properties().fireResistant().durability(Type.HELMET.getDurability(37))));
	public static final DeferredItem<Item> NECROMIUM_CHESTPLATE = ITEMS.createItem("necromium_chestplate", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, Type.CHESTPLATE, new Item.Properties().fireResistant().durability(Type.CHESTPLATE.getDurability(37))));
	public static final DeferredItem<Item> NECROMIUM_LEGGINGS = ITEMS.createItem("necromium_leggings", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, Type.LEGGINGS, new Item.Properties().fireResistant().durability(Type.LEGGINGS.getDurability(37))));
	public static final DeferredItem<Item> NECROMIUM_BOOTS = ITEMS.createItem("necromium_boots", () -> new NecromiumArmorItem(CCArmorMaterials.NECROMIUM, Type.BOOTS, new Item.Properties().fireResistant().durability(Type.BOOTS.getDurability(37))));
	public static final DeferredItem<Item> NECROMIUM_HORSE_ARMOR = ITEMS.createItem("necromium_horse_armor", () -> new NecromiumHorseArmorItem(CCArmorMaterials.NECROMIUM, new Item.Properties().stacksTo(1).fireResistant()));

	public static final DeferredItem<Item> LIVING_FLESH = ITEMS.createItem("living_flesh", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SANGUINE_HELMET = ITEMS.createItem("sanguine_helmet", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(23))));
	public static final DeferredItem<Item> SANGUINE_CHESTPLATE = ITEMS.createItem("sanguine_chestplate", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, Type.CHESTPLATE, new Item.Properties().durability(Type.CHESTPLATE.getDurability(23))));
	public static final DeferredItem<Item> SANGUINE_LEGGINGS = ITEMS.createItem("sanguine_leggings", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(23))));
	public static final DeferredItem<Item> SANGUINE_BOOTS = ITEMS.createItem("sanguine_boots", () -> new SanguineArmorItem(CCArmorMaterials.SANGUINE, Type.BOOTS, new Item.Properties().durability(Type.BOOTS.getDurability(23))));

	public static final DeferredItem<Item> RAW_TIN = ITEMS.createItem("raw_tin", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> TIN_INGOT = ITEMS.createItem("tin_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> TIN_NUGGET = ITEMS.createItem("tin_nugget", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> TINPLATE = ITEMS.createItem("tinplate", () -> new HoneycombItem(new Item.Properties()));
	public static final DeferredItem<Item> RICOCHET_ARROW = ITEMS.createItem("ricochet_arrow", () -> new RicochetArrowItem(new Item.Properties()));
	public static final DeferredItem<Item> PACKING_CONTAINER = ITEMS.createItem("packing_container", () -> new PackingContainerItem(new Item.Properties().stacksTo(1).component(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY)));
	public static final DeferredItem<Item> AEGIS = ITEMS.createItem("aegis", () -> new AegisItem(new Item.Properties().durability(945)));

	public static final DeferredItem<Item> TURQUOISE = ITEMS.createItem("turquoise", () -> new Item(new Item.Properties().rarity(CCEnums.FANCY.getValue())));
	public static final DeferredItem<Item> CAVIAR = ITEMS.createItem("caviar", () -> new CaviarItem(new Item.Properties().stacksTo(1).rarity(CCEnums.FANCY.getValue()).food(CCFoods.CAVIAR)));
	public static final DeferredItem<Item> MONOCLE = ITEMS.createItem("monocle", () -> new MonocleItem(new Item.Properties().stacksTo(1).rarity(CCEnums.FANCY.getValue())));
	public static final DeferredItem<Item> UNICORN_HORN = ITEMS.createItem("unicorn_horn", () -> new UnicornHornItem((new Item.Properties()).stacksTo(1).rarity(CCEnums.FANCY.getValue())));

	public static final DeferredItem<Item> SPINEL = ITEMS.createItem("spinel", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> BEJEWELED_PEARL = ITEMS.createItem("bejeweled_pearl", () -> new BejeweledPearlItem(new Item.Properties().stacksTo(16)));
	public static final DeferredItem<Item> BEJEWELED_APPLE = ITEMS.createItem("bejeweled_apple", () -> new BejeweledAppleItem(new Item.Properties().food(CCFoods.BEJEWELED_APPLE).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> TETHER_POTION = ITEMS.createItem("tether_potion", () -> new TetherPotionItem((new Item.Properties()).stacksTo(1)));
	public static final DeferredItem<Item> IMPACT_POTION = ITEMS.createItem("impact_potion", () -> new ImpactPotionItem(new Item.Properties().stacksTo(1)));
	public static final DeferredItem<Item> TRAIL_POTION = ITEMS.createItem("trail_potion", () -> new TrailPotionItem(new Item.Properties().stacksTo(1)));
	public static final DeferredItem<Item> BLUNT_ARROW = ITEMS.createItem("blunt_arrow", () -> new BluntArrowItem(new Item.Properties()));
	public static final DeferredItem<Item> TMT_MINECART = ITEMS.createItem("tmt_minecart", () -> new TmtMinecartItem(new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> ZIRCONIA = ITEMS.createItem("zirconia", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> MUSIC_DISC_COPY = ITEMS.createItem("music_disc_copy", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

	public static final DeferredItem<Item> DEEPER_HEAD = ITEMS.createItem("deeper_head", () -> new StandingAndWallBlockItem(CCBlocks.DEEPER_HEAD.get(), CCBlocks.DEEPER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
	public static final DeferredItem<Item> EVENDEEPER_HEAD = ITEMS.createItem("evendeeper_head", () -> new StandingAndWallBlockItem(CCBlocks.EVENDEEPER_HEAD.get(), CCBlocks.EVENDEEPER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
	public static final DeferredItem<Item> PEEPER_HEAD = ITEMS.createItem("peeper_head", () -> new StandingAndWallBlockItem(CCBlocks.PEEPER_HEAD.get(), CCBlocks.PEEPER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
	public static final DeferredItem<Item> MIME_HEAD = ITEMS.createItem("mime_head", () -> new StandingAndWallBlockItem(CCBlocks.MIME_HEAD.get(), CCBlocks.MIME_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));

	public static final DeferredItem<Item> GOLDEN_BUCKET = ITEMS.createItem("golden_bucket", () -> new GoldenBucketItem(Fluids.EMPTY, new Item.Properties().stacksTo(16)));
	public static final DeferredItem<Item> GOLDEN_WATER_BUCKET = ITEMS.createItem("golden_water_bucket", () -> new GoldenBucketItem(Fluids.WATER, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final DeferredItem<Item> GOLDEN_LAVA_BUCKET = ITEMS.createItem("golden_lava_bucket", () -> new GoldenBucketItem(Fluids.LAVA, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final DeferredItem<Item> GOLDEN_POWDER_SNOW_BUCKET = ITEMS.createItem("golden_powder_snow_bucket", () -> new GoldenSolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.POWDER_SNOW_PLACE, new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));
	public static final DeferredItem<Item> GOLDEN_MILK_BUCKET = ITEMS.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().craftRemainder(GOLDEN_BUCKET.get()).stacksTo(1)));

	public static final Pair<DeferredItem<BlueprintBoatItem>, DeferredItem<BlueprintBoatItem>> AZALEA_BOAT = ITEMS.createBoatAndChestBoatItem("azalea", CCBlocks.AZALEA_PLANKS);
	public static final DeferredItem<Item> AZALEA_FURNACE_BOAT = ITEMS.createItem("azalea_furnace_boat", ModList.get().isLoaded("boatload") ? CCBoatTypes.AZALEA_FURNACE_BOAT : () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> LARGE_AZALEA_BOAT = ITEMS.createItem("large_azalea_boat", ModList.get().isLoaded("boatload") ? CCBoatTypes.LARGE_AZALEA_BOAT : () -> new Item(new Item.Properties()));

	public static final DeferredItem<Item> COPPER_HORN = ITEMS.createItem("copper_horn", () -> new CopperHornItem((new Item.Properties()).stacksTo(1), CCInstrumentTags.HARMONY_COPPER_HORNS, CCInstrumentTags.MELODY_COPPER_HORNS, CCInstrumentTags.BASS_COPPER_HORNS));
	public static final DeferredItem<Item> LOST_GOAT_HORN = ITEMS.createItem("lost_goat_horn", () -> new InstrumentItem((new Item.Properties()).stacksTo(1), CCInstrumentTags.LOST_GOAT_HORNS));
	public static final DeferredItem<Item> BONE_FLUTE = ITEMS.createItem("bone_flute", () -> new BoneFluteItem(new Item.Properties().stacksTo(1)));

	public static final DeferredItem<Item> COWL = ITEMS.createItem("cowl", () -> new CowlItem(CCArmorMaterials.COWL, Type.HELMET, new Item.Properties().durability(Type.HELMET.getDurability(5))));
	public static final DeferredItem<Item> TOOLBELT = ITEMS.createItem("toolbelt", () -> new ToolbeltItem(CCArmorMaterials.TOOLBELT, Type.LEGGINGS, new Item.Properties().durability(Type.LEGGINGS.getDurability(5))));

	public static final DeferredItem<Item> MUSIC_DISC_ANALOGUE = ITEMS.createItem("music_disc_analogue", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(CCJukeboxSongs.ANALOGUE)));
	public static final DeferredItem<Item> MUSIC_DISC_EPILOGUE = ITEMS.createItem("music_disc_epilogue", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(CCJukeboxSongs.EPILOGUE)));
	public static final DeferredItem<Item> ABNORMALS_BANNER_PATTERN = ITEMS.createItem("abnormals_banner_pattern", () -> new BannerPatternItem(CCBannerPatternTags.PATTERN_ITEM_ABNORMALS, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

	public static final DeferredItem<Item> TRIM_MODIFIER_SMITHING_TEMPLATE = ITEMS.createItem("trim_modifier_smithing_template", TrimModifierSmithingTemplateItem::createTrimModifierTemplate);
	public static final DeferredItem<Item> EXILE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("exile_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.EXILE));
	public static final DeferredItem<Item> CORE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("core_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.CORE));
	public static final DeferredItem<Item> FORGER_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("forger_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.FORGER));
	public static final DeferredItem<Item> IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("immolate_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.IMMOLATE));
	public static final DeferredItem<Item> PLATE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("plate_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.PLATE));
	public static final DeferredItem<Item> RIM_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.createItem("rim_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(CCTrimPatterns.RIM));

	public static final DeferredItem<Item> BOOM_POTTERY_SHERD = ITEMS.createItem("boom_pottery_sherd", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> CAST_POTTERY_SHERD = ITEMS.createItem("cast_pottery_sherd", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> RIDE_POTTERY_SHERD = ITEMS.createItem("ride_pottery_sherd", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> STALKER_POTTERY_SHERD = ITEMS.createItem("stalker_pottery_sherd", () -> new Item(new Item.Properties()));

	public static final DeferredItem<Item> DIMMER = ITEMS.createItem("dimmer", () -> new DimmerBlockItem(CCBlocks.DIMMER.get(), CCBlocks.WALL_DIMMER.get(), new Item.Properties()));
	public static final DeferredItem<Item> ROLLER_DOOR = ITEMS.createMovingDoorItem("roller_door", MovingDoorType.ROLLER_DOOR, CCBlocks.ROLLER_DOOR);
	public static final DeferredItem<Item> ROLLER_WINDOW = ITEMS.createMovingDoorItem("roller_window", MovingDoorType.ROLLER_WINDOW, CCBlocks.ROLLER_DOOR);

	public static final DeferredItem<Item> CAVEFISH = ITEMS.createItem("cavefish", () -> new Item(new Item.Properties().food(CCFoods.CAVEFISH)));
	public static final DeferredItem<Item> CAVEFISH_BUCKET = ITEMS.createItem("cavefish_bucket", () -> new MobBucketItem(CCEntityTypes.CAVEFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

	public static final DeferredItem<DeferredSpawnEggItem> DEEPER_SPAWN_EGG = ITEMS.createSpawnEggItem("deeper", CCEntityTypes.DEEPER::get, 8355711, 13717260);
	public static final DeferredItem<DeferredSpawnEggItem> EVENDEEPER_SPAWN_EGG = ITEMS.createSpawnEggItem("evendeeper", CCEntityTypes.EVENDEEPER::get, 3092279, 13717260);
	public static final DeferredItem<DeferredSpawnEggItem> PEEPER_SPAWN_EGG = ITEMS.createSpawnEggItem("peeper", CCEntityTypes.PEEPER::get, 0x3E3434, 0x694242);
	//	public static final DeferredItem<DeferredSpawnEggItem> FLY_SPAWN_EGG = HELPER.createSpawnEggItem("fly", CCEntityTypes.FLY::get, 920336, 7080720);
	public static final DeferredItem<DeferredSpawnEggItem> MIME_SPAWN_EGG = ITEMS.createSpawnEggItem("mime", CCEntityTypes.MIME::get, 0x5A5050, 0x969964);
	public static final DeferredItem<DeferredSpawnEggItem> RAT_SPAWN_EGG = ITEMS.createSpawnEggItem("rat", CCEntityTypes.RAT::get, 0x3B4248, 0xA76E6C);
	public static final DeferredItem<DeferredSpawnEggItem> CAVEFISH_SPAWN_EGG = ITEMS.createSpawnEggItem("cavefish", CCEntityTypes.CAVEFISH::get, 0xF2DCD5, 0xEA7872);
	public static final DeferredItem<DeferredSpawnEggItem> GLARE_SPAWN_EGG = ITEMS.createSpawnEggItem("glare", CCEntityTypes.GLARE::get, 0x72942F, 0x516F2C);
	public static final DeferredItem<DeferredSpawnEggItem> COPPER_GOLEM_SPAWN_EGG = ITEMS.createSpawnEggItem("copper_golem", CCEntityTypes.COPPER_GOLEM::get, 0xDE7D65, 0x8A4129);
	public static final DeferredItem<DeferredSpawnEggItem> GRAZER_SPAWN_EGG = ITEMS.createSpawnEggItem("grazer", CCEntityTypes.GRAZER::get, 0x838C8B, 0xE0B455);
	public static final DeferredItem<DeferredSpawnEggItem> SADDLED_GRAZER_SPAWN_EGG = ITEMS.createSpawnEggItem("saddled_grazer", CCEntityTypes.SADDLED_GRAZER::get, 0x838C8B, 0xB73B37);

	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(CavernsAndChasms.MOD_ID)
				.tab(FOOD_AND_DRINKS)
				.addItemsBefore(of(Items.TROPICAL_FISH), CAVEFISH)
				.addItemsBefore(of(Items.GOLDEN_APPLE), BEJEWELED_APPLE)
				.addItemsBefore(of(Items.MILK_BUCKET), CAVIAR)
				.addItemsAfter(of(Items.MILK_BUCKET), GOLDEN_MILK_BUCKET)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsAfter(of(Items.ARMOR_STAND), OXIDIZED_COPPER_GOLEM, WAXED_OXIDIZED_COPPER_GOLEM)
				.addItemsAfter(of(Items.CREEPER_HEAD), DEEPER_HEAD, EVENDEEPER_HEAD, PEEPER_HEAD, MIME_HEAD)
				.tab(INGREDIENTS)
				.addItemsAfter(of(Items.RAW_COPPER), RAW_TIN)
				.addItemsAfter(of(Items.RAW_GOLD), RAW_SILVER)
				.addItemsAfter(of(Items.LAPIS_LAZULI), SPINEL, TURQUOISE, ZIRCONIA)
				.addItemsBefore(of(Items.GOLD_NUGGET), COPPER_NUGGET, TIN_NUGGET)
				.addItemsAfter(of(Items.GOLD_NUGGET), SILVER_NUGGET, NETHERITE_NUGGET, NECROMIUM_NUGGET)
				.addItemsAfter(of(Items.COPPER_INGOT), TIN_INGOT)
				.addItemsAfter(of(Items.GOLD_INGOT), SILVER_INGOT)
				.addItemsAfter(of(Items.NETHERITE_INGOT), NECROMIUM_INGOT, LIVING_FLESH)
				.addItemsAfter(of(Items.MOJANG_BANNER_PATTERN), ABNORMALS_BANNER_PATTERN)
				.addItemsAfter(of(Items.HONEYCOMB), TINPLATE)
				.addItemsAfter(of(Items.ENDER_EYE), BEJEWELED_PEARL)
				.addItemsAfter(of(Items.TRIAL_KEY), TRIAL_TOKEN)
				.addItemsAfter(of(Items.OMINOUS_TRIAL_KEY), OMINOUS_TRIAL_TOKEN)
				.addItemsAfter(of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), TRIM_MODIFIER_SMITHING_TEMPLATE)
				.addItemsBefore(of(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE), CORE_ARMOR_TRIM_SMITHING_TEMPLATE, FORGER_ARMOR_TRIM_SMITHING_TEMPLATE, IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE, PLATE_ARMOR_TRIM_SMITHING_TEMPLATE, RIM_ARMOR_TRIM_SMITHING_TEMPLATE)
				.addItemsAfter(of(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE), EXILE_ARMOR_TRIM_SMITHING_TEMPLATE)
				.addItemsAlphabetically(stack -> stack.is(ItemTags.DECORATED_POT_SHERDS), "pottery_sherd|_", BOOM_POTTERY_SHERD, CAST_POTTERY_SHERD, RIDE_POTTERY_SHERD, STALKER_POTTERY_SHERD)
				.addItems(EXPOSED_COPPER_INGOT, WEATHERED_COPPER_INGOT, OXIDIZED_COPPER_INGOT, WAXED_COPPER_INGOT, WAXED_EXPOSED_COPPER_INGOT, WAXED_WEATHERED_COPPER_INGOT, WAXED_OXIDIZED_COPPER_INGOT)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsAfter(of(Items.STONE_HOE), COPPER_SHOVEL, COPPER_PICKAXE, COPPER_AXE, COPPER_HOE)
				.addItemsAfter(of(Items.GOLDEN_HOE), SILVER_SHOVEL, SILVER_PICKAXE, SILVER_AXE, SILVER_HOE)
				.addItemsAfter(of(Items.NETHERITE_HOE), NECROMIUM_SHOVEL, NECROMIUM_PICKAXE, NECROMIUM_AXE, NECROMIUM_HOE)
				.addItemsBefore(of(Items.CLOCK), BAROMETER, TUNING_FORK)
				.addItemsAfter(of(Items.SPYGLASS), MONOCLE, UNICORN_HORN, DEPTH_GAUGE)
				.addItemsBefore(of(Items.TROPICAL_FISH_BUCKET), CAVEFISH_BUCKET)
				.addItemsAfter(of(Items.TNT_MINECART), TMT_MINECART)
				.editor(event -> CCBlocks.remove(event, Items.BUNDLE))
				.addItemsBefore(of(Items.FISHING_ROD), GOLDEN_BUCKET, GOLDEN_WATER_BUCKET, GOLDEN_LAVA_BUCKET, GOLDEN_POWDER_SNOW_BUCKET, GOLDEN_MILK_BUCKET, () -> Items.BUNDLE)
				.addItemsBefore(of(Items.FISHING_ROD), PACKING_CONTAINER)
				.addItemsAfter(of(Items.ENDER_EYE), BEJEWELED_PEARL)
				.addItemsBefore(of(Items.MUSIC_DISC_PIGSTEP), MUSIC_DISC_ANALOGUE, MUSIC_DISC_EPILOGUE)
				.addItemsBefore(of(Items.BAMBOO_RAFT), AZALEA_BOAT.getFirst(), AZALEA_BOAT.getSecond())
				.addItemsBefore(modLoaded(Items.BAMBOO_RAFT, "boatload"), AZALEA_FURNACE_BOAT, LARGE_AZALEA_BOAT)
				.editor(event -> event.getParameters().holders().lookup(Registries.INSTRUMENT).ifPresent(registry -> {
					generateInstrumentTypes(event, registry, COPPER_HORN.get(), CCInstrumentTags.HARMONY_COPPER_HORNS, CCInstrumentTags.MELODY_COPPER_HORNS, CCInstrumentTags.BASS_COPPER_HORNS);
				}))
				.addItemsBefore(of(Items.MUSIC_DISC_13), BONE_FLUTE)
				.tab(COMBAT)
				.addItemsAfter(of(Items.STONE_SWORD), COPPER_SWORD)
				.addItemsAfter(of(Items.STONE_AXE), COPPER_AXE)
				.addItemsAfter(of(Items.LEATHER_BOOTS), COPPER_HELMET, COPPER_CHESTPLATE, COPPER_LEGGINGS, COPPER_BOOTS)
				.addItemsAfter(of(Items.LEATHER_HORSE_ARMOR), COPPER_HORSE_ARMOR)
				.addItemsAfter(of(Items.GOLDEN_SWORD), SILVER_SWORD)
				.addItemsAfter(of(Items.GOLDEN_AXE), SILVER_AXE)
				.addItemsAfter(of(Items.GOLDEN_BOOTS), SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS)
				.addItemsAfter(of(Items.GOLDEN_HORSE_ARMOR), SILVER_HORSE_ARMOR)
				.addItemsAfter(of(Items.NETHERITE_SWORD), NECROMIUM_SWORD)
				.addItemsAfter(of(Items.NETHERITE_AXE), NECROMIUM_AXE)
				.addItemsAfter(of(Items.NETHERITE_BOOTS),
						NECROMIUM_HELMET, NECROMIUM_CHESTPLATE, NECROMIUM_LEGGINGS, NECROMIUM_BOOTS,
						SANGUINE_HELMET, SANGUINE_CHESTPLATE, SANGUINE_LEGGINGS, SANGUINE_BOOTS)
				.addItemsBefore(of(Items.TURTLE_HELMET), COWL, TOOLBELT)
				.addItemsAfter(of(Items.SHIELD), AEGIS)
				.addItemsAfter(of(Items.DIAMOND_HORSE_ARMOR), NETHERITE_HORSE_ARMOR, NECROMIUM_HORSE_ARMOR)
				.addItemsBefore(of(Items.SNOWBALL), KUNAI)
				.addItemsAfter(of(Items.SPECTRAL_ARROW), BLUNT_ARROW, RICOCHET_ARROW, LARGE_ARROW)
				.editor(event -> event.getParameters().holders().lookup(Registries.POTION).ifPresent(registry -> {
					generatePotionEffectTypes(event, of(Items.TIPPED_ARROW), registry, TETHER_POTION.get());
					generatePotionEffectTypes(event, of(Items.TIPPED_ARROW), registry, IMPACT_POTION.get());
					generatePotionEffectTypes(event, of(Items.TIPPED_ARROW), registry, TRAIL_POTION.get());
				}))
				.tab(REDSTONE_BLOCKS)
				.addItemsAfter(of(Items.TNT_MINECART), TMT_MINECART)
				.tab(SPAWN_EGGS)
				.addSpawnEggsAlphabetically(DEEPER_SPAWN_EGG, EVENDEEPER_SPAWN_EGG, PEEPER_SPAWN_EGG, MIME_SPAWN_EGG, GLARE_SPAWN_EGG, COPPER_GOLEM_SPAWN_EGG, RAT_SPAWN_EGG, CAVEFISH_SPAWN_EGG, GRAZER_SPAWN_EGG, SADDLED_GRAZER_SPAWN_EGG);
	}

	public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
		return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
	}

	private static void generatePotionEffectTypes(BuildCreativeModeTabContentsEvent event, Predicate<ItemStack> predicate, HolderLookup<Potion> potion, Item potionItem) {
		TabVisibility visibility = TabVisibility.PARENT_AND_SEARCH_TABS;
		List<ItemStack> items = potion.listElements().map((p_269986_) -> PotionContents.createItemStack(potionItem, p_269986_)).toList();

		ObjectSortedSet<ItemStack> entries = event.getParentEntries();
		for (ItemStack entry : entries) {
			if (predicate.test(entry)) {
				for (ItemStack itemValue : items) {
					event.accept(itemValue, visibility);
				}
				return;
			}
		}
	}

	private static void generateInstrumentTypes(BuildCreativeModeTabContentsEvent event, HolderLookup<Instrument> lookup, Item item, TagKey<Instrument> harmonyTag, TagKey<Instrument> melodyTag, TagKey<Instrument> bassTag) {
		TabVisibility visibility = TabVisibility.PARENT_AND_SEARCH_TABS;

		Optional<Named<Instrument>> harmonyOptional = lookup.get(harmonyTag);
		Optional<Named<Instrument>> melodyOptional = lookup.get(melodyTag);
		Optional<Named<Instrument>> bassOptional = lookup.get(bassTag);

		ObjectSortedSet<ItemStack> entries = event.getParentEntries();
		if (harmonyOptional.isPresent() && melodyOptional.isPresent() && bassOptional.isPresent()) {
			for (ItemStack entry : entries) {
				if (of(Items.GOAT_HORN).test(entry) && lookup.get(harmonyTag).isPresent()) {
					for (int i = 0; i < lookup.get(harmonyTag).get().size(); i++) {
						ItemStack horn = CopperHornItem.create(item, harmonyOptional.get().get(i), melodyOptional.get().get(i), bassOptional.get().get(i));
						event.insertBefore(new ItemStack(Items.MUSIC_DISC_13), horn, visibility);
					}
					return;
				}
			}
		}
	}

	public static class CCFoods {
		public static final FoodProperties BEJEWELED_APPLE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
		public static final FoodProperties CAVIAR = new FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build();
		public static final FoodProperties CAVEFISH = new FoodProperties.Builder().nutrition(3).saturationModifier(0.1F).effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 300), 0.4F).build();
	}
}
