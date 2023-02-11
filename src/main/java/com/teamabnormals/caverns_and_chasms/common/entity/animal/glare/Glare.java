package com.teamabnormals.caverns_and_chasms.common.entity.animal.glare;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class Glare extends PathfinderMob {
	protected static final ImmutableList<SensorType<? extends Sensor<? super Glare>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY);
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.IS_PANICKING);

	public Glare(EntityType<? extends Glare> glare, Level level) {
		super(glare, level);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		PositionSource positionsource = new EntityPositionSource(this, this.getEyeHeight());
	}

	protected Brain.Provider<Glare> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	protected Brain<?> makeBrain(Dynamic<?> p_218344_) {
		return GlareAi.makeBrain(this.brainProvider().makeBrain(p_218344_));
	}

	public Brain<Glare> getBrain() {
		return (Brain<Glare>) super.getBrain();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.FLYING_SPEED, 0.1F).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	protected PathNavigation createNavigation(Level p_218342_) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
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
			Optional<UUID> optional = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (optional.isPresent() && player.getUUID().equals(optional.get())) {
				this.getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
			}
		}

		return super.hurt(source, damage);
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	protected void checkFallDamage(double p_218316_, boolean p_218317_, BlockState p_218318_, BlockPos p_218319_) {
	}

//	protected SoundEvent getAmbientSound() {
//		return this.hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.GLARE_AMBIENT_WITH_ITEM : SoundEvents.GLARE_AMBIENT_WITHOUT_ITEM;
//	}
//
//	protected SoundEvent getHurtSound(DamageSource p_218369_) {
//		return SoundEvents.GLARE_HURT;
//	}
//
//	protected SoundEvent getDeathSound() {
//		return SoundEvents.GLARE_DEATH;
//	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected void customServerAiStep() {
		this.level.getProfiler().push("glareBrain");
		this.getBrain().tick((ServerLevel) this.level, this);
		this.level.getProfiler().pop();
		this.level.getProfiler().push("glareActivityUpdate");
		GlareAi.updateActivity(this);
		this.level.getProfiler().pop();
		super.customServerAiStep();
	}

	public void aiStep() {
		super.aiStep();
	}

	public void tick() {
		super.tick();
	}

	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(Items.GLOW_BERRIES)) {
			this.removeInteractionItem(player, stack);
			//this.level.playSound(player, this, SoundEvents.GLARE_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
			this.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
			return InteractionResult.SUCCESS;
		} else if (hand == InteractionHand.MAIN_HAND && stack.isEmpty()) {
			//this.level.playSound(player, this, SoundEvents.GLARE_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
			this.getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
			return InteractionResult.SUCCESS;
		} else {
			return super.mobInteract(player, hand);
		}
	}

	protected void sendDebugPackets() {
		super.sendDebugPackets();
		DebugPackets.sendEntityBrain(this);
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
