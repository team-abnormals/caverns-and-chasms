package com.teamabnormals.caverns_and_chasms.common.block.turquoise;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class TurquoiseOreBlock extends DropExperienceBlock {

	public TurquoiseOreBlock(Properties properties, IntProvider provider) {
		super(properties, provider);
	}

	@Override
	public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean no) {
		super.spawnAfterBreak(state, level, pos, stack, no);
		this.breakParticles(level, pos);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(level, pos, state, player);
		this.breakParticles(level, pos);
	}

	public void breakParticles(Level level, BlockPos pos) {
		if (level.isClientSide) {
			RandomSource random = level.getRandom();
			for (int i = 0; i < 256; ++i) {
				double d0 = Mth.nextDouble(random, -1.0D, 1.0D);
				double d1 = Mth.nextDouble(random, -1.0D, 1.0D);
				double d2 = Mth.nextDouble(random, -1.0D, 1.0D);
				level.addParticle(CCParticleTypes.TURQUOISE_BLUE.get(), pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat(), d0, d1, d2);
			}
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level.isClientSide() && (entity.getDeltaMovement().x > 0 || entity.getDeltaMovement().z > 0)) {
			Supplier<Vec3> supplier = () -> new Vec3(Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F));
			ParticleUtils.spawnParticlesOnBlockFace(level, pos, CCParticleTypes.TURQUOISE_BLUE_STEP.get(), ConstantInt.of(2), Direction.UP, supplier, 0.55D);
		}
	}
}