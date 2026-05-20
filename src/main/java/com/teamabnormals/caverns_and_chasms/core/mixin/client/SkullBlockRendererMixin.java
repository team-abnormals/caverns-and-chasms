package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.PeeperHeadModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(SkullBlockRenderer.class)
public abstract class SkullBlockRendererMixin {

	@WrapOperation(method = "renderSkull", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 1))
	private static void renderSkull(PoseStack instance, float x, float y, float z, Operation<Void> original, @Nullable Direction direction, float yRot, float mouthAnimation, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkullModelBase model, RenderType renderType) {
		if (model instanceof PeeperHeadModel && direction != null) {
			float offset = 1.0F / 16.0F;
			original.call(instance, x + offset * direction.getStepX(), y - offset, z + offset * direction.getStepZ());
		} else {
			original.call(instance, x, y, z);
		}
	}
}
