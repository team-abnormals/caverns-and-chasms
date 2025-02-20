package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MovingPlayer {
	private boolean moving;

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "checkMovementStatistics", at = @At("HEAD"))
	private void checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
		this.moving = Math.sqrt(x * x + y * y + z * z) > 0;
	}

	public boolean isMoving() {
		return this.moving;
	}
}