package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
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
	private A outerModel;

	@Shadow
	protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, ArmorItem p_289650_, Model p_289658_, boolean p_289668_, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource);

	private static final ResourceLocation TETHER_POTION_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion.png");
	private static final ResourceLocation TETHER_POTION_OVERLAY_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion_overlay.png");

	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> entityRenderer) {
		super(entityRenderer);
	}

	@Inject(at = @At("TAIL"), method = "render")
	public void render(PoseStack stack, MultiBufferSource source, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		this.renderWornTetherPotion(stack, source, packedLight, entity);
	}

	private void renderWornTetherPotion(PoseStack poseStack, MultiBufferSource source, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof TetherPotionItem) {
			this.getParentModel().copyPropertiesTo(this.outerModel);

			this.outerModel.setAllVisible(false);
			this.outerModel.head.visible = true;
			this.outerModel.hat.visible = true;

			boolean flag = stack.hasFoil();
			int i = PotionUtils.getColor(stack);
			float r = (float) (i >> 16 & 255) / 255.0F;
			float g = (float) (i >> 8 & 255) / 255.0F;
			float b = (float) (i & 255) / 255.0F;

			this.renderModel(poseStack, source, packedLight, null, this.outerModel, flag, r, g, b, TETHER_POTION_LOCATION);
			this.renderModel(poseStack, source, packedLight, null, this.outerModel, flag, 1.0F, 1.0F, 1.0F, TETHER_POTION_OVERLAY_LOCATION);
		}
	}
}
