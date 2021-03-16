package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class RatEntity extends AnimalEntity {
	private static final DataParameter<Integer> RAT_TYPE = EntityDataManager.createKey(RatEntity.class, DataSerializers.VARINT);

	public RatEntity(EntityType<? extends RatEntity> type, World worldIn) {
		super(type, worldIn);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));

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

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_FOX_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_FOX_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_FOX_DEATH;
	}

	public RatEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		return CCEntities.RAT.get().create(world);
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
}

