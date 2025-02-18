package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MovingPlayer {
	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_36257_);

	private boolean moving;

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "checkMovementStatistics", at = @At("HEAD"))
	private void checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
		this.moving = Math.sqrt(x * x + y * y + z * z) > 0;
	}

	public boolean isMoving() {
		return moving;
	}

	@Inject(method = "isScoping", at = @At("RETURN"), cancellable = true)
	private void isScoping(CallbackInfoReturnable<Boolean> cir) {
		if ((this.isUsingItem() && this.getUseItem().is(CCItems.MONOCLE.get())) || this.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MONOCLE.get())) {
			cir.setReturnValue(true);
		}
	}
}