package com.teamabnormals.caverns_and_chasms.core.mixin.brewing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.item.SubtleMobEffectInstance;
import com.teamabnormals.caverns_and_chasms.common.item.SubtlePotion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(PotionUtils.class)
public abstract class PotionUtilsMixin {

	@Inject(at = @At("RETURN"), method = "getAllEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
	private static void getAllEffects(@Nullable CompoundTag tag, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			cir.setReturnValue(SubtlePotion.setSubtleEffects(cir.getReturnValue()));
		}
	}

	@Inject(at = @At("RETURN"), method = "getCustomEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
	private static void getCustomEffects(@Nullable CompoundTag tag, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			cir.setReturnValue(SubtlePotion.setSubtleEffects(cir.getReturnValue()));
		}
	}

	@Inject(at = @At("RETURN"), method = "getPotion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/alchemy/Potion;", cancellable = true)
	private static void getPotion(@Nullable CompoundTag tag, CallbackInfoReturnable<Potion> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			cir.setReturnValue(new SubtlePotion(cir.getReturnValue()));
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isVisible()Z"), method = "getColor(Ljava/util/Collection;)I")
	private static boolean getColor(MobEffectInstance instance, Operation<Boolean> original) {
		return original.call(instance) || instance instanceof SubtleMobEffectInstance subtleEffect && subtleEffect.isSubtle();
	}
}