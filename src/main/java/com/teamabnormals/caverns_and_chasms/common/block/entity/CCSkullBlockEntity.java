package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CCSkullBlockEntity extends SkullBlockEntity {

	public CCSkullBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return CCBlockEntityTypes.SKULL.get();
	}

	@Override
	public float getAnimation(float p_59763_) {
		if (this.isAnimating && this.getLevel() != null) {
			double x = this.getBlockPos().getX() + 0.5F;
			double z = this.getBlockPos().getZ() + 0.5F;
			Player player = this.getLevel().getNearestPlayer(x, this.getBlockPos().getY(), z, 16.0F, false);
			if (player != null) {
				double xDist = player.getX() - x;
				double zDist = player.getZ() - z;
				return (!(Math.abs(zDist) > (double) 1.0E-5F) && !(Math.abs(xDist) > (double) 1.0E-5F) ? 0.0F : (float) (Mth.atan2(zDist, xDist) + ((float) Math.PI / 2.0F))) - ((float) Math.PI * 2.0F);
			}
		}
		return 0.0F;
	}
}