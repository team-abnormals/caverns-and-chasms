package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload.ParticleInstance;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CCProjectileUtil {
	public static final ProjectileDeflection AEGIS_DEFLECT = (projectile, entity, random) -> {
		if (entity != null) {
			Vec3 movement = projectile.getDeltaMovement().normalize();
			Vec3 location = projectile.position();

			ProjectileDeflection.AIM_DEFLECT.deflect(projectile, entity, random);

			SoundEvent deflectSound = decideUsedDeflectSound(projectile, CCSoundEvents.AEGIS_DEFLECT.get());
			incrementRicochetCounter(projectile);
			setBonusDeflect(projectile, false);
			playRicochetEffects(entity.level(), location, movement.reverse().normalize(), movement.length(), deflectSound, random, true);
			entity.level().gameEvent(CCGameEvents.TIN_DEFLECT, location, GameEvent.Context.of(projectile));
		}
	};

	public static final ProjectileDeflection SHIELD_TIN_DEFLECT = (projectile, entity, random) -> {
		if (entity != null) {
			Vec3 movement = projectile.getDeltaMovement();
			Vec3 normal = entity.getLookAngle().normalize();
			Vec3 location = projectile.position();

			deflectAccordingToNormal(projectile, movement, normal, 0.0D, 0.0D);
			projectile.hasImpulse = true;

			SoundEvent deflectSound = decideUsedDeflectSound(projectile, CCSoundEvents.TIN_DEFLECT.get());
			incrementRicochetCounter(projectile);
			setBonusDeflect(projectile, false);
			playRicochetEffects(entity.level(), projectile.position(), movement.reverse().normalize(), movement.length(), deflectSound, random, false);
			entity.level().gameEvent(CCGameEvents.TIN_DEFLECT, location, GameEvent.Context.of(projectile));
		}
	};

	public static final ProjectileDeflection GRAZER_DEFLECT = (projectile, entity, random) -> {
		if (entity instanceof GrazerPart grazerPart) {
			AABB aabb = grazerPart.getBoundingBox().inflate(0.3D);
			Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(grazerPart.getX(), grazerPart.getY(0.5D), grazerPart.getZ()))).orElse(projectile.position());
			Vec3 movement = projectile.getDeltaMovement();
			Vec3 normal = grazerPart.getParent().calculateDeflectionNormal(location);

			deflectAccordingToNormal(projectile, movement, normal, 0.65D, 0.75D);
			projectile.hasImpulse = true;

			SoundEvent deflectSound = decideUsedDeflectSound(projectile, CCSoundEvents.GRAZER_DEFLECT.get());
			incrementRicochetCounter(projectile);
			setBonusDeflect(projectile, false);
			playRicochetEffects(entity.level(), location, movement.reverse().normalize(), movement.length(), deflectSound, random, true);
			entity.level().gameEvent(CCGameEvents.TIN_DEFLECT, location, GameEvent.Context.of(projectile));
		}
	};

	public static EntityHitResult getExaggeratedHitboxEntityHitResult(Entity entity, Vec3 startLoc, Vec3 endLoc, AABB aabb, Predicate<Entity> predicate, double range, boolean returnParent) {
		Level level = entity.level();
		double d0 = range;
		Vec3 vec3 = null;
		Entity entity1 = null;

		for (Entity entity2 : level.getEntities(entity, aabb, predicate)) {
			AABB aabb1 = entity2.getBoundingBox();
			AABB aabb2 = aabb1.inflate(entity2.getPickRadius() + 0.25D + Mth.clamp(startLoc.distanceTo(entity2.position()) * 0.05D, 0.0D, 1.0D));
			Optional<Vec3> optional = aabb2.clip(startLoc, endLoc);
			if (optional.isPresent()) {
				Vec3 vec31 = optional.get();
				double d1 = startLoc.distanceToSqr(vec31);
				if (d1 < d0) {
					Vec3 vec32 = new Vec3(Mth.clamp(vec31.x, aabb1.minX + 0.01D, aabb1.maxX - 0.01D), Mth.clamp(vec31.y, aabb1.minY + 0.01D, aabb1.maxY - 0.01D), Mth.clamp(vec31.z, aabb1.minZ + 0.01D, aabb1.maxZ - 0.01D));
					BlockHitResult hitResult = level.clip(new ClipContext(vec31, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
					if (hitResult.getType() == HitResult.Type.MISS) {
						entity1 = returnParent && entity2 instanceof PartEntity<?> partEntity ? partEntity.getParent() : entity2;
						vec3 = vec31;
						d0 = d1;
					}
				}
			}
		}

		return entity1 == null ? null : new EntityHitResult(entity1, vec3);
	}

	public static void incrementRicochetCounter(Projectile projectile) {
		IDataManager data = (IDataManager) projectile;
		data.setValue(CCDataProcessors.RICOCHETS, data.getValue(CCDataProcessors.RICOCHETS) + 1);
	}

	public static void setBonusDeflect(Projectile projectile, boolean bonusDeflect) {
		IDataManager data = (IDataManager) projectile;
		data.setValue(CCDataProcessors.BONUS_DEFLECT, bonusDeflect);
	}

	public static Pair<Double, Double> decideUsedDeflectFactors(Projectile projectile, double minVerticalFactor, double minHorizontalFactor) {
		IDataManager data = (IDataManager) projectile;
		if (projectile.getType() == CCEntityTypes.RICOCHET_ARROW.get()) {
			return Pair.of(Math.max(0.65D, minVerticalFactor), Math.max(0.75D, minHorizontalFactor));
		} else if (data.getValue(CCDataProcessors.BONUS_DEFLECT)) {
			return Pair.of(Math.max(0.40D, minVerticalFactor), Math.max(0.50D, minHorizontalFactor));
		}
		return Pair.of(minVerticalFactor, minHorizontalFactor);
	}

	public static SoundEvent decideUsedDeflectSound(Projectile projectile, SoundEvent soundEvent) {
		IDataManager data = (IDataManager) projectile;
		if (projectile.getType() == CCEntityTypes.RICOCHET_ARROW.get()) {
			return CCSoundEvents.RICOCHET_ARROW_DEFLECT.get();
		} else if (data.getValue(CCDataProcessors.BONUS_DEFLECT)) {
			return CCSoundEvents.TINPLATE_SECOND_DEFLECT.get();
		}
		return soundEvent;
	}

	public static void deflectAccordingToNormal(Projectile projectile, Vec3 movement, Vec3 normal, double minVerticalFactor, double minHorizontalFactor) {
		Vec3 vComponent = normal.scale(movement.dot(normal));
		Vec3 hComponent = movement.subtract(vComponent);
		Pair<Double, Double> deflectFactors = CCProjectileUtil.decideUsedDeflectFactors(projectile, minVerticalFactor, minHorizontalFactor);
		projectile.setDeltaMovement(vComponent.scale(-deflectFactors.getFirst()).add(hComponent.scale(deflectFactors.getSecond())));
	}

	public static void playRicochetEffects(Level level, Vec3 location, Vec3 sparkDir, double speed, SoundEvent soundEvent, RandomSource random, boolean fromServer) {
		playRicochetSound(level, location, speed, soundEvent);

		List<ParticleInstance> particles = new ArrayList<>();
		for (int i = 0; i < 4; ++i) {
			double d1 = sparkDir.x * 0.2D + random.nextGaussian() * 0.05D;
			double d2 = sparkDir.y * 0.2D + random.nextGaussian() * 0.05D;
			double d3 = sparkDir.z * 0.2D + random.nextGaussian() * 0.05D;
			if (fromServer)
				particles.add(new ParticleInstance(location.x, location.y, location.z, d1, d2, d3));
			else
				level.addParticle(CCParticleTypes.TIN_SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
		}
		if (fromServer) {
			NetworkUtil.spawnParticle((ServerLevel) level, CCParticleTypes.TIN_SPARK.get(), particles);
		}
	}

	public static void playRicochetSound(Level level, Vec3 location, double speed, SoundEvent soundEvent) {
		level.playSound(null, location.x, location.y, location.z, soundEvent, SoundSource.BLOCKS, Math.min((float) speed * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) speed * 0.8F, 1.8F));
	}
}