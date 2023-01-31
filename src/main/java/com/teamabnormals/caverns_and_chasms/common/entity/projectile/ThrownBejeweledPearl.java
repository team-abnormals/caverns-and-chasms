package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.common.item.BejeweledPearlItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class ThrownBejeweledPearl extends ThrowableItemProjectile {
	private int life;

	public ThrownBejeweledPearl(EntityType<? extends ThrownBejeweledPearl> entityType, Level level) {
		super(entityType, level);
	}

	public ThrownBejeweledPearl(Level level, LivingEntity entity) {
		super(CCEntityTypes.BEJEWELED_PEARL.get(), entity, level);
	}

	public ThrownBejeweledPearl(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(CCEntityTypes.BEJEWELED_PEARL.get(), level);
	}

	@Override
	protected Item getDefaultItem() {
		return CCItems.BEJEWELED_PEARL.get();
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return this.life;
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.doTeleport();
	}

	private void doTeleport() {
		for (int i = 0; i < 32; ++i) {
			this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
		}

		if (!this.level.isClientSide && !this.isRemoved()) {
			Entity entity = this.getOwner();
			if (entity instanceof ServerPlayer) {
				ServerPlayer player = (ServerPlayer) entity;
				if (player.connection.getConnection().isConnected() && player.level == this.level && !player.isSleeping()) {
					if (entity.isPassenger()) {
						player.dismountTo(this.getX(), this.getY(), this.getZ());
					} else {
						entity.teleportTo(this.getX(), this.getY(), this.getZ());
					}

					entity.teleportTo(this.getX(), this.getY(), this.getZ());
					entity.fallDistance = 0.0F;
					entity.hurt(DamageSource.MAGIC, 2.0F);
				}
			} else if (entity != null) {
				entity.teleportTo(this.getX(), this.getY(), this.getZ());
				entity.fallDistance = 0.0F;
			}

			this.discard();
		}
	}

	@Override
	public void tick() {
		Entity entity = this.getOwner();
		if (entity instanceof Player && !entity.isAlive()) {
			this.discard();
		} else {
			this.life++;
			if (this.life >= BejeweledPearlItem.getMaxLifetime())
				this.doTeleport();
			super.tick();
		}
	}

	@Nullable
	@Override
	public Entity changeDimension(ServerLevel level, ITeleporter teleporter) {
		Entity entity = this.getOwner();
		if (entity != null && entity.level.dimension() != level.dimension()) {
			this.setOwner(null);
		}

		return super.changeDimension(level, teleporter);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Life", this.life);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.life = compound.getInt("Life");
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}