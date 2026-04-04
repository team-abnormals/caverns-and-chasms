package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.SubtleMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements SubtleMobEffectInstance {
	@Unique
	private boolean subtle = false;

	@Override
	public boolean isSubtle() {
		return this.subtle;
	}

	@Override
	public void setSubtle(boolean subtle) {
		this.subtle = subtle;
	}
}