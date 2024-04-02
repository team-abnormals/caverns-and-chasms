package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatHeldItemLayer extends RenderLayer<Rat, RatModel<Rat>> {
	private final ItemInHandRenderer itemInHandRenderer;

	public RatHeldItemLayer(RenderLayerParent<Rat, RatModel<Rat>> entityRenderer, ItemInHandRenderer itemInHandRenderer) {
		super(entityRenderer);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Rat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		renderItem(this.getParentModel(), this.itemInHandRenderer, matrixStackIn, bufferIn, packedLightIn, rat, rat.isBaby(), rat.getItemBySlot(EquipmentSlot.MAINHAND), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
	}

	public static void renderItem(RatModel<Rat> model, ItemInHandRenderer itemInHandRenderer, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entity, boolean isBaby, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		poseStack.pushPose();
		if (isBaby) {
			poseStack.translate(0.0D, 0.3125D, 0.125D);
		}

		poseStack.translate(model.head.x / 16.0F, model.head.y / 16.0F, model.head.z / 16.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
		poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
		poseStack.translate(0.0F, 0.08D, -0.4D);

		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.GROUND, false, poseStack, bufferIn, packedLightIn);
		poseStack.popPose();
	}
}