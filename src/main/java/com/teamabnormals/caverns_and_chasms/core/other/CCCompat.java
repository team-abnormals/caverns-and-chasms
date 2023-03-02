package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.dispenser.*;
import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CCCompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		registerWaxables();
		registerCauldronInteractions();
		registerFireworkIngredients();
		changeLocalization();
		setFireproof();
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
		DispenserBlock.registerBehavior(CCItems.MIME_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.TETHER_POTION.get(), armorDispenseBehavior);
	}

	private static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
		DataUtil.changeBlockLocalization(Blocks.AMETHYST_BLOCK, CavernsAndChasms.MOD_ID, "amethyst");
		DataUtil.changeBlockLocalization(CCBlocks.AMETHYST_BLOCK.get(), "minecraft", "amethyst_block");
	}

	private static void setFireproof() {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, CCBlocks.NECROMIUM_BLOCK.get().asItem(), true, "f_41372_");
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
		HoneycombItem.WAXABLES = Suppliers.memoize(builder::build);
	}

	private static void registerFireworkIngredients() {
		FireworkStarRecipe.SHAPE_INGREDIENT = Ingredient.merge(List.of(FireworkStarRecipe.SHAPE_INGREDIENT, Ingredient.of(CCItems.DEEPER_HEAD.get(), CCItems.MIME_HEAD.get())));
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.DEEPER_HEAD.get(), FireworkRocketItem.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.MIME_HEAD.get(), FireworkRocketItem.Shape.CREEPER);
	}

	private static void registerCauldronInteractions() {
		addFillBucketInteractions();
		addDefaultInteractions(CauldronInteraction.EMPTY);
		addDefaultInteractions(CauldronInteraction.WATER);
		addDefaultInteractions(CauldronInteraction.LAVA);
		addDefaultInteractions(CauldronInteraction.POWDER_SNOW);
//		addDefaultInteractions(CauldronInteraction.MILK);
	}

	private static void addDefaultInteractions(Map<Item, CauldronInteraction> map) {
		map.put(CCItems.GOLDEN_LAVA_BUCKET.get(), FILL_LAVA);
		map.put(CCItems.GOLDEN_WATER_BUCKET.get(), FILL_WATER);
		map.put(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW);
//		map.put(CCItems.GOLDEN_MILK_BUCKET.get(), FILL_MILK);
	}

	private static void addFillBucketInteractions() {
		CauldronInteraction.WATER.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get()), blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
//		CauldronInteraction.MILK.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_MILK));

		CauldronInteraction.WATER.put(CCItems.GOLDEN_MILK_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.put(CCItems.GOLDEN_LAVA_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.put(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
//		CauldronInteraction.MILK.put(CCItems.GOLDEN_MILK_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_MILK));
	}

	public static CauldronInteraction FILL_WATER = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY);
	public static CauldronInteraction FILL_LAVA = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.LAVA_CAULDRON.defaultBlockState(), blockState -> true, SoundEvents.BUCKET_EMPTY_LAVA);
	public static CauldronInteraction FILL_POWDER_SNOW = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
//	public static CauldronInteraction FILL_MILK = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.MILK_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY_MILK);

	public static InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (!statePredicate.test(state)) {
			return InteractionResult.PASS;
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, output));
				player.awardStat(Stats.USE_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static InteractionResult fillFilledBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (stack.getOrCreateTag().getInt("FluidLevel") == 3 || !statePredicate.test(state)) {
			return InteractionResult.PASS;
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, GoldenBucketItem.increaseFluidLevel(stack));
				player.awardStat(Stats.USE_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static InteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (state.equals(level.getBlockState(pos)) && stack.getOrCreateTag().getInt("FluidLevel") != 3 && statePredicate.test(state)) {
			return fillFilledBucket(state, level, pos, player, hand, stack, statePredicate, soundEvent);
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, GoldenBucketItem.getEmptySuccessItem(stack, player));
				player.awardStat(Stats.FILL_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, state);
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}
}
