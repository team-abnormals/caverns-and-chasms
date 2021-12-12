package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolem.class)
public abstract class IronGolemEntityMixin extends AbstractGolem {

	private IronGolemEntityMixin(EntityType<? extends AbstractGolem> entity, Level world) {
		super(entity, world);
	}

	@Shadow
	public abstract boolean isPlayerCreated();

	@Inject(at = @At("HEAD"), method = "doPush")
	public void doPush(Entity entity, CallbackInfo ci) {
		if (entity instanceof Enemy && this.getRandom().nextInt(20) == 0) {
			this.setTarget((LivingEntity) entity);
		}
		super.doPush(entity);
	}

	@Inject(at = @At("RETURN"), method = "canAttackType", cancellable = true)
	public void canAttackType(EntityType<?> typeIn, CallbackInfoReturnable<Boolean> ci) {
		if (this.isPlayerCreated() && typeIn == EntityType.PLAYER || CCConfig.COMMON.creeperExplosionsDestroyBlocks.get() && typeIn == EntityType.CREEPER) {
			ci.setReturnValue(false);
		}
		ci.setReturnValue(super.canAttackType(typeIn));
	}
}
