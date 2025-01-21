package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.CCSubtleMobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PotionUtils.class)
public abstract class PotionUtilsMixin {

	@Inject(at = @At("RETURN"), method = "getAllEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
	private static void getAllEffects(CompoundTag tag, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
		if (tag.getBoolean("Subtle")) {
			List<MobEffectInstance> effects = new ArrayList<>();

			cir.getReturnValue().forEach(effect -> {
				MobEffectInstance clone = new MobEffectInstance(
						effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), false,
						effect.showIcon(), null, effect.getFactorData()
				);
				clone.setCurativeItems(effect.getCurativeItems());
				((CCSubtleMobEffectInstance) clone).setSubtle(true);

				effects.add(clone);
			});


			cir.setReturnValue(effects);
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isVisible()Z"), method = "getColor(Ljava/util/Collection;)I")
	private static boolean getColor(MobEffectInstance instance) {
		return instance.isVisible() || instance instanceof CCSubtleMobEffectInstance subtleEffect && subtleEffect.isSubtle();
	}
}