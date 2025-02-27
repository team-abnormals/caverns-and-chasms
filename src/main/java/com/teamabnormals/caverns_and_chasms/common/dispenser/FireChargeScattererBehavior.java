package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class FireChargeScattererBehavior extends DefaultDispenseItemBehavior {
	@Override
	public ItemStack execute(BlockSource source, ItemStack p_123557_) {
		Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
		Position position = DispenserBlock.getDispensePosition(source);
		ScattererBlock scatterer = (ScattererBlock) source.getBlockState().getBlock();
		double d0 = position.x() + (double)((float)dir.getStepX() * 0.3F);
		double d1 = position.y() + (double)((float)dir.getStepY() * 0.3F);
		double d2 = position.z() + (double)((float)dir.getStepZ() * 0.3F);
		Level level = source.getLevel();
		RandomSource randomsource = level.random;

		double d3 = randomsource.triangle((double)dir.getStepX(), 0.11485000000000001D);
		double d4 = randomsource.triangle((double)dir.getStepY(), 0.11485000000000001D);
		double d5 = randomsource.triangle((double)dir.getStepZ(), 0.11485000000000001D);
		if (dir.getAxis() == Direction.Axis.X) {
			d5 += level.random.nextGaussian() * scatterer.powerLevel / 10 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		if (dir.getAxis() == Direction.Axis.Z) {
			d3 += level.random.nextGaussian() * scatterer.powerLevel / 10 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		if (dir.getAxis() == Direction.Axis.Y) {
			d3 += level.random.nextGaussian() * scatterer.powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
			d5 += level.random.nextGaussian() * scatterer.powerLevel / 20 * 0.1D * (level.random.nextBoolean() ? 1 : -1);
		}
		SmallFireball smallfireball = new SmallFireball(level, d0, d1, d2, d3, d4, d5);
		level.addFreshEntity(Util.make(smallfireball, (p_123552_) -> {
			p_123552_.setItem(p_123557_);
		}));
		p_123557_.shrink(1);
		return p_123557_;
	}

	@Override
	protected void playSound(BlockSource p_123554_) {
		p_123554_.getLevel().levelEvent(1018, p_123554_.getPos(), 0);
	}
}