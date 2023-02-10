package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCPotionUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	@Final
	@Shadow
	private A innerModel;

	@Shadow
	private void renderModel(PoseStack stack, MultiBufferSource source, int packedLight, boolean hasFoil, net.minecraft.client.model.Model odel, float red, float green, float blue, ResourceLocation armorResource) {
	}

	private static final ResourceLocation TETHER_POTION_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion.png");
	private static final ResourceLocation TETHER_POTION_OVERLAY_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion_overlay.png");

	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> entityRenderer) {
		super(entityRenderer);
	}

	@Inject(at = @At("TAIL"), method = "render")
	public void render(PoseStack stack, MultiBufferSource source, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		this.renderWornTetherPotion(stack, source, packedLight, entity);
	}

	private void renderWornTetherPotion(PoseStack stack, MultiBufferSource source, int packedLight, T entity) {
		ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (itemstack.getItem() instanceof TetherPotionItem) {
			this.getParentModel().copyPropertiesTo(this.innerModel);

			this.innerModel.setAllVisible(false);
			this.innerModel.head.visible = true;
			this.innerModel.hat.visible = true;

			boolean flag = itemstack.hasFoil();
			int i = CCPotionUtil.getTetherPotionColor(itemstack);
			float r = (float) (i >> 16 & 255) / 255.0F;
			float g = (float) (i >> 8 & 255) / 255.0F;
			float b = (float) (i & 255) / 255.0F;

			this.renderModel(stack, source, packedLight, flag, this.innerModel, r, g, b, TETHER_POTION_LOCATION);
			this.renderModel(stack, source, packedLight, flag, this.innerModel, 1.0F, 1.0F, 1.0F, TETHER_POTION_OVERLAY_LOCATION);
		}
	}
}
