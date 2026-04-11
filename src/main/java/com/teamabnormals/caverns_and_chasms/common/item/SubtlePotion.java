package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.other.CCEnums;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

import java.util.ArrayList;
import java.util.List;

public class SubtlePotion extends Potion {
	private final Potion potion;

	public SubtlePotion(Potion potion) {
		super(potion.name, potion.effects.toArray(new MobEffectInstance[0]));
		this.potion = potion;
	}

	public Potion getPotion() {
		return this.potion;
	}

	@Override
	public boolean hasInstantEffects() {
		return this.potion.hasInstantEffects();
	}

	public static Iterable<MobEffectInstance> setSubtleEffects(Iterable<MobEffectInstance> list) {
		ArrayList<MobEffectInstance> effects = new ArrayList<>();
		list.forEach(effect -> {
			MobEffectInstance clone = new MobEffectInstance(
					effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), false,
					effect.showIcon(), null
			);
			effect.getEffect().value().fillEffectCures(effect.getCures(), clone);
			((SubtleMobEffectInstance) clone).setSubtle(true);

			effects.add(clone);
		});

		return effects;
	}

	public static Iterable<MobEffectInstance> updateEffects(ItemStack stack, Iterable<MobEffectInstance> list) {
		if (stack.has(CCDataComponents.SUBTLE)) {
			return setSubtleEffects(list);
		} else {
			return list;
		}
	}

	public static Iterable<MobEffectInstance> getAllEffects(ItemStack stack) {
		if (stack.has(DataComponents.POTION_CONTENTS)) {
			return updateEffects(stack, stack.get(DataComponents.POTION_CONTENTS).getAllEffects());
		}
		return new ArrayList<>();
	}

	public static boolean isSubtle(ItemStack stack) {
		return stack.has(DataComponents.POTION_CONTENTS) && stack.has(CCDataComponents.SUBTLE);
	}

	public static ItemStack setSubtle(ItemStack stack) {
		stack.set(CCDataComponents.SUBTLE, Unit.INSTANCE);
		stack.set(DataComponents.RARITY, CCEnums.FANCY.getValue());
		return stack;
	}
}
