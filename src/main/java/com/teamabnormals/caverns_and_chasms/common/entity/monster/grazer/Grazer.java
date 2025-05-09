package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.HashSet;
import java.util.Set;

public class Grazer extends Monster {
	private static final EntityDataAccessor<Integer> RUN_PHASE = SynchedEntityData.defineId(Grazer.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> ROLL_ANGLE = SynchedEntityData.defineId(Grazer.class, EntityDataSerializers.FLOAT);

	private final GrazerPart[] parts = new GrazerPart[7];
	private final Set<Entity> pushedThisTick = new HashSet<>();
	private Set<Projectile> deflectedThisTick = new HashSet<>();
	private Set<Projectile> deflectedPrevTick = new HashSet<>();
	private long lastDeflectTick;
	private float rollAngleO;

	public Grazer(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.lookControl = new GrazerLookControl();
		this.moveControl = new GrazerMoveControl();
		this.parts[0] = new GrazerPart(this, true, 12F, 6D, 7D);
		this.parts[1] = new GrazerPart(this, true, 12F, -1D, 7D);
		this.parts[2] = new GrazerPart(this, true, 12F, -8D, 7D);
		this.parts[3] = new GrazerPart(this, false, 15F, 5D, -5.5D);
		this.parts[4] = new GrazerPart(this, true, 12F, -8D, 0D);
		this.parts[5] = new GrazerPart(this, true, 12F, -8D, -7D);
		this.parts[6] = new GrazerPart(this, false, 12F, -8D, -15D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		// this.goalSelector.addGoal(1, new GrazerRunGoal(this, entity -> entity instanceof Player, 8.0D, 0.5F));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RUN_PHASE, 0);
		this.entityData.define(ROLL_ANGLE, 0.0F);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("RunPhase", this.getRunPhase().getId());
		compound.putFloat("RollAngle", this.getRollAngle());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setRunPhase(GrazerRunPhase.byId(compound.getInt("RunPhase")));
		this.setRollAngle(compound.getFloat("RollAngle"));
		this.rollAngleO = this.getRollAngle();
	}

	public GrazerRunPhase getRunPhase() {
		return GrazerRunPhase.byId(this.entityData.get(RUN_PHASE));
	}

	public void setRunPhase(GrazerRunPhase phase) {
		this.entityData.set(RUN_PHASE, phase.getId());
		this.setSprinting(phase == GrazerRunPhase.RUNNING);
	}

	public float getRollAngle() {
		return this.entityData.get(ROLL_ANGLE);
	}

	public void setRollAngle(float angle) {
		this.entityData.set(ROLL_ANGLE, angle);
	}

	public float getRollAnim(float partialTick) {
		return Mth.rotLerp(partialTick, this.rollAngleO, this.getRollAngle());
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
		GrazerRunPhase runphase = this.getRunPhase();
		return (runphase == GrazerRunPhase.RUNNING || runphase == GrazerRunPhase.ROLLING) ? 1.0F : super.getStepHeight();
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
		return 8D / 16D;
	}

	public double shellCenterY() {
		return 21D / 16D;
	}

	public Vec3 calculateDeflectionNormal(Vec3 location) {
		float f = this.getRollAngle() * Mth.DEG_TO_RAD;
		float f1 = this.getYRot() * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(), this.shellCenterZ()).yRot(-f1);
		Vec3 vec31 = location.subtract(this.position()).subtract(vec3).yRot(f1).xRot(-f).multiply(2D / this.shellWidth(), 1D / this.shellRadius(), 1D / this.shellRadius());

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

		super.aiStep();

		float currentangle = this.getRollAngle();
		this.rollAngleO = currentangle;

		/*
		if (this.getRunPhase() == GrazerRunPhase.ROLLING)
			this.setRollAngle(currentangle - (float) Mth.length(this.getX() - this.xo, this.getZ() - this.zo) * 40.0F);

		 */
		if (this.getRunPhase() == GrazerRunPhase.RUNNING || this.getRunPhase() == GrazerRunPhase.ROLLING)
			this.setRollAngle(currentangle - 10.0F);

		for (GrazerPart part : this.parts)
			part.pushEntities();
	}

	@Override
	public void move(MoverType moverType, Vec3 movement) {
		double xOriginal = this.getX();
		double zOriginal = this.getZ();
		super.move(moverType, movement);
		GrazerRunPhase runphase = this.getRunPhase();
		if (!this.noPhysics && (runphase == GrazerRunPhase.RUNNING || runphase == GrazerRunPhase.ROLLING)) {
			boolean ricocheted = false;

			if (xOriginal + movement.x != this.getX()) {
				this.setYRot(-this.getYRot() + (this.random.nextFloat() - this.random.nextFloat()) * 40.0F);
				ricocheted = true;
			}
			if (zOriginal + movement.z != this.getZ()) {
				this.setYRot(180.0F - this.getYRot() + (this.random.nextFloat() - this.random.nextFloat()) * 40.0F);
				ricocheted = true;
			}

			if (ricocheted)
				this.setRunPhase(GrazerRunPhase.ROLLING);
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
			if (this.resetXRotOnTick()) {
				Grazer.this.setXRot(0.0F);
			}

			if (this.lookAtCooldown > 0) {
				--this.lookAtCooldown;
				this.getYRotD().ifPresent((p_287447_) -> {
					Grazer.this.setYRot(this.rotateTowards(Grazer.this.getYRot(), p_287447_, this.yMaxRotSpeed));
				});
				this.getXRotD().ifPresent((p_289400_) -> {
					Grazer.this.setXRot(this.rotateTowards(Grazer.this.getXRot(), p_289400_, this.xMaxRotAngle));
				});
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
			if (Grazer.this.getRunPhase() == GrazerRunPhase.RUNNING || Grazer.this.getRunPhase() == GrazerRunPhase.ROLLING) {
				// Grazer.this.setSpeed(0.5F);
				Grazer.this.setSpeed(0.0F);
			} else {
				super.tick();
			}
		}
	}
}