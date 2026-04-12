package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	Minecraft minecraft;
	@Shadow
	@Final
	private RenderBuffers renderBuffers;
	@Shadow
	@Final
	private LightTexture lightTexture;

	@ModifyArg(method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;"), index = 4)
	private Predicate<Entity> modifyEntityPickPredicate(Predicate<Entity> predicate) {
		return predicate.and(entity -> !(entity instanceof Rat rat && rat.getAttachedEntity() == this.minecraft.getCameraEntity()));
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.BEFORE))
	private void renderLevel(DeltaTracker deltaTracker, CallbackInfo ci) {
		Entity cameraentity = this.minecraft.getCameraEntity();
		if (cameraentity instanceof LivingEntity livingentity && this.minecraft.options.getCameraType().isFirstPerson()) {
			float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
			PoseStack posestack = new PoseStack();
			List<Rat> rats = ((RatHolder) livingentity).getAttachedRats();
			if (!rats.isEmpty()) {
				RenderSystem.clear(256, Minecraft.ON_OSX);
				MultiBufferSource.BufferSource buffer = this.renderBuffers.bufferSource();
				int packedLight = this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(livingentity, partialTick);
				this.lightTexture.turnOnLightLayer();

				for (Rat rat : rats) {
					posestack.setIdentity();
					posestack.pushPose();
					double offset = rat.getFirstPersonPos() * 0.8D;
					posestack.translate(offset, -0.85D, -1.4D);
					posestack.mulPose(Axis.YP.rotation((float) -Math.atan(offset / 1.4D)));
					posestack.rotateAround(Axis.XP.rotationDegrees(30.0F), 0.0F, 0.0F, 0.0F);

					EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

					float f = rat.yBodyRotO;
					float f1 = rat.yBodyRot;
					float f2 = rat.yRotO;
					float f3 = rat.getYRot();
					float f4 = rat.xRotO;
					float f5 = rat.getXRot();
					rat.yBodyRotO = 0.0F;
					rat.yBodyRot = 0.0F;
					rat.yRotO = 0.0F;
					rat.setYRot(0.0F);
					rat.xRotO = 0.0F;
					rat.setXRot(0.0F);
					entityrenderdispatcher.setRenderShadow(false);
					RenderSystem.runAsFancy(() -> {
						entityrenderdispatcher.render(rat, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, posestack, buffer, packedLight);
					});
					entityrenderdispatcher.setRenderShadow(true);
					rat.yBodyRotO = f;
					rat.yBodyRot = f1;
					rat.yRotO = f2;
					rat.setYRot(f3);
					rat.xRotO = f4;
					rat.setXRot(f5);

					posestack.popPose();
				}

				this.lightTexture.turnOffLightLayer();
				buffer.endBatch();
			}
		}
	}
}