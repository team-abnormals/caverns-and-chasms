package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CavefishEntity extends AbstractSchoolingFish {
	public CavefishEntity(EntityType<? extends CavefishEntity> entity, Level world) {
		super(entity, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D);
	}

	@Override
	public boolean isAffectedByFluids() {
		return false;
	}

	@Override
	public int getMaxSchoolSize() {
		return 3;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(CCItems.CAVEFISH_BUCKET.get());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SALMON_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SALMON_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.SALMON_HURT;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.SALMON_FLOP;
	}

	public static boolean canCavefishSpawn(EntityType<? extends AbstractFish> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
		return pos.getY() < 42 && worldIn.getBlockState(pos).is(Blocks.WATER) && worldIn.getBlockState(pos.above()).is(Blocks.WATER);
	}
}
