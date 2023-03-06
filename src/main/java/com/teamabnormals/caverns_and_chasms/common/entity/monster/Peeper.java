package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.PeeperSwellGoal;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class Peeper extends Creeper {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("06ecce1b-07a2-4f68-ba2c-8b90cb8bc7a3");

	public Peeper(EntityType<? extends Peeper> type, Level worldIn) {
		super(type, worldIn);
		this.maxSwell = 20;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PeeperFreezeWhenTargetStill(this));
		this.goalSelector.addGoal(2, new PeeperSwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.ENTITY_DEEPER_HURT.get();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.28D);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_DEEPER_DEATH.get();
	}

	@Override
	public void tick() {
		if (this.isAlive()) {
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
		}
		super.tick();
	}

	@Override
	public void explodeCreeper() {
		if (!this.level.isClientSide) {
			Explosion.BlockInteraction explosion$mode = ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, this.isOnFire(), explosion$mode);
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

	static class PeeperFreezeWhenTargetStill extends Goal {
		private final Peeper peeper;
		@Nullable
		private LivingEntity target;

		public PeeperFreezeWhenTargetStill(Peeper peeper) {
			this.peeper = peeper;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		public boolean canUse() {
			this.target = this.peeper.getTarget();
			if (this.target == null || !target.isAlive())
				return true;
			return target instanceof MovingPlayer player && !player.isMoving();
		}

		public void start() {
			this.peeper.getNavigation().stop();
		}

		public void tick() {
			if (this.target != null)
				this.peeper.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
		}
	}
}