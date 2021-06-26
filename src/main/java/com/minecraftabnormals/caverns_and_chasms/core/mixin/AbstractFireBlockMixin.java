package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.common.block.CursedFireBlock;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock.Properties;

@Mixin(AbstractFireBlock.class)
public final class AbstractFireBlockMixin extends Block {

	private AbstractFireBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "getState", cancellable = true)
	private static void cursedFirePlacement(IBlockReader reader, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		if (CursedFireBlock.isCursedFireBase(reader.getBlockState(pos.below()).getBlock())) {
			info.setReturnValue(CCBlocks.CURSED_FIRE.get().defaultBlockState());
		}
	}
}