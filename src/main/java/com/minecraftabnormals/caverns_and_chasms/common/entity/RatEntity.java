package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class RatEntity extends AnimalEntity {
	private static final DataParameter<Integer> RAT_TYPE = EntityDataManager.defineId(RatEntity.class, DataSerializers.INT);
	private int eatTicks;
	private static final Predicate<ItemEntity> TRUSTED_TARGET_SELECTOR = (entity) -> !entity.hasPickUpDelay() && entity.isAlive();

	public RatEntity(EntityType<? extends RatEntity> type, World worldIn) {
		super(type, worldIn);
		this.setCanPickUpLoot(true);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new FindItemsGoal());
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));

		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RAT_TYPE, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("RatType", this.getRatType());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setRatType(compound.getInt("RatType"));
	}

	public void aiStep() {
		if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi()) {
			++this.eatTicks;
			ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.MAINHAND);
			if (this.canEatItem(itemstack)) {
				if (this.eatTicks > 600) {
					ItemStack itemstack1 = itemstack.finishUsingItem(this.level, this);
					if (!itemstack1.isEmpty()) {
						this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack1);
					}

					this.eatTicks = 0;
				} else if (this.eatTicks > 560 && this.random.nextFloat() < 0.1F) {
					this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
					this.level.broadcastEntityEvent(this, (byte) 45);
				}
			}
		}

		super.aiStep();
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

			this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
		}
	}

	private boolean canEatItem(ItemStack itemStackIn) {
		return itemStackIn.getItem().isEdible() && this.getTarget() == null && this.onGround;
	}

	@Override
	public boolean canTakeItem(ItemStack itemstackIn) {
		EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(itemstackIn);
		if (!this.getItemBySlot(equipmentslottype).isEmpty()) {
			return false;
		} else {
			return equipmentslottype == EquipmentSlotType.MAINHAND && super.canTakeItem(itemstackIn);
		}
	}

	@Override
	public boolean canHoldItem(ItemStack stack) {
		Item item = stack.getItem();
		ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.MAINHAND);
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

			this.spitOutItem(this.getItemBySlot(EquipmentSlotType.MAINHAND));
			this.onItemPickup(itemEntity);
			this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.split(1));
			this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 2.0F;
			this.take(itemEntity, itemstack.getCount());
			itemEntity.remove();
			this.eatTicks = 0;
		}

	}

	private void setRatType(int id) {
		this.entityData.set(RAT_TYPE, id);
	}

	public int getRatType() {
		return this.entityData.get(RAT_TYPE);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
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
	public RatEntity getBreedOffspring(ServerWorld world, AgeableEntity entity) {
		RatEntity child = CCEntities.RAT.get().create(world);
		if (child != null) {
			if (entity instanceof RatEntity && this.random.nextBoolean()) {
				child.setRatType(((RatEntity) entity).getRatType());
			} else {
				child.setRatType(this.getRatType());
			}
		}

		return child;
	}

	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float f = MathHelper.sin(this.yBodyRot * ((float) Math.PI / 180F));
		float f1 = MathHelper.cos(this.yBodyRot * ((float) Math.PI / 180F));
		passenger.setPos(this.getX() + (double) (0.1F * f), this.getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, this.getZ() - (double) (0.1F * f1));
		if (passenger instanceof LivingEntity) {
			((LivingEntity) passenger).yBodyRot = this.yBodyRot;
		}
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
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
		private final LazyValue<ResourceLocation> textureLocation = new LazyValue<>(() -> new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/" + this.name().toLowerCase(Locale.ROOT) + ".png"));

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

	class FindItemsGoal extends Goal {
		public FindItemsGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (!RatEntity.this.getItemBySlot(EquipmentSlotType.MAINHAND).isEmpty()) {
				return false;
			} else if (RatEntity.this.getTarget() == null && RatEntity.this.getLastHurtByMob() == null) {
				if (RatEntity.this.getRandom().nextInt(10) != 0) {
					return false;
				} else {
					List<ItemEntity> list = RatEntity.this.level.getEntitiesOfClass(ItemEntity.class, RatEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
					return !list.isEmpty() && RatEntity.this.getItemBySlot(EquipmentSlotType.MAINHAND).isEmpty();
				}
			} else {
				return false;
			}
		}

		public void tick() {
			List<ItemEntity> list = RatEntity.this.level.getEntitiesOfClass(ItemEntity.class, RatEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
			ItemStack itemstack = RatEntity.this.getItemBySlot(EquipmentSlotType.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				RatEntity.this.getNavigation().moveTo(list.get(0), (double) 1.2F);
			}

		}

		public void start() {
			List<ItemEntity> list = RatEntity.this.level.getEntitiesOfClass(ItemEntity.class, RatEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
			if (!list.isEmpty()) {
				RatEntity.this.getNavigation().moveTo(list.get(0), (double) 1.2F);
			}

		}
	}
}

