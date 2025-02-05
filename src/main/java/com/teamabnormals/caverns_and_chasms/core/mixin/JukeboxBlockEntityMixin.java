package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.CopyRecordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "startPlaying")
	private ItemStack startPlayingGetFirstItem(JukeboxBlockEntity entity) {
		ItemStack stack = entity.getFirstItem();
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;getFirstItem()Lnet/minecraft/world/item/ItemStack;"), method = "tick")
	private ItemStack tickGetFirstItem(JukeboxBlockEntity entity) {
		ItemStack stack = entity.getFirstItem();
		if (stack.getItem() instanceof CopyRecordItem copy) {
			return copy.getDiscStack(stack);
		}
		return stack;
	}
}