package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.LiftPlateBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LiftPlateBlockEntity extends BlockEntity {
	public LiftPlateBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.LIFT_PLATE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, LiftPlateBlockEntity blockEntity) {
		if (!level.isClientSide) {
			LiftPlateBlock liftPlateBlock = (LiftPlateBlock) state.getBlock();
			liftPlateBlock.deactivate(null, level, pos, state);
		}
	}
}