package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.BlockHitResult;
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
		boolean connectsabove = isParallelDoor(abovestate, facing, face);
		boolean connectsbelow = isParallelDoor(belowstate, facing, face);

		BlockState newstate = connectsabove ? CCBlocks.ROLLER_DOOR.get().defaultBlockState() : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
		int newopenness = connectsabove ? abovestate.getValue(RollerDoorBlock.OPENNESS) : connectsbelow ? belowstate.getValue(RollerDoorBlock.OPENNESS) : openness;

		if (connectsabove && this instanceof RollerDoorHeaderBlock && abovestate.getBlock() instanceof RollerDoorHeaderBlock)
			if (level.getBlockEntity(pos) instanceof RollerDoorHeaderBlockEntity door && level.getBlockEntity(abovepos) instanceof RollerDoorHeaderBlockEntity aboveDoor)
				aboveDoor.deserializeNBT(door.serializeNBT());

		return newstate.setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.OPENNESS, newopenness).setValue(RollerDoorBlock.BOTTOM, !connectsbelow).setValue(RollerDoorBlock.WATERLOGGED, waterlogged);
	}

	default InteractionResult handleLifting(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!player.getItemInHand(hand).is(CCBlocks.ROLLER_DOOR.get().asItem()) && (state.getValue(RollerDoorBlock.BOTTOM) || isHitResultInLiftArea(state, level, pos, hitResult))) {
			if (!level.isClientSide) {
				RollerDoorHeaderBlockEntity blockEntity = findHeaderBlockEntity(level, state, pos);
				if (blockEntity != null)
					blockEntity.setBeingLifted();
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	static boolean isHitResultInLiftArea(BlockState state, LevelAccessor level, BlockPos pos, BlockHitResult hitResult) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		BlockState belowstate = level.getBlockState(pos.relative(getBelowDirection(facing, face)));
		return isParallelDoor(belowstate, facing, face) && belowstate.getValue(RollerDoorBlock.BOTTOM) && hitResult.getLocation().y - pos.getY() < state.getValue(RollerDoorBlock.OPENNESS) / 16.0D;
	}

	static RollerDoorHeaderBlockEntity findHeaderBlockEntity(LevelAccessor level, BlockState state, BlockPos pos) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		Direction direction = getAboveDirection(facing, face);
		MutableBlockPos mutable = pos.mutable();
		while (true) {
			if (level.getBlockEntity(mutable) instanceof RollerDoorHeaderBlockEntity rollerDoor)
				return rollerDoor;
			else if (!isParallelDoor(level.getBlockState(mutable), facing, face))
				return null;
			mutable.move(direction);
		}
	}

	static Direction getAboveDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.UP : facing;
	}

	static Direction getBelowDirection(Direction facing, AttachFace face) {
		return face == AttachFace.WALL ? Direction.DOWN : facing.getOpposite();
	}

	static boolean isParallelDoor(BlockState neighborState, Direction facing, AttachFace face) {
		return neighborState.getBlock() instanceof RollerDoor && neighborState.getValue(RollerDoorBlock.FACING) == facing && neighborState.getValue(RollerDoorBlock.FACE) == face;
	}
}
