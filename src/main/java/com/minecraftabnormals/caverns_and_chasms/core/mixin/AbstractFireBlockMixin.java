package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.common.block.CursedFireBlock;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public final class AbstractFireBlockMixin extends Block {

	private AbstractFireBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "getState", cancellable = true)
	private static void cursedFirePlacement(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		if (CursedFireBlock.canSurviveOnBlock(reader.getBlockState(pos.below()))) {
			info.setReturnValue(CCBlocks.CURSED_FIRE.get().defaultBlockState());
		}
	}
}