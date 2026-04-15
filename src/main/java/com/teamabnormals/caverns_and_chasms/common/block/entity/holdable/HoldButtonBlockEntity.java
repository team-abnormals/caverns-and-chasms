package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HoldButtonBlockEntity extends AbstractHoldableButtonBlockEntity {
	public HoldButtonBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOLD_BUTTON.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoldButtonBlockEntity blockEntity) {
		blockEntity.handleTick(level, pos, state);
	}

	@Override
	protected void tickPowered(Level level, BlockPos pos, BlockState state) {
		if (level.getGameTime() % 2 == 0) {
			level.playSound(null, pos, CCSoundEvents.TIN_BUTTON_HOLD.get(), SoundSource.BLOCKS);
		}
	}
}