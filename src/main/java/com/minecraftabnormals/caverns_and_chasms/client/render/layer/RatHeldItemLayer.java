package com.minecraftabnormals.caverns_and_chasms.client.render.layer;

import com.minecraftabnormals.caverns_and_chasms.client.model.RatModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RatHeldItemLayer extends LayerRenderer<RatEntity, RatModel<RatEntity>> {
	public RatHeldItemLayer(IEntityRenderer<RatEntity, RatModel<RatEntity>> entityRenderer) {
		super(entityRenderer);
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, RatEntity ratEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		matrixStackIn.pushPose();

		matrixStackIn.translate((this.getParentModel()).head.x / 16.0F, (this.getParentModel()).head.y / 16.0F, ((this.getParentModel()).head.z / 16.0F));
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(headPitch));
		matrixStackIn.translate(0.0F, ratEntity.isBaby() ? 0.4F : 0.08F, ratEntity.isBaby() ? -0.7D : -0.5D);

		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		ItemStack stack = ratEntity.getItemBySlot(EquipmentSlotType.MAINHAND);
		Minecraft.getInstance().getItemInHandRenderer().renderItem(ratEntity, stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.popPose();
	}
}