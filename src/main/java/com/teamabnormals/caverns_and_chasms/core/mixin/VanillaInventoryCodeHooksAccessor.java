package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.VanillaInventoryCodeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(VanillaInventoryCodeHooks.class)
public class VanillaInventoryCodeHooksAccessor {

	@Invoker("getItemHandlerAt")
	public static Optional<Pair<IItemHandler, Object>> invokeGetItemHandlerAt(Level worldIn, double x, double y, double z, final Direction side) {
		return Optional.empty();
	}
}
