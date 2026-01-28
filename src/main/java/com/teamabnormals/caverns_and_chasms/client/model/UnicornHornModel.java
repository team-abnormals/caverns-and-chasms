package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.item.copper.CopperHorseArmorItem;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class UnicornHornModel<T extends AbstractHorse> extends HorseModel<T> {
	private final ModelPart horn;
	private final float baseHornY;

	public UnicornHornModel(ModelPart root) {
		super(root);
		this.horn = root.getChild("head_parts").getChild("head").getChild("horn");
		this.baseHornY = this.horn.y;
	}

	public static MeshDefinition createBodyMesh(CubeDeformation deformation) {
		MeshDefinition mesh = HorseModel.createBodyMesh(deformation);
		PartDefinition root = mesh.getRoot();
		PartDefinition headParts = root.getChild("head_parts");
		PartDefinition head = headParts.getChild("head");
		head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 57).addBox(-0.5F, -17.0F, 0.5F, 1.0F, 6.0F, 1.0F), PartPose.ZERO);
		return mesh;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingTicks, ageInTicks, netHeadYaw, headPitch);
		for (ModelPart part : this.saddleParts) {
			part.visible = false;
		}

		boolean copperArmor = entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof CopperHorseArmorItem;
		this.horn.y = copperArmor ? this.baseHornY - 6.0F : this.baseHornY;
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of();
	}
}