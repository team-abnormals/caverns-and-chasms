package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;


public class SplurterDispenseItemBehavior extends DefaultDispenseItemBehavior {

	protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
		Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
		Position position = DispenserBlock.getDispensePosition(blockSource);
		BlockState state = blockSource.getBlockState();
		shootItem(blockSource.getLevel(), stack, 6, direction, position, ((ScattererBlock) state.getBlock()).powerLevel, true);
		return ItemStack.EMPTY;
	}

	public static void shootItem(Level level, ItemStack p_123380_, int p_123381_, Direction dir, Position p_123383_, int powerLevel, boolean splurter) {
		double d0 = p_123383_.x();
		double d1 = p_123383_.y();
		double d2 = p_123383_.z();
		if (dir.getAxis() == Direction.Axis.Y) {
			d1 -= 0.125D;
		} else {
			d1 -= 0.15625D;
		}

		ItemEntity itementity = new ItemEntity(level, d0, d1, d2, p_123380_);
		double d3 = level.random.nextDouble() * 0.1D + 0.2D;
		double xOffset = dir.getStepX() * d3;
		double yOffset = 0.2D;
		double zOffset = dir.getStepZ() * d3;

		double powerFactor = (double) powerLevel / 10;

		xOffset *= powerFactor;
		yOffset *= powerFactor;
		zOffset *= powerFactor;
		if (!splurter) {
			if (dir.getAxis() == Direction.Axis.X) {
				zOffset += level.random.nextGaussian() * powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
			}
			if (dir.getAxis() == Direction.Axis.Z) {
				xOffset += level.random.nextGaussian() * powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
			}
			if (dir.getAxis() == Direction.Axis.Y) {
				xOffset += level.random.nextGaussian() * powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
				zOffset += level.random.nextGaussian() * powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
			}
		}

		itementity.setDeltaMovement(
				level.random.triangle(xOffset, 0.0172275D * (double)p_123381_),
				level.random.triangle(yOffset, 0.0172275D * (double)p_123381_),
				level.random.triangle(zOffset, 0.0172275D * (double)p_123381_)
		);
		level.addFreshEntity(itementity);
	}
}