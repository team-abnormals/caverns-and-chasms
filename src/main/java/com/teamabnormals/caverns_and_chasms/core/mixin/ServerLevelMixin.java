package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerLevel.class)
public final class ServerLevelMixin {

	@Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
	private boolean tickChunk(BlockState state, Block block) {
		if (block == Blocks.LIGHTNING_ROD && state.getBlock() instanceof LightningRodBlock) {
			return true;
		}

		return state.is(block);
	}

	@Inject(method = "findLightningRod", at = @At("RETURN"), cancellable = true)
	private void findLightningRod(BlockPos origin, CallbackInfoReturnable<Optional<BlockPos>> cir) {
		ServerLevel level = (ServerLevel) (Object) this;
		Optional<BlockPos> optional = level.getPoiManager().findClosest(holder -> holder.is(PoiTypes.LIGHTNING_ROD) || holder.is(CCPoiTypes.LIGHTNING_ROD.getKey()), pos -> pos.getY() == level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1, origin, 128, PoiManager.Occupancy.ANY);
		if (optional.isPresent()) {
			cir.setReturnValue(optional.map(pos -> pos.above(1)));
		}
	}
}