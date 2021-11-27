package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class SpinelCrownModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "spinel_crown"), "main");
	private static final Map<Float, SpinelCrownModel<?>> CACHE = new HashMap<>();

	public SpinelCrownModel(float modelSize) {
		super(createLayerDefinition(modelSize).bakeRoot());
	}

	public static LayerDefinition createLayerDefinition(float modelSize) {
		CubeDeformation deformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation.extend(modelSize)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@SuppressWarnings("unchecked")
	public static <A extends HumanoidModel<?>> A getModel(float modelSize) {
		return (A) CACHE.computeIfAbsent(modelSize, SpinelCrownModel::new);
	}
}