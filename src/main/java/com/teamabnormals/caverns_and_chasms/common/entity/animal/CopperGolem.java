package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CopperGolem extends AbstractGolem {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("A8EF581F-B1E8-4950-860C-06FA72505003");
	private static final EntityDataAccessor<Integer> WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);

	public CopperGolem(EntityType<? extends AbstractGolem> entity, Level level) {
		super(entity, level);
		this.lookControl = new CopperGolem.CopperGolemLookControl();
		this.moveControl = new CopperGolem.CopperGolemMoveControl();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new CopperGolem.BeFrozenGoal());
		this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(WEATHER_STATE, 0);
		this.entityData.define(WAXED, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return size.height * 0.7F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("WeatherAmount", this.getWeatherAmount().getId());
		compound.putBoolean("Waxed", this.isWaxed());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setWeatherAmount(WeatherAmount.byId(compound.getInt("WeatherAmount")));
		this.setWaxed(compound.getBoolean("Waxed"));
	}

	public WeatherAmount getWeatherAmount() {
		return WeatherAmount.byId(this.entityData.get(WEATHER_STATE));
	}

	public void setWeatherAmount(WeatherAmount amount) {
		this.entityData.set(WEATHER_STATE, amount.getId());

		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		attributeinstance.removeModifier(SPEED_MODIFIER_UUID);
		attributeinstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_UUID, "Weathering speed penalty", -amount.getSpeedPenalty(), AttributeModifier.Operation.ADDITION));
	}

	public boolean isWaxed() {
		return this.entityData.get(WAXED);
	}

	public void setWaxed(boolean waxed) {
		this.entityData.set(WAXED, waxed);
	}

	@Override
	public void aiStep() {
		if (this.isFrozen() || this.isImmobile()) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.zza = 0.0F;
		}

		super.aiStep();

		if (this.level.isClientSide) {
			if (this.isWaxed() && this.tickCount % 30 == 0) {
				double d0 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d1 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d2 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				this.level.addParticle(ParticleTypes.WAX_ON, this.getRandomX(0.8D), this.getY(0.1D), this.getRandomZ(0.8D), d0, d1, d2);
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		boolean success = false;
		if (item == Items.HONEYCOMB) {
			if (!this.isWaxed()) {
				this.setWaxed(true);
				this.spawnSparkParticles(ParticleTypes.WAX_ON);
				level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL, 1.0F, 1.0F);
				itemstack.shrink(1);
				success = true;
			}
		} else if ((item instanceof AxeItem)) {
			if (this.isWaxed()) {
				this.setWaxed(false);
				this.spawnSparkParticles(ParticleTypes.WAX_OFF);
				level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			} else if (this.deoxidize()) {
				this.spawnSparkParticles(ParticleTypes.SCRAPE);
				level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_SCRAPE, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			}

			if (success) {
				itemstack.hurtAndBreak(1, player, (entity) -> {
					entity.broadcastBreakEvent(hand);
				});
			}
		}

		return success ? InteractionResult.sidedSuccess(this.level.isClientSide) : InteractionResult.PASS;
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
		if (this.getWeatherAmount() != WeatherAmount.UNAFFECTED) {
			this.setWeatherAmount(WeatherAmount.UNAFFECTED);
			this.spawnSparkParticles(ParticleTypes.ELECTRIC_SPARK);
		}
	}

	public boolean isFrozen() {
		return this.getWeatherAmount() == WeatherAmount.OXIDIZED;
	}

	public boolean oxidize() {
		return this.changeWeatherAmount(1);
	}

	public boolean deoxidize() {
		return this.changeWeatherAmount(-1);
	}

	private boolean changeWeatherAmount(int amount) {
		WeatherAmount weatheramount = this.getWeatherAmount();
		this.setWeatherAmount(WeatherAmount.byId(Mth.clamp(weatheramount.getId() + amount, 0, 3)));
		return this.getWeatherAmount() != weatheramount;
	}

	private void spawnSparkParticles(ParticleOptions particle) {
		if (this.level.isClientSide) {
			for(int i = 0; i < 7; ++i) {
				double d0 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d1 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d2 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				this.level.addParticle(particle, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
	}

	public class CopperGolemLookControl extends LookControl {
		public CopperGolemLookControl() {
			super(CopperGolem.this);
		}

		public void tick() {
			if (!CopperGolem.this.isFrozen()) {
				super.tick();
			}
		}

		protected boolean resetXRotOnTick() {
			return !CopperGolem.this.isFrozen();
		}
	}

	class CopperGolemMoveControl extends MoveControl {
		public CopperGolemMoveControl() {
			super(CopperGolem.this);
		}

		public void tick() {
			if (!CopperGolem.this.isFrozen()) {
				super.tick();
			}

		}
	}

	class BeFrozenGoal extends Goal {
		public BeFrozenGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
		}

		public boolean canUse() {
			return CopperGolem.this.isFrozen();
		}

		public boolean canContinueToUse() {
			return CopperGolem.this.isFrozen();
		}

		public void start() {
			CopperGolem.this.getNavigation().stop();
		}
	}

	public static enum WeatherAmount {
		UNAFFECTED(0, 0.0D),
		EXPOSED(1, 0.05D),
		WEATHERED(2, 0.1D),
		OXIDIZED(3, 0.2D);

		private static final WeatherAmount[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(WeatherAmount::getId)).toArray(WeatherAmount[]::new);

		private final int id;
		private final double speedPenalty;
		private final LazyLoadedValue<ResourceLocation> textureLocation = new LazyLoadedValue<>(() -> new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/copper_golem/copper_golem_" + this.name().toLowerCase(Locale.ROOT) + ".png"));

		WeatherAmount(int id, double speedPenalty) {
			this.id = id;
			this.speedPenalty = speedPenalty;
		}

		public int getId() {
			return this.id;
		}

		public double getSpeedPenalty() {
			return this.speedPenalty;
		}

		public ResourceLocation getTextureLocation() {
			return this.textureLocation.get();
		}

		public static WeatherAmount byId(int id) {
			if (id < 0 || id >= VALUES.length) {
				id = 0;
			}

			return VALUES[id];
		}
	}
}