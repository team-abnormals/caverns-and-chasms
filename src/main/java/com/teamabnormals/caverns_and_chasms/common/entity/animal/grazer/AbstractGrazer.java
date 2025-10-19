package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBeBabyGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBeStupidGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBounceGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerRunGoal;
import com.teamabnormals.caverns_and_chasms.core.other.CCEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGrazer extends Animal {
	private static final float ROTATION_SPEED = 15.0F;

	private static final EntityDimensions BOUNCING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.625F);
	private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(1.8F, 1.98F);

	private static final TargetingConditions HIT_TARGETING = TargetingConditions.forCombat().selector(livingentity -> {
		return livingentity.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox()) && !livingentity.isPassenger();
	});

	private static final EntityDataAccessor<Integer> RUN_PHASE = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> BODY_LOWER_AMOUNT = SynchedEntityData.defineId(AbstractGrazer.class, EntityDataSerializers.FLOAT);

	private final GrazerPart[] parts = new GrazerPart[7];

	private final Set<Entity> pushedThisTick = new HashSet<>();
	private Set<Projectile> deflectedThisTick = new HashSet<>();
	private Set<Projectile> deflectedPrevTick = new HashSet<>();
	private long lastDeflectTick;

	private boolean bouncingBackwards;
	private double bounceHeight;
	private float bodyLowerAmountO;

	private boolean beingStupid;

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

	private int wingFlapAnim;
	private int wingFlapAnimO;

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
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new GrazerBeBabyGoal(this));
		this.goalSelector.addGoal(1, new GrazerBounceGoal(this));
		this.goalSelector.addGoal(2, new FloatGoal(this));
		this.goalSelector.addGoal(3, new GrazerRunGoal(this, entity -> entity instanceof Player, 8.0D));
		this.goalSelector.addGoal(4, new GrazerBeStupidGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RUN_PHASE, 0);
		this.entityData.define(BODY_LOWER_AMOUNT, 0.0F);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("RatPose", this.getState().getId());
		compound.putFloat("BodyLowerAmount", this.getBodyLowerAmount());
		compound.putBoolean("BouncingBackwards", this.bouncingBackwards);
		compound.putDouble("BounceHeight", this.bounceHeight);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setState(GrazerState.byId(compound.getInt("RatPose")));
		this.setBodyLowerAmount(compound.getFloat("BodyLowerAmount"));
		this.bouncingBackwards = compound.getBoolean("BouncingBackwards");
		this.bounceHeight = compound.getDouble("BounceHeight");
	}

	public GrazerState getState() {
		return GrazerState.byId(this.entityData.get(RUN_PHASE));
	}

	public void setState(GrazerState state) {
		this.entityData.set(RUN_PHASE, state.getId());
		this.setSprinting(state == GrazerState.RUNNING);
		this.setDiscardFriction(this.isBouncingState(state));
	}

	public boolean isBouncingState(GrazerState state) {
		return state == GrazerState.BOUNCING || state == GrazerState.LANDING;
	}

	public boolean canMove() {
		GrazerState state = this.getState();
		return !this.isBaby() && !this.isBouncingState(state) && state != GrazerState.WIGGLING && state != GrazerState.FLIPPING_OVER;
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

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (RUN_PHASE.equals(key))
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
		return state == GrazerState.RUNNING ? 1.0F : super.getStepHeight();
	}

	@Override
	protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
		return this.isBouncingState(this.getState()) ? 0 : super.calculateFallDamage(fallDistance, damageMultiplier);
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
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	public double getPassengersRidingOffset() {
		return this.shellCenterY(1.0F) + this.shellRadius() - 0.3D;
	}

	@Override
	protected void positionRider(Entity rider, Entity.MoveFunction function) {
		if (this.hasPassenger(rider)) {
			Vec3 vec3 = new Vec3(0.0D, this.getPassengersRidingOffset() + rider.getMyRidingOffset(), this.shellCenterZ(1.0F) - 0.3F).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
			function.accept(rider, this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
		}
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
		float f = this.getXRot() * Mth.DEG_TO_RAD;
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
		return !this.isBouncingState(this.getState()) && super.isPushable();
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

	public float getWingFlapAnim(float partialTick) {
		return Mth.lerp(partialTick, this.wingFlapAnimO, this.wingFlapAnim);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.beingStupid = true;
		} else if (id == 5) {
			this.beingStupid = false;
		} else if (id == 6) {
			this.wingFlapAnim = 20;
			this.wingFlapAnimO = this.wingFlapAnim;
		}
		super.handleEntityEvent(id);
	}

	@Override
	public void tick() {
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
			if (state == GrazerState.WIGGLING && this.getXRot() == -90.0F)
				this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.1F);
			else
				this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.1F);

			this.beStupidAmountO = this.beStupidAmount;
			if (this.beingStupid)
				this.beStupidAmount = Math.min(1.0F, this.beStupidAmount + 0.003F);
			else
				this.beStupidAmount = Math.max(0.0F, this.beStupidAmount - 0.2F);

			this.wingFlapAnimO = this.wingFlapAnim;
			this.bodyLowerAmountO = this.getBodyLowerAmount();
		} else {
			float bodyLowerAmount = this.getBodyLowerAmount();
			if (state == GrazerState.BOUNCING || state == GrazerState.LANDING || state == GrazerState.WIGGLING)
				this.setBodyLowerAmount(Math.min(1.0F, bodyLowerAmount + 0.1F));
			else
				this.setBodyLowerAmount(Math.max(0.0F, bodyLowerAmount - 0.1F));
		}

		if (this.wingFlapAnim > 0)
			this.wingFlapAnim--;

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
		this.pushedThisTick.clear();
		int lerpstepsold = this.lerpSteps;
		float xrotold = this.getXRot();

		super.aiStep();

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
				float f = this.getXRot() * Mth.DEG_TO_RAD;
				float f1 = this.getYRot() * Mth.DEG_TO_RAD;
				Vec3 offset = new Vec3(-5.0D / 16.0D, -12.0D / 16.0D, 9.5D / 16.0D).scale(this.getScale());
				Vec3 offsetrotated = offset.xRot(-f).yRot(-f1);
				Vec3 shellcenter = new Vec3(0.0D, this.shellCenterY(1.0F) - this.getDimensions(net.minecraft.world.entity.Pose.STANDING).height * 0.5D, this.shellCenterZ(1.0F)).yRot(-f1);
				Vec3 pos = offsetrotated.add(shellcenter).add(this.position());
				double tangentialspeed = Mth.TWO_PI * 18.0D / ROTATION_SPEED * Math.sqrt(offset.y * offset.y + offset.z * offset.z) * Mth.DEG_TO_RAD;
				Vec3 tangentialvelcity = new Vec3(0.0D, offsetrotated.z, -offsetrotated.y).normalize().scale(tangentialspeed).add(this.getDeltaMovement());
				this.level().addParticle(CCParticleTypes.DROOL.get(), pos.x, pos.y, pos.z, tangentialvelcity.x + this.random.nextGaussian() * 0.02F, tangentialvelcity.y + this.random.nextGaussian() * 0.02F, tangentialvelcity.z + this.random.nextGaussian() * 0.02F);
			}
		} else if (this.isAlive()) {
			// Undo aging tick if not enough space
			if (!this.level().isClientSide && this.isAlive() && this.getAge() < 0 && !this.level().noCollision(this, this.getType().getDimensions().scale(0.5F, 1.0F).makeBoundingBox(this.position()).deflate(1.0E-6D)))
				this.setAge(this.age - 1);

			// Flap wings randomly
			if (this.getState() == GrazerState.DEFAULT && this.wingFlapAnim <= 0 && this.random.nextInt(200) == 0) {
				this.wingFlapAnim = 20;
				this.level().broadcastEntityEvent(this, (byte) 6);
			}

			// Rotation while bouncing
			if (this.getState() == GrazerState.BOUNCING) {
				Vec3 movement = this.getDeltaMovement();
				this.setXRot(Mth.wrapDegrees(this.getXRot() - ROTATION_SPEED));
				if (this.bouncingBackwards)
					this.setYRot((float) (Mth.atan2(movement.x, -movement.z) * Mth.RAD_TO_DEG));
				else
					this.setYRot((float) (Mth.atan2(-movement.x, movement.z) * Mth.RAD_TO_DEG));
			} else if (this.getState() == GrazerState.LANDING) {
				float xrot = Mth.wrapDegrees(this.getXRot());
				if (xrot == -180.0F || xrot == -90.0F || xrot == 90.0F) {
					this.setState(GrazerState.WIGGLING);
				} else {
					if (xrot < -90.0F)
						this.setXRot(Math.max(xrot - ROTATION_SPEED, -180.0F));
					else if (xrot < 90.0F)
						this.setXRot(Math.max(xrot - ROTATION_SPEED, -90.0F));
					else
						this.setXRot(Math.max(xrot - ROTATION_SPEED, 90.0F));
				}
			} else if (this.getState() == GrazerState.FLIPPING_OVER) {
				float xrot = Mth.wrapDegrees(this.getXRot());
				if (xrot > 0.0F)
					this.setXRot(Math.max(xrot - 20.0F, 0.0F));
				else if (xrot < 0.0F)
					this.setXRot(Math.min(xrot + 20.0F, 0.0F));
				else
					this.setState(GrazerState.DEFAULT);
			}

			// Colliding with entities while running or bouncing
			if (this.getState() == GrazerState.RUNNING || this.isBouncingState(this.getState())) {
				LivingEntity livingentity = this.level().getNearestEntity(LivingEntity.class, HIT_TARGETING, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(0.55D, 0.0D, 0.55D));
				if (livingentity != null) {
					if (livingentity instanceof AbstractGrazer other && !other.isBaby()) {
						GrazerState otherstate = other.getState();
						double d0 = this.position().distanceTo(other.position()) - this.position().add(this.getDeltaMovement()).distanceTo(other.position().add(other.getDeltaMovement()));

						if (d0 > 0.0D) {
							Vec3 deltapos = other.position().subtract(this.position());
							Vec3 motion = this.getState() == GrazerState.RUNNING ? this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.55D) : this.getDeltaMovement();

							Vec3 collvector = deltapos.scale(motion.dot(deltapos) / deltapos.dot(deltapos));
							Vec3 othercollvector = deltapos.scale(other.getDeltaMovement().dot(deltapos) / deltapos.dot(deltapos));

							Vec3 newmotion = motion.subtract(collvector).add(othercollvector);
							Vec3 othernewmotion = other.getDeltaMovement().subtract(othercollvector).add(collvector);

							if (this.getState() == GrazerState.RUNNING) {
								this.setState(GrazerState.BOUNCING);
								this.bounceHeight = 0.8D;
								this.bouncingBackwards = true;
								newmotion.add(0.0D, this.bounceHeight, 0.0D);
							}

							if (!this.isBouncingState(otherstate)) {
								other.setState(GrazerState.BOUNCING);
								other.bounceHeight = this.bounceHeight;
								othernewmotion.add(0.0D, other.bounceHeight, 0.0D);
							}

							this.setDeltaMovement(newmotion);
							other.setDeltaMovement(othernewmotion);

							CCEvents.playTinDeflectSound(this.level(), this.position(), d0);
						}
					} else {
						// TODO: Add knockback
						livingentity.hurt(this.level().damageSources().noAggroMobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
					}
				}
			}
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

		// Ricocheting off of walls
		GrazerState state = this.getState();
		if (!this.level().isClientSide && this.isAlive() && !this.noPhysics && (state == GrazerState.RUNNING || state == GrazerState.BOUNCING || state == GrazerState.LANDING)) {
			Vec3 newmotion = oldmotion;
			boolean ricocheted = false;
			boolean horizontal = false;

			if (xold + movement.x != this.getX()) {
				newmotion = new Vec3(-oldmotion.x, oldmotion.y, oldmotion.z);
				ricocheted = true;
				horizontal = true;
			}
			if (this.isBouncingState(this.getState())) {
				double d0 = yold + movement.y;
				if (d0 > this.getY()) {
					newmotion = new Vec3(oldmotion.x, -oldmotion.y, oldmotion.z);
					ricocheted = true;
				} else if (d0 < this.getY()) {
					if (this.bounceHeight < 0.4D && this.getState() != GrazerState.LANDING)
						this.setState(GrazerState.LANDING);
					this.bounceHeight *= 0.94D;
					newmotion = new Vec3(oldmotion.x, this.bounceHeight, oldmotion.z);
					ricocheted = true;
				}
			}
			if (zold + movement.z != this.getZ()) {
				newmotion = new Vec3(oldmotion.x, oldmotion.y, -oldmotion.z);
				ricocheted = true;
				horizontal = true;
			}

			if (ricocheted) {
				if (horizontal) {
					double adjustangle = this.calculateCollisionAdjustAngle(newmotion);
					newmotion = new Vec3(newmotion.x * Math.cos(adjustangle) - newmotion.z * Math.sin(adjustangle), newmotion.y, newmotion.x * Math.sin(adjustangle) + newmotion.z * Math.cos(adjustangle));
				}

				if (this.getState() == GrazerState.RUNNING) {
					this.setState(GrazerState.BOUNCING);
					this.bounceHeight = 0.8D;
					this.bouncingBackwards = true;
					newmotion = newmotion.multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.55D).add(0.0D, this.bounceHeight, 0.0D);
				} else {
					if (oldmotion.x * newmotion.x + oldmotion.z * newmotion.z < 0.0D)
						this.bouncingBackwards = !this.bouncingBackwards;
				}

				this.setDeltaMovement(newmotion);

				CCEvents.playTinDeflectSound(this.level(), this.position(), newmotion.lengthSqr());
			}
		}
	}

	private double calculateCollisionAdjustAngle(Vec3 newmotion) {
		LivingEntity target = this.getTarget();
		if (target != null && target.isAlive()) {
			Vec3 targetvector = target.position().subtract(this.position());
			double anglediff = Math.atan2(targetvector.x * newmotion.z - newmotion.x * targetvector.z, targetvector.x * newmotion.x + targetvector.z * newmotion.z);
			if (Math.abs(anglediff) <= Math.PI / 2.0D)
				return anglediff > 0.0D ? Math.max(-0.6D, -anglediff) : anglediff < 0.0D ? Math.min(0.6D, -anglediff) : 0.0D;
		}
		return this.random.nextDouble() - 0.5D;
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
					if (!AbstractGrazer.this.isBouncingState(AbstractGrazer.this.getState())) {
						this.getYRotD().ifPresent((rot) -> {
							AbstractGrazer.this.setYRot(this.rotateTowards(AbstractGrazer.this.getYRot(), rot, this.yMaxRotSpeed));
						});
					}
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
				if (AbstractGrazer.this.getState() == GrazerState.RUNNING) {
					AbstractGrazer.this.setSpeed(0.5F);
				} else {
					super.tick();
				}
			}
		}
	}
}