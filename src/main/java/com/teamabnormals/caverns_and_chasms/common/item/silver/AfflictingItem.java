package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDamageSources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import java.util.List;
import java.util.Random;

public interface AfflictingItem {

	default void causeAfflictionDamage(LivingEntity target) {
		this.causeAfflictionDamage(target, false);
	}

	default void causeAfflictionDamage(LivingEntity target, boolean defensive) {
		if (target.isInvertedHealAndHarm()) {
			float damage = defensive ? 1.0F : 2.0F;
			if (target instanceof WitherBoss)
				damage *= 3.0F;
			target.hurt(CCDamageSources.AFFLICTION, damage);
			Random rand = target.getRandom();
			int count = 2 + rand.nextInt(2) + rand.nextInt(2);
			for (int i = 0; i < count; ++i) {
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				String particle = CavernsAndChasms.MOD_ID + ":affliction_" + (defensive ? "spark" : "damage");
				NetworkUtil.spawnParticle(particle, target.getRandomX(1.0D), target.getRandomY() + (defensive ? 0.3F : 0.0F), target.getRandomZ(1.0D), d0, d1, d2);
			}
		}
	}

	default void addAfflictionTooltip(List<Component> tooltip) {
		tooltip.add(new TranslatableComponent("tooltip.caverns_and_chasms.afflicting").withStyle(ChatFormatting.GRAY));
	}
}