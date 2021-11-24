package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class GoldenSolidBucketItem extends BlockItem implements DispensibleContainerItem {
	private final SoundEvent placeSound;

	public GoldenSolidBucketItem(Block block, SoundEvent placeSound, Item.Properties properties) {
		super(block, properties);
		this.placeSound = placeSound;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		int fluidLevel = stack.getOrCreateTag().getInt("FluidLevel");

		if (state.getBlock() instanceof AbstractCauldronBlock cauldronBlock && player != null) {
			if (player.isCrouching() || fluidLevel == 2 || !cauldronBlock.isFull(state) || cauldronBlock != Blocks.POWDER_SNOW_CAULDRON) {
				level.setBlockAndUpdate(pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
				level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.awardStat(Stats.FILL_CAULDRON);
				if (!player.isCreative()) {
					if (fluidLevel > 0) {
						stack.getOrCreateTag().putInt("FluidLevel", fluidLevel - 1);
					} else {
						stack = GoldenBucketItem.getEmptyBucket();
					}
					player.setItemInHand(hand, stack);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			} else if (cauldronBlock.isFull(state)) {
				level.playSound(null, pos, SoundEvents.BUCKET_FILL_POWDER_SNOW, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.awardStat(Stats.USE_CAULDRON);
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				if (!player.isCreative()) {
					stack.getOrCreateTag().putInt("FluidLevel", fluidLevel + 1);
					player.setItemInHand(hand, stack);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		} else if (state.getBlock() instanceof PowderSnowBlock powderSnowBlock && player != null && !player.isCrouching() && fluidLevel < 2) {
			powderSnowBlock.pickupBlock(level, pos, state);
			player.playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW, 1.0F, 1.0F);
			stack.getOrCreateTag().putInt("FluidLevel", fluidLevel + 1);
			if (!level.isClientSide) {
				CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}

		InteractionResult result = super.useOn(new UseOnContext(level, player, hand, stack.copy(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside())));

		if (result.consumesAction() && player != null && !player.isCreative()) {
			if (fluidLevel > 0) {
				stack.getOrCreateTag().putInt("FluidLevel", fluidLevel - 1);
			} else {
				stack = GoldenBucketItem.getEmptyBucket();
			}
			player.setItemInHand(hand, stack);
		}

		return result;
	}

	@Override
	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}

	@Override
	protected SoundEvent getPlaceSound(BlockState state) {
		return this.placeSound;
	}

	@Override
	public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
		if (level.isInWorldBounds(pos) && level.isEmptyBlock(pos)) {
			if (!level.isClientSide) {
				level.setBlock(pos, this.getBlock().defaultBlockState(), 3);
			}

			level.playSound(player, pos, this.placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt("FluidLevel");
		if (level > 0) {
			ItemStack newStack = new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
			newStack.getOrCreateTag().putInt("FluidLevel", level - 1);
			return newStack;
		}
		return GoldenBucketItem.getEmptyBucket();
	}
}