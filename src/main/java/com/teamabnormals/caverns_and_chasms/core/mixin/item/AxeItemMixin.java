package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {

	@WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean useOn(Level level, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
		BlockEntity entity = level.getBlockEntity(pos);
		boolean success = original.call(level, pos, state, flags);
		if (entity instanceof ToolboxBlockEntity toolbox) {
			CompoundTag tag = toolbox.serializeNBT();
			if (success) {
				level.getBlockEntity(pos).deserializeNBT(tag);
			}
		}
		return success;
	}
}
