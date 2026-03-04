package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Cavefish;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class CavefishModel<T extends Cavefish> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tailFin;
	private final ModelPart leftFin;
	private final ModelPart rightFin;

	public CavefishModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root;
		this.body = root.getChild("body");
		this.tailFin = this.body.getChild("tail_fin");
		this.leftFin = this.body.getChild("left_fin");
		this.rightFin = this.body.getChild("right_fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 6.0F)
				.texOffs(16, 0).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.2F))
				.texOffs(17, 8).addBox(0.0F, -3.5F, -1.5F, 0.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 21.5F, 0.5F));

		PartDefinition tailFin = body.addOrReplaceChild("tail_fin", CubeListBuilder.create()
				.texOffs(6, 5).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 5.0F), PartPose.offset(0.0F, -0.5F, 2.5F));

		PartDefinition leftFin = body.addOrReplaceChild("left_fin", CubeListBuilder.create()
				.texOffs(1, 10).addBox(0.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(1.0F, 1.5F, -0.5F, 0.0F, -0.6109F, 0.0F));

		PartDefinition rightFin = body.addOrReplaceChild("right_fin", CubeListBuilder.create()
				.texOffs(1, 12).mirror().addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.5F, -0.5F, 0.0F, 0.6109F, 0.0F));

		return LayerDefinition.create(mesh, 32, 16);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}

		this.tailFin.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}
}