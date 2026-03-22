package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatHeldItemLayer extends RenderLayer<Rat, RatModel> {
	private final ItemInHandRenderer itemInHandRenderer;

	public RatHeldItemLayer(RenderLayerParent<Rat, RatModel> entityRenderer, ItemInHandRenderer itemInHandRenderer) {
		super(entityRenderer);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Rat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		renderItem(rat, this.getParentModel(), this.itemInHandRenderer, poseStack, buffer, packedLight, rat, rat.isBaby(), rat.getItemBySlot(EquipmentSlot.MAINHAND), netHeadYaw, headPitch);
	}

	public static void renderItem(Rat rat, RatModel model, ItemInHandRenderer itemInHandRenderer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, boolean isBaby, ItemStack stack, float netHeadYaw, float headPitch) {
		poseStack.pushPose();
		if (isBaby) {
			poseStack.translate(0.0D, 0.3125D, 0.125D);
		}

		if (rat != null && rat.isEating()) {
			poseStack.translate(model.rightFrontLeg.x / 16.0F, model.rightFrontLeg.y / 16.0F, model.rightFrontLeg.z / 16.0F);
			poseStack.mulPose(Axis.YP.rotation(model.rightFrontLeg.yRot));
			poseStack.mulPose(Axis.XP.rotation(model.rightFrontLeg.xRot + Mth.PI));
			poseStack.translate(2.0F / 16.0F, -1.0D / 16.0F, -1.5D / 16.0F);
		} else {
			poseStack.translate(model.head.x / 16.0F, model.head.y / 16.0F, model.head.z / 16.0F);
			poseStack.mulPose(Axis.ZP.rotation(model.head.zRot));
			poseStack.mulPose(Axis.YP.rotation(model.head.yRot));
			poseStack.mulPose(Axis.XP.rotation(model.head.xRot));
			poseStack.translate(0.0F, 0.08D, -0.4D);
		}

		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight);
		poseStack.popPose();
	}
}