package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.common.entity.RottenEggEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class RottenEggItem extends EggItem {

	public RottenEggItem(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isClientSide) {
			RottenEggEntity eggentity = new RottenEggEntity(worldIn, playerIn);
			eggentity.setItem(itemstack);
			eggentity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
			worldIn.addFreshEntity(eggentity);
		}
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		if (!playerIn.abilities.instabuild) {
			itemstack.shrink(1);
		}
		return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	}
}
