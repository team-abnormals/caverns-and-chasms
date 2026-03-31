package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MovingPlayer {
	private boolean moving;

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "checkMovementStatistics", at = @At("HEAD"))
	private void checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
		this.moving = Math.sqrt(x * x + y * y + z * z) > 0;
	}

	public boolean isMoving() {
		return this.moving;
	}

	@Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
	private void writeRats(CompoundTag compound, CallbackInfo info) {
		List<Rat> rats = ((RatHolder) this).getAttachedRats();
		if (!rats.isEmpty()) {
			ListTag ratstag = new ListTag();

			for (Rat rat : rats) {
				if (!((Object) this instanceof Player) || !rat.isTame()) {
					CompoundTag compoundnbt = new CompoundTag();
					if (rat.saveAsPassenger(compoundnbt)) {
						ratstag.add(compoundnbt);
					}
				}
			}

			if (!ratstag.isEmpty()) {
				compound.put("AttachedRats", ratstag);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), method = "eat")
	private void playSound(Level level, Player player, double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, Operation<Void> original, Level lvl, ItemStack stack) {
		if (stack.is(CCItems.CAVIAR.get())) {
			level.playSound(player, this, CCSoundEvents.CAVIAR_BURP.get(), soundSource, volume, pitch);
		} else {
			original.call(level, player, x, y, z, stack.is(CCItems.BEJEWELED_APPLE.get()) ? CCSoundEvents.BEJEWELED_APPLE_BURP.get() : soundEvent, soundSource, volume, pitch);
		}
	}
}