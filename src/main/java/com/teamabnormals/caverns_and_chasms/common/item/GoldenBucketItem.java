package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GoldenBucketItem extends Item implements DispensibleContainerItem {
	private static final String NBT_TAG = "FluidLevel";
	private final Supplier<? extends Fluid> fluidSupplier;

	public GoldenBucketItem(Supplier<? extends Fluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	public static ItemStack resetFluidLevel(ItemStack stack) {
		stack.getOrCreateTag().putInt(NBT_TAG, 0);
		return stack;
	}

	public static void setFluidLevel(ItemStack stack, int level) {
		stack.getOrCreateTag().putInt(NBT_TAG, level);
	}

	public static ItemStack decreaseFluidLevel(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		if (level > 0) stack.getOrCreateTag().putInt(NBT_TAG, level - 1);
		return stack;
	}

	public static ItemStack increaseFluidLevel(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		if (level < 3) stack.getOrCreateTag().putInt(NBT_TAG, level + 1);
		return stack;
	}

	public static boolean canBeFilled(ItemStack stack) {
		return stack.getOrCreateTag().getInt(NBT_TAG) < 2;
	}

	@Override
	public ItemStack getDefaultInstance() {
		return resetFluidLevel(new ItemStack(this));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
		resetFluidLevel(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getOrCreateTag();
		int bucketLevel = tag.getInt(NBT_TAG);

		BlockHitResult result = getPlayerPOVHitResult(level, player, (this.getFluid() == Fluids.EMPTY || bucketLevel < 2) && !(player.isCrouching() && this.getFluid() != Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
		InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, result);
		if (ret != null) return ret;
		if (result.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(stack);
		} else if (result.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		} else {
			BlockPos pos = result.getBlockPos();
			Direction direction = result.getDirection();
			BlockPos sourcePos = pos.relative(direction);
			if (level.mayInteract(player, pos) && player.mayUseItemAt(sourcePos, direction, stack)) {
				BlockState sourceState = level.getBlockState(pos);
				if (this.getFluid() == Fluids.EMPTY || (bucketLevel < 2 && sourceState.getFluidState().getType() == this.getFluid())) {
					// Picking up raw fluids & powder snow
					if (sourceState.getBlock() instanceof BucketPickup bucketPickup) {
						bucketPickup.pickupBlock(level, pos, sourceState);
						Fluid fluid = sourceState.getFluidState().is(Fluids.WATER) ? Fluids.WATER : sourceState.getFluidState().is(Fluids.LAVA) ? Fluids.LAVA : Fluids.EMPTY;
						ItemStack newBucket = ItemStack.EMPTY;

						if (fluid != Fluids.EMPTY && getFilledBucket(sourceState) != null) {
							newBucket = ItemUtils.createFilledResult(stack, player, getFilledBucket(sourceState));
							if (this.getFluid() != Fluids.EMPTY)
								setFluidLevel(newBucket, bucketLevel + 1);
						}

						if (sourceState.is(Blocks.POWDER_SNOW)) {
							newBucket = ItemUtils.createFilledResult(stack, player, new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get()));
						}

						if (!newBucket.isEmpty()) {
							player.awardStat(Stats.ITEM_USED.get(this));
							bucketPickup.getPickupSound(sourceState).ifPresent((soundEvent) -> player.playSound(soundEvent, 1.0F, 1.0F));
							level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
							if (!level.isClientSide) {
								CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, newBucket);
							}

							return InteractionResultHolder.sidedSuccess(newBucket, level.isClientSide());
						}
					}

					return InteractionResultHolder.fail(stack);
				} else {
					result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
					if (result.getType() == HitResult.Type.MISS) {
						return InteractionResultHolder.pass(stack);
					} else if (result.getType() != HitResult.Type.BLOCK) {
						return InteractionResultHolder.pass(stack);
					}

					// Placing contents
					pos = result.getBlockPos();
					direction = result.getDirection();
					sourcePos = pos.relative(direction);
					BlockState state = level.getBlockState(pos);
					BlockPos newPos = canBlockContainFluid(level, pos, state) || (this.getFluid() != Fluids.EMPTY && bucketLevel < 2) ? pos : sourcePos;
					if (this.emptyContents(player, level, newPos, result)) {
						this.checkExtraContent(level, stack, newPos);
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, newPos, stack);
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

	public static ItemStack getEmptySuccessItem(ItemStack stack, @Nullable Player player) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		ItemStack returnStack = level > 0 ? stack : getEmptyBucket();
		if (player == null || !player.getAbilities().instabuild)
			decreaseFluidLevel(returnStack);
		return player == null || !player.getAbilities().instabuild ? returnStack : stack;
	}

	public void checkExtraContent(Level worldIn, ItemStack p_203792_2_, BlockPos pos) {
	}

	public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
		if (!(this.getFluid() instanceof FlowingFluid)) {
			return false;
		} else {
			BlockState state = level.getBlockState(pos);
			Block block = state.getBlock();
			boolean replaceable = state.canBeReplaced(this.getFluid());
			if (!(state.isAir() || replaceable || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, state, this.getFluid()))) {
				return result != null && this.emptyContents(player, level, result.getBlockPos().relative(result.getDirection()), null);
			} else if (level.dimensionType().ultraWarm() && this.getFluid().is(FluidTags.WATER)) {
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
				for (int l = 0; l < 8; ++l) {
					level.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
				}

				return true;
			} else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, state, getFluid())) {
				((LiquidBlockContainer) block).placeLiquid(level, pos, state, ((FlowingFluid) this.getFluid()).getSource(false));
				this.playEmptySound(player, level, pos);
				return true;
			} else {
				if (!level.isClientSide && replaceable && !state.liquid()) {
					level.destroyBlock(pos, true);
				}

				if (!level.setBlock(pos, this.getFluid().defaultFluidState().createLegacyBlock(), 11) && !state.getFluidState().isSource()) {
					return false;
				} else {
					this.playEmptySound(player, level, pos);
					return true;
				}
			}
		}
	}

	protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
		SoundEvent soundevent = this.getFluid().getFluidType().getSound(player, level, pos, SoundActions.BUCKET_EMPTY);
		if (soundevent == null)
			soundevent = this.getFluid().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
		level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
		if (this.getClass() == GoldenBucketItem.class) return new FluidBucketWrapper(stack);
		else return super.initCapabilities(stack, nbt);
	}

	public Fluid getFluid() {
		return fluidSupplier.get();
	}

	private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
		return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid());
	}

	public static ItemStack getFilledBucket(Fluid fluid) {
		if (fluid == Fluids.WATER) return new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get());
		if (fluid == Fluids.LAVA) return new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get());
		if (ForgeMod.MILK.isPresent() && fluid == ForgeMod.MILK.get()) return new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get());
		return null;
	}

	public static ItemStack getFilledBucket(BlockState state) {
		if (state.getFluidState().is(Fluids.WATER)) return new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get());
		if (state.getFluidState().is(Fluids.LAVA)) return new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get());
		if (state.is(Blocks.POWDER_SNOW)) return new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
		if (ForgeMod.MILK.isPresent() && state.getFluidState().is(ForgeMod.MILK.get())) return new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get());
		return null;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		ItemStack newStack = getFilledBucket(this.getFluid());
		if (level > 0 && newStack != null) {
			setFluidLevel(newStack, level - 1);
			return newStack;
		}
		return getEmptyBucket();
	}

	@Override
	public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
		return stack.getItem() == CCItems.GOLDEN_LAVA_BUCKET.get() ? 20000 : super.getBurnTime(stack, recipeType);
	}

	public static ItemStack getEmptyBucket() {
		return resetFluidLevel(new ItemStack(CCItems.GOLDEN_BUCKET.get()));
	}
}