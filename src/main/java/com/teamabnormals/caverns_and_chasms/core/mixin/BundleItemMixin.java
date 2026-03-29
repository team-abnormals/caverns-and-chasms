package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin implements DyeableLeatherItem {

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");
		return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
	}

	@Inject(method = "overrideOtherStackedOnMe", at = @At("RETURN"))
	private void overrideOtherStackedOnMe(ItemStack p_150742_, ItemStack p_150743_, Slot slot, ClickAction p_150745_, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			player.containerMenu.slotsChanged(player.getInventory());
		}
	}
}
