package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {

	@Shadow
	public abstract void addCooldown(Item p_41525_, int p_41526_);

	@Shadow
	public abstract boolean isOnCooldown(Item p_41520_);

	@Inject(at = @At("RETURN"), method = "addCooldown")
	private void getCooldownPercent(Item item, int duration, CallbackInfo ci) {
		if (item == CCItems.LOST_GOAT_HORN.get()) {
			this.addCooldown(duration, Items.GOAT_HORN, CCItems.COPPER_HORN.get());
		} else if (item == Items.GOAT_HORN) {
			this.addCooldown(duration, CCItems.LOST_GOAT_HORN.get(), CCItems.COPPER_HORN.get());
		} else if (item == CCItems.COPPER_HORN.get()) {
			this.addCooldown(duration, Items.GOAT_HORN, CCItems.LOST_GOAT_HORN.get());
		}
	}

	@Unique
	private void addCooldown(int duration, Item... items) {
		for (Item item : items) {
			if (!this.isOnCooldown(item)) {
				this.addCooldown(item, duration);
			}
		}
	}
}