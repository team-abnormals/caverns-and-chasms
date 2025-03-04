package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RollerDoorHeaderBlockEntity extends BlockEntity {
	public RollerDoorHeaderBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.ROLLER_DOOR_HEADER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorHeaderBlockEntity blockEntity) {

	}
}