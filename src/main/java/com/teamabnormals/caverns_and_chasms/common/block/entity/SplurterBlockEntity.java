package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SplurterBlockEntity extends DispenserBlockEntity {

	public SplurterBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.SPLURTER.get(), pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.caverns_and_chasms.splurter");
	}
}
