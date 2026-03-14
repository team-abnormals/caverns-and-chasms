package com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.PeeperSwellGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

public class Peeper extends CCCreeper {
	private static final UUID FREEZE_MODIFIER_UUID = UUID.fromString("113f0691-d920-423d-acd2-9ca0c577991f");
	private static final UUID SPEED_UP_MODIFIER_UUID = UUID.fromString("6866925d-f410-42b9-b2f2-7a22c60a6380");
	private static final AttributeModifier FREEZE_MODIFIER = new AttributeModifier(FREEZE_MODIFIER_UUID, "Peeper frozen", -100.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);

	private int followingTicks;

	public Peeper(EntityType<? extends Peeper> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PeeperSwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CCSoundEvents.PEEPER_PULSE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.PEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.PEEPER_DEATH.get();
	}

	@Override
	protected SoundEvent getPrimedSound() {
		return CCSoundEvents.PEEPER_PRIMED.get();
	}

	@Override
	protected SoundEvent getExplosionSound() {
		return CCSoundEvents.PEEPER_EXPLODE.get();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.FOLLOW_RANGE, 50.0D);
	}

	public static boolean checkPeeperSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return pos.getY() < CCConfig.COMMON.peeperMaxSpawnHeight.get() && Mime.checkUndergroundMonsterSpawnRules(type, level, reason, pos, random);
	}

	@Override
	public void tick() {
		if (this.isAlive()) {
			AttributeInstance speedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
			if (speedAttribute.getModifier(FREEZE_MODIFIER_UUID) != null) {
				speedAttribute.removeModifier(FREEZE_MODIFIER_UUID);
			}

			if (this.getTarget() instanceof MovingPlayer player) {
				if (!player.isMoving()) {
					speedAttribute.addTransientModifier(FREEZE_MODIFIER);
					this.getLookControl().setLookAt(this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ());
				} else {
					this.followingTicks++;
					speedAttribute.removeModifier(SPEED_UP_MODIFIER_UUID);
					speedAttribute.addTransientModifier(new AttributeModifier(SPEED_UP_MODIFIER_UUID, "Peeper speed boost", Math.min(this.followingTicks * 0.0004D, 0.23D), Operation.ADDITION));
				}
			}

			if (this.getTarget() == null) {
				speedAttribute.removeModifier(SPEED_UP_MODIFIER_UUID);
			}
		}

		super.tick();
	}

	@Override
	protected void handleSwell() {
		if (this.isAlive()) {
			super.handleSwell();
		} else if (this.swell > 0) {
			this.swell--;
		}
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (target instanceof ServerPlayer player) {
			CCCriteriaTriggers.SPOTTED_BY_PEEPER.trigger(player);
		}
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(CCItems.PEEPER_HEAD.get());
	}

	@Override
	public boolean canDropMobsSkull() {
		return this.isPowered();
	}
}