package com.minecraftabnormals.cave_upgrade.common.entity;

import com.minecraftabnormals.cave_upgrade.core.registry.CUItems;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class DeeperEntity extends CreeperEntity {

	public DeeperEntity(EntityType<? extends DeeperEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(CUItems.DEEPER_SPAWN_EGG.get());
	}
}