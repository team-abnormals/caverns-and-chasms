package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Sets;
import com.teamabnormals.blueprint.common.entity.BlueprintFallingBlockEntity;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashSet;

public interface FragileBlock {
	@Nonnull
	String getDustParticle();

	@Nonnull
	String getChipParticle();

	default void breakNeighbors(Level level, BlockPos pos) {
		HashSet<BlockPos> positions = Sets.newHashSet();
		boolean dropOres = CCConfig.COMMON.fragileStoneDropsOresEnabled;

		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.relative(direction);
			BlockState state = level.getBlockState(offsetPos);
			if (state.getBlock() instanceof FragileBlock) {
				level.scheduleTick(offsetPos, state.getBlock(), 4 + level.getRandom().nextInt(4));
			}

			if (dropOres && canFall(level, offsetPos)) {
				positions.add(offsetPos);
				positions.addAll(findNearbyOres(positions, level, offsetPos));
			}
		}

		if (dropOres) {
			for (BlockPos offsetPos : positions.stream().sorted(Comparator.comparingInt(Vec3i::getY)).toList()) {
				BlockState state = level.getBlockState(offsetPos);

				if (level.getBlockState(offsetPos.below()).canBeReplaced() || offsetPos.below().equals(pos)) {
					BlueprintFallingBlockEntity fallingOre = BlueprintFallingBlockEntity.fall(level, offsetPos, state);
					fallingOre.time = -100;
					level.addFreshEntity(fallingOre);

					this.crack(level, state, offsetPos, level.getRandom());
				}
			}
		}
	}

	static HashSet<BlockPos> findNearbyOres(HashSet<BlockPos> currentPositions, Level level, BlockPos pos) {
		if (canFall(level, pos)) {
			currentPositions.add(pos);
			for (Direction dir : Direction.values()) {
				BlockPos offsetPos = pos.relative(dir);
				if (!currentPositions.contains(offsetPos)) {
					currentPositions.addAll(findNearbyOres(currentPositions, level, offsetPos));
				}
			}
		}

		return currentPositions;
	}

	static boolean blockCanFall(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.canBeReplaced() || state.getBlock() instanceof FragileBlock;
	}

	static boolean canFall(Level level, BlockPos pos) {
		if (level.getBlockState(pos).is(Tags.Blocks.ORES)) {
			boolean canFall = blockCanFall(level, pos.below());
			if (!canFall) {
				int i = 0;
				boolean touchingOre = true;
				while (touchingOre) {
					i++;
					touchingOre = level.getBlockState(pos.below(i)).is(Tags.Blocks.ORES);
				}
				if (blockCanFall(level, pos.below(i))) {
					canFall = true;
				}
			}

			if (canFall) {
				boolean hasSupport = false;
				for (Direction direction : Direction.values()) {
					BlockState state = level.getBlockState(pos.relative(direction));
					if (!state.is(Tags.Blocks.ORES) && !blockCanFall(level, pos.relative(direction)) && !(state.getBlock() instanceof FallingBlock)) {
						hasSupport = true;
						break;
					}
				}
				return !hasSupport;
			}

		}

		return false;
	}

	default void crack(Level level, BlockState state, BlockPos pos, RandomSource random) {
		double d0 = pos.getX() + random.nextDouble() * 0.8D + 0.1D;
		double d1 = pos.getY() + random.nextDouble() * 0.8D + 0.1D;
		double d2 = pos.getZ() + random.nextDouble() * 0.8D + 0.1D;
		double d3 = random.nextGaussian() * 0.04D;
		double d4 = random.nextGaussian() * 0.04D;
		double d5 = random.nextGaussian() * 0.04D;
		NetworkUtil.spawnParticle(this.getDustParticle(), d0, d1, d2, d3, d4, d5);

		int i = random.nextInt(2) + 1;
		for (int j = 0; j < i; ++j) {
			double d6 = pos.getX() + random.nextDouble() * 0.8D + 0.1D;
			double d7 = pos.getY() + random.nextDouble() * 0.8D + 0.1D;
			double d8 = pos.getZ() + random.nextDouble() * 0.8D + 0.1D;
			double d9 = ((double) random.nextFloat() - 0.5D) * 0.02D;
			double d10 = ((double) random.nextFloat() - 0.5D) * 0.02D;
			NetworkUtil.spawnParticle(this.getChipParticle(), d6, d7, d8, d9, -0.4D, d10);
		}

		SoundType soundtype = state.getSoundType(level, pos, null);
		level.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		level.removeBlock(pos, true);
		level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
	}

	default boolean shouldBreakNeighbors(Player player, ItemStack stack) {
		return !player.isCreative() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0;
	}
}