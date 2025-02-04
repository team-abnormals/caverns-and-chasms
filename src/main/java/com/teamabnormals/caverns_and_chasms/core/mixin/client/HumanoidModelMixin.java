package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.core.other.CCClientEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

	@Shadow
	protected abstract HumanoidArm getAttackArm(T p_102857_);

	@Shadow
	protected abstract ModelPart getArm(HumanoidArm p_102852_);

	@Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
	public void setupAttackAnimation(T entity, float val, CallbackInfo ci) {
		if (!(this.attackTime <= 0.0F)) {
			HumanoidArm attackArm = this.getAttackArm(entity);
			ModelPart armModel = this.getArm(attackArm);
			if (entity.getItemInHand(entity.swingingArm).is(CCItems.FOIL.get())) {
				CCClientEvents.customFoilAnimation((HumanoidModel<?>) (Object) this, armModel, attackArm == HumanoidArm.LEFT);
				ci.cancel();
			}
		}
	}
}