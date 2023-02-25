package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class TMTDispenseBehavior extends DefaultDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.getLevel();
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		PrimedTmt primedtmt = new PrimedTmt(level, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, null);
		level.addFreshEntity(primedtmt);
		level.playSound(null, primedtmt.getX(), primedtmt.getY(), primedtmt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
		stack.shrink(1);
		return stack;
	}
}