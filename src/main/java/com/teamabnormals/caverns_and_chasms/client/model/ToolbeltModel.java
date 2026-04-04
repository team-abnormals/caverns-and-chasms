package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToolbeltModel<T extends LivingEntity> extends HumanoidArmorModel<T> {
	public static final CubeDeformation SLIGHT_DEFORMATION = new CubeDeformation(0.25F);

	public static final ToolbeltModel<?> INSTANCE = new ToolbeltModel<>(createLayerDefinition(CubeDeformation.NONE).bakeRoot());
	public static final ToolbeltModel<?> ARMOR_INSTANCE = new ToolbeltModel<>(createLayerDefinition(SLIGHT_DEFORMATION).bakeRoot());

	public ToolbeltModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static MeshDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshDefinition = HumanoidArmorModel.createBodyLayer(deformation);
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-5.0F, 8.0F, -3.0F, 10.0F, 3.0F, 6.0F, deformation)
				.texOffs(10, 13).addBox(-4.0F, 11.0F, 2.5F, 3.0F, 2.0F, 1.0F, deformation)
				.texOffs(0, 10).addBox(1.0F, 8.0F, 2.0F, 3.0F, 4.0F, 2.0F, deformation), PartPose.ZERO);

		return meshDefinition;
	}

	public static LayerDefinition createLayerDefinition(CubeDeformation deformation) {
		return LayerDefinition.create(createBodyLayer(deformation), 32, 16);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body);
	}
}