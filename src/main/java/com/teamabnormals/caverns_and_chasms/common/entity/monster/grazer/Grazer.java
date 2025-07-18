package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBeStupidGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerBounceGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer.GrazerRunGoal;
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
import java.util.Set;

// TODO: Make sure when you get to saddled grazers that they dont despawn on peaceful
public class Grazer extends Monster {
	private static final EntityDimensions BOUNCING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.625F);

	private static final TargetingConditions HIT_TARGETING = TargetingConditions.forCombat().selector(livingentity -> {
		return !livingentity.getType().equals(CCEntityTypes.GRAZER) && livingentity.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
	});

	private static final EntityDataAccessor<Integer> RUN_PHASE = SynchedEntityData.defineId(Grazer.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> BODY_LOWER_AMOUNT = SynchedEntityData.defineId(Grazer.class, EntityDataSerializers.FLOAT);

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
		this.goalSelector.addGoal(0, new GrazerBounceGoal(this));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new GrazerRunGoal(this, entity -> entity instanceof Player, 8.0D));
		this.goalSelector.addGoal(3, new GrazerBeStupidGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
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

	public static boolean checkGrazerSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return pos.getY() < CCConfig.COMMON.grazerMaxSpawnHeight.get() && Mime.checkUndergroundMonsterSpawnRules(type, level, reason, pos, random);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("State", this.getState().getId());
		compound.putFloat("BodyLowerAmount", this.getBodyLowerAmount());
		compound.putBoolean("BouncingBackwards", this.bouncingBackwards);
		compound.putDouble("BounceHeight", this.bounceHeight);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setState(GrazerState.byId(compound.getInt("State")));
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
		this.setDiscardFriction(state == GrazerState.BOUNCING || state == GrazerState.LANDING);
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
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.55F;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		GrazerState state = this.getState();
		if (state == GrazerState.BOUNCING || state == GrazerState.LANDING || state == GrazerState.WIGGLING) {
			return BOUNCING_DIMENSIONS.scale(this.getScale());
		} else {
			return super.getDimensions(pose);
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
		return this.getState() == GrazerState.BOUNCING || this.getState() == GrazerState.LANDING ? 0 : super.calculateFallDamage(fallDistance, damageMultiplier);
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

	public double shellCenterZ(float partialTick) {
		return 7D / 16D * (1.0F - this.getBodyLowerAmount(partialTick));
	}

	public double shellCenterY(float partialTick) {
		float f = this.getBodyLowerAmount(partialTick);
		return 21D / 16D * (1.0F - f) + this.shellRadius() * f;
	}

	public Vec3 calculateDeflectionNormal(Vec3 location) {
		float f = this.getXRot() * Mth.DEG_TO_RAD;
		float f1 = this.getYRot() * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.shellCenterY(1.0F), this.shellCenterZ(1.0F)).yRot(-f1);
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
	public boolean isPushable() {
		GrazerState state = this.getState();
		return state != GrazerState.BOUNCING && state != GrazerState.LANDING && super.isPushable();
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
			if (state == GrazerState.RUNNING_STILL || state == GrazerState.RUNNING) {
				this.runAmount = Math.min(1.0F, this.runAmount + 0.2F);
			} else {
				this.runAmount = Math.max(0.0F, this.runAmount - 0.2F);
			}

			this.bounceAmountO = this.bounceAmount;
			if (state == GrazerState.BOUNCING) {
				this.bounceAmount = Math.min(1.0F, this.bounceAmount + 0.1F);
			} else {
				this.bounceAmount = Math.max(0.0F, this.bounceAmount - 0.1F);
			}

			this.wiggleAmountO = this.wiggleAmount;
			if (state == GrazerState.WIGGLING || state == GrazerState.FLIPPING_OVER) {
				this.wiggleAmount = Math.min(1.0F, this.wiggleAmount + 0.1F);
			} else {
				this.wiggleAmount = Math.max(0.0F, this.wiggleAmount - 0.1F);
			}

			this.onBackAmountO = this.onBackAmount;
			if (state == GrazerState.WIGGLING && this.getXRot() == -90.0F) {
				this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.1F);
			} else {
				this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.1F);
			}

			this.beStupidAmountO = this.beStupidAmount;
			if (this.beingStupid) {
				this.beStupidAmount = Math.min(1.0F, this.beStupidAmount + 0.003F);
			} else {
				this.beStupidAmount = Math.max(0.0F, this.beStupidAmount - 0.2F);
			}

			this.wingFlapAnimO = this.wingFlapAnim;
			if (this.wingFlapAnim > 0) {
				this.wingFlapAnim--;
			}
		}

		float bodyLowerAmount = this.getBodyLowerAmount();
		if (this.level().isClientSide)
			this.bodyLowerAmountO = bodyLowerAmount;
		if (state == GrazerState.BOUNCING || state == GrazerState.LANDING || state == GrazerState.WIGGLING) {
			this.setBodyLowerAmount(Math.min(1.0F, bodyLowerAmount + 0.1F));
		} else {
			this.setBodyLowerAmount(Math.max(0.0F, bodyLowerAmount - 0.1F));
		}

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
		} else if (this.isAlive()) {
			if (this.getState() == GrazerState.DEFAULT && this.wingFlapAnim <= 0 && this.random.nextInt(200) == 0) {
				this.wingFlapAnim = 20;
				this.level().broadcastEntityEvent(this, (byte) 6);
			}

			if (this.getState() == GrazerState.BOUNCING) {
				Vec3 movement = this.getDeltaMovement();
				this.setXRot(Mth.wrapDegrees(this.getXRot() - 15.0F));
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
						this.setXRot(Math.max(xrot - 15.0F, -180.0F));
					else if (xrot < 90.0F)
						this.setXRot(Math.max(xrot - 15.0F, -90.0F));
					else
						this.setXRot(Math.max(xrot - 15.0F, 90.0F));
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

			if (this.getState() == GrazerState.RUNNING || this.getState() == GrazerState.BOUNCING || this.getState() == GrazerState.LANDING) {
				LivingEntity livingentity = this.level().getNearestEntity(LivingEntity.class, HIT_TARGETING, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(0.55D, 0.0D, 0.55D));
				if (livingentity != null) {
					if (livingentity instanceof Grazer other) {
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

							if (otherstate != GrazerState.BOUNCING && otherstate != GrazerState.LANDING) {
								other.setState(GrazerState.BOUNCING);
								other.bounceHeight = this.bounceHeight;
								othernewmotion.add(0.0D, other.bounceHeight, 0.0D);
							}

							this.setDeltaMovement(newmotion);
							other.setDeltaMovement(othernewmotion);

							this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CCSoundEvents.TIN_DEFLECT.get(), SoundSource.BLOCKS, Math.min((float) d0 * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) d0 * 0.8F, 1.8F));
						}
					} else {
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
			if (this.getState() == GrazerState.BOUNCING || this.getState() == GrazerState.LANDING) {
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
					double adjustangle = this.calculateAdjustAngle(newmotion);
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

				double speed = newmotion.lengthSqr();
				this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CCSoundEvents.TIN_DEFLECT.get(), SoundSource.BLOCKS, Math.min((float) speed * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) speed * 0.8F, 1.8F));
			}
		}
	}

	private double calculateAdjustAngle(Vec3 newmotion) {
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

	private class GrazerLookControl extends LookControl {
		public GrazerLookControl() {
			super(Grazer.this);
		}

		public void tick() {
			if (this.lookAtCooldown > 0) {
				--this.lookAtCooldown;
				if (Grazer.this.getState() != GrazerState.BOUNCING) {
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