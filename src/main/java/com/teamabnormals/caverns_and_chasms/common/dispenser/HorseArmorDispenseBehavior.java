package com.teamabnormals.caverns_and_chasms.common.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public class HorseArmorDispenseBehavior extends OptionalDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		for (AbstractHorse abstracthorse : source.getLevel().getEntitiesOfClass(AbstractHorse.class, new AABB(blockpos), (p_123533_) -> p_123533_.isAlive() && p_123533_.canWearArmor())) {
			if (abstracthorse.isArmor(stack) && !abstracthorse.isWearingArmor() && abstracthorse.isTamed()) {
				abstracthorse.getSlot(401).set(stack.split(1));
				this.setSuccess(true);
				return stack;
			}
		}

		return super.execute(source, stack);
	}
}