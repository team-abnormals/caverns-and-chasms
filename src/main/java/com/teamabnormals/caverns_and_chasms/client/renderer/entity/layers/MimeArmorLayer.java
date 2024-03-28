package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class MimeArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {

	public MimeArmorLayer(RenderLayerParent<T, M> parent, A innerModel, A outerModel) {
		super(parent, innerModel, outerModel);
	}

	@Override
	protected void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, T entity, EquipmentSlot slot, int packedLight, A armorModel) {
		ItemStack itemstack = entity.getItemBySlot(slot);
		if (itemstack.getItem() instanceof ArmorItem) {
			ArmorItem armoritem = (ArmorItem)itemstack.getItem();
			if (armoritem.getSlot() == slot) {
				this.getParentModel().copyPropertiesTo(armorModel);
				if (slot == EquipmentSlot.LEGS) {
					this.verticallyOffsetModelPart(this.getParentModel().body, armorModel.body, 1.0F);
				} else if (slot == EquipmentSlot.FEET) {
					this.verticallyOffsetModelPart(this.getParentModel().rightLeg, armorModel.rightLeg, 1.0F);
					this.verticallyOffsetModelPart(this.getParentModel().leftLeg, armorModel.leftLeg, 1.0F);
				}
				this.setPartVisibility(armorModel, slot);
				net.minecraft.client.model.Model model = getArmorModelHook(entity, itemstack, slot, armorModel);
				boolean flag = itemstack.hasFoil();
				if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
					int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
					float f = (float)(i >> 16 & 255) / 255.0F;
					float f1 = (float)(i >> 8 & 255) / 255.0F;
					float f2 = (float)(i & 255) / 255.0F;
					this.renderModel(poseStack, buffer, packedLight, flag, model, f, f1, f2, this.getArmorResource(entity, itemstack, slot, null));
					this.renderModel(poseStack, buffer, packedLight, flag, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, "overlay"));
				} else {
					this.renderModel(poseStack, buffer, packedLight, flag, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, null));
				}
			}
		}
	}

	private void renderModel(PoseStack PoseStack, MultiBufferSource buffer, int packedLight, boolean hasFoil, Model model, float r, float g, float b, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, hasFoil);
		model.renderToBuffer(PoseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
	}

	private void verticallyOffsetModelPart(ModelPart parentModelPart, ModelPart modelPart, float offset) {
		float yaw = parentModelPart.xRot;
		float pitch = parentModelPart.yRot;
		float roll = parentModelPart.zRot;

		modelPart.x += (Mth.sin(yaw) * Mth.sin(pitch) * Mth.cos(roll) - Mth.cos(yaw) * Mth.sin(roll)) * offset;
		modelPart.y += (Mth.sin(yaw) * Mth.sin(pitch) * Mth.sin(roll) + Mth.cos(yaw) * Mth.cos(roll)) * offset;
		modelPart.z += Mth.sin(yaw) * Mth.cos(pitch) * offset;
	}
}