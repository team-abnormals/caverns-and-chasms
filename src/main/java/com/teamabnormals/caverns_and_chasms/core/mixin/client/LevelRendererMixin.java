package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 30), method = "levelEvent")
	private void playDismantlingTableUseSound(ClientLevel level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, boolean b, Operation<Void> original) {
		original.call(level, pos, level.getBlockState(pos).is(CCBlocks.DISMANTLING_TABLE.get()) ? CCSoundEvents.DISMANTLING_TABLE_USE.get() : soundEvent, soundSource, volume, pitch, b);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 19), method = "levelEvent")
	private void playBejeweledAnvilUseSound(ClientLevel level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, boolean b, Operation<Void> original) {
		if (level.getBlockState(pos).is(CCBlocks.BEJEWELED_ANVIL.get())) {
			level.playLocalSound(pos, CCSoundEvents.BEJEWELED_ANVIL_USE.get(), soundSource, volume, level.random.nextFloat() * 0.1F + 0.9F, b);
			level.playLocalSound(pos, CCSoundEvents.BEJEWELED_ANVIL_SHATTER.get(), soundSource, volume, 0.8F + level.random.nextFloat() * 0.4F, b);
		} else {
			original.call(level, pos, soundEvent, soundSource, volume, pitch, b);
		}
	}
}