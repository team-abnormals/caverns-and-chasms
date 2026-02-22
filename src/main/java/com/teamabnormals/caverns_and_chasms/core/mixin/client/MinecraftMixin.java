package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.HoldableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow private int rightClickDelay;

	@Shadow @Nullable public ClientLevel level;

	@Shadow @Nullable public HitResult hitResult;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;consumesAction()Z", ordinal = 2, shift = At.Shift.AFTER), method = "startUseItem")
	public void startUseItem(CallbackInfo ci) {
		if (this.level.getBlockState(((BlockHitResult) this.hitResult).getBlockPos()).getBlock() instanceof HoldableBlock) {
			this.rightClickDelay = 0;
		}
	}
}