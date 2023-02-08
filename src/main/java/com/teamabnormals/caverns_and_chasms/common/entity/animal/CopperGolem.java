package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.CopperButtonBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowTuningForkGoal;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.*;

public class CopperGolem extends AbstractGolem implements ControllableGolem {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("A8EF581F-B1E8-4950-860C-06FA72505003");
	private static final EntityDataAccessor<Integer> OXIDATION = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<UUID>> CONTROLLER_UUID = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.OPTIONAL_UUID);

	private int oxidationTime = this.nextOxidationTime();
	private int ticksSinceButtonPress;

	private int forgetControllerTime;
    @Nullable
    private BlockPos tuningForkPos;

	public long lastHit;

	private int headSpinTicks;
	private int headSpinTicksO;
	private int buttonPressTicks;
	private int buttonPressTicksO;

	public CopperGolem(EntityType<? extends AbstractGolem> entity, Level level) {
		super(entity, level);
		this.moveControl = new CopperGolem.CopperGolemMoveControl();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new CopperGolem.CopperGolemFollowTuningForkGoal());
		this.goalSelector.addGoal(1, new CopperGolem.PressButtonGoal());
		this.goalSelector.addGoal(2, new CopperGolem.RandomWalkingGoal());
		this.goalSelector.addGoal(3, new CopperGolem.StareAtPlayerGoal());
		this.goalSelector.addGoal(4, new CopperGolem.LookAroundRandomly());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(OXIDATION, 0);
		this.entityData.define(WAXED, false);
		this.entityData.define(CONTROLLER_UUID, Optional.empty());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return this.isStatue() ? Entity.MovementEmission.NONE : super.getMovementEmission();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return CCSoundEvents.ENTITY_COPPER_GOLEM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_COPPER_GOLEM_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(CCSoundEvents.ENTITY_COPPER_GOLEM_STEP.get(), 1.0F, 1.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Oxidation", this.getOxidation().getId());
		compound.putInt("OxidationTime", this.oxidationTime);
		compound.putInt("TicksSinceButtonPress", this.ticksSinceButtonPress);
		compound.putBoolean("Waxed", this.isWaxed());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setOxidation(Oxidation.byId(compound.getInt("Oxidation")));
		if (compound.contains("OxidationTime")) {
			this.oxidationTime = compound.getInt("OxidationTime");
		}
		this.ticksSinceButtonPress = compound.getInt("TicksSinceButtonPress");
		this.setWaxed(compound.getBoolean("Waxed"));
	}

	public Oxidation getOxidation() {
		return Oxidation.byId(this.entityData.get(OXIDATION));
	}

	public void setOxidation(Oxidation oxidation) {
		this.entityData.set(OXIDATION, oxidation.getId());

		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		attributeinstance.removeModifier(SPEED_MODIFIER_UUID);
		if (oxidation != Oxidation.UNAFFECTED) {
			double penalty = oxidation == Oxidation.EXPOSED ? -0.075D : oxidation == Oxidation.WEATHERED ? -0.15D : -0.25D;
			attributeinstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_UUID, "Weathering speed penalty", penalty, AttributeModifier.Operation.ADDITION));
		}

		if (oxidation == Oxidation.OXIDIZED) {
			this.maxUpStep = 0.0F;
		} else {
			this.maxUpStep = 1.0F;
		}
	}

	public boolean isStatue() {
		return this.getOxidation() == Oxidation.OXIDIZED;
	}

	public boolean isWaxed() {
		return this.entityData.get(WAXED);
	}

	public void setWaxed(boolean waxed) {
		this.entityData.set(WAXED, waxed);
	}

	@Override
	public boolean canBeControlled(Player controller) {
		return !this.isStatue();
	}

	@Override
	public void onTuningForkControl(Player controller) {
		this.spinHead();
	}

	@Override
	public boolean shouldMoveToTuningForkPos(BlockPos pos, Player controller) {
		return true;
	}

    @Override
    public boolean shouldAttackTuningForkTarget(LivingEntity target, Player controller) {
        return false;
    }

	@Override
	public void setControllerUUID(UUID uuid) {
		this.entityData.set(CONTROLLER_UUID, Optional.ofNullable(uuid));
	}

	@Nullable
	@Override
	public UUID getControllerUUID() {
		return this.entityData.get(CONTROLLER_UUID).orElse((UUID) null);
	}

	@Override
	public void setForgetControllerTime(int time) {
		this.forgetControllerTime = time;
	}

	@Override
	public int getForgetControllerTime() {
		return this.forgetControllerTime;
	}

	@Override
	public void setTuningForkPos(BlockPos pos) {
        this.tuningForkPos = pos;
    }

    @Nullable
    @Override
    public BlockPos getTuningForkPos() {
        return this.tuningForkPos;
    }

	@Override
	public void setTuningForkTarget(LivingEntity target) {}

	@Nullable
	@Override
	public LivingEntity getTuningForkTarget() {
		return null;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide && this.isAlive() && !this.isWaxed() && !this.isStatue() && --this.oxidationTime <= 0 && this.oxidize()) {
			this.oxidationTime = this.nextOxidationTime();
		}

		this.updateHeadSpinTicks();
		this.updateButtonPressTicks();
	}

	@Override
	public void aiStep() {
		if (this.isStatue() || this.isImmobile()) {
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
		} else {
			if (this.ticksSinceButtonPress > 0) {
				--this.ticksSinceButtonPress;
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		boolean success = false;
		if (item == Items.COPPER_INGOT) {
			float f = this.getHealth();
			this.heal(15.0F);
			if (this.getHealth() != f) {
				this.playSound(CCSoundEvents.ENTITY_COPPER_GOLEM_REPAIR.get(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				success = true;
			}
		} else if (item == Items.HONEYCOMB) {
			if (!this.isWaxed()) {
				this.setWaxed(true);
				this.spawnSparkParticles(ParticleTypes.WAX_ON);
				level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL, 1.0F, 1.0F);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				success = true;
			}
		} else if (item instanceof AxeItem) {
			if (this.isWaxed()) {
				this.setWaxed(false);
				this.oxidationTime = this.nextOxidationTime();
				this.spawnSparkParticles(ParticleTypes.WAX_OFF);
				level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			} else if (this.deoxidize()) {
				this.oxidationTime = this.nextOxidationTime();
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
		if (this.getOxidation() != Oxidation.UNAFFECTED) {
			this.setOxidation(Oxidation.UNAFFECTED);
		}

		this.spinHead();
		this.level.broadcastEntityEvent(this, (byte) 6);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (this.isStatue()) {
			if (!this.level.isClientSide && !this.isRemoved()) {
				Entity directentity = source.getDirectEntity();
				if (DamageSource.OUT_OF_WORLD.equals(source)) {
					this.removeStatue();
					return false;
				} else if (source.isExplosion()) {
					this.breakStatue(source, true, false);
					return false;
				} else if ("player".equals(source.getMsgId()) && directentity instanceof Player && ((Player) directentity).getAbilities().mayBuild) {
					if (((Player) directentity).getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG)) {
						this.breakStatue(source, true, true);
						return true;
					} else if (source.isCreativePlayer()) {
						this.breakStatue(source, false, false);
						return true;
					} else {
						long i = this.level.getGameTime();
						if (i - this.lastHit > 5L) {
							this.level.broadcastEntityEvent(this, (byte) 10);
							this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
							this.lastHit = i;
						}

						return true;
					}
				}
			}

			return false;
		} else {
			boolean damaged = this.isDamaged();
			boolean flag = super.hurt(source, amount);
			if (flag) {
				this.spinHead();
				this.level.broadcastEntityEvent(this, (byte) 4);
				if (this.isDamaged() != damaged)
					this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
			}
			return flag;
		}
	}

	private void breakStatue(DamageSource source, boolean dropLoot, boolean brokenWithPickaxe) {
		if (dropLoot) {
			if (brokenWithPickaxe) {
				Block.popResource(this.level, this.blockPosition(), new ItemStack(CCItems.OXIDIZED_COPPER_GOLEM.get()));
			} else {
				this.dropAllDeathLoot(source);
			}
		}

		((ServerLevel) this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OXIDIZED_COPPER.defaultBlockState()), this.getX(), this.getY(0.6666666666666666D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F), (double) (this.getBbHeight() / 4.0F), (double) (this.getBbWidth() / 4.0F), 0.05D);
		this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_BREAK, this.getSoundSource(), 1.0F, 1.0F);
		this.removeStatue();
	}

	private void removeStatue() {
		this.remove(RemovalReason.KILLED);
	}

	public boolean isDamaged() {
		return this.getHealth() / this.getMaxHealth() < 0.5F;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return size.height * 0.7F;
	}

	@Override
	public void knockback(double x, double y, double z) {
		if (!this.isStatue()) {
			super.knockback(x, y, z);
		}
	}

	@Override
	public boolean isPushable() {
		return !this.isStatue() && super.isPushable();
	}

	@Override
	public void push(double x, double y, double z) {
		if (!this.isStatue()) {
			super.push(x, y, z);
		}
	}

	@Override
	protected int decreaseAirSupply(int supply) {
		return supply;
	}

	@Override
	protected float tickHeadTurn(float yRot, float xRot) {
		if (this.isStatue()) {
			this.yBodyRotO = this.yRotO;
			this.yBodyRot = this.getYRot();
			return 0.0F;
		} else {
			return super.tickHeadTurn(yRot, xRot);
		}
	}

	@Override
	public void calculateEntityAnimation(LivingEntity entity, boolean isFlying) {
		entity.animationSpeedOld = entity.animationSpeed;
		double d0 = this.isStatue() ? 0.0D : entity.getX() - entity.xo;
		double d1 = this.isStatue() ? 0.0D : entity.getZ() - entity.zo;
		float f = (float) Math.sqrt(d0 * d0 + d1 * d1) * 4.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		entity.animationSpeed += (f - entity.animationSpeed) * 0.4F;
		entity.animationPosition += entity.animationSpeed;
	}

	private boolean oxidize() {
		return this.changeOxidation(1);
	}

	private boolean deoxidize() {
		return this.changeOxidation(-1);
	}

	private boolean changeOxidation(int amount) {
		Oxidation oxidation = this.getOxidation();
		this.setOxidation(Oxidation.byId(Mth.clamp(oxidation.getId() + amount, 0, 3)));
		return this.getOxidation() != oxidation;
	}

	private int nextOxidationTime() {
		return this.random.nextInt(144000) + 240000;
	}

	private void spinHead() {
		if (this.headSpinTicks <= 12) {
			this.headSpinTicks = 22;
			this.headSpinTicksO = this.headSpinTicks;
			this.playSound(CCSoundEvents.ENTITY_COPPER_GOLEM_GEAR.get(), 1.0F, 1.0F);
		}
	}

	private void pressButton() {
		this.buttonPressTicks = 12;
		this.buttonPressTicksO = this.buttonPressTicks;
	}

	private void updateHeadSpinTicks() {
		this.headSpinTicksO = this.headSpinTicks;
		if (this.headSpinTicks > 0) {
			--this.headSpinTicks;
		}
	}

	private void updateButtonPressTicks() {
		this.buttonPressTicksO = this.buttonPressTicks;
		if (this.buttonPressTicks > 0) {
			--this.buttonPressTicks;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadSpinTicks(float partialTicks) {
		return Mth.lerp(partialTicks, this.headSpinTicksO, this.headSpinTicks);
	}

	@OnlyIn(Dist.CLIENT)
	public float getPressButtonTicks(float partialTicks) {
		return Mth.lerp(partialTicks, this.buttonPressTicksO, this.buttonPressTicks);
	}

	private void spawnSparkParticles(ParticleOptions particle) {
		if (this.level.isClientSide) {
			for (int i = 0; i < 7; ++i) {
				double d0 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d1 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d2 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				this.level.addParticle(particle, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
	}

	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.spinHead();
		} else if (id == 6) {
			this.spinHead();
			this.spawnSparkParticles(ParticleTypes.ELECTRIC_SPARK);
		} else if (id == 8) {
			this.pressButton();
		} else if (id == 10) {
			if (this.level.isClientSide) {
				this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_BREAK, this.getSoundSource(), 1.0F, 1.0F, false);
				this.lastHit = this.level.getGameTime();
			}
		}
		super.handleEntityEvent(id);
	}

	public class CopperGolemLookControl extends LookControl {
		public CopperGolemLookControl() {
			super(CopperGolem.this);
		}

		public void tick() {
			if (!CopperGolem.this.isStatue()) {
				super.tick();
			}
		}
	}

	class CopperGolemMoveControl extends MoveControl {
		public CopperGolemMoveControl() {
			super(CopperGolem.this);
		}

		public void tick() {
			if (!CopperGolem.this.isStatue()) {
				super.tick();
			}
		}
	}

	class RandomWalkingGoal extends WaterAvoidingRandomStrollGoal {
		private int walkDelay;

		public RandomWalkingGoal() {
			super(CopperGolem.this, 1.0D);
		}

		@Override
		public boolean canUse() {
			return !CopperGolem.this.isStatue() && CopperGolem.this.ticksSinceButtonPress <= 0 && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return (this.walkDelay > 0 || !this.mob.getNavigation().isDone()) && !CopperGolem.this.isStatue() && !this.mob.isVehicle();
		}

		@Override
		public void start() {
			this.walkDelay = 10;
			CopperGolem.this.spinHead();
			CopperGolem.this.level.broadcastEntityEvent(CopperGolem.this, (byte) 4);
		}

		@Override
		public void tick() {
			if (this.walkDelay > 0) {
				--this.walkDelay;
				if (this.walkDelay == 0) {
					CopperGolem.this.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
				}
			}
		}
	}

	class StareAtPlayerGoal extends LookAtPlayerGoal {
		public StareAtPlayerGoal() {
			super(CopperGolem.this, Player.class, 6.0F);
		}

		@Override
		public boolean canUse() {
			return CopperGolem.this.headSpinTicks <= 0 && !CopperGolem.this.isStatue() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return CopperGolem.this.headSpinTicks <= 0 && !CopperGolem.this.isStatue() && super.canContinueToUse();
		}
	}

	class LookAroundRandomly extends RandomLookAroundGoal {
		public LookAroundRandomly() {
			super(CopperGolem.this);
		}

		@Override
		public boolean canUse() {
			return CopperGolem.this.headSpinTicks <= 0 && !CopperGolem.this.isStatue() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return CopperGolem.this.headSpinTicks <= 0 && !CopperGolem.this.isStatue() && super.canContinueToUse();
		}
	}

	class CopperGolemFollowTuningForkGoal extends FollowTuningForkGoal {
		public CopperGolemFollowTuningForkGoal() {
			super(CopperGolem.this, 1.0D);
		}

		@Override
		public boolean canUse() {
			return !CopperGolem.this.isStatue() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !CopperGolem.this.isStatue() && super.canContinueToUse();
		}
	}

	class PressButtonGoal extends Goal {
		private BlockPos blockPos = BlockPos.ZERO;
		private Vec3i buttonNormal;
		private int nextStartTicks;
		private int tryTicks;
		private int maxStayTicks;
		private int pressWaitTicks;

		public PressButtonGoal() {
			super();
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			if (this.nextStartTicks > 0) {
				--this.nextStartTicks;
				return false;
			} else {
				this.nextStartTicks = 20;
				return !CopperGolem.this.isStatue() && this.findRandomButton();
			}
		}

		@Override
		public boolean canContinueToUse() {
			if (this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200) {
				return !CopperGolem.this.isStatue() && this.isUnpressedButton(CopperGolem.this.level, this.blockPos);
			} else {
				return false;
			}
		}

		@Override
		public void start() {
			this.moveMobToBlock();
			this.tryTicks = 0;
			this.maxStayTicks = CopperGolem.this.getRandom().nextInt(CopperGolem.this.getRandom().nextInt(1200) + 1200) + 1200;
			this.pressWaitTicks = 16;
		}

		@Override
		public void tick() {
			CopperGolem.this.getLookControl().setLookAt(this.blockPos.getX() + 0.5D + this.buttonNormal.getX() * 0.5D, this.blockPos.getY() + 0.5D + this.buttonNormal.getY() * 0.5D, this.blockPos.getZ() + 0.5D + this.buttonNormal.getZ() * 0.5D, 10.0F, CopperGolem.this.getMaxHeadXRot());

			if (CopperGolem.this.distanceToSqr(this.blockPos.getX() + 0.5D, this.blockPos.getY() + 0.5D, this.blockPos.getZ() + 0.5D) > 1.5625D) {
				++this.tryTicks;
				this.pressWaitTicks = 20;
				if (this.tryTicks % 20 == 0) {
					this.moveMobToBlock();
				}
			} else {
				--this.tryTicks;
				--this.pressWaitTicks;

				if (this.pressWaitTicks == 6) {
					CopperGolem.this.pressButton();
					CopperGolem.this.level.broadcastEntityEvent(CopperGolem.this, (byte) 8);
				} else if (this.pressWaitTicks <= 0) {
					BlockState state = CopperGolem.this.level.getBlockState(this.blockPos);
					if (!state.getValue(CopperButtonBlock.POWERED)) {
						((CopperButtonBlock) state.getBlock()).press(state, CopperGolem.this.level, this.blockPos);
						CopperGolem.this.level.playSound(null, this.blockPos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
						CopperGolem.this.level.gameEvent(CopperGolem.this, GameEvent.BLOCK_ACTIVATE, this.blockPos);
						CopperGolem.this.ticksSinceButtonPress = 80;
					}
				}
			}
		}

		@Override
		public void stop() {
			CopperGolem.this.getNavigation().stop();
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		private void moveMobToBlock() {
			CopperGolem.this.getNavigation().moveTo(CopperGolem.this.getNavigation().createPath(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(), 0), 1.0D);
		}

		private boolean findRandomButton() {
			List<BlockPos> buttonpositions = Lists.newArrayList();

			for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(CopperGolem.this.getX() - 8.0D), Mth.floor(CopperGolem.this.getY() - 4.0D), Mth.floor(CopperGolem.this.getZ() - 8.0D), Mth.floor(CopperGolem.this.getX() + 8.0D), Mth.floor(CopperGolem.this.getY() + 4.0D), Mth.floor(CopperGolem.this.getZ() + 8.0D))) {
				if (CopperGolem.this.isWithinRestriction(pos) && this.isUnpressedButton(CopperGolem.this.level, pos)) {
					buttonpositions.add(new BlockPos(pos));
				}
			}

			if (buttonpositions.size() > 0) {
				this.blockPos = buttonpositions.get(CopperGolem.this.getRandom().nextInt(buttonpositions.size()));
				BlockState state = CopperGolem.this.level.getBlockState(this.blockPos);
				AttachFace face = state.getValue(CopperButtonBlock.FACE);
				Direction direction = face == AttachFace.CEILING ? Direction.UP : face == AttachFace.FLOOR ? Direction.DOWN : state.getValue(CopperButtonBlock.FACING).getOpposite();
				this.buttonNormal = direction.getNormal();
				return true;
			}

			return false;
		}

		private boolean isUnpressedButton(LevelReader level, BlockPos pos) {
			BlockState state = level.getBlockState(pos);
			BlockPos belowpos = pos.below();
			BlockState belowstate = level.getBlockState(belowpos);
			return state.getBlock() instanceof CopperButtonBlock && !state.getValue(CopperButtonBlock.POWERED) && belowstate.entityCanStandOn(level, belowpos, CopperGolem.this);
		}
	}

	public enum Oxidation {
		UNAFFECTED(0),
		EXPOSED(1),
		WEATHERED(2),
		OXIDIZED(3);

		private static final Oxidation[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Oxidation::getId)).toArray(Oxidation[]::new);

		private final int id;

		Oxidation(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static Oxidation byId(int id) {
			if (id < 0 || id >= VALUES.length) {
				id = 0;
			}

			return VALUES[id];
		}
	}
}