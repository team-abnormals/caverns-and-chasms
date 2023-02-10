package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.UUID;

public interface SilverItem {
	UUID MAGIC_DAMAGE_UUID = UUID.fromString("b3406524-886c-49c3-94e6-88edd0e8e63b");

	default Multimap<Attribute, AttributeModifier> addMagicDamageAttribute(float damage, EquipmentSlot slot, Multimap<Attribute, AttributeModifier> map) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(map);
		builder.put(CCAttributes.MAGIC_DAMAGE.get(), new AttributeModifier(MAGIC_DAMAGE_UUID, "Magic damage", damage, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? builder.build() : map;
	}

	default void causeMagicDamage(ItemStack stack, LivingEntity target) {
		Collection<AttributeModifier> magicDamageModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.MAGIC_DAMAGE.get());
		if (!magicDamageModifiers.isEmpty()) {
			float magicDamage = (float) magicDamageModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
			if (target instanceof WitherBoss)
				magicDamage *= 3.0F;
			target.hurt(DamageSource.MAGIC, magicDamage);
			causeMagicDamageParticles(target);
		}
	}

	static void causeMagicDamageParticles(LivingEntity target) {
		causeMagicParticles(target, false);
	}

	static void causeMagicProtectionParticles(LivingEntity defender) {
		causeMagicParticles(defender, true);
	}

	static void causeMagicParticles(LivingEntity entity, boolean defensive) {
		RandomSource random = entity.getRandom();
		int count = 3 + random.nextInt(2) + random.nextInt(2);
		if (defensive) {
			count += 3 + random.nextInt(2);
		}
		for (int i = 0; i < count; ++i) {
			double d0 = random.nextGaussian() * 0.02D;
			double d1 = random.nextGaussian() * 0.02D;
			double d2 = random.nextGaussian() * 0.02D;
			String particle = CavernsAndChasms.MOD_ID + ":affliction_" + (defensive ? "spark" : "damage");
			NetworkUtil.spawnParticle(particle, entity.getRandomX(0.75D), entity.getRandomY() + (defensive ? 0.3F : 0.0F), entity.getRandomZ(0.75D), d0, d1, d2);
		}
	}
}