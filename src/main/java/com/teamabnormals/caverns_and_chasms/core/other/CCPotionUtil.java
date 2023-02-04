package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CCPotionUtil {

	public static void addTetherPotionTooltip(ItemStack stack, List<Component> tooltip) {
		List<MobEffectInstance> list = CCPotionUtil.getContinuousEffects(stack, true);
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

		tooltip.add(Component.empty());
		tooltip.add((Component.translatable("item.modifiers." + EquipmentSlot.HEAD.getName())).withStyle(ChatFormatting.GRAY));
		if (list.isEmpty()) {
			tooltip.add((Component.translatable("effect.none")).withStyle(ChatFormatting.GRAY));
		} else {
			for (MobEffectInstance mobeffectinstance : list) {
				MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
				MobEffect effect = mobeffectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (mobeffectinstance.getAmplifier() > 0) {
					mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
				}

				mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, StringUtil.formatTickDuration(getTetherPotionDuration(mobeffectinstance.getDuration())));

				tooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			tooltip.add(Component.empty());
			tooltip.add((Component.translatable("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add((Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					tooltip.add((Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
				}
			}
		}
	}

	public static List<MobEffectInstance> getContinuousEffects(ItemStack stack, boolean includeCustom) {
		List<MobEffectInstance> list = includeCustom ? PotionUtils.getMobEffects(stack) : new ArrayList<>(PotionUtils.getPotion(stack).getEffects());
		list.removeIf(mobEffectInstance -> mobEffectInstance.getEffect().isInstantenous());
		return list;
	}

	public static int getTetherPotionDuration(int originalDuration) {
		int duration = Math.round(10 - 1 / ((originalDuration / 20 + 200) * 0.0005F)) * 20;
		return Math.max(duration, 20);
	}

	public static boolean isElegantTetherPotion(ItemStack stack) {
		return getContinuousEffects(stack, false).isEmpty() && PotionUtils.getPotion(stack).hasInstantEffects();
	}

	public static int getTetherPotionColor(ItemStack stack) {
		CompoundTag compoundtag = stack.getTag();
		if (compoundtag != null && compoundtag.contains("CustomPotionColor", 99)) {
			return compoundtag.getInt("CustomPotionColor");
		} else {
			return PotionUtils.getPotion(stack) == Potions.EMPTY ? 16253176 : PotionUtils.getColor(getContinuousEffects(stack, true));
		}
	}
}