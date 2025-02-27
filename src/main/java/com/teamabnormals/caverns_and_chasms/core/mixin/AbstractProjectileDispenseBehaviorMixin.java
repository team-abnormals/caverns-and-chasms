package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(AbstractProjectileDispenseBehavior.class)
public abstract class AbstractProjectileDispenseBehaviorMixin {

	@Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"))
	private void modifyShoot(Projectile projectile, double x, double y, double z, float power, float uncertainty, BlockSource source) {
		BlockState state = source.getBlockState();
		Direction dir = state.getValue(BlockStateProperties.FACING);
		if (state.getBlock() == CCBlocks.SCATTERER.get()) {
			ScattererBlock scatterer = (ScattererBlock) state.getBlock();
			RandomSource random = projectile.level().random;
			if (dir == Direction.EAST || dir == Direction.WEST) {
				z += (random.nextGaussian() * 0.1D * (random.nextBoolean() ? 1 : -1));
			}
			if (dir == Direction.NORTH || dir == Direction.SOUTH) {
				x += (random.nextGaussian() * 0.1D * (random.nextBoolean() ? 1 : -1));
			}
			if (dir.getAxis().getPlane() == Direction.Plane.VERTICAL) {
				x += random.nextGaussian() * scatterer.powerLevel/50 * 0.1D * (random.nextBoolean() ? 1 : -1);
				z += random.nextGaussian() * scatterer.powerLevel/50 * 0.1D * (random.nextBoolean() ? 1 : -1);
			}
			power *= (float) scatterer.powerLevel / 10;
			uncertainty -= (float) scatterer.powerLevel / 10;
		}
		projectile.shoot(x, y, z, power, uncertainty);
	}
}