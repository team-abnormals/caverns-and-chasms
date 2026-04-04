package com.teamabnormals.caverns_and_chasms.common.block.turquoise;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class TurquoiseWallBlock extends WallBlock {

	public TurquoiseWallBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level.isClientSide() && (entity.getDeltaMovement().x > 0 || entity.getDeltaMovement().z > 0)) {
			Supplier<Vec3> supplier = () -> new Vec3(Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F));
			ParticleUtils.spawnParticlesOnBlockFace(level, pos, CCParticleTypes.TURQUOISE_GREEN_STEP.get(), ConstantInt.of(1), Direction.UP, supplier, 0.55D);
		}
	}
}