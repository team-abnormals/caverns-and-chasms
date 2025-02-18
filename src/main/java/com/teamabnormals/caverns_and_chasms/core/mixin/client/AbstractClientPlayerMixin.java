package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {

	public AbstractClientPlayerMixin(Level level, BlockPos pos, float f, GameProfile profile) {
		super(level, pos, f, profile);
	}

	@Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
	private void getFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
		if (this.isUsingItem() && Minecraft.getInstance().options.getCameraType().isFirstPerson() && this.getUseItem().is(CCItems.MONOCLE.get())) {
			cir.setReturnValue(1.5F);
		}
	}
}