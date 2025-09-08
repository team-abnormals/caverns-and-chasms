package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure.Type;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MineshaftStructure.Type.class)
public class MineshaftStructureTypeMixin {
	@Shadow
	@Final
	private static MineshaftStructure.Type[] $VALUES;

	@Mutable
	@Shadow
	@Final
	public static Codec<Type> CODEC;

	@Invoker(value = "<init>")
	private static MineshaftStructure.Type create(String valueName, int ordinal, String name, Block woodBlock, Block planksBlock, Block fenceBlock) {
		throw new IllegalStateException("Unreachable");
	}

	static {
		var entry = create("CAVERNS_AND_CHASMS_LUSH", $VALUES.length, "lush", Blocks.MANGROVE_LOG, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE);
		//noinspection ShadowFinalModification
		$VALUES = ArrayUtils.add($VALUES, entry);

		CODEC = StringRepresentable.fromEnum(() -> $VALUES);
	}

	@Inject(method = "getWoodState", at = @At("RETURN"), cancellable = true)
	private void getWoodState(CallbackInfoReturnable<BlockState> cir) {
		if (cir.getReturnValue().is(Blocks.MANGROVE_LOG)) {
			cir.setReturnValue(CCBlocks.AZALEA_LOG.get().defaultBlockState());
		}
	}

	@Inject(method = "getPlanksState", at = @At("RETURN"), cancellable = true)
	private void getPlanksState(CallbackInfoReturnable<BlockState> cir) {
		if (cir.getReturnValue().is(Blocks.MANGROVE_PLANKS)) {
			cir.setReturnValue(CCBlocks.AZALEA_PLANKS.get().defaultBlockState());
		}
	}

	@Inject(method = "getFenceState", at = @At("RETURN"), cancellable = true)
	private void getFenceState(CallbackInfoReturnable<BlockState> cir) {
		if (cir.getReturnValue().is(Blocks.MANGROVE_FENCE)) {
			cir.setReturnValue(CCBlocks.AZALEA_FENCE.get().defaultBlockState());
		}
	}
}
