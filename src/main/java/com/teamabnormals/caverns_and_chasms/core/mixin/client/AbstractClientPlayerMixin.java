package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
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
		if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && MonocleItem.isUsingMonocle(this)) {
			float amount = 1.3F;
			if (this.isUsingItem() && this.getUseItem().is(CCItems.MONOCLE.get()) && this.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MONOCLE.get())) {
				amount = 1.45F;
			}
			cir.setReturnValue(cir.getReturnValue() * amount);
		}
	}

	@Inject(method = "getCloakTextureLocation", at = @At("RETURN"), cancellable = true)
	private void getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
		if (this.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.COWL.get())) {
			cir.setReturnValue(CavernsAndChasms.location("textures/models/armor/cowl_cloak.png"));
		}
	}

	@Inject(method = "getElytraTextureLocation", at = @At("RETURN"), cancellable = true)
	private void getElytraTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
		if (this.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.COWL.get())) {
			cir.setReturnValue(CavernsAndChasms.location("textures/models/armor/cowl_cloak.png"));
		}
	}
}