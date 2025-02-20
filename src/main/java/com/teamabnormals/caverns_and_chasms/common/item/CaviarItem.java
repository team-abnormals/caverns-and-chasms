package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CaviarItem extends BlockItem {

	public CaviarItem(Properties properties) {
		super(CCBlocks.CAVIAR.get(), properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack copy = stack.copy();
		super.finishUsingItem(stack, level, entity);

		if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(this, 400);
		}

		return copy;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.PLAYER_BURP;
	}

	@Override
	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		return (context.getPlayer() == null || context.getPlayer().isSecondaryUseActive()) && super.canPlace(context, state);
	}
}