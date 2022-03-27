package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class Rat extends ShoulderRidingEntity {
	private static final Predicate<Rat> FRIEND_RATS = (entity) -> !entity.isTame() && !entity.isBaby() && entity.isAlive();
	private static final Predicate<ItemEntity> ALLOWED_ITEMS = (entity) -> !entity.hasPickUpDelay() && entity.isAlive();
	private static final Predicate<Entity> AVOID_PLAYERS = (entity) -> !entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);

	private static final EntityDataAccessor<Integer> RAT_TYPE = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> TRUSTING = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RUNNING_AWAY = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);

	private List<Rat> group = Lists.newArrayList();
	private int eatTicks;

	public Rat(EntityType<? extends Rat> type, Level worldIn) {
		super(type, worldIn);
		this.setCanPickUpLoot(true);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
		this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new Rat.RatTemptGoal());
		this.goalSelector.addGoal(6, new Rat.RatJumpOnOwnersShoulderGoal());
		this.goalSelector.addGoal(7, new Rat.RatStayInGroupGoal());
		this.goalSelector.addGoal(8, new Rat.RatFollowParentGoal());
		this.goalSelector.addGoal(9, new Rat.RatAvoidEntityGoal<>(Player.class, 10.0F, 1.0F, 1.2F, AVOID_PLAYERS::test));
		this.goalSelector.addGoal(10, new Rat.RatRandomStrollGoal());
		this.goalSelector.addGoal(11, new Rat.RatFindItemsGoal());
		this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new Rat.RatStopAttackingGoal());
		this.targetSelector.addGoal(4, (new Rat.RatHurtByTargetGoal()).setAlertOthers());
		this.targetSelector.addGoal(5, new Rat.RatRandomTargetGoal<>(Player.class, true, null));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RAT_TYPE, 0);
		this.entityData.define(COLLAR_COLOR, DyeColor.RED.getId());
		this.entityData.define(TRUSTING, false);
		this.entityData.define(RUNNING_AWAY, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.4D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.FOX_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.FOX_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.FOX_DEATH;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Type", this.getRatType());
		compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
		compound.putBoolean("Trusting", this.isTrusting());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setRatType(compound.getInt("Type"));
		if (compound.contains("CollarColor", 99)) {
			this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
		}
		this.setTrusting(compound.getBoolean("Trusting"));
	}

	private void setRatType(int id) {
		this.entityData.set(RAT_TYPE, id);
	}

	public int getRatType() {
		return this.entityData.get(RAT_TYPE);
	}

	public DyeColor getCollarColor() {
		return DyeColor.byId(this.entityData.get(COLLAR_COLOR));
	}

	public void setCollarColor(DyeColor color) {
		this.entityData.set(COLLAR_COLOR, color.getId());
	}

	boolean isTrusting() {
		return this.entityData.get(TRUSTING);
	}

	private void setTrusting(boolean trusting) {
		this.entityData.set(TRUSTING, trusting);
	}

	boolean isRunningAway() {
		return this.entityData.get(RUNNING_AWAY);
	}

	private void setRunningAway(boolean runningAway) {
		this.entityData.set(RUNNING_AWAY, runningAway);
	}

	public void aiStep() {
		if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi()) {
			++this.eatTicks;
			ItemStack itemstack = this.getMainHandItem();
			if (this.canEatItem(itemstack)) {
				if (this.eatTicks > 600) {
					ItemStack itemstack1 = itemstack.finishUsingItem(this.level, this);
					if (!itemstack1.isEmpty()) {
						this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
					}

					this.eatTicks = 0;
				} else if (this.eatTicks > 560 && this.random.nextFloat() < 0.1F) {
					this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
					this.level.broadcastEntityEvent(this, (byte) 45);
				}
			}

			List<Rat> rats = this.level.getEntitiesOfClass(Rat.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), FRIEND_RATS);
			rats.sort(Comparator.comparing((entity) -> this.distanceToSqr(entity)));
			this.group = rats.stream().limit(4).collect(Collectors.toList());
		}

		super.aiStep();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();

		if (this.isTame()) {
			if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
				this.usePlayerItem(player, hand, itemstack);
				this.heal((float)item.getFoodProperties().getNutrition());
				this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());

				return InteractionResult.sidedSuccess(this.level.isClientSide);
			} else if (item instanceof DyeItem) {
				DyeColor dyecolor = ((DyeItem) item).getDyeColor();
				if (dyecolor != this.getCollarColor()) {
					this.setCollarColor(dyecolor);
					this.usePlayerItem(player, hand, itemstack);

					return InteractionResult.sidedSuccess(this.level.isClientSide);
				}
			} else {
				InteractionResult interactionresult = super.mobInteract(player, hand);
				if (!interactionresult.consumesAction() && this.isOwnedBy(player)) {
					this.jumping = false;
					this.navigation.stop();
					this.setTarget(null);
					this.setOrderedToSit(!this.isOrderedToSit());

					return InteractionResult.sidedSuccess(this.level.isClientSide);
				}

				return interactionresult;
			}
		} else if ((this.isTrusting() || (this.getTarget() != player && !this.isRunningAway())) && itemstack.is(CCItemTags.RAT_TAME_ITEMS)) {
			this.usePlayerItem(player, hand, itemstack);

			if (!this.level.isClientSide) {
				if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
					this.tame(player);
					this.navigation.stop();
					this.setTarget(null);
					this.setOrderedToSit(true);
					this.level.broadcastEntityEvent(this, (byte)7);
				} else {
					this.level.broadcastEntityEvent(this, (byte)6);
				}
			}

			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}

		return super.mobInteract(player, hand);
	}

	public List<Rat> getGroup() {
		return this.group;
	}

	public int getFriendAmount() {
		int size = this.group.size();
		return this.isBaby() ? size : size - 1;
	}

	public boolean isSurroundedByFriends() {
		return this.getFriendAmount() > 0;
	}

	public boolean trustsPlayers() {
		return this.isTrusting() || this.isTame();
	}

	public boolean shouldAttack(LivingEntity target) {
		if (this.isTame() || this.getFriendAmount() > 1) {
			if (target instanceof Player) {
				return !this.trustsPlayers();
			} else {
				return true;
			}
		}

		return false;
	}

	public boolean shouldRunAway() {
		return !this.trustsPlayers() && this.getFriendAmount() <= 1;
	}

	public Vec3 findGroupCenter(List<Rat> groupIn) {
		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;

		for(Rat friend : groupIn) {
			x += friend.getX();
			y += friend.getY();
			z += friend.getZ();
		}

		return new Vec3(x / groupIn.size(), y / groupIn.size(), z / groupIn.size());
	}

	public float getTailWagAmount() {
		if (this.isTame()) {
			float f = Mth.clamp(1.0F - (this.getMaxHealth() - this.getHealth()) / this.getMaxHealth(), 0.0F, 1.0F);
			return this.isInWater() ? f : f * 1.5F;
		} else {
			return this.isInWater() ? 1.0F : 1.5F;
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		if (this.random.nextFloat() < 0.2F) {
			float f = this.random.nextFloat();
			ItemStack itemstack;
			if (f < 0.05F) {
				itemstack = new ItemStack(Items.EMERALD);
			} else if (f < 0.2F) {
				itemstack = new ItemStack(Items.EGG);
			} else if (f < 0.4F) {
				itemstack = this.random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
			} else if (f < 0.6F) {
				itemstack = new ItemStack(Items.WHEAT);
			} else if (f < 0.8F) {
				itemstack = new ItemStack(Items.LEATHER);
			} else {
				itemstack = new ItemStack(Items.FEATHER);
			}

			this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
		}
	}

	@Override
	public boolean isFood(ItemStack itemstack) {
		return itemstack.is(CCItemTags.RAT_FOOD);
	}

	private boolean canEatItem(ItemStack itemStackIn) {
		return itemStackIn.getItem().isEdible() && this.getTarget() == null && this.onGround;
	}

	@Override
	public boolean canTakeItem(ItemStack itemstackIn) {
		EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstackIn);
		if (!this.getItemBySlot(equipmentslottype).isEmpty()) {
			return false;
		} else {
			return equipmentslottype == EquipmentSlot.MAINHAND && super.canTakeItem(itemstackIn);
		}
	}

	@Override
	public boolean canHoldItem(ItemStack stack) {
		Item item = stack.getItem();
		ItemStack itemstack = this.getMainHandItem();
		return itemstack.isEmpty() || this.eatTicks > 0 && item.isEdible() && !itemstack.getItem().isEdible();
	}

	private void spitOutItem(ItemStack stackIn) {
		if (!stackIn.isEmpty() && !this.level.isClientSide) {
			ItemEntity itementity = new ItemEntity(this.level, this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, stackIn);
			itementity.setPickUpDelay(40);
			itementity.setThrower(this.getUUID());
			this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
			this.level.addFreshEntity(itementity);
		}
	}

	private void spawnItem(ItemStack stackIn) {
		ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stackIn);
		this.level.addFreshEntity(itementity);
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
			this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
			this.take(itemEntity, itemstack.getCount());
			itemEntity.discard();
			this.eatTicks = 0;
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return size.height * 0.5F;
	}

	@Override
	public void setTame(boolean tamed) {
		super.setTame(tamed);
		if (tamed) {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
			this.setHealth(10.0F);
		} else {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4.0D);
		}
	}

	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
		if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
			if (target instanceof Wolf wolf) {
				return !wolf.isTame() || wolf.getOwner() != owner;
			} else if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
				return false;
			} else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
				return false;
			} else {
				return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
			}
		} else {
			return false;
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
				level.broadcastEntityEvent(this, (byte)18);
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
	public Rat getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		Rat child = CCEntityTypes.RAT.get().create(level);
		if (child != null) {
			if (otherParent instanceof Rat) {
				if (this.random.nextBoolean()) {
					child.setRatType(((Rat) otherParent).getRatType());
				} else {
					child.setRatType(this.getRatType());
				}

				if (this.trustsPlayers() || ((Rat) otherParent).trustsPlayers()) {
					child.setTrusting(true);
				}
			}
		}

		return child;
	}

	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
		float f1 = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
		passenger.setPos(this.getX() + (double) (0.1F * f), this.getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, this.getZ() - (double) (0.1F * f1));
		if (passenger instanceof LivingEntity) {
			((LivingEntity) passenger).yBodyRot = this.yBodyRot;
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		spawnDataIn = super.finalizeSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);

		float chance = random.nextFloat();
		RatType type = chance < 0.05F ? RatType.WHITE : chance < 0.30F ? RatType.BROWN : chance < 0.65F ? RatType.GRAY : RatType.BLUE;
		this.setRatType(type.getId());

		this.populateDefaultEquipmentSlots(difficulty);
		return super.finalizeSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
	}

	public enum RatType {
		BLUE(0),
		GRAY(1),
		BROWN(2),
		WHITE(3);

		private static final RatType[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(RatType::getId)).toArray(RatType[]::new);

		private final int id;
		private final LazyLoadedValue<ResourceLocation> textureLocation = new LazyLoadedValue<>(() -> new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/rat_" + this.name().toLowerCase(Locale.ROOT) + ".png"));

		RatType(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public ResourceLocation getTextureLocation() {
			return this.textureLocation.get();
		}

		public static RatType byId(int id) {
			if (id < 0 || id >= VALUES.length) {
				id = 0;
			}
			return VALUES[id];
		}
	}

	class RatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
		public RatAvoidEntityGoal(Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicate) {
			super(Rat.this, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, predicate);
		}

		@Override
		public boolean canUse() {
			return Rat.this.shouldRunAway() && super.canUse();
		}

		@Override
		public void start() {
			super.start();
			Rat.this.setRunningAway(true);
		}

		@Override
		public void stop() {
			super.stop();
			Rat.this.setRunningAway(false);
		}
	}

	class RatTemptGoal extends TemptGoal {
		public RatTemptGoal() {
			super(Rat.this, 1.0D, Ingredient.of(CCItemTags.RAT_FOOD), false);
		}

		@Override
		public boolean canUse() {
			return Rat.this.trustsPlayers() && super.canUse();
		}
	}

	class RatJumpOnOwnersShoulderGoal extends Goal {
		private ServerPlayer owner;

		public RatJumpOnOwnersShoulderGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return !Rat.this.isOrderedToSit() && Rat.this.canSitOnShoulder() && this.shouldJumpOnShoulder();
		}

		@Override
		public void start() {
			this.owner = (ServerPlayer) Rat.this.getOwner();
		}

		@Override
		public void stop() {
			this.owner = null;
		}

		@Override
		public void tick() {
			if (!Rat.this.isInSittingPose() && !Rat.this.isLeashed() && Rat.this.getBoundingBox().intersects(this.owner.getBoundingBox())) {
				Rat.this.setEntityOnShoulder(this.owner);
			} else {
				Rat.this.getLookControl().setLookAt(this.owner, 10.0F, Rat.this.getMaxHeadXRot());
				Rat.this.getNavigation().moveTo(this.owner, 1.0D);
			}
		}

		private boolean shouldJumpOnShoulder() {
			ServerPlayer owner = (ServerPlayer) Rat.this.getOwner();
			return owner != null && owner.isCrouching() && !owner.isSpectator() && !owner.getAbilities().flying && !owner.isInWater() && !owner.isInPowderSnow;
		}
	}

	class RatStayInGroupGoal extends Goal {
		private Vec3 groupCenter;
		private int timeToRecalcPath;

		public RatStayInGroupGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!Rat.this.isTame() && !Rat.this.isBaby() && Rat.this.isSurroundedByFriends()) {
				this.setGroupCenter();
				return Rat.this.distanceToSqr(this.groupCenter) > 16.0D;
			}

			return false;
		}

		@Override
		public boolean canContinueToUse() {
			double d0 = Rat.this.distanceToSqr(this.groupCenter);
			return !(d0 < 9.0D) && !(d0 > 256.0D);
		}

		@Override
		public void start() {
			this.timeToRecalcPath = 0;
		}

		@Override
		public void stop() {
			if (Rat.this.random.nextBoolean()) {
				Rat.this.getNavigation().stop();
			}
		}

		@Override
		public void tick() {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = this.adjustedTickDelay(10);
				this.setGroupCenter();
				Rat.this.getNavigation().moveTo(this.groupCenter.x, this.groupCenter.y, this.groupCenter.z, 1.0D);
			}
		}

		private void setGroupCenter() {
			this.groupCenter = Rat.this.findGroupCenter(Rat.this.getGroup());
		}
	}

	class RatFollowParentGoal extends FollowParentGoal {
		public RatFollowParentGoal() {
			super(Rat.this, 1.2D);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
	}

	class RatRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
		public RatRandomStrollGoal() {
			super(Rat.this, 1.0D);
		}

		@Nullable
		@Override
		protected Vec3 getPosition() {
			if (Rat.this.isInWaterOrBubble()) {
				Vec3 vec3 = LandRandomPos.getPos(Rat.this, 15, 7);
				return vec3 == null ? super.getPosition() : vec3;
			} else {
				boolean flag = Rat.this.isTame() || Rat.this.isSurroundedByFriends();
				int max = flag ? 6 : 10;
				int min = flag ? 3 : 7;
				return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, max, min) : DefaultRandomPos.getPos(this.mob, max, min);
			}
		}
	}

	class RatFindItemsGoal extends Goal {
		public RatFindItemsGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!Rat.this.getMainHandItem().isEmpty()) {
				return false;
			} else if (Rat.this.getTarget() == null && Rat.this.getLastHurtByMob() == null) {
				if (Rat.this.getRandom().nextInt(10) != 0) {
					return false;
				} else {
					List<ItemEntity> list = Rat.this.level.getEntitiesOfClass(ItemEntity.class, Rat.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Rat.ALLOWED_ITEMS);
					return !list.isEmpty() && Rat.this.getMainHandItem().isEmpty();
				}
			} else {
				return false;
			}
		}

		@Override
		public void tick() {
			List<ItemEntity> list = Rat.this.level.getEntitiesOfClass(ItemEntity.class, Rat.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Rat.ALLOWED_ITEMS);
			ItemStack itemstack = Rat.this.getMainHandItem();
			if (itemstack.isEmpty() && !list.isEmpty()) {
				Rat.this.getNavigation().moveTo(list.get(0), 1.2F);
			}
		}

		@Override
		public void start() {
			List<ItemEntity> list = Rat.this.level.getEntitiesOfClass(ItemEntity.class, Rat.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Rat.ALLOWED_ITEMS);
			if (!list.isEmpty()) {
				Rat.this.getNavigation().moveTo(list.get(0), 1.2F);
			}
		}
	}

	class RatStopAttackingGoal extends Goal {
		public RatStopAttackingGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		}

		@Override
		public boolean canUse() {
			return !Rat.this.isTame() && !Rat.this.shouldAttack(Rat.this.getTarget());
		}

		@Override
		public boolean canContinueToUse() {
			return !Rat.this.isTame() && !Rat.this.shouldAttack(Rat.this.getTarget());
		}

		@Override
		public void start() {
			Rat.this.setTarget(null);
		}
	}

	class RatHurtByTargetGoal extends HurtByTargetGoal {
		public RatHurtByTargetGoal() {
			super(Rat.this);
		}

		@Override
		public boolean canUse() {
			return Rat.this.shouldAttack(Rat.this.getLastHurtByMob()) && super.canUse();
		}

		@Override
		protected void alertOther(Mob mob, LivingEntity target) {
			if (mob instanceof Rat && ((Rat) mob).shouldAttack(this.targetMob)) {
				super.alertOther(mob, target);
			}
		}
	}

	class RatRandomTargetGoal<T extends LivingEntity> extends NonTameRandomTargetGoal<T> {
		public RatRandomTargetGoal(Class<T> targetType, boolean mustReach, Predicate<LivingEntity> predicate) {
			super(Rat.this, targetType, mustReach, predicate);
		}

		@Override
		public boolean canUse() {
			return super.canUse() && Rat.this.shouldAttack(this.target);
		}

		@Override
		public void start() {
			for(Rat friend : Rat.this.getGroup()) {
				if (friend != Rat.this && friend.shouldAttack(this.target) && friend.getTarget() == null) {
					friend.setTarget(this.target);
				}
			}
			super.start();
		}
	}
}