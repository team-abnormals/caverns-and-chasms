package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.item.CopyRecordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "startPlaying")
	private ItemStack startPlayingGetFirstItem(JukeboxBlockEntity entity, Operation<ItemStack> original) {
		ItemStack stack = original.call(entity);
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "tick")
	private ItemStack tickGetFirstItem(JukeboxBlockEntity entity, Operation<ItemStack> original) {
		ItemStack stack = original.call(entity);
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}
}