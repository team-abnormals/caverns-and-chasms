package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel.RatPose;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AttachedRatsLayer<T extends LivingEntity & RatHolder, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final RatModel ratModel;
	private final ItemInHandRenderer itemInHandRenderer;

	public AttachedRatsLayer(RenderLayerParent<T, M> parent, EntityModelSet entityModelSet, ItemInHandRenderer itemInHandRenderer) {
		super(parent);
		this.ratModel = new RatModel(entityModelSet.bakeLayer(CCModelLayers.RAT));
		this.ratModel.pose = RatPose.ATTACHED;
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		for (AttachedRat attachedrat : entity.getAttachedRats()) {
			poseStack.pushPose();
			float animtime = ageInTicks + attachedrat.getAnimOffset();
			float angle = attachedrat.getAngle();
			poseStack.translate(-Math.sin(angle * Mth.DEG_TO_RAD) * entity.getBbWidth() * 0.45D, entity.getBbHeight() * -attachedrat.getPosY(), -Math.cos(angle * Mth.DEG_TO_RAD) * entity.getBbWidth() * 0.45D);
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + angle));
			poseStack.rotateAround(Axis.XP.rotationDegrees(-90.0F), 0.0F, 1.5F, 0.0F);
			poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(animtime * 0.75F) * 10.0F));
			this.ratModel.renderFromTag(attachedrat.getEntityData(), entity.level(), entity, this.itemInHandRenderer, poseStack, buffer, packedLight, 0.0F, 0.0F, animtime, Mth.sin(ageInTicks * 0.75F) * 15.0F, 35.0F);
			poseStack.popPose();
		}
	}
}