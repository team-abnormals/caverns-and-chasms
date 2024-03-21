package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.CopperButtonBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowTuningForkGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.decoration.OxidizedCopperGolem;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

public class CopperGolem extends AbstractGolem implements ControllableGolem {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("A8EF581F-B1E8-4950-860C-06FA72505003");
	private static final EntityDataAccessor<Integer> OXIDATION = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);

	private int oxidationTime = this.nextOxidationTime();
	private int ticksSinceButtonPress;

	private int headSpinTicks;
	private int headSpinTicksO;
	private int buttonPressTicks;
	private int buttonPressTicksO;

	public CopperGolem(EntityType<? extends CopperGolem> entity, Level level) {
		super(entity, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FollowTuningForkGoal(this, 1.0D));
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
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
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
		compound.putBoolean("Waxed", this.isWaxed());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setOxidation(Oxidation.byId(compound.getInt("Oxidation")));
		if (compound.contains("OxidationTime")) {
			this.oxidationTime = compound.getInt("OxidationTime");
		}
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
	}

	public boolean isWaxed() {
		return this.entityData.get(WAXED);
	}

	public void setWaxed(boolean waxed) {
		this.entityData.set(WAXED, waxed);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide && this.isAlive() && !this.isWaxed() && --this.oxidationTime <= 0) {
			Oxidation oxidation = this.getOxidation();
			if (oxidation != Oxidation.WEATHERED) {
				this.setOxidation(Oxidation.byId(oxidation.getId() + 1));
				this.oxidationTime = this.nextOxidationTime();
			} else {
				this.oxidizeIntoStatue();
			}
		}

		this.updateHeadSpinTicks();
		this.updateButtonPressTicks();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level.isClientSide) {
			if (this.isWaxed() && this.tickCount % 40 == 0) {
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
				this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL, 1.0F, 1.0F);
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
				this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			} else if (this.getOxidation().getId() > 0) {
				this.setOxidation(Oxidation.byId(this.getOxidation().getId() - 1));
				this.oxidationTime = this.nextOxidationTime();
				this.spawnSparkParticles(ParticleTypes.SCRAPE);
				this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_SCRAPE, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			}

			if (success && !this.level.isClientSide) {
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
		this.level.broadcastEntityEvent(this, (byte) 5);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean damaged = this.isDamaged();
		boolean flag = super.hurt(source, amount);
		if (flag) {
			this.spinHead();
			this.level.broadcastEntityEvent(this, (byte) 4);
			if (this.isDamaged() != damaged)
				this.playSound(CCSoundEvents.ENTITY_COPPER_GOLEM_DAMAGE.get(), 1.0F, 1.0F);
		}
		return flag;
	}

	private void oxidizeIntoStatue() {
		OxidizedCopperGolem oxidizedgolem = CCEntityTypes.OXIDIZED_COPPER_GOLEM.get().create(this.level);

		float yRot = (float) Mth.floor((this.getYRot() + 22.5F) / 45.0F) * 45.0F;
		oxidizedgolem.moveTo(this.getX(), this.getY(), this.getZ(), yRot, 0.0F);
		oxidizedgolem.setYHeadRot(yRot);

		oxidizedgolem.setNoAi(this.isNoAi());
		oxidizedgolem.setSilent(this.isSilent());
		oxidizedgolem.setNoGravity(this.isNoGravity());
		oxidizedgolem.setGlowingTag(this.hasGlowingTag());
		oxidizedgolem.setInvulnerable(this.isInvulnerable());
		oxidizedgolem.setPersistenceRequired(this.isPersistenceRequired());

		oxidizedgolem.setDamaged(this.isDamaged());

		if (this.hasCustomName()) {
			oxidizedgolem.setCustomName(this.getCustomName());
			oxidizedgolem.setCustomNameVisible(this.isCustomNameVisible());
		}

		this.level.addFreshEntity(oxidizedgolem);
		this.discard();
	}

	public boolean isDamaged() {
		return this.getHealth() / this.getMaxHealth() < 0.5F;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return size.height * 0.8F;
	}

	@Override
	protected int decreaseAirSupply(int supply) {
		return supply;
	}

	@Override
	public void onTuningForkControlStart(Player controller) {
		this.spinHead();
	}

	@Override
	public void onTuningForkControlEnd(Player controller) {
		this.spinHead();
	}

	private int nextOxidationTime() {
		return this.random.nextInt(144000) + 240000;
	}

	public void spinHead() {
		if (this.headSpinTicks <= 10) {
			this.headSpinTicks = 24;
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

	public boolean isSpinningHead() {
		return this.headSpinTicks > 0;
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
		} else if (id == 5) {
			this.spinHead();
			this.spawnSparkParticles(ParticleTypes.ELECTRIC_SPARK);
		} else if (id == 6) {
			this.pressButton();
		}
		super.handleEntityEvent(id);
	}

	public static void createGolem(Level level, BlockPos pos, BlockState state) {
		float yRot = state.getValue(CarvedPumpkinBlock.FACING).toYRot();

		BlockPos abovepos = pos.above();
		BlockState abovestate = level.getBlockState(abovepos);

		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		level.setBlock(abovepos, Blocks.AIR.defaultBlockState(), 2);
		level.levelEvent(2001, pos, Block.getId(state));
		level.levelEvent(2001, abovepos, Block.getId(abovestate));

		LivingEntity golem;
		if (abovestate.is(CCBlocks.OXIDIZED_LIGHTNING_ROD.get()) || abovestate.is(CCBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get())) {
			yRot = (float) Mth.floor((yRot + 22.5F) / 45.0F) * 45.0F;
			golem = CCEntityTypes.OXIDIZED_COPPER_GOLEM.get().create(level);
		} else {
			CopperGolem coppergolem = CCEntityTypes.COPPER_GOLEM.get().create(level);
			coppergolem.setOxidation(abovestate.is(Blocks.LIGHTNING_ROD) || abovestate.is(CCBlocks.WAXED_LIGHTNING_ROD.get()) ? Oxidation.UNAFFECTED : abovestate.is(CCBlocks.EXPOSED_LIGHTNING_ROD.get()) || abovestate.is(CCBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get()) ? Oxidation.EXPOSED : Oxidation.WEATHERED);
			if (abovestate.is(CCBlockTags.WAXED_COPPER_BLOCKS)) {
				coppergolem.setWaxed(true);
			}
			golem = coppergolem;
		}

		golem.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.05D, (double) pos.getZ() + 0.5D, yRot, 0.0F);
		golem.setYHeadRot(yRot);
		golem.setYBodyRot(yRot);

		level.addFreshEntity(golem);
		for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0D))) {
			CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
		}

		level.blockUpdated(pos, Blocks.AIR);
		level.blockUpdated(abovepos, Blocks.AIR);
	}

	class RandomWalkingGoal extends WaterAvoidingRandomStrollGoal {
		private int walkDelay;

		public RandomWalkingGoal() {
			super(CopperGolem.this, 1.0D);
		}

		@Override
		public boolean canUse() {
			return CopperGolem.this.ticksSinceButtonPress <= 0 && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return (this.walkDelay > 0 || !this.mob.getNavigation().isDone()) && !this.mob.isVehicle();
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
			return !CopperGolem.this.isSpinningHead() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !CopperGolem.this.isSpinningHead() && super.canContinueToUse();
		}
	}

	class LookAroundRandomly extends RandomLookAroundGoal {
		public LookAroundRandomly() {
			super(CopperGolem.this);
		}

		@Override
		public boolean canUse() {
			return !CopperGolem.this.isSpinningHead() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !CopperGolem.this.isSpinningHead() && super.canContinueToUse();
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
				return !CopperGolem.this.isBeingTuningForkControlled() && this.findRandomButton();
			}
		}

		@Override
		public boolean canContinueToUse() {
			if (CopperGolem.this.isBeingTuningForkControlled()) {
				return false;
			} else if (this.tryTicks < -this.maxStayTicks || this.tryTicks > 1200) {
				return false;
			} else {
				return this.isUnpressedButton(CopperGolem.this.level, this.blockPos);
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
				if (this.tryTicks % 40 == 0) {
					this.moveMobToBlock();
				}
			} else {
				--this.tryTicks;
				--this.pressWaitTicks;

				if (this.pressWaitTicks == 6) {
					CopperGolem.this.pressButton();
					CopperGolem.this.level.broadcastEntityEvent(CopperGolem.this, (byte) 6);
				} else if (this.pressWaitTicks <= 0) {
					BlockState state = CopperGolem.this.level.getBlockState(this.blockPos);
					if (state.getBlock() instanceof CopperButtonBlock && !state.getValue(CopperButtonBlock.POWERED)) {
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
		WEATHERED(2);

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