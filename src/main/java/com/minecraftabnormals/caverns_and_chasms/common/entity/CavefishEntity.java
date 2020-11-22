package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class CavefishEntity extends AbstractGroupFishEntity {
	public CavefishEntity(EntityType<? extends CavefishEntity> entity, World world) {
		super(entity, world);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 3.0D);
	}

	@Override
	public boolean func_241208_cS_() {
		return false;
	}

	@Override
	public int getMaxGroupSize() {
		return 3;
	}

	@Override
	protected ItemStack getFishBucket() {
		return new ItemStack(CCItems.CAVEFISH_BUCKET.get());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SALMON_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SALMON_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SALMON_HURT;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_SALMON_FLOP;
	}

	public static boolean canCavefishSpawn(EntityType<? extends AbstractFishEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
		return pos.getY() < 42 && worldIn.getBlockState(pos).isIn(Blocks.WATER) && worldIn.getBlockState(pos.up()).isIn(Blocks.WATER);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(CCItems.CAVEFISH_SPAWN_EGG.get());
	}
}
