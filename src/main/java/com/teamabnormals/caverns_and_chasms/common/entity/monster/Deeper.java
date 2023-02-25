package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;

public class Deeper extends Creeper {

	public Deeper(EntityType<? extends Deeper> type, Level worldIn) {
		super(type, worldIn);
		this.explosionRadius = 4;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.ENTITY_DEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_DEEPER_DEATH.get();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity entity) {
			if (entity.getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG))
				amount *= 3.0F;
		}
		return super.hurt(source, amount);
	}

	@Override
	public void explodeCreeper() {
		if (!this.level.isClientSide) {
			Explosion.BlockInteraction mode = CCConfig.COMMON.deepersDropAllBlocks.get() ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.DESTROY;
			Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? mode : Explosion.BlockInteraction.NONE;
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
		return new ItemStack(CCItems.DEEPER_HEAD.get());
	}
}