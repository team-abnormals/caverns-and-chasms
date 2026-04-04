package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), CCSoundEvents.KUNAI_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
		player.getCooldowns().addCooldown(this, 3);
		if (!level.isClientSide()) {
			Kunai kunai = new Kunai(level, player);
			kunai.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.75F);
			if (player.getAbilities().instabuild) {
				kunai.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}
			level.addFreshEntity(kunai);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
}
