package com.teamabnormals.caverns_and_chasms.common.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class Spiderling extends Spider {
	public Spiderling(EntityType<? extends Spiderling> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Spider.createAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		return spawnDataIn;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(Items.SPIDER_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 0.225F;
	}
}