package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class FireworkScattererBehavior extends DefaultDispenseItemBehavior {
	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.getLevel();
		ScattererBlock scatterer = (ScattererBlock) source.getBlockState().getBlock();
		Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
		FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(source.getLevel(), stack, source.x(), source.y(), source.x(), true);
		DispenseItemBehavior.setEntityPokingOutOfBlock(source, fireworkrocketentity, dir);

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
		fireworkrocketentity.shoot(x, y, z, (0.5F) * ((float) scatterer.powerLevel / 10), 1.0F);
		source.getLevel().addFreshEntity(fireworkrocketentity);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		source.getLevel().levelEvent(1004, source.getPos(), 0);
	}
}