package com.teamabnormals.caverns_and_chasms.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperHeadModel;
import com.teamabnormals.caverns_and_chasms.common.block.entity.DeeperSkullBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.DeeperHat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DeeperSkullBlockRenderer extends SkullBlockRenderer {
	private static final ResourceLocation DEEPER_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/deeper/deeper.png");
	private DeeperHeadModel model;

	public DeeperSkullBlockRenderer(Context context) {
		super(context);
		this.model = new DeeperHeadModel(context.bakeLayer(CCModelLayers.DEEPER_HEAD));
	}

	@Override
	public void render(SkullBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int p_112539_) {
		float f = blockEntity.getAnimation(partialTick);
		BlockState blockstate = blockEntity.getBlockState();
		boolean flag = blockstate.getBlock() instanceof WallSkullBlock;
		Direction direction = flag ? blockstate.getValue(WallSkullBlock.FACING) : null;
		int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : blockstate.getValue(SkullBlock.ROTATION);
		float f1 = RotationSegment.convertToDegrees(i);
		renderDeeperSkull(direction, f1, f, poseStack, buffer, packedLight, ((DeeperSkullBlockEntity) blockEntity).getHat());
	}

	private void renderDeeperSkull(@Nullable Direction direction, float rot, float anim, PoseStack poseStack, MultiBufferSource buffer, int packedLight, DeeperHat hat) {
		poseStack.pushPose();
		if (direction == null) {
			poseStack.translate(0.5F, 0.0F, 0.5F);
		} else {
			float f = 0.25F;
			poseStack.translate(0.5F - (float)direction.getStepX() * 0.25F, 0.25F, 0.5F - (float)direction.getStepZ() * 0.25F);
		}

		poseStack.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(DEEPER_TEXTURE));
		this.model.setupAnim(anim, rot, 0.0F);
		this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (hat != DeeperHat.NONE) {
			vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(hat.getTexture()));
			this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
		poseStack.popPose();
	}
}