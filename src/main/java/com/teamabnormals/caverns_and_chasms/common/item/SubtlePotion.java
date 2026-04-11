package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

public interface SubtlePotion {
	static boolean isSubtle(ItemStack stack) {
		if (stack.has(DataComponents.POTION_CONTENTS)) {
			PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
			for (MobEffectInstance effect : contents.getAllEffects()) {
				if (effect.getEffect().is(CCMobEffects.SUBTLE)) {
					return true;
				}
			}
		}
		return false;
	}
}
