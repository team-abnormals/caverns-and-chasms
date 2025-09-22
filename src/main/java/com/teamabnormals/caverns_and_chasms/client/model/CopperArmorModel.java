package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CopperArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {
	public static final CopperArmorModel<?> INSTANCE = new CopperArmorModel<>(createLayerDefinition(LayerDefinitions.OUTER_ARMOR_DEFORMATION).bakeRoot());

	public CopperArmorModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static MeshDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshDefinition = HumanoidArmorModel.createBodyLayer(deformation);
		PartDefinition partDefinition = meshDefinition.getRoot();

		CubeDeformation base = new CubeDeformation(deformation.growX / 4.0F, deformation.growY / 8.0F, deformation.growZ / 4.0F);
		float baseX = deformation.growX * -1.0F;
		float baseY = deformation.growY * -1.125F;

		CubeDeformation top = new CubeDeformation(deformation.growX / 2.0F, deformation.growY / (8.0F / 3.0F), deformation.growZ / 2.0F);
		float topX = deformation.growX * 1.0F;
		float topY = deformation.growY * -1.625F;

		partDefinition.getChild("head").addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F + baseX, -9.0F + baseY, -1.0F, 2.0F, 1.0F, 2.0F, base), PartPose.ZERO);
		partDefinition.getChild("head").addOrReplaceChild("top", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F + topX, -12.0F + topY, -2.0F, 4.0F, 3.0F, 4.0F, top), PartPose.ZERO);
		return meshDefinition;
	}

	public static LayerDefinition createLayerDefinition(CubeDeformation deformation) {
		return LayerDefinition.create(createBodyLayer(deformation), 64, 32);
	}
}