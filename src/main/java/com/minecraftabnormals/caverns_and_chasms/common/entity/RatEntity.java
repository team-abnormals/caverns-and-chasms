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
	private static final DataParameter<Integer> RAT_TYPE = EntityDataManager.createKey(RatEntity.class, DataSerializers.VARINT);
	private int eatTicks;
	private static final Predicate<ItemEntity> TRUSTED_TARGET_SELECTOR = (entity) -> !entity.cannotPickup() && entity.isAlive();

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
	protected void registerData() {
		super.registerData();
		this.dataManager.register(RAT_TYPE, 0);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("RatType", this.getRatType());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setRatType(compound.getInt("RatType"));
	}

	public void livingTick() {
		if (!this.world.isRemote && this.isAlive() && this.isServerWorld()) {
			++this.eatTicks;
			ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			if (this.canEatItem(itemstack)) {
				if (this.eatTicks > 600) {
					ItemStack itemstack1 = itemstack.onItemUseFinish(this.world, this);
					if (!itemstack1.isEmpty()) {
						this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack1);
					}

					this.eatTicks = 0;
				} else if (this.eatTicks > 560 && this.rand.nextFloat() < 0.1F) {
					this.playSound(this.getEatSound(itemstack), 1.0F, 1.0F);
					this.world.setEntityState(this, (byte) 45);
				}
			}
		}

		super.livingTick();
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.rand.nextFloat() < 0.2F) {
			float f = this.rand.nextFloat();
			ItemStack itemstack;
			if (f < 0.05F) {
				itemstack = new ItemStack(Items.EMERALD);
			} else if (f < 0.2F) {
				itemstack = new ItemStack(Items.EGG);
			} else if (f < 0.4F) {
				itemstack = this.rand.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
			} else if (f < 0.6F) {
				itemstack = new ItemStack(Items.WHEAT);
			} else if (f < 0.8F) {
				itemstack = new ItemStack(Items.LEATHER);
			} else {
				itemstack = new ItemStack(Items.FEATHER);
			}

			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
		}
	}

	private boolean canEatItem(ItemStack itemStackIn) {
		return itemStackIn.getItem().isFood() && this.getAttackTarget() == null && this.onGround;
	}

	@Override
	public boolean canPickUpItem(ItemStack itemstackIn) {
		EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstackIn);
		if (!this.getItemStackFromSlot(equipmentslottype).isEmpty()) {
			return false;
		} else {
			return equipmentslottype == EquipmentSlotType.MAINHAND && super.canPickUpItem(itemstackIn);
		}
	}

	@Override
	public boolean canEquipItem(ItemStack stack) {
		Item item = stack.getItem();
		ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		return itemstack.isEmpty() || this.eatTicks > 0 && item.isFood() && !itemstack.getItem().isFood();
	}

	private void spitOutItem(ItemStack stackIn) {
		if (!stackIn.isEmpty() && !this.world.isRemote) {
			ItemEntity itementity = new ItemEntity(this.world, this.getPosX() + this.getLookVec().x, this.getPosY() + 1.0D, this.getPosZ() + this.getLookVec().z, stackIn);
			itementity.setPickupDelay(40);
			itementity.setThrowerId(this.getUniqueID());
			this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0F, 1.0F);
			this.world.addEntity(itementity);
		}
	}

	private void spawnItem(ItemStack stackIn) {
		ItemEntity itementity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), stackIn);
		this.world.addEntity(itementity);
	}

	@Override
	protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
		ItemStack itemstack = itemEntity.getItem();
		if (this.canEquipItem(itemstack)) {
			int i = itemstack.getCount();
			if (i > 1) {
				this.spawnItem(itemstack.split(i - 1));
			}

			this.spitOutItem(this.getItemStackFromSlot(EquipmentSlotType.MAINHAND));
			this.triggerItemPickupTrigger(itemEntity);
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack.split(1));
			this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 2.0F;
			this.onItemPickup(itemEntity, itemstack.getCount());
			itemEntity.remove();
			this.eatTicks = 0;
		}

	}

	private void setRatType(int id) {
		this.dataManager.set(RAT_TYPE, id);
	}

	public int getRatType() {
		return this.dataManager.get(RAT_TYPE);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 4.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_FOX_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_FOX_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_FOX_DEATH;
	}

	@Override
	public RatEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		RatEntity child = CCEntities.RAT.get().create(world);
		if (child != null) {
			if (entity instanceof RatEntity && this.rand.nextBoolean()) {
				child.setRatType(((RatEntity) entity).getRatType());
			} else {
				child.setRatType(this.getRatType());
			}
		}

		return child;
	}

	public void updatePassenger(Entity passenger) {
		super.updatePassenger(passenger);
		float f = MathHelper.sin(this.renderYawOffset * ((float) Math.PI / 180F));
		float f1 = MathHelper.cos(this.renderYawOffset * ((float) Math.PI / 180F));
		passenger.setPosition(this.getPosX() + (double) (0.1F * f), this.getPosYHeight(0.5D) + passenger.getYOffset() + 0.0D, this.getPosZ() - (double) (0.1F * f1));
		if (passenger instanceof LivingEntity) {
			((LivingEntity) passenger).renderYawOffset = this.renderYawOffset;
		}
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.onInitialSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);

		float chance = rand.nextFloat();
		RatType type = chance < 0.05F ? RatType.WHITE : chance < 0.30F ? RatType.BROWN : chance < 0.65F ? RatType.GRAY : RatType.BLUE;
		this.setRatType(type.getId());

		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
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
			return this.textureLocation.getValue();
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
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean shouldExecute() {
			if (!RatEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty()) {
				return false;
			} else if (RatEntity.this.getAttackTarget() == null && RatEntity.this.getRevengeTarget() == null) {
				if (RatEntity.this.getRNG().nextInt(10) != 0) {
					return false;
				} else {
					List<ItemEntity> list = RatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, RatEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
					return !list.isEmpty() && RatEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty();
				}
			} else {
				return false;
			}
		}

		public void tick() {
			List<ItemEntity> list = RatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, RatEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
			ItemStack itemstack = RatEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				RatEntity.this.getNavigator().tryMoveToEntityLiving(list.get(0), (double) 1.2F);
			}

		}

		public void startExecuting() {
			List<ItemEntity> list = RatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, RatEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), RatEntity.TRUSTED_TARGET_SELECTOR);
			if (!list.isEmpty()) {
				RatEntity.this.getNavigator().tryMoveToEntityLiving(list.get(0), (double) 1.2F);
			}

		}
	}
}

