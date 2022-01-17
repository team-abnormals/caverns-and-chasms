package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class SpinelCrownModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "spinel_crown"), "outer_armor");
	private static final Map<Boolean, SpinelCrownModel<?>> CACHE = new HashMap<>();

	public SpinelCrownModel(boolean isPiglin) {
		super(createLayerDefinition(isPiglin).bakeRoot());
	}

	public static LayerDefinition createLayerDefinition(boolean isPiglin) {
		CubeDeformation deformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -13.5F, -4.0F, 8.0F, 8.0F, 8.0F, deformation.extend(0.51F).extend(isPiglin ? 1.0F : 0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@SuppressWarnings("unchecked")
	public static <A extends HumanoidModel<?>> A getModel(boolean isPiglin) {
		return (A) CACHE.computeIfAbsent(isPiglin, SpinelCrownModel::new);
	}
}