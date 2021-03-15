package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.minecraftabnormals.caverns_and_chasms.common.recipe.MimingRecipe;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MimeEntity extends MonsterEntity {
	public static final EntitySize STANDING_SIZE = EntitySize.flexible(0.6F, 2.1F);
	private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder()
			.put(Pose.STANDING, STANDING_SIZE)
			.put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F))
			.put(Pose.CROUCHING, EntitySize.flexible(0.6F, 1.8F))
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
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.3F)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
				.createMutableAttribute(Attributes.ARMOR, 2.0D);
	}

	public static boolean canMimeSpawn(EntityType<? extends MonsterEntity> type, IServerWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
		return pos.getY() <= 42 && canMonsterSpawnInLight(type, worldIn, reason, pos, randomIn);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_ZOMBIE_STEP;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean result = super.attackEntityAsMob(entityIn);
		if (entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;

			EquipmentSlotType slot1 = EquipmentSlotType.MAINHAND;
			EquipmentSlotType slot2 = null;
			List<EquipmentSlotType> slotsWithGear = Lists.newArrayList();

			for (EquipmentSlotType slot : EquipmentSlotType.values()) {
				this.setItemStackToSlot(slot, ItemStack.EMPTY);
				if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
					this.inventoryHandsDropChances[slot.getIndex()] = 0.0F;
					continue;
				}
				this.inventoryArmorDropChances[slot.getIndex()] = 0.0F;
				ItemStack stack = entity.getItemStackFromSlot(slot);
				if (!stack.isEmpty())
					slotsWithGear.add(slot);
			}

			ItemStack mainhand = entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			ItemStack offhand = entity.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
			EquipmentSlotType armor1 = null;
			EquipmentSlotType armor2 = null;

			if (slotsWithGear.size() > 0) {
				int index = this.rand.nextInt(slotsWithGear.size());
				armor1 = slotsWithGear.get(index);
				slotsWithGear.remove(index);
				armor2 = slotsWithGear.isEmpty() ? null : slotsWithGear.get(this.rand.nextInt(slotsWithGear.size()));
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

			world.playMovingSound(null, this, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.HOSTILE, 1.0F, 1.0F);
			this.setItemStackToSlot(slot1 == EquipmentSlotType.OFFHAND ? EquipmentSlotType.MAINHAND : slot1, entity.getItemStackFromSlot(slot1));
			if (slot2 != null)
				this.setItemStackToSlot(slot2, entity.getItemStackFromSlot(slot2));
		}
		return result;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		Entity source = cause.getTrueSource();

		if (source instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) source;
			ItemStack stack = attacker.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
			List<MimingRecipe> recipes = world.getRecipeManager().getRecipesForType(RecipeTypes.MIMING);

			for (MimingRecipe recipe : recipes) {
				for (Ingredient ingredient : recipe.getIngredients()) {
					if (stack.getCount() == 1 && ingredient.test(stack)) {
						attacker.setItemStackToSlot(EquipmentSlotType.OFFHAND, recipe.getRecipeOutput());
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
	public void livingTick() {
		super.livingTick();
		if (!world.isRemote()) {
			Pose pose = this.getAttackTarget() != null ? this.getAttackTarget().getPose() : Pose.STANDING;
			if (pose == Pose.SWIMMING || pose == Pose.CROUCHING || pose == Pose.STANDING) {
				if (!this.isPoseClear(pose)) {
					if (this.isPoseClear(Pose.CROUCHING))
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
	public EntitySize getSize(Pose poseIn) {
		return SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(CCItems.MIME_SPAWN_EGG.get());
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getPosX() - this.chasingPosX;
		double d1 = this.getPosY() - this.chasingPosY;
		double d2 = this.getPosZ() - this.chasingPosZ;
		if (d0 > 10.0D)
			this.chasingPosX = this.getPosX();
		if (d2 > 10.0D)
			this.chasingPosZ = this.getPosZ();
		if (d1 > 10.0D)
			this.chasingPosY = this.getPosY();
		if (d0 < -10.0D)
			this.chasingPosX = this.getPosX();
		if (d2 < -10.0D)
			this.chasingPosZ = this.getPosZ();
		if (d1 < -10.0D)
			this.chasingPosY = this.getPosY();
		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}
}
