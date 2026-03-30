package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Invoker("updateInvisibilityStatus")
	void invokeUpdateInvisibilityStatus();
}