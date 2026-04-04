package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.CopperHorseArmorModel;
import com.teamabnormals.caverns_and_chasms.common.item.copper.CopperHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public abstract class HorseArmorLayerMixin extends RenderLayer<Horse, HorseModel<Horse>> {
	@Mutable
	@Shadow
	@Final
	private HorseModel<Horse> model;
	@Unique
	private CopperHorseArmorModel<Horse> copperHorseArmorModel;

	public HorseArmorLayerMixin(RenderLayerParent<Horse, HorseModel<Horse>> parent) {
		super(parent);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(RenderLayerParent<Horse, HorseModel<Horse>> parent, EntityModelSet context, CallbackInfo ci) {
		this.copperHorseArmorModel = new CopperHorseArmorModel<>(context.bakeLayer(CCModelLayers.COPPER_HORSE_ARMOR));
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At("HEAD"))
	private void init(PoseStack p_117032_, MultiBufferSource p_117033_, int p_117034_, Horse horse, float p_117036_, float p_117037_, float p_117038_, float p_117039_, float p_117040_, float p_117041_, CallbackInfo ci) {
		if (horse.getArmor().getItem() instanceof CopperHorseArmorItem) {
			this.model = this.copperHorseArmorModel;
		}
	}
}