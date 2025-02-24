package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StorageDuctBlockEntity extends BlockEntity {
	public StorageDuctBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(CCBlockEntityTypes.STORAGE_DUCT.get(), p_155229_, p_155230_);
	}
}