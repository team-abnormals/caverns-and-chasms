package com.teamabnormals.caverns_and_chasms.common.level;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
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
	private final Explosion.BlockInteraction blockInteraction;
	private final ExplosionDamageCalculator damageCalculator;
	private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
	private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

	public SpinelBoom(Level level, @Nullable Entity source, double x, double y, double z, float radius) {
		this(level, source, x, y, z, radius, BlockInteraction.DESTROY);
	}

	public SpinelBoom(Level level, @Nullable Entity source, double x, double y, double z, float radius, BlockInteraction blockInteraction) {
		super(level, source, null, null, x, y, z, radius, false, blockInteraction);
		this.level = level;
		this.source = source;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockInteraction = blockInteraction;
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
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f);
		Vec3 vec3 = new Vec3(this.x, this.y, this.z);

		for (int i = 0; i < list.size(); ++i) {
			Entity entity = list.get(i);
			if (!entity.ignoreExplosion()) {
				double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f;
				if (d0 <= 1.0D) {
					double d1 = entity.getX() - this.x;
					double d2 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
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
						entity.hurtMarked = true;
						if (entity instanceof Player) {
							Player player = (Player) entity;
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
		if (this.level.isClientSide)
			this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);

		boolean flag = this.interactsWithBlocks();
		if (spawnParticles) {
			if (!(this.radius < 2.0F) && flag)
				this.level.addParticle(CCParticleTypes.SPINEL_BOOM_EMITTER.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
			else
				this.level.addParticle(this.level.random.nextBoolean() ? CCParticleTypes.SPINEL_BOOM_CIRCLE.get() : CCParticleTypes.SPINEL_BOOM_STAR.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
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