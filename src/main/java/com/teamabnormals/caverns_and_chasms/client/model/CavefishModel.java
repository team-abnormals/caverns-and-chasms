package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.entity.Cavefish;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CavefishModel<T extends Cavefish> extends ListModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "cavefish"), "main");

	public ModelPart body;
	public ModelPart tail;
	public ModelPart fin;

	public CavefishModel(ModelPart root) {
		this.body = root.getChild("body");
		this.tail = root.getChild("tail");
		this.fin = root.getChild("fin");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 1.0F, 0.0F, 2.0F, 3.0F, 7.0F, false), PartPose.offsetAndRotation(-1.0F, 20.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, 1.0F, 0.0F, 0.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 20.0F, 3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition fin = root.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 18.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.body, this.tail, this.fin);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entityIn.isInWater()) {
			f = 1.5F;
		}

		this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}
}
