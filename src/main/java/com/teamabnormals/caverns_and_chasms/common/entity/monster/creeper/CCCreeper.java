package com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper;

import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class CCCreeper extends Creeper {
	public CCCreeper(EntityType<? extends Creeper> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		this.handleSwell();

		int realSwell = this.swell;
		int realOldSwell = this.oldSwell;

		this.swell = -1;

		super.tick();

		this.swell = realSwell;
		this.oldSwell = realOldSwell;
	}

	protected void handleSwell() {
		if (this.isAlive()) {
			this.oldSwell = this.swell;
			if (this.isIgnited()) {
				this.setSwellDir(1);
			}

			int i = this.getSwellDir();
			if (i > 0 && this.swell == 0) {
				this.playSound(this.getPrimedSound(), 1.0F, 0.5F);
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
	}

	@Override
	public void explodeCreeper() {
		if (!this.level().isClientSide && this.isAlive()) {
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			BlockInteraction blockinteraction = !ForgeEventFactory.getMobGriefingEvent(this.level(), this) ? Explosion.BlockInteraction.KEEP : this.level().getGameRules().getBoolean(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
			CustomExplosion.spawnExplosion(this.level(), this, this.getX(), this.getY(), this.getZ(), this.explosionRadius * f, this.isOnFire(), blockinteraction, this.getExplosionSound());
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

	protected SoundEvent getPrimedSound() {
		return SoundEvents.CREEPER_PRIMED;
	}

	protected SoundEvent getExplosionSound() {
		return SoundEvents.GENERIC_EXPLODE;
	}

	protected abstract ItemStack getSkull();
}