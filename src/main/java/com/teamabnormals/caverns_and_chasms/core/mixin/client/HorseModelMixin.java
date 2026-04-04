package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.teamabnormals.caverns_and_chasms.common.item.copper.CopperHorseArmorItem;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;

@Mixin(HorseModel.class)
public abstract class HorseModelMixin<T extends AbstractHorse> extends AgeableListModel<T> {

	@Shadow
	@Final
	protected ModelPart headParts;

	@Shadow
	@Final
	protected ModelPart body;

	@Inject(method = "createBodyMesh", at = @At("TAIL"))
	private static void createBodyMesh(CubeDeformation deformation, CallbackInfoReturnable<MeshDefinition> cir, @Local(ordinal = 3) PartDefinition head) {
		head.addOrReplaceChild("rod_base", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -17.0F, -1.0F, 4.0F, 4.0F, 4.0F), PartPose.ZERO);
		head.addOrReplaceChild("rod", CubeListBuilder.create().texOffs(56, 8).addBox(-1.0F, -13.0F, 0.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;FFFFF)V", at = @At("TAIL"))
	private void setupAnim(T entity, float limbSwing, float limbSwingTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		try {
			ModelPart head = this.headParts.getChild("head");
			ModelPart rod = head.getChild("rod");
			ModelPart rodBase = head.getChild("rod_base");

			boolean hasArmor = entity instanceof Horse horse && horse.getArmor().getItem() instanceof CopperHorseArmorItem;
			rod.visible = hasArmor;
			rodBase.visible = hasArmor;
		} catch (NoSuchElementException ignored) {
		}
	}
}