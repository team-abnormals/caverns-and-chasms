package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class RatHeldItemLayer extends RenderLayer<Rat, RatModel<Rat>> {
	public RatHeldItemLayer(RenderLayerParent<Rat, RatModel<Rat>> entityRenderer) {
		super(entityRenderer);
	}

	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Rat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		matrixStackIn.pushPose();

		matrixStackIn.translate((this.getParentModel()).head.x / 16.0F, (this.getParentModel()).head.y / 16.0F, ((this.getParentModel()).head.z / 16.0F));
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(headPitch));
		matrixStackIn.translate(0.0F, rat.isBaby() ? 0.4F : 0.08F, rat.isBaby() ? -0.7D : -0.5D);

		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		ItemStack stack = rat.getItemBySlot(EquipmentSlot.MAINHAND);
		Minecraft.getInstance().getItemInHandRenderer().renderItem(rat, stack, ItemTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.popPose();
	}
}