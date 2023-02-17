package com.teamabnormals.caverns_and_chasms.common.level;

import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
		float f2 = this.radius * 2.0F;
		int k1 = Mth.floor(this.x - (double) f2 - 1.0D);
		int l1 = Mth.floor(this.x + (double) f2 + 1.0D);
		int i2 = Mth.floor(this.y - (double) f2 - 1.0D);
		int i1 = Mth.floor(this.y + (double) f2 + 1.0D);
		int j2 = Mth.floor(this.z - (double) f2 - 1.0D);
		int j1 = Mth.floor(this.z + (double) f2 + 1.0D);
		List<Entity> list = this.level.getEntities(this.source, new AABB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
		Vec3 vec3 = new Vec3(this.x, this.y, this.z);

		for (int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if (!entity.ignoreExplosion()) {
				double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
				if (d12 <= 1.0D) {
					double d5 = entity.getX() - this.x;
					double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
					double d9 = entity.getZ() - this.z;
					double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
					if (d13 != 0.0D) {
						d5 /= d13;
						d7 /= d13;
						d9 /= d13;
						double d14 = (double) getSeenPercent(vec3, entity);
						double d10 = (1.0D - d12) * d14;
						double d11 = d10;
						if (entity instanceof LivingEntity) {
							d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
						}

						entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
						if (entity instanceof Player player) {
							if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
								this.hitPlayers.put(player, new Vec3(d5 * d10, d7 * d10, d9 * d10));
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
			RandomSource random = this.level.getRandom();
			float amount = random.nextFloat() - random.nextFloat();
			this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + amount * 0.2F) * 0.7F, false);
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