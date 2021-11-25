package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.common.entity.Fly;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * FlyModel - MCVinnyq
 * Created using Tabula 7.1.0
 */

public class FlyModel<T extends Fly> extends AgeableListModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "fly"), "main");
	public ModelPart body;
	public ModelPart leftAntenna;
	public ModelPart rightAntenna;
	public ModelPart rightWing;
	public ModelPart leftWing;
	public ModelPart Mouth;
	public ModelPart frontLegs;
	public ModelPart middleLegs;
	public ModelPart backLegs;

	public FlyModel(ModelPart root) {
		this.body = root.getChild("body");
		this.leftAntenna = this.body.getChild("leftAntenna");
		this.backLegs = this.body.getChild("backLegs");
		this.frontLegs = this.body.getChild("frontLegs");
		this.rightWing = this.body.getChild("rightWing");
		this.rightAntenna = this.body.getChild("rightAntenna");
		this.leftWing = this.body.getChild("leftWing");
		this.Mouth = this.body.getChild("Mouth");
		this.middleLegs = this.body.getChild("middleLegs");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftAntenna = body.addOrReplaceChild("leftAntenna", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(0.5F, -2.5F, -3.0F, 0.0F, 0.0F, 0.21816616F));
		PartDefinition backLegs = body.addOrReplaceChild("backLegs", CubeListBuilder.create().texOffs(16, 4).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 2.5F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition frontLegs = body.addOrReplaceChild("frontLegs", CubeListBuilder.create().texOffs(16, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 2.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, false), PartPose.offsetAndRotation(-2.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightAntenna = body.addOrReplaceChild("rightAntenna", CubeListBuilder.create().texOffs(0, 3).addBox(-3.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(-0.5F, -2.5F, -3.0F, 0.0F, 0.0F, -0.21816616F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, false), PartPose.offsetAndRotation(2.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Mouth = body.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(12, 11).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 0.5F, -3.0F, 0.20943952F, 0.0F, 0.0F));
		PartDefinition middleLegs = body.addOrReplaceChild("middleLegs", CubeListBuilder.create().texOffs(16, 2).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.body.render(matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setupAnim(T fly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.rightWing.xRot = 0.0f;
		this.leftAntenna.xRot = 0.0f;
		this.rightAntenna.xRot = 0.0f;
		if (fly.isOnGround()) {
			this.rightWing.yRot = -0.2618f;
			this.rightWing.zRot = 0.0f;
			this.leftWing.xRot = 0.0f;
			this.leftWing.yRot = 0.2618f;
			this.leftWing.zRot = 0.0f;
			this.frontLegs.xRot = 0.0f;
			this.middleLegs.xRot = 0.0f;
			this.backLegs.xRot = 0.0f;
		} else {
			this.rightWing.yRot = 0.0f;
			this.rightWing.zRot = Mth.cos(ageInTicks * 2.1f) * 3.1415927f * 0.15f;
			this.leftWing.xRot = this.rightWing.xRot;
			this.leftWing.yRot = this.rightWing.yRot;
			this.leftWing.zRot = -this.rightWing.zRot;
			this.frontLegs.xRot = 0.7853982f;
			this.middleLegs.xRot = 0.7853982f;
			this.backLegs.xRot = 0.7853982f;
		}
	}
}
