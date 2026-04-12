package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
	private void isValidRepairItem(ItemStack item, ItemStack repairIngredient, CallbackInfoReturnable<Boolean> cir) {
		if (repairIngredient.is(CCItems.ZIRCONIA.get()) && !item.is(CCItemTags.UNREPAIRABLE_BY_ZIRCONIA) && CCConfig.COMMON.zirconiaUniversalRepairing.get()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
	private void useItem(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack stack = context.getItemInHand();
		if (stack.is(CCItemTags.PLACEABLE_ITEMS)) {
			Optional<Registry<Item>> registry = context.getLevel().registryAccess().registry(Registries.ITEM);
			if (registry.isPresent()) {
				for (Item item1 : registry.get()) {
					if (item1 instanceof BlockItem blockItem && stack.is(blockItem.getBlock().asItem())) {
						cir.setReturnValue(item1.useOn(context));
						break;
					}
				}
			}
		}

	}
}