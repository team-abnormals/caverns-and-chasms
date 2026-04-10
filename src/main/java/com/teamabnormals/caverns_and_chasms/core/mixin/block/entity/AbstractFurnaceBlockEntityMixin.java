package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

	@Shadow
	protected NonNullList<ItemStack> items;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = Shift.BEFORE), method = "burn")
	private static void burn(RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Boolean> cir) {
		ItemStack stack = inventory.get(1);
		if (inventory.get(0).is(Blocks.WET_SPONGE.asItem()) && !stack.isEmpty()) {
			if (stack.is(CCItems.GOLDEN_BUCKET.get())) {
				inventory.set(1, new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get()));
			} else if (stack.is(CCItems.GOLDEN_WATER_BUCKET.get()) && GoldenBucketItem.canBeFilled(stack)) {
				inventory.set(1, GoldenBucketItem.increaseFluidLevel(stack));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "canTakeItemThroughFace", cancellable = true)
	private void canTakeItemThroughFace(int index, ItemStack stack, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (direction == Direction.DOWN && index == 1 && stack.is(CCItems.GOLDEN_WATER_BUCKET.get()) || stack.is(CCItems.GOLDEN_BUCKET.get())) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("RETURN"), method = "canPlaceItem", cancellable = true)
	private void canPlaceItem(int index, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		ItemStack existingStack = this.items.get(1);
		if (index == 1 && (stack.is(CCItems.GOLDEN_BUCKET.get()) && !existingStack.is(CCItems.GOLDEN_BUCKET.get())) || (stack.is(CCItems.GOLDEN_WATER_BUCKET.get()) && GoldenBucketItem.canBeFilled(stack))) {
			cir.setReturnValue(true);
		}
	}
}