package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "pick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", shift = At.Shift.AFTER, ordinal = 8), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void pick(float p_109088_, CallbackInfo ci, Entity entity, double d0, double entityReach, Vec3 vec3, boolean flag, int i, double d1, Vec3 vec31, Vec3 vec32, float f, AABB aabb, EntityHitResult entityhitresult, Entity entity1) {
		if (entity1 instanceof GrazerPart)
			this.minecraft.crosshairPickEntity = entity1;
	}
}