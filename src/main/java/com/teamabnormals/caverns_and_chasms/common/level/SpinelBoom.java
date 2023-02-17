package com.teamabnormals.caverns_and_chasms.common.level;

import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class SpinelBoom extends Explosion {
	private final Level level;
	@Nullable
	private final Entity source;
	private final float radius;
	private final double x;
	private final double y;
	private final double z;
	private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();


	public SpinelBoom(Level levelIn, @Nullable Entity sourceIn, double xIn, double yIn, double zIn, float radiusIn) {
		super(levelIn, sourceIn, null, null, xIn, yIn, zIn, radiusIn, false, BlockInteraction.NONE);
		this.level = levelIn;
		this.source = sourceIn;
		this.radius = radiusIn;
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
	}

	@Override
	public void explode() {
		this.level.gameEvent(this.source, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));
	}

	public void applyKnockback() {
		float f = this.radius * 2.0F;
		int x1 = Mth.floor(this.x - f - 1.0D);
		int x2 = Mth.floor(this.x + f + 1.0D);
		int y1 = Mth.floor(this.y - f - 1.0D);
		int y2 = Mth.floor(this.y + f + 1.0D);
		int z1 = Mth.floor(this.z - f - 1.0D);
		int z2 = Mth.floor(this.z + f + 1.0D);
		List<Entity> list = this.level.getEntities(this.source, new AABB(x1, y1, z1, x2, y2, z2));
		ForgeEventFactory.onExplosionDetonate(this.level, this, list, f);
		Vec3 vec3 = new Vec3(this.x, this.y, this.z);

		for (Entity entity : list) {
			if (!entity.ignoreExplosion()) {
				double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f;
				if (d0 <= 1.0D) {
					double d1 = entity.getX() - this.x;
					double d2 = (entity instanceof PrimedTmt ? entity.getY() : entity.getEyeY()) - this.y;
					double d3 = entity.getZ() - this.z;
					double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
					if (d4 != 0.0D) {
						d1 /= d4;
						d2 /= d4;
						d3 /= d4;
						double d5 = getSeenPercent(vec3, entity);
						double d6 = (1.0D - d0) * d5;
						double d7 = d6;
						if (entity instanceof LivingEntity) {
							d7 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d6);
						}

						entity.setDeltaMovement(entity.getDeltaMovement().add(d1 * d7, d2 * d7, d3 * d7));
						if (entity instanceof Player player) {
							if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
								this.hitPlayers.put(player, new Vec3(d1 * d6, d2 * d6, d3 * d6));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void finalizeExplosion(boolean spawnParticles) {
		if (this.level.isClientSide) {
			this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.getRandom().nextFloat() - this.level.getRandom().nextFloat()) * 0.2F) * 0.7F, false);
		}

		if (spawnParticles) {
			this.level.addParticle(CCParticleTypes.SPINEL_BOOM_EMITTER.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public Map<Player, Vec3> getHitPlayers() {
		return this.hitPlayers;
	}
}