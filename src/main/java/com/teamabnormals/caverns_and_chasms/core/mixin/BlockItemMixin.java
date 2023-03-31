package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {

	@Shadow
	public abstract Block getBlock();

	public BlockItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "canFitInsideContainerItems", at = @At("RETURN"), cancellable = true)
	private void canFitInsideContainerItems(CallbackInfoReturnable<Boolean> cir) {
		if (this.getBlock() instanceof ToolboxBlock) {
			cir.setReturnValue(false);
		}
	}
}