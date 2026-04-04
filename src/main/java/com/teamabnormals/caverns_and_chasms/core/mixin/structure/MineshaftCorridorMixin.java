package com.teamabnormals.caverns_and_chasms.core.mixin.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.blueprint.core.util.BlockUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftCorridor;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MineshaftPieces.MineShaftCorridor.class)
public class MineshaftCorridorMixin {

	@WrapOperation(method = "placeSupport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;maybeGenerateBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;FIIILnet/minecraft/world/level/block/state/BlockState;)V"))
	private void placeSupport(MineShaftCorridor instance, WorldGenLevel level, BoundingBox aabb, RandomSource random, float v, int i, int j, int k, BlockState state, Operation<Void> original) {
		original.call(instance, level, aabb, random, v, i, j, k, BlockUtil.transferAllBlockStates(state, (instance.type == Type.MESA ? CCBlocks.ORANGE_SPARKLER : CCBlocks.SPARKLER).getSecond().get().defaultBlockState()));
	}
}
