package com.teamabnormals.caverns_and_chasms.common.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TetherPotionItem extends PotionItem implements Wearable {

	public TetherPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (!world.isClientSide()) {
			for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(stack)) {
				if (!mobeffectinstance.getEffect().isInstantenous()) {
					player.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), 40, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible(), mobeffectinstance.showIcon()));
				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = player.getItemBySlot(equipmentslot);
		if (itemstack1.isEmpty()) {
			player.setItemSlot(equipmentslot, itemstack.copy());
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}

			itemstack.setCount(0);
			return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) && !isElegantPotion(stack);
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		if (isElegantPotion(stack)) {
			return this.getDescriptionId() + ".effect.elegant";
		} else {
			return super.getDescriptionId(stack);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		List<MobEffectInstance> list = getContinuousEffects(stack, true);
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

		if (list.isEmpty()) {
			tooltip.add((new TranslatableComponent("effect.none")).withStyle(ChatFormatting.GRAY));
		} else {
			for(MobEffectInstance mobeffectinstance : list) {
				MutableComponent mutablecomponent = new TranslatableComponent(mobeffectinstance.getDescriptionId());
				MobEffect effect = mobeffectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for(Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (mobeffectinstance.getAmplifier() > 0) {
					mutablecomponent = new TranslatableComponent("potion.withAmplifier", mutablecomponent, new TranslatableComponent("potion.potency." + mobeffectinstance.getAmplifier()));
				}

				mutablecomponent = new TranslatableComponent("potion.withDuration", mutablecomponent, StringUtil.formatTickDuration(40));

				tooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			tooltip.add(TextComponent.EMPTY);
			tooltip.add((new TranslatableComponent("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

			for(Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add((new TranslatableComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					tooltip.add((new TranslatableComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
				}
			}
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			List<List<Pair<MobEffect, Integer>>> list = Lists.newArrayList();

			for(Potion potion : Registry.POTION) {
				if (potion != Potions.EMPTY) {
					ItemStack itemstack = PotionUtils.setPotion(new ItemStack(this), potion);
					if (!isElegantPotion(itemstack)) {
						boolean flag = false;

						if (potion.getEffects().isEmpty()) {
							flag = true;
						} else {
							List<Pair<MobEffect, Integer>> list1 = Lists.newArrayList();

							for (MobEffectInstance mobeffectinstance : potion.getEffects()) {
								list1.add(new Pair<MobEffect, Integer>(mobeffectinstance.getEffect(), mobeffectinstance.getAmplifier()));
							}

							if (list.stream().filter(list2 -> list2.equals(list1)).toList().isEmpty()) {
								list.add(list1);
								flag = true;
							}
						}

						if (flag) {
							items.add(itemstack);
							if (potion == Potions.AWKWARD) {
								items.add(PotionUtils.setPotion(new ItemStack(this), Potions.HEALING));
							}
						}
					}
				}
			}
		}
	}

	public static List<MobEffectInstance> getContinuousEffects(ItemStack stack, boolean includeCustom) {
		List<MobEffectInstance> list = includeCustom ? PotionUtils.getMobEffects(stack) : new ArrayList<>(PotionUtils.getPotion(stack).getEffects());
		Iterator<MobEffectInstance> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getEffect().isInstantenous()) {
				iterator.remove();
			}
		}

		return list;
	}

	public static boolean isElegantPotion(ItemStack stack) {
		return getContinuousEffects(stack, false).isEmpty() && PotionUtils.getPotion(stack).hasInstantEffects();
	}

	public static int getColor(ItemStack stack) {
		CompoundTag compoundtag = stack.getTag();
		if (compoundtag != null && compoundtag.contains("CustomPotionColor", 99)) {
			return compoundtag.getInt("CustomPotionColor");
		} else {
			return PotionUtils.getPotion(stack) == Potions.EMPTY ? 16253176 : PotionUtils.getColor(getContinuousEffects(stack, true));
		}
	}
}