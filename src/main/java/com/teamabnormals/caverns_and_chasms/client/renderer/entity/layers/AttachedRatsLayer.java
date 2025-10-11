package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel.RatPose;
import com.teamabnormals.caverns_and_chasms.common.entity.RatHolder;
import com.teamabnormals.caverns_and_chasms.common.entity.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AttachedRatsLayer<T extends LivingEntity & RatHolder, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final RatModel<Rat> ratModel;
	private final ItemInHandRenderer itemInHandRenderer;

	public AttachedRatsLayer(RenderLayerParent<T, M> parent, EntityModelSet entityModelSet, ItemInHandRenderer itemInHandRenderer) {
		super(parent);
		this.ratModel = new RatModel<>(entityModelSet.bakeLayer(CCModelLayers.RAT));
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		RandomSource random = RandomSource.create(entity.getId());
		for (AttachedRat ratdata : entity.getAttachedRats()) {
			CompoundTag compound = ratdata.getEntityData();
			poseStack.pushPose();

			float animtime = ageInTicks + random.nextFloat() * 100.0F;

			float angle = ratdata.getAngle();
			poseStack.translate(-Math.sin(angle * Mth.DEG_TO_RAD) * entity.getBbWidth() * 0.45D, entity.getBbHeight() * -ratdata.getPosY(), -Math.cos(angle * Mth.DEG_TO_RAD) * entity.getBbWidth() * 0.45D);
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + angle));
			poseStack.rotateAround(Axis.XP.rotationDegrees(-90.0F), 0.0F, 1.5F, 0.0F);
			poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(animtime * 0.75F) * 10.0F));

			boolean isbaby = compound.getInt("Age") < 0;
			RatVariant type = entity.level().registryAccess().registryOrThrow(CCRegistries.RAT_VARIANT).get(new ResourceLocation(compound.getString("Variant")));
			DyeColor collarcolor = compound.contains("CollarColor", 99) ? DyeColor.byId(compound.getInt("CollarColor")) : DyeColor.RED;
			ItemStack heldstack = ItemStack.of(compound.getList("HandItems", 10).getCompound(0));
			VertexConsumer vertexconsumer = buffer.getBuffer(this.ratModel.renderType(type.texture().withPrefix("textures/").withSuffix(".png")));

			this.ratModel.young = isbaby;
			this.ratModel.renderWithoutMob(RatPose.ATTACHED, poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.0F, 0.0F, animtime, 0.0F, 0.0F);
			RatCollarLayer.renderCollar(this.ratModel, poseStack, buffer, packedLight, collarcolor, 0, 0);
			RatHeldItemLayer.renderItem(this.ratModel, this.itemInHandRenderer, poseStack, buffer, packedLight, entity, isbaby, heldstack, 0.0F, 0.0F);

			poseStack.popPose();
		}
	}
}