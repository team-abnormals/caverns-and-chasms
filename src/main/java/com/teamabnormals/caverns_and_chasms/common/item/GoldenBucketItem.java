package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class GoldenBucketItem extends BucketItem {
	public static final Map<GoldenBucketPredicate, Supplier<Item>> GOLDEN_BUCKET_BEHAVIORS = Util.make(Maps.newHashMap(), map -> {
		map.put(state -> state.getFluidState().is(Fluids.WATER), CCItems.GOLDEN_WATER_BUCKET);
		map.put(state -> state.getFluidState().is(Fluids.LAVA), CCItems.GOLDEN_LAVA_BUCKET);
		map.put(state -> state.is(Blocks.POWDER_SNOW), CCItems.GOLDEN_POWDER_SNOW_BUCKET);
		map.put(state -> ForgeMod.MILK.isPresent() && state.getFluidState().is(ForgeMod.MILK.get()), CCItems.GOLDEN_MILK_BUCKET);
	});

	public static final String NBT_TAG = "FluidLevel";

	public GoldenBucketItem(Supplier<? extends Fluid> supplier, Item.Properties builder) {
		super(supplier, builder);
	}

	@Override
	public ItemStack getDefaultInstance() {
		return resetFluidLevel(new ItemStack(this));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		resetFluidLevel(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		boolean empty = isEmpty(stack);
		BlockHitResult fillResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

		BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
		if (empty || (canBeFilled(stack) && !player.isCrouching() && level.getBlockState(fillResult.getBlockPos()).getFluidState().is(this.getFluid()))) {
			result = fillResult;
		}

		InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, result);
		if (ret != null) return ret;
		if (result.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(stack);
		} else if (result.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		} else {
			BlockPos pos = result.getBlockPos();
			Direction dir = result.getDirection();
			BlockPos offsetPos = pos.relative(dir);
			if (level.mayInteract(player, pos) && player.mayUseItemAt(offsetPos, dir, stack)) {
				BlockState state = level.getBlockState(pos);
				if (empty || (canBeFilled(stack) && state.getFluidState().is(this.getFluid()))) {
					if (state.getBlock() instanceof BucketPickup pickup) {
						ItemStack pickupStack = pickup.pickupBlock(level, pos, state);
						if (!pickupStack.isEmpty()) {
							ItemStack returnStack = getFilledBucket(state);
							if (!returnStack.isEmpty()) {
								if (!empty) {
									setFluidLevel(returnStack, getFluidLevel(stack) + 1);
								}
								returnStack = createFilledResult(stack, player, returnStack);

								if (!returnStack.isEmpty()) {
									player.awardStat(Stats.ITEM_USED.get(this));
									pickup.getPickupSound(state).ifPresent((sound) -> player.playSound(sound, 1.0F, 1.0F));
									level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
									if (!level.isClientSide()) {
										CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, returnStack);
									}

									return InteractionResultHolder.sidedSuccess(returnStack, level.isClientSide());
								}
							}
						}
					}

					return InteractionResultHolder.fail(stack);
				} else {
					BlockPos newPos = canBlockContainFluid(level, pos, state) ? pos : offsetPos;
					if (this.emptyContents(player, level, newPos, result, stack)) {
						this.checkExtraContent(player, level, stack, newPos);
						if (player instanceof ServerPlayer serverPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, newPos, stack);
						}

						player.awardStat(Stats.ITEM_USED.get(this));
						return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
					} else {
						return InteractionResultHolder.fail(stack);
					}
				}
			} else {
				return InteractionResultHolder.fail(stack);
			}
		}
	}

	public static ItemStack createFilledResult(ItemStack stack, Player player, ItemStack newStack) {
		boolean isCreative = player.getAbilities().instabuild;
		if (isCreative) {
			if (!player.getInventory().hasAnyOf(Set.of(newStack.getItem()))) {
				player.getInventory().add(newStack);
			}

			return increaseFluidLevel(stack);
		} else {
			return ItemUtils.createFilledResult(stack, player, newStack);
		}
	}

	public static boolean isEmpty(ItemStack stack) {
		return stack.is(getEmptyBucket().getItem());
	}

	public static ItemStack getEmptySuccessItem(ItemStack stack, @Nullable Player player) {
		if (player == null || !player.getAbilities().instabuild) {
			return decreaseFluidLevel(stack);
		}
		return stack;
	}

	public static ItemStack getFilledBucket(BlockState state) {
		for (GoldenBucketPredicate pair : GOLDEN_BUCKET_BEHAVIORS.keySet()) {
			if (pair.test(state)) {
				return resetFluidLevel(new ItemStack(GOLDEN_BUCKET_BEHAVIORS.get(pair).get()));

			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return decreaseFluidLevel(stack.copy());
	}

	@Override
	public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
		return stack.getItem() == CCItems.GOLDEN_LAVA_BUCKET.get() ? 20000 : super.getBurnTime(stack, recipeType);
	}

	public static ItemStack getEmptyBucket() {
		return resetFluidLevel(new ItemStack(CCItems.GOLDEN_BUCKET.get()));
	}

	public static int getFluidLevel(ItemStack stack) {
		return stack.getOrCreateTag().getInt(NBT_TAG);
	}

	public static ItemStack resetFluidLevel(ItemStack stack) {
		stack.getOrCreateTag().putInt(NBT_TAG, 0);
		return stack;
	}

	public static ItemStack setFluidLevel(ItemStack stack, int level) {
		if (level <= 2) {
			stack.getOrCreateTag().putInt(NBT_TAG, level);
		}
		return stack;
	}

	public static ItemStack decreaseFluidLevel(ItemStack stack) {
		int level = getFluidLevel(stack);
		if (stack != null && level > 0) {
			return setFluidLevel(stack, level - 1);
		} else {
			return getEmptyBucket();
		}
	}

	public static ItemStack increaseFluidLevel(ItemStack stack) {
		if (!isEmpty(stack) && canBeFilled(stack)) {
			return setFluidLevel(stack, getFluidLevel(stack) + 1);
		}
		return stack;
	}

	public static boolean canBeFilled(ItemStack stack) {
		return getFluidLevel(stack) < 2;
	}

	public interface GoldenBucketPredicate {
		boolean test(BlockState state);
	}
}