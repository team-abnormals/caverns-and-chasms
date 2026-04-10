package com.teamabnormals.caverns_and_chasms.core.mixin.dispenser;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileDispenseBehavior.class)
public abstract class AbstractProjectileDispenseBehaviorMixin {

	@WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileItem;shoot(Lnet/minecraft/world/entity/projectile/Projectile;DDDFF)V"))
	private void modifyShoot(ProjectileItem item, Projectile projectile, double x, double y, double z, float power, float uncertainty, Operation<ItemStack> original, BlockSource source) {
		BlockState state = source.state();
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
				x += random.nextGaussian() * scatterer.powerLevel / 50 * 0.1D * (random.nextBoolean() ? 1 : -1);
				z += random.nextGaussian() * scatterer.powerLevel / 50 * 0.1D * (random.nextBoolean() ? 1 : -1);
			}
			power *= (float) scatterer.powerLevel / 10;
			uncertainty -= (float) scatterer.powerLevel / 10;
		}

		original.call(item, projectile, x, y, z, power, uncertainty);
	}
}