package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ServerLevel.class)
public final class ServerLevelMixin {
	@Shadow
	@Final
	EntityTickList entityTickList;

	@WrapOperation(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
	private boolean tickChunk(BlockState state, Block block, Operation<Boolean> original) {
		return (block == Blocks.LIGHTNING_ROD && state.getBlock() instanceof LightningRodBlock) || original.call(state, block);
	}

	@Inject(method = "findLightningRod", at = @At("RETURN"), cancellable = true)
	private void findLightningRod(BlockPos origin, CallbackInfoReturnable<Optional<BlockPos>> cir) {
		ServerLevel level = (ServerLevel) (Object) this;
		BlockPos closestPos = null;

		Optional<BlockPos> optional = level.getPoiManager().findClosest(holder -> holder.is(PoiTypes.LIGHTNING_ROD) || holder.is(CCPoiTypes.LIGHTNING_ROD.getKey()), pos -> pos.getY() == level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1, origin, 128, PoiManager.Occupancy.ANY);
		if (optional.isPresent()) {
			closestPos = optional.get().above(1);
		}

		AABB aabb = (new AABB(origin, new BlockPos(origin.getX(), level.getMaxBuildHeight(), origin.getZ()))).inflate(128.0D);
		List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> {
			return entity != null && entity.isAlive() && level.canSeeSky(entity.blockPosition()) && entity.getItemBySlot(EquipmentSlot.HEAD).is(CCItemTags.COPPER_HELMETS);
		});

		if (!list.isEmpty()) {
			for (LivingEntity entity : list) {
				if (closestPos == null || entity.blockPosition().distSqr(origin) < closestPos.distSqr(origin)) {
					closestPos = entity.blockPosition();
				}
			}
		}

		if (closestPos != null) {
			cir.setReturnValue(Optional.of(closestPos));
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 0, shift = At.Shift.AFTER), method = "tickNonPassenger")
	private void updateRats(Entity entity, CallbackInfo info) {
		if (entity instanceof RatHolder ratholder)
			ratholder.tickRats(this.entityTickList);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER), method = "tickPassenger")
	private void updatePassengerRats(Entity ridingEntity, Entity passenger, CallbackInfo info) {
		if (passenger instanceof RatHolder ratholder)
			ratholder.tickRats(this.entityTickList);
	}
}