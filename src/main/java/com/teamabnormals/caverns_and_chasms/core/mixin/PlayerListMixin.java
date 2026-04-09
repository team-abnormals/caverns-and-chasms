package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.network.UpdateAttachedRatsPayload;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;save(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER), method = "remove")
	private void removeRats(ServerPlayer player, CallbackInfo info) {
		for (Rat rat : ((RatHolder) player).getAttachedRats()) {
			if (!rat.isTame()) {
				rat.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
			} else {
				rat.detachFromEntity();
			}
			PacketDistributor.sendToPlayersInDimension(player.serverLevel(), new UpdateAttachedRatsPayload((RatHolder) player));
		}
	}

	@Inject(at = @At("RETURN"), method = "placeNewPlayer", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnRats(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo info, GameProfile gameProfile, GameProfileCache gameProfileCache, String s, Optional<CompoundTag> compoundTag) {
		ServerLevel serverlevel = player.serverLevel();
		if (compoundTag.isPresent() && compoundTag.get().contains("AttachedRats", 9)) {
			ListTag ratstag = compoundTag.get().getList("AttachedRats", 10);
			if (!ratstag.isEmpty()) {
				for (int i = 0; i < ratstag.size(); i++) {
					Entity entity = EntityType.loadEntityRecursive(ratstag.getCompound(i), serverlevel, (rat -> !serverlevel.addWithUUID(rat) ? null : rat));
					if (entity instanceof Rat rat) {
						rat.setAttachedToEntity(player);
						PacketDistributor.sendToPlayersInDimension(serverlevel, new UpdateAttachedRatsPayload((RatHolder) player));
					}
				}
			}
		}
	}
}
