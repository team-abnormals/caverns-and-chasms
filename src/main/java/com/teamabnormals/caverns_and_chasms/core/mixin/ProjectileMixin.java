package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {
	public ProjectileMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		IDataManager data = (IDataManager) this;
		if (data.getValue(CCDataProcessors.SHOULD_DEFLECT)) {
			this.setDeltaMovement(data.getValue(CCDataProcessors.DEFLECT_X), data.getValue(CCDataProcessors.DEFLECT_Y), data.getValue(CCDataProcessors.DEFLECT_Z));
			data.setValue(CCDataProcessors.SHOULD_DEFLECT, false);
		}
	}
}