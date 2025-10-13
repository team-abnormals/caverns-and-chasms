package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel.RatPose;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
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
	/*
	@Shadow
	@Final
	Minecraft minecraft;

	@Shadow
	private int tick;

	@Shadow @Final public ItemInHandRenderer itemInHandRenderer;
	@Shadow @Final private RenderBuffers renderBuffers;
	@Unique
	private RatModel ratModel;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ResourceManager resourceManager, RenderBuffers renderBuffers, CallbackInfo ci) {
		this.ratModel = new RatModel(minecraft.getEntityModels().bakeLayer(CCModelLayers.RAT));
		this.ratModel.pose = RatPose.ATTACHED;
	}

	@Inject(method = "pick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", shift = At.Shift.AFTER, ordinal = 8), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void pick(float p_109088_, CallbackInfo ci, Entity entity, double d0, double entityReach, Vec3 vec3, boolean flag, int i, double d1, Vec3 vec31, Vec3 vec32, float f, AABB aabb, EntityHitResult entityhitresult, Entity entity1) {
		if (entity1 instanceof GrazerPart)
			this.minecraft.crosshairPickEntity = entity1;
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemActivationAnimation(IIF)V", shift = At.Shift.AFTER))
	private void render(float partialTick, long p_109095_, boolean p_109096_, CallbackInfo ci) {
		List<AttachedRat> attachedrats = ((RatHolder) this.minecraft.player).getAttachedRats();
		if (!attachedrats.isEmpty()) {
			RenderSystem.enableDepthTest();
			RenderSystem.disableCull();
			PoseStack posestack = new PoseStack();
			posestack.pushPose();
			posestack.translate((float) (p_109101_ / 2) + f5 * Mth.abs(Mth.sin(f4 * 2.0F)), (float) (p_109102_ / 2) + f6 * Mth.abs(Mth.sin(f4 * 2.0F)), -50.0F);
			posestack.scale(1.0F, -1.0F, 1.0F);
			posestack.mulPose(Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(f4))));
			posestack.mulPose(Axis.XP.rotationDegrees(6.0F * Mth.cos(f * 8.0F)));
			posestack.mulPose(Axis.ZP.rotationDegrees(6.0F * Mth.cos(f * 8.0F)));
			float animtime = this.tick + partialTick + random.nextFloat() * 100.0F;
			MultiBufferSource.BufferSource buffer = this.renderBuffers.bufferSource();
			this.ratModel.renderFromTag(attachedrats.get(0).getEntityData(), this.minecraft.level, this.minecraft.player, this.itemInHandRenderer, posestack, buffer, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, partialTick), 0.0F, 0.0F, animtime, 0.0F, 0.0F);
			posestack.popPose();
			buffer.endBatch();
			RenderSystem.enableCull();
			RenderSystem.disableDepthTest();
		}
	}
	*/
}