package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.common.entity.KunaiEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KunaiItem extends Item {

	public KunaiItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isClientSide()) {
			KunaiEntity kunai = new KunaiEntity(worldIn, playerIn);
			kunai.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 2.5F, 0.75F);
			if (playerIn.getAbilities().instabuild) {
				kunai.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}
			worldIn.addFreshEntity(kunai);
		}
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		if (!playerIn.getAbilities().instabuild) {
			itemstack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
	}
}
