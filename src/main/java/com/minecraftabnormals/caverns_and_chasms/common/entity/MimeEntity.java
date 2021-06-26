package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.minecraftabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCRecipes.RecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MimeEntity extends MonsterEntity {
	public static final EntitySize STANDING_SIZE = EntitySize.scalable(0.6F, 2.1F);
	private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder()
			.put(Pose.STANDING, STANDING_SIZE)
			.put(Pose.SWIMMING, EntitySize.scalable(0.6F, 0.6F))
			.put(Pose.CROUCHING, EntitySize.scalable(0.6F, 1.8F))
			.build();
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;

	public MimeEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new OpenDoorGoal(this, false));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0F)
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	public static boolean canMimeSpawn(EntityType<? extends MonsterEntity> type, IServerWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
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

			EquipmentSlotType slot1 = EquipmentSlotType.MAINHAND;
			EquipmentSlotType slot2 = null;
			List<EquipmentSlotType> slotsWithGear = Lists.newArrayList();

			for (EquipmentSlotType slot : EquipmentSlotType.values()) {
				this.setItemSlot(slot, ItemStack.EMPTY);
				if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
					this.handDropChances[slot.getIndex()] = 0.0F;
					continue;
				}
				this.armorDropChances[slot.getIndex()] = 0.0F;
				ItemStack stack = entity.getItemBySlot(slot);
				if (!stack.isEmpty())
					slotsWithGear.add(slot);
			}

			ItemStack mainhand = entity.getItemBySlot(EquipmentSlotType.MAINHAND);
			ItemStack offhand = entity.getItemBySlot(EquipmentSlotType.OFFHAND);
			EquipmentSlotType armor1 = null;
			EquipmentSlotType armor2 = null;

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
					slot2 = EquipmentSlotType.OFFHAND;
			} else {
				if (isValidWeapon(offhand)) {
					slot1 = EquipmentSlotType.OFFHAND;
					if (armor1 != null)
						slot2 = armor1;
				} else if (armor1 != null) {
					slot1 = armor1;
					if (armor2 != null)
						slot2 = armor2;
					else
						slot2 = EquipmentSlotType.MAINHAND;
				}
			}

			level.playSound(null, this, SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.HOSTILE, 1.0F, 1.0F);
			this.setItemSlot(slot1 == EquipmentSlotType.OFFHAND ? EquipmentSlotType.MAINHAND : slot1, entity.getItemBySlot(slot1));
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
			ItemStack stack = attacker.getItemBySlot(EquipmentSlotType.OFFHAND);
			List<MimingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeTypes.MIMING);

			for (MimingRecipe recipe : recipes) {
				for (Ingredient ingredient : recipe.getIngredients()) {
					if (stack.getCount() == 1 && ingredient.test(stack)) {
						attacker.setItemSlot(EquipmentSlotType.OFFHAND, recipe.getResultItem());
					}
				}
			}
		}
	}

	private static boolean isValidWeapon(ItemStack stack) {
		return stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem;
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
	public EntitySize getDimensions(Pose poseIn) {
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
