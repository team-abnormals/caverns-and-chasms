package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface FragileBlock {

	default void breakNeighbors(Level level, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			Block block = level.getBlockState(blockpos).getBlock();
			if (block instanceof FragileBlock) {
				level.scheduleTick(blockpos, block, 4 + level.getRandom().nextInt(4));
			}
		}
	}

	default boolean shouldBreakNeighbors(Player player, ItemStack stack) {
		return !player.isCreative() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0;
	}
}