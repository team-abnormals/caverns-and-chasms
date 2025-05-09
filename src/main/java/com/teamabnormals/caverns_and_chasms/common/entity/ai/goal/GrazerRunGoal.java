package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerRunPhase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GrazerRunGoal extends Goal {
	private final Grazer grazer;
	private final Level level;
	private final float speed;
	private final double range;
	private final TargetingConditions avoidEntityTargeting;

	private int cooldown;

	public GrazerRunGoal(Grazer grazer, Predicate<LivingEntity> avoidPredicate, double range, float speed) {
		this.grazer = grazer;
		this.range = range;
		this.speed = speed;
		this.level = grazer.level();
		this.avoidEntityTargeting = TargetingConditions.forCombat().range(range).selector(avoidPredicate);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.grazer.getRunPhase() != GrazerRunPhase.DEFAULT) {
			return true;
		} else if (this.cooldown-- > 0) {
			return false;
		} else if (this.grazer.getLastHurtByMob() != null || this.grazer.isFreezing() || this.grazer.isOnFire() || this.isNearEntityToAvoid()) {
			return true;
		}

		return false;
	}

	private boolean isNearEntityToAvoid() {
		return this.grazer.level().getNearestEntity(LivingEntity.class, this.avoidEntityTargeting, this.grazer, this.grazer.getX(), this.grazer.getY(), this.grazer.getZ(), this.grazer.getBoundingBox().inflate(this.range, 4.0D, this.range)) != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.grazer.getRunPhase() != GrazerRunPhase.DEFAULT;
	}

	@Override
	public void start() {
		this.grazer.getNavigation().stop();
		this.grazer.setRunPhase(GrazerRunPhase.RUNNING);
	}

	@Override
	public void stop() {
		this.grazer.setRunPhase(GrazerRunPhase.DEFAULT);
	}

	@Override
	public void tick() {
		/*
		List<LivingEntity> list = this.level.getNearbyEntities(LivingEntity.class, this.ramTargeting, this, this.grazer.getBoundingBox());
		for (LivingEntity livingentity : list) {
			livingentity.hurt(this.level.damageSources().noAggroMobAttack(this.grazer), (float) this.grazer.getAttributeValue(Attributes.ATTACK_DAMAGE));
			int i = p_217367_.hasEffect(MobEffects.MOVEMENT_SPEED) ? p_217367_.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
			int j = p_217367_.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? p_217367_.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
			float f = 0.25F * (float) (i - j);
			float f1 = Mth.clamp(p_217367_.getSpeed() * 1.65F, 0.2F, 3.0F) + f;
			float f2 = livingentity.isDamageSourceBlocked(p_217366_.damageSources().mobAttack(p_217367_)) ? 0.5F : 1.0F;
			livingentity.knockback((double) (f2 * f1) * this.getKnockbackForce.applyAsDouble(p_217367_), this.ramDirection.x(), this.ramDirection.z());
		}
		*/
	}
}