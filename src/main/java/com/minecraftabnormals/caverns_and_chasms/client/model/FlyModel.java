package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * FlyModel - MCVinnyq
 * Created using Tabula 7.1.0
 */

public class FlyModel<T extends FlyEntity> extends AgeableModel<T> {
	public ModelRenderer body;
	public ModelRenderer leftAntenna;
	public ModelRenderer rightAntenna;
	public ModelRenderer rightWing;
	public ModelRenderer leftWing;
	public ModelRenderer Mouth;
	public ModelRenderer frontLegs;
	public ModelRenderer middleLegs;
	public ModelRenderer backLegs;

	public FlyModel() {
		this.texWidth = 32;
		this.texHeight = 32;
		this.leftAntenna = new ModelRenderer(this, 0, 0);
		this.leftAntenna.setPos(0.5F, -2.5F, -3.0F);
		this.leftAntenna.addBox(0.0F, -3.0F, 0.0F, 3, 3, 0, 0.0F);
		this.setRotateAngle(leftAntenna, 0.0F, 0.0F, 0.2181661564992912F);
		this.backLegs = new ModelRenderer(this, 16, 4);
		this.backLegs.setPos(0.0F, 2.5F, 2.0F);
		this.backLegs.addBox(-2.5F, 0.0F, 0.0F, 5, 2, 0, 0.0F);
		this.frontLegs = new ModelRenderer(this, 16, 0);
		this.frontLegs.setPos(0.0F, 2.5F, -2.0F);
		this.frontLegs.addBox(-2.5F, 0.0F, 0.0F, 5, 2, 0, 0.0F);
		this.body = new ModelRenderer(this, 0, 0);
		this.body.setPos(0.0F, 20.0F, 0.0F);
		this.body.addBox(-2.5F, -2.5F, -3.0F, 5, 5, 6, 0.0F);
		this.rightWing = new ModelRenderer(this, 0, 12);
		this.rightWing.setPos(-2.5F, -0.5F, 0.0F);
		this.rightWing.addBox(-4.0F, 0.0F, -2.0F, 4, 0, 4, 0.0F);
		this.rightAntenna = new ModelRenderer(this, 0, 3);
		this.rightAntenna.setPos(-0.5F, -2.5F, -3.0F);
		this.rightAntenna.addBox(-3.0F, -3.0F, 0.0F, 3, 3, 0, 0.0F);
		this.setRotateAngle(rightAntenna, 0.0F, 0.0F, -0.2181661564992912F);
		this.leftWing = new ModelRenderer(this, 0, 16);
		this.leftWing.setPos(2.5F, -0.5F, 0.0F);
		this.leftWing.addBox(0.0F, 0.0F, -2.0F, 4, 0, 4, 0.0F);
		this.Mouth = new ModelRenderer(this, 12, 11);
		this.Mouth.setPos(0.0F, 0.5F, -3.0F);
		this.Mouth.addBox(0.0F, 0.0F, -3.0F, 0, 3, 3, 0.0F);
		this.setRotateAngle(Mouth, 0.20943951023931953F, 0.0F, 0.0F);
		this.middleLegs = new ModelRenderer(this, 16, 2);
		this.middleLegs.setPos(0.0F, 2.5F, 0.0F);
		this.middleLegs.addBox(-2.5F, 0.0F, 0.0F, 5, 2, 0, 0.0F);
		this.body.addChild(this.leftAntenna);
		this.body.addChild(this.backLegs);
		this.body.addChild(this.frontLegs);
		this.body.addChild(this.rightWing);
		this.body.addChild(this.rightAntenna);
		this.body.addChild(this.leftWing);
		this.body.addChild(this.Mouth);
		this.body.addChild(this.middleLegs);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.body.render(matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
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
			this.rightWing.zRot = MathHelper.cos(ageInTicks * 2.1f) * 3.1415927f * 0.15f;
			this.leftWing.xRot = this.rightWing.xRot;
			this.leftWing.yRot = this.rightWing.yRot;
			this.leftWing.zRot = -this.rightWing.zRot;
			this.frontLegs.xRot = 0.7853982f;
			this.middleLegs.xRot = 0.7853982f;
			this.backLegs.xRot = 0.7853982f;
		}
	}
}
