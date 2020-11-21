package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class MimeEntity extends MonsterEntity {
	public static final EntitySize STANDING_SIZE = EntitySize.flexible(0.6F, 2.1F);
	private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder()
			.put(Pose.STANDING, STANDING_SIZE)
			.put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F))
			.put(Pose.CROUCHING, EntitySize.flexible(0.6F, 1.5F))
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
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.26F)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
				.createMutableAttribute(Attributes.ARMOR, 2.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean result = super.attackEntityAsMob(entityIn);
		if (entityIn instanceof LivingEntity) {
			EquipmentSlotType itemSlot = EquipmentSlotType.MAINHAND;
			LivingEntity entity = (LivingEntity) entityIn;
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
			ItemStack weapon = entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			ItemStack offhand = entity.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
			if (!(weapon.getItem() instanceof ToolItem || weapon.getItem() instanceof SwordItem)) {
				if (offhand.getItem() instanceof ToolItem || offhand.getItem() instanceof SwordItem)
					itemSlot = EquipmentSlotType.OFFHAND;
				else if (!slotsWithGear.isEmpty()) {
					itemSlot = slotsWithGear.get(this.rand.nextInt(slotsWithGear.size()));
				}
			}
			this.setItemStackToSlot(itemSlot == EquipmentSlotType.OFFHAND ? EquipmentSlotType.MAINHAND : itemSlot, entity.getItemStackFromSlot(itemSlot));
		}
		return result;
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
		System.out.println(this.getCreatureAttribute());
	}

	@Override
	public void livingTick() {
		super.livingTick();
		if (this.getAttackTarget() != null) {
			Pose pose = this.getAttackTarget().getPose();
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

	protected void updatePose() {
		if (this.isPoseClear(Pose.SWIMMING)) {
			Pose pose;
			if (this.isSwimming())
				pose = Pose.SWIMMING;
			else if (this.isSneaking())
				pose = Pose.CROUCHING;
			else
				pose = Pose.STANDING;

			Pose pose1;
			if (!this.isSpectator() && !this.isPassenger() && !this.isPoseClear(pose)) {
				if (this.isPoseClear(Pose.CROUCHING))
					pose1 = Pose.CROUCHING;
				else
					pose1 = Pose.SWIMMING;
			} else
				pose1 = pose;
			this.setPose(pose1);
		}
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
