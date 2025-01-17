package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CupricCampfireBlockEntity extends CampfireBlockEntity {

	public CupricCampfireBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public BlockEntityType<?> getType() {
		return CCBlockEntityTypes.CUPRIC_CAMPFIRE.get();
	}
}
