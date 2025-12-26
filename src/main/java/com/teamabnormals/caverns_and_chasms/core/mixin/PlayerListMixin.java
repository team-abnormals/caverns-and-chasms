package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.network.S2CUpdateAttachedRatsMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
			CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(player.level()::dimension), new S2CUpdateAttachedRatsMessage((RatHolder) player));
		}
	}

	@Inject(at = @At("RETURN"), method = "placeNewPlayer", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void spawnRats(Connection connection, ServerPlayer player, CallbackInfo info, GameProfile gameProfile, GameProfileCache gameProfileCache, String s, CompoundTag compoundTag) {
		ServerLevel serverlevel = (ServerLevel) player.level();

		if (compoundTag != null && compoundTag.contains("AttachedRats", 9)) {
			ListTag ratstag = compoundTag.getList("AttachedRats", 10);
			if (!ratstag.isEmpty()) {
				for (int i = 0; i < ratstag.size(); i++) {
					Entity entity = EntityType.loadEntityRecursive(ratstag.getCompound(i), serverlevel, (rat -> !serverlevel.addWithUUID(rat) ? null : rat));
					if (entity instanceof Rat rat) {
						rat.attachToEntity(player);
						CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(serverlevel::dimension), new S2CUpdateAttachedRatsMessage((RatHolder) player));
					}
				}
			}
		}
	}
}
