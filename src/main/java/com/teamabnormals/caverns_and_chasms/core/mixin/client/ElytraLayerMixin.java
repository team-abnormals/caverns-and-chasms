package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	@Unique
	private static final ResourceLocation MIME_WINGS_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/mime_elytra.png");

	public ElytraLayerMixin(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Inject(method = "getElytraTexture", at = @At("RETURN"), cancellable = true, remap = false)
	public void isRandomlyTicking(ItemStack stack, T entity, CallbackInfoReturnable<ResourceLocation> cir) {
		if (entity.getType() == CCEntityTypes.MIME.get() || entity.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MIME_HEAD.get())) {
			cir.setReturnValue(MIME_WINGS_LOCATION);
		}
	}
}