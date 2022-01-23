package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class FragileStoneBlock extends Block {
	public FragileStoneBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockentity, ItemStack stack) {
		if (!level.isClientSide() && !player.isCreative() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			breakNeighbors(level, pos);
		}
		super.playerDestroy(level, player, pos, state, blockentity, stack);
	}

	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
		if (!level.isClientSide()) {
			breakNeighbors(level, pos);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		breakNeighbors(level, pos);
		level.destroyBlock(pos, true);
	}

	private static void breakNeighbors(Level level, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			Block block = level.getBlockState(blockpos).getBlock();
			if (block == CCBlocks.FRAGILE_STONE.get()) {
				level.scheduleTick(blockpos, block, 3 + level.getRandom().nextInt(3));
			}
		}
	}
}