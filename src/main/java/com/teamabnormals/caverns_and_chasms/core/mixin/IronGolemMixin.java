package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IronGolem.class)
public abstract class IronGolemMixin extends AbstractGolem implements ControllableGolem {

	private IronGolemMixin(EntityType<? extends AbstractGolem> entity, Level world) {
		super(entity, world);
	}

	@Override
	public boolean canBeTuningForkControlled(Player controller) {
		return this.getTarget() != controller && !((NeutralMob) this).isAngryAt(controller);
	}

	@Override
	public boolean shouldAttackTuningForkTarget(LivingEntity target, Player controller) {
		return !(target instanceof Villager);
	}
}