package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class CCSkullBlockEntity extends SkullBlockEntity {
	private float rot;
	private float oldRot;

	public CCSkullBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
		this.rotateToNormalPosition(state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return CCBlockEntityTypes.SKULL.get();
	}

	public static void animation(Level level, BlockPos pos, BlockState state, CCSkullBlockEntity blockEntity) {
		blockEntity.oldRot = blockEntity.rot;
		if (level.hasNeighborSignal(pos)) {
			double x = pos.getX() + 0.5F;
			double z = pos.getZ() + 0.5F;
			Player player = level.getNearestPlayer(x, pos.getY(), z, 16.0F, false);
			if (player != null) {
				double xDist = player.getX() - x;
				double zDist = player.getZ() - z;
				blockEntity.rot = (float) (Mth.atan2(zDist, xDist) + ((float) Math.PI / 2.0F)) - ((float) Math.PI * 2.0F) - Mth.PI;
			}
		} else {
			blockEntity.rotateToNormalPosition(state);
		}
	}

	private void rotateToNormalPosition(BlockState state) {
		boolean flag = state.getBlock() instanceof WallSkullBlock;
		Direction direction = flag ? state.getValue(WallSkullBlock.FACING) : null;
		int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : state.getValue(SkullBlock.ROTATION);
		this.rot = RotationSegment.convertToDegrees(i) * Mth.DEG_TO_RAD - Mth.PI;
	}

	@Override
	public float getAnimation(float partialTick) {
		float f = this.rot - this.oldRot;

		while (f >= Mth.PI)
			f -= Mth.TWO_PI;

		while (f < -Mth.PI)
			f += Mth.TWO_PI;

		return this.oldRot + f * partialTick;
	}
}