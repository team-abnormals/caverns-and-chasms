package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StorageDuctHatchBlockEntity extends BlockEntity {
	public StorageDuctHatchBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.STORAGE_DUCT_HATCH.get(), pos, state);
	}
}