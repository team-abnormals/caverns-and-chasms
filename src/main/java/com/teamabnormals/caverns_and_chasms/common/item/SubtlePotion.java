package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
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
	public List<MobEffectInstance> getEffects() {
		return setSubtleEffects(this.potion.getEffects());
	}

	@Override
	public boolean hasInstantEffects() {
		return this.potion.hasInstantEffects();
	}

	public static ImmutableList<MobEffectInstance> setSubtleEffects(Iterable<MobEffectInstance> list) {
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

		return ImmutableList.copyOf(effects);
	}
}
