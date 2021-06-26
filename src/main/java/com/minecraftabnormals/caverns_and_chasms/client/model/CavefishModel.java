package com.minecraftabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.caverns_and_chasms.common.entity.CavefishEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class CavefishModel<T extends CavefishEntity> extends SegmentedModel<T> {
	public ModelRenderer body;
	public ModelRenderer tail;
	public ModelRenderer fin;

	public CavefishModel() {
		this.texWidth = 32;
		this.texHeight = 16;
		this.body = new ModelRenderer(this);
		this.body.setPos(-1.0F, 20.0F, -4.0F);
		this.body.texOffs(0, 0).addBox(0.0F, 1.0F, 0.0F, 2.0F, 3.0F, 7.0F, 0.0F, false);
		this.tail = new ModelRenderer(this);
		this.tail.setPos(0.0F, 20.0F, 3.0F);
		this.tail.texOffs(18, 0).addBox(0.0F, 1.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, false);
		this.fin = new ModelRenderer(this);
		this.fin.setPos(0.0F, 18.0F, -2.0F);
		this.fin.texOffs(24, 0).addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body, this.tail, this.fin);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entityIn.isInWater()) {
			f = 1.5F;
		}

		this.tail.yRot = -f * 0.45F * MathHelper.sin(0.6F * ageInTicks);
	}
}
