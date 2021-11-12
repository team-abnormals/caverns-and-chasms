package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.MimeEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;

public class MimeModel<T extends MimeEntity> extends PlayerModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "mime"), "main");
	private final ModelPart bipedCape;

	public MimeModel(ModelPart root) {
		super(root, false);
		this.bipedCape = root.getChild("biped_cape");
	}

	//Layer Definition
	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, false).texOffs(8, 60).addBox(-2.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, false).texOffs(0, 59).addBox(-4.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, false).texOffs(18, 60).addBox(1.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, false).texOffs(10, 59).addBox(2.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 13.0F, 4.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 33).addBox(-3.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offsetAndRotation(-1.9F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offsetAndRotation(1.9F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition bipedCape = root.addOrReplaceChild("biped_cape", CubeListBuilder.create().texOffs(16, 33).addBox(-4.0F, -2.0F, 2.0F, 8.0F, 21.0F, 1.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.bipedCape, this.hat);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
		double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * partialTicks - (entity.xo + (entity.getX() - entity.xo) * partialTicks);
		double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * partialTicks - (entity.yo + (entity.getY() - entity.yo) * partialTicks);
		double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * partialTicks - (entity.zo + (entity.getZ() - entity.zo) * partialTicks);
		float f = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
		double d3 = Mth.sin(f * 0.017453292F);
		double d4 = (-Mth.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = Mth.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = Mth.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = Mth.clamp(f3, -20.0F, 20.0F);

		float f4 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
		f1 = f1 + Mth.sin((entity.walkDistO + (entity.walkDist - entity.walkDistO) * partialTicks) * 6.0F) * 32.0F * f4;
		if (entity.isShiftKeyDown())
			f1 += 25.0F;

		this.bipedCape.xRot = (float) Math.toRadians(6.0F + f2 / 2.0F + f1);
		this.bipedCape.yRot = (float) Math.toRadians(f3 / 2.0F);
		this.bipedCape.zRot = (float) Math.toRadians(f3 / 2.0F);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.crouching = entityIn.isCrouching();
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (entityIn.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
			if (entityIn.isCrouching()) {
				this.bipedCape.z = 1.4F;
				this.bipedCape.y = 1.85F;
			} else {
				this.bipedCape.z = 0.0F;
				this.bipedCape.y = 0.0F;
			}
		} else if (entityIn.isCrouching()) {
			this.bipedCape.z = 0.3F;
			this.bipedCape.y = 0.8F;
		} else {
			this.bipedCape.z = -1.1F;
			this.bipedCape.y = -0.85F;
		}
	}

	@Override
	public void setAllVisible(boolean visible) {
		super.setAllVisible(visible);
		this.bipedCape.visible = visible;
	}

	@Override
	public void translateToHand(HumanoidArm sideIn, PoseStack matrixStackIn) {
		ModelPart modelrenderer = this.getArm(sideIn);
		modelrenderer.y -= 2.0F;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.y += 2.0F;
	}
}