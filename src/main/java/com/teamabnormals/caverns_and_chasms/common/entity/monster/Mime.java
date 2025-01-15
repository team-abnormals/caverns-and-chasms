package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import com.google.common.collect.ImmutableMap;
import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Mime extends Monster {
	private static final UUID SPEED_MODIFIER_SNEAKING_UUID = UUID.fromString("D0DEF8EE-3E50-4FFC-A20D-3B9B27F4A3F3");
	private static final AttributeModifier SPEED_MODIFIER_SNEAKING = new AttributeModifier(SPEED_MODIFIER_SNEAKING_UUID, "Sneaking speed boost", (double) -0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
	public static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(0.6F, 2.1F);
	private static final Map<Pose, EntityDimensions> SIZE_BY_POSE = ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.8F)).build();
	public final Vector3f[] armPositions = new Vector3f[]{new Vector3f(-5.0F, 2.0F, 0.0F), new Vector3f(5.0F, 2.0F, 0.0F)};
	public final Vector3f[] armRotations = new Vector3f[]{new Vector3f(0.0F), new Vector3f(0.0F)};
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;
	private int copyTime;

	public Mime(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		Arrays.fill(this.handDropChances, 0.0F);
		Arrays.fill(this.armorDropChances, 0.0F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new OpenDoorGoal(this, false));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0F)
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	public static boolean checkMimeSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return pos.getY() <= 48 && (random.nextInt(10) == 0 || pos.getY() <= 0) && checkUndergroundMonsterSpawnRules(type, level, reason, pos, random);
	}

	public static boolean checkUndergroundMonsterSpawnRules(EntityType<? extends Monster> monster, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource p_219018_) {
		return level.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawnNoSkylight(level, pos, p_219018_) && checkMobSpawnRules(monster, level, reason, pos, p_219018_);
	}

	public static boolean isDarkEnoughToSpawnNoSkylight(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
		if (level.getBrightness(LightLayer.SKY, pos) > 0) {
			return false;
		} else {
			DimensionType dimension = level.dimensionType();
			int i = dimension.monsterSpawnBlockLightLimit();
			if (i < 15 && level.getBrightness(LightLayer.BLOCK, pos) > i) {
				return false;
			} else {
				int j = level.getLevel().isThundering() ? level.getMaxLocalRawBrightness(pos, 10) : level.getMaxLocalRawBrightness(pos);
				return j <= dimension.monsterSpawnLightTest().sample(random);
			}
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.MIME_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.MIME_HURT.get();
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int p_34292_, boolean p_34293_) {
		super.dropCustomDeathLoot(source, p_34292_, p_34293_);
		Entity entity = source.getEntity();
		if (entity instanceof Creeper creeper) {
			if (creeper.canDropMobsSkull()) {
				ItemStack itemstack = this.getSkull();
				if (!itemstack.isEmpty()) {
					creeper.increaseDroppedSkulls();
					this.spawnAtLocation(itemstack);
				}
			}
		}
	}

	protected ItemStack getSkull() {
		return new ItemStack(CCItems.MIME_HEAD.get());
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		boolean result = super.doHurtTarget(entityIn);
		if (entityIn instanceof LivingEntity entity) {
			boolean mimed = false;
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack stack = entity.getItemBySlot(slot);
					if (this.shouldCopyItem(this.getItemBySlot(slot), stack)) {
						this.setItemSlot(slot, stack.copy());
						mimed = true;
					}
				}
			}

			if (mimed) {
				this.playSound(CCSoundEvents.MIME_IMPERSONATE.get(), 1.0F, 1.0F);
			}
		}
		return result;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		Entity source = cause.getEntity();

		if (!this.level().isClientSide() && source instanceof LivingEntity attacker) {
			ItemStack stack = attacker.getItemBySlot(EquipmentSlot.OFFHAND);
			List<MimingRecipe> recipes = this.level().getRecipeManager().getAllRecipesFor(CCRecipeTypes.MIMING.get());

			for (MimingRecipe recipe : recipes) {
				for (Ingredient ingredient : recipe.getIngredients()) {
					if (stack.getCount() == 1 && ingredient.test(stack)) {
						attacker.setItemSlot(EquipmentSlot.OFFHAND, recipe.getResultItem(this.level().registryAccess()).copy());
						source.playSound(CCSoundEvents.MIME_MIME.get(), 1.0F, 1.0F);
						return;
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isAlive()) {
			if (this.level().isClientSide) {
				if (this.random.nextInt(5) == 0) {
					int i = this.random.nextInt(2);
					float f = -Mth.lerp(this.getSwimAmount(0.0F), 0F, this.isInWater() ? -90F - this.getXRot() : -90F) * Mth.DEG_TO_RAD;
					float f1 = (180F - this.yBodyRot) * Mth.DEG_TO_RAD;

					float yaw = this.armRotations[i].x();
					float pitch = this.armRotations[i].y();
					float roll = this.armRotations[i].z();

					float x = i == 0 ? 0.0625F : -0.0625F;
					float y = -0.71875F;

					float x1 = Mth.cos(pitch) * Mth.cos(roll) * x + (Mth.sin(yaw) * Mth.sin(pitch) * Mth.cos(roll) - Mth.cos(yaw) * Mth.sin(roll)) * y;
					float y1 = Mth.cos(pitch) * Mth.sin(roll) * x + (Mth.sin(yaw) * Mth.sin(pitch) * Mth.sin(roll) + Mth.cos(yaw) * Mth.cos(roll)) * y;
					float z1 = Mth.sin(pitch) * x - Mth.sin(yaw) * Mth.cos(pitch) * y;

					Vec3 vec3 = new Vec3(x1, y1, z1).add(new Vec3(this.armPositions[i]).scale(-0.0625D)).add(0.0D, 1.5D, 0.0D);

					vec3 = vec3.xRot(f);
					if (this.isVisuallySwimming())
						vec3 = vec3.add(0.0D, 0.3D, 1.0D);
					vec3 = vec3.yRot(f1);

					double d0 = this.random.nextFloat() * 0.1D - 0.05D;
					double d1 = this.random.nextFloat() * 0.1D - 0.05D;
					double d2 = this.random.nextFloat() * 0.1D - 0.05D;
					this.level().addParticle(CCParticleTypes.MIME_ENERGY.get(), this.getX() + vec3.x() + d0, this.getY() + vec3.y() + d1, this.getZ() + vec3.z() + d2, 0.0D, 0.0D, 0.0D);
				}
			} else {
				LivingEntity target = this.getTarget();

				if (target != null) {
					this.copyMainArm(target);

					if (target.swinging && !this.swinging)
						this.swing(target.swingingArm);

					if (this.copyTime > 0) {
						--this.copyTime;
						if (this.copyTime == 0) {
							boolean mimed = false;

							if (this.shouldCopyItem(this.getMainHandItem(), target.getMainHandItem())) {
								this.setItemSlot(EquipmentSlot.MAINHAND, target.getMainHandItem().copy());
								mimed = true;
							}
							if (this.shouldCopyItem(this.getOffhandItem(), target.getOffhandItem())) {
								this.setItemSlot(EquipmentSlot.OFFHAND, target.getOffhandItem().copy());
								mimed = true;
							}

							if (mimed)
								this.level().playSound(null, this, CCSoundEvents.MIME_MIME.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
						}
					} else if (this.shouldCopyItem(this.getMainHandItem(), target.getMainHandItem()) || this.shouldCopyItem(this.getOffhandItem(), target.getOffhandItem())) {
						this.copyTime = this.random.nextInt(3) + 4;
					}
				} else {
					this.copyTime = 0;
				}

				Pose pose = target != null ? target.getPose() : Pose.STANDING;
				if (!this.isPassenger() && (pose == Pose.SWIMMING || pose == Pose.CROUCHING || pose == Pose.STANDING)) {
					if (!this.canEnterPose(pose)) {
						if (this.canEnterPose(Pose.CROUCHING)) pose = Pose.CROUCHING;
						else pose = Pose.SWIMMING;
					}
				} else {
					pose = Pose.STANDING;
				}
				this.setPose(pose);

				this.handleSneakingSpeed();
				this.setSprinting((this.level().getDifficulty() == Difficulty.NORMAL || this.level().getDifficulty() == Difficulty.HARD) && target != null && target.isSprinting());
			}
		}
	}

	private boolean shouldCopyItem(ItemStack currentStack, ItemStack targetStack) {
		if (currentStack.isEmpty() != targetStack.isEmpty()) {
			return true;
		} else if (currentStack.isDamageableItem() != targetStack.isDamageableItem()) {
			return true;
		} else {
			ItemStack itemstack = targetStack.copy();
			itemstack.setCount(currentStack.getCount());
			if (currentStack.isDamageableItem())
				itemstack.setDamageValue(currentStack.getDamageValue());
			return !ItemStack.matches(currentStack, itemstack);
		}
	}

	private void copyMainArm(LivingEntity entity) {
		if (this.getMainArm() != entity.getMainArm()) {
			this.setLeftHanded(!this.isLeftHanded());
		}
	}

	private void handleSneakingSpeed() {
		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attributeinstance.getModifier(SPEED_MODIFIER_SNEAKING_UUID) != null) {
			attributeinstance.removeModifier(SPEED_MODIFIER_SNEAKING);
		}

		if (this.isCrouching() || this.isVisuallyCrawling()) {
			attributeinstance.addTransientModifier(SPEED_MODIFIER_SNEAKING);
		}
	}

	@Override
	public boolean isSteppingCarefully() {
		return this.getPose() == Pose.CROUCHING || super.isSteppingCarefully();
	}

	@Override
	public double getMyRidingOffset() {
		return -0.45D;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return SIZE_BY_POSE.getOrDefault(pose, STANDING_SIZE);
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getX() - this.chasingPosX;
		double d1 = this.getY() - this.chasingPosY;
		double d2 = this.getZ() - this.chasingPosZ;
		if (d0 > 10.0D) this.chasingPosX = this.getX();
		if (d2 > 10.0D) this.chasingPosZ = this.getZ();
		if (d1 > 10.0D) this.chasingPosY = this.getY();
		if (d0 < -10.0D) this.chasingPosX = this.getX();
		if (d2 < -10.0D) this.chasingPosZ = this.getZ();
		if (d1 < -10.0D) this.chasingPosY = this.getY();
		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}
}