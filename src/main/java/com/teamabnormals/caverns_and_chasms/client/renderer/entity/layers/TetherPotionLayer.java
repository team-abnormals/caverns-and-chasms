package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCPotionUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TetherPotionLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TETHER_POTION_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion.png");
    private static final ResourceLocation TETHER_POTION_OVERLAY_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/models/armor/tether_potion_overlay.png");
    private final A model;

    public TetherPotionLayer(RenderLayerParent<T, M> renderer, A modelIn) {
        super(renderer);
        this.model = modelIn;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource source, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (itemstack.getItem() instanceof TetherPotionItem) {
            this.getParentModel().copyPropertiesTo(this.model);

            this.model.setAllVisible(false);
            this.model.head.visible = true;
            this.model.hat.visible = true;

            boolean flag = itemstack.hasFoil();
            int i = CCPotionUtil.getTetherPotionColor(itemstack);
            float r = (float)(i >> 16 & 255) / 255.0F;
            float g = (float)(i >> 8 & 255) / 255.0F;
            float b = (float)(i & 255) / 255.0F;

            this.renderModel(stack, source, packedLight, flag, r, g, b, TETHER_POTION_LOCATION);
            this.renderModel(stack, source, packedLight, flag, 1.0F, 1.0F, 1.0F, TETHER_POTION_OVERLAY_LOCATION);
        }
    }

    private void renderModel(PoseStack stack, MultiBufferSource source, int packedLight, boolean hasFoil, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(armorResource), false, hasFoil);
        this.model.renderToBuffer(stack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}