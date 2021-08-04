package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieChickenEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.Tag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FlyEntity extends CreatureEntity implements IFlyingAnimal {
	@Nullable
	private int underWaterTicks;

	public FlyEntity(EntityType<? extends FlyEntity> fly, World world) {
		super(fly, world);
		this.moveControl = new FlyingMovementController(this, 20, true);
		this.lookControl = new LookController(this);
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
		this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
		this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	@SuppressWarnings("deprecation")
	public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new TemptGoal(this, 1.25D, Ingredient.of(Items.ROTTEN_FLESH), false));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double) 1.4F, true));
		this.goalSelector.addGoal(2, new WanderGoal());
		this.goalSelector.addGoal(3, new SwimGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(new Class[0]));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<ZombieEntity>(this, ZombieEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<ZombieChickenEntity>(this, ZombieChickenEntity.class, true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<ZombieHorseEntity>(this, ZombieHorseEntity.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
	}

	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
	}

	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
	}


	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
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
					((LivingEntity) entityIn).addEffect(new EffectInstance(Effects.WEAKNESS, i * 20, 0));
				}
			}

			this.setTarget((LivingEntity) null);
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

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0D)
				.add(Attributes.FLYING_SPEED, 0.8F)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	@SuppressWarnings("deprecation")
	protected PathNavigator createNavigation(World worldIn) {
		FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
			public boolean isStableDestination(BlockPos pos) {
				return !this.level.getBlockState(pos.below()).isAir();
			}

			public void tick() {
				super.tick();
			}
		};
		flyingpathnavigator.setCanOpenDoors(false);
		flyingpathnavigator.setCanFloat(false);
		flyingpathnavigator.setCanPassDoors(true);
		return flyingpathnavigator;
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
			if (!this.level.isClientSide && entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() && this.canSee(entity) && !this.isNoAi()) {
				this.func_226391_a_(entity);
			}

			return super.hurt(source, amount);
		}
	}

	public CreatureAttribute getMobType() {
		return CreatureAttribute.ARTHROPOD;
	}

	protected void handleFluidJump(Tag<Fluid> fluidTag) {
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
	}

	public boolean canBeeContinueToUse() {
		return false;
	}

	class WanderGoal extends Goal {
		WanderGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		public boolean canUse() {
			return FlyEntity.this.navigation.isDone() && FlyEntity.this.random.nextInt(10) == 0;
		}

		public boolean canContinueToUse() {
			return FlyEntity.this.navigation.isInProgress();
		}

		public void start() {
			Vector3d vec3d = this.findPos();
			if (vec3d != null) {
				FlyEntity.this.navigation.moveTo(FlyEntity.this.navigation.createPath(new BlockPos(vec3d), 1), 1.0D);
			}
		}

		@Nullable
		private Vector3d findPos() {
			Vector3d vec3d;
			vec3d = FlyEntity.this.getViewVector(0.0F);

			Vector3d vec3d2 = RandomPositionGenerator.getAboveLandPos(FlyEntity.this, 8, 7, vec3d, ((float) Math.PI / 2F), 2, 1);
			return vec3d2 != null ? vec3d2 : RandomPositionGenerator.getAirPos(FlyEntity.this, 8, 4, -2, vec3d, (double) ((float) Math.PI / 2F));
		}
	}
}
