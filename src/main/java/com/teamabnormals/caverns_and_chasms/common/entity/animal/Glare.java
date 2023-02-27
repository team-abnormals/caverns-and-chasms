package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowLikedPlayerGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.NearestViableOwnerGoal;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
	protected static final EntityDataAccessor<Optional<UUID>> DATA_LIKED_PLAYER_UUID = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.OPTIONAL_UUID);

	public Glare(EntityType<? extends Glare> glare, Level level) {
		super(glare, level);
		this.moveControl = new FlyingMoveControl(this, 20, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.FLYING_SPEED, 0.1F).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
		this.goalSelector.addGoal(2, new FollowLikedPlayerGoal(this, 1.75F));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));

		this.targetSelector.addGoal(0, new NearestViableOwnerGoal<>(this, Player.class, true, (entity) -> {
			IDataManager manager = (IDataManager) entity;
			Optional<UUID> ownedGlare = manager.getValue(CCDataProcessors.OWNED_GLARE_UUID);
			return ownedGlare.isEmpty() || entity.getLevel() instanceof ServerLevel server && server.getEntity(ownedGlare.get()) == null;
		}));

		//TODO: More AI
		// Spin under Spore Blossoms
		// Go to dark blocks
		// Look at ore blocks
		// Dislike player when hit until fed
		// Press down on big dripleaves
		// Approach axolotls
		// Shed leaf particles while moving
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_GRUMPY, false);
		this.entityData.define(DATA_LIKED_PLAYER_UUID, Optional.empty());
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
	public UUID getLikedPlayerUUID() {
		return this.entityData.get(DATA_LIKED_PLAYER_UUID).orElse(null);
	}

	public void setLikedPlayerUUID(@Nullable UUID uuid) {
		this.entityData.set(DATA_LIKED_PLAYER_UUID, Optional.ofNullable(uuid));
	}

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

	protected float getStandingEyeHeight(Pose p_218356_, EntityDimensions p_218357_) {
		return p_218357_.height * 0.6F;
	}

	public boolean causeFallDamage(float p_218321_, float p_218322_, DamageSource p_218323_) {
		return false;
	}

	public boolean hurt(DamageSource source, float damage) {
		Entity attacker = source.getEntity();
		if (attacker instanceof Player player) {
			IDataManager manager = (IDataManager) attacker;
			manager.setValue(CCDataProcessors.OWNED_GLARE_UUID, Optional.empty());
			if (this.getLikedPlayerUUID() != null && player.getUUID().equals(this.getLikedPlayerUUID())) {
				this.playSound(CCSoundEvents.ENTITY_GLARE_UNTAME.get(), 1.0F, 1.0F);
				this.setLikedPlayerUUID(null);
			}
		}

		if (source.isFire()) {
			damage *= 2.0F;
		}

		return super.hurt(source, damage);
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	protected void checkFallDamage(double p_218316_, boolean p_218317_, BlockState p_218318_, BlockPos p_218319_) {
	}

	protected SoundEvent getAmbientSound() {
		return this.isGrumpy() ? CCSoundEvents.ENTITY_GLARE_ANGRY.get() : CCSoundEvents.ENTITY_GLARE_AMBIENT.get();
	}

	protected SoundEvent getHurtSound(DamageSource p_218369_) {
		return CCSoundEvents.ENTITY_GLARE_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_GLARE_DEATH.get();
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	public void aiStep() {
		super.aiStep();
		if (this.isGrumpy() && !shouldBeGrumpy(this.getLevel(), this.blockPosition())) {
			this.setGrumpy(false);
			this.playAmbientSound();
		} else if (!this.isGrumpy() && shouldBeGrumpy(this.getLevel(), this.blockPosition())) {
			this.setGrumpy(true);
		}
	}

	public void tick() {
		super.tick();
	}

	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		//Optional<UUID> optional = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
// || (optional.isPresent() && player.getUUID().equals(optional.get()))
		if (stack.is(Items.GLOW_BERRIES)) {
			//this.getBrain().eraseMemory(CCMemoryModuleTypes.DISLIKED_PLAYER.get());
			this.level.playSound(player, this, CCSoundEvents.ENTITY_GLARE_EAT.get(), SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.level.random, 0.8F, 1.2F));
			for (int i = 0; i < 3; i++) {
				spawnHeartParticle();
			}
			this.removeInteractionItem(player, stack);
			return InteractionResult.SUCCESS;
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public boolean isFlapping() {
		return !this.isOnGround();
	}

	public boolean removeWhenFarAway(double p_218384_) {
		return false;
	}

	public void addAdditionalSaveData(CompoundTag p_218367_) {
		super.addAdditionalSaveData(p_218367_);
	}

	public void readAdditionalSaveData(CompoundTag p_218350_) {
		super.readAdditionalSaveData(p_218350_);
	}

	protected boolean shouldStayCloseToLeashHolder() {
		return false;
	}

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

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, (double) this.getEyeHeight() * 0.6D, (double) this.getBbWidth() * 0.1D);
	}

	public void handleEntityEvent(byte p_239347_) {
		if (p_239347_ == 18) {
			for (int i = 0; i < 3; ++i) {
				this.spawnHeartParticle();
			}
		} else {
			super.handleEntityEvent(p_239347_);
		}
	}

	private void spawnHeartParticle() {
		double d0 = this.random.nextGaussian() * 0.02D;
		double d1 = this.random.nextGaussian() * 0.02D;
		double d2 = this.random.nextGaussian() * 0.02D;
		this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
	}
}
