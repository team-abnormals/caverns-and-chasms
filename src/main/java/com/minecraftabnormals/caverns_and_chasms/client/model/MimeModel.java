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

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(8, 60).addBox(-2.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
		this.head.texOffs(0, 59).addBox(-4.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		this.head.texOffs(18, 60).addBox(1.0F, -16.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
		this.head.texOffs(10, 59).addBox(2.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize + 0.5F);
		this.hat.setPos(0.0F, 0.0F, 0.0F);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, -2.0F, -2.0F, 8.0F, 13.0F, 4.0F, modelSize);
		this.body.setPos(0.0F, 0.0F, 0.0F);

		this.rightArm = new ModelRenderer(this, 40, 33);
		this.rightArm.addBox(-3.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);

		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.addBox(-1.0F, -4.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize, true);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);

		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize);
		this.rightLeg.setPos(-1.9F, 13.0F, 0.0F);

		this.leftLeg = new ModelRenderer(this, 0, 33);
		this.leftLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, modelSize, true);
		this.leftLeg.setPos(1.9F, 13.0F, 0.0F);

		this.bipedCape = new ModelRenderer(this, 16, 33);
		this.bipedCape.addBox(-4.0F, -2.0F, 2.0F, 8.0F, 21.0F, 1.0F, 0.0F, false);
		this.bipedCape.setPos(0.0F, 0.0F, 0.0F);
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.bipedCape, this.hat);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
		double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * partialTicks - (entity.xo + (entity.getX() - entity.xo) * partialTicks);
		double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * partialTicks - (entity.yo + (entity.getY() - entity.yo) * partialTicks);
		double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * partialTicks - (entity.zo + (entity.getZ() - entity.zo) * partialTicks);
		float f = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
		double d3 = MathHelper.sin(f * 0.017453292F);
		double d4 = (-MathHelper.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = MathHelper.clamp(f3, -20.0F, 20.0F);

		float f4 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
		f1 = f1 + MathHelper.sin((entity.walkDistO + (entity.walkDist - entity.walkDistO) * partialTicks) * 6.0F) * 32.0F * f4;
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
		if (entityIn.getItemBySlot(EquipmentSlotType.CHEST).isEmpty()) {
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
	public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
		ModelRenderer modelrenderer = this.getArm(sideIn);
		modelrenderer.y -= 2.0F;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.y += 2.0F;
	}
}