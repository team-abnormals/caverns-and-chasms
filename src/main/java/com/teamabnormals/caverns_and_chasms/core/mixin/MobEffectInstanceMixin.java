package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.CCSubtleMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements CCSubtleMobEffectInstance {
	@Unique
	private boolean subtle;

	@Override
	public boolean isSubtle() {
		return this.subtle;
	}

	@Override
	public void setSubtle(boolean subtle) {
		this.subtle = subtle;
	}
}