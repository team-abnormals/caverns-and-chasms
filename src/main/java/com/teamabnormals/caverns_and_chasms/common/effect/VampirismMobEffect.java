package com.teamabnormals.caverns_and_chasms.common.effect;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class VampirismMobEffect extends MobEffect {

	public VampirismMobEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x971B21);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		Level level = entity.level();
		for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(0.3D))) {
			if (living.isAlive() && living != entity && level.getGameTime() % 5 == 0) {
				float damage = (amplifier + 1);
				living.hurt(CCDamageTypes.draining(level, entity), damage);
				entity.heal(damage * 0.5F);
				entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), CCSoundEvents.DRAIN.get(), entity.getSoundSource(), 1.0F, 1.0F);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}