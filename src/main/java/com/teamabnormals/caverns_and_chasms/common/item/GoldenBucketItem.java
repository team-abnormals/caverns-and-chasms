package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GoldenBucketItem extends Item {
	private static final String NBT_TAG = "FluidLevel";
	private final Supplier<? extends Fluid> fluidSupplier;

	public GoldenBucketItem(java.util.function.Supplier<? extends Fluid> supplier, Item.Properties builder) {
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

	public static void decreaseFluidLevel(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		if (level > 0) stack.getOrCreateTag().putInt(NBT_TAG, level - 1);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			items.add(resetFluidLevel(new ItemStack(this)));
		}
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

				boolean empty = this.getFluid() == Fluids.EMPTY;
				boolean canFitMoreFluid = bucketLevel < 2 && sourceState.getFluidState().getType() == this.getFluid();
				boolean canFitFromCauldron = !empty && bucketLevel < 2 && !player.isCrouching() && sourceState.getBlock() instanceof AbstractCauldronBlock cauldronBlock && cauldronBlock.isFull(sourceState) && !(this.getFluid() != Fluids.EMPTY && cauldronBlock != Blocks.CAULDRON && cauldronBlock != getCauldron());

				if (empty || canFitMoreFluid || canFitFromCauldron) {
					// Picking up from Cauldron
					if (sourceState.getBlock() instanceof AbstractCauldronBlock cauldronBlock && cauldronBlock.isFull(sourceState) && getBucketForCauldron(cauldronBlock) != null) {
						ItemStack newBucket = ItemUtils.createFilledResult(stack, player, getBucketForCauldron(cauldronBlock));
						SoundEvent fillSound = newBucket.getItem() == CCItems.GOLDEN_LAVA_BUCKET.get() ? SoundEvents.BUCKET_FILL_LAVA : newBucket.getItem() == CCItems.GOLDEN_POWDER_SNOW_BUCKET.get() ? SoundEvents.BUCKET_FILL_POWDER_SNOW : SoundEvents.BUCKET_FILL;
						if (this.getFluid() != Fluids.EMPTY) {
							setFluidLevel(newBucket, bucketLevel + 1);
						}

						player.awardStat(Stats.USE_CAULDRON);
						level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
						level.playSound(null, pos, fillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
						return InteractionResultHolder.sidedSuccess(newBucket, level.isClientSide());
					}

					// Picking up raw fluids & powder snow
					if (sourceState.getBlock() instanceof BucketPickup bucketPickup) {
						bucketPickup.pickupBlock(level, pos, sourceState);
						Fluid fluid = sourceState.is(Blocks.WATER) ? Fluids.WATER : sourceState.is(Blocks.LAVA) ? Fluids.LAVA : Fluids.EMPTY;
						if (fluid != Fluids.EMPTY && getFilledBucket(fluid) != null) {
							player.awardStat(Stats.ITEM_USED.get(this));

							SoundEvent emptySound = this.getFluid().getAttributes().getEmptySound();
							if (emptySound == null)
								emptySound = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
							player.playSound(emptySound, 1.0F, 1.0F);
							ItemStack newBucket = ItemUtils.createFilledResult(stack, player, getFilledBucket(fluid));
							if (this.getFluid() != Fluids.EMPTY)
								setFluidLevel(newBucket, bucketLevel + 1);
							if (!level.isClientSide) {
								CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, newBucket);
							}

							return InteractionResultHolder.sidedSuccess(newBucket, level.isClientSide());
						}

						if (sourceState.is(Blocks.POWDER_SNOW)) {
							player.playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW, 1.0F, 1.0F);
							ItemStack newBucket = ItemUtils.createFilledResult(stack, player, new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get()));
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

					// Placing contents into Cauldron
					pos = result.getBlockPos();
					direction = result.getDirection();
					sourcePos = pos.relative(direction);
					BlockState state = level.getBlockState(pos);
					if (state.getBlock() instanceof AbstractCauldronBlock) {
						Block cauldron = getCauldron();
						if (cauldron != null) {
							player.awardStat(Stats.FILL_CAULDRON);
							BlockState newState = cauldron.defaultBlockState();
							if (newState.hasProperty(LayeredCauldronBlock.LEVEL))
								newState = newState.setValue(LayeredCauldronBlock.LEVEL, 3);
							level.setBlockAndUpdate(pos, newState);
							playEmptySound(null, level, pos);
							return InteractionResultHolder.sidedSuccess(emptyBucket(stack, player), level.isClientSide());
						}
					}

					// Placing contents
					BlockPos newPos = canBlockContainFluid(level, pos, state) || (this.getFluid() != Fluids.EMPTY && bucketLevel < 2) ? pos : sourcePos;
					if (this.tryPlaceContainedLiquid(player, level, newPos, result)) {
						this.onLiquidPlaced(level, stack, newPos);
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, newPos, stack);
						}

						player.awardStat(Stats.ITEM_USED.get(this));
						return InteractionResultHolder.sidedSuccess(emptyBucket(stack, player), level.isClientSide());
					} else {
						return InteractionResultHolder.fail(stack);
					}
				}
			} else {
				return InteractionResultHolder.fail(stack);
			}
		}
	}

	public static ItemStack emptyBucket(ItemStack stack, Player player) {
		int level = stack.getOrCreateTag().getInt(NBT_TAG);
		ItemStack returnStack = level > 0 ? stack : getEmptyBucket();
		if (!player.getAbilities().instabuild)
			decreaseFluidLevel(stack);
		return !player.getAbilities().instabuild ? returnStack : stack;
	}

	public void onLiquidPlaced(Level worldIn, ItemStack p_203792_2_, BlockPos pos) {
	}

	public boolean tryPlaceContainedLiquid(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
		if (!(this.getFluid() instanceof FlowingFluid)) {
			return false;
		} else {
			BlockState state = level.getBlockState(pos);
			Block block = state.getBlock();
			Material material = state.getMaterial();
			boolean replaceable = state.canBeReplaced(this.getFluid());
			if (!(state.isAir() || replaceable || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, state, this.getFluid()))) {
				return result != null && this.tryPlaceContainedLiquid(player, level, result.getBlockPos().relative(result.getDirection()), null);
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
				if (!level.isClientSide && replaceable && !material.isLiquid()) {
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
		SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
		if (soundevent == null)
			soundevent = this.getFluid().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
		level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
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
		return null;

	}

	public static ItemStack getBucketForCauldron(AbstractCauldronBlock block) {
		if (block == Blocks.WATER_CAULDRON) return new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get());
		if (block == Blocks.LAVA_CAULDRON) return new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get());
		if (block == Blocks.POWDER_SNOW_CAULDRON) return new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get());
		return null;
	}

	public Block getCauldron() {
		if (this.getFluid() == Fluids.WATER) return Blocks.WATER_CAULDRON;
		if (this.getFluid() == Fluids.LAVA) return Blocks.LAVA_CAULDRON;
		return null;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
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