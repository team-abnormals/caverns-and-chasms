package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GoldenBucketItem extends Item {
	private final Supplier<? extends Fluid> fluidSupplier;

	public GoldenBucketItem(java.util.function.Supplier<? extends Fluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ItemStack stack = new ItemStack(this);
			stack.getOrCreateTag().putInt("FluidLevel", 0);
			items.add(stack);
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("FluidLevel", 0);
		return stack;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
		stack.getOrCreateTag().putInt("FluidLevel", 0);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		CompoundTag tag = stack.getOrCreateTag();
		int level = tag.getInt("FluidLevel");

		BlockHitResult result = getPlayerPOVHitResult(worldIn, playerIn, (this.getFluid() == Fluids.EMPTY || level < 2) && !(playerIn.isCrouching() && this.getFluid() != Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
		InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, stack, result);
		if (ret != null) return ret;
		if (result.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(stack);
		} else if (result.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		} else {
			BlockPos blockpos = result.getBlockPos();
			Direction direction = result.getDirection();
			BlockPos blockpos1 = blockpos.relative(direction);
//			if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos1, direction, stack)) {
//				if (this.getFluid() == Fluids.EMPTY || (level < 2 && worldIn.getBlockState(blockpos).getFluidState().getType() == this.getFluid())) {
//					BlockState blockstate1 = worldIn.getBlockState(blockpos);
//					if (blockstate1.getBlock() instanceof LayeredCauldronBlock && blockstate1.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL) {
//						ItemStack itemstack1 = ItemUtils.createFilledResult(stack, playerIn, new ItemStack(getFilledBucket(Fluids.WATER)));
//						if (this.getFluid() != Fluids.EMPTY)
//							stack.getOrCreateTag().putInt("FluidLevel", level + 1);
//
//						playerIn.awardStat(Stats.USE_CAULDRON);
//						((LayeredCauldronBlock) blockstate1.getBlock()).setWaterLevel(worldIn, blockpos, blockstate1, 0);
//						worldIn.playSound(null, blockpos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
//
//						return InteractionResultHolder.sidedSuccess(itemstack1, worldIn.isClientSide());
//					}
//
//					if (blockstate1.getBlock() instanceof BucketPickup) {
//						Fluid fluid = ((BucketPickup) blockstate1.getBlock()).takeLiquid(worldIn, blockpos, blockstate1);
//						if (fluid != Fluids.EMPTY) {
//							playerIn.awardStat(Stats.ITEM_USED.get(this));
//
//							SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
//							if (soundevent == null)
//								soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
//							playerIn.playSound(soundevent, 1.0F, 1.0F);
//							ItemStack itemstack1 = ItemUtils.createFilledResult(stack, playerIn, new ItemStack(getFilledBucket(fluid)));
//							if (this.getFluid() != Fluids.EMPTY)
//								itemstack1.getOrCreateTag().putInt("FluidLevel", level + 1);
//							if (!worldIn.isClientSide) {
//								CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) playerIn, new ItemStack(getFilledBucket(fluid)));
//							}
//
//							return InteractionResultHolder.sidedSuccess(itemstack1, worldIn.isClientSide());
//						}
//					}
//
//					return InteractionResultHolder.fail(stack);
//				} else {
//					result = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.NONE);
//					if (result.getType() == HitResult.Type.MISS) {
//						return InteractionResultHolder.pass(stack);
//					} else if (result.getType() != HitResult.Type.BLOCK) {
//						return InteractionResultHolder.pass(stack);
//					}
//
//					blockpos = result.getBlockPos();
//					direction = result.getDirection();
//					blockpos1 = blockpos.relative(direction);
//					BlockState blockstate = worldIn.getBlockState(blockpos);
//					if (blockstate.is(Blocks.CAULDRON) && this.getFluid() == Fluids.WATER) {
//						playerIn.awardStat(Stats.FILL_CAULDRON);
//						((CauldronBlock) blockstate.getBlock()).setWaterLevel(worldIn, blockpos, blockstate, 3);
//						worldIn.playSound(null, blockpos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
//						return InteractionResultHolder.sidedSuccess(emptyBucket(stack, playerIn), worldIn.isClientSide());
//					}
//
//					BlockPos blockpos2 = canBlockContainFluid(worldIn, blockpos, blockstate) || (this.getFluid() != Fluids.EMPTY && level < 2) ? blockpos : blockpos1;
//					if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos2, result)) {
//						this.onLiquidPlaced(worldIn, stack, blockpos2);
//						if (playerIn instanceof ServerPlayer) {
//							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, blockpos2, stack);
//						}
//
//						playerIn.awardStat(Stats.ITEM_USED.get(this));
//						return InteractionResultHolder.sidedSuccess(emptyBucket(stack, playerIn), worldIn.isClientSide());
//					} else {
//						return InteractionResultHolder.fail(stack);
//					}
//				}
//			} else {
			return InteractionResultHolder.fail(stack);
//			}
		}
	}

	public static ItemStack emptyBucket(ItemStack stack, Player player) {
		int level = stack.getOrCreateTag().getInt("FluidLevel");
		ItemStack returnStack = level > 0 ? stack : getEmptyBucket();
		if (level > 0 && !player.getAbilities().instabuild)
			returnStack.getOrCreateTag().putInt("FluidLevel", level - 1);
		return !player.getAbilities().instabuild ? returnStack : stack;
	}

	public void onLiquidPlaced(Level worldIn, ItemStack p_203792_2_, BlockPos pos) {
	}

	public boolean tryPlaceContainedLiquid(@Nullable Player player, Level worldIn, BlockPos posIn, @Nullable BlockHitResult rayTrace) {
		if (!(this.getFluid() instanceof FlowingFluid)) {
			return false;
		} else {
			BlockState blockstate = worldIn.getBlockState(posIn);
			Block block = blockstate.getBlock();
			Material material = blockstate.getMaterial();
			boolean flag = blockstate.canBeReplaced(this.getFluid());
			boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid());
			if (!flag1) {
				return rayTrace != null && this.tryPlaceContainedLiquid(player, worldIn, rayTrace.getBlockPos().relative(rayTrace.getDirection()), (BlockHitResult) null);
			} else if (worldIn.dimensionType().ultraWarm() && this.getFluid().is(FluidTags.WATER)) {
				int i = posIn.getX();
				int j = posIn.getY();
				int k = posIn.getZ();
				worldIn.playSound(player, posIn, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

				for (int l = 0; l < 8; ++l) {
					worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
				}

				return true;
			} else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, getFluid())) {
				((LiquidBlockContainer) block).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid) this.getFluid()).getSource(false));
				this.playEmptySound(player, worldIn, posIn);
				return true;
			} else {
				if (!worldIn.isClientSide && flag && !material.isLiquid()) {
					worldIn.destroyBlock(posIn, true);
				}

				if (!worldIn.setBlock(posIn, this.getFluid().defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
					return false;
				} else {
					this.playEmptySound(player, worldIn, posIn);
					return true;
				}
			}
		}
	}

	protected void playEmptySound(@Nullable Player player, LevelAccessor worldIn, BlockPos pos) {
		SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
		if (soundevent == null)
			soundevent = this.getFluid().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
		worldIn.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
		if (this.getClass() == GoldenBucketItem.class)
			return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
		else
			return super.initCapabilities(stack, nbt);
	}

	public Fluid getFluid() {
		return fluidSupplier.get();
	}

	private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
		return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid());
	}

	public static Item getFilledBucket(Fluid fluid) {
		if (fluid == Fluids.WATER) {
			return CCItems.GOLDEN_WATER_BUCKET.get();
		} else if (fluid == Fluids.LAVA) {
			return CCItems.GOLDEN_LAVA_BUCKET.get();
		} else {
			return CCItems.GOLDEN_BUCKET.get();
		}
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt("FluidLevel");
		if (level > 0) {
			ItemStack newStack = new ItemStack(getFilledBucket(this.getFluid()));
			newStack.getOrCreateTag().putInt("FluidLevel", level - 1);
			return newStack;
		}
		return getEmptyBucket();
	}

	@Override
	public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
		return itemStack.getItem() == CCItems.GOLDEN_LAVA_BUCKET.get() ? 20000 : super.getBurnTime(itemStack, recipeType);
	}

	public static ItemStack getEmptyBucket() {
		ItemStack stack = new ItemStack(CCItems.GOLDEN_BUCKET.get());
		stack.getOrCreateTag().putInt("FluidLevel", 0);
		return stack;
	}
}