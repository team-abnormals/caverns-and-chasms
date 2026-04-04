package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitResult.class)
public abstract class EntityHitResultMixin {
	@Mutable
	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "<init>(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V", at = @At("TAIL"))
	private void init(Entity entity, Vec3 vec3, CallbackInfo ci) {
		if (entity instanceof GrazerPart grazerpart && !grazerpart.deflectsAttacks())
			this.entity = grazerpart.getParent();
	}
}