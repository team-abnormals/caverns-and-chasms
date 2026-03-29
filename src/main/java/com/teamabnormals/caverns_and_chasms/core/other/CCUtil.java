package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.events.ProjectileDeflectEvent;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.PartEntity;

import java.util.Optional;
import java.util.function.Predicate;

public class CCUtil {
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

	public static void deflectProjectileRaw(Entity projectile, double xMovement, double yMovement, double zMovement, double x, double y, double z) {
		IDataManager data = (IDataManager) projectile;
		data.setValue(CCDataProcessors.RICOCHETS, data.getValue(CCDataProcessors.RICOCHETS) + 1);
		data.setValue(CCDataProcessors.DEFLECT_X, xMovement);
		data.setValue(CCDataProcessors.DEFLECT_Y, yMovement);
		data.setValue(CCDataProcessors.DEFLECT_Z, zMovement);
		data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
		projectile.setDeltaMovement(Vec3.ZERO);
		projectile.setPos(x, y, z);
		projectile.checkInsideBlocks();
	}

	public static void deflectProjectileRaw(Entity projectile, Vec3 deflectMovement, Vec3 deflectLocation) {
		deflectProjectileRaw(projectile, deflectMovement.x, deflectMovement.y, deflectMovement.z, deflectLocation.x, deflectLocation.y, deflectLocation.z);
	}

	public static boolean deflectProjectile(Level level, Projectile projectile, HitResult hitResult, Vec3 oldMovement, Vec3 deflectMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
		ProjectileDeflectEvent.Pre deflectEventPre = ProjectileDeflectEvent.onProjectileDeflectPre(projectile, hitResult, deflectMovement, deflectLocation, soundEvent);
		if (!deflectEventPre.isCanceled()) {
			ProjectileDeflectEvent.Post deflectEventPost = ProjectileDeflectEvent.onProjectileDeflectPost(projectile, hitResult, deflectEventPre.getDeflectedMovement(), deflectEventPre.getDeflectLocation(), deflectEventPre.getSoundEvent());
			CCUtil.deflectProjectileRaw(projectile, deflectEventPost.getDeflectedMovement(), deflectEventPost.getDeflectLocation());
			playRicochetEffects(level, deflectLocation, oldMovement.reverse().normalize(), oldMovement.lengthSqr(), deflectEventPost.getSoundEvent(), soundEvent == CCSoundEvents.STORAGE_DUCT_DEFLECT.get() ? 0.5F : 1.0F, level.random, false);
			return true;
		}
		return false;
	}

	public static void playRicochetEffects(Level level, Vec3 location, Vec3 normalizedMovement, double speed, SoundEvent soundEvent, float pitchMultiplier, RandomSource random, boolean fromServer) {
		playRicochetSound(level, location, speed, soundEvent, pitchMultiplier);

		for (int i = 0; i < 4; ++i) {
			double d1 = normalizedMovement.x * 0.2D + random.nextGaussian() * 0.05D;
			double d2 = normalizedMovement.y * 0.2D + random.nextGaussian() * 0.05D;
			double d3 = normalizedMovement.z * 0.2D + random.nextGaussian() * 0.05D;
			if (fromServer)
				NetworkUtil.spawnParticle(CCParticleTypes.TIN_SPARK.getId().toString(), location.x, location.y, location.z, d1, d2, d3);
			else
				level.addParticle(CCParticleTypes.TIN_SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
		}
	}

	public static void playRicochetSound(Level level, Vec3 location, double speed, SoundEvent soundEvent, float pitchMultiplier) {
		level.playSound(null, location.x, location.y, location.z, soundEvent, SoundSource.BLOCKS, Math.min((float) speed * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) speed * 0.8F * pitchMultiplier, 1.8F));
	}
}