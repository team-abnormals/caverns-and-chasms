package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Fly extends PathfinderMob implements FlyingAnimal {
	@Nullable
	private int underWaterTicks;

	public Fly(EntityType<? extends Fly> fly, Level world) {
		super(fly, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		this.lookControl = new LookControl(this);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	@SuppressWarnings("deprecation")
	public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new TemptGoal(this, 1.25D, Ingredient.of(Items.ROTTEN_FLESH), false));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4F, true));
		this.goalSelector.addGoal(2, new WanderGoal());
		this.goalSelector.addGoal(3, new FloatGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, ZombieHorse.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
	}


	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return sizeIn.height * 0.5F;
	}

	public boolean doHurtTarget(Entity entityIn) {
		boolean flag = entityIn.hurt(DamageSource.sting(this), (float) ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
		if (flag) {
			this.doEnchantDamageEffects(this, entityIn);
			if (entityIn instanceof LivingEntity) {
				int i = 0;
				if (this.level.getDifficulty() == Difficulty.NORMAL) {
					i = 5;
				} else if (this.level.getDifficulty() == Difficulty.HARD) {
					i = 9;
				}

				if (i > 0) {
					((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, i * 20, 0));
				}
			}

			this.setTarget(null);
			this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
		}

		return flag;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 3;
	}

	protected void customServerAiStep() {
		if (this.isInWaterOrBubble()) {
			++this.underWaterTicks;
		} else {
			this.underWaterTicks = 0;
		}

		if (this.underWaterTicks > 20) {
			this.hurt(DamageSource.DROWN, 1.0F);
		}
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0D)
				.add(Attributes.FLYING_SPEED, 0.8F)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	protected PathNavigation createNavigation(Level worldIn) {
		FlyingPathNavigation navigator = new FlyingPathNavigation(this, worldIn) {
			public boolean isStableDestination(BlockPos pos) {
				return !this.level.getBlockState(pos.below()).isAir();
			}

			public void tick() {
				super.tick();
			}
		};
		navigator.setCanOpenDoors(false);
		navigator.setCanFloat(false);
		navigator.setCanPassDoors(true);
		return navigator;
	}


	protected SoundEvent getAmbientSound() {
		return null;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.BEE_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.BEE_DEATH;
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	protected boolean makeFlySound() {
		return true;
	}

	public boolean func_226391_a_(Entity p_226391_1_) {
		if (p_226391_1_ instanceof LivingEntity) {
			this.setLastHurtByMob((LivingEntity) p_226391_1_);
		}

		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			Entity entity = source.getEntity();
			if (!this.level.isClientSide && entity instanceof Player && !((Player) entity).isCreative() && this.isAlive() && !this.isNoAi()) {
				this.func_226391_a_(entity);
			}

			return super.hurt(source, amount);
		}
	}

	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	protected void handleFluidJump(TagKey<Fluid> fluidTag) {
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
	}

	public boolean canBeeContinueToUse() {
		return false;
	}

	@Override
	public boolean isFlying() {
		return false;
	}

	class WanderGoal extends Goal {

		WanderGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return Fly.this.navigation.isDone() && Fly.this.random.nextInt(10) == 0;
		}

		public boolean canContinueToUse() {
			return Fly.this.navigation.isInProgress();
		}

		public void start() {
			Vec3 vec3 = this.findPos();
			if (vec3 != null) {
				Fly.this.navigation.moveTo(Fly.this.navigation.createPath(new BlockPos(vec3), 1), 1.0D);
			}

		}

		@Nullable
		private Vec3 findPos() {
			Vec3 vec3 = Fly.this.getViewVector(0.0F);
			Vec3 vec32 = HoverRandomPos.getPos(Fly.this, 8, 7, vec3.x, vec3.z, ((float) Math.PI / 2F), 3, 1);
			return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(Fly.this, 8, 4, -2, vec3.x, vec3.z, (float) Math.PI / 2F);
		}
	}
}
