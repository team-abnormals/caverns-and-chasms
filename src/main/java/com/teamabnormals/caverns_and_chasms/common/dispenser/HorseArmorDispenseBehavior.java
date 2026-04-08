package com.teamabnormals.caverns_and_chasms.common.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public class HorseArmorDispenseBehavior extends OptionalDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		for (AbstractHorse abstracthorse : source.level().getEntitiesOfClass(AbstractHorse.class, new AABB(pos), (horse) -> horse.isAlive() && horse.canUseSlot(EquipmentSlot.BODY))) {
			if (abstracthorse.isBodyArmorItem(stack) && !abstracthorse.isWearingBodyArmor() && abstracthorse.isTamed()) {
				abstracthorse.setBodyArmorItem(stack.split(1));
				this.setSuccess(true);
				return stack;
			}
		}

		return super.execute(source, stack);
	}
}