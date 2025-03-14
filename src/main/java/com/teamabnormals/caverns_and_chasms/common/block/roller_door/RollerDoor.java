package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface RollerDoor {
	static VoxelShape[] makeShapes(double x1, double y1, double z1, double x2, double y2, double z2, VoxelShape header, Direction openingDirection) {
		VoxelShape[] shapes = new VoxelShape[16];
		for (int i = 0; i < 16; i++) {
			if (openingDirection == Direction.EAST)
				x1 = i;
			else if (openingDirection == Direction.WEST)
				x2 = 16 - i;
			else if (openingDirection == Direction.UP)
				y1 = i;
			else if (openingDirection == Direction.DOWN)
				y2 = 16 - i;
			else if (openingDirection == Direction.SOUTH)
				z1 = i;
			else
				z2 = 16 - i;

			shapes[i] = Shapes.or(Block.box(x1, y1, z1, x2, y2, z2), header);
		}
		return shapes;
	}

	default BlockState getUpdatedState(LevelAccessor level, BlockPos pos, Direction facing, AttachFace face, int openness, boolean waterlogged) {
		BlockPos abovepos = pos.relative(getAboveDirection(facing, face));
		BlockPos belowpos = pos.relative(getBelowDirection(facing, face));
		BlockState abovestate = level.getBlockState(abovepos);
		BlockState belowstate = level.getBlockState(belowpos);
		boolean connectsabove = abovestate.getBlock() instanceof RollerDoor && abovestate.getValue(RollerDoorBlock.FACING) == facing && abovestate.getValue(RollerDoorBlock.FACE) == face;
		boolean connectsbelow = belowstate.getBlock() instanceof RollerDoor && belowstate.getValue(RollerDoorBlock.FACING) == facing && belowstate.getValue(RollerDoorBlock.FACE) == face;

		BlockState newstate = connectsabove ? CCBlocks.ROLLER_DOOR.get().defaultBlockState() : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
		int newopenness = connectsabove ? abovestate.getValue(RollerDoorBlock.OPENNESS) : connectsbelow ? belowstate.getValue(RollerDoorBlock.OPENNESS) : openness;

		if (connectsabove && this instanceof RollerDoorHeaderBlock && abovestate.getBlock() instanceof RollerDoorHeaderBlock)
			if (level.getBlockEntity(pos) instanceof RollerDoorHeaderBlockEntity door && level.getBlockEntity(abovepos) instanceof RollerDoorHeaderBlockEntity aboveDoor)
				aboveDoor.deserializeNBT(door.serializeNBT());

		return newstate.setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.OPENNESS, newopenness).setValue(RollerDoorBlock.BOTTOM, !connectsbelow).setValue(RollerDoorBlock.WATERLOGGED, waterlogged);
	}

	static Direction getAboveDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.UP : facing;
	}

	static Direction getBelowDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.DOWN : facing.getOpposite();
	}
}
