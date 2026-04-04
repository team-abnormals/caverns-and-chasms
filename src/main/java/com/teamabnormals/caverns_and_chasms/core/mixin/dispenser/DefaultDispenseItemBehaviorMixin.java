package com.teamabnormals.caverns_and_chasms.core.mixin.dispenser;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.block.ScattererBlock;
import com.teamabnormals.caverns_and_chasms.common.dispenser.SplurterDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultDispenseItemBehavior.class)
public abstract class DefaultDispenseItemBehaviorMixin {

	@WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/dispenser/DefaultDispenseItemBehavior;spawnItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;Lnet/minecraft/core/Position;)V"))
	private void modifySpawnItem(Level level, ItemStack stack, int i, Direction dir, Position pos, Operation<Void> original, BlockSource source) {
		if (source.getBlockState().getBlock() instanceof ScattererBlock scatterer) {
			SplurterDispenseItemBehavior.shootItem(level, stack, i, dir, pos, scatterer.powerLevel, false);
		} else {
			original.call(level, stack, i, dir, pos);
		}
	}
}