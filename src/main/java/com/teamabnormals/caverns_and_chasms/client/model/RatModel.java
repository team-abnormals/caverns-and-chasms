package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RatModel<T extends Rat> extends AgeableListModel<T> {
	public ModelPart head;
	public ModelPart body;
	public ModelPart tail;
	public ModelPart leftFrontLeg;
	public ModelPart leftHindLeg;
	public ModelPart rightFrontLeg;
	public ModelPart rightHindLeg;

	public RatModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.tail = root.getChild("tail");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("whisker", CubeListBuilder.create().texOffs(12, 14).addBox(-3.5F, -2.0F, -4.0F, 7.0F, 3.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("tooth", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 1.0F, -5.0F, 1.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 5.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 23.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 23.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 5).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 21.0F, 5.0F, 0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg, this.tail);
	}

	public void renderOnShoulder(PoseStack matrixStackIn, VertexConsumer vertexconsumer, int packedLightIn, int overlayTexture, float tailWagAmount, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.setupAnim(RatModel.State.ON_SHOULDER, tailWagAmount, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, overlayTexture, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(T rat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.setupAnim(rat.isInSittingPose() ? RatModel.State.SITTING : RatModel.State.STANDING, rat.getTailWagAmount(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	public void setupAnim(RatModel.State state, float tailWagAmount, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		switch (state) {
			case STANDING:
				this.head.setPos(0.0F, !this.young ? 21.0F : 17.5F, -3.0F);
				this.body.setPos(0.0F, 21.0F, -3.0F);
				this.body.xRot = 0.0F;
				this.rightHindLeg.setPos(-2.0F, 23.0F, 4.0F);
				this.leftHindLeg.setPos(2.0F, 23.0F, 4.0F);
				this.rightFrontLeg.setPos(-2.0F, 23.0F, -1.0F);
				this.leftFrontLeg.setPos(2.0F, 23.0F, -1.0F);
				this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
				this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
				this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
				this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
				this.tail.setPos(0.0F, 21.0F, 5.0F);
				this.tail.xRot = 0.0F;
				break;
			case SITTING:
				this.head.setPos(0.0F, !this.young ? 15.0F : 14.0F, !this.young ? 0.5F : -1.5F);
				this.body.setPos(0.0F, 16.0F, -0.5F);
				this.body.xRot = -Mth.PI / 2.0F;
				this.rightHindLeg.setPos(-2.0F, 24.0F, -3.0F);
				this.leftHindLeg.setPos(2.0F, 24.0F, -3.0F);
				this.rightFrontLeg.setPos(-2.0F, 19.0F, -2.5F);
				this.leftFrontLeg.setPos(2.0F, 19.0F, -2.5F);
				this.rightHindLeg.xRot = -Mth.PI / 2.0F;
				this.leftHindLeg.xRot = -Mth.PI / 2.0F;
				this.rightFrontLeg.xRot = -Mth.PI / 4.0F;
				this.leftFrontLeg.xRot = -Mth.PI / 4.0F;
				this.tail.setPos(0.0F, 23.0F, 2.5F);
				this.tail.xRot = 0.0F;
				break;
			case ON_SHOULDER:
				this.head.setPos(0.0F, !this.young ? 21.0F : 17.5F, -3.0F);
				this.body.setPos(0.0F, 21.0F, -3.0F);
				this.body.xRot = 0.0F;
				this.rightHindLeg.setPos(-1.0F, 23.0F, 3.0F);
				this.leftHindLeg.setPos(1.0F, 23.0F, 3.0F);
				this.rightFrontLeg.setPos(-1.0F, 23.0F, 0.0F);
				this.leftFrontLeg.setPos(1.0F, 23.0F, 0.0F);
				this.rightHindLeg.xRot = 0.0F;
				this.leftHindLeg.xRot = 0.0F;
				this.rightFrontLeg.xRot = 0.0F;
				this.leftFrontLeg.xRot = 0.0F;
				this.tail.setPos(0.0F, 21.0F, 5.0F);
				this.tail.xRot = -1.0F;
				break;
		}

		this.head.xRot = headPitch * (Mth.PI / 180F);
		this.head.yRot = netHeadYaw * (Mth.PI / 180F);
		this.tail.yRot = -tailWagAmount * 0.45F * Mth.sin(0.6F * ageInTicks);
	}

	@OnlyIn(Dist.CLIENT)
	public static enum State {
		STANDING,
		SITTING,
		ON_SHOULDER;
	}
}