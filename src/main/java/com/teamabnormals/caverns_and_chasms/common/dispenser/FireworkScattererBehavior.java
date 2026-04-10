package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class FireworkScattererBehavior extends ProjectileDispenseBehavior {

	public FireworkScattererBehavior(Item projectile) {
		super(projectile);
	}

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		ScattererBlock scatterer = (ScattererBlock) source.state().getBlock();
		Direction dir = source.state().getValue(DispenserBlock.FACING);
		Position position = this.dispenseConfig.positionFunction().getDispensePosition(source, dir);
		Projectile projectile = this.projectileItem.asProjectile(level, position, stack, dir);

		double x = dir.getStepX();
		double y = dir.getStepY();
		double z = dir.getStepZ();
		if (dir.getAxis() == Direction.Axis.X) {
			z += level.random.nextGaussian() * scatterer.powerLevel / 5 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		if (dir.getAxis() == Direction.Axis.Z) {
			x += level.random.nextGaussian() * scatterer.powerLevel / 5 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		if (dir.getAxis() == Direction.Axis.Y) {
			x += level.random.nextGaussian() * scatterer.powerLevel / 5 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
			z += level.random.nextGaussian() * scatterer.powerLevel / 5 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		this.projectileItem.shoot(projectile, x, y, z, (0.5F) * ((float) scatterer.powerLevel / 10), 1.0F);
		source.level().addFreshEntity(projectile);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		source.level().levelEvent(1004, source.pos(), 0);
	}
}