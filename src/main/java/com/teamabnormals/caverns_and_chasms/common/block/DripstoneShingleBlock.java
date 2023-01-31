package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DripstoneShingleBlock extends Block {

	public DripstoneShingleBlock(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
		BlockPos belowPos = pos.below();
		BlockState belowState = level.getBlockState(belowPos);
		ParticleOptions particle = ParticleTypes.DRIPPING_DRIPSTONE_WATER;

		if (stateIn.is(CCBlocks.CHISELED_DRIPSTONE_SHINGLES.get()) && level.getGameTime() % 2 == 0)
			return;

		if (belowState.getFluidState().isEmpty()) {
			VoxelShape voxelshape = belowState.getCollisionShape(level, belowPos);
			double d0 = voxelshape.max(Direction.Axis.Y);
			if (d0 < 1.0D) {
				if (stateIn.isFaceSturdy(level, belowPos, Direction.DOWN)) {
					spawnFluidParticle(level, belowPos.getX(), belowPos.getX() + 1, belowPos.getZ(), belowPos.getZ() + 1, (double) (belowPos.getY() + 1) - 0.05D, particle);
				}
			} else if (!belowState.is(BlockTags.IMPERMEABLE)) {
				double d1 = voxelshape.min(Direction.Axis.Y);
				if (d1 > 0.0D) {
					spawnParticle(level, belowPos, particle, voxelshape, (double) belowPos.getY() + d1 - 0.05D);
				} else {
					BlockPos belowBelowPos = belowPos.below();
					BlockState blockstate = level.getBlockState(belowBelowPos);
					VoxelShape voxelshape1 = blockstate.getCollisionShape(level, belowBelowPos);
					double d2 = voxelshape1.max(Direction.Axis.Y);
					if (d2 < 1.0D && blockstate.getFluidState().isEmpty()) {
						spawnParticle(level, belowPos, particle, voxelshape, (double) belowPos.getY() - 0.05D);
					}
				}
			}

		}
	}

	private void spawnParticle(Level level, BlockPos pos, ParticleOptions particle, VoxelShape voxelShape, double y) {
		spawnFluidParticle(level, (double) pos.getX() + voxelShape.min(Direction.Axis.X), (double) pos.getX() + voxelShape.max(Direction.Axis.X), (double) pos.getZ() + voxelShape.min(Direction.Axis.Z), (double) pos.getZ() + voxelShape.max(Direction.Axis.Z), y, particle);
	}

	private void spawnFluidParticle(Level level, double minX, double maxX, double minZ, double maxZ, double y, ParticleOptions particle) {
		level.addParticle(particle, Mth.lerp(level.random.nextDouble(), minX, maxX), y, Mth.lerp(level.random.nextDouble(), minZ, maxZ), 0.0D, 0.0D, 0.0D);
	}
}