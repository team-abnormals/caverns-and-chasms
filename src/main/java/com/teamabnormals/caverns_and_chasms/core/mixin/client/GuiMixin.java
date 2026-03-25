package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.common.item.BoneFluteCommand;
import com.teamabnormals.caverns_and_chasms.common.item.BoneFluteItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@Unique
	private static final ResourceLocation BONE_FLUTE_CROSSHAIR_FRAME = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/frame.png");

	@Shadow
	@Final
	protected Minecraft minecraft;

	@ModifyVariable(method = "renderCrosshair", at = @At("STORE"), ordinal = 0)
	private boolean showAttackIndicatorOnGrazerParts(boolean flag) {
		if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof GrazerPart && this.minecraft.player.getAttackStrengthScale(0.0F) >= 1.0F) {
			boolean flag1 = this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0F;
			flag1 &= this.minecraft.crosshairPickEntity.isAlive();
			return flag1;
		} else {
			return flag;
		}
	}

	@WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 0))
	private void renderCrosshair(GuiGraphics guiGraphics, ResourceLocation location, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, Operation<Void> original) {
		if (this.minecraft.player.getMainHandItem().is(CCItems.BONE_FLUTE.get()) || this.minecraft.player.getOffhandItem().is(CCItems.BONE_FLUTE.get())) {
			BoneFluteCommand command = BoneFluteItem.getCommand(this.minecraft.player, BoneFluteItem.getHitResult(this.minecraft.player));
			if (command != null) {
				float cooldown = this.minecraft.player.getCooldowns().getCooldownPercent(CCItems.BONE_FLUTE.get(), this.minecraft.getFrameTime());
				int i = 7 - Mth.ceil(cooldown * 7);
				guiGraphics.blit(BONE_FLUTE_CROSSHAIR_FRAME, x - 1, y - 1, 0.0F, 0.0F, 17, 17, 17, 17);
				guiGraphics.blit(command.getCrosshairIconBackground(), x + 4 + i, y + 4, i, 0.0F, 7 - i, 7, 7, 7);
				guiGraphics.blit(command.getCrosshairIcon(), x + 4, y + 4, 0.0F, 0.0F, i, 7, 7, 7);
				return;
			}
		}
		original.call(guiGraphics, location, x, y, uOffset, vOffset, uWidth, vHeight);
	}
}