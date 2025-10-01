package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(InteractWithDoor.class)
public abstract class InteractWithDoorMixin {

//	@WrapOperation(method = "lambda$create$3", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;Ljava/util/function/Predicate;)Z"))
//	private static boolean create(BlockState state, TagKey tagKey, Predicate predicate, Operation<Boolean> original) {
//		return state.is(CCBlockTags.MOB_INTERACTABLE_DOORS);
//	}

	@WrapOperation(method = "closeDoorsThatIHaveOpenedOrPassedThrough", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;Ljava/util/function/Predicate;)Z"))
	private static boolean closeDoorsThatIHaveOpenedOrPassedThrough(BlockState state, TagKey tagKey, Predicate predicate, Operation<Boolean> original) {
		return state.is(CCBlockTags.MOB_INTERACTABLE_DOORS);
	}
}
