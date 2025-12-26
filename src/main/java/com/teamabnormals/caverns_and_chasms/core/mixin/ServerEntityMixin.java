package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.network.S2CUpdateAttachedRatsMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerEntity.class)
public final class ServerEntityMixin {
	@Unique
	private List<Rat> prevRats = Lists.newArrayList();

	@Shadow
	@Final
	private Entity entity;

	@Shadow @Final private ServerLevel level;

	@Inject(at = @At("HEAD"), method = "sendChanges")
	private void updateRats(CallbackInfo info) {
		if (this.entity instanceof RatHolder ratholder) {
			List<Rat> currentrats = ratholder.getAttachedRats();
			if (!currentrats.equals(this.prevRats)) {
				this.prevRats = currentrats;
				// TODO: Try to figure out what the problem with tracking entity is. Might also be a problem in PlayerListMixin, which is why I set it to dimension too.
				CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> this.level.dimension()), new S2CUpdateAttachedRatsMessage(ratholder));
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"), method = "sendChanges")
	private boolean wrapPositionUpdate(Entity trackedEntity, Operation<Boolean> original) {
		return original.call(trackedEntity) || trackedEntity instanceof Rat rat && rat.isAttachedToEntity();
	}
}