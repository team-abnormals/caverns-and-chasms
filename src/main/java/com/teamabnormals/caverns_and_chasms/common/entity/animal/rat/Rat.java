package com.teamabnormals.caverns_and_chasms.common.entity.animal.rat;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat.*;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCLootTableProvider.CCGiftLoot;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCRatVariants;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Rat extends ShoulderRidingEntity implements VariantHolder<RatVariant>, NeutralMob {
	public static final float WOUNDED_THRESHOLD = 1.0F;
	public static final int SHAKE_TIME = 16;

	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

	private static final AttributeModifier SPEED_MODIFIER_WOUNDED = new AttributeModifier(UUID.fromString("5317A396-A13A-4019-92EA-F2BBB84769E2"), "Wounded speed reduction", -0.1D, AttributeModifier.Operation.MULTIPLY_BASE);

	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DIRTY = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RUNNING_AWAY = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SITTING_BECAUSE_ORDERED = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);

	private static final EntityDataAccessor<Float> ATTACH_ANGLE = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ATTACH_HEIGHT = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> FIRST_PERSON_POS = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.FLOAT);

	private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.INT);

	private List<Rat> pack = Lists.newArrayList();

	private BlockPos commandedPos;
	private LivingEntity commandedTarget;
	private int commandedTargetOwnerTimestamp;

	private Player tamer;
	private BlockPos rottenFleshPos;

	private LivingEntity attachedEntity;
	private UUID attachedEntityUUID;
	private float grip;
	private float prevHostXRot;
	private float prevHostYRot;
	private float prevHostDeltaRot;
	private int attachCooldown = 20;
	private final float animTimeOffset = this.random.nextFloat() * 10F;

	private int shakeAnim;
	private boolean wasWounded;

	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private UUID persistentAngerTarget;

	private boolean floatingInWater;

	public Rat(EntityType<? extends Rat> type, Level level) {
		super(type, level);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new RatAttachedToMobGoal(this));
		this.goalSelector.addGoal(1, new RatFloatGoal(this));
		this.goalSelector.addGoal(2, new RatSitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(3, new RatJumpAtTargetGoal(this));
		this.goalSelector.addGoal(4, new RatMeleeAttackGoal(this, 1.2D, true));
		this.goalSelector.addGoal(5, new RatTeleportToOwnerGoal(this));
		this.goalSelector.addGoal(6, new RatAvoidEntityGoal(this, 10.0F, 1.0F, 1.2F));
		this.goalSelector.addGoal(7, new RatGoToCommandedPosGoal(this, 1.2D));
		this.goalSelector.addGoal(8, new RatFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
		this.goalSelector.addGoal(9, new RatBreedGoal(this, 1.0D));
		this.goalSelector.addGoal(10, new RatTemptGoal(this));
		this.goalSelector.addGoal(11, new RatDevourRottenFleshGoal(this, 1.25D));
		this.goalSelector.addGoal(12, new RatStayInGroupGoal(this));
		this.goalSelector.addGoal(13, new RatFollowParentGoal(this));
		this.goalSelector.addGoal(14, new RatEatGoal(this));
		this.goalSelector.addGoal(15, new RatRandomStrollGoal(this));
		this.goalSelector.addGoal(16, new RatFindItemsGoal(this));
		this.goalSelector.addGoal(17, new RatLookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(18, new RatRandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new RatStopAttackingGoal(this));
		this.targetSelector.addGoal(1, new RatAttackCommandedTargetGoal(this));
		this.targetSelector.addGoal(2, new RatOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new RatOwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(4, (new RatHurtByTargetGoal(this)).setAlertOthers());
		this.targetSelector.addGoal(5, new RatNearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
		this.targetSelector.addGoal(6, new ResetUniversalAngerTargetGoal<>(this, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, CCRatVariants.BLUE.location().toString());
		this.entityData.define(COLLAR_COLOR, DyeColor.RED.getId());
		this.entityData.define(DIRTY, false);
		this.entityData.define(RUNNING_AWAY, false);
		this.entityData.define(SITTING_BECAUSE_ORDERED, false);
		this.entityData.define(EATING, false);
		this.entityData.define(ATTACH_ANGLE, 0.0F);
		this.entityData.define(ATTACH_HEIGHT, 0.0F);
		this.entityData.define(FIRST_PERSON_POS, 0.0F);
		this.entityData.define(REMAINING_ANGER_TIME, 0);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.4D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	public static boolean checkRatSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return Mime.isDarkEnoughToSpawnNoSkylight(level, pos, random) && checkMobSpawnRules(type, level, reason, pos, random);
	}

	@Override
	public boolean removeWhenFarAway(double distanceSqr) {
		return !this.isTame();
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		return -level.getPathfindingCostFromLightLevels(pos);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isWounded() ? CCSoundEvents.RAT_WOUNDED.get() : this.isAngry() ? CCSoundEvents.RAT_ANGRY.get() : CCSoundEvents.RAT_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return CCSoundEvents.RAT_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.RAT_DEATH.get();
	}

	@Override
	public SoundEvent getEatingSound(ItemStack stack) {
		SoundEvent sound = super.getEatingSound(stack);
		return sound == SoundEvents.GENERIC_EAT ? CCSoundEvents.RAT_EAT.get() : sound;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(CCSoundEvents.RAT_STEP.get(), 1.0F, 1.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("Variant", this.getStringVariant());
		tag.putByte("CollarColor", (byte) this.getCollarColor().getId());
		tag.putBoolean("Dirty", this.isDirty());
		if (this.isAttachedToEntity() && !(this.attachedEntity instanceof Player)) {
			tag.put("Pos", this.newDoubleList(this.attachedEntity.getX(), this.attachedEntity.getY(), this.attachedEntity.getZ()));
			tag.putUUID("AttachedUUID", this.attachedEntity.getUUID());
		}
		tag.putFloat("AttachAngle", this.getAttachAngle());
		tag.putFloat("AttachHeight", this.getAttachHeight());
		tag.putFloat("FirstPersonPos", this.getFirstPersonPos());
		BlockPos commandedPos = this.getCommandedPos();
		if (commandedPos != null) {
			tag.putInt("CommandedPosX", commandedPos.getX());
			tag.putInt("CommandedPosY", commandedPos.getY());
			tag.putInt("CommandedPosZ", commandedPos.getZ());
		}
		this.addPersistentAngerSaveData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setStringVariant(tag.getString("Variant"));
		this.setDirty(tag.getBoolean("Dirty"));
		if (tag.contains("CollarColor", 99)) {
			this.setCollarColor(DyeColor.byId(tag.getInt("CollarColor")));
		}
		if (tag.hasUUID("AttachedUUID")) {
			this.attachedEntityUUID = tag.getUUID("AttachedUUID");
		}
		this.setAttachAngle(tag.getFloat("AttachAngle"));
		this.setAttachHeight(tag.getFloat("AttachHeight"));
		this.setFirstPersonPos(tag.getFloat("FirstPersonPos"));
		if (tag.contains("CommandedPosX", 99) && tag.contains("CommandedPosY", 99) && tag.contains("CommandedPosZ", 99)) {
			this.setCommandedPos(new BlockPos(tag.getInt("CommandedPosX"), tag.getInt("CommandedPosY"), tag.getInt("CommandedPosZ")));
		}
		this.readPersistentAngerSaveData(this.level(), tag);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 8) {
			this.shakeAnim = SHAKE_TIME;
		} else {
			super.handleEntityEvent(id);
		}
	}

	public String getStringVariant() {
		return !this.entityData.get(VARIANT).isEmpty() ? this.entityData.get(VARIANT) : CCRatVariants.BLUE.location().toString();
	}

	private void setStringVariant(String var) {
		this.entityData.set(VARIANT, var);
	}

	@Override
	public void setVariant(RatVariant variant) {
		this.setVariant(this.level().registryAccess().registryOrThrow(CCRegistries.RAT_VARIANT).getKey(variant));
	}

	@Override
	public RatVariant getVariant() {
		return this.level().registryAccess().registryOrThrow(CCRegistries.RAT_VARIANT).get(new ResourceLocation(this.getStringVariant()));
	}

	public void setVariant(ResourceLocation variant) {
		this.setStringVariant(variant.toString());
	}

	public DyeColor getCollarColor() {
		return DyeColor.byId(this.entityData.get(COLLAR_COLOR));
	}

	public void setCollarColor(DyeColor color) {
		this.entityData.set(COLLAR_COLOR, color.getId());
	}

	public boolean isDirty() {
		return this.entityData.get(DIRTY);
	}

	public void setDirty(boolean dirty) {
		this.entityData.set(DIRTY, dirty);
	}

	public boolean isRunningAway() {
		return this.entityData.get(RUNNING_AWAY);
	}

	public void setRunningAway(boolean runningAway) {
		this.entityData.set(RUNNING_AWAY, runningAway);
	}

	public boolean isSittingBecauseOrdered() {
		return this.entityData.get(SITTING_BECAUSE_ORDERED);
	}

	public void setSittingBecauseOrdered(boolean sitting) {
		this.entityData.set(SITTING_BECAUSE_ORDERED, sitting);
	}

	public boolean isEating() {
		return this.entityData.get(EATING);
	}

	public void setEating(boolean eating) {
		this.entityData.set(EATING, eating);
	}

	public boolean isFloatingInWater() {
		return this.floatingInWater;
	}

	public void setFloatingInWater(boolean floating) {
		this.floatingInWater = floating;
	}

	public boolean canSit() {
		return !this.isAttachedToEntity() && this.onGround() && !this.isInWaterOrBubble() && !this.isFloatingInWater();
	}

	public boolean isSitting() {
		return this.isSittingBecauseOrdered() || this.isEating();
	}

	public boolean shouldFollowOwner() {
		return !this.isSittingBecauseOrdered() && this.getCommandedPos() == null;
	}

	// Attach to entity stuff
	public void setAttachedToEntity(LivingEntity target) {
		((RatHolder) target).attachRat(this);
		this.attachedEntity = target;
		this.noPhysics = true;
		this.blocksBuilding = false;
		this.prevHostXRot = target.getXRot();
		this.prevHostYRot = target.getYRot();
	}

	public void tryToAttachToEntity(LivingEntity target) {
		if (((RatHolder) target).canHoldMoreRats()) {
			List<Integer> availableslots = Lists.newArrayList(-1, 0, 1);
			for (Rat attachedrat : ((RatHolder) target).getAttachedRats())
				availableslots.remove(Integer.valueOf(Math.round(attachedrat.getFirstPersonPos())));

			this.setAttachedToEntity(target);
			this.setAttachAngle(this.getRandom().nextFloat() * 360.0F);
			this.setAttachHeight((0.25F + this.getRandom().nextFloat() * Math.max(target.getEyeHeight() - 0.5F, 0.0F)) / target.getBbHeight());

			this.grip = 20F;
			if (target instanceof Mob mob && mob.getTarget() == this)
				mob.setTarget(null);
			if (target.getLastHurtByMob() == this)
				target.setLastHurtByMob(null);

			int i = (availableslots.isEmpty() ? this.getRandom().nextInt(3) - 1 : availableslots.get(this.getRandom().nextInt(availableslots.size())));
			this.setFirstPersonPos(i + (this.getRandom().nextFloat() - 0.5F) * 0.6F);

			this.playSound(CCSoundEvents.RAT_LATCH.get(), 1.0F, 1.0F);
		}
	}

	public void setDetachedFromEntity() {
		((RatHolder) this.attachedEntity).detachRat(this);
		this.attachedEntity = null;
		this.noPhysics = false;
		this.blocksBuilding = true;
	}

	public void detachFromEntity() {
		if (this.isAttachedToEntity()) {
			float bbWidth = this.getBbWidth();
			float bbHeight = this.getBbHeight();
			double attachDist = this.getHorizontalAttachDist(this.attachedEntity);
			double attachBbHeight = this.attachedEntity.getBbHeight();
			double searchWidth = attachDist * 2.0D + 1.0E-6D;
			double searchHeight = attachBbHeight + 1.0E-6D;

			VoxelShape searchShape = Shapes.create(AABB.ofSize(this.attachedEntity.position().add(0.0D, attachBbHeight / 2.0D, 0.0D), searchWidth, searchHeight, searchWidth));
			this.level().findFreePosition(this, searchShape, this.position(), bbWidth, bbHeight, bbWidth).ifPresent(this::setPos);
			this.setDetachedFromEntity();
			this.attachCooldown = 80;
		}
	}

	public void tickAttached() {
		this.setDeltaMovement(Vec3.ZERO);
		if (this.canUpdate())
			this.tick();
		this.updateAttachedPosition();
	}

	public void updateAttachedPosition() {
		if (this.isAttachedToEntity()) {
			Vec3 vec3 = (new Vec3(0.0D, this.attachedEntity.getBbHeight() * this.getAttachHeight(), this.getHorizontalAttachDist(this.attachedEntity))).yRot(-(this.attachedEntity.yBodyRot + this.getAttachAngle()) * Mth.DEG_TO_RAD);
			// Maybe this should use moveTo when the host teleports like with passengers?
			this.setPos(this.attachedEntity.position().add(vec3));
			this.setYRot((float) (Mth.atan2(this.attachedEntity.getZ() - this.getZ(), this.attachedEntity.getX() - this.getX()) * Mth.RAD_TO_DEG - 90.0F));
			this.yHeadRot = this.getYRot();
			this.yBodyRot = this.getYRot();
		}
	}

	public double getHorizontalAttachDist(LivingEntity entity) {
		return entity.getBbWidth() / 2.0D + 0.15D;
	}

	public LivingEntity getAttachedEntity() {
		return this.attachedEntity;
	}

	public boolean isAttachedToEntity() {
		return this.attachedEntity != null;
	}

	public float getAttachAngle() {
		return this.entityData.get(ATTACH_ANGLE);
	}

	public void setAttachAngle(float angle) {
		this.entityData.set(ATTACH_ANGLE, angle);
	}

	public float getAttachHeight() {
		return this.entityData.get(ATTACH_HEIGHT);
	}

	public void setAttachHeight(float height) {
		this.entityData.set(ATTACH_HEIGHT, height);
	}

	public float getFirstPersonPos() {
		return this.entityData.get(FIRST_PERSON_POS);
	}

	public void setFirstPersonPos(float pos) {
		this.entityData.set(FIRST_PERSON_POS, pos);
	}

	public float getAnimTimeOffset() {
		return this.animTimeOffset;
	}

	public boolean isOnAttachCooldown() {
		return this.attachCooldown > 0;
	}

	@Override
	public boolean isPushable() {
		return !this.isAttachedToEntity() && super.isPushable();
	}

	@Override
	protected void doPush(Entity entity) {
		if (!this.isAttachedToEntity()) {
			if (!this.level().isClientSide && this.isSittingBecauseOrdered() && entity instanceof ServerPlayer player && player == this.getOwner() && player.isCrouching()) {
				this.setEntityOnShoulder(player);
			} else {
				super.doPush(entity);
			}
		}
	}

	@Override
	protected void pushEntities() {
		if (!this.isAttachedToEntity())
			super.pushEntities();
	}

	@Override
	public boolean startRiding(Entity entity, boolean p_19967_) {
		return !this.isAttachedToEntity() && super.startRiding(entity, p_19967_);
	}

	@Override
	public void setHealth(float health) {
		super.setHealth(health);
		if (this.isAlive() && this.isAddedToWorld() && !this.level().isClientSide()) {
			AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			if (attributeinstance != null) {
				attributeinstance.removeModifier(SPEED_MODIFIER_WOUNDED);
				if (this.isWounded()) {
					attributeinstance.addTransientModifier(SPEED_MODIFIER_WOUNDED);
				}
			}
		}
	}

	public void loosenGrip(float amount) {
		this.grip -= amount;
	}

	@Override
	public void tick() {
		super.tick();

		// TODO: Make this better
		if (this.isAttachedToEntity() && (!this.attachedEntity.isAlive() || this.attachedEntity.isSpectator())) {
			this.detachFromEntity();
		} else if (!this.level().isClientSide) {
			if (this.isAttachedToEntity()) {
				boolean shouldflyoff;

				if (this.attachedEntity instanceof Player) {
					float deltaXRot = Mth.degreesDifference(this.attachedEntity.getXRot(), this.prevHostXRot);
					float deltaYRot = Mth.degreesDifference(this.attachedEntity.getYRot(), this.prevHostYRot);
					float deltaRot = Mth.sqrt(deltaXRot * deltaXRot + deltaYRot * deltaYRot);
					float deltaRotAcc = Mth.abs(deltaRot - this.prevHostDeltaRot);

					if (deltaRotAcc > 30.0D) {
						this.grip--;
					} else {
						this.grip = Math.min(this.grip + 0.35F, 20F);
					}

					this.prevHostXRot = this.attachedEntity.getXRot();
					this.prevHostYRot = this.attachedEntity.getYRot();
					this.prevHostDeltaRot = deltaRot;

					shouldflyoff = this.grip <= 0F;
				} else {
					shouldflyoff = this.random.nextInt(100) == 0;
				}

				if (shouldflyoff) {
					if (!(this.attachedEntity instanceof Player))
						this.attachedEntity.swing(InteractionHand.MAIN_HAND);
					this.attachedEntity.setLastHurtByMob(this);
					this.setDeltaMovement(new Vec3(0.0D, 0.3D, 0.4D).yRot(-(this.attachedEntity.yBodyRot + this.getAttachAngle()) * Mth.DEG_TO_RAD));
					this.detachFromEntity();
				}
			} else if (this.attachedEntityUUID != null) {
				Entity entity = ((ServerLevel) this.level()).getEntity(this.attachedEntityUUID);
				if (entity instanceof LivingEntity living) {
					this.tryToAttachToEntity(living);
				} else {
					CavernsAndChasms.LOGGER.warn("Could not find entity the rat was attached to with UUID: {}", this.attachedEntityUUID);
				}
			}

			this.attachedEntityUUID = null;
		}
	}

	@Override
	public void aiStep() {
		if (this.isAlive()) {
			if (this.isEffectiveAi()) {
				if (this.attachCooldown > 0)
					--this.attachCooldown;

				if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
					this.heal(1.0F);
				}

				boolean wounded = this.isWounded();

				if (this.wasWounded && !wounded) {
					this.level().broadcastEntityEvent(this, (byte) 8);
				}

				this.wasWounded = wounded;

				if (this.commandedTarget != null && !this.commandedTarget.isAlive()) {
					this.commandedTarget = null;
				}

				LivingEntity owner = this.getOwner();
				if (owner != null && owner.tickCount < this.commandedTargetOwnerTimestamp) {
					this.commandedTargetOwnerTimestamp = owner.tickCount;
				}

				List<Rat> rats = this.level().getEntitiesOfClass(Rat.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), this::isAdultOfSamePack);
				this.pack = rats.stream().sorted(Comparator.comparing(this::distanceToSqr)).limit(4).collect(Collectors.toList());
			}

			if (this.level().isClientSide) {
				this.shakeAnim = Math.max(0, this.shakeAnim - 1);
			}

			if (this.tickCount % 5 == 0 && this.isEating()) {
				ItemStack stack = this.getMainHandItem();
				if (this.level().isClientSide) {
					for (int i = 0; i < 2; ++i) {
						Vec3 vec3 = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
						vec3 = vec3.xRot(-this.getXRot() * Mth.DEG_TO_RAD);
						vec3 = vec3.yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
						double d0 = (double) (-this.random.nextFloat()) * 0.3D - 0.15D;
						Vec3 vec31 = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.15D, d0, 0.3D);
						vec31 = vec31.xRot(-this.getXRot() * Mth.DEG_TO_RAD);
						vec31 = vec31.yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
						vec31 = vec31.add(this.getX(), this.getEyeY(), this.getZ());
						this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
					}
				} else {
					this.playSound(this.getEatingSound(stack), 1.0F, 1.0F);
				}
			}
		}

		super.aiStep();

		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();

		if (this.isTame()) {
			if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
				this.usePlayerItem(player, hand, stack);
				this.heal((float) item.getFoodProperties().getNutrition());
				this.gameEvent(GameEvent.EAT, this);

				return InteractionResult.sidedSuccess(this.level().isClientSide);
			} else if (item instanceof DyeItem) {
				DyeColor dyecolor = ((DyeItem) item).getDyeColor();
				if (dyecolor != this.getCollarColor()) {
					this.setCollarColor(dyecolor);
					this.usePlayerItem(player, hand, stack);

					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}
			} else {
				InteractionResult interactionresult = super.mobInteract(player, hand);
				if (!interactionresult.consumesAction() && this.isOwnedBy(player)) {
					this.jumping = false;
					this.navigation.stop();
					this.setTarget(null);
					this.setOrderedToSit(!this.isOrderedToSit());

					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}

				return interactionresult;
			}
		} else if (!this.isAngry()) {
			if (!this.isRunningAway() && stack.is(CCItemTags.RAT_TAME_ITEMS)) {
				this.usePlayerItem(player, hand, stack);
				if (!this.level().isClientSide) {
					if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
						this.tame(player);
						this.navigation.stop();
						this.setTarget(null);
						this.setCommandedTarget(null);
						this.setOrderedToSit(true);
						this.setDirty(false);
						this.level().broadcastEntityEvent(this, (byte) 7);
					} else {
						this.level().broadcastEntityEvent(this, (byte) 6);
					}
				}

				return InteractionResult.sidedSuccess(this.level().isClientSide);
			} else if (this.isDirty() && stack.is(BlueprintItemTags.BUCKETS_WATER)) {
				this.level().playSound(null, this, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, stack.getCraftingRemainingItem()));
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				this.setDirty(false);
				if (!this.level().isClientSide) {
					ServerLevel serverlevel = (ServerLevel) this.level();
					for (int i = 0; i < 15; ++i) {
						serverlevel.sendParticles(ParticleTypes.SPLASH, this.getX() + (random.nextDouble() - random.nextDouble()) * 0.4F, this.getY() + random.nextDouble() * 0.3F, this.getZ() + (random.nextDouble() - random.nextDouble()) * 0.4F, 1, 0.0D, 0.0D, 0.0D, 1.0D);
					}
				}

				this.level().playSound(null, this, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}

			return super.mobInteract(player, hand);
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		DamageSource source = entity == this.getAttachedEntity() ? this.damageSources().noAggroMobAttack(this) : this.damageSources().mobAttack(this);
		boolean flag = entity.hurt(source, (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag) {
			this.doEnchantDamageEffects(this, entity);
		}

		return flag;
	}

	// Taming stuff
	public void setMassTamedBy(Player player, BlockPos rottenFleshPos) {
		this.tamer = player;
		this.rottenFleshPos = rottenFleshPos;
	}

	public void setTamer(Player entity) {
		this.tamer = entity;
	}

	public Player getTamer() {
		return this.tamer;
	}

	public BlockPos getRottenFleshPos() {
		return this.rottenFleshPos;
	}

	// TODO: alliedTo stuff
	public boolean isAdultOfSamePack(Rat rat) {
		return !rat.isBaby() && rat.isAlive() && rat.getOwner() == this.getOwner();
	}

	// Pack stuff
	public List<Rat> getPack() {
		return this.pack;
	}

	public boolean hasPack() {
		return !this.pack.isEmpty();
	}

	public boolean hasBraveryToFight() {
		return !this.isWounded() && (this.isTame() || (this.isBaby() ? this.pack.size() > 2 : this.pack.size() > 1));
	}

	// NeutralMob Stuff
	@Override
	public int getRemainingPersistentAngerTime() {
		return this.entityData.get(REMAINING_ANGER_TIME);
	}

	@Override
	public void setRemainingPersistentAngerTime(int time) {
		this.entityData.set(REMAINING_ANGER_TIME, time);
	}

	@Override
	public UUID getPersistentAngerTarget() {
		return this.persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(UUID target) {
		this.persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}

	@Override
	public void stopBeingAngry() {
		this.setPersistentAngerTarget(null);
		this.setTarget(null);
		this.setRemainingPersistentAngerTime(0);
	}

	public boolean isWounded() {
		return this.getHealth() <= WOUNDED_THRESHOLD;
	}

	public boolean isVisuallyWounded() {
		return this.isWounded() || this.shakeAnim > SHAKE_TIME / 2;
	}

	public boolean isScaredOf(LivingEntity entity) {
		if (this.getOwner() == entity)
			return false;
		else if (!this.isTame() && entity instanceof Player)
			return true;
		else if (this.getLastHurtByMob() == entity)
			return true;
		else if (entity instanceof Mob mob && mob.getTarget() == this)
			return true;
		else
			return false;
	}

	// Bone flute command stuff
	public void setCommandedPos(BlockPos pos) {
		this.commandedPos = pos;
	}

	public BlockPos getCommandedPos() {
		return this.commandedPos;
	}

	public void setCommandedTarget(LivingEntity target) {
		this.commandedTarget = target;
		LivingEntity owner = this.getOwner();
		if (owner != null) {
			this.commandedTargetOwnerTimestamp = owner.tickCount;
		}
	}

	public LivingEntity getCommandedTarget() {
		return this.commandedTarget;
	}

	public int getCommandedTargetOwnerTimestamp() {
		return this.commandedTargetOwnerTimestamp;
	}

	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
		return this.hasBraveryToFight() && canRatsAttack(target, owner);
	}

	// TODO: Look at the canAttack method
	public static boolean canRatsAttack(LivingEntity target, LivingEntity owner) {
		if (target instanceof Creeper || target instanceof Ghast) {
			return false;
		} else if (target instanceof Wolf wolf) {
			return !wolf.isTame() || wolf.getOwner() != owner;
		} else if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
			return false;
		} else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
			return false;
		} else {
			return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
		}
	}

	@Override
	public boolean canBeSeenAsEnemy() {
		return !this.isAttachedToEntity() && super.canBeSeenAsEnemy();
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);

		if (this.dead && !source.is(DamageTypeTags.NO_ANGER) && source.getEntity() instanceof LivingEntity living && HURT_BY_TARGETING.test(this, living) && this.isWithinRestriction(living.blockPosition()))
			this.alertOthers(living);
	}

	public void alertOthers(LivingEntity target) {
		double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
		AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(d0, 10.0D, d0);
		List<Rat> list = this.level().getEntitiesOfClass(Rat.class, aabb);
		Iterator<Rat> iterator = list.iterator();

		Rat rat;

		while (true) {
			if (!iterator.hasNext())
				return;

			rat = iterator.next();

			if (this != rat && rat.getTarget() == null && (this.getOwner() == rat.getOwner() && !rat.isAlliedTo(target)) && rat.hasBraveryToFight())
				rat.setTarget(target);
		}
	}

	public Vec3 findPackCenter(List<Rat> pack) {
		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;

		for (Rat friend : pack) {
			x += friend.getX();
			y += friend.getY();
			z += friend.getZ();
		}

		return new Vec3(x / pack.size(), y / pack.size(), z / pack.size());
	}

	public float getTailWagAmount() {
		return calculateTailWagAmount(this.getHealth(), this.getMaxHealth());
	}

	public static float calculateTailWagAmount(float health, float maxHealth) {
		float f = Mth.clamp(1.0F - (maxHealth - health) / (maxHealth - 1.0F), 0.0F, 1.0F);
		return f * 1.5F;
	}

	@OnlyIn(Dist.CLIENT)
	public float getShakeAnim(float partialTick) {
		return this.shakeAnim > 0 ? Mth.lerp(partialTick, this.shakeAnim, this.shakeAnim - 1) : 0;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		if (random.nextFloat() < 0.2F) {
			ServerLevel serverLevel = (ServerLevel) this.level();
			LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(CCGiftLoot.RAT_SPAWN_ITEMS);
			LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.GIFT);
			List<ItemStack> list = lootTable.getRandomItems(lootParams);

			if (!list.isEmpty()) {
				this.setItemSlot(EquipmentSlot.MAINHAND, list.get(0));
				this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			}
		}
	}

	@Override
	public boolean isFood(ItemStack itemstack) {
		return itemstack.is(CCItemTags.RAT_FOOD);
	}

	@Override
	public boolean canTakeItem(ItemStack stack) {
		EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(stack);
		if (!this.getItemBySlot(equipmentslottype).isEmpty()) {
			return false;
		} else {
			return equipmentslottype == EquipmentSlot.MAINHAND && super.canTakeItem(stack);
		}
	}

	@Override
	public boolean canHoldItem(ItemStack stack) {
		ItemStack currentStack = this.getMainHandItem();
		return stack.getItem().isEdible() && (currentStack.isEmpty() || !currentStack.getItem().isEdible());
	}

	private void spitOutItem(ItemStack stackIn) {
		if (!stackIn.isEmpty() && !this.level().isClientSide) {
			ItemEntity itementity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, stackIn);
			itementity.setPickUpDelay(40);
			itementity.setThrower(this.getUUID());
			this.playSound(CCSoundEvents.RAT_SPIT.get(), 1.0F, 1.0F);
			this.level().addFreshEntity(itementity);
		}
	}

	private void spawnItem(ItemStack stackIn) {
		ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stackIn);
		this.level().addFreshEntity(itementity);
	}

	@Override
	protected void pickUpItem(ItemEntity itemEntity) {
		ItemStack itemstack = itemEntity.getItem();
		if (this.canHoldItem(itemstack)) {
			int i = itemstack.getCount();
			if (i > 1) {
				this.spawnItem(itemstack.split(i - 1));
			}

			this.spitOutItem(this.getMainHandItem());
			this.onItemPickup(itemEntity);
			this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
			this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			this.take(itemEntity, itemstack.getCount());
			itemEntity.discard();
		}
	}

	@Override
	protected void dropEquipment() {
		super.dropEquipment();
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		if (!itemstack.isEmpty()) {
			this.spawnAtLocation(itemstack);
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (DATA_FLAGS_ID.equals(key)) {
			this.refreshDimensions();
		}
		super.refreshDimensions();
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return this.isSitting() ? size.height : size.height * 0.5F;
	}

	@Override
	public void awardKillScore(Entity target, int deathScore, DamageSource damageSource) {
		super.awardKillScore(target, deathScore, damageSource);
		if (target == this.commandedTarget && this.getOwner() instanceof ServerPlayer serverPlayer) {
			CCCriteriaTriggers.RAT_KILLED_ENTITY.trigger(serverPlayer, this, target, damageSource);
		}
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.getEffect() != MobEffects.HUNGER && super.canBeAffected(effect);
	}

	@Override
	public void tame(Player player) {
		super.tame(player);
		this.setTameAttributes(true);
	}

	public void setTameAttributes(boolean tamed) {
		if (tamed) {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
			this.setHealth(10.0F);
		} else {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4.0D);
		}
	}

	@Override
	public void spawnChildFromBreeding(ServerLevel level, Animal otherParent) {
		AgeableMob firstbaby = null;
		int babies = this.random.nextInt(4) + 1;
		boolean babyspawned = false;

		for (int i = 0; i < babies; ++i) {
			AgeableMob ageablemob = this.getBreedOffspring(level, otherParent);
			final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, otherParent, ageablemob);
			final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
			ageablemob = event.getChild();
			if (cancelled) {
				this.setAge(6000);
				otherParent.setAge(6000);
				this.resetLove();
				otherParent.resetLove();
				return;
			}
			if (ageablemob != null) {
				babyspawned = true;

				if (firstbaby == null) {
					firstbaby = ageablemob;
				}

				ageablemob.setBaby(true);
				ageablemob.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
				level.addFreshEntityWithPassengers(ageablemob);
				level.broadcastEntityEvent(this, (byte) 18);
			}
		}

		if (babyspawned) {
			ServerPlayer serverplayer = this.getLoveCause();
			if (serverplayer == null && otherParent.getLoveCause() != null) {
				serverplayer = otherParent.getLoveCause();
			}

			if (serverplayer != null) {
				serverplayer.awardStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this, otherParent, firstbaby);
			}

			this.setAge(6000);
			otherParent.setAge(6000);
			this.resetLove();
			otherParent.resetLove();
			if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				level.addFreshEntity(new ExperienceOrb(level, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
			}
		}
	}

	@Override
	public Rat getBreedOffspring(ServerLevel level, AgeableMob parent) {
		Rat child = CCEntityTypes.RAT.get().create(level);
		if (child != null && parent instanceof Rat rat) {
			child.setVariant(this.random.nextBoolean() ? rat.getVariant() : this.getVariant());
			if (this.isTame()) {
				child.setOwnerUUID(this.getOwnerUUID());
				child.setTame(true);
				child.setTameAttributes(true);
				child.setCollarColor(this.random.nextBoolean() ? rat.getCollarColor() : this.getCollarColor());
			}
		}

		return child;
	}

	@Override
	public void positionRider(Entity passenger, Entity.MoveFunction function) {
		super.positionRider(passenger, function);
		float f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
		float f1 = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
		function.accept(passenger, this.getX() + (double) (0.1F * f), this.getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, this.getZ() - (double) (0.1F * f1));
		if (passenger instanceof LivingEntity living) {
			living.yBodyRot = this.yBodyRot;
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag dataTag) {
		groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, dataTag);
		this.setVariant(RatVariant.getSpawnVariant(level.registryAccess(), this.random).value());
		this.setDirty(this.random.nextBoolean());
		this.populateDefaultEquipmentSlots(this.random, difficulty);
		if (spawnType == MobSpawnType.NATURAL && this.random.nextDouble() < CCConfig.COMMON.ratPackSpawnChance.get()) {
			List<Pair<Rat, Vec3>> rats = Lists.newArrayList();
			int min = CCConfig.COMMON.minimumRatPackSize.get();
			int max = CCConfig.COMMON.maximumRatPackSize.get();
			if (max >= min) {
				int range = max - min;
				int ratCount = min + random.nextInt(range / 2 + 1) + random.nextInt(range / 2 + 1);

				for (int i = 0; i < 64; ++i) {
					int spawnRange = 6;
					double d0 = this.getX() + (random.nextDouble() - random.nextDouble()) * (double) spawnRange;
					double d1 = this.getY() + (random.nextDouble() - random.nextDouble()) * (double) spawnRange / 2;
					double d2 = this.getZ() + (random.nextDouble() - random.nextDouble()) * (double) spawnRange;

					if (rats.size() < ratCount) {
						if (level.noCollision(CCEntityTypes.RAT.get().getAABB(d0, d1, d2)) && SpawnPlacements.checkSpawnRules(CCEntityTypes.RAT.get(), level, MobSpawnType.NATURAL, BlockPos.containing(d0, d1, d2), random)) {
							Rat rat = CCEntityTypes.RAT.get().create(level.getLevel());
							if (rat != null) {
								rats.add(Pair.of(rat, new Vec3(d0, d1, d2)));
							}
						}
					} else {
						break;
					}
				}
			}

			if (rats.size() > 6) {
				for (Pair<Rat, Vec3> pair : rats) {
					Rat rat = pair.getFirst();
					Vec3 ratPos = pair.getSecond();
					rat.moveTo(ratPos.x(), ratPos.y(), ratPos.z(), level.getRandom().nextFloat() * 360.0F, 0.0F);
					groupData = rat.finalizeSpawn(level, level.getCurrentDifficultyAt(rat.blockPosition()), MobSpawnType.EVENT, groupData, null);
					level.addFreshEntity(rat);
					rat.spawnAnim();
				}
			}
		}

		return groupData;
	}
}