package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.google.common.base.Predicates;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBeStupidGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBounceGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerFloatGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerRunGoal;
import com.teamabnormals.caverns_and_chasms.core.other.CCUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGrazer extends Animal {
	private static final float BOUNCE_ROT_SPEED = 15.0F;
	private static final byte FLAP_WINGS_ANIM = 6;
	private static final byte BABY_WIGGLE_LEGS_ANIM = 7;
	private static final byte VOCALIZE_ANIM = 8;

	private static final EntityDimensions BOUNCING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.625F);
	private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(1.8F, 1.98F);

	private static final TargetingConditions HIT_TARGETING = TargetingConditions.forCombat().selector(livingentity -> {
		return livingentity.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox()) && !livingentity.isPassenger();
	});

	private static final EntityDataAccessor<Byte> STATE = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> BODY_LOWER_AMOUNT = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> CUSTOM_X_ROT = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> TARGET_X_ROT = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.FLOAT);

	private final GrazerPart[] parts = new GrazerPart[7];

	private final Set<Entity> pushedThisTick = new HashSet<>();
	private Set<Projectile> deflectedThisTick = new HashSet<>();
	private Set<Projectile> deflectedPrevTick = new HashSet<>();
	private long lastDeflectTick;

	private boolean bouncingBackwards;
	protected double bounceHeight;
	private float bodyLowerAmountO;
	public float customXRotO;

	private float passengerBounceAmount;

	private float runAmount;
	private float runAmountO;
	private float bounceAmount;
	private float bounceAmountO;
	private float wiggleAmount;
	private float wiggleAmountO;
	private float onBackAmount;
	private float onBackAmountO;
	private float beStupidAmount;
	private float beStupidAmountO;
	private float vocalizeAmount;
	private float vocalizeAmountO;
	private float babyWiggleLegsAmount;
	private float babyWiggleLegsAmountO;

	private int wingFlapAnim;
	private int wingFlapAnimO;
	private int vocalizeTime;
	private int babyWiggleLegsTime;

	public AbstractGrazer(EntityType<? extends Animal> type, Level level) {
		super(type, level);
		this.lookControl = new GrazerLookControl();
		this.moveControl = new GrazerMoveControl();
		this.parts[0] = new GrazerPart(this, 12F, 7D, 7D);
		this.parts[1] = new GrazerPart(this, 12F, 0D, 7D);
		this.parts[2] = new GrazerPart(this, 12F, -7D, 7D);
		this.parts[3] = new GrazerHeadPart(this, 15F, 22F, 6D, -5.5D, 10D, -10D);
		this.parts[4] = new GrazerPart(this, 12F, -7D, 0D);
		this.parts[5] = new GrazerPart(this, 12F, -7D, -7D);
		this.parts[6] = new GrazerLegsPart(this, 12F, -7D, -15D, -9D);
		if (!level.isClientSide) {
			this.reassessAgeGoals();
		}
	}

	protected void reassessAgeGoals() {
		this.goalSelector.removeAllGoals(Predicates.alwaysTrue());

		if (!this.isBaby()) {
			this.goalSelector.addGoal(0, new GrazerFloatGoal(this));
			this.goalSelector.addGoal(1, new GrazerBounceGoal(this));
			this.goalSelector.addGoal(2, new GrazerRunGoal(this, entity -> entity instanceof Player, 8.0D));
			this.goalSelector.addGoal(3, new GrazerBeStupidGoal(this));
			this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
			this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STATE, (byte) 0);
		this.entityData.define(BODY_LOWER_AMOUNT, 0.0F);
		this.entityData.define(CUSTOM_X_ROT, 0.0F);
		this.entityData.define(TARGET_X_ROT, 0.0F);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 26.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CCSoundEvents.GRAZER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return CCSoundEvents.GRAZER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.GRAZER_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		if (!this.isBaby()) {
			this.playSound(CCSoundEvents.GRAZER_STEP.get(), 1.0F, 1.0F);
		}
	}

	@Override
	public void playAmbientSound() {
		if (this.getState() == GrazerState.DEFAULT) {
			super.playAmbientSound();
			if (!this.level().isClientSide)
				this.level().broadcastEntityEvent(this, VOCALIZE_ANIM);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getState().getsSaved())
			compound.putByte("State", this.getState().getId());
		compound.putFloat("BodyLowerAmount", this.getBodyLowerAmount());
		compound.putFloat("CustomXRot", this.getCustomXRot());
		compound.putFloat("TargetXRot", this.getTargetXRot());
		compound.putBoolean("BouncingBackwards", this.bouncingBackwards);
		compound.putDouble("BounceHeight", this.bounceHeight);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		GrazerState state = GrazerState.byId(compound.getByte("State"));
		if (state.getsSaved())
			this.setState(state);
		this.setBodyLowerAmount(compound.getFloat("BodyLowerAmount"));
		this.setCustomXRot(compound.getFloat("CustomXRot"));
		this.setTargetXRot(compound.getFloat("TargetXRot"));
		this.bouncingBackwards = compound.getBoolean("BouncingBackwards");
		this.bounceHeight = compound.getDouble("BounceHeight");
		this.reassessAgeGoals();
	}

	public GrazerState getState() {
		return GrazerState.byId(this.entityData.get(STATE));
	}

	public void setState(GrazerState state) {
		this.entityData.set(STATE, state.getId());
		this.setSprinting(state == GrazerState.RUNNING);
		this.setDiscardFriction(this.isBouncingState(state));
	}

	public boolean isBouncingState(GrazerState state) {
		return state == GrazerState.BOUNCING || state == GrazerState.LANDING;
	}

	public boolean isIdleState(GrazerState state) {
		return state == GrazerState.DEFAULT || state == GrazerState.BEING_STUPID;
	}

	public boolean canMove() {
		GrazerState state = this.getState();
		return !this.isBaby() && !this.isBouncingState(state) && state != GrazerState.WIGGLING && state != GrazerState.FLIPPING_OVER;
	}

	public double getBounceHeight() {
		return this.bounceHeight;
	}

	public void setBounceHeight(double height) {
		this.bounceHeight = height;
	}

	public boolean shouldShowDroolModel() {
		GrazerState state = this.getState();
		return !this.isBouncingState(state) && state != GrazerState.WIGGLING && state != GrazerState.FLIPPING_OVER;
	}

	public float getBodyLowerAmount() {
		return this.entityData.get(BODY_LOWER_AMOUNT);
	}

	public void setBodyLowerAmount(float amount) {
		this.entityData.set(BODY_LOWER_AMOUNT, amount);
	}

	public float getCustomXRot() {
		return this.entityData.get(CUSTOM_X_ROT);
	}

	public void setCustomXRot(float rot) {
		this.entityData.set(CUSTOM_X_ROT, rot);
	}

	public float getTargetXRot() {
		return this.entityData.get(TARGET_X_ROT);
	}

	public void setTargetXRot(float rot) {
		this.entityData.set(TARGET_X_ROT, rot);
	}

	public boolean isBouncingBackwards() {
		return this.bouncingBackwards;
	}

	public void setBouncingBackwards(boolean bouncingBackwards) {
		this.bouncingBackwards = bouncingBackwards;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (STATE.equals(key))
			this.refreshDimensions();
		super.onSyncedDataUpdated(key);
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
		for (GrazerPart part : this.parts)
			part.refreshDimensions();
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.55F;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		if (this.isBaby()) {
			return BABY_DIMENSIONS.scale(this.getScale());
		} else {
			GrazerState state = this.getState();
			if (this.isBouncingState(state) || state == GrazerState.WIGGLING) {
				return BOUNCING_DIMENSIONS.scale(this.getScale());
			} else {
				return super.getDimensions(pose);
			}
		}
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
		return state == GrazerState.RUNNING || state == GrazerState.SLOWING_DOWN || state == GrazerState.BOUNCING ? 1.0F : super.getStepHeight();
	}

	@Override
	public float maxUpStep() {
		if (this.getControllingPassenger() instanceof Player && this.isIdleState(this.getState())) {
			return Math.max(this.maxUpStep, 1.0F);
		} else {
			return this.maxUpStep;
		}
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return !this.isBouncingState(this.getState()) && super.causeFallDamage(fallDistance, damageMultiplier, damageSource);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return null;
	}

	@Override
	protected void ageBoundaryReached() {
		super.ageBoundaryReached();
		this.reassessAgeGoals();
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	public double getPassengersRidingOffset() {
		return this.shellCenterY(1.0F) + this.shellRadius() - 0.3D + Math.abs(Mth.sin(this.tickCount * 0.3F)) * 0.75D * this.passengerBounceAmount;
	}

	@Override
	protected void positionRider(Entity rider, Entity.MoveFunction function) {
		if (this.hasPassenger(rider)) {
			Vec3 vec3 = new Vec3(0.0D, this.getPassengersRidingOffset() + rider.getMyRidingOffset(), this.shellCenterZ(1.0F) - 0.3F).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
			function.accept(rider, this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
		}
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		return -level.getPathfindingCostFromLightLevels(pos);
	}

	public double shellWidth() {
		return 12D / 16D * this.getScale();
	}

	public double shellRadius() {
		return 13D / 16D * this.getScale();
	}

	public double shellCenterZ(float partialTick) {
		if (this.isBaby())
			return 0D;
		else
			return 7D / 16D * (1.0F - this.getBodyLowerAmount(partialTick)) * this.getScale();
	}

	public double shellCenterY(float partialTick) {
		float f = this.getBodyLowerAmount(partialTick);
		return 21D / 16D * (1.0F - f) * this.getScale() + this.shellRadius() * f;
	}

	public Vec3 calculateDeflectionNormal(Vec3 hitLocation) {
		float f = this.getCustomXRot() * Mth.DEG_TO_RAD;
		float f1 = this.getYRot() * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(1.0F), this.shellCenterZ(1.0F)).yRot(-f1);
		Vec3 vec31 = hitLocation.subtract(this.position()).subtract(vec3).yRot(f1).xRot(f).multiply(2D / this.shellWidth(), 1D / this.shellRadius(), 1D / this.shellRadius());

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
	public boolean isPushable() {
		return this.getState() != GrazerState.BOUNCING && super.isPushable();
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

	public float getBodyLowerAmount(float partialTick) {
		return Mth.lerp(partialTick, this.bodyLowerAmountO, this.getBodyLowerAmount());
	}

	public float getCustomXRot(float partialTick) {
		return Mth.rotLerp(partialTick, this.customXRotO, this.getCustomXRot());
	}

	public float getRunAmount(float partialTick) {
		return Mth.lerp(partialTick, this.runAmountO, this.runAmount);
	}

	public float getBounceAmount(float partialTick) {
		return Mth.lerp(partialTick, this.bounceAmountO, this.bounceAmount);
	}

	public float getWiggleAmount(float partialTick) {
		return Mth.lerp(partialTick, this.wiggleAmountO, this.wiggleAmount);
	}

	public float getOnBackAmount(float partialTick) {
		return Mth.lerp(partialTick, this.onBackAmountO, this.onBackAmount);
	}

	public float getBeStupidAmount(float partialTick) {
		return Mth.lerp(partialTick, this.beStupidAmountO, this.beStupidAmount);
	}

	public float getVocalizeAmount(float partialTick) {
		return Mth.lerp(partialTick, this.vocalizeAmountO, this.vocalizeAmount);
	}

	public float getBabyWiggleLegsAmount(float partialTick) {
		return Mth.lerp(partialTick, this.babyWiggleLegsAmountO, this.babyWiggleLegsAmount);
	}

	public float getWingFlapAnim(float partialTick) {
		return Mth.lerp(partialTick, this.wingFlapAnimO, this.wingFlapAnim);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == FLAP_WINGS_ANIM) {
			this.wingFlapAnim = 20;
			this.wingFlapAnimO = this.wingFlapAnim;
		} else if (id == BABY_WIGGLE_LEGS_ANIM) {
			this.babyWiggleLegsTime = 30;
		} else if (id == VOCALIZE_ANIM) {
			this.vocalizeTime = 25;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	public void tick() {
		this.pushedThisTick.clear();

		if (this.level().isClientSide) {
			this.customXRotO = this.getCustomXRot();
			this.bodyLowerAmountO = this.getBodyLowerAmount();
		}

		super.tick();

		this.updateDeflectProjectiles();

		GrazerState state = this.getState();

		if (this.level().isClientSide) {
			this.runAmountO = this.runAmount;
			if (state == GrazerState.RUNNING_STILL || state == GrazerState.RUNNING)
				this.runAmount = Math.min(1.0F, this.runAmount + 0.2F);
			else
				this.runAmount = Math.max(0.0F, this.runAmount - 0.2F);

			this.bounceAmountO = this.bounceAmount;
			if (this.isBouncingState(state))
				this.bounceAmount = Math.min(1.0F, this.bounceAmount + 0.2F);
			else
				this.bounceAmount = Math.max(0.0F, this.bounceAmount - 0.2F);

			this.wiggleAmountO = this.wiggleAmount;
			if (state == GrazerState.WIGGLING || state == GrazerState.FLIPPING_OVER)
				this.wiggleAmount = Math.min(1.0F, this.wiggleAmount + 0.1F);
			else
				this.wiggleAmount = Math.max(0.0F, this.wiggleAmount - 0.1F);

			this.onBackAmountO = this.onBackAmount;
			if (state == GrazerState.WIGGLING && this.getCustomXRot() == -90.0F)
				this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.1F);
			else
				this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.1F);

			this.beStupidAmountO = this.beStupidAmount;
			if (this.getState() == GrazerState.BEING_STUPID)
				this.beStupidAmount = Math.min(1.0F, this.beStupidAmount + 0.003F);
			else
				this.beStupidAmount = Math.max(0.0F, this.beStupidAmount - 0.2F);

			this.vocalizeAmountO = this.vocalizeAmount;
			if (this.vocalizeTime > 0)
				this.vocalizeAmount = Math.min(1.0F, this.vocalizeAmount + 0.2F);
			else
				this.vocalizeAmount = Math.max(0.0F, this.vocalizeAmount - (this.isBaby() ? 0.2F : 0.025F));

			this.babyWiggleLegsAmountO = this.babyWiggleLegsAmount;
			if (this.babyWiggleLegsTime > 0)
				this.babyWiggleLegsAmount = Math.min(1.0F, this.babyWiggleLegsAmount + 0.075F);
			else
				this.babyWiggleLegsAmount = Math.max(0.0F, this.babyWiggleLegsAmount - 0.075F);

			if (this.vocalizeTime > 0)
				this.vocalizeTime--;

			this.wingFlapAnimO = this.wingFlapAnim;
		}

		if (this.wingFlapAnim > 0)
			this.wingFlapAnim--;

		if (this.babyWiggleLegsTime > 0)
			this.babyWiggleLegsTime--;

		if (this.isBouncingState(this.getState()))
			this.passengerBounceAmount = Math.min(1.0F, this.passengerBounceAmount + 0.2F);
		else
			this.passengerBounceAmount = Math.max(0.0F, this.passengerBounceAmount - 0.2F);

		double d0 = this.shellRadius();
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(1.0F), this.shellCenterZ(1.0F)).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
		AABB aabb = new AABB(-d0, -d0, -d0, d0, d0, d0).move(this.position()).move(vec3).inflate(0.3D);
		for (Projectile projectile : this.level().getEntitiesOfClass(Projectile.class, aabb))
			if (this.deflectedPrevTick.contains(projectile))
				this.deflectedThisTick.add(projectile);

		for (GrazerPart part : this.parts)
			part.updatePosition();
	}

	@Override
	public void aiStep() {
		int lerpstepsold = this.lerpSteps;
		float xrotold = this.getXRot();

		super.aiStep();

		float bodyLowerAmount = this.getBodyLowerAmount();
		if (this.isBouncingState(this.getState()) || this.getState() == GrazerState.WIGGLING)
			this.setBodyLowerAmount(Math.min(1.0F, bodyLowerAmount + 0.1F));
		else
			this.setBodyLowerAmount(Math.max(0.0F, bodyLowerAmount - 0.1F));

		if (this.getState() == GrazerState.BOUNCING) {
			Vec3 movement = this.getDeltaMovement();
			this.setCustomXRot(Mth.wrapDegrees(this.getCustomXRot() - BOUNCE_ROT_SPEED));
			if (!this.level().isClientSide) {
				if (this.bouncingBackwards)
					this.setYRot((float) (Mth.atan2(movement.x, -movement.z) * Mth.RAD_TO_DEG));
				else
					this.setYRot((float) (Mth.atan2(-movement.x, movement.z) * Mth.RAD_TO_DEG));
			}
		} else if (this.getState() == GrazerState.LANDING || this.getState() == GrazerState.WIGGLING) {
			float xrot = Mth.wrapDegrees(this.getCustomXRot());
			float targetrot = this.getTargetXRot();
			if (xrot == targetrot) {
				if (!this.level().isClientSide && this.getState() != GrazerState.WIGGLING)
					this.setState(GrazerState.WIGGLING);
			} else {
				if (xrot < targetrot) {
					xrot += 360.0F;
				}
				this.setCustomXRot(Mth.wrapDegrees(Math.max(xrot - BOUNCE_ROT_SPEED, targetrot)));
			}
		} else {
			float xrot = Mth.wrapDegrees(this.getCustomXRot());
			if (xrot > 0.0F)
				this.setCustomXRot(Math.max(xrot - 20.0F, 0.0F));
			else if (xrot < 0.0F)
				this.setCustomXRot(Math.min(xrot + 20.0F, 0.0F));
			else if (!this.level().isClientSide && this.getState() == GrazerState.FLIPPING_OVER)
				this.setState(GrazerState.DEFAULT);

			if (!this.level().isClientSide) {
				if (this.isIdleState(this.getState()) && this.wingFlapAnim <= 0 && this.random.nextInt(200) == 0) {
					this.wingFlapAnim = 20;
					this.level().broadcastEntityEvent(this, FLAP_WINGS_ANIM);
				}

				if (this.isBaby() && this.babyWiggleLegsTime <= 0 && this.random.nextInt(50) == 0) {
					this.level().broadcastEntityEvent(this, BABY_WIGGLE_LEGS_ANIM);
				}
			}
		}

		this.setXRot(this.getCustomXRot());

		if (this.level().isClientSide) {
			if (lerpstepsold > 0)
				this.setXRot((xrotold + (float) Mth.wrapDegrees(this.lerpXRot - (double) xrotold) / (float) lerpstepsold) % 360.0F);

			if (this.isBaby() && this.tickCount % 100 == 0) {
				float rot = this.getYRot() * Mth.DEG_TO_RAD;
				Vec3 vec3 = new Vec3(this.getX() + 0.45D * Math.sin(-rot), this.getY(), this.getZ() + 0.45D * Math.cos(-rot));
				if (this.level().clip(new ClipContext(vec3, vec3.add(0.0D, -0.05D, 0.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).isInside())
					this.level().addParticle(CCParticleTypes.DROOL_PUDDLE.get(), vec3.x, vec3.y, vec3.z, this.random.nextInt(4) * Mth.HALF_PI, 0.0D, 0.0D);
			}

			if (this.isBouncingState(this.getState())) {
				float f = this.getCustomXRot() * Mth.DEG_TO_RAD;
				float f1 = this.getYRot() * Mth.DEG_TO_RAD;
				Vec3 offset = new Vec3(-5.0D / 16.0D, -12.0D / 16.0D, 9.5D / 16.0D).scale(this.getScale());
				Vec3 offsetrotated = offset.xRot(-f).yRot(-f1);
				Vec3 shellcenter = new Vec3(0.0D, this.shellCenterY(1.0F) - this.getDimensions(net.minecraft.world.entity.Pose.STANDING).height * 0.5D, this.shellCenterZ(1.0F)).yRot(-f1);
				Vec3 pos = offsetrotated.add(shellcenter).add(this.position());
				double tangentialspeed = Mth.TWO_PI * 18.0D / BOUNCE_ROT_SPEED * Math.sqrt(offset.y * offset.y + offset.z * offset.z) * Mth.DEG_TO_RAD;
				Vec3 tangentialvelcity = new Vec3(0.0D, offsetrotated.z, -offsetrotated.y).normalize().scale(tangentialspeed).add(this.getDeltaMovement());
				this.level().addParticle(CCParticleTypes.DROOL.get(), pos.x, pos.y, pos.z, tangentialvelcity.x + this.random.nextGaussian() * 0.02F, tangentialvelcity.y + this.random.nextGaussian() * 0.02F, tangentialvelcity.z + this.random.nextGaussian() * 0.02F);
			}
		}

		for (GrazerPart part : this.parts)
			part.pushEntities();
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();

		// Undo aging tick if not enough space
		if (this.getAge() < 0 && !this.level().noCollision(this, this.getType().getDimensions().scale(0.5F, 1.0F).makeBoundingBox(this.position()).deflate(1.0E-6D)))
			this.setAge(this.age - 1);

		// Colliding with entities while running or bouncing
		if (this.getState() == GrazerState.RUNNING || this.getState() == GrazerState.BOUNCING) {
			LivingEntity livingentity = this.level().getNearestEntity(LivingEntity.class, HIT_TARGETING, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(0.55D, 0.0D, 0.55D));
			if (livingentity != null) {
				if (livingentity instanceof AbstractGrazer other && !other.isBaby()) {
					Vec3 posdiff = other.position().subtract(this.position());
					double distance = posdiff.length();
					double distancechange = this.position().add(this.getDeltaMovement()).distanceTo(other.position().add(other.getDeltaMovement())) - distance;

					if (distancechange < 0.0D) {
						Vec3 motion = this.getState() == GrazerState.RUNNING ? this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.55D) : this.getDeltaMovement();

						Vec3 collvector = posdiff.scale(motion.dot(posdiff) / posdiff.dot(posdiff));
						Vec3 othercollvector = posdiff.scale(other.getDeltaMovement().dot(posdiff) / posdiff.dot(posdiff));

						Vec3 newmotion = motion.subtract(collvector).add(othercollvector);
						Vec3 othernewmotion = other.getDeltaMovement().subtract(othercollvector).add(collvector);

						if (this.getState() == GrazerState.RUNNING) {
							this.setState(GrazerState.BOUNCING);
							this.bounceHeight = 0.8D;
							this.bouncingBackwards = true;
							newmotion.add(0.0D, this.bounceHeight, 0.0D);
						}

						if (other.getState() != GrazerState.BOUNCING) {
							other.setState(GrazerState.BOUNCING);
							other.bounceHeight = this.bounceHeight;
							othernewmotion.add(0.0D, other.bounceHeight, 0.0D);
						}

						this.setDeltaMovement(newmotion);
						other.setDeltaMovement(othernewmotion);

						double horizontaldist = Math.sqrt(posdiff.x * posdiff.x + posdiff.z * posdiff.z);
						double d0 = (horizontaldist + this.getBbWidth() * 0.5D - other.getBbWidth() * 0.5D) / horizontaldist * 0.5D;

						Vec3 collpoint = new Vec3(this.position().x + posdiff.x * d0, (this.position().y + this.getBbHeight() + other.position().y) * 0.5D, this.position().z + posdiff.z * d0);
						CCUtil.playRicochetSound(this.level(), collpoint, distancechange, CCSoundEvents.GRAZER_RICOCHET.get(), 1.0F);

						for (int i = 0; i < 4; i++) {
							double d1 = this.random.nextGaussian() * 0.05D;
							double d2 = 0.3D + this.random.nextGaussian() * 0.05D;
							double d3 = this.random.nextGaussian() * 0.05D;
							NetworkUtil.spawnParticle(CCParticleTypes.TIN_SPARK.getId().toString(), collpoint.x, collpoint.y, collpoint.z, d1, d2, d3);
						}
					}
				} else {
					// TODO: Add knockback
					DamageSource damageSource;
					LivingEntity rider = this.getControllingPassenger();
					if (rider != null) {
						damageSource = CCDamageTypes.ridingGrazer(this.level(), this, rider);
					} else {
						damageSource = CCDamageTypes.grazer(this.level(), this);
					}

					livingentity.hurt(damageSource, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
				}
			}
		}
	}

	@Override
	public void move(MoverType moverType, Vec3 movement) {
		double xold = this.getX();
		double yold = this.getY();
		double zold = this.getZ();
		Vec3 oldmotion = this.getDeltaMovement();

		super.move(moverType, movement);

		this.ricochet(movement, xold, yold, zold, oldmotion);
	}

	public void ricochet(Vec3 movement, double xOld, double yOld, double zOld, Vec3 oldMotion) {
		if (!this.level().isClientSide && this.isAlive() && !this.noPhysics && (this.getState() == GrazerState.RUNNING || this.getState() == GrazerState.BOUNCING)) {
			Vec3 newMotion = oldMotion;
			Vec3 collPoint = Vec3.ZERO;
			boolean ricocheted = false;
			boolean horizontal = false;
			boolean ground = false;

			if (xOld + movement.x != this.getX()) {
				newMotion = new Vec3(-newMotion.x, newMotion.y, newMotion.z);
				collPoint = new Vec3(this.getBbWidth() * 0.5D * (movement.x >= 0 ? 1 : -1), collPoint.y, collPoint.z);
				ricocheted = true;
				horizontal = true;
			}
			if (this.getState() == GrazerState.BOUNCING) {
				double d0 = yOld + movement.y;
				if (d0 > this.getY()) {
					newMotion = new Vec3(newMotion.x, -newMotion.y, newMotion.z);
					collPoint = new Vec3(collPoint.x, this.getBbHeight(), collPoint.z);
					ricocheted = true;
				} else if (d0 < this.getY()) {
					if (this.bounceHeight < 0.376D && this.getState() != GrazerState.LANDING) {
						this.setState(GrazerState.LANDING);
						float xrot = Mth.wrapDegrees(this.getCustomXRot());
						this.setTargetXRot(xrot > 90.0F ? 90.0F : xrot > -90.0F ? -90.0F : this.getPassengers().isEmpty() ? -180.0F : 90.0F);
						return;
					} else {
						this.bounceHeight *= 0.94D;
						newMotion = new Vec3(newMotion.x, this.bounceHeight, newMotion.z);
						ricocheted = true;
						ground = true;
					}
				}
			}
			if (zOld + movement.z != this.getZ()) {
				newMotion = new Vec3(newMotion.x, newMotion.y, -newMotion.z);
				collPoint = new Vec3(collPoint.x, collPoint.y, this.getBbWidth() * 0.5D * (movement.z >= 0 ? 1 : -1));
				ricocheted = true;
				horizontal = true;
			}

			if (ricocheted) {
				double adjustAngle = horizontal ? this.random.nextDouble() - 0.5D : 0.0D;

				if (this.getControllingPassenger() instanceof Player player) {
					if (horizontal || ground) {
						Vec3 playerMoveVec = this.getRidingPlayerAbsMovement(player);
						if (Math.abs(playerMoveVec.x) >= 1.0E-7D || Math.abs(playerMoveVec.z) >= 1.0E-7D) {
							double newAdjustAngle = this.calculateHorizontalAngleDiff(newMotion, playerMoveVec);
							adjustAngle = Mth.clamp(newAdjustAngle, -0.6D, 0.6D);
						}
					}
				} else {
					if (horizontal || ground) {
						LivingEntity target = this.getTarget();
						if (target != null && target.isAlive()) {
							Vec3 targetVec = target.position().subtract(this.position());
							double newAdjustAngle = this.calculateHorizontalAngleDiff(newMotion, targetVec);
							if (Math.abs(newAdjustAngle) <= Math.PI / 2.0D) {
								adjustAngle = Mth.clamp(newAdjustAngle, -0.6D, 0.6D);
							}
						}
					}
				}

				newMotion = new Vec3(newMotion.x * Math.cos(adjustAngle) - newMotion.z * Math.sin(adjustAngle), newMotion.y, newMotion.x * Math.sin(adjustAngle) + newMotion.z * Math.cos(adjustAngle));

				if (this.getState() == GrazerState.RUNNING) {
					this.setState(GrazerState.BOUNCING);
					this.bounceHeight = 0.8D;
					this.bouncingBackwards = true;
					newMotion = newMotion.multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.55D).add(0.0D, this.bounceHeight, 0.0D);
				} else {
					if (oldMotion.x * newMotion.x + oldMotion.z * newMotion.z < 0.0D)
						this.bouncingBackwards = !this.bouncingBackwards;
				}

				this.setDeltaMovement(newMotion);

				Vec3 vec3 = this.position().add(collPoint);
				Vec3 vec31 = oldMotion.reverse().normalize();
				CCUtil.playRicochetSound(this.level(), vec3, newMotion.lengthSqr(), CCSoundEvents.GRAZER_RICOCHET.get(), 1.0F);

				for (int i = 0; i < 8; i++) {
					double d1 = vec31.x * 0.3D + this.random.nextGaussian() * 0.05D;
					double d2 = vec31.y * 0.3D + this.random.nextGaussian() * 0.05D;
					double d3 = vec31.z * 0.3D + this.random.nextGaussian() * 0.05D;
					double d7 = 1.0D + this.random.nextDouble() * 2.0D;
					double d4 = vec3.x + vec31.x * d7 + this.random.nextGaussian() * 0.2D;
					double d5 = vec3.y + vec31.y + this.random.nextGaussian() * 0.2D;
					double d6 = vec3.z + vec31.z * d7 + this.random.nextGaussian() * 0.2D;
					NetworkUtil.spawnParticle(CCParticleTypes.TIN_SPARK.getId().toString(), d4, d5, d6, d1, d2, d3);
				}
			}
		}
	}

	private Vec3 getRidingPlayerAbsMovement(Player player) {
		return new Vec3(player.xxa, player.yya, player.zza).yRot(-player.getYRot() * Mth.DEG_TO_RAD);
	}

	private double calculateHorizontalAngleDiff(Vec3 motion, Vec3 targetVector) {
		double angleDiff = Math.atan2(targetVector.x * motion.z - motion.x * targetVector.z, targetVector.x * motion.x + targetVector.z * motion.z);
		return -angleDiff;
	}

	@Override
	public void knockback(double strength, double ratioX, double ratioZ) {
		super.knockback(strength, ratioX, ratioZ);
		if (this.getState() == GrazerState.WIGGLING) {
			this.setState(GrazerState.FLIPPING_OVER);
		}
	}

	@Override
	public MoveControl getMoveControl() {
		return this.moveControl;
	}

	@Override
	protected BodyRotationControl createBodyControl() {
		return new GrazerBodyRotationControl();
	}

	private class GrazerLookControl extends LookControl {
		public GrazerLookControl() {
			super(AbstractGrazer.this);
		}

		public void tick() {
			if (AbstractGrazer.this.canMove()) {
				if (this.lookAtCooldown > 0) {
					--this.lookAtCooldown;
					this.getYRotD().ifPresent((rot) -> {
						AbstractGrazer.this.setYRot(this.rotateTowards(AbstractGrazer.this.getYRot(), rot, this.yMaxRotSpeed));
					});
				}
			}
		}
	}

	private class GrazerBodyRotationControl extends BodyRotationControl {
		public GrazerBodyRotationControl() {
			super(AbstractGrazer.this);
		}

		@Override
		public void clientTick() {
			AbstractGrazer.this.yHeadRot = AbstractGrazer.this.getYRot();
			AbstractGrazer.this.yBodyRot = AbstractGrazer.this.getYRot();
		}
	}

	private class GrazerMoveControl extends MoveControl {

		public GrazerMoveControl() {
			super(AbstractGrazer.this);
		}

		@Override
		public void tick() {
			if (AbstractGrazer.this.canMove()) {
				GrazerState state = AbstractGrazer.this.getState();
				if (state == GrazerState.RUNNING) {
					AbstractGrazer.this.setSpeed(0.5F);
				} else if (state == GrazerState.SLOWING_DOWN) {
					AbstractGrazer.this.setSpeed(Math.max(AbstractGrazer.this.getSpeed() - 0.01F, 0.0F));
					if (AbstractGrazer.this.getSpeed() <= 0.0F)
						AbstractGrazer.this.setState(GrazerState.DEFAULT);
				} else {
					super.tick();
				}
			}
		}
	}
}