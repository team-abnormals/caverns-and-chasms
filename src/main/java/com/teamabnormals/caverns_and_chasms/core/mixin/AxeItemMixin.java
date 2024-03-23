package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {

	@Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean useOn(Level level, BlockPos pos, BlockState state, int flags) {
		if (level.getBlockEntity(pos) instanceof ToolboxBlockEntity toolbox) {
			CompoundTag tag = toolbox.serializeNBT();
			boolean success = level.setBlock(pos, state, flags);
			if (success) {
				level.getBlockEntity(pos).deserializeNBT(tag);
			}
			return success;
		} else {
			return level.setBlock(pos, state, flags);
		}
	}
}
