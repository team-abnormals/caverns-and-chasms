package com.teamabnormals.caverns_and_chasms.common.item.copper;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface WeatheringCopperItem {

	static void updateOxidation(ItemStack stack, Level level) {
		CompoundTag tag = stack.getOrCreateTag();
		int oxidation = tag.getInt("oxidation");
		if (!level.isClientSide() && !tag.getBoolean("waxed") && oxidation < 3 && level.getGameTime() % 20 == 0) {
			for (int i = 0; i < level.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get(); i++) {
				RandomSource random = level.getRandom();
				float chance = 0.05688889F * 0.01F;
				if (oxidation == 0) {
					chance *= 0.75F;
				}

				if (random.nextFloat() < chance) {
					tag.putInt("oxidation", oxidation + 1);
				}
			}
		}
	}

	default String getOrCreateDescriptionId(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		int ox = tag.getInt("oxidation");
		return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).withPrefix(
				(tag.getBoolean("waxed") ? "waxed_" : "") + (ox == 0 ? "" : ox == 1 ? "exposed_" : ox == 2 ? "weathered_" : "oxidized_")
		));
	}
}
