package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCMobEffectTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class BejeweledAppleItem extends Item {

	public BejeweledAppleItem(Properties properties) {
		super(properties);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		super.finishUsingItem(stack, level, entity);

		RandomSource random = level.getRandom();
		if (!level.isClientSide) {
			addRandomEffect(entity, random, 60, 120, 0);
			addRandomEffect(entity, random, 20, 40, 1);
		}

		if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(this, 400);
		}

		return stack;
	}

	public static void addRandomEffect(LivingEntity entity, RandomSource random, int minSecs, int maxSecs, int level) {
		entity.addEffect(new MobEffectInstance(getRandomEffect(random), 20 * (minSecs + random.nextInt(1 + maxSecs - minSecs)), level));
	}

	private static Holder<MobEffect> getRandomEffect(RandomSource random) {
		List<Reference<MobEffect>> mobEffectList = BuiltInRegistries.MOB_EFFECT.holders().toList();
		Reference<MobEffect> effect = mobEffectList.get(random.nextInt(mobEffectList.size()));
		while (BuiltInRegistries.MOB_EFFECT.getTag(CCMobEffectTags.BEJEWELED_APPLE_CANNOT_INFLICT).get().contains(effect) || effect.value().isInstantenous())
			effect = mobEffectList.get(random.nextInt(mobEffectList.size()));
		return effect;
	}

	@Override
	public SoundEvent getEatingSound() {
		return CCSoundEvents.BEJEWELED_APPLE_EAT.get();
	}
}