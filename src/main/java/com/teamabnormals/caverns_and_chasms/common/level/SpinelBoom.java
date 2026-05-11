package com.teamabnormals.caverns_and_chasms.common.level;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpinelBoom extends Explosion {
	private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
	private final Level level;
	@Nullable
	private final Entity source;
	private final float radius;
	private final double x;
	private final double y;
	private final double z;
	private final ExplosionDamageCalculator damageCalculator;
	private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
	private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

	public SpinelBoom(Level level, @Nullable Entity source, double x, double y, double z, float radius) {
		super(level, source, null, null, x, y, z, radius, false, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, CCSoundEvents.TMT_EXPLODE);
		this.level = level;
		this.source = source;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.z = z;
		this.damageCalculator = this.makeDamageCalculator(source);
	}

	private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity) {
		return (entity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity));
	}

	@Override
	public void explode() {
		this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
		Set<BlockPos> set = Sets.newHashSet();

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d0 = (float) j / 15.0F * 2.0F - 1.0F;
						double d1 = (float) k / 15.0F * 2.0F - 1.0F;
						double d2 = (float) l / 15.0F * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
						double d4 = this.x;
						double d6 = this.y;
						double d8 = this.z;

						for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
							BlockPos blockpos = BlockPos.containing(d4, d6, d8);
							BlockState blockstate = this.level.getBlockState(blockpos);
							FluidState fluidstate = this.level.getFluidState(blockpos);

							if (!this.level.isInWorldBounds(blockpos)) {
								break;
							}

							Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
							if (optional.isPresent()) {
								f -= (optional.get() + 0.3F) * 0.3F;
							}

							if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f) && blockstate.getBlock() instanceof TntBlock) {
								set.add(blockpos);
							}

							d4 += d0 * (double) 0.3F;
							d6 += d1 * (double) 0.3F;
							d8 += d2 * (double) 0.3F;
						}
					}
				}
			}
		}

		this.toBlow.addAll(set);

		float f = this.radius * 2.0F;
		int x1 = Mth.floor(this.x - f - 1.0D);
		int x2 = Mth.floor(this.x + f + 1.0D);
		int y1 = Mth.floor(this.y - f - 1.0D);
		int y2 = Mth.floor(this.y + f + 1.0D);
		int z1 = Mth.floor(this.z - f - 1.0D);
		int z2 = Mth.floor(this.z + f + 1.0D);

		List<Entity> list = this.level.getEntities(this.source, new AABB(x1, y1, z1, x2, y2, z2));
		EventHooks.onExplosionDetonate(this.level, this, list, f);
		Vec3 vec3 = new Vec3(this.x, this.y, this.z);

		for (Entity entity : list) {
			if (!entity.ignoreExplosion(this)) {
				double d11 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f;
				if (d11 <= 1.0) {
					double d5 = entity.getX() - this.x;
					double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
					double d9 = entity.getZ() - this.z;
					double d12 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
					if (d12 != 0.0) {
						d5 /= d12;
						d7 /= d12;
						d9 /= d12;
						double d13 = (1.0 - d11) * (double) getSeenPercent(vec3, entity) * (double) this.damageCalculator.getKnockbackMultiplier(entity);
						double d10;
						if (entity instanceof LivingEntity livingentity) {
							d10 = d13 * (1.0 - livingentity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
						} else {
							d10 = d13;
						}

						d5 *= d10;
						d7 *= d10;
						d9 *= d10;
						Vec3 vec31 = new Vec3(d5, d7, d9);
						vec31 = net.neoforged.neoforge.event.EventHooks.getExplosionKnockback(this.level, this, entity, vec31);
						entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
						entity.hurtMarked = true;
						if (entity instanceof Player player) {
							if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
								this.hitPlayers.put(player, vec31);
							}
						}

						entity.onExplosionHit(this.source);
					}
				}
			}
		}
	}

	@Override
	public void finalizeExplosion(boolean spawnParticles) {
		if (this.level.isClientSide) this.level.playLocalSound(this.x, this.y, this.z, CCSoundEvents.TMT_EXPLODE.value(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);

		boolean flag = this.interactsWithBlocks();
		if (spawnParticles) {
			if (!(this.radius < 2.0F) && flag) this.level.addParticle(CCParticleTypes.SPINEL_BOOM_EMITTER.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			else this.level.addParticle(this.level.random.nextBoolean() ? CCParticleTypes.SPINEL_BOOM_CIRCLE.get() : CCParticleTypes.SPINEL_BOOM_STAR.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
		}

		if (flag) {
			Util.shuffle(this.toBlow, this.level.random);

			for (BlockPos blockpos : this.toBlow) {
				BlockState blockstate = this.level.getBlockState(blockpos);
				this.level.getProfiler().push("explosion_blocks");
				blockstate.onBlockExploded(this.level, blockpos, this);
				this.level.getProfiler().pop();
			}
		}
	}

	@Override
	public void clearToBlow() {
		this.toBlow.clear();
	}

	@Override
	public List<BlockPos> getToBlow() {
		return this.toBlow;
	}

	@Override
	public Map<Player, Vec3> getHitPlayers() {
		return this.hitPlayers;
	}
}