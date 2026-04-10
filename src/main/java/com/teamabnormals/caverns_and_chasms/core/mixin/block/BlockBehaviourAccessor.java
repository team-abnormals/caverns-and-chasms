package com.teamabnormals.caverns_and_chasms.core.mixin.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor {
	@Invoker("getMaxHorizontalOffset")
	float invokeGetMaxHorizontalOffset();

	@Invoker("getMaxVerticalOffset")
	float invokeGetMaxVerticalOffset();
}