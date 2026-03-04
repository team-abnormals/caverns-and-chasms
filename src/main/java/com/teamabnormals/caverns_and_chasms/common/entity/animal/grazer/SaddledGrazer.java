package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SaddledGrazer extends AbstractGrazer {
	public SaddledGrazer(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!this.isVehicle() && !this.isBaby() && !player.isSecondaryUseActive()) {
			if (!this.level().isClientSide)
				player.startRiding(this);
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CCEntityTypes.SADDLED_GRAZER.get().create(level);
	}
}