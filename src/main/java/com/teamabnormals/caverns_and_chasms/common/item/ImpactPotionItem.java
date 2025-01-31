package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ImpactPotionItem extends TetherPotionItem {

	public ImpactPotionItem(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		List<MobEffectInstance> list = PotionUtils.getMobEffects(stack);
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

		List<Component> instanttooltip = Lists.newArrayList();
		List<Component> continuoustooltip = Lists.newArrayList();

		if (list.isEmpty()) {
			continuoustooltip.add((Component.translatable("effect.none")).withStyle(ChatFormatting.GRAY));
		} else {
			for (MobEffectInstance mobeffectinstance : list) {
				MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
				MobEffect effect = mobeffectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (mobeffectinstance.getAmplifier() > 0) {
					mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
				}

				if (effect.isInstantenous()) {
					instanttooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
				} else {
					mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, StringUtil.formatTickDuration(mobeffectinstance.getDuration()));
					continuoustooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
				}
			}
		}

		tooltip.addAll(instanttooltip);
		if (!continuoustooltip.isEmpty()) {
			tooltip.add(Component.empty());
			tooltip.add((Component.translatable("item.modifiers.splash").withStyle(ChatFormatting.GRAY)));
			tooltip.addAll(continuoustooltip);
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
}
