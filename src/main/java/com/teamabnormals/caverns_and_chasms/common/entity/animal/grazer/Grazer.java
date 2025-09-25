package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class Grazer extends AbstractGrazer implements Enemy {

	public Grazer(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}
}