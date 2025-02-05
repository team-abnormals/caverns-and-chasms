package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.CopyRecordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "getAnalogOutputSignal")
	private ItemStack startPlayingGetFirstItem(JukeboxBlockEntity entity) {
		ItemStack stack = entity.getFirstItem();
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}
}