package com.teamabnormals.caverns_and_chasms.core.mixin.brewing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.item.SubtleMobEffectInstance;
import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(PotionContents.class)
public abstract class PotionContentsMixin {

	@Shadow
	@Final
	private Optional<Holder<Potion>> potion;

	@Shadow public abstract Iterable<MobEffectInstance> getAllEffects();

	@Inject(at = @At("RETURN"), method = "getAllEffects", cancellable = true)
	private void getAllEffects(CallbackInfoReturnable<Iterable<MobEffectInstance>> cir) {
		if (this.potion.isPresent() && this.potion.get().value() instanceof SubtlePotion) {
			cir.setReturnValue(SubtlePotion.setSubtleEffects(cir.getReturnValue()));
		}
	}

	@Inject(at = @At("HEAD"), method = "forEachEffect", cancellable = true)
	private void getAllEffects(Consumer<MobEffectInstance> action, CallbackInfo ci) {
		for (MobEffectInstance instance : this.getAllEffects()) {
			action.accept(instance);
		}
		ci.cancel();
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isVisible()Z"), method = "getColorOptional")
	private static boolean getColor(MobEffectInstance instance, Operation<Boolean> original) {
		return original.call(instance) || instance instanceof SubtleMobEffectInstance subtleEffect && subtleEffect.isSubtle();
	}
}