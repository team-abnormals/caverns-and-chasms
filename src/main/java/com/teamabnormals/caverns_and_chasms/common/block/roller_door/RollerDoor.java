package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface RollerDoor {
	static BlockState getCorrectDoorAndOpenness(LevelAccessor level, BlockPos pos, Direction facing, int openness) {
		BlockState abovestate = level.getBlockState(pos.relative(Direction.UP));
		BlockState belowstate = level.getBlockState(pos.relative(Direction.DOWN));
		boolean connectsabove = abovestate.getBlock() instanceof RollerDoor && abovestate.getValue(RollerDoorBlock.FACING) == facing;
		boolean connectsbelow = belowstate.getBlock() instanceof RollerDoor && belowstate.getValue(RollerDoorBlock.FACING) == facing;

		BlockState returnstate;
		if (connectsabove) {
			if (connectsbelow)
				returnstate = CCBlocks.ROLLER_DOOR.get().defaultBlockState();
			else
				returnstate = CCBlocks.ROLLER_DOOR_BOTTOM.get().defaultBlockState();
		} else {
			if (connectsbelow)
				returnstate = CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
			else
				returnstate = CCBlocks.ROLLER_DOOR_HEADER_BOTTOM.get().defaultBlockState();
		}

		return returnstate.setValue(RollerDoorBlock.OPENNESS, connectsabove ? abovestate.getValue(RollerDoorBlock.OPENNESS) : connectsbelow ? belowstate.getValue(RollerDoorBlock.OPENNESS) : openness);
	}

	default boolean isHeader() {
		return false;
	}
}
