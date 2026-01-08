package com.teamabnormals.caverns_and_chasms.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class HoldableBlockEntity extends BlockEntity {
	private List<Player> interatctingPlayers;

	public HoldableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private boolean isBeingHeld() {
		for (Player player : this.interatctingPlayers) {
			if (!player.isUsingItem()) {

			}
		}

		return false;
	}
}