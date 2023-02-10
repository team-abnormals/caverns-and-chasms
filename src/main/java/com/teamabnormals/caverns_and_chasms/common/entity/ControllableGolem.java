package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.caverns_and_chasms.common.item.TuningForkItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ControllableGolem {

	boolean canBeControlled(Player controller);

	void onTuningForkControl(Player controller);

	boolean shouldMoveToTuningForkPos(BlockPos pos, Player controller);

	boolean shouldAttackTuningForkTarget(LivingEntity target, Player controller);

	void setControllerUUID(UUID uuid);

	@Nullable
	UUID getControllerUUID();

	default void setController(Player player) {
		if (player != null)
			this.setControllerUUID(player.getUUID());
		else
			this.setControllerUUID(null);
	}

	@Nullable
	default Player getController() {
		try {
			UUID uuid = this.getControllerUUID();
			return uuid == null ? null : ((Mob) this).level.getPlayerByUUID(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	void setForgetControllerTime(int time);

	int getForgetControllerTime();

	void setTuningForkPos(BlockPos pos);

	@Nullable
	BlockPos getTuningForkPos();

	void setTuningForkTarget(LivingEntity target);

	@Nullable
	LivingEntity getTuningForkTarget();

	default void tuningForkBehaviorTick() {
		if (!((Mob) this).level.isClientSide) {
			Player controller = this.getController();
			int forgettime = this.getForgetControllerTime();
			if (!this.canBeControlled(controller) || !controller.isAlive() || forgettime <= 0) {
				this.setController(null);
			} else {
				if (((Mob) this).distanceToSqr(controller) > 256.0D || (!(controller.getMainHandItem().getItem() instanceof TuningForkItem) && !(controller.getOffhandItem().getItem() instanceof TuningForkItem))) {
					this.setForgetControllerTime(forgettime - 1);
				} else {
					this.setForgetControllerTime(200);
				}
			}
		}
	}
}
