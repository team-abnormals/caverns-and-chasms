package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.MimeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class MimeModel<T extends MimeEntity> extends PlayerModel<T> {
	private final ModelRenderer bipedCape;

	public MimeModel(float modelSize) {
		super(modelSize, false);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(8, 60).addBox(-2.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(0, 59).addBox(-4.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(18, 60).addBox(1.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(10, 59).addBox(2.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4.0F, -2.0F, -2.0F, 8.0F, 13.0F, 4.0F, modelSize);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 40, 33);
		this.bipedRightArm.addBox(-3.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.addBox(-1.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize, true);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize);
		this.bipedRightLeg.setRotationPoint(-1.9F, 13.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 33);
		this.bipedLeftLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize, true);
		this.bipedLeftLeg.setRotationPoint(1.9F, 13.0F, 0.0F);

		this.bipedCape = new ModelRenderer(this, 16, 33);
		this.bipedCape.addBox(-4.0F, -2.0F, 2.0F, 8.0F, 21.0F, 1.0F, 0.0F, false);
		this.bipedCape.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg, this.bipedCape, this.bipedHeadwear);
	}

	@Override
	public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
		double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * partialTicks - (entity.prevPosX + (entity.getPosX() - entity.prevPosX) * partialTicks);
		double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * partialTicks - (entity.prevPosY + (entity.getPosY() - entity.prevPosY) * partialTicks);
		double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * partialTicks - (entity.prevPosZ + (entity.getPosZ() - entity.prevPosZ) * partialTicks);
		float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
		double d3 = MathHelper.sin(f * 0.017453292F);
		double d4 = (-MathHelper.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = MathHelper.clamp(f3, -20.0F, 20.0F);

		float f4 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
		f1 = f1 + MathHelper.sin((entity.prevDistanceWalkedModified + (entity.distanceWalkedModified - entity.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
		if (entity.isSneaking())
			f1 += 25.0F;

		this.bipedCape.rotateAngleX = (float) Math.toRadians(6.0F + f2 / 2.0F + f1);
		this.bipedCape.rotateAngleY = (float) Math.toRadians(f3 / 2.0F);
		this.bipedCape.rotateAngleZ = (float) Math.toRadians(f3 / 2.0F);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.isSneak = entityIn.isCrouching();
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
			if (entityIn.isCrouching()) {
				this.bipedCape.rotationPointZ = 1.4F;
				this.bipedCape.rotationPointY = 1.85F;
			} else {
				this.bipedCape.rotationPointZ = 0.0F;
				this.bipedCape.rotationPointY = 0.0F;
			}
		} else if (entityIn.isCrouching()) {
			this.bipedCape.rotationPointZ = 0.3F;
			this.bipedCape.rotationPointY = 0.8F;
		} else {
			this.bipedCape.rotationPointZ = -1.1F;
			this.bipedCape.rotationPointY = -0.85F;
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedCape.showModel = visible;
	}

	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
		ModelRenderer modelrenderer = this.getArmForSide(sideIn);
		modelrenderer.rotationPointY -= 2.0F;
		modelrenderer.translateRotate(matrixStackIn);
		modelrenderer.rotationPointY += 2.0F;
	}
}