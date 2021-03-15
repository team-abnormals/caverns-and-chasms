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

public class KunaiItem extends Item {

	public KunaiItem(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote()) {
			KunaiEntity kunai = new KunaiEntity(worldIn, playerIn);
			kunai.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 0.75F);
			if (playerIn.abilities.isCreativeMode) {
				kunai.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
			}
			worldIn.addEntity(kunai);
		}
		playerIn.addStat(Stats.ITEM_USED.get(this));
		if (!playerIn.abilities.isCreativeMode) {
			itemstack.shrink(1);
		}
		return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
	}
}
