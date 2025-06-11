package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.GrazerRollGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.GrazerRunGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Make sure when you get to saddled grazers that they dont despawn on peaceful
public class Grazer extends Monster {
	private static final EntityDataAccessor<Integer> RUN_PHASE = SynchedEntityData.defineId(Grazer.class, EntityDataSerializers.INT);
	private static final TargetingConditions HIT_TARGETING = TargetingConditions.forCombat().selector(livingentity -> {
		return !livingentity.getType().equals(CCEntityTypes.GRAZER) && livingentity.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
	});

	private final GrazerPart[] parts = new GrazerPart[7];
	private final Set<Entity> pushedThisTick = new HashSet<>();
	private Set<Projectile> deflectedThisTick = new HashSet<>();
	private Set<Projectile> deflectedPrevTick = new HashSet<>();
	private long lastDeflectTick;
	private boolean bouncingBackwards;
	private double bounceHeight;

	public Grazer(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.lookControl = new GrazerLookControl();
		this.moveControl = new GrazerMoveControl();
		this.parts[0] = new GrazerPart(this, true, 12F, 7D, 7D);
		this.parts[1] = new GrazerPart(this, true, 12F, 0D, 7D);
		this.parts[2] = new GrazerPart(this, true, 12F, -7D, 7D);
		this.parts[3] = new GrazerPart(this, false, 15F, 6D, -5.5D);
		this.parts[4] = new GrazerPart(this, true, 12F, -7D, 0D);
		this.parts[5] = new GrazerPart(this, true, 12F, -7D, -7D);
		this.parts[6] = new GrazerPart(this, false, 12F, -7D, -15D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new GrazerRollGoal(this));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new GrazerRunGoal(this, entity -> entity instanceof Player, 8.0D));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RUN_PHASE, 0);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	public static boolean checkGrazerSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return pos.getY() < CCConfig.COMMON.grazerMaxSpawnHeight.get() && Mime.checkUndergroundMonsterSpawnRules(type, level, reason, pos, random);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("State", this.getState().getId());
		compound.putBoolean("BouncingBackwards", this.bouncingBackwards);
		compound.putDouble("BounceHeight", this.bounceHeight);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setState(GrazerState.byId(compound.getInt("State")));
		this.bouncingBackwards = compound.getBoolean("BouncingBackwards");
		this.bounceHeight = compound.getDouble("BounceHeight");
	}

	public GrazerState getState() {
		return GrazerState.byId(this.entityData.get(RUN_PHASE));
	}

	public void setState(GrazerState state) {
		this.entityData.set(RUN_PHASE, state.getId());
		this.setSprinting(state == GrazerState.RUNNING);
		this.setDiscardFriction(state == GrazerState.ROLLING);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.55F;
	}

	@Override
	public int getMaxHeadXRot() {
		return 1;
	}

	@Override
	public int getMaxHeadYRot() {
		return 1;
	}

	@Override
	public float getStepHeight() {
		GrazerState state = this.getState();
		return state == GrazerState.RUNNING ? 1.0F : super.getStepHeight();
	}

	@Override
	protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
		return this.getState() == GrazerState.ROLLING ? 0 : super.calculateFallDamage(fallDistance, damageMultiplier);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	public double shellWidth() {
		return 12D / 16D;
	}

	public double shellRadius() {
		return 13D / 16D;
	}

	public double shellCenterZ() {
		GrazerState state = this.getState();
		return state == GrazerState.ROLLING || state == GrazerState.WIGGLING ? 0D : 7D / 16D;
	}

	public double shellCenterY() {
		GrazerState state = this.getState();
		return state == GrazerState.ROLLING || state == GrazerState.WIGGLING ? this.shellRadius() : 21D / 16D;
	}

	public Vec3 calculateDeflectionNormal(Vec3 location) {
		float f = this.getXRot() * Mth.DEG_TO_RAD;
		float f1 = this.getYRot() * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(), this.shellCenterZ()).yRot(-f1);
		Vec3 vec31 = location.subtract(this.position()).subtract(vec3).yRot(f1).xRot(f).multiply(2D / this.shellWidth(), 1D / this.shellRadius(), 1D / this.shellRadius());

		Vec3 normal;
		double d0 = Math.abs(vec31.x);
		double d1 = Math.abs(vec31.y);
		double d2 = Math.abs(vec31.z);

		if (d0 > d1 && d0 > d2) {
			normal = new Vec3(vec31.x > 0 ? 1.0D : -1.0D, 0.0D, 0.0D);
		} else if (d1 > d0 && d1 > d2) {
			normal = new Vec3(0.0D, vec31.y > 0 ? 1.0D : -1.0D, 0.0D);
		} else {
			normal = new Vec3(0.0D, 0.0D, vec31.z > 0 ? 1.0D : -1.0D);
		}

		return normal.multiply(0.5D * this.shellWidth(), this.shellRadius(), this.shellRadius()).normalize().xRot(f).yRot(-f1);
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void setId(int id) {
		super.setId(id);
		for (int i = 0; i < this.parts.length; i++)
			this.parts[i].setId(id + i + 1);
	}

	@Override
	public PartEntity<?>[] getParts() {
		return this.parts;
	}

	@Override
	public void push(Entity entity) {
		if (!this.pushedThisTick.contains(entity)) {
			super.push(entity);
			this.pushedThisTick.add(entity);
		}
	}

	private void updateDeflectProjectiles() {
		long l = this.level().getGameTime();
		if (this.lastDeflectTick != l) {
			this.deflectedPrevTick = this.deflectedThisTick;
			this.deflectedThisTick = new HashSet<>();
			this.lastDeflectTick = l;
		}
	}

	public boolean projectileJustDeflected(Projectile projectile) {
		this.updateDeflectProjectiles();
		return this.deflectedPrevTick.contains(projectile);
	}

	public void addDeflectedProjectile(Projectile projectile) {
		this.deflectedThisTick.add(projectile);
	}

	@Override
	public void calculateEntityAnimation(boolean isFlying) {
		if (this.getState() == GrazerState.RUNNING_STILL)
			this.updateWalkAnimation(0.2F);
		else
			super.calculateEntityAnimation(isFlying);
	}

	@Override
	public void tick() {
		super.tick();

		this.updateDeflectProjectiles();

		double d0 = this.shellRadius();
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(), this.shellCenterZ()).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
		AABB aabb = new AABB(-d0, -d0, -d0, d0, d0, d0).move(this.position()).move(vec3).inflate(0.3D);
		for (Projectile projectile : this.level().getEntitiesOfClass(Projectile.class, aabb))
			if (this.deflectedPrevTick.contains(projectile))
				this.deflectedThisTick.add(projectile);

		for (GrazerPart part : this.parts)
			part.updatePosition();
	}

	@Override
	public void aiStep() {
		this.pushedThisTick.clear();
		int lerpstepsold = this.lerpSteps;
		float xrotold = this.getXRot();

		super.aiStep();

		if (this.level().isClientSide) {
			if (lerpstepsold > 0)
				this.setXRot((xrotold + (float) Mth.wrapDegrees(this.lerpXRot - (double) xrotold) / (float) lerpstepsold) % 360.0F);
		} else if (this.getState() == GrazerState.ROLLING && this.isAlive()) {
			Vec3 movement = this.getDeltaMovement();
			this.setXRot(Mth.wrapDegrees(this.getXRot() - 15.0F));
			if (this.bouncingBackwards)
				this.setYRot((float) (Mth.atan2(movement.x, -movement.z) * Mth.RAD_TO_DEG));
			else
				this.setYRot((float) (Mth.atan2(-movement.x, movement.z) * Mth.RAD_TO_DEG));

			List<LivingEntity> hitentities = this.level().getNearbyEntities(LivingEntity.class, HIT_TARGETING, this, this.getBoundingBox().inflate(0.55D, 0.0D, 0.55D));
			for (LivingEntity livingentity : hitentities)
				livingentity.hurt(this.level().damageSources().noAggroMobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		}

		for (GrazerPart part : this.parts)
			part.pushEntities();
	}

	@Override
	public void move(MoverType moverType, Vec3 movement) {
		double xold = this.getX();
		double yold = this.getY();
		double zold = this.getZ();
		Vec3 oldmotion = this.getDeltaMovement();

		super.move(moverType, movement);

		GrazerState state = this.getState();
		if (!this.level().isClientSide && this.isAlive() && !this.noPhysics && (state == GrazerState.RUNNING || state == GrazerState.ROLLING)) {
			Vec3 newmotion = oldmotion;
			boolean ricocheted = false;
			boolean horizontal = false;

			if (xold + movement.x != this.getX()) {
				newmotion = new Vec3(-oldmotion.x, oldmotion.y, oldmotion.z);
				ricocheted = true;
				horizontal = true;
			}
			if (this.getState() == GrazerState.ROLLING) {
				double d0 = yold + movement.y;
				if (d0 > this.getY()) {
					newmotion = new Vec3(oldmotion.x, -oldmotion.y, oldmotion.z);
					ricocheted = true;
				} else if (d0 < this.getY()) {
					if (this.bounceHeight < 0.4D) {
						this.setState(GrazerState.WIGGLING);
					} else {
						this.bounceHeight *= 0.94D;
						newmotion = new Vec3(oldmotion.x, this.bounceHeight, oldmotion.z);
						ricocheted = true;
					}
				}
			}
			if (zold + movement.z != this.getZ()) {
				newmotion = new Vec3(oldmotion.x, oldmotion.y, -oldmotion.z);
				ricocheted = true;
				horizontal = true;
			}

			if (ricocheted) {
				if (horizontal) {
					double randomangle = this.random.nextDouble() - 0.5D;
					newmotion = new Vec3(newmotion.x * Math.cos(randomangle) - newmotion.z * Math.sin(randomangle), newmotion.y, newmotion.x * Math.sin(randomangle) + newmotion.z * Math.cos(randomangle));
				}

				if (this.getState() != GrazerState.ROLLING) {
					this.setState(GrazerState.ROLLING);
					this.bounceHeight = 0.8D;
					this.bouncingBackwards = true;
					newmotion = newmotion.normalize().multiply(0.55D, 0.0D, 0.55D).add(0.0D, this.bounceHeight, 0.0D);
				} else {
					if (oldmotion.x * newmotion.x + oldmotion.z * newmotion.z < 0.0D)
						this.bouncingBackwards = !this.bouncingBackwards;
				}

				this.setDeltaMovement(newmotion);

				double speed = newmotion.lengthSqr();
				this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CCSoundEvents.TIN_DEFLECT.get(), SoundSource.BLOCKS, Math.min((float) speed * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) speed * 0.8F, 1.8F));
			}
		}
	}

	@Override
	public MoveControl getMoveControl() {
		return this.moveControl;
	}

	private class GrazerLookControl extends LookControl {
		public GrazerLookControl() {
			super(Grazer.this);
		}

		public void tick() {
			if (this.lookAtCooldown > 0) {
				--this.lookAtCooldown;
				if (Grazer.this.getState() != GrazerState.ROLLING) {
					this.getYRotD().ifPresent((rot) -> {
						Grazer.this.setYRot(this.rotateTowards(Grazer.this.getYRot(), rot, this.yMaxRotSpeed));
					});
				}
			}

			Grazer.this.yHeadRot = Grazer.this.getYRot();
			Grazer.this.yBodyRot = Grazer.this.getYRot();
		}
	}

	private class GrazerMoveControl extends MoveControl {
		public GrazerMoveControl() {
			super(Grazer.this);
		}

		@Override
		public void tick() {
			if (Grazer.this.getState() == GrazerState.RUNNING) {
				Grazer.this.setSpeed(0.5F);
			} else {
				super.tick();
			}
		}
	}
}