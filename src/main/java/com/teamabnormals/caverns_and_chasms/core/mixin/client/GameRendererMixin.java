package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel.RatPose;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

	@Shadow @Final public ItemInHandRenderer itemInHandRenderer;

	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Final private LightTexture lightTexture;

	@Unique
	private RatModel ratModel;

	@Inject(method = "pick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", shift = At.Shift.AFTER, ordinal = 8), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void pick(float p_109088_, CallbackInfo ci, Entity entity, double d0, double entityReach, Vec3 vec3, boolean flag, int i, double d1, Vec3 vec31, Vec3 vec32, float f, AABB aabb, EntityHitResult entityhitresult, Entity entity1) {
		if (entity1 instanceof GrazerPart)
			this.minecraft.crosshairPickEntity = entity1;
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.BEFORE))
	private void renderLevel(float partialTick, long p_109091_, PoseStack posestack, CallbackInfo ci) {
		// TODO: Conditions for when the rats are rendered
		Entity cameraentity = this.minecraft.getCameraEntity();
		if (cameraentity instanceof LivingEntity livingentity && this.minecraft.options.getCameraType().isFirstPerson()) {
			List<AttachedRat> attachedrats = ((RatHolder) livingentity).getAttachedRats();
			if (!attachedrats.isEmpty()) {
				if (this.ratModel == null) {
					this.ratModel = new RatModel(minecraft.getEntityModels().bakeLayer(CCModelLayers.RAT));
					this.ratModel.pose = RatPose.ATTACHED;
				}

				// this.resetProjectionMatrix(this.getProjectionMatrix(this.getFov(this.mainCamera, partialTick, false)));
				RenderSystem.clear(256, Minecraft.ON_OSX);
				MultiBufferSource.BufferSource buffer = this.renderBuffers.bufferSource();
				this.lightTexture.turnOnLightLayer();

				for (AttachedRat attachedrat : attachedrats) {
					float animtime = this.tick + partialTick + attachedrat.getAnimOffset();
					posestack.setIdentity();
					posestack.pushPose();
					double offset = attachedrat.getFirstPersonPos() * 0.8D;
					posestack.translate(offset, 0.65D, -1.2D);
					posestack.scale(-1.0F, -1.0F, 1.0F);
					posestack.mulPose(Axis.YP.rotation(Mth.PI + (float) Math.atan(offset / 1.2D)));
					posestack.rotateAround(Axis.XP.rotationDegrees(-60.0F), 0.0F, 1.5F, 0.0F);
					posestack.mulPose(Axis.YP.rotationDegrees(Mth.sin(animtime * 0.75F) * 10.0F));
					this.ratModel.renderFromTag(attachedrat.getEntityData(), this.minecraft.level, livingentity, this.itemInHandRenderer, posestack, buffer, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(livingentity, partialTick), 0.0F, 0.0F, animtime, Mth.sin(animtime * 0.75F) * 15.0F, 35.0F);
					posestack.popPose();
				}

				this.lightTexture.turnOffLightLayer();
				buffer.endBatch();
			}
		}
	}
}