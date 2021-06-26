package com.minecraftabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MimeArmorModel<T extends LivingEntity> extends BipedModel<T> {

	public MimeArmorModel(float modelSizeIn) {
		super(modelSizeIn);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn);
		this.head.setPos(0.0F, 0.0F, 0.0F);

		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn + 0.5F);
		this.hat.setPos(0.0F, 0.0F, 0.0F);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, -2.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelSizeIn);
		this.body.setPos(0.0F, 0.0F, 0.0F);

		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.addBox(-3.0F, -4.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);

		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.addBox(-1.0F, -4.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn, true);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);

		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
		this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);

		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn, true);
		this.leftLeg.setPos(1.9F, 12.0F, 0.0F);
	}
}