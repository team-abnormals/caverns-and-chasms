package com.teamabnormals.caverns_and_chasms.common.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mime extends Monster {
	public static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(0.6F, 2.1F);
	private static final Map<Pose, EntityDimensions> SIZE_BY_POSE = ImmutableMap.<Pose, EntityDimensions>builder()
			.put(Pose.STANDING, STANDING_SIZE)
			.put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F))
			.put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.8F))
			.build();
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;

	public Mime(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
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
				.add(Attributes.MAX_HEALTH, 20.0F)
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	public static boolean canMimeSpawn(EntityType<? extends Monster> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
		return pos.getY() <= 42 && checkMonsterSpawnRules(type, worldIn, reason, pos, randomIn);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ZOMBIE_HURT;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ZOMBIE_STEP;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		boolean result = super.doHurtTarget(entityIn);
		if (entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;

			EquipmentSlot slot1 = EquipmentSlot.MAINHAND;
			EquipmentSlot slot2 = null;
			List<EquipmentSlot> slotsWithGear = Lists.newArrayList();

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				this.setItemSlot(slot, ItemStack.EMPTY);
				if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
					this.handDropChances[slot.getIndex()] = 0.0F;
					continue;
				}
				this.armorDropChances[slot.getIndex()] = 0.0F;
				ItemStack stack = entity.getItemBySlot(slot);
				if (!stack.isEmpty())
					slotsWithGear.add(slot);
			}

			ItemStack mainhand = entity.getItemBySlot(EquipmentSlot.MAINHAND);
			ItemStack offhand = entity.getItemBySlot(EquipmentSlot.OFFHAND);
			EquipmentSlot armor1 = null;
			EquipmentSlot armor2 = null;

			if (slotsWithGear.size() > 0) {
				int index = this.random.nextInt(slotsWithGear.size());
				armor1 = slotsWithGear.get(index);
				slotsWithGear.remove(index);
				armor2 = slotsWithGear.isEmpty() ? null : slotsWithGear.get(this.random.nextInt(slotsWithGear.size()));
			}

			if (isValidWeapon(mainhand)) {
				if (armor1 != null)
					slot2 = armor1;
				else if (isValidWeapon(offhand))
					slot2 = EquipmentSlot.OFFHAND;
			} else {
				if (isValidWeapon(offhand)) {
					slot1 = EquipmentSlot.OFFHAND;
					if (armor1 != null)
						slot2 = armor1;
				} else if (armor1 != null) {
					slot1 = armor1;
					if (armor2 != null)
						slot2 = armor2;
					else
						slot2 = EquipmentSlot.MAINHAND;
				}
			}

			level.playSound(null, this, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.HOSTILE, 1.0F, 1.0F);
			this.setItemSlot(slot1 == EquipmentSlot.OFFHAND ? EquipmentSlot.MAINHAND : slot1, entity.getItemBySlot(slot1));
			if (slot2 != null)
				this.setItemSlot(slot2, entity.getItemBySlot(slot2));
		}
		return result;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		Entity source = cause.getEntity();

		if (source instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) source;
			ItemStack stack = attacker.getItemBySlot(EquipmentSlot.OFFHAND);
			List<MimingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeTypes.MIMING);

			for (MimingRecipe recipe : recipes) {
				for (Ingredient ingredient : recipe.getIngredients()) {
					if (stack.getCount() == 1 && ingredient.test(stack)) {
						attacker.setItemSlot(EquipmentSlot.OFFHAND, recipe.getResultItem());
					}
				}
			}
		}
	}

	private static boolean isValidWeapon(ItemStack stack) {
		return stack.getItem() instanceof DiggerItem || stack.getItem() instanceof SwordItem;
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!level.isClientSide()) {
			Pose pose = this.getTarget() != null ? this.getTarget().getPose() : Pose.STANDING;
			if (pose == Pose.SWIMMING || pose == Pose.CROUCHING || pose == Pose.STANDING) {
				if (!this.canEnterPose(pose)) {
					if (this.canEnterPose(Pose.CROUCHING))
						pose = Pose.CROUCHING;
					else
						pose = Pose.SWIMMING;
				}
			} else {
				pose = Pose.STANDING;
			}
			this.setPose(pose);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getX() - this.chasingPosX;
		double d1 = this.getY() - this.chasingPosY;
		double d2 = this.getZ() - this.chasingPosZ;
		if (d0 > 10.0D)
			this.chasingPosX = this.getX();
		if (d2 > 10.0D)
			this.chasingPosZ = this.getZ();
		if (d1 > 10.0D)
			this.chasingPosY = this.getY();
		if (d0 < -10.0D)
			this.chasingPosX = this.getX();
		if (d2 < -10.0D)
			this.chasingPosZ = this.getZ();
		if (d1 < -10.0D)
			this.chasingPosY = this.getY();
		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}
}
