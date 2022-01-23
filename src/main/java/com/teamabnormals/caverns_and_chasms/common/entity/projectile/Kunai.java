package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.common.item.silver.AfflictingItem;
import com.teamabnormals.caverns_and_chasms.core.other.CCDamageSources;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class Kunai extends AbstractArrow implements ItemSupplier, AfflictingItem {

	public Kunai(EntityType<? extends Kunai> type, Level worldIn) {
		super(type, worldIn);
	}

	public Kunai(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(CCEntityTypes.KUNAI.get(), world);
	}

	public Kunai(Level worldIn, LivingEntity shooter) {
		super(CCEntityTypes.KUNAI.get(), shooter, worldIn);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		this.causeAfflictionDamage(living);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity target = result.getEntity();
		Entity shooter = this.getOwner();

		float motion = (float) this.getDeltaMovement().length();
		int damage = Mth.ceil(Mth.clamp((double) motion * 0.5F * this.getBaseDamage(), 0.0D, 2.147483647E9D));

		DamageSource damagesource;
		if (shooter == null) {
			damagesource = CCDamageSources.causeKunaiDamage(this, this);
		} else {
			damagesource = CCDamageSources.causeKunaiDamage(this, shooter);
			if (shooter instanceof LivingEntity) {
				((LivingEntity) shooter).setLastHurtMob(target);
			}
		}

		boolean isEnderman = target.getType() == EntityType.ENDERMAN;
		if (this.isOnFire() && !isEnderman) {
			target.setSecondsOnFire(5);
		}

		if (target.hurt(damagesource, (float) damage)) {
			if (isEnderman) return;

			if (target instanceof LivingEntity livingTarget) {

				if (!this.level.isClientSide() && shooter instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(livingTarget, shooter);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) shooter, livingTarget);
				}

				this.doPostHurtEffects(livingTarget);
				if (livingTarget != shooter && livingTarget instanceof Player && shooter instanceof ServerPlayer && !this.isSilent()) {
					((ServerPlayer) shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
				}

				if (!target.isAlive() && this.piercedAndKilledEntities != null) {
					this.piercedAndKilledEntities.add(livingTarget);
				}
			}

			this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		} else {
			target.setRemainingFireTicks(target.getRemainingFireTicks());
			this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
			this.setYRot(this.getYRot() + 180.0F);
			this.yRotO += 180.0F;
			if (!this.level.isClientSide() && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
				if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}

				this.discard();
			}
		}
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public ItemStack getItem() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	protected void tickDespawn() {
		++this.life;
		if (this.life >= 6000) {
			this.discard();
		}
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.WOOD_BREAK;
	}

	@Override
	public double getBaseDamage() {
		return 2.0D;
	}
}