package com.minecraftabnormals.caverns_and_chasms.client.render.layer;

import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ParrotModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UndeadParrotLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>> {
   private final ParrotModel parrotModel = new ParrotModel();

   public UndeadParrotLayer(IEntityRenderer<T, PlayerModel<T>> rendererIn) {
      super(rendererIn);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
      this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
   }

   private void renderParrot(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, boolean leftShoulderIn) {
      CompoundNBT nbt = leftShoulderIn ? entity.getLeftShoulderEntity() : entity.getRightShoulderEntity();
      EntityType.byKey(nbt.getString("id")).filter((type) -> type == CCEntities.ZOMBIE_PARROT.get() || type == CCEntities.SKELETON_PARROT.get()).ifPresent((type) -> {
         matrixStackIn.push();
         matrixStackIn.translate(leftShoulderIn ? (double)0.4F : (double)-0.4F, entity.isCrouching() ? (double)-1.3F : -1.5D, 0.0D);
         IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.parrotModel.getRenderType(type == CCEntities.ZOMBIE_PARROT.get() ? ZombieParrotRenderer.TEXTURE : SkeletonParrotRenderer.TEXTURE));
         this.parrotModel.renderOnShoulder(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, limbSwing, limbSwingAmount, netHeadYaw, headPitch, entity.ticksExisted);
         matrixStackIn.pop();
      });
   }
}
