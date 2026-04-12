package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.LargeFireball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ghast.class)
public abstract class GhastMixin {

	@Inject(method = "isReflectedFireball", at = @At("RETURN"), cancellable = true)
	private static void isReflectedFireball(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (damageSource.getDirectEntity() instanceof LargeFireball fireball && ((IDataManager) fireball).getValue(CCDataProcessors.RICOCHETS) > 0) {
			cir.setReturnValue(true);
		}
	}
}