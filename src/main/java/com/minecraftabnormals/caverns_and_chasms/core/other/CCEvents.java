package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID)
public class CCEvents {

	@SubscribeEvent
	public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		PlayerEntity player = event.getPlayer();
		ItemStack stack = player.getHeldItem(event.getHand());
		CompoundNBT tag = stack.getOrCreateTag();
		Hand hand = event.getHand();
		if (event.getTarget() instanceof CowEntity && !((CowEntity) event.getTarget()).isChild()) {
			if (stack.getItem() == CCItems.GOLDEN_BUCKET.get()) {
				player.swingArm(hand);
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				ItemStack milkBucket = DrinkHelper.func_241445_a_(stack, player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				player.setHeldItem(hand, milkBucket);
			} else if (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get() && tag.getInt("FluidLevel") < 2) {
				player.swingArm(hand);
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				ItemStack milkBucket = DrinkHelper.func_241445_a_(stack, player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				if (!player.abilities.isCreativeMode)
					milkBucket.getOrCreateTag().putInt("FluidLevel", tag.getInt("FluidLevel") + 1);
				player.setHeldItem(hand, milkBucket);
			}
		}
	}
}
