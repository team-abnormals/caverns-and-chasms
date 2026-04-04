package com.teamabnormals.caverns_and_chasms.core.mixin.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure.Type;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MineshaftStructure.class)
public class MineshaftStructureMixin {

	@Shadow
	@Final
	private Type type;

	@Inject(method = "findGenerationPoint", at = @At("RETURN"), cancellable = true)
	private void findGenerationPoint(GenerationContext context, CallbackInfoReturnable<Optional<GenerationStub>> cir) {
		if (this.type == Type.NORMAL && cir.getReturnValue().isPresent() && isLushCaves(cir.getReturnValue().get(), context)) {
			cir.setReturnValue(Optional.empty());
		}
	}

	private static boolean isLushCaves(Structure.GenerationStub stub, Structure.GenerationContext context) {
		BlockPos pos = stub.position();
		return context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), context.randomState().sampler()).is(Biomes.LUSH_CAVES);
	}
}
