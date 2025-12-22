package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public final class ClientLevelMixin {
	@Shadow
	@Final
	EntityTickList tickingEntities;

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"), method = "*(Lnet/minecraft/world/entity/Entity;)V")
	private boolean shouldNotTick(Entity entity, Operation<Boolean> original) {
		return original.call(entity) || entity instanceof Rat rat && rat.isAttachedToEntity();
	}

	@Inject(at = @At(value = "RETURN"), method = "tickNonPassenger")
	private void updateRats(Entity entity, CallbackInfo info) {
		if (entity instanceof RatHolder ratholder)
			ratholder.tickRats(this.tickingEntities);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V", shift = At.Shift.AFTER), method = "tickPassenger")
	private void updatePassengerRats(Entity ridingEntity, Entity passenger, CallbackInfo info) {
		if (passenger instanceof RatHolder ratholder)
			ratholder.tickRats(this.tickingEntities);
	}
}