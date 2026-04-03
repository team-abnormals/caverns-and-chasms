package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteraction.InteractionMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Predicate;

public class CCCauldronInteractions {
	public static final ResourceLocation MILK = ResourceLocation.fromNamespaceAndPath("neapolitan", "milk");
	public static final ResourceLocation MILK_CAULDRON = ResourceLocation.fromNamespaceAndPath("neapolitan", "milk_cauldron");

	public static final CauldronInteraction FILL_WATER = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
	public static final CauldronInteraction FILL_LAVA = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.LAVA_CAULDRON.defaultBlockState(), blockState -> true, SoundEvents.BUCKET_EMPTY_LAVA, SoundEvents.BUCKET_FILL_LAVA);
	public static final CauldronInteraction FILL_POWDER_SNOW = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, SoundEvents.BUCKET_FILL_POWDER_SNOW);
	public static final CauldronInteraction FILL_MILK = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, BuiltInRegistries.BLOCK.get(MILK_CAULDRON).defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);

	public static void registerCauldronInteractions() {
		CauldronInteraction.WATER.map().put(Items.BUNDLE, CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(CCItems.UNICORN_HORN.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(CCItems.PACKING_CONTAINER.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(CCItems.AEGIS.get(), CauldronInteraction.DYED_ITEM);

		addFillBucketInteractions();
		DataUtil.addDefaultCauldronInteraction(CCItems.GOLDEN_LAVA_BUCKET.get(), FILL_LAVA);
		DataUtil.addDefaultCauldronInteraction(CCItems.GOLDEN_WATER_BUCKET.get(), FILL_WATER);
		DataUtil.addDefaultCauldronInteraction(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW);
		if (BuiltInRegistries.BLOCK.containsKey(MILK_CAULDRON) && CauldronInteraction.INTERACTIONS.containsKey(MILK.toString())) {
			DataUtil.addDefaultCauldronInteraction(CCItems.GOLDEN_MILK_BUCKET.get(), FILL_MILK);
		}
	}

	private static void addFillBucketInteractions() {
		CauldronInteraction.WATER.map().put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.map().put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get()), blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.map().put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));

		CauldronInteraction.WATER.map().put(CCItems.GOLDEN_WATER_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.map().put(CCItems.GOLDEN_LAVA_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.map().put(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));

		InteractionMap milk = CauldronInteraction.INTERACTIONS.get(MILK.toString());
		if (milk != null) {
			milk.map().put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
			milk.map().put(CCItems.GOLDEN_MILK_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		}
	}

	public static ItemInteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (!statePredicate.test(state)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, GoldenBucketItem.createFilledResult(stack, player, output));
				player.awardStat(Stats.USE_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static ItemInteractionResult fillFilledBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (!GoldenBucketItem.canBeFilled(stack) || !statePredicate.test(state)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static ItemInteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, Predicate<BlockState> statePredicate, SoundEvent soundEvent, SoundEvent fillSoundEvent) {
		if (state.equals(level.getBlockState(pos)) && GoldenBucketItem.canBeFilled(stack) && statePredicate.test(state)) {
			return fillFilledBucket(state, level, pos, player, hand, stack, statePredicate, fillSoundEvent);
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

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}
}
