package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MimeHeadModel extends SkullModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "mime_head"), "main");

	public MimeHeadModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition createHeadLayer() {
		MeshDefinition meshdefinition = SkullModel.createHeadModel();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.getChild("head").addOrReplaceChild("horns", CubeListBuilder.create().texOffs(8, 60).addBox(-2.0F, -14.0F, 0.0F, 1.0F, 4.0F, 0.0F).texOffs(0, 59).addBox(-4.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F).texOffs(18, 60).addBox(1.0F, -14.0F, 0.0F, 1.0F, 4.0F, 0.0F).texOffs(10, 59).addBox(2.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.ZERO);
		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(float p_104188_, float p_104189_, float p_104190_) {
		super.setupAnim(p_104188_, p_104189_, p_104190_);
		this.head.yRot = p_104188_ == 0.0F ? p_104189_ * ((float) Math.PI / 180F) : p_104188_;
	}
}