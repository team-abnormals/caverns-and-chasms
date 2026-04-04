package com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.EvendeeperSwellGoal;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Evendeeper extends Deeper {
	public Evendeeper(EntityType<? extends Evendeeper> type, Level level) {
		super(type, level);
		this.goalSelector.removeAllGoals(goal -> goal instanceof SwellGoal);
		this.goalSelector.addGoal(2, new EvendeeperSwellGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.22D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.EVENDEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.EVENDEEPER_DEATH.get();
	}

	@Override
	protected SoundEvent getPrimedSound() {
		return CCSoundEvents.EVENDEEPER_PRIMED.get();
	}

	@Override
	protected SoundEvent getExplosionSound() {
		return CCSoundEvents.EVENDEEPER_EXPLODE.get();
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(CCItems.EVENDEEPER_HEAD.get());
	}
}