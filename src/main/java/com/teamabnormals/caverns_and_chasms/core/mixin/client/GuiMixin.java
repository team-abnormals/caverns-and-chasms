package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@Shadow
	@Final
	protected Minecraft minecraft;

	@ModifyVariable(method = "renderCrosshair", at = @At("STORE"), ordinal = 0)
	private boolean renderCrosshair(boolean flag) {
		if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof GrazerPart && this.minecraft.player.getAttackStrengthScale(0.0F) >= 1.0F) {
			boolean flag1 = this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0F;
			flag1 &= this.minecraft.crosshairPickEntity.isAlive();
			return flag1;
		} else {
			return flag;
		}
	}
}