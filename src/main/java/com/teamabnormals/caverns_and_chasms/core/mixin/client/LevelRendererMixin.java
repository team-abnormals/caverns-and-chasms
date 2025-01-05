package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 30), method = "levelEvent")
	private void playLocalSound(ClientLevel level, BlockPos pos, SoundEvent soundEvent, SoundSource source, float x, float y, boolean b) {
		if (level.getBlockState(pos).is(CCBlocks.DISMANTLING_TABLE.get())) {
			level.playLocalSound(pos, CCSoundEvents.DISMANTLING_TABLE_US.get(), source, x, y, b);
		} else {
			level.playLocalSound(pos, soundEvent, source, x, y, b);
		}
	}
}