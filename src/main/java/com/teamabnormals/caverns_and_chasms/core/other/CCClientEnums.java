package com.teamabnormals.caverns_and_chasms.core.other;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class CCClientEnums {
	public static final EnumProxy<ArmPose> FLUTE_ARM_POSE = new EnumProxy<>(HumanoidModel.ArmPose.class, true, (IArmPoseTransformer) (model, entity, arm) -> {
		float f = model.head.xRot * 0.8F;
		float f1 = model.head.yRot * Mth.cos(f);
		float xRotR = arm == HumanoidArm.RIGHT ? -1.05F : -1.55F;
		float xRotL = arm == HumanoidArm.RIGHT ? -1.55F : -1.05F;
		float yRotR = (arm == HumanoidArm.RIGHT ? -0.5F : -0.35F) + f1;
		float yRotL = (arm == HumanoidArm.RIGHT ? 0.35F : 0.5F) + f1;

		Vector3f vec3R = (new Matrix3f()).rotationZYX(0.0F, yRotR, xRotR).rotateLocalX(f).getEulerAnglesZYX(new Vector3f());
		Vector3f vec3L = (new Matrix3f()).rotationZYX(0.0F, yRotL, xRotL).rotateLocalX(f).getEulerAnglesZYX(new Vector3f());

		model.rightArm.setRotation(vec3R.x, vec3R.y, vec3R.z);
		model.leftArm.setRotation(vec3L.x, vec3L.y, vec3L.z);
	});
}
