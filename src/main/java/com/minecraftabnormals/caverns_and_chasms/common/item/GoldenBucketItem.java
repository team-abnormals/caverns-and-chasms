package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GoldenBucketItem extends Item {
	private final Supplier<? extends Fluid> fluidSupplier;

	public GoldenBucketItem(java.util.function.Supplier<? extends Fluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
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
	public void onCraftedBy(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		stack.getOrCreateTag().putInt("FluidLevel", 0);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		CompoundNBT tag = stack.getOrCreateTag();
		int level = tag.getInt("FluidLevel");

		BlockRayTraceResult result = getPlayerPOVHitResult(worldIn, playerIn, (this.getFluid() == Fluids.EMPTY || level < 2) && !(playerIn.isCrouching() && this.getFluid() != Fluids.EMPTY) ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, stack, result);
		if (ret != null) return ret;
		if (result.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.pass(stack);
		} else if (result.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.pass(stack);
		} else {
			BlockPos blockpos = result.getBlockPos();
			Direction direction = result.getDirection();
			BlockPos blockpos1 = blockpos.relative(direction);
			if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos1, direction, stack)) {
				if (this.getFluid() == Fluids.EMPTY || (level < 2 && worldIn.getBlockState(blockpos).getFluidState().getType() == this.getFluid())) {
					BlockState blockstate1 = worldIn.getBlockState(blockpos);
					if (blockstate1.is(Blocks.CAULDRON) && blockstate1.getValue(CauldronBlock.LEVEL) == 3) {
						ItemStack itemstack1 = DrinkHelper.createFilledResult(stack, playerIn, new ItemStack(getFilledBucket(Fluids.WATER)));
						if (this.getFluid() != Fluids.EMPTY)
							stack.getOrCreateTag().putInt("FluidLevel", level + 1);

						playerIn.awardStat(Stats.USE_CAULDRON);
						((CauldronBlock) blockstate1.getBlock()).setWaterLevel(worldIn, blockpos, blockstate1, 0);
						worldIn.playSound(null, blockpos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

						return ActionResult.sidedSuccess(itemstack1, worldIn.isClientSide());
					}

					if (blockstate1.getBlock() instanceof IBucketPickupHandler) {
						Fluid fluid = ((IBucketPickupHandler) blockstate1.getBlock()).takeLiquid(worldIn, blockpos, blockstate1);
						if (fluid != Fluids.EMPTY) {
							playerIn.awardStat(Stats.ITEM_USED.get(this));

							SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
							if (soundevent == null)
								soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
							playerIn.playSound(soundevent, 1.0F, 1.0F);
							ItemStack itemstack1 = DrinkHelper.createFilledResult(stack, playerIn, new ItemStack(getFilledBucket(fluid)));
							if (this.getFluid() != Fluids.EMPTY)
								itemstack1.getOrCreateTag().putInt("FluidLevel", level + 1);
							if (!worldIn.isClientSide) {
								CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) playerIn, new ItemStack(getFilledBucket(fluid)));
							}

							return ActionResult.sidedSuccess(itemstack1, worldIn.isClientSide());
						}
					}

					return ActionResult.fail(stack);
				} else {
					result = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
					if (result.getType() == RayTraceResult.Type.MISS) {
						return ActionResult.pass(stack);
					} else if (result.getType() != RayTraceResult.Type.BLOCK) {
						return ActionResult.pass(stack);
					}

					blockpos = result.getBlockPos();
					direction = result.getDirection();
					blockpos1 = blockpos.relative(direction);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					if (blockstate.is(Blocks.CAULDRON) && blockstate.getValue(CauldronBlock.LEVEL) == 0 && this.getFluid() == Fluids.WATER) {
						playerIn.awardStat(Stats.FILL_CAULDRON);
						((CauldronBlock) blockstate.getBlock()).setWaterLevel(worldIn, blockpos, blockstate, 3);
						worldIn.playSound(null, blockpos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						return ActionResult.sidedSuccess(emptyBucket(stack, playerIn), worldIn.isClientSide());
					}

					BlockPos blockpos2 = canBlockContainFluid(worldIn, blockpos, blockstate) || (this.getFluid() != Fluids.EMPTY && level < 2) ? blockpos : blockpos1;
					if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos2, result)) {
						this.onLiquidPlaced(worldIn, stack, blockpos2);
						if (playerIn instanceof ServerPlayerEntity) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockpos2, stack);
						}

						playerIn.awardStat(Stats.ITEM_USED.get(this));
						return ActionResult.sidedSuccess(emptyBucket(stack, playerIn), worldIn.isClientSide());
					} else {
						return ActionResult.fail(stack);
					}
				}
			} else {
				return ActionResult.fail(stack);
			}
		}
	}

	public static ItemStack emptyBucket(ItemStack stack, PlayerEntity player) {
		int level = stack.getOrCreateTag().getInt("FluidLevel");
		ItemStack returnStack = level > 0 ? stack : getEmptyBucket();
		if (level > 0 && !player.abilities.instabuild)
			returnStack.getOrCreateTag().putInt("FluidLevel", level - 1);
		return !player.abilities.instabuild ? returnStack : stack;
	}

	public void onLiquidPlaced(World worldIn, ItemStack p_203792_2_, BlockPos pos) {
	}

	public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, @Nullable BlockRayTraceResult rayTrace) {
		if (!(this.getFluid() instanceof FlowingFluid)) {
			return false;
		} else {
			BlockState blockstate = worldIn.getBlockState(posIn);
			Block block = blockstate.getBlock();
			Material material = blockstate.getMaterial();
			boolean flag = blockstate.canBeReplaced(this.getFluid());
			boolean flag1 = blockstate.isAir() || flag || block instanceof ILiquidContainer && ((ILiquidContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid());
			if (!flag1) {
				return rayTrace != null && this.tryPlaceContainedLiquid(player, worldIn, rayTrace.getBlockPos().relative(rayTrace.getDirection()), (BlockRayTraceResult) null);
			} else if (worldIn.dimensionType().ultraWarm() && this.getFluid().is(FluidTags.WATER)) {
				int i = posIn.getX();
				int j = posIn.getY();
				int k = posIn.getZ();
				worldIn.playSound(player, posIn, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

				for (int l = 0; l < 8; ++l) {
					worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
				}

				return true;
			} else if (block instanceof ILiquidContainer && ((ILiquidContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, getFluid())) {
				((ILiquidContainer) block).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid) this.getFluid()).getSource(false));
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

	protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
		SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
		if (soundevent == null)
			soundevent = this.getFluid().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
		worldIn.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
		if (this.getClass() == GoldenBucketItem.class)
			return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
		else
			return super.initCapabilities(stack, nbt);
	}

	public Fluid getFluid() {
		return fluidSupplier.get();
	}

	private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate) {
		return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid());
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
	public int getBurnTime(ItemStack itemStack) {
		return itemStack.getItem() == CCItems.GOLDEN_LAVA_BUCKET.get() ? 20000 : super.getBurnTime(itemStack);
	}

	public static ItemStack getEmptyBucket() {
		ItemStack stack = new ItemStack(CCItems.GOLDEN_BUCKET.get());
		stack.getOrCreateTag().putInt("FluidLevel", 0);
		return stack;
	}
}