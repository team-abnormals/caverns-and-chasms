package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

import javax.annotation.Nullable;

public interface CCArmorTrim {
	boolean isFaded();

	void setFaded(boolean faded);

	boolean isEmissive();

	void setEmissive(boolean emissive);

	boolean isPulse();

	void setPulse(boolean pulse);

	@Nullable
	static CCArmorTrim create(ItemStack stack) {
		ArmorTrim trim = stack.get(DataComponents.TRIM);
		if (trim != null) {
			CCArmorTrim cc = (CCArmorTrim) trim;
			cc.setFaded(stack.has(CCDataComponents.FADED_TRIM));
			cc.setEmissive(stack.has(CCDataComponents.EMISSIVE_TRIM));
			cc.setPulse(stack.has(CCDataComponents.PULSE_TRIM));
			return cc;
		}
		return null;
	}
}
