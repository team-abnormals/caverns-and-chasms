package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class RatModel<T extends RatEntity> extends AgeableModel<T> {
	public ModelRenderer body;
	public ModelRenderer tail;
	public ModelRenderer head;
	public ModelRenderer earRight;
	public ModelRenderer earLeft;
	public ModelRenderer whisker;
	public ModelRenderer tooth;
	public ModelRenderer legFrontLeft;
	public ModelRenderer legBackLeft;
	public ModelRenderer legFrontRight;
	public ModelRenderer legBackRight;

	public RatModel() {
		texWidth = 32;
		texHeight = 32;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 21.0F, -2.0F);
		body.texOffs(0, 0).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setPos(0.0F, 0.0F, 8.0F);
		body.addChild(tail);
		tail.texOffs(12, 5).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 8.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 21.0F, -2.0F);
		head.texOffs(0, 13).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		earRight = new ModelRenderer(this);
		earRight.setPos(0.0F, -3.0F, -1.0F);
		head.addChild(earRight);
		earRight.texOffs(0, 6).addBox(-3.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, 0.0F, false);

		earLeft = new ModelRenderer(this);
		earLeft.setPos(0.0F, -3.0F, -1.0F);
		head.addChild(earLeft);
		earLeft.texOffs(0, 6).addBox(1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, 0.0F, true);

		whisker = new ModelRenderer(this);
		whisker.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(whisker);
		whisker.texOffs(12, 14).addBox(-3.5F, -2.0F, -4.0F, 7.0F, 3.0F, 0.0F, 0.0F, false);

		tooth = new ModelRenderer(this);
		tooth.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(tooth);
		tooth.texOffs(4, 0).addBox(-0.5F, 1.0F, -5.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		legFrontLeft = new ModelRenderer(this);
		legFrontLeft.setPos(-2.0F, 23.0F, 5.0F);
		legFrontLeft.texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		legBackLeft = new ModelRenderer(this);
		legBackLeft.setPos(2.0F, 23.0F, 5.0F);
		legBackLeft.texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);

		legFrontRight = new ModelRenderer(this);
		legFrontRight.setPos(-2.0F, 23.0F, 0.0F);
		legFrontRight.texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		legBackRight = new ModelRenderer(this);
		legBackRight.setPos(2.0F, 23.0F, 0.0F);
		legBackRight.texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(body, legFrontRight, legFrontLeft, legBackRight, legBackLeft);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		float f = entity.isInWater() ? 1.0F : 1.5F;
		this.tail.yRot = -f * 0.45F * MathHelper.sin(0.6F * ageInTicks);
		this.legBackRight.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.legBackLeft.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.legFrontRight.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.legFrontLeft.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.head.y = !this.young ? 21.0F : 17.5F;
	}
}