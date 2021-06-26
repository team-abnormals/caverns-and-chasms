package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin {

	@Inject(at = @At("RETURN"), method = "isTargetable", cancellable = true)
	private static void isTargetable(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
		if (!CCConfig.COMMON.creeperExplosionsDestroyBlocks.get() && entity.getType() == EntityType.CREEPER)
			ci.setReturnValue(true);
	}
}
