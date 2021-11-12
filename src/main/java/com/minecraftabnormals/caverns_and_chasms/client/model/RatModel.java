package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
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

public class RatModel<T extends RatEntity> extends AgeableListModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "rat"), "main");
	public ModelPart body;
	public ModelPart tail;
	public ModelPart head;
	public ModelPart earRight;
	public ModelPart earLeft;
	public ModelPart whisker;
	public ModelPart tooth;
	public ModelPart legFrontLeft;
	public ModelPart legBackLeft;
	public ModelPart legFrontRight;
	public ModelPart legBackRight;

	//Constructor
	public RatModel(ModelPart root) {
		this.body = root.getChild("body");
		this.tail = this.body.getChild("tail");
		this.head = root.getChild("head");
		this.earRight = this.head.getChild("earRight");
		this.earLeft = this.head.getChild("earLeft");
		this.whisker = this.head.getChild("whisker");
		this.tooth = this.head.getChild("tooth");
		this.legFrontLeft = root.getChild("legFrontLeft");
		this.legBackLeft = root.getChild("legBackLeft");
		this.legFrontRight = root.getChild("legFrontRight");
		this.legBackRight = root.getChild("legBackRight");
	}
	//Layer Definition
	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 5.0F, 8.0F, false), PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 5).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 8.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 3.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(0, 6).addBox(1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition whisker = head.addOrReplaceChild("whisker", CubeListBuilder.create().texOffs(12, 14).addBox(-3.5F, -2.0F, -4.0F, 7.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tooth = head.addOrReplaceChild("tooth", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 1.0F, -5.0F, 1.0F, 1.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition legFrontLeft = root.addOrReplaceChild("legFrontLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(-2.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition legBackLeft = root.addOrReplaceChild("legBackLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(2.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition legFrontRight = root.addOrReplaceChild("legFrontRight", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(-2.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition legBackRight = root.addOrReplaceChild("legBackRight", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(2.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body, legFrontRight, legFrontLeft, legBackRight, legBackLeft);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		float f = entity.isInWater() ? 1.0F : 1.5F;
		this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
		this.legBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.legBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.legFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.legFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.head.y = !this.young ? 21.0F : 17.5F;
	}
}