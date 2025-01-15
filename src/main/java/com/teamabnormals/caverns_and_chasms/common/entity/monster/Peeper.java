package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.PeeperSwellGoal;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.UUID;

public class Peeper extends Creeper {
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
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.DEEPER_HURT.get();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.FOLLOW_RANGE, 50.0D);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.DEEPER_DEATH.get();
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

			this.oldSwell = this.swell;
			if (this.isIgnited()) {
				this.setSwellDir(1);
			}

			int i = this.getSwellDir();
			if (i > 0 && this.swell == 0) {
				this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
				this.gameEvent(GameEvent.PRIME_FUSE);
			}

			this.swell += i;
			if (this.swell < 0) {
				this.swell = 0;
			}

			if (this.swell >= this.maxSwell) {
				this.swell = this.maxSwell;
				this.explodeCreeper();
			}
		} else if (this.swell > 0) {
			this.swell--;
		}
		super.tick();
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (target instanceof ServerPlayer player) {
			CCCriteriaTriggers.SPOTTED_BY_PEEPER.trigger(player);
		}
	}

	@Override
	public void explodeCreeper() {
		if (!this.level().isClientSide && this.isAlive()) {
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, this.isOnFire(), CCConfig.COMMON.creepersDropAllBlocks.get() ? ExplosionInteraction.TNT : ExplosionInteraction.MOB);
			this.discard();
			this.spawnLingeringCloud();
		}
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int p_34292_, boolean p_34293_) {
		for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
			ItemStack itemstack = this.getItemBySlot(equipmentslot);
			float f = this.getEquipmentDropChance(equipmentslot);
			boolean flag = f > 1.0F;
			if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (p_34293_ || flag) && Math.max(this.random.nextFloat() - (float) p_34292_ * 0.01F, 0.0F) < f) {
				if (!flag && itemstack.isDamageableItem()) {
					itemstack.setDamageValue(itemstack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
				}

				this.spawnAtLocation(itemstack);
				this.setItemSlot(equipmentslot, ItemStack.EMPTY);
			}
		}

		Entity entity = source.getEntity();
		if (entity instanceof Creeper creeper) {
			if (creeper.canDropMobsSkull()) {
				ItemStack itemstack = this.getSkull();
				if (!itemstack.isEmpty()) {
					creeper.increaseDroppedSkulls();
					this.spawnAtLocation(itemstack);
				}
			}
		}
	}

	protected ItemStack getSkull() {
		return new ItemStack(CCItems.PEEPER_HEAD.get());
	}
}