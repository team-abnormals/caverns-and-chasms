package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.MimeModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MimeItemInHandLayer extends RenderLayer<Mime, MimeModel> {
	private final ItemInHandRenderer itemInHandRenderer;

	public MimeItemInHandLayer(RenderLayerParent<Mime, MimeModel> parent, ItemInHandRenderer itemRenderer) {
		super(parent);
		this.itemInHandRenderer = itemRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Mime mime, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean flag = mime.getMainArm() == HumanoidArm.RIGHT;
		ItemStack itemstack = flag ? mime.getOffhandItem() : mime.getMainHandItem();
		ItemStack itemstack1 = flag ? mime.getMainHandItem() : mime.getOffhandItem();
		if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
			poseStack.pushPose();
			if (this.getParentModel().young) {
				poseStack.translate(0.0D, 0.75D, 0.0D);
				poseStack.scale(0.5F, 0.5F, 0.5F);
			}

			this.renderArmWithItem(mime, itemstack1, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);
			this.renderArmWithItem(mime, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, buffer, packedLight);
			poseStack.popPose();
		}
	}

	protected void renderArmWithItem(Mime mime, ItemStack stack, ItemDisplayContext transformType, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			this.getParentModel().translateToHand(arm, poseStack);
			poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			boolean flag = arm == HumanoidArm.LEFT;
			poseStack.translate(((float) (flag ? -1 : 1) / 16.0F), 0.125D, -0.6875D);
			this.itemInHandRenderer.renderItem(mime, stack, transformType, flag, poseStack, buffer, packedLight);
			poseStack.popPose();
		}
	}
}