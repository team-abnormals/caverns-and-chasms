package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

	@Inject(method = "onDestroyed", at = @At("TAIL"))
	private void onDestroyed(ItemEntity itementity, CallbackInfo ci) {
		if (this.getBlock() instanceof ToolboxBlock) {
			ItemStack itemstack = itementity.getItem();
			CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
			if (compoundtag != null && compoundtag.contains("Items", 9)) {
				ListTag listtag = compoundtag.getList("Items", 10);
				ItemUtils.onContainerDestroyed(itementity, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of));
			}
		}
	}
}