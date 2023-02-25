package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends LivingEntity {

	protected CreeperMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Redirect(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;"))
	private Explosion explodeCreeper(Level level, Entity creeper, double x, double y, double z, float power, BlockInteraction interaction) {
		if (CCConfig.COMMON.creeperExplosionNerf.get()) {
			interaction = BlockInteraction.BREAK;
		}
		if (CCConfig.COMMON.creeperExplosionNerf.get()) {
			power *= CCConfig.COMMON.creeperExplosionNerfFactor.get().floatValue();
		}

		return level.explode(creeper, x, y, z, power, interaction);
	}
}
