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
		matrixStackIn.push();
		if (ratEntity.isChild()) {
			matrixStackIn.scale(0.75F, 0.75F, 0.75F);
			matrixStackIn.translate(0.0D, 0.5D, 0.209375F);
		}

		matrixStackIn.translate((this.getEntityModel()).head.rotationPointX / 16.0F, (this.getEntityModel()).head.rotationPointY / 16.0F, ((this.getEntityModel()).head.rotationPointZ / 16.0F));
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(headPitch));
		if (ratEntity.isChild()) {
			matrixStackIn.translate(0.0F, 0.08F, -0.5D);
		} else {
			matrixStackIn.translate(0.0F, 0.08F, -0.5D);
		}

		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
		ItemStack stack = ratEntity.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(ratEntity, stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pop();
	}
}