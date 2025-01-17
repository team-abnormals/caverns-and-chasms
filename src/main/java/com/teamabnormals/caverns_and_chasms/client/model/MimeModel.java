package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import org.joml.Vector3f;

public class MimeModel extends PlayerModel<Mime> {
	private final ModelPart rightHorn;
	private final ModelPart leftHorn;
	private final ModelPart bipedCape;

	public MimeModel(ModelPart root) {
		super(root, false);
		this.rightHorn = this.head.getChild("right_horn");
		this.leftHorn = this.head.getChild("left_horn");
		this.bipedCape = root.getChild("biped_cape");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offset(0.0F, -2.0F, 0.0F));
		head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(8, 60).addBox(-2.0F, -14.0F, 0.0F, 1.0F, 4.0F, 0.0F, false).texOffs(0, 59).addBox(-4.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(18, 60).addBox(1.0F, -14.0F, 0.0F, 1.0F, 4.0F, 0.0F, false).texOffs(10, 59).addBox(2.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F, false), PartPose.offset(0.0F, 2.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 33).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offset(-5.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, true), PartPose.offset(5.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, false), PartPose.offset(-1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, true), PartPose.offset(1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("biped_cape", CubeListBuilder.create().texOffs(16, 33).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 21.0F, 1.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.bipedCape, this.hat);
	}

	@Override
	public void prepareMobModel(Mime mime, float limbSwing, float limbSwingAmount, float partialTicks) {
		super.prepareMobModel(mime, limbSwing, limbSwingAmount, partialTicks);
		double d0 = mime.prevChasingPosX + (mime.chasingPosX - mime.prevChasingPosX) * partialTicks - (mime.xo + (mime.getX() - mime.xo) * partialTicks);
		double d1 = mime.prevChasingPosY + (mime.chasingPosY - mime.prevChasingPosY) * partialTicks - (mime.yo + (mime.getY() - mime.yo) * partialTicks);
		double d2 = mime.prevChasingPosZ + (mime.chasingPosZ - mime.prevChasingPosZ) * partialTicks - (mime.zo + (mime.getZ() - mime.zo) * partialTicks);
		float f = mime.yBodyRotO + (mime.yBodyRot - mime.yBodyRotO) * partialTicks;
		double d3 = Mth.sin(f * 0.017453292F);
		double d4 = (-Mth.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = Mth.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = Mth.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = Mth.clamp(f3, -20.0F, 20.0F);

		float f4 = mime.prevCameraYaw + (mime.cameraYaw - mime.prevCameraYaw) * partialTicks;
		f1 = f1 + Mth.sin((mime.walkDistO + (mime.walkDist - mime.walkDistO) * partialTicks) * 6.0F) * 32.0F * f4;
		if (mime.isShiftKeyDown())
			f1 += 25.0F;

		boolean showhorns = mime.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
		this.rightHorn.visible = showhorns;
		this.leftHorn.visible = showhorns;

		this.bipedCape.visible = !mime.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);

		this.bipedCape.xRot = (float) Math.toRadians(6.0F + f2 / 2.0F + f1);
		this.bipedCape.yRot = (float) Math.toRadians(f3 / 2.0F);
		this.bipedCape.zRot = (float) Math.toRadians(f3 / 2.0F);
	}

	@Override
	public void setupAnim(Mime mime, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.crouching = mime.isCrouching();
		float f = limbSwing * 0.75F;

		super.setupAnim(mime, f, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if (mime.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
			if (mime.isCrouching()) {
				this.bipedCape.z = -1.4F;
				this.bipedCape.y = 1.85F;
			} else {
				this.bipedCape.z = 0.0F;
				this.bipedCape.y = 0.0F;
			}
		} else if (mime.isCrouching()) {
			this.bipedCape.z = -0.3F;
			this.bipedCape.y = 1.85F;
		} else {
			this.bipedCape.z = 1.1F;
			this.bipedCape.y = 0.0F;
		}

		if (!this.riding && this.swimAmount <= 0.0F) {
			float f1 = Mth.cos(f * 0.6662F) * limbSwingAmount * 0.2F;

			this.body.zRot = f1;
			this.head.zRot = Mth.cos(f * 0.6662F - Mth.PI * 0.5F) * limbSwingAmount * 0.2F;
			this.head.x = Mth.sin(f1) * 11.0F;
			this.head.y += 11.0F - Mth.cos(f1) * 11.0F;
			this.body.x = Mth.sin(f1) * 11.0F;
			this.body.y += 11.0F - Mth.cos(f1) * 11.0F;
			this.rightArm.x += Mth.sin(f1) * 11.0F;
			this.rightArm.y += 11.0F - Mth.cos(f1) * 11.0F;
			this.leftArm.x += Mth.sin(f1) * 11.0F;
			this.leftArm.y += 11.0F - Mth.cos(f1) * 11.0F;
			this.bipedCape.x = Mth.sin(f1) * 11.0F;
			this.bipedCape.y += 11.0F - Mth.cos(f1) * 11.0F;

			if (this.rightLeg.xRot < 0.0F) {
				this.rightLeg.yRot += (1.0F + Mth.cos(f * 0.6662F * 2F)) * limbSwingAmount * 0.4F;
				this.rightLeg.zRot += this.rightLeg.yRot * 0.25F;
			}
			if (this.leftLeg.xRot < 0.0F) {
				this.leftLeg.yRot += (-1.0F - Mth.cos(f * 0.6662F * 2F)) * limbSwingAmount * 0.4F;
				this.leftLeg.zRot += this.rightLeg.yRot * 0.25F;
			}
		} else {
			this.head.zRot = 0.0F;
			this.body.zRot = 0.0F;
			this.head.x = 0.0F;
			this.body.x = 0.0F;
			this.bipedCape.x = 0.0F;
		}

		this.head.y -= 2.0F;
		this.body.y -= 2.0F;
		this.rightArm.y -= 2.0F;
		this.leftArm.y -= 2.0F;
		this.rightLeg.y -= 1.0F;
		this.leftLeg.y -= 1.0F;
		this.bipedCape.y -= 2.0F;
		this.bipedCape.z += 2.0F;

		this.saveAnimationValues(mime);
	}

	private void saveAnimationValues(Mime mime) {
		mime.armPositions[0] = this.getPositionVector(this.rightArm);
		mime.armPositions[1] = this.getPositionVector(this.leftArm);
		mime.armRotations[0] = this.getRotationVector(this.rightArm);
		mime.armRotations[1] = this.getRotationVector(this.leftArm);
	}

	private Vector3f getRotationVector(ModelPart modelPart) {
		return new Vector3f(modelPart.xRot, modelPart.yRot, modelPart.zRot);
	}

	private Vector3f getPositionVector(ModelPart modelPart) {
		return new Vector3f(modelPart.x, modelPart.y, modelPart.z);
	}

	@Override
	public void setAllVisible(boolean visible) {
		super.setAllVisible(visible);
		this.bipedCape.visible = visible;
	}
}