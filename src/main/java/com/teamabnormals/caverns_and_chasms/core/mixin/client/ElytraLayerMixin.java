package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity> {
	@Shadow
	@Final
	private ElytraModel<T> elytraModel;
	@Unique
	private static final ResourceLocation MIME_WINGS_LOCATION = CavernsAndChasms.location("textures/entity/mime_elytra.png");

	@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
	public RenderType getElytraTexture(ResourceLocation texture, Operation<RenderType> original, PoseStack poseStack, MultiBufferSource bufferSource, int i, T entity) {
		if (entity.getType() == CCEntityTypes.MIME.get() || entity.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MIME_HEAD.get())) {
			return RenderType.armorCutoutNoCull(MIME_WINGS_LOCATION);
		} else {
			return original.call(texture);
		}
	}

	@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
	private void getCloakTextureLocation(ElytraModel model, PoseStack poseStack, VertexConsumer vertexConsumer, int i1, int i2, float v, float v2, float v3, float v4, Operation<Void> original, PoseStack p_116951_, MultiBufferSource p_116952_, int p_116953_, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.is(CCItems.COWL.get())) {
			int i = ((DyeableLeatherItem) stack.getItem()).getColor(stack);
			float f = (float) (i >> 16 & 255) / 255.0F;
			float f1 = (float) (i >> 8 & 255) / 255.0F;
			float f2 = (float) (i & 255) / 255.0F;
			this.elytraModel.renderToBuffer(poseStack, vertexConsumer, p_116953_, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
		} else {
			original.call(model, poseStack, vertexConsumer, i1, i2, v, v2, v3, v4);
		}
	}
}