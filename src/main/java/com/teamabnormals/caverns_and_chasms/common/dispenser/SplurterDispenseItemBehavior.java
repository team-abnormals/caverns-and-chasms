package com.teamabnormals.caverns_and_chasms.common.dispenser;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;


public class SplurterDispenseItemBehavior extends DefaultDispenseItemBehavior {

	protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
		Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
		Position position = DispenserBlock.getDispensePosition(blockSource);
		spawnItem(blockSource.getLevel(), stack, 6, direction, position);
		return stack;
	}

	public static void spawnItem(Level p_123379_, ItemStack p_123380_, int p_123381_, Direction p_123382_, Position p_123383_) {
		double d0 = p_123383_.x();
		double d1 = p_123383_.y();
		double d2 = p_123383_.z();
		if (p_123382_.getAxis() == Direction.Axis.Y) {
			d1 -= 0.125D;
		} else {
			d1 -= 0.15625D;
		}

		ItemEntity itementity = new ItemEntity(p_123379_, d0, d1, d2, p_123380_);
		double d3 = p_123379_.random.nextDouble() * 0.1D + 0.2D;
		itementity.setDeltaMovement(p_123379_.random.triangle((double) p_123382_.getStepX() * d3, 0.0172275D * (double) p_123381_), p_123379_.random.triangle(0.2D, 0.0172275D * (double) p_123381_), p_123379_.random.triangle((double) p_123382_.getStepZ() * d3, 0.0172275D * (double) p_123381_));
		p_123379_.addFreshEntity(itementity);
	}

	protected void playSound(BlockSource p_123384_) {
		p_123384_.getLevel().levelEvent(1000, p_123384_.getPos(), 0);
	}

	protected void playAnimation(BlockSource p_123388_, Direction p_123389_) {
		p_123388_.getLevel().levelEvent(2000, p_123388_.getPos(), p_123389_.get3DDataValue());
	}
}
