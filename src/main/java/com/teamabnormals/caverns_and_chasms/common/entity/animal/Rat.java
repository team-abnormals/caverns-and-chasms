package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat.*;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCRatVariants;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Rat extends ShoulderRidingEntity implements VariantHolder<RatVariant> {
	public static final Predicate<ItemEntity> ALLOWED_ITEMS = (entity) -> !entity.hasPickUpDelay() && entity.isAlive();
	private static final Predicate<Entity> AVOID_PLAYERS = (entity) -> !entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);

	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> TRUSTING = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RUNNING_AWAY = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);

	private List<Rat> pack = Lists.newArrayList();
	private int ticksSinceEaten;
	private Player tamer;

	public Rat(EntityType<? extends Rat> type, Level level) {
		super(type, level);
		this.setCanPickUpLoot(true);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(2, new RatAttachToTargetGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
		this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
		this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new RatTemptGoal(this));
		this.goalSelector.addGoal(7, new RatJumpOnShoulderGoal(this));
		this.goalSelector.addGoal(8, new RatDevourRottenFleshGoal(this, 1.25D, 16, 4));
		this.goalSelector.addGoal(9, new RatStayInGroupGoal(this));
		this.goalSelector.addGoal(10, new RatFollowParentGoal(this));
		this.goalSelector.addGoal(11, new RatAvoidEntityGoal<>(this, Player.class, 10.0F, 1.0F, 1.2F, AVOID_PLAYERS::test));
		this.goalSelector.addGoal(12, new RatRandomStrollGoal(this));
		this.goalSelector.addGoal(13, new RatFindItemsGoal(this));
		this.goalSelector.addGoal(14, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new RatStopAttackingGoal(this));
		this.targetSelector.addGoal(4, (new RatHurtByTargetGoal(this)).setAlertOthers());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, CCRatVariants.BLUE.location().toString());
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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("Variant", this.getStringVariant());
		tag.putByte("CollarColor", (byte) this.getCollarColor().getId());
		tag.putBoolean("Trusting", this.isTrusting());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setStringVariant(tag.getString("Variant"));
		if (tag.contains("CollarColor", 99)) {
			this.setCollarColor(DyeColor.byId(tag.getInt("CollarColor")));
		}
		this.setTrusting(tag.getBoolean("Trusting"));
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

	public boolean isTrusting() {
		return this.entityData.get(TRUSTING);
	}

	private void setTrusting(boolean trusting) {
		this.entityData.set(TRUSTING, trusting);
	}

	public boolean isRunningAway() {
		return this.entityData.get(RUNNING_AWAY);
	}

	public void setRunningAway(boolean runningAway) {
		this.entityData.set(RUNNING_AWAY, runningAway);
	}

	public void aiStep() {
		if (!this.level().isClientSide && this.isAlive() && this.isEffectiveAi()) {
			++this.ticksSinceEaten;
			ItemStack itemstack = this.getMainHandItem();
			if (this.canEatItem(itemstack)) {
				if (this.ticksSinceEaten > 600) {
					ItemStack itemstack1 = itemstack.finishUsingItem(this.level(), this);
					if (!itemstack1.isEmpty()) {
						this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
					}

					this.ticksSinceEaten = 0;
				} else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1F) {
					this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
					this.level().broadcastEntityEvent(this, (byte) 45);
				}
			}

			List<Rat> rats = this.level().getEntitiesOfClass(Rat.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), rat -> !rat.isBaby() && rat.isAlive() && (!rat.isTame() || rat.getOwner() == this.getOwner()) && !rat.is(this));
			rats.sort(Comparator.comparing(this::distanceToSqr));
			this.pack = rats.stream().limit(4).collect(Collectors.toList());
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
				this.heal((float) item.getFoodProperties().getNutrition());
				this.gameEvent(GameEvent.EAT, this);

				return InteractionResult.sidedSuccess(this.level().isClientSide);
			} else if (item instanceof DyeItem) {
				DyeColor dyecolor = ((DyeItem) item).getDyeColor();
				if (dyecolor != this.getCollarColor()) {
					this.setCollarColor(dyecolor);
					this.usePlayerItem(player, hand, itemstack);

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
		} else if ((this.isTrusting() || (this.getTarget() != player && !this.isRunningAway())) && itemstack.is(CCItemTags.RAT_TAME_ITEMS)) {
			this.usePlayerItem(player, hand, itemstack);

			if (!this.level().isClientSide) {
				if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
					this.tame(player);
					this.navigation.stop();
					this.setTarget(null);
					this.setOrderedToSit(true);
					this.level().broadcastEntityEvent(this, (byte) 7);
				} else {
					this.level().broadcastEntityEvent(this, (byte) 6);
				}
			}

			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

		return super.mobInteract(player, hand);
	}

	public void setTamer(Player entity) {
		this.tamer = entity;
	}

	public Player getTamer() {
		return this.tamer;
	}

	public List<Rat> getPack() {
		return this.pack;
	}

	public boolean hasPack() {
		return !this.pack.isEmpty();
	}

	public boolean trustsPlayers() {
		return this.isTrusting() || this.isTame();
	}

	public boolean isPackBigEnoughToAttack() {
		return this.isBaby() ? this.pack.size() > 2 : this.pack.size() > 1;
	}

	public boolean shouldAttack(LivingEntity target) {
		if (this.isTame() || (this.isPackBigEnoughToAttack() && this.tamer == null))
			return !(target instanceof Player) || !this.trustsPlayers();

		return false;
	}

	public boolean shouldRunAway() {
		return !this.trustsPlayers() && !this.isPackBigEnoughToAttack();
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
		return calculateTailWagAmount(this.getHealth(), this.getMaxHealth(), this.isTame());
	}

	public static float calculateTailWagAmount(float health, float maxHealth, boolean isTame) {
		if (isTame) {
			float f = Mth.clamp(1.0F - (maxHealth - health) / maxHealth, 0.0F, 1.0F);
			return f * 1.5F;
		} else {
			return 1.5F;
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		if (random.nextFloat() < 0.2F) {
			float f = random.nextFloat();
			ItemStack itemstack;
			if (f < 0.05F) {
				itemstack = new ItemStack(Items.DIAMOND);
			} else if (f < 0.2F) {
				itemstack = random.nextBoolean() ? new ItemStack(Items.RAW_GOLD) : new ItemStack(CCItems.RAW_SILVER.get());
			} else if (f < 0.4F) {
				itemstack = random.nextBoolean() ? new ItemStack(Items.LAPIS_LAZULI) : new ItemStack(CCItems.SPINEL.get());
			} else if (f < 0.6F) {
				itemstack = new ItemStack(Items.REDSTONE);
			} else if (f < 0.8F) {
				itemstack = new ItemStack(Items.SLIME_BALL);
			} else {
				itemstack = new ItemStack(Items.BONE);
			}

			this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
		}
	}

	@Override
	public boolean isFood(ItemStack itemstack) {
		return itemstack.is(CCItemTags.RAT_FOOD);
	}

	private boolean canEatItem(ItemStack itemStackIn) {
		return itemStackIn.getItem().isEdible() && this.getTarget() == null && this.onGround();
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
		return itemstack.isEmpty() || this.ticksSinceEaten > 0 && item.isEdible() && !itemstack.getItem().isEdible();
	}

	private void spitOutItem(ItemStack stackIn) {
		if (!stackIn.isEmpty() && !this.level().isClientSide) {
			ItemEntity itementity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, stackIn);
			itementity.setPickUpDelay(40);
			itementity.setThrower(this.getUUID());
			this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
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
			this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
			this.take(itemEntity, itemstack.getCount());
			itemEntity.discard();
			this.ticksSinceEaten = 0;
		}
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
		if (!this.isTame() && !this.isPackBigEnoughToAttack()) {
			return false;
		}

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
			if (this.trustsPlayers() || rat.trustsPlayers()) {
				child.setTrusting(true);
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
		this.populateDefaultEquipmentSlots(this.random, difficulty);
		return super.finalizeSpawn(level, difficulty, spawnType, groupData, dataTag);
	}
}