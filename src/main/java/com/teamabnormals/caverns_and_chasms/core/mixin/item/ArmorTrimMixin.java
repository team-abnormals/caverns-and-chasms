package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.common.item.CCArmorTrim;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements CCArmorTrim {

	@Unique
	private boolean caverns_and_chasms$isFaded = false;

	@Unique
	private boolean caverns_and_chasms$isEmissive = false;

	@Unique
	private boolean caverns_and_chasms$isPulse = false;

	@Override
	public boolean isFaded() {
		return this.caverns_and_chasms$isFaded;
	}

	@Override
	public void setFaded(boolean faded) {
		this.caverns_and_chasms$isFaded = faded;
	}

	@Override
	public boolean isEmissive() {
		return this.caverns_and_chasms$isEmissive;
	}

	@Override
	public void setEmissive(boolean emissive) {
		this.caverns_and_chasms$isEmissive = emissive;
	}

	@Override
	public boolean isPulse() {
		return this.caverns_and_chasms$isPulse;
	}

	@Override
	public void setPulse(boolean pulse) {
		this.caverns_and_chasms$isPulse = pulse;
	}
}
