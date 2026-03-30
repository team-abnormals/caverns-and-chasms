package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {
	@Shadow
	public abstract DamageType type();

	@Shadow
	@Final
	@Nullable
	private Entity causingEntity;

	@Shadow
	@Final
	@Nullable
	private Entity directEntity;

	@Inject(method = "getLocalizedDeathMessage", at = @At("RETURN"), cancellable = true)
	private void getLocalizedDeathMessage(LivingEntity entity, CallbackInfoReturnable<Component> cir) {
		if (this.type().msgId().equals("caverns_and_chasms.riding_grazer")) {
			cir.setReturnValue(Component.translatable("death.attack." + this.type().msgId(), entity.getDisplayName(), this.causingEntity.getDisplayName(), this.directEntity.getDisplayName()));
		}
	}
}
