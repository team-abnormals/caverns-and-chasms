package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FragileStoneBlock extends Block implements FragileBlock {

	public FragileStoneBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ParticleOptions getDustParticle() {
		return CCParticleTypes.STONE_DUST.get();
	}

	@Override
	public ParticleOptions getChipParticle() {
		return CCParticleTypes.STONE_CHIP.get();
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		if (!level.isClientSide() && this.shouldBreakNeighbors(player, stack)) {
			this.breakNeighbors(level, pos);
		}
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
	}

	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
		if (!level.isClientSide()) {
			this.breakNeighbors(level, pos);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		this.breakNeighbors(level, pos);
		this.crack(level, state, pos, random);
	}
}