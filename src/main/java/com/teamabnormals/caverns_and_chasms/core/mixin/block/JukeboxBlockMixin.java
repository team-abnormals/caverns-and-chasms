package com.teamabnormals.caverns_and_chasms.core.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.item.CopyRecordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "getAnalogOutputSignal")
	private ItemStack startPlayingGetFirstItem(JukeboxBlockEntity entity, Operation<ItemStack> original) {
		ItemStack stack = original.call(entity);
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}
}