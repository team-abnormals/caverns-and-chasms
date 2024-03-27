package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.dispenser.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SculkSensorBlock;

import java.util.List;

public class CCCompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		registerWaxables();
		registerFireworkIngredients();
		registerParrotImitations();
		registerVibrationFrequencies();
		changeLocalization();
		CCCauldronInteractions.registerCauldronInteractions();
	}

	private static void registerFlammables() {
		DataUtil.registerFlammable(CCBlocks.AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOARDS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BEEHIVE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_HEDGE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.FLOWERING_AZALEA_HEDGE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.VERTICAL_AZALEA_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.TMT.get(), 15, 100);
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(CCItems.KUNAI.get(), new KunaiDispenseBehavior());
		DispenserBlock.registerBehavior(CCItems.BLUNT_ARROW.get(), new BluntArrowDispenseBehavior());
		DispenserBlock.registerBehavior(CCItems.LARGE_ARROW.get(), new LargeArrowDispenserBehavior());
		DispenserBlock.registerBehavior(CCBlocks.TMT.get(), new TMTDispenseBehavior());
		DispenserBlock.registerBehavior(CCItems.GOLDEN_BUCKET.get(), new GoldenBucketDispenseBehavior());

		DispenseItemBehavior goldenBucketDispenseBehavior = new FilledGoldenBucketDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.GOLDEN_LAVA_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_WATER_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), goldenBucketDispenseBehavior);

		DefaultDispenseItemBehavior horseArmorDispenseBehavior = new HorseArmorDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.SILVER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NETHERITE_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NECROMIUM_HORSE_ARMOR.get(), horseArmorDispenseBehavior);

		DispenseItemBehavior armorDispenseBehavior = new ArmorDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.DEEPER_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.PEEPER_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.MIME_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.TETHER_POTION.get(), armorDispenseBehavior);
	}

	private static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
		DataUtil.changeBlockLocalization(Blocks.AMETHYST_BLOCK, CavernsAndChasms.MOD_ID, "amethyst");
		DataUtil.changeBlockLocalization(CCBlocks.AMETHYST_BLOCK.get(), "minecraft", "amethyst_block");
	}

	private static void registerWaxables() {
		ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
		HoneycombItem.WAXABLES.get().forEach(builder::put);
		builder.put(CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
		builder.put(CCBlocks.COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
		builder.put(CCBlocks.TOOLBOX.get(), CCBlocks.WAXED_TOOLBOX.get());
		builder.put(CCBlocks.EXPOSED_TOOLBOX.get(), CCBlocks.WAXED_EXPOSED_TOOLBOX.get());
		builder.put(CCBlocks.WEATHERED_TOOLBOX.get(), CCBlocks.WAXED_WEATHERED_TOOLBOX.get());
		builder.put(CCBlocks.OXIDIZED_TOOLBOX.get(), CCBlocks.WAXED_OXIDIZED_TOOLBOX.get());
		builder.put(Blocks.LIGHTNING_ROD, CCBlocks.WAXED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.EXPOSED_LIGHTNING_ROD.get(), CCBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.WEATHERED_LIGHTNING_ROD.get(), CCBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.OXIDIZED_LIGHTNING_ROD.get(), CCBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.FLOODLIGHT.get(), CCBlocks.WAXED_FLOODLIGHT.get());
		builder.put(CCBlocks.EXPOSED_FLOODLIGHT.get(), CCBlocks.WAXED_EXPOSED_FLOODLIGHT.get());
		builder.put(CCBlocks.WEATHERED_FLOODLIGHT.get(), CCBlocks.WAXED_WEATHERED_FLOODLIGHT.get());
		builder.put(CCBlocks.OXIDIZED_FLOODLIGHT.get(), CCBlocks.WAXED_OXIDIZED_FLOODLIGHT.get());
		HoneycombItem.WAXABLES = Suppliers.memoize(builder::build);
	}

	private static void registerFireworkIngredients() {
		FireworkStarRecipe.SHAPE_INGREDIENT = Ingredient.merge(List.of(FireworkStarRecipe.SHAPE_INGREDIENT, Ingredient.of(CCItems.DEEPER_HEAD.get(), CCItems.PEEPER_HEAD.get(), CCItems.MIME_HEAD.get())));
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.DEEPER_HEAD.get(), FireworkRocketItem.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.PEEPER_HEAD.get(), FireworkRocketItem.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.MIME_HEAD.get(), FireworkRocketItem.Shape.CREEPER);
	}

	private static void registerParrotImitations() {
		DataUtil.registerParrotImitation(CCEntityTypes.DEEPER.get(), CCSoundEvents.PARROT_IMITATE_DEEPER.get());
		DataUtil.registerParrotImitation(CCEntityTypes.PEEPER.get(), CCSoundEvents.PARROT_IMITATE_PEEPER.get());
		DataUtil.registerParrotImitation(CCEntityTypes.MIME.get(), CCSoundEvents.PARROT_IMITATE_MIME.get());
	}

	private static void registerVibrationFrequencies() {
		SculkSensorBlock.VIBRATION_FREQUENCY_FOR_EVENT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(SculkSensorBlock.VIBRATION_FREQUENCY_FOR_EVENT), (map) -> {
			map.put(CCGameEvents.TUNING_FORK_VIBRATE.get(), 10);
		}));
	}
}
