package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;save(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER), method = "remove")
	private void removeBalloons(ServerPlayer player, CallbackInfo info) {
		List<Rat> attachedrats = ((RatHolder) player).getAttachedRats();
		for (Rat rat : attachedrats) {
			if (!rat.isTame())
				rat.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
		}
	}
}
