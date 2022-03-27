package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCMobEffectTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

public class BejeweledAppleItem extends Item {

	public BejeweledAppleItem(Properties properties) {
		super(properties);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		super.finishUsingItem(stack, level, entity);

		Random random = level.getRandom();
		if (!level.isClientSide) {
			entity.addEffect(new MobEffectInstance(getRandomEffect(random), 400 + (20 * random.nextInt(21)), 1));
			entity.addEffect(new MobEffectInstance(getRandomEffect(random), 1200 + (20 * random.nextInt(61))));
		}

		if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(this, 400);
		}

		return stack;
	}

	private MobEffect getRandomEffect(Random random) {
		List<MobEffect> mobEffectList = ForgeRegistries.MOB_EFFECTS.getValues().stream().toList();
		MobEffect effect = mobEffectList.get(random.nextInt(mobEffectList.size()));
		while (ForgeRegistries.MOB_EFFECTS.tags().getTag(CCMobEffectTags.BEJEWELED_APPLE_CANNOT_INFLICT).contains(effect) || effect.isInstantenous())
			effect = mobEffectList.get(random.nextInt(mobEffectList.size()));
		return effect;
	}
}