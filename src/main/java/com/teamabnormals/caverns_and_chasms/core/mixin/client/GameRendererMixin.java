package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	Minecraft minecraft;
	@Shadow
	private int tick;
	@Shadow
	@Final
	private RenderBuffers renderBuffers;
	@Shadow
	@Final
	private LightTexture lightTexture;

	@Inject(method = "pick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", shift = At.Shift.AFTER, ordinal = 8), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void pick(float p_109088_, CallbackInfo ci, Entity entity, double d0, double entityReach, Vec3 vec3, boolean flag, int i, double d1, Vec3 vec31, Vec3 vec32, float f, AABB aabb, EntityHitResult entityhitresult, Entity entity1) {
		if (entity1 instanceof GrazerPart)
			this.minecraft.crosshairPickEntity = entity1;
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.BEFORE))
	private void renderLevel(float partialTick, long p_109091_, PoseStack posestack, CallbackInfo ci) {
		Entity cameraentity = this.minecraft.getCameraEntity();
		if (cameraentity instanceof LivingEntity livingentity && this.minecraft.options.getCameraType().isFirstPerson()) {
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