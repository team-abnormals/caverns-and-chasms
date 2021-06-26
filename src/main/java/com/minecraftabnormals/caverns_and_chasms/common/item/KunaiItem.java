package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.common.entity.KunaiEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class KunaiItem extends Item {

	public KunaiItem(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isClientSide()) {
			KunaiEntity kunai = new KunaiEntity(worldIn, playerIn);
			kunai.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 2.5F, 0.75F);
			if (playerIn.abilities.instabuild) {
				kunai.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
			}
			worldIn.addFreshEntity(kunai);
		}
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		if (!playerIn.abilities.instabuild) {
			itemstack.shrink(1);
		}
		return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	}
}
