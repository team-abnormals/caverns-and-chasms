package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ScattererBlockEntity extends DispenserBlockEntity {
	public ScattererBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.SCATTERER.get(), pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.caverns_and_chasms.scatterer");
	}
}
