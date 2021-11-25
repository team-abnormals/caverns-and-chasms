package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class ThrownSpinelPearl extends ThrowableItemProjectile {
	private int ticks;

	public ThrownSpinelPearl(EntityType<? extends ThrownSpinelPearl> entityType, Level level) {
		super(entityType, level);
	}

	public ThrownSpinelPearl(Level level, LivingEntity entity) {
		super(CCEntityTypes.SPINEL_PEARL.get(), entity, level);
		this.ticks = getDefaultTicks();
	}

	public ThrownSpinelPearl(FMLPlayMessages.SpawnEntity spawnEntity, Level level) {
		this(CCEntityTypes.SPINEL_PEARL.get(), level);
	}

	protected Item getDefaultItem() {
		return CCItems.SPINEL_PEARL.get();
	}

	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		Entity owner = this.getOwner();
		entity.hurt(DamageSource.thrown(this, owner), 0.0F);

		if (owner != null) {
			double oldX = owner.getX();
			double oldY = owner.getY();
			double oldZ = owner.getZ();

			owner.teleportTo(entity.getX(), entity.getY(), entity.getZ());
			entity.teleportTo(oldX, oldY, oldZ);
		}
	}

	public void subtractTicks(int ticks) {
		this.ticks -= ticks;
	}

	protected void onHit(HitResult result) {
		super.onHit(result);
		this.doTeleport();
	}

	private int getDefaultTicks() {
		return 60;
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

	public void tick() {
		Entity entity = this.getOwner();
		if (entity instanceof Player && !entity.isAlive()) {
			this.discard();
		} else {
			this.ticks--;
			if (ticks <= 0)
				this.doTeleport();
			super.tick();
		}
	}

	@Nullable
	public Entity changeDimension(ServerLevel level, ITeleporter teleporter) {
		Entity entity = this.getOwner();
		if (entity != null && entity.level.dimension() != level.dimension()) {
			this.setOwner(null);
		}

		return super.changeDimension(level, teleporter);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}