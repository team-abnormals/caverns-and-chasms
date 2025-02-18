package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.item.CCSubtleMobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(PotionUtils.class)
public abstract class PotionUtilsMixin {

	@Inject(at = @At("RETURN"), method = "getAllEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
	private static void getAllEffects(@Nullable CompoundTag tag, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			cir.setReturnValue(setSubtleEffects(cir.getReturnValue()));
		}
	}

	@Inject(at = @At("RETURN"), method = "getCustomEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
	private static void getCustomEffects(@Nullable CompoundTag tag, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			cir.setReturnValue(setSubtleEffects(cir.getReturnValue()));
		}
	}

	@Inject(at = @At("RETURN"), method = "getPotion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/alchemy/Potion;", cancellable = true)
	private static void getPotion(@Nullable CompoundTag tag, CallbackInfoReturnable<Potion> cir) {
		if (tag != null && tag.getBoolean("Subtle")) {
			Potion potion = cir.getReturnValue();
			potion.effects = ImmutableList.copyOf(setSubtleEffects(potion.getEffects()));
			cir.setReturnValue(potion);
		}
	}

	private static List<MobEffectInstance> setSubtleEffects(List<MobEffectInstance> list) {
		List<MobEffectInstance> effects = new ArrayList<>();
		list.forEach(effect -> {
			MobEffectInstance clone = new MobEffectInstance(
					effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), false,
					effect.showIcon(), null, effect.getFactorData()
			);
			clone.setCurativeItems(effect.getCurativeItems());
			((CCSubtleMobEffectInstance) clone).setSubtle(true);

			effects.add(clone);
		});

		return effects;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isVisible()Z"), method = "getColor(Ljava/util/Collection;)I")
	private static boolean getColor(MobEffectInstance instance) {
		return instance.isVisible() || instance instanceof CCSubtleMobEffectInstance subtleEffect && subtleEffect.isSubtle();
	}
}