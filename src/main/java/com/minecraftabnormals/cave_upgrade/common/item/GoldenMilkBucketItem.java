package com.minecraftabnormals.cave_upgrade.common.item;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.cave_upgrade.core.registry.CUItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GoldenMilkBucketItem extends Item {
	public GoldenMilkBucketItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!worldIn.isRemote) {
			ImmutableList<EffectInstance> effects = ImmutableList.copyOf(entityLiving.getActivePotionEffects());
			for (int i = 0; i < effects.size(); ++i) {
				entityLiving.removePotionEffect(effects.get(i).getPotion());
			}
		}

		int level = stack.getOrCreateTag().getInt("FluidLevel");

		if (entityLiving instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entityLiving;
			CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
			serverplayerentity.addStat(Stats.ITEM_USED.get(this));
		}

		if (entityLiving instanceof PlayerEntity && !((PlayerEntity) entityLiving).abilities.isCreativeMode) {
			if (level > 0)
				stack.getOrCreateTag().putInt("FluidLevel", level - 1);
			else
				stack.shrink(1);
		}

		return stack.isEmpty() ? new ItemStack(CUItems.GOLDEN_BUCKET.get()) : stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		return DrinkHelper.func_234707_a_(worldIn, playerIn, handIn);
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundNBT nbt) {
		return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		int level = stack.getOrCreateTag().getInt("FluidLevel");
		if (level > 0) {
			ItemStack newStack = new ItemStack(CUItems.GOLDEN_MILK_BUCKET.get());
			newStack.getOrCreateTag().putInt("FluidLevel", level - 1);
			return newStack;
		}
		return new ItemStack(CUItems.GOLDEN_BUCKET.get());
	}
}