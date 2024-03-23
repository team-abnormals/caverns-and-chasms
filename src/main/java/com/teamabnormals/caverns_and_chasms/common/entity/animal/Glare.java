package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowLikedPlayerGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.GoToDarkSpotGoal;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class Glare extends PathfinderMob {
	private static final EntityDataAccessor<Boolean> DATA_GRUMPY = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_ANGRY_AT_UUID = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.OPTIONAL_UUID);

	public Glare(EntityType<? extends Glare> glare, Level level) {
		super(glare, level);
		this.moveControl = new FlyingMoveControl(this, 20, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FLYING_SPEED, 0.1F).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
		this.goalSelector.addGoal(2, new FollowLikedPlayerGoal(this, 1.75F));
		this.goalSelector.addGoal(3, new GoToDarkSpotGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));

		//TODO: More AI
		// Spin under Spore Blossoms
		// Look at ore blocks
		// Press down on big dripleaves
		// Approach axolotls
		// Shed leaf particles while moving
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_GRUMPY, false);
		this.entityData.define(DATA_OWNER_UUID, Optional.empty());
		this.entityData.define(DATA_ANGRY_AT_UUID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getOwnerUUID() != null) {
			tag.putUUID("Owner", this.getOwnerUUID());
		}
		if (this.getAngryAtUUID() != null) {
			tag.putUUID("AngryAt", this.getAngryAtUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.hasUUID("Owner")) {
			this.setOwnerUUID(tag.getUUID("Owner"));
		}
		if (tag.hasUUID("AngryAt")) {
			this.setAngryAtUUID(tag.getUUID("AngryAt"));
		}
	}


	public static boolean checkGlareSpawnRules(EntityType<? extends Glare> glare, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
		if (pos.getY() < 48 && level.getBrightness(LightLayer.SKY, pos) == 0) {
			int plants = 0;
			int range = 5;

			start:
			for (int i = -range; i <= range; i++) {
				for (int j = -range; j <= range; j++) {
					for (int k = -range; k <= range; k++) {
						if (level.getBlockState(pos.offset(i, j, k)).is(CCBlockTags.GLARE_SPAWNABLE_NEAR)) {
							plants++;
							if (plants > 10) {
								break start;
							}
						}
					}
				}
			}
			return plants > 0 && (plants > 10 || random.nextInt(plants) != 0);
		}
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level p_218342_) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	public static boolean shouldBeGrumpy(Level level, BlockPos pos) {
		DimensionType dimension = level.dimensionType();
		int i = dimension.monsterSpawnBlockLightLimit();
		return i >= 15 || level.getBrightness(LightLayer.BLOCK, pos) <= i;
	}

	public boolean isGrumpy() {
		return this.entityData.get(DATA_GRUMPY);
	}

	public void setGrumpy(boolean grumpy) {
		if (!this.level.isClientSide) {
			this.entityData.set(DATA_GRUMPY, grumpy);
		}
	}

	@Nullable
	public UUID getOwnerUUID() {
		return this.entityData.get(DATA_OWNER_UUID).orElse(null);
	}

	public void setOwnerUUID(@Nullable UUID uuid) {
		this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
	}

	@Nullable
	public UUID getAngryAtUUID() {
		return this.entityData.get(DATA_ANGRY_AT_UUID).orElse(null);
	}

	public void setAngryAtUUID(@Nullable UUID uuid) {
		this.entityData.set(DATA_ANGRY_AT_UUID, Optional.ofNullable(uuid));
	}

	@Override
	public void travel(Vec3 p_218382_) {
		if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
			if (this.isInWater()) {
				this.moveRelative(0.02F, p_218382_);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
			} else if (this.isInLava()) {
				this.moveRelative(0.02F, p_218382_);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
			} else {
				this.moveRelative(this.getSpeed(), p_218382_);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
			}
		}

		this.calculateEntityAnimation(this, false);
	}

	@Override
	protected float getStandingEyeHeight(Pose p_218356_, EntityDimensions p_218357_) {
		return p_218357_.height * 0.6F;
	}

	@Override
	public boolean causeFallDamage(float p_218321_, float p_218322_, DamageSource p_218323_) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		Entity attacker = source.getEntity();
		if (attacker instanceof Player player) {
			if (this.getOwnerUUID() != null && player.getUUID().equals(this.getOwnerUUID())) {
				this.playSound(CCSoundEvents.ENTITY_GLARE_UNTAME.get(), 1.0F, 1.0F);
				this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
				this.setOwnerUUID(null);
			}
			this.setAngryAtUUID(player.getUUID());
		}

		if (source.isFire()) {
			damage *= 2.0F;
		}

		return super.hurt(source, damage);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	protected void checkFallDamage(double p_218316_, boolean p_218317_, BlockState p_218318_, BlockPos p_218319_) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isGrumpy() ? CCSoundEvents.ENTITY_GLARE_ANGRY.get() : CCSoundEvents.ENTITY_GLARE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_218369_) {
		return CCSoundEvents.ENTITY_GLARE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_GLARE_DEATH.get();
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		boolean shouldBeGrumpy = shouldBeGrumpy(this.getLevel(), this.blockPosition());
		if ((this.isGrumpy() && !shouldBeGrumpy) || (!this.isGrumpy() && shouldBeGrumpy)) {
			this.setGrumpy(!this.isGrumpy());
			this.playAmbientSound();
		}
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(CCItemTags.GLARE_FOOD)) {
			this.level.playSound(player, this, CCSoundEvents.ENTITY_GLARE_EAT.get(), this.getSoundSource(), 1.0F, Mth.randomBetween(this.level.random, 0.8F, 1.2F));
			this.addParticlesAroundSelf(ParticleTypes.HEART);
			this.removeInteractionItem(player, stack);
			this.setAngryAtUUID(null);
			if (this.getOwnerUUID() == null) {
				this.playSound(CCSoundEvents.ENTITY_GLARE_TAME.get(), 1.0F, 1.0F);
				this.setOwnerUUID(player.getUUID());
			}
			return InteractionResult.SUCCESS;
		} else if (this.getOwnerUUID() == null && this.getAngryAtUUID() != player.getUUID()) {
			this.addParticlesAroundSelf(ParticleTypes.HEART);
			this.setOwnerUUID(player.getUUID());
			this.playSound(CCSoundEvents.ENTITY_GLARE_TAME.get(), 1.0F, 1.0F);
			return InteractionResult.SUCCESS;
		} else {
			return super.mobInteract(player, hand);
		}
	}

	@Override
	public boolean isFlapping() {
		return !this.isOnGround();
	}

	@Override
	public boolean removeWhenFarAway(double p_218384_) {
		return this.getOwnerUUID() == null;
	}

	@Override
	public boolean isPersistenceRequired() {
		return this.getOwnerUUID() != null;
	}

	@Override
	protected boolean shouldStayCloseToLeashHolder() {
		return true;
	}

	@Override
	public Iterable<BlockPos> iteratePathfindingStartNodeCandidatePositions() {
		AABB aabb = this.getBoundingBox();
		int i = Mth.floor(aabb.minX - 0.5D);
		int j = Mth.floor(aabb.maxX + 0.5D);
		int k = Mth.floor(aabb.minZ - 0.5D);
		int l = Mth.floor(aabb.maxZ + 0.5D);
		int i1 = Mth.floor(aabb.minY - 0.5D);
		int j1 = Mth.floor(aabb.maxY + 0.5D);
		return BlockPos.betweenClosed(i, i1, k, j, j1, l);
	}

	private void removeInteractionItem(Player player, ItemStack stack) {
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, (double) this.getEyeHeight() * 0.6D, (double) this.getBbWidth() * 0.1D);
	}

	@Override
	public void handleEntityEvent(byte p_239347_) {
		if (p_239347_ == 18) {
			for (int i = 0; i < 3; ++i) {
				this.addParticlesAroundSelf(ParticleTypes.HEART);
			}
		} else {
			super.handleEntityEvent(p_239347_);
		}
	}

	protected void addParticlesAroundSelf(ParticleOptions p_35288_) {
		for (int i = 0; i < 5; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(p_35288_, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
		}
	}
}
