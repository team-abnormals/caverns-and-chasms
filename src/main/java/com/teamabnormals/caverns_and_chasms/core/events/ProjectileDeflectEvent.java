package com.teamabnormals.caverns_and_chasms.core.events;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * The two events in this class are fired when a projectile is about to be deflected by a Grazer's shell, the Aegis or a block with the {@link CCBlockTags#DEFLECTS_PROJECTILES} tag.
 * They can be used to modify the deflected projectile, change its course and to change the deflect sound.
 * <br>
 * See {@link ProjectileDeflectEvent.Pre} and {@link ProjectileDeflectEvent.Post} for documentation about their differences.
 */
public abstract class ProjectileDeflectEvent extends EntityEvent {
	private final Projectile projectile;
	private final HitResult ray;
	private Vec3 deflectedMovement;
	private Vec3 deflectLocation;
	private SoundEvent soundEvent;

	public ProjectileDeflectEvent(Projectile projectile, HitResult ray, Vec3 deflectedMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
		super(projectile);
		this.projectile = projectile;
		this.ray = ray;
		this.deflectedMovement = deflectedMovement;
		this.deflectLocation = deflectLocation;
		this.soundEvent = soundEvent;
	}

	public static ProjectileDeflectEvent.Pre onProjectileDeflectPre(Projectile projectile, HitResult ray, Vec3 deflectedMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
		ProjectileDeflectEvent.Pre event = new ProjectileDeflectEvent.Pre(projectile, ray, deflectedMovement, deflectLocation, soundEvent);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}

	public static ProjectileDeflectEvent.Post onProjectileDeflectPost(Projectile projectile, HitResult ray, Vec3 deflectedMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
		ProjectileDeflectEvent.Post event = new ProjectileDeflectEvent.Post(projectile, ray, deflectedMovement, deflectLocation, soundEvent);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}

	public Projectile getProjectile() {
		return this.projectile;
	}

	public HitResult getRayTraceResult() {
		return this.ray;
	}

	public Vec3 getDeflectedMovement() {
		return this.deflectedMovement;
	}

	public void setDeflectedMovement(Vec3 deflectedMovement) {
		this.deflectedMovement = deflectedMovement;
	}

	public Vec3 getDeflectLocation() {
		return this.deflectLocation;
	}

	public void setDeflectLocation(Vec3 deflectLocation) {
		this.deflectLocation = deflectLocation;
	}

	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	public void setSoundEvent(SoundEvent soundEvent) {
		this.soundEvent = soundEvent;
	}

	/**
	 * Fired when a projectile is about to be deflected.
	 * <br>
	 * This event is {@link Cancelable} and cancelling it causes the projectile to not deflect.
	 * <br>
	 * If the event is not cancelled, {@link ProjectileDeflectEvent.Post} is fired immediately after.
	 */
	@Cancelable
	public static class Pre extends ProjectileDeflectEvent {
		public Pre(Projectile projectile, HitResult ray, Vec3 deflectedMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
			super(projectile, ray, deflectedMovement, deflectLocation, soundEvent);
		}
	}

	/**
	 * Fired immediately after {@link ProjectileDeflectEvent.Pre} if it was not canceled.
	 * <br>
	 * Despite the name, the event occurs before the projectile is actually deflected.
	 * The main purpose of the event is to allow modifying the deflected projectile without having to worry about the deflection being cancelled.
	 * <br>
	 * This event is not {@link Cancelable}.
	 */
	public static class Post extends ProjectileDeflectEvent {
		public Post(Projectile projectile, HitResult ray, Vec3 deflectedMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
			super(projectile, ray, deflectedMovement, deflectLocation, soundEvent);
		}
	}
}